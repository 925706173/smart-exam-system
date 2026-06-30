package com.smartexam.module.ai.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartexam.common.exception.BizException;
import com.smartexam.common.result.ResultCode;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smartexam.module.ai.entity.AiGradingLog;
import com.smartexam.module.ai.mapper.AiGradingLogMapper;
import com.smartexam.module.exam.entity.ExamAnswer;
import com.smartexam.module.exam.entity.ExamPaperQuestion;
import com.smartexam.module.exam.entity.ExamRecord;
import com.smartexam.module.exam.mapper.ExamAnswerMapper;
import com.smartexam.module.exam.mapper.ExamPaperQuestionMapper;
import com.smartexam.module.exam.mapper.ExamRecordMapper;
import com.smartexam.module.question.entity.Question;
import com.smartexam.module.question.mapper.QuestionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * AI阅卷服务
 * <p>
 * 功能：
 * 1. 使用通义千问DashScope API进行主观题阅卷
 * 2. 结构化JSON Prompt设计
 * 3. 超时15s + 失败重试2次
 * 4. 超限自动降级为0分 + 标记需人工复核
 * 5. 解析返回JSON，校验score不超过满分
 * 6. 记录完整调用日志
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiGradingService {

    @Value("${ali.qwen.api-key}")
    private String apiKey;

    @Value("${ali.qwen.base-url}")
    private String baseUrl;

    @Value("${ali.qwen.model}")
    private String model;

    @Value("${ali.qwen.timeout}")
    private int timeout;

    @Value("${ali.qwen.max-retries}")
    private int maxRetries;

    private final ExamAnswerMapper examAnswerMapper;
    private final QuestionMapper questionMapper;
    private final AiGradingLogMapper aiGradingLogMapper;
    private final ObjectMapper objectMapper;
    private final ExamRecordMapper examRecordMapper;
    private final ExamPaperQuestionMapper examPaperQuestionMapper;

    /**
     * 系统提示词模板
     */
    private static final String SYSTEM_PROMPT = "你是一名严谨的大学专业课阅卷教师。请严格按照评分标准进行评分。";

    /**
     * 用户提示词模板
     */
    private static final String USER_PROMPT_TEMPLATE = """
            请根据【题目】、【参考答案/评分标准】与【学生作答】进行批改。

            【题目】：%s

            【参考答案/评分标准】：%s

            【学生作答】：%s

            请严格按照以下JSON格式返回，不要包含任何其他内容：
            {"score": 分数, "explanation": "评分理由", "confidence": 置信度}

            注意：
            1. score为0到%s之间的数字（可以是小数）
            2. confidence为0到1之间的数字，表示评分把握程度
            3. explanation需详细说明给分理由和扣分点
            """;

    /**
     * 异步执行AI阅卷
     *
     * @param answerId 答题记录ID
     * @return CompletableFuture
     */
    @Async("aiGradingExecutor")
    public CompletableFuture<Void> gradeSubjectiveAsync(Long answerId) {
        return CompletableFuture.runAsync(() -> {
            try {
                gradeSubjective(answerId);
            } catch (Exception e) {
                log.error("AI阅卷异步执行失败, answerId={}", answerId, e);
            }
        });
    }

    /**
     * 执行单题AI阅卷
     *
     * @param answerId 答题记录ID
     */
    public void gradeSubjective(Long answerId) {
        // 1. 获取答题记录
        ExamAnswer answer = examAnswerMapper.selectById(answerId);
        if (answer == null || answer.getUserAnswer() == null || answer.getUserAnswer().trim().isEmpty()) {
            log.warn("答题记录不存在或答案为空, answerId={}", answerId);
            return;
        }

        // 2. 获取题目信息
        Question question = questionMapper.selectById(answer.getQuestionId());
        if (question == null) {
            log.warn("题目不存在, questionId={}", answer.getQuestionId());
            return;
        }

        // 3. 获取试卷中设置的分值（而非题库中的原始分值）
        ExamRecord record = examRecordMapper.selectById(answer.getRecordId());
        BigDecimal questionScore = question.getScore(); // 默认使用题库分值
        if (record != null) {
            // 从试卷题目关联表中获取试卷中设置的分值
            ExamPaperQuestion pq = examPaperQuestionMapper.selectOne(
                    new LambdaQueryWrapper<ExamPaperQuestion>()
                            .eq(ExamPaperQuestion::getPaperId, record.getPaperId())
                            .eq(ExamPaperQuestion::getQuestionId, answer.getQuestionId())
            );
            if (pq != null && pq.getScore() != null) {
                questionScore = pq.getScore();
            }
        }

        // 4. 构建Prompt
        String prompt = String.format(USER_PROMPT_TEMPLATE,
                question.getTitle(),
                question.getAnswer() != null ? question.getAnswer() : "无参考答案",
                answer.getUserAnswer(),
                questionScore
        );

        // 4. 记录日志对象
        AiGradingLog gradingLog = new AiGradingLog();
        gradingLog.setAnswerId(answerId);
        gradingLog.setQuestionId(question.getId());
        gradingLog.setModelName(model);
        gradingLog.setPrompt(prompt);

        long startTime = System.currentTimeMillis();

        // 5. 调用API（含重试逻辑）
        String responseStr = null;
        int retryCount = 0;
        Exception lastException = null;

        while (retryCount <= maxRetries) {
            try {
                responseStr = callDashScopeApi(prompt);
                gradingLog.setStatus("SUCCESS");
                break;
            } catch (Exception e) {
                lastException = e;
                retryCount++;
                gradingLog.setRetryCount(retryCount);
                log.warn("AI调用失败, 重试第{}次, answerId={}", retryCount, answerId, e);

                if (retryCount > maxRetries) {
                    gradingLog.setStatus(e.getMessage().contains("timeout") ? "TIMEOUT" : "FAILED");
                    gradingLog.setErrorMsg(e.getMessage());
                }
            }
        }

        long duration = System.currentTimeMillis() - startTime;
        gradingLog.setDurationMs((int) duration);

        // 6. 处理结果
        if (responseStr != null) {
            gradingLog.setResponse(responseStr);
            try {
                parseAndUpdateScore(answer, question, responseStr, gradingLog);
            } catch (Exception e) {
                log.error("解析AI返回失败, answerId={}", answerId, e);
                gradingLog.setStatus("FAILED");
                gradingLog.setErrorMsg("返回解析失败: " + e.getMessage());
                // 降级处理
                fallbackToZero(answer, question, gradingLog);
            }
        } else {
            // 调用失败，降级处理
            fallbackToZero(answer, question, gradingLog);
        }

        // 7. 保存日志
        gradingLog.setCreateTime(LocalDateTime.now());
        aiGradingLogMapper.insert(gradingLog);
    }

    /**
     * 调用DashScope API（使用OpenAI兼容格式）
     */
    private String callDashScopeApi(String prompt) throws Exception {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(timeout))
                .build();

        // 构建请求体（OpenAI兼容格式）
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);

        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", SYSTEM_PROMPT);

        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);

        requestBody.put("messages", new Object[]{systemMessage, userMessage});
        requestBody.put("temperature", 0.1);
        requestBody.put("max_tokens", 500);

        String jsonBody = objectMapper.writeValueAsString(requestBody);

        // 使用OpenAI兼容端点
        String endpoint = "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .timeout(Duration.ofMillis(timeout))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("API调用失败, HTTP状态码: " + response.statusCode() + ", 响应: " + response.body());
        }

        // 解析响应获取文本内容
        JsonNode root = objectMapper.readTree(response.body());
        JsonNode choices = root.path("choices");

        if (choices.isArray() && choices.size() > 0) {
            return choices.get(0).path("message").path("content").asText();
        }

        throw new RuntimeException("API返回格式异常: " + response.body());
    }

    /**
     * 解析AI返回并更新分数
     */
    private void parseAndUpdateScore(ExamAnswer answer, Question question,
                                      String responseStr, AiGradingLog gradingLog) {
        try {
            // 提取JSON部分（AI可能返回包含其他文本）
            String jsonStr = extractJson(responseStr);
            JsonNode json = objectMapper.readTree(jsonStr);

            BigDecimal score = json.has("score") ?
                    new BigDecimal(json.get("score").asText()) : BigDecimal.ZERO;
            String explanation = json.has("explanation") ?
                    json.get("explanation").asText() : "";
            BigDecimal confidence = json.has("confidence") ?
                    new BigDecimal(json.get("confidence").asText()) : BigDecimal.ZERO;

            // 校验分数范围
            BigDecimal maxScore = question.getScore();
            if (score.compareTo(BigDecimal.ZERO) < 0) {
                score = BigDecimal.ZERO;
            }
            if (score.compareTo(maxScore) > 0) {
                log.warn("AI评分{}超过满分{}, 已截断", score, maxScore);
                score = maxScore;
            }

            // 更新答题记录
            answer.setAiScore(score.setScale(2, RoundingMode.HALF_UP));
            answer.setAiComment(explanation);
            answer.setFinalScore(answer.getAiScore());
            answer.setGradingStatus("AI_DONE");
            examAnswerMapper.updateById(answer);

            // 更新日志
            gradingLog.setAiScore(answer.getAiScore());
            gradingLog.setAiComment(explanation);
            gradingLog.setConfidence(confidence.setScale(2, RoundingMode.HALF_UP));

        } catch (Exception e) {
            throw new RuntimeException("JSON解析失败: " + e.getMessage());
        }
    }

    /**
     * 提取JSON字符串
     * 处理AI返回可能包含```json```标记的情况
     */
    private String extractJson(String text) {
        // 尝试直接解析
        text = text.trim();
        if (text.startsWith("{")) {
            return text;
        }
        // 提取```json```中的内容
        int start = text.indexOf("{");
        int end = text.lastIndexOf("}");
        if (start >= 0 && end > start) {
            return text.substring(start, end + 1);
        }
        throw new RuntimeException("无法提取JSON: " + text);
    }

    /**
     * 降级处理：0分 + 标记需人工复核
     */
    private void fallbackToZero(ExamAnswer answer, Question question, AiGradingLog gradingLog) {
        answer.setAiScore(BigDecimal.ZERO);
        answer.setAiComment("AI阅卷失败，需人工复核");
        answer.setGradingStatus("PENDING");
        examAnswerMapper.updateById(answer);

        gradingLog.setAiScore(BigDecimal.ZERO);
        gradingLog.setAiComment("降级处理");
        gradingLog.setStatus("FALLBACK");
    }
}

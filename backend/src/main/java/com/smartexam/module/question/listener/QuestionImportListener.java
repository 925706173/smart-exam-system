package com.smartexam.module.question.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.smartexam.module.question.dto.QuestionImportDTO;
import com.smartexam.module.question.entity.Question;
import com.smartexam.module.question.entity.QuestionOption;
import com.smartexam.module.question.mapper.QuestionMapper;
import com.smartexam.module.question.mapper.QuestionOptionMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 题目导入监听器
 * <p>
 * 功能：
 * 1. 逐行解析Excel
 * 2. 校验字段非空、枚举合法、难度范围
 * 3. 每500条批量插入MySQL
 * 4. 记录失败行号与原因
 */
@Slf4j
public class QuestionImportListener implements ReadListener<QuestionImportDTO> {

    private static final int BATCH_SIZE = 500;

    private final QuestionMapper questionMapper;
    private final QuestionOptionMapper questionOptionMapper;
    private final Long subjectId;
    private final Long createBy;

    /**
     * 导入结果
     */
    @Data
    public static class ImportResult {
        private int successCount = 0;
        private int failCount = 0;
        private List<ErrorDetail> errorDetails = new ArrayList<>();
    }

    @Data
    public static class ErrorDetail {
        private int rowNum;
        private String reason;

        public ErrorDetail(int rowNum, String reason) {
            this.rowNum = rowNum;
            this.reason = reason;
        }
    }

    /**
     * 缓存的数据列表
     */
    private List<QuestionImportDTO> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_SIZE);

    /**
     * 导入结果
     */
    private final ImportResult result = new ImportResult();

    /**
     * 当前行号
     */
    private int currentRow = 1;

    public QuestionImportListener(QuestionMapper questionMapper,
                                   QuestionOptionMapper questionOptionMapper,
                                   Long subjectId,
                                   Long createBy) {
        this.questionMapper = questionMapper;
        this.questionOptionMapper = questionOptionMapper;
        this.subjectId = subjectId;
        this.createBy = createBy;
    }

    public ImportResult getResult() {
        return result;
    }

    @Override
    public void invoke(QuestionImportDTO data, AnalysisContext context) {
        currentRow++;
        // 校验数据
        String error = validate(data);
        if (error != null) {
            result.setFailCount(result.getFailCount() + 1);
            result.getErrorDetails().add(new ErrorDetail(currentRow, error));
            return;
        }
        cachedDataList.add(data);
        // 达到BATCH_SIZE，存储一次
        if (cachedDataList.size() >= BATCH_SIZE) {
            saveData();
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_SIZE);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 存储剩余数据
        saveData();
        log.info("题目导入完成: 成功{}, 失败{}", result.getSuccessCount(), result.getFailCount());
    }

    /**
     * 校验数据合法性
     */
    private String validate(QuestionImportDTO data) {
        if (data.getTitle() == null || data.getTitle().trim().isEmpty()) {
            return "题干不能为空";
        }
        if (data.getType() == null || !isValidType(data.getType())) {
            return "题型不合法，应为: SINGLE/MULTI/JUDGE/FILL/SUBJECTIVE";
        }
        if (data.getDifficulty() == null || data.getDifficulty() < 1 || data.getDifficulty() > 5) {
            return "难度等级不合法，应为1-5";
        }
        if (data.getScore() == null || data.getScore() <= 0) {
            return "分值必须大于0";
        }
        if ("SINGLE".equals(data.getType()) || "MULTI".equals(data.getType())) {
            if (data.getOptions() == null || data.getOptions().trim().isEmpty()) {
                return "选择题必须提供选项";
            }
            if (data.getAnswer() == null || data.getAnswer().trim().isEmpty()) {
                return "选择题必须提供答案";
            }
        }
        return null;
    }

    /**
     * 将标签文本转为JSON数组格式
     * "基础" -> ["基础"], "基础,重点" -> ["基础","重点"]
     */
    private String convertTagsToJson(String tags) {
        if (tags == null || tags.trim().isEmpty()) {
            return null;
        }
        // 已经是JSON格式
        if (tags.trim().startsWith("[")) {
            return tags;
        }
        String[] parts = tags.split("[,，、]");
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) sb.append(",");
            sb.append("\"").append(parts[i].trim()).append("\"");
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * 校验题型是否合法
     */
    private boolean isValidType(String type) {
        return "SINGLE".equals(type) || "MULTI".equals(type) ||
                "JUDGE".equals(type) || "FILL".equals(type) || "SUBJECTIVE".equals(type);
    }

    /**
     * 批量保存数据
     */
    private void saveData() {
        List<Question> questions = new ArrayList<>();
        List<QuestionOption> allOptions = new ArrayList<>();

        for (QuestionImportDTO data : cachedDataList) {
            Question question = new Question();
            question.setTitle(data.getTitle().trim());
            question.setType(data.getType().trim().toUpperCase());
            question.setDifficulty(data.getDifficulty());
            question.setSubjectId(subjectId);
            question.setChapter(data.getChapter());
            question.setScore(BigDecimal.valueOf(data.getScore()));
            question.setAnswer(data.getAnswer());
            question.setExplanation(data.getExplanation());
            question.setTags(convertTagsToJson(data.getTags()));
            question.setStatus("PUBLISHED");
            question.setCreateBy(createBy);
            questions.add(question);
        }

        // 批量插入题目
        for (Question question : questions) {
            questionMapper.insert(question);
            result.setSuccessCount(result.getSuccessCount() + 1);

            // 解析选项
            if ("SINGLE".equals(question.getType()) || "MULTI".equals(question.getType())) {
                List<QuestionOption> options = parseOptions(question.getId(), cachedDataList.get(questions.indexOf(question)));
                allOptions.addAll(options);
            }
        }

        // 批量插入选项
        for (QuestionOption option : allOptions) {
            questionOptionMapper.insert(option);
        }
    }

    /**
     * 解析选项字符串
     * 格式: A.选项内容|B.选项内容|C.选项内容|D.选项内容
     */
    private List<QuestionOption> parseOptions(Long questionId, QuestionImportDTO data) {
        List<QuestionOption> options = new ArrayList<>();
        String optionsStr = data.getOptions();
        if (optionsStr == null || optionsStr.trim().isEmpty()) {
            return options;
        }

        String[] parts = optionsStr.split("\\|");
        int sortOrder = 0;
        for (String part : parts) {
            part = part.trim();
            if (part.isEmpty()) continue;

            // 解析选项标识和内容
            Pattern pattern = Pattern.compile("^([A-Za-z])[.．、\\s](.+)$");
            Matcher matcher = pattern.matcher(part);
            if (matcher.matches()) {
                String label = matcher.group(1).toUpperCase();
                String content = matcher.group(2).trim();

                QuestionOption option = new QuestionOption();
                option.setQuestionId(questionId);
                option.setOptionLabel(label);
                option.setOptionContent(content);
                option.setSortOrder(sortOrder++);

                // 判断是否为正确答案
                String answer = data.getAnswer() != null ? data.getAnswer().trim().toUpperCase() : "";
                if ("MULTI".equals(data.getType())) {
                    // 多选题答案格式: A,B,C
                    option.setIsCorrect(answer.contains(label) ? 1 : 0);
                } else {
                    // 单选题答案格式: A
                    option.setIsCorrect(label.equals(answer) ? 1 : 0);
                }

                options.add(option);
            }
        }
        return options;
    }
}

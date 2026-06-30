package com.smartexam.module.exam.algorithm;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartexam.module.exam.entity.ExamRule;
import com.smartexam.module.question.entity.Question;
import com.smartexam.module.question.entity.QuestionOption;
import com.smartexam.module.question.mapper.QuestionMapper;
import com.smartexam.module.question.mapper.QuestionOptionMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 千人千卷抽题算法
 * <p>
 * 核心逻辑：
 * 1. 按规则查询3倍候选题ID
 * 2. Java Collections.shuffle() 截取目标数
 * 3. 题目整体乱序
 * 4. 选项 Fisher-Yates 乱序并记录 originCorrect 映射
 * 5. 杜绝 ORDER BY RAND()
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class QuestionSelector {

    private final QuestionMapper questionMapper;
    private final QuestionOptionMapper questionOptionMapper;
    private final ObjectMapper objectMapper;

    /**
     * 规则配置项
     */
    @Data
    public static class RuleItem {
        private String chapter;
        private String type;
        private Integer difficulty;
        private Integer count;
        private Double score;
    }

    /**
     * 抽题结果
     */
    @Data
    public static class SelectResult {
        private List<Question> questions;
        private Map<Long, List<QuestionOption>> optionMap;
        private Map<Long, Map<String, String>> originCorrectMap;
        private Map<Long, Double> scoreMap;
    }

    /**
     * 根据规则抽取题目
     *
     * @param ruleConfig 规则JSON配置
     * @param subjectId  科目ID
     * @param seed       随机种子（用于千人千卷）
     * @return 抽题结果
     */
    public SelectResult selectByRules(String ruleConfig, Long subjectId, Long seed) {
        try {
            List<RuleItem> rules = objectMapper.readValue(ruleConfig, new TypeReference<>() {});
            Random random = new Random(seed);

            List<Question> allQuestions = new ArrayList<>();
            Map<Long, List<QuestionOption>> allOptions = new HashMap<>();
            Map<Long, Double> scoreMap = new HashMap<>();

            for (RuleItem rule : rules) {
                // 1. 查询3倍候选题
                List<Question> candidates = questionMapper.selectByRule(
                        subjectId, rule.getChapter(), rule.getType(), rule.getDifficulty()
                );

                if (candidates.isEmpty()) {
                    log.warn("规则匹配题目为空: {}", rule);
                    continue;
                }

                // 2. 候选题不足时取实际数量
                int selectCount = Math.min(rule.getCount(), candidates.size());

                // 3. Java端随机抽取（杜绝 ORDER BY RAND()）
                Collections.shuffle(candidates, random);
                List<Question> selected = candidates.subList(0, selectCount);

                // 记录分值
                for (Question q : selected) {
                    scoreMap.put(q.getId(), rule.getScore());
                }

                allQuestions.addAll(selected);
            }

            // 4. 题目整体乱序
            Collections.shuffle(allQuestions, random);

            // 5. 选项乱序（Fisher-Yates）
            Map<Long, Map<String, String>> originCorrectMap = new HashMap<>();
            List<Long> questionIds = allQuestions.stream()
                    .map(Question::getId)
                    .collect(Collectors.toList());

            if (!questionIds.isEmpty()) {
                List<QuestionOption> options = questionOptionMapper.selectList(
                        new LambdaQueryWrapper<QuestionOption>()
                                .in(QuestionOption::getQuestionId, questionIds)
                                .orderByAsc(QuestionOption::getSortOrder)
                );

                // 按题目ID分组
                Map<Long, List<QuestionOption>> optionGroupMap = options.stream()
                        .collect(Collectors.groupingBy(QuestionOption::getQuestionId));

                // 对每个题目的选项进行Fisher-Yates乱序
                for (Map.Entry<Long, List<QuestionOption>> entry : optionGroupMap.entrySet()) {
                    Long questionId = entry.getKey();
                    List<QuestionOption> questionOptions = entry.getValue();

                    // 记录原始正确答案标识
                    String originalCorrect = questionOptions.stream()
                            .filter(opt -> opt.getIsCorrect() == 1)
                            .map(QuestionOption::getOptionLabel)
                            .collect(Collectors.joining(","));

                    // Fisher-Yates 洗牌算法
                    shuffleOptions(questionOptions, random);

                    // 构建映射：乱序后的标识 -> 原始标识
                    Map<String, String> correctMapping = new HashMap<>();
                    for (int i = 0; i < questionOptions.size(); i++) {
                        QuestionOption opt = questionOptions.get(i);
                        String newLabel = String.valueOf((char) ('A' + i));
                        if (opt.getIsCorrect() == 1) {
                            correctMapping.put(newLabel, opt.getOptionLabel());
                        }
                        opt.setOptionLabel(newLabel);
                        opt.setSortOrder(i);
                    }

                    allOptions.put(questionId, questionOptions);
                    originCorrectMap.put(questionId, correctMapping);
                }
            }

            SelectResult result = new SelectResult();
            result.setQuestions(allQuestions);
            result.setOptionMap(allOptions);
            result.setOriginCorrectMap(originCorrectMap);
            result.setScoreMap(scoreMap);
            return result;

        } catch (Exception e) {
            log.error("抽题算法执行失败", e);
            throw new RuntimeException("抽题失败: " + e.getMessage());
        }
    }

    /**
     * Fisher-Yates 洗牌算法
     * 时间复杂度 O(n)，保证均匀随机
     */
    private void shuffleOptions(List<QuestionOption> options, Random random) {
        for (int i = options.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            Collections.swap(options, i, j);
        }
    }
}

package com.smartexam.module.stats.service;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smartexam.module.exam.entity.ExamPaper;
import com.smartexam.module.exam.entity.ExamRecord;
import com.smartexam.module.exam.mapper.ExamPaperMapper;
import com.smartexam.module.exam.mapper.ExamRecordMapper;
import com.smartexam.module.stats.vo.PaperReportVO;
import com.smartexam.module.user.entity.SysUser;
import com.smartexam.module.user.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 统计服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StatsService {

    private final ExamRecordMapper recordMapper;
    private final ExamPaperMapper paperMapper;
    private final SysUserMapper userMapper;

    /**
     * 获取试卷成绩报告
     */
    public PaperReportVO getPaperReport(Long paperId) {
        // 查询试卷信息
        ExamPaper paper = paperMapper.selectById(paperId);
        if (paper == null) {
            return new PaperReportVO();
        }

        // 查询已出分的考试记录
        List<ExamRecord> records = recordMapper.selectList(
                new LambdaQueryWrapper<ExamRecord>()
                        .eq(ExamRecord::getPaperId, paperId)
                        .eq(ExamRecord::getStatus, "GRADED")
                        .isNotNull(ExamRecord::getTotalScore)
        );

        PaperReportVO report = new PaperReportVO();
        report.setPaperId(paperId);
        report.setPaperTitle(paper.getTitle());
        report.setTotalScore(paper.getTotalScore());
        report.setPassScore(paper.getPassScore());
        report.setTotalCount(records.size());

        if (records.isEmpty()) {
            return report;
        }

        // 提取分数列表
        List<BigDecimal> scores = records.stream()
                .map(ExamRecord::getTotalScore)
                .sorted()
                .toList();

        // 计算及格人数
        int passCount = (int) scores.stream()
                .filter(s -> s.compareTo(paper.getPassScore()) >= 0)
                .count();

        report.setPassCount(passCount);
        report.setFailCount(records.size() - passCount);
        report.setPassRate(new BigDecimal(passCount)
                .divide(new BigDecimal(records.size()), 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100))
                .setScale(2, RoundingMode.HALF_UP));

        // 计算平均分、最高分、最低分
        BigDecimal sum = scores.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        report.setAvgScore(sum.divide(new BigDecimal(records.size()), 2, RoundingMode.HALF_UP));
        report.setMaxScore(scores.get(scores.size() - 1));
        report.setMinScore(scores.get(0));

        // 计算分数段分布
        List<Map<String, Object>> distribution = new ArrayList<>();
        int[] ranges = {0, 60, 70, 80, 90, 101};
        String[] labels = {"0-59", "60-69", "70-79", "80-89", "90-100"};

        for (int i = 0; i < labels.length; i++) {
            final int min = ranges[i];
            final int max = ranges[i + 1];
            final boolean isLast = (i == labels.length - 1);

            long count = scores.stream()
                    .filter(s -> {
                        int score = s.intValue();
                        if (isLast) {
                            return score >= min && score <= max;
                        }
                        return score >= min && score < max;
                    })
                    .count();

            Map<String, Object> item = new HashMap<>();
            item.put("label", labels[i]);
            item.put("count", count);
            distribution.add(item);
        }

        report.setScoreDistribution(distribution);
        return report;
    }

    /**
     * 获取成绩排名
     */
    public List<Map<String, Object>> getPaperRank(Long paperId) {
        List<ExamRecord> records = recordMapper.selectList(
                new LambdaQueryWrapper<ExamRecord>()
                        .eq(ExamRecord::getPaperId, paperId)
                        .eq(ExamRecord::getStatus, "GRADED")
                        .isNotNull(ExamRecord::getTotalScore)
                        .orderByDesc(ExamRecord::getTotalScore)
        );

        List<Map<String, Object>> rankList = new ArrayList<>();
        int rank = 1;
        for (ExamRecord record : records) {
            SysUser user = userMapper.selectById(record.getUserId());
            Map<String, Object> item = new HashMap<>();
            item.put("rank", rank++);
            item.put("userId", record.getUserId());
            item.put("realName", user != null ? user.getRealName() : "未知");
            item.put("totalScore", record.getTotalScore());
            item.put("submitTime", record.getSubmitTime());
            rankList.add(item);
        }

        return rankList;
    }

    /**
     * 获取学生历史成绩
     */
    public List<Map<String, Object>> getStudentHistory() {
        Long userId = StpUtil.getLoginIdAsLong();

        List<ExamRecord> records = recordMapper.selectList(
                new LambdaQueryWrapper<ExamRecord>()
                        .eq(ExamRecord::getUserId, userId)
                        .eq(ExamRecord::getStatus, "GRADED")
                        .orderByDesc(ExamRecord::getSubmitTime)
        );

        List<Map<String, Object>> historyList = new ArrayList<>();
        for (ExamRecord record : records) {
            // 检查试卷是否已被删除（逻辑删除的试卷selectById会返回null）
            ExamPaper paper = paperMapper.selectById(record.getPaperId());
            if (paper == null) {
                // 试卷已被删除，跳过该记录
                continue;
            }
            Map<String, Object> item = new HashMap<>();
            item.put("recordId", record.getId());
            item.put("paperId", record.getPaperId());
            item.put("paperTitle", paper.getTitle());
            item.put("totalScore", record.getTotalScore());
            item.put("submitTime", record.getSubmitTime());
            historyList.add(item);
        }

        return historyList;
    }
}

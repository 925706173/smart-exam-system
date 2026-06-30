package com.smartexam.module.exam.service;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartexam.module.exam.entity.ErrorBook;
import com.smartexam.module.exam.mapper.ErrorBookMapper;
import com.smartexam.module.exam.vo.PracticeCheckVO;
import com.smartexam.module.question.entity.Question;
import com.smartexam.module.question.entity.QuestionOption;
import com.smartexam.module.question.mapper.QuestionMapper;
import com.smartexam.module.question.mapper.QuestionOptionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 练习服务
 */
@Service
@RequiredArgsConstructor
public class PracticeService {

    private final QuestionMapper questionMapper;
    private final QuestionOptionMapper questionOptionMapper;
    private final ErrorBookMapper errorBookMapper;

    /**
     * 单题即时判分
     */
    public PracticeCheckVO checkAnswer(Long questionId, String answer) {
        Question question = questionMapper.selectById(questionId);
        if (question == null) {
            PracticeCheckVO vo = new PracticeCheckVO();
            vo.setIsCorrect(false);
            vo.setCorrectAnswer("题目不存在");
            vo.setExplanation("");
            return vo;
        }

        // 获取正确答案
        String correctAnswer = question.getAnswer();
        boolean isCorrect = false;

        if ("SINGLE".equals(question.getType()) || "JUDGE".equals(question.getType())) {
            isCorrect = correctAnswer != null && correctAnswer.equalsIgnoreCase(answer.trim());
        } else if ("MULTI".equals(question.getType())) {
            // 多选题需要排序后比较
            String sortedAnswer = sortAnswer(answer);
            String sortedCorrect = sortAnswer(correctAnswer);
            isCorrect = sortedCorrect != null && sortedCorrect.equals(sortedAnswer);
        } else if ("FILL".equals(question.getType())) {
            isCorrect = correctAnswer != null && correctAnswer.trim().equals(answer.trim());
        }

        // 如果答错，加入错题本
        if (!isCorrect) {
            addToErrorBook(questionId, answer, correctAnswer);
        }

        PracticeCheckVO vo = new PracticeCheckVO();
        vo.setIsCorrect(isCorrect);
        vo.setCorrectAnswer(correctAnswer);
        vo.setExplanation(question.getExplanation());
        return vo;
    }

    /**
     * 排序答案（用于多选题比较）
     */
    private String sortAnswer(String answer) {
        if (answer == null || answer.isEmpty()) {
            return "";
        }
        return answer.chars()
                .sorted()
                .mapToObj(c -> String.valueOf((char) c))
                .collect(Collectors.joining());
    }

    /**
     * 加入错题本
     */
    private void addToErrorBook(Long questionId, String userAnswer, String correctAnswer) {
        Long userId = StpUtil.getLoginIdAsLong();

        ErrorBook existBook = errorBookMapper.selectOne(
                new LambdaQueryWrapper<ErrorBook>()
                        .eq(ErrorBook::getUserId, userId)
                        .eq(ErrorBook::getQuestionId, questionId)
        );

        if (existBook != null) {
            existBook.setErrorCount(existBook.getErrorCount() + 1);
            existBook.setLastErrorTime(LocalDateTime.now());
            existBook.setUserAnswer(userAnswer);
            existBook.setMastered(0);
            errorBookMapper.updateById(existBook);
        } else {
            ErrorBook book = new ErrorBook();
            book.setUserId(userId);
            book.setQuestionId(questionId);
            book.setRecordId(0L); // 练习模式无考试记录
            book.setUserAnswer(userAnswer);
            book.setCorrectAnswer(correctAnswer);
            book.setErrorCount(1);
            book.setLastErrorTime(LocalDateTime.now());
            book.setMastered(0);
            errorBookMapper.insert(book);
        }
    }

    /**
     * 获取错题本
     */
    public Page<ErrorBook> getErrorBook(int pageNum, int pageSize) {
        Long userId = StpUtil.getLoginIdAsLong();
        Page<ErrorBook> page = new Page<>(pageNum, pageSize);
        return errorBookMapper.selectPage(page,
                new LambdaQueryWrapper<ErrorBook>()
                        .eq(ErrorBook::getUserId, userId)
                        .eq(ErrorBook::getMastered, 0)
                        .orderByDesc(ErrorBook::getLastErrorTime)
        );
    }

    /**
     * 错题复习
     */
    public void reviewError(Long id) {
        ErrorBook book = errorBookMapper.selectById(id);
        if (book != null) {
            book.setReviewCount(book.getReviewCount() + 1);
            book.setLastReviewTime(LocalDateTime.now());
            errorBookMapper.updateById(book);
        }
    }
}

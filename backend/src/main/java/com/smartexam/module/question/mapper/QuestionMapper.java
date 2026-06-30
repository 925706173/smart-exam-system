package com.smartexam.module.question.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartexam.module.question.entity.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 题目Mapper
 */
@Mapper
public interface QuestionMapper extends BaseMapper<Question> {

    /**
     * 根据规则查询候选题目(用于抽题算法)
     * @param subjectId 科目ID
     * @param chapter 章节
     * @param type 题型
     * @param difficulty 难度
     * @return 候选题目列表
     */
    @Select("<script>" +
            "SELECT q.* FROM question q " +
            "LEFT JOIN exam_paper_question epq ON q.id = epq.question_id " +
            "WHERE q.status = 'PUBLISHED' " +
            "AND epq.id IS NULL " +
            "<if test='subjectId != null'>AND q.subject_id = #{subjectId}</if> " +
            "<if test='chapter != null and chapter != \"\"'>AND q.chapter = #{chapter}</if> " +
            "<if test='type != null and type != \"\"'>AND q.type = #{type}</if> " +
            "<if test='difficulty != null'>AND q.difficulty = #{difficulty}</if>" +
            "</script>")
    List<Question> selectByRule(@Param("subjectId") Long subjectId,
                                @Param("chapter") String chapter,
                                @Param("type") String type,
                                @Param("difficulty") Integer difficulty);
}

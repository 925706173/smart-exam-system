package com.smartexam.module.exam.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("exam_paper_class")
@Schema(description = "试卷班级关联实体")
public class ExamPaperClass {

    @TableId(type = IdType.AUTO)
    @Schema(description = "ID")
    private Long id;

    @Schema(description = "试卷ID")
    private Long paperId;

    @Schema(description = "班级ID")
    private Long classId;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}

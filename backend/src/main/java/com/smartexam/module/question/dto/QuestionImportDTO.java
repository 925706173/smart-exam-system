package com.smartexam.module.question.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * 题目导入DTO
 */
@Data
public class QuestionImportDTO {

    @ExcelProperty("题干")
    @ColumnWidth(50)
    private String title;

    @ExcelProperty("题型")
    @ColumnWidth(15)
    private String type;

    @ExcelProperty("难度")
    @ColumnWidth(10)
    private Integer difficulty;

    @ExcelProperty("章节")
    @ColumnWidth(20)
    private String chapter;

    @ExcelProperty("分值")
    @ColumnWidth(10)
    private Double score;

    @ExcelProperty("选项")
    @ColumnWidth(60)
    private String options;

    @ExcelProperty("答案")
    @ColumnWidth(20)
    private String answer;

    @ExcelProperty("解析")
    @ColumnWidth(50)
    private String explanation;

    @ExcelProperty("标签")
    @ColumnWidth(30)
    private String tags;
}

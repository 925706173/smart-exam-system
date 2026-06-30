package com.smartexam;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 智能在线考试系统启动类
 */
@SpringBootApplication
@MapperScan({"com.smartexam.module.user.mapper", "com.smartexam.module.question.mapper", "com.smartexam.module.exam.mapper", "com.smartexam.module.ai.mapper", "com.smartexam.module.dict.mapper", "com.smartexam.module.stats.mapper", "com.smartexam.module.grading.mapper", "com.smartexam.module.class_.mapper"})
@EnableAsync
public class SmartExamApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartExamApplication.class, args);
    }
}

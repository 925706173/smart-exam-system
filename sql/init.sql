-- ============================================================
-- 智能在线考试系统 - 数据库初始化脚本
-- 数据库: MySQL 8.0+
-- 字符集: utf8mb4
-- 排序规则: utf8mb4_general_ci
-- ============================================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS smart_exam
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_general_ci;

USE smart_exam;

-- ============================================================
-- 1. 用户表 sys_user
-- 说明: 存储学生、教师、管理员三类用户
-- ============================================================
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    id              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    username        VARCHAR(50)     NOT NULL COMMENT '用户名(学号/工号)',
    password        VARCHAR(100)    NOT NULL COMMENT '密码(BCrypt加密)',
    real_name       VARCHAR(30)     NOT NULL COMMENT '真实姓名',
    role            VARCHAR(20)     NOT NULL COMMENT '角色: STUDENT-学生, TEACHER-教师, ADMIN-管理员',
    gender          TINYINT         DEFAULT 0 COMMENT '性别: 0-未知, 1-男, 2-女',
    email           VARCHAR(100)    DEFAULT NULL COMMENT '邮箱',
    phone           VARCHAR(20)     DEFAULT NULL COMMENT '手机号',
    avatar          VARCHAR(255)    DEFAULT NULL COMMENT '头像URL',
    class_id        BIGINT          DEFAULT NULL COMMENT '班级ID(关联sys_dict)',
    major_id        BIGINT          DEFAULT NULL COMMENT '专业ID(关联sys_dict)',
    status          TINYINT         DEFAULT 1 COMMENT '状态: 0-冻结, 1-正常',
    last_login_time DATETIME        DEFAULT NULL COMMENT '最后登录时间',
    last_login_ip   VARCHAR(50)     DEFAULT NULL COMMENT '最后登录IP',
    create_time     DATETIME        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted         TINYINT         DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username),
    KEY idx_role (role),
    KEY idx_class_id (class_id),
    KEY idx_major_id (major_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ============================================================
-- 2. 字典表 sys_dict
-- 说明: 存储班级、专业、科目、学期等基础字典数据
-- ============================================================
DROP TABLE IF EXISTS sys_dict;
CREATE TABLE sys_dict (
    id          BIGINT          NOT NULL AUTO_INCREMENT COMMENT '字典ID',
    type        VARCHAR(50)     NOT NULL COMMENT '字典类型: CLASS-班级, MAJOR-专业, SUBJECT-科目, SEMESTER-学期',
    code        VARCHAR(50)     NOT NULL COMMENT '字典编码',
    name        VARCHAR(100)    NOT NULL COMMENT '字典名称',
    sort_order  INT             DEFAULT 0 COMMENT '排序号',
    status      TINYINT         DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    remark      VARCHAR(255)    DEFAULT NULL COMMENT '备注',
    create_time DATETIME        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted     TINYINT         DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_type_code (type, code),
    KEY idx_type (type),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典表';

-- ============================================================
-- 3. 题库表 question
-- 说明: 存储所有题目，支持单选、多选、判断、填空、主观题
-- ============================================================
DROP TABLE IF EXISTS question;
CREATE TABLE question (
    id              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '题目ID',
    title           TEXT            NOT NULL COMMENT '题干内容(支持HTML富文本)',
    type            VARCHAR(20)     NOT NULL COMMENT '题型: SINGLE-单选, MULTI-多选, JUDGE-判断, FILL-填空, SUBJECTIVE-主观',
    difficulty      TINYINT         NOT NULL DEFAULT 3 COMMENT '难度等级: 1-简单, 2-较易, 3-中等, 4-较难, 5-困难',
    subject_id      BIGINT          DEFAULT NULL COMMENT '科目ID(关联sys_dict)',
    chapter         VARCHAR(100)    DEFAULT NULL COMMENT '章节',
    score           DECIMAL(5,2)    NOT NULL DEFAULT 0 COMMENT '默认分值',
    answer          TEXT            DEFAULT NULL COMMENT '参考答案(客观题为选项标识，主观题为详细答案)',
    explanation     TEXT            DEFAULT NULL COMMENT '答案解析',
    tags            JSON            DEFAULT NULL COMMENT '标签数组: ["重点","常考","易错"]',
    status          VARCHAR(20)     DEFAULT 'DRAFT' COMMENT '状态: DRAFT-草稿, PUBLISHED-已发布, DISABLED-已禁用',
    create_by       BIGINT          DEFAULT NULL COMMENT '创建人ID',
    update_by       BIGINT          DEFAULT NULL COMMENT '更新人ID',
    create_time     DATETIME        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted         TINYINT         DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (id),
    KEY idx_type (type),
    KEY idx_difficulty (difficulty),
    KEY idx_subject_id (subject_id),
    KEY idx_chapter (chapter),
    KEY idx_status (status),
    KEY idx_create_by (create_by)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='题库表';

-- ============================================================
-- 4. 选项表 question_option
-- 说明: 存储单选题、多选题、判断题的选项
-- ============================================================
DROP TABLE IF EXISTS question_option;
CREATE TABLE question_option (
    id              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '选项ID',
    question_id     BIGINT          NOT NULL COMMENT '题目ID',
    option_label    VARCHAR(10)     NOT NULL COMMENT '选项标识: A, B, C, D...',
    option_content  TEXT            NOT NULL COMMENT '选项内容',
    is_correct      TINYINT         DEFAULT 0 COMMENT '是否正确答案: 0-否, 1-是',
    sort_order      INT             DEFAULT 0 COMMENT '排序号',
    create_time     DATETIME        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_question_id (question_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='选项表';

-- ============================================================
-- 5. 试卷表 exam_paper
-- 说明: 存储试卷基本信息
-- ============================================================
DROP TABLE IF EXISTS exam_paper;
CREATE TABLE exam_paper (
    id              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '试卷ID',
    title           VARCHAR(200)    NOT NULL COMMENT '试卷标题',
    description     TEXT            DEFAULT NULL COMMENT '试卷说明',
    subject_id      BIGINT          DEFAULT NULL COMMENT '科目ID(关联sys_dict)',
    subject_name    VARCHAR(100)    DEFAULT NULL COMMENT '科目名称',
    semester_id     BIGINT          DEFAULT NULL COMMENT '学期ID(关联sys_dict)',
    total_score     DECIMAL(5,2)    NOT NULL DEFAULT 0 COMMENT '试卷总分',
    pass_score      DECIMAL(5,2)    NOT NULL DEFAULT 60 COMMENT '及格分数',
    duration        INT             NOT NULL DEFAULT 120 COMMENT '考试时长(分钟)',
    start_time      DATETIME        DEFAULT NULL COMMENT '开始时间',
    end_time        DATETIME        DEFAULT NULL COMMENT '结束时间',
    shuffle_question TINYINT        DEFAULT 1 COMMENT '是否乱序题目: 0-否, 1-是',
    shuffle_option  TINYINT         DEFAULT 1 COMMENT '是否乱序选项: 0-否, 1-是',
    show_answer     TINYINT         DEFAULT 0 COMMENT '交卷后是否显示答案: 0-否, 1-是',
    max_attempts    INT             DEFAULT 1 COMMENT '最大考试次数',
    status          VARCHAR(20)     DEFAULT 'DRAFT' COMMENT '状态: DRAFT-草稿, PUBLISHED-已发布, CLOSED-已结束',
    type            VARCHAR(20)     DEFAULT 'EXAM' COMMENT '类型: EXAM-考试, PRACTICE-练习',
    create_by       BIGINT          DEFAULT NULL COMMENT '创建人ID',
    create_time     DATETIME        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted         TINYINT         DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (id),
    KEY idx_subject_id (subject_id),
    KEY idx_semester_id (semester_id),
    KEY idx_status (status),
    KEY idx_type (type),
    KEY idx_create_by (create_by),
    KEY idx_start_time (start_time),
    KEY idx_end_time (end_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='试卷表';

-- ============================================================
-- 6. 组卷规则表 exam_rule
-- 说明: 存储自动组卷的规则配置(JSON格式)
-- ============================================================
DROP TABLE IF EXISTS exam_rule;
CREATE TABLE exam_rule (
    id              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '规则ID',
    paper_id        BIGINT          NOT NULL COMMENT '试卷ID',
    rule_config     JSON            NOT NULL COMMENT '规则配置JSON: [{"chapter":"Ch1","type":"SINGLE","difficulty":2,"count":5,"score":2}]',
    create_time     DATETIME        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_paper_id (paper_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='组卷规则表';

-- ============================================================
-- 7. 试卷题目关联表 exam_paper_question
-- 说明: 存储试卷与题目的关联关系
-- ============================================================
DROP TABLE IF EXISTS exam_paper_question;
CREATE TABLE exam_paper_question (
    id              BIGINT          NOT NULL AUTO_INCREMENT COMMENT 'ID',
    paper_id        BIGINT          NOT NULL COMMENT '试卷ID',
    question_id     BIGINT          NOT NULL COMMENT '题目ID',
    score           DECIMAL(5,2)    NOT NULL COMMENT '该题分值',
    sort_order      INT             DEFAULT 0 COMMENT '题目排序',
    create_time     DATETIME        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_paper_question (paper_id, question_id),
    KEY idx_paper_id (paper_id),
    KEY idx_question_id (question_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='试卷题目关联表';

-- ============================================================
-- 8. 考试记录表 exam_record
-- 说明: 记录学生每次考试的状态和结果
-- ============================================================
DROP TABLE IF EXISTS exam_record;
CREATE TABLE exam_record (
    id              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    paper_id        BIGINT          NOT NULL COMMENT '试卷ID',
    user_id         BIGINT          NOT NULL COMMENT '学生ID',
    attempt_no      INT             DEFAULT 1 COMMENT '第几次考试',
    status          VARCHAR(20)     DEFAULT 'NOT_STARTED' COMMENT '状态: NOT_STARTED-未开始, IN_PROGRESS-进行中, SUBMITTED-已提交, GRADING-阅卷中, GRADED-已出分',
    total_score     DECIMAL(5,2)    DEFAULT NULL COMMENT '总得分',
    start_time      DATETIME        DEFAULT NULL COMMENT '开始时间',
    end_time        DATETIME        DEFAULT NULL COMMENT '结束时间',
    submit_time     DATETIME        DEFAULT NULL COMMENT '提交时间',
    shuffle_seed    BIGINT          DEFAULT NULL COMMENT '乱序种子(用于选项还原)',
    question_order  JSON            DEFAULT NULL COMMENT '题目顺序: [3,1,5,2,4]',
    option_order    JSON            DEFAULT NULL COMMENT '选项顺序: {"1":["B","A","D","C"],"2":["C","A","B","D"]}',
    ip_address      VARCHAR(50)     DEFAULT NULL COMMENT '考试IP地址',
    create_time     DATETIME        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    KEY idx_paper_id (paper_id),
    KEY idx_user_id (user_id),
    KEY idx_status (status),
    KEY idx_submit_time (submit_time),
    UNIQUE KEY uk_paper_user_attempt (paper_id, user_id, attempt_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='考试记录表';

-- ============================================================
-- 9. 答题记录表 exam_answer
-- 说明: 记录学生每道题的作答情况
-- ============================================================
DROP TABLE IF EXISTS exam_answer;
CREATE TABLE exam_answer (
    id              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '答题ID',
    record_id       BIGINT          NOT NULL COMMENT '考试记录ID',
    question_id     BIGINT          NOT NULL COMMENT '题目ID',
    user_answer     TEXT            DEFAULT NULL COMMENT '学生答案(客观题为选项标识，主观题为文字)',
    score           DECIMAL(5,2)    DEFAULT NULL COMMENT '得分',
    is_correct      TINYINT         DEFAULT NULL COMMENT '是否正确: 0-错误, 1-正确, NULL-未判',
    ai_score        DECIMAL(5,2)    DEFAULT NULL COMMENT 'AI评分(仅主观题)',
    ai_comment      TEXT            DEFAULT NULL COMMENT 'AI评语(仅主观题)',
    final_score     DECIMAL(5,2)    DEFAULT NULL COMMENT '最终分数(教师可覆盖AI分)',
    grading_status  VARCHAR(20)     DEFAULT 'PENDING' COMMENT '阅卷状态: PENDING-待阅, AI_DONE-AI已阅, TEACHER_DONE-教师已阅',
    answer_time     INT             DEFAULT NULL COMMENT '答题耗时(秒)',
    create_time     DATETIME        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    KEY idx_record_id (record_id),
    KEY idx_question_id (question_id),
    KEY idx_grading_status (grading_status),
    UNIQUE KEY uk_record_question (record_id, question_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='答题记录表';

-- ============================================================
-- 10. AI阅卷日志表 ai_grading_log
-- 说明: 记录AI阅卷的完整调用日志
-- ============================================================
DROP TABLE IF EXISTS ai_grading_log;
CREATE TABLE ai_grading_log (
    id              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    answer_id       BIGINT          NOT NULL COMMENT '答题记录ID',
    question_id     BIGINT          NOT NULL COMMENT '题目ID',
    model_name      VARCHAR(50)     DEFAULT NULL COMMENT '模型名称: qwen-turbo, qwen-plus',
    prompt          TEXT            NOT NULL COMMENT '发送的Prompt',
    response        TEXT            DEFAULT NULL COMMENT 'AI返回结果',
    ai_score        DECIMAL(5,2)    DEFAULT NULL COMMENT 'AI评分',
    ai_comment      TEXT            DEFAULT NULL COMMENT 'AI评语',
    confidence      DECIMAL(3,2)    DEFAULT NULL COMMENT '置信度(0-1)',
    token_used      INT             DEFAULT NULL COMMENT 'Token消耗量',
    duration_ms     INT             DEFAULT NULL COMMENT '调用耗时(毫秒)',
    status          VARCHAR(20)     DEFAULT 'SUCCESS' COMMENT '状态: SUCCESS-成功, FAILED-失败, TIMEOUT-超时, FALLBACK-降级',
    error_msg       TEXT            DEFAULT NULL COMMENT '错误信息',
    retry_count     INT             DEFAULT 0 COMMENT '重试次数',
    create_time     DATETIME        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_answer_id (answer_id),
    KEY idx_question_id (question_id),
    KEY idx_status (status),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI阅卷日志表';

-- ============================================================
-- 11. 错题本表 error_book
-- 说明: 记录学生的错题，支持复习
-- ============================================================
DROP TABLE IF EXISTS error_book;
CREATE TABLE error_book (
    id              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '错题ID',
    user_id         BIGINT          NOT NULL COMMENT '学生ID',
    question_id     BIGINT          NOT NULL COMMENT '题目ID',
    record_id       BIGINT          NOT NULL COMMENT '考试记录ID',
    user_answer     TEXT            DEFAULT NULL COMMENT '学生作答',
    correct_answer  TEXT            DEFAULT NULL COMMENT '正确答案',
    error_count     INT             DEFAULT 1 COMMENT '错误次数',
    last_error_time DATETIME        DEFAULT NULL COMMENT '最近错误时间',
    review_count    INT             DEFAULT 0 COMMENT '复习次数',
    last_review_time DATETIME       DEFAULT NULL COMMENT '最近复习时间',
    mastered        TINYINT         DEFAULT 0 COMMENT '是否已掌握: 0-未掌握, 1-已掌握',
    create_time     DATETIME        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    KEY idx_user_id (user_id),
    KEY idx_question_id (question_id),
    KEY idx_mastered (mastered),
    UNIQUE KEY uk_user_question (user_id, question_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='错题本表';

-- ============================================================
-- 12. 班级表 sys_class
-- 说明: 管理员创建的班级，用于组织学生和分配试卷
-- ============================================================
DROP TABLE IF EXISTS sys_class;
CREATE TABLE sys_class (
    id              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '班级ID',
    name            VARCHAR(100)    NOT NULL COMMENT '班级名称',
    description     VARCHAR(255)    DEFAULT NULL COMMENT '班级描述',
    create_time     DATETIME        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted         TINYINT         DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='班级表';

-- ============================================================
-- 13. 班级用户关联表 sys_class_user
-- 说明: 班级与用户的多对多关联（教师和学生都可属于班级）
-- ============================================================
DROP TABLE IF EXISTS sys_class_user;
CREATE TABLE sys_class_user (
    id              BIGINT          NOT NULL AUTO_INCREMENT COMMENT 'ID',
    class_id        BIGINT          NOT NULL COMMENT '班级ID',
    user_id         BIGINT          NOT NULL COMMENT '用户ID',
    create_time     DATETIME        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_class_user (class_id, user_id),
    KEY idx_class_id (class_id),
    KEY idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='班级用户关联表';

-- ============================================================
-- 14. 试卷班级关联表 exam_paper_class
-- 说明: 试卷与班级的多对多关联，指定哪些班级可以参加该试卷考试
-- ============================================================
DROP TABLE IF EXISTS exam_paper_class;
CREATE TABLE exam_paper_class (
    id              BIGINT          NOT NULL AUTO_INCREMENT COMMENT 'ID',
    paper_id        BIGINT          NOT NULL COMMENT '试卷ID',
    class_id        BIGINT          NOT NULL COMMENT '班级ID',
    create_time     DATETIME        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_paper_class (paper_id, class_id),
    KEY idx_paper_id (paper_id),
    KEY idx_class_id (class_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='试卷班级关联表';

-- ============================================================
-- 初始化数据
-- ============================================================

-- 管理员账号 (密码: 123456, BCrypt加密)
INSERT INTO sys_user (username, password, real_name, role, status) VALUES
('admin', '$2a$10$6mDkpPbucmVZ9NQPvowqdOZGpc4Xy0QLaApNKMbvtpi1k6EAllmRi', '系统管理员', 'ADMIN', 1);

-- 教师账号 (用户名: t1, 密码: 123456)
INSERT INTO sys_user (username, password, real_name, role, status) VALUES
('t1', '$2a$10$6mDkpPbucmVZ9NQPvowqdOZGpc4Xy0QLaApNKMbvtpi1k6EAllmRi', '张老师', 'TEACHER', 1);

-- 学生账号 (用户名: s1, 密码: 123456)
INSERT INTO sys_user (username, password, real_name, role, class_id, major_id, status) VALUES
('s1', '$2a$10$6mDkpPbucmVZ9NQPvowqdOZGpc4Xy0QLaApNKMbvtpi1k6EAllmRi', '王同学', 'STUDENT', 1, 1, 1);

-- 额外学生账号 (用户名: s2, 密码: 123456)
INSERT INTO sys_user (username, password, real_name, role, class_id, major_id, status) VALUES
('s2', '$2a$10$6mDkpPbucmVZ9NQPvowqdOZGpc4Xy0QLaApNKMbvtpi1k6EAllmRi', '李同学', 'STUDENT', 2, 1, 1);

-- 字典数据 - 班级
INSERT INTO sys_dict (type, code, name, sort_order) VALUES
('CLASS', 'CLASS_2024_01', '2024级计算机1班', 1),
('CLASS', 'CLASS_2024_02', '2024级计算机2班', 2),
('CLASS', 'CLASS_2024_03', '2024级软件工程1班', 3);

-- 字典数据 - 专业
INSERT INTO sys_dict (type, code, name, sort_order) VALUES
('MAJOR', 'MAJOR_CS', '计算机科学与技术', 1),
('MAJOR', 'MAJOR_SE', '软件工程', 2),
('MAJOR', 'MAJOR_AI', '人工智能', 3);

-- 字典数据 - 科目
INSERT INTO sys_dict (type, code, name, sort_order) VALUES
('SUBJECT', 'SUBJ_JAVA', 'Java程序设计', 1),
('SUBJECT', 'SUBJ_DB', '数据库原理', 2),
('SUBJECT', 'SUBJ_ALGO', '数据结构与算法', 3),
('SUBJECT', 'SUBJ_NET', '计算机网络', 4);

-- 字典数据 - 学期
INSERT INTO sys_dict (type, code, name, sort_order) VALUES
('SEMESTER', 'SEM_2024_1', '2024-2025学年第一学期', 1),
('SEMESTER', 'SEM_2024_2', '2024-2025学年第二学期', 2),
('SEMESTER', 'SEM_2025_1', '2025-2026学年第一学期', 3);

-- 班级种子数据
INSERT INTO sys_class (id, name, description) VALUES
(1, '2024级计算机1班', '2024级计算机科学与技术1班'),
(2, '2024级计算机2班', '2024级计算机科学与技术2班');

-- 班级成员: 教师t1属于两个班，学生s1属于1班，学生s2属于2班
INSERT INTO sys_class_user (class_id, user_id) VALUES
(1, 2),  -- t1 → 1班
(2, 2),  -- t1 → 2班
(1, 3),  -- s1 → 1班
(2, 4);  -- s2 → 2班

# SmartExam 智能在线考试系统 - 完整项目文档

---

### 项目配置文件
- `backend/pom.xml`
- `backend/src/main/resources/application.yml`
- `backend/src/main/resources/application-dev.yml`
- `backend/src/main/resources/application-prod.yml`
- `frontend/package.json`
- `frontend/vite.config.js`
- `frontend/index.html`
- `sql/init.sql`
- `README.md`

### 后端 common 模块
- `common/config/CorsConfig.java`
- `common/config/MyBatisPlusConfig.java`
- `common/config/RedisConfig.java`
- `common/config/SaTokenConfig.java`
- `common/config/SecurityConfig.java`
- `common/config/StpInterfaceImpl.java`
- `common/config/SwaggerConfig.java`
- `common/config/ThreadPoolConfig.java`
- `common/exception/BizException.java`
- `common/exception/GlobalExceptionHandler.java`
- `common/result/ResultCode.java`
- `common/result/ResultVO.java`

### 后端工具类
- `util/RedisUtil.java`

### 后端启动类
- `SmartExamApplication.java`

### 后端 auth 模块
- `module/auth/controller/AuthController.java`
- `module/auth/dto/LoginDTO.java`
- `module/auth/service/AuthService.java`

### 后端 user 模块
- `module/user/controller/UserController.java`
- `module/user/dto/UserQueryDTO.java`
- `module/user/entity/SysUser.java`
- `module/user/mapper/SysUserMapper.java`
- `module/user/service/UserService.java`

### 后端 question 模块
- `module/question/controller/QuestionController.java`
- `module/question/dto/QuestionImportDTO.java`
- `module/question/dto/QuestionQueryDTO.java`
- `module/question/entity/Question.java`
- `module/question/entity/QuestionOption.java`
- `module/question/listener/QuestionImportListener.java`
- `module/question/mapper/QuestionMapper.java`
- `module/question/mapper/QuestionOptionMapper.java`
- `module/question/service/QuestionService.java`

### 后端 exam 模块
- `module/exam/algorithm/QuestionSelector.java`
- `module/exam/controller/ExamController.java`
- `module/exam/controller/ExamPaperController.java`
- `module/exam/controller/PracticeController.java`
- `module/exam/dto/DraftDTO.java`
- `module/exam/dto/ExamStartDTO.java`
- `module/exam/dto/ExamSubmitDTO.java`
- `module/exam/entity/ErrorBook.java`
- `module/exam/entity/ExamAnswer.java`
- `module/exam/entity/ExamPaper.java`
- `module/exam/entity/ExamPaperClass.java`
- `module/exam/entity/ExamPaperQuestion.java`
- `module/exam/entity/ExamRecord.java`
- `module/exam/entity/ExamRule.java`
- `module/exam/mapper/ErrorBookMapper.java`
- `module/exam/mapper/ExamAnswerMapper.java`
- `module/exam/mapper/ExamPaperClassMapper.java`
- `module/exam/mapper/ExamPaperMapper.java`
- `module/exam/mapper/ExamPaperQuestionMapper.java`
- `module/exam/mapper/ExamRecordMapper.java`
- `module/exam/mapper/ExamRuleMapper.java`
- `module/exam/service/ExamPaperService.java`
- `module/exam/service/ExamService.java`
- `module/exam/service/PracticeService.java`
- `module/exam/vo/PracticeCheckVO.java`

### 后端 class 模块
- `module/class_/entity/SysClass.java`
- `module/class_/entity/SysClassUser.java`
- `module/class_/mapper/SysClassMapper.java`
- `module/class_/mapper/SysClassUserMapper.java`
- `module/class_/service/ClassService.java`
- `module/class_/controller/ClassController.java`

### 后端 dict 模块
- `module/dict/controller/DictController.java`
- `module/dict/entity/SysDict.java`
- `module/dict/mapper/SysDictMapper.java`
- `module/dict/service/DictService.java`

### 后端 grading 模块
- `module/grading/controller/GradingController.java`
- `module/grading/dto/BatchGradeDTO.java`
- `module/grading/dto/GradeAnswerDTO.java`
- `module/grading/service/GradingService.java`
- `module/grading/vo/GradingPaperDetailVO.java`
- `module/grading/vo/GradingPaperVO.java`
- `module/grading/vo/GradingQuestionItem.java`

### 后端 ai 模块
- `module/ai/entity/AiGradingLog.java`
- `module/ai/mapper/AiGradingLogMapper.java`
- `module/ai/service/AiGradingService.java`

### 后端 stats 模块
- `module/stats/controller/StatsController.java`
- `module/stats/service/StatsService.java`
- `module/stats/vo/PaperReportVO.java`

### 前端入口与配置
- `frontend/src/main.js`
- `frontend/src/App.vue`
- `frontend/src/router/index.js`
- `frontend/src/store/user.js`
- `frontend/src/store/exam.js`

### 前端 API 模块
- `frontend/src/api/request.js`
- `frontend/src/api/auth.js`
- `frontend/src/api/class.js`
- `frontend/src/api/dict.js`
- `frontend/src/api/exam.js`
- `frontend/src/api/paper.js`
- `frontend/src/api/question.js`
- `frontend/src/api/stats.js`
- `frontend/src/api/user.js`

### 前端工具类
- `frontend/src/utils/format.js`
- `frontend/src/utils/storage.js`

### 前端 admin 视图
- `frontend/src/views/admin/Layout.vue`
- `frontend/src/views/admin/ClassManage.vue`
- `frontend/src/views/admin/DictManage.vue`
- `frontend/src/views/admin/UserManage.vue`

### 前端 teacher 视图
- `frontend/src/views/teacher/Layout.vue`
- `frontend/src/views/teacher/DataBoard.vue`
- `frontend/src/views/teacher/GradingCenter.vue`
- `frontend/src/views/teacher/PaperManage.vue`
- `frontend/src/views/teacher/QuestionBank.vue`

### 前端 student 视图
- `frontend/src/views/student/Layout.vue`
- `frontend/src/views/student/Dashboard.vue`
- `frontend/src/views/student/ErrorBook.vue`
- `frontend/src/views/student/ExamHall.vue`
- `frontend/src/views/student/ExamRoom.vue`
- `frontend/src/views/student/History.vue`

### 前端登录视图
- `frontend/src/views/login/index.vue`

### 项目文档
- `docs/API.md`
- `docs/DEPLOY.md`
- `docs/TEST_GUIDE.md`

---

## 1. 项目概述

### 1.1 项目名称
SmartExam - 智能在线考试系统

### 1.2 项目目标
构建一套支持千人千卷、AI辅助阅卷、实时数据可视化的企业级在线考试平台。

### 1.3 核心价值
- **防作弊**：题目/选项双重乱序，杜绝抄袭
- **AI赋能**：通义千问自动批改主观题，降低教师负担
- **数据驱动**：ECharts可视化看板，精准分析教学效果
- **班级管理**：管理员管理班级，教师指定班级发布试卷，只有指定班级学生可参加考试

### 1.4 测试账号

| 角色 | 用户名 | 密码 | 说明 |
|------|--------|------|------|
| 管理员 | admin | admin123 | 系统管理员 |
| 教师 | t1 | 123456 | 张老师 |
| 学生 | s1 | 123456 | 王同学 |

---

## 2. 技术架构

### 2.1 整体架构图
```
┌─────────────────────────────────────────────────────────────┐
│                        Nginx (反向代理)                       │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌──────────────┐              ┌──────────────────────┐     │
│  │   Vue 3 SPA  │   Axios     │   Spring Boot 3.2    │     │
│  │   + Vite 5   │◄───────────►│   + Sa-Token JWT     │     │
│  │   + Element   │             │   + MyBatis-Plus     │     │
│  │   + ECharts   │             │   + EasyExcel        │     │
│  └──────────────┘             └──────────┬───────────┘     │
│                                          │                  │
│                              ┌───────────┴───────────┐     │
│                              │                       │     │
│                    ┌─────────▼─────┐     ┌───────────▼──┐  │
│                    │   MySQL 8.0   │     │  内存缓存    │  │
│                    │   (持久化)     │     │(ConcurrentMap│  │
│                    └───────────────┘     └──────────────┘  │
│                                                             │
│                    ┌───────────────────────────────────┐    │
│                    │   DashScope SDK (通义千问 API)     │    │
│                    └───────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

### 2.2 技术选型明细

| 层次 | 技术 | 版本 | 用途 |
|------|------|------|------|
| 前端框架 | Vue 3 | 3.4+ | Composition API + `<script setup>` |
| 构建工具 | Vite | 5.x | HMR/ESM/Tree-shaking |
| 状态管理 | Pinia | 2.x | 试卷状态/用户信息/草稿队列 |
| 路由 | Vue Router | 4.x | SPA路由守卫/权限控制 |
| HTTP | Axios | 1.x | 请求封装/拦截器/重试 |
| UI库 | Element Plus | 2.x | 表单/表格/弹窗/消息 |
| 图表 | ECharts | 5.x | 饼图/柱状图/数据看板 |
| 后端框架 | Spring Boot | 3.2.5 | 自动配置/依赖注入 |
| ORM | MyBatis-Plus | 3.5.6 | CRUD/分页/代码生成 |
| 鉴权 | Sa-Token | 1.38+ | JWT登录/权限/路由拦截 |
| Excel | EasyExcel | 3.x | 题库/账号批量导入 |
| AI SDK | DashScope SDK | 2.x | 通义千问调用 |
| 数据库 | MySQL | 8.0 | 主数据存储 |
| 缓存 | 内存缓存 | ConcurrentHashMap | 草稿/缓存（开发环境，未使用Redis） |
| API文档 | SpringDoc | 2.x | Swagger/OpenAPI 3.0 |

### 2.3 项目目录结构

```
smart-exam-system/
├── backend/                          # 后端工程
│   ├── pom.xml                       # Maven依赖
│   └── src/main/
│       ├── java/com/smartexam/
│       │   ├── SmartExamApplication.java    # 启动类
│       │   ├── common/               # 公共模块
│       │   │   ├── result/           # 统一响应
│       │   │   │   ├── ResultVO.java
│       │   │   │   └── ResultCode.java
│       │   │   ├── exception/        # 异常处理
│       │   │   │   ├── BizException.java
│       │   │   │   └── GlobalExceptionHandler.java
│       │   │   └── config/           # 全局配置
│       │   │       ├── CorsConfig.java
│       │   │       ├── SaTokenConfig.java
│       │   │       ├── MyBatisPlusConfig.java
│       │   │       └── AsyncConfig.java
│       │   ├── module/
│       │   │   ├── auth/             # 认证模块
│       │   │   ├── user/             # 用户模块
│       │   │   ├── question/         # 题库模块
│       │   │   ├── exam/             # 考试模块
│       │   │   ├── grading/          # 阅卷模块
│       │   │   ├── stats/            # 统计模块
│       │   │   ├── ai/               # AI模块
│       │   │   ├── dict/             # 字典模块
│       │   │   └── class_/           # 班级模块
│       │   └── util/                 # 工具类
│       │       └── RedisUtil.java
│       └── resources/
│           ├── application.yml
│           ├── application-dev.yml
│           └── mapper/               # MyBatis XML
│
├── frontend/                         # 前端工程
│   ├── package.json
│   ├── vite.config.js
│   └── src/
│       ├── main.js                   # 入口文件
│       ├── App.vue                   # 根组件
│       ├── api/                      # API封装 (9个文件)
│       ├── router/                   # 路由配置
│       ├── store/                    # Pinia状态 (2个)
│       ├── views/                    # 页面组件 (16个)
│       └── utils/                    # 工具函数 (2个)
│
├── sql/                              # 数据库脚本
│   └── init.sql
├── PROJECT.md                        # 本文档
└── PROJECT_PLAN.md                   # 项目规划书
```

---

## 3. 数据库设计

### 3.1 表清单

| 表名 | 说明 | 预估数据量 |
|------|------|-----------|
| `sys_user` | 用户表(学生/教师/管理员) | 10万+ |
| `sys_dict` | 字典表(班级/专业/科目/学期) | 1000+ |
| `sys_class` | 班级表 | 500+ |
| `sys_class_user` | 班级-用户关联表 | 5000+ |
| `question` | 题库表 | 10万+ |
| `question_option` | 选项表(单选/多选/判断) | 50万+ |
| `exam_paper` | 试卷表 | 1万+ |
| `exam_paper_class` | 试卷-班级关联表 | 5000+ |
| `exam_rule` | 组卷规则表 | 5万+ |
| `exam_paper_question` | 试卷-题目关联表 | 10万+ |
| `exam_record` | 考试记录表 | 100万+ |
| `exam_answer` | 答题记录表 | 1000万+ |
| `ai_grading_log` | AI阅卷日志 | 10万+ |
| `error_book` | 错题本 | 100万+ |

### 3.2 ER关系图
```
sys_user (1) ────── (N) exam_record
                │
exam_paper (1) ─┘
                │
exam_record (1) ──── (N) exam_answer
                │
question (1) ───┘
                │
exam_paper (1) ──── (N) exam_rule
                │
question (1) ──── (N) question_option
                │
exam_answer (1) ──── (N) ai_grading_log

sys_class (1) ──── (N) sys_class_user ──── (N) sys_user
exam_paper (1) ──── (N) exam_paper_class ──── (N) sys_class
```

### 3.3 各表详细字段

#### 3.3.1 sys_user 用户表
```sql
CREATE TABLE sys_user (
    id              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    username        VARCHAR(50)     NOT NULL COMMENT '用户名(学号/工号)',
    password        VARCHAR(100)    NOT NULL COMMENT '密码(BCrypt加密)',
    real_name       VARCHAR(30)     NOT NULL COMMENT '真实姓名',
    role            VARCHAR(20)     NOT NULL COMMENT '角色: STUDENT/TEACHER/ADMIN',
    gender          TINYINT         DEFAULT 0 COMMENT '性别: 0-未知, 1-男, 2-女',
    email           VARCHAR(100)    DEFAULT NULL COMMENT '邮箱',
    phone           VARCHAR(20)     DEFAULT NULL COMMENT '手机号',
    avatar          VARCHAR(255)    DEFAULT NULL COMMENT '头像URL',
    class_id        BIGINT          DEFAULT NULL COMMENT '班级ID(关联sys_dict)',
    major_id        BIGINT          DEFAULT NULL COMMENT '专业ID(关联sys_dict)',
    status          TINYINT         DEFAULT 1 COMMENT '状态: 0-冻结, 1-正常',
    last_login_time DATETIME        DEFAULT NULL COMMENT '最后登录时间',
    last_login_ip   VARCHAR(50)     DEFAULT NULL COMMENT '最后登录IP',
    create_time     DATETIME        DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted         TINYINT         DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username)
);
```

#### 3.3.2 sys_dict 字典表
```sql
CREATE TABLE sys_dict (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    type        VARCHAR(50)     NOT NULL COMMENT '类型: CLASS/MAJOR/SUBJECT/SEMESTER',
    code        VARCHAR(50)     NOT NULL COMMENT '编码',
    name        VARCHAR(100)    NOT NULL COMMENT '名称',
    sort_order  INT             DEFAULT 0 COMMENT '排序号',
    status      TINYINT         DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    remark      VARCHAR(255)    DEFAULT NULL COMMENT '备注',
    create_time DATETIME        DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted     TINYINT         DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY uk_type_code (type, code)
);
```

#### 3.3.3 sys_class 班级表
```sql
CREATE TABLE sys_class (
    id          BIGINT NOT NULL AUTO_INCREMENT,
    name        VARCHAR(100) NOT NULL COMMENT '班级名称',
    description VARCHAR(255) DEFAULT NULL COMMENT '班级描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted     TINYINT DEFAULT 0,
    PRIMARY KEY (id)
);
```

#### 3.3.4 sys_class_user 班级用户关联表
```sql
CREATE TABLE sys_class_user (
    id          BIGINT NOT NULL AUTO_INCREMENT,
    class_id    BIGINT NOT NULL COMMENT '班级ID',
    user_id     BIGINT NOT NULL COMMENT '用户ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_class_user (class_id, user_id)
);
```

#### 3.3.5 question 题库表
```sql
CREATE TABLE question (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    title           TEXT            NOT NULL COMMENT '题干(支持HTML)',
    type            VARCHAR(20)     NOT NULL COMMENT '题型: SINGLE/MULTI/JUDGE/FILL/SUBJECTIVE',
    difficulty      TINYINT         NOT NULL DEFAULT 3 COMMENT '难度: 1-5',
    subject_id      BIGINT          DEFAULT NULL COMMENT '科目ID',
    chapter         VARCHAR(100)    DEFAULT NULL COMMENT '章节',
    score           DECIMAL(5,2)    NOT NULL DEFAULT 0 COMMENT '默认分值',
    answer          TEXT            DEFAULT NULL COMMENT '参考答案',
    explanation     TEXT            DEFAULT NULL COMMENT '答案解析',
    tags            JSON            DEFAULT NULL COMMENT '标签数组',
    status          VARCHAR(20)     DEFAULT 'DRAFT' COMMENT '状态: DRAFT/PUBLISHED/DISABLED',
    create_by       BIGINT          DEFAULT NULL,
    update_by       BIGINT          DEFAULT NULL,
    create_time     DATETIME        DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted         TINYINT         DEFAULT 0,
    PRIMARY KEY (id)
);
```

#### 3.3.6 question_option 选项表
```sql
CREATE TABLE question_option (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    question_id     BIGINT          NOT NULL COMMENT '题目ID',
    option_label    VARCHAR(10)     NOT NULL COMMENT '选项标识: A/B/C/D',
    option_content  TEXT            NOT NULL COMMENT '选项内容',
    is_correct      TINYINT         DEFAULT 0 COMMENT '是否正确: 0-否, 1-是',
    sort_order      INT             DEFAULT 0 COMMENT '排序号',
    create_time     DATETIME        DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_question_id (question_id)
);
```

#### 3.3.7 exam_paper 试卷表
```sql
CREATE TABLE exam_paper (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    title           VARCHAR(200)    NOT NULL COMMENT '试卷标题',
    description     TEXT            DEFAULT NULL COMMENT '试卷说明',
    subject_id      BIGINT          DEFAULT NULL COMMENT '科目ID',
    subject_name    VARCHAR(100)    DEFAULT NULL COMMENT '科目名称',
    semester_id     BIGINT          DEFAULT NULL COMMENT '学期ID',
    total_score     DECIMAL(5,2)    NOT NULL DEFAULT 0 COMMENT '总分',
    pass_score      DECIMAL(5,2)    NOT NULL DEFAULT 60 COMMENT '及格分数',
    duration        INT             NOT NULL DEFAULT 120 COMMENT '时长(分钟)',
    start_time      DATETIME        DEFAULT NULL COMMENT '开始时间',
    end_time        DATETIME        DEFAULT NULL COMMENT '结束时间',
    shuffle_question TINYINT        DEFAULT 1 COMMENT '乱序题目: 0-否, 1-是',
    shuffle_option  TINYINT         DEFAULT 1 COMMENT '乱序选项: 0-否, 1-是',
    show_answer     TINYINT         DEFAULT 0 COMMENT '交卷后显示答案',
    max_attempts    INT             DEFAULT 1 COMMENT '最大考试次数',
    status          VARCHAR(20)     DEFAULT 'DRAFT' COMMENT '状态: DRAFT/PUBLISHED/CLOSED',
    type            VARCHAR(20)     DEFAULT 'EXAM' COMMENT '类型: EXAM/PRACTICE',
    create_by       BIGINT          DEFAULT NULL,
    create_time     DATETIME        DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted         TINYINT         DEFAULT 0,
    PRIMARY KEY (id)
);
```

#### 3.3.8 exam_paper_class 试卷班级关联表
```sql
CREATE TABLE exam_paper_class (
    id          BIGINT NOT NULL AUTO_INCREMENT,
    paper_id    BIGINT NOT NULL COMMENT '试卷ID',
    class_id    BIGINT NOT NULL COMMENT '班级ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_paper_class (paper_id, class_id)
);
```

#### 3.3.9 exam_rule 组卷规则表
```sql
CREATE TABLE exam_rule (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    paper_id        BIGINT          NOT NULL COMMENT '试卷ID',
    rule_config     JSON            NOT NULL COMMENT '规则配置JSON',
    create_time     DATETIME        DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);
```

#### 3.3.10 exam_paper_question 试卷题目关联表
```sql
CREATE TABLE exam_paper_question (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    paper_id        BIGINT          NOT NULL COMMENT '试卷ID',
    question_id     BIGINT          NOT NULL COMMENT '题目ID',
    score           DECIMAL(5,2)    NOT NULL COMMENT '该题分值',
    sort_order      INT             DEFAULT 0 COMMENT '排序',
    create_time     DATETIME        DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_paper_question (paper_id, question_id)
);
```

#### 3.3.11 exam_record 考试记录表
```sql
CREATE TABLE exam_record (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    paper_id        BIGINT          NOT NULL COMMENT '试卷ID',
    user_id         BIGINT          NOT NULL COMMENT '学生ID',
    attempt_no      INT             DEFAULT 1 COMMENT '第几次考试',
    status          VARCHAR(20)     DEFAULT 'NOT_STARTED' COMMENT '状态',
    total_score     DECIMAL(5,2)    DEFAULT NULL COMMENT '总得分',
    start_time      DATETIME        DEFAULT NULL,
    end_time        DATETIME        DEFAULT NULL,
    submit_time     DATETIME        DEFAULT NULL,
    shuffle_seed    BIGINT          DEFAULT NULL COMMENT '乱序种子',
    question_order  JSON            DEFAULT NULL COMMENT '题目顺序',
    option_order    JSON            DEFAULT NULL COMMENT '选项顺序',
    ip_address      VARCHAR(50)     DEFAULT NULL,
    create_time     DATETIME        DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_paper_user_attempt (paper_id, user_id, attempt_no)
);
```

#### 3.3.12 exam_answer 答题记录表
```sql
CREATE TABLE exam_answer (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    record_id       BIGINT          NOT NULL COMMENT '考试记录ID',
    question_id     BIGINT          NOT NULL COMMENT '题目ID',
    user_answer     TEXT            DEFAULT NULL COMMENT '学生答案',
    score           DECIMAL(5,2)    DEFAULT NULL COMMENT '得分',
    is_correct      TINYINT         DEFAULT NULL COMMENT '是否正确',
    ai_score        DECIMAL(5,2)    DEFAULT NULL COMMENT 'AI评分',
    ai_comment      TEXT            DEFAULT NULL COMMENT 'AI评语',
    final_score     DECIMAL(5,2)    DEFAULT NULL COMMENT '最终分数',
    grading_status  VARCHAR(20)     DEFAULT 'PENDING' COMMENT '阅卷状态',
    answer_time     INT             DEFAULT NULL COMMENT '答题耗时(秒)',
    create_time     DATETIME        DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_record_question (record_id, question_id)
);
```

#### 3.3.13 ai_grading_log AI阅卷日志表
```sql
CREATE TABLE ai_grading_log (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    answer_id       BIGINT          NOT NULL COMMENT '答题记录ID',
    question_id     BIGINT          NOT NULL COMMENT '题目ID',
    model_name      VARCHAR(50)     DEFAULT NULL COMMENT '模型名称',
    prompt          TEXT            NOT NULL COMMENT '发送的Prompt',
    response        TEXT            DEFAULT NULL COMMENT 'AI返回结果',
    ai_score        DECIMAL(5,2)    DEFAULT NULL,
    ai_comment      TEXT            DEFAULT NULL,
    confidence      DECIMAL(3,2)    DEFAULT NULL COMMENT '置信度',
    token_used      INT             DEFAULT NULL COMMENT 'Token消耗',
    duration_ms     INT             DEFAULT NULL COMMENT '调用耗时(ms)',
    status          VARCHAR(20)     DEFAULT 'SUCCESS' COMMENT '状态',
    error_msg       TEXT            DEFAULT NULL,
    retry_count     INT             DEFAULT 0,
    create_time     DATETIME        DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);
```

#### 3.3.14 error_book 错题本表
```sql
CREATE TABLE error_book (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    user_id         BIGINT          NOT NULL COMMENT '学生ID',
    question_id     BIGINT          NOT NULL COMMENT '题目ID',
    record_id       BIGINT          NOT NULL COMMENT '考试记录ID',
    user_answer     TEXT            DEFAULT NULL COMMENT '学生作答',
    correct_answer  TEXT            DEFAULT NULL COMMENT '正确答案',
    error_count     INT             DEFAULT 1 COMMENT '错误次数',
    last_error_time DATETIME        DEFAULT NULL,
    review_count    INT             DEFAULT 0 COMMENT '复习次数',
    last_review_time DATETIME       DEFAULT NULL,
    mastered        TINYINT         DEFAULT 0 COMMENT '是否已掌握',
    create_time     DATETIME        DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_question (user_id, question_id)
);
```

### 3.4 初始数据
```sql
-- 管理员 (密码: admin123)
INSERT INTO sys_user (username, password, real_name, role, status) VALUES
('admin', '$2a$10$TNggo3Dwhw6kZ0e3T1cvwO4WF1TsoB4cWOX2viakOIMukS0UVikbi', '系统管理员', 'ADMIN', 1);

-- 教师 (密码: 123456)
INSERT INTO sys_user (username, password, real_name, role, status) VALUES
('t1', '$2a$10$6mDkpPbucmVZ9NQPvowqdOZGpc4Xy0QLaApNKMbvtpi1k6EAllmRi', '张老师', 'TEACHER', 1);

-- 学生 (密码: 123456)
INSERT INTO sys_user (username, password, real_name, role, class_id, major_id, status) VALUES
('s1', '$2a$10$6mDkpPbucmVZ9NQPvowqdOZGpc4Xy0QLaApNKMbvtpi1k6EAllmRi', '王同学', 'STUDENT', 1, 1, 1);

-- 班级
INSERT INTO sys_class (id, name, description) VALUES
(1, '2024级计算机1班', '2024级计算机科学与技术1班'),
(2, '2024级计算机2班', '2024级计算机科学与技术2班'),
(3, '2024级软件工程1班', '2024级软件工程1班');

-- 字典数据
INSERT INTO sys_dict (type, code, name, sort_order) VALUES
('CLASS', 'CLASS_2024_01', '2024级计算机1班', 1),
('MAJOR', 'MAJOR_CS', '计算机科学与技术', 1),
('SUBJECT', 'SUBJ_JAVA', 'Java程序设计', 1),
('SEMESTER', 'SEM_2024_1', '2024-2025学年第一学期', 1);
```

---

## 4. 后端实现详解

### 4.1 启动类 SmartExamApplication.java

**路径**: `backend/src/main/java/com/smartexam/SmartExamApplication.java`

**作用**: Spring Boot 应用入口，配置 MyBatis-Plus 的 Mapper 扫描路径，启用异步支持。

**关键注解**:
- `@SpringBootApplication` - Spring Boot 自动配置
- `@MapperScan({...})` - 扫描所有 Mapper 接口所在的包
- `@EnableAsync` - 启用异步方法支持（用于AI阅卷）

**扫描的 Mapper 包**:
```java
@MapperScan({
    "com.smartexam.module.user.mapper",
    "com.smartexam.module.question.mapper",
    "com.smartexam.module.exam.mapper",
    "com.smartexam.module.ai.mapper",
    "com.smartexam.module.dict.mapper",
    "com.smartexam.module.stats.mapper",
    "com.smartexam.module.grading.mapper",
    "com.smartexam.module.class_.mapper"
})
```

---

### 4.2 公共模块 (common)

#### 4.2.1 ResultVO.java - 统一响应封装

**路径**: `backend/src/main/java/com/smartexam/common/result/ResultVO.java`

**作用**: 所有 API 接口的统一返回格式，包含状态码、消息、数据、时间戳。

**字段**:
| 字段 | 类型 | 说明 |
|------|------|------|
| code | int | 状态码，200表示成功 |
| message | String | 响应消息 |
| data | T | 响应数据（泛型） |
| timestamp | long | 时间戳 |

**静态方法**:
| 方法 | 说明 |
|------|------|
| `success()` | 成功响应，无数据 |
| `success(T data)` | 成功响应，带数据 |
| `success(String message, T data)` | 成功响应，自定义消息 |
| `fail()` | 失败响应 |
| `fail(String message)` | 失败响应，自定义消息 |
| `fail(int code, String message)` | 失败响应，自定义状态码 |
| `fail(ResultCode resultCode)` | 失败响应，使用错误码枚举 |

#### 4.2.2 ResultCode.java - 统一错误码枚举

**路径**: `backend/src/main/java/com/smartexam/common/result/ResultCode.java`

**作用**: 定义系统所有错误码，按模块分组。

**错误码分类**:

| 范围 | 模块 | 示例 |
|------|------|------|
| 200-500 | 通用 | SUCCESS(200), FAILURE(500), UNAUTHORIZED(401) |
| 1001-1999 | 认证 | LOGIN_FAILED(1001), ACCOUNT_DISABLED(1002) |
| 2001-2999 | 用户 | USER_EXISTS(2001), USER_NOT_FOUND(2002) |
| 3001-3999 | 题库 | QUESTION_NOT_FOUND(3001) |
| 4001-4999 | 考试 | PAPER_NOT_FOUND(4001), ATTEMPT_LIMIT_EXCEEDED(4008) |
| 5001-5999 | 阅卷 | GRADING_FAILED(5001), SCORE_INVALID(5002) |
| 6001-6999 | AI | AI_CALL_FAILED(6001), AI_TIMEOUT(6002) |

#### 4.2.3 BizException.java - 业务异常

**路径**: `backend/src/main/java/com/smartexam/common/exception/BizException.java`

**作用**: 自定义业务异常，携带错误码和消息。Controller 层抛出后由全局异常处理器捕获。

**构造方法**:
- `BizException(ResultCode resultCode)` - 使用错误码枚举
- `BizException(int code, String message)` - 自定义错误码和消息
- `BizException(String message)` - 仅消息，默认500

#### 4.2.4 GlobalExceptionHandler.java - 全局异常处理器

**路径**: `backend/src/main/java/com/smartexam/common/exception/GlobalExceptionHandler.java`

**作用**: 使用 `@RestControllerAdvice` 捕获所有异常，统一返回 `ResultVO` 格式。

**处理的异常类型**:
- `BizException` - 业务异常，返回对应错误码
- `NotLoginException` - Sa-Token 未登录异常，返回401
- `NotRoleException` - Sa-Token 无角色异常，返回403
- `MethodArgumentNotValidException` - 参数校验失败，返回400
- `Exception` - 其他未知异常，返回500

#### 4.2.5 CorsConfig.java - 跨域配置

**路径**: `backend/src/main/java/com/smartexam/common/config/CorsConfig.java`

**作用**: 配置 CORS 跨域，允许前端开发服务器（localhost:3000）访问后端 API。

**配置项**:
- 允许来源: `http://localhost:3000`
- 允许方法: GET, POST, PUT, DELETE, OPTIONS
- 允许头: `*`
- 允许凭证: true
- 最大缓存: 3600秒

#### 4.2.6 SaTokenConfig.java - Sa-Token 鉴权配置

**路径**: `backend/src/main/java/com/smartexam/common/config/SaTokenConfig.java`

**作用**: 配置 Sa-Token 路由拦截器，定义各 API 路径的角色权限。

**权限规则**:

| 路径模式 | 所需角色 | 说明 |
|----------|----------|------|
| `/api/auth/login` | 无 | 登录接口放行 |
| `/swagger-ui/**`, `/v3/api-docs/**` | 无 | Swagger 放行 |
| `/api/exam/**`, `/api/practice/**`, `/api/stats/student/**` | STUDENT | 学生专属接口 |
| `/api/questions/**`, `/api/grading/**`, `/api/stats/paper/**` | TEACHER | 教师专属接口 |
| `/api/papers/**` | STUDENT 或 TEACHER | 试卷接口（学生看列表，教师管理） |
| `/api/users/**`, `/api/classes/**` | ADMIN | 管理员专属接口 |
| `/api/dicts/**` | ADMIN 或 TEACHER | 字典接口 |
| `/**` | 登录 | 其他接口需要登录 |

**实现方式**: 使用 `SaInterceptor` + `SaRouter.match().check()` 链式调用。

#### 4.2.7 MyBatisPlusConfig.java - MyBatis-Plus 配置

**路径**: `backend/src/main/java/com/smartexam/common/config/MyBatisPlusConfig.java`

**作用**: 配置 MyBatis-Plus 分页插件和自动填充。

**配置内容**:
- 注册 `MybatisPlusInterceptor`
- 添加 `PaginationInnerInterceptor` 分页插件
- 配置 `MetaObjectHandler` 自动填充 createTime/updateTime

#### 4.2.8 AsyncConfig.java - 异步线程池配置

**路径**: `backend/src/main/java/com/smartexam/common/config/AsyncConfig.java`

**作用**: 配置 AI 阅卷专用的异步线程池。

**线程池参数**:
- 核心线程数: 5
- 最大线程数: 10
- 队列容量: 200
- 线程名前缀: `ai-grading-`

---

### 4.3 认证模块 (auth)

#### 4.3.1 AuthController.java

**路径**: `backend/src/main/java/com/smartexam/module/auth/controller/AuthController.java`

**接口列表**:

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| POST | `/api/auth/login` | 用户登录 | 无 |
| POST | `/api/auth/logout` | 退出登录 | 登录 |
| GET | `/api/auth/info` | 获取当前用户信息 | 登录 |

#### 4.3.2 AuthService.java

**路径**: `backend/src/main/java/com/smartexam/module/auth/service/AuthService.java`

**核心方法**:

`login(LoginDTO dto)` 方法流程:
1. 根据 username 查询用户
2. 校验用户是否存在
3. 校验账号是否被冻结（status=0）
4. 使用 BCryptPasswordEncoder 校验密码
5. 调用 `StpUtil.login(userId)` 创建 Sa-Token 会话
6. 更新最后登录时间和IP
7. 返回 token、userId、role、realName

`getUserInfo()` 方法流程:
1. 从 Sa-Token 获取当前登录用户ID
2. 查询用户信息
3. 返回 userId、username、realName、role、avatar

#### 4.3.3 LoginDTO.java

**路径**: `backend/src/main/java/com/smartexam/module/auth/dto/LoginDTO.java`

**字段**:
- `username` (String, 必填) - 用户名
- `password` (String, 必填) - 密码

---

### 4.4 用户模块 (user)

#### 4.4.1 SysUser.java - 用户实体

**路径**: `backend/src/main/java/com/smartexam/module/user/entity/SysUser.java`

**关键注解**:
- `@TableName("sys_user")` - 映射数据库表
- `@TableId(type = IdType.AUTO)` - 主键自增
- `@TableLogic` - 逻辑删除字段
- `@TableField(fill = FieldFill.INSERT)` - createTime 自动填充

**字段**: id, username, password, realName, role, gender, email, phone, avatar, classId, majorId, status, lastLoginTime, lastLoginIp, createTime, updateTime, deleted

#### 4.4.2 SysUserMapper.java

**路径**: `backend/src/main/java/com/smartexam/module/user/mapper/SysUserMapper.java`

**作用**: 继承 `BaseMapper<SysUser>`，提供基础 CRUD 操作。无需手写 SQL。

#### 4.4.3 UserService.java

**路径**: `backend/src/main/java/com/smartexam/module/user/service/UserService.java`

**核心方法**:

| 方法 | 说明 |
|------|------|
| `page(keyword, role, pageNum, pageSize)` | 分页查询用户，支持按角色和关键词筛选 |
| `create(user)` | 创建用户，密码BCrypt加密，默认密码123456 |
| `update(user)` | 更新用户信息 |
| `delete(id)` | 逻辑删除用户 |
| `resetPassword(id)` | 重置密码为123456 |
| `toggleStatus(id)` | 切换用户冻结/正常状态 |
| `importUsers(file)` | Excel批量导入学生账号 |
| `updateProfile(userId, data)` | 用户修改个人信息 |

#### 4.4.4 UserController.java

**路径**: `backend/src/main/java/com/smartexam/module/user/controller/UserController.java`

**接口列表**:

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/users` | 分页查询用户 |
| POST | `/api/users` | 创建用户 |
| PUT | `/api/users/{id}` | 更新用户 |
| DELETE | `/api/users/{id}` | 删除用户 |
| POST | `/api/users/import` | Excel批量导入 |
| PUT | `/api/users/{id}/reset-password` | 重置密码 |
| PUT | `/api/users/{id}/toggle-status` | 冻结/解冻 |
| PUT | `/api/users/profile` | 修改个人信息 |

---

### 4.5 题库模块 (question)

#### 4.5.1 Question.java - 题目实体

**路径**: `backend/src/main/java/com/smartexam/module/question/entity/Question.java`

**字段**: id, title, type, difficulty, subjectId, chapter, score, answer, explanation, tags, status, createBy, updateBy, createTime, updateTime, deleted

**题型枚举**:
- `SINGLE` - 单选题
- `MULTI` - 多选题
- `JUDGE` - 判断题
- `FILL` - 填空题
- `SUBJECTIVE` - 主观题

#### 4.5.2 QuestionOption.java - 选项实体

**路径**: `backend/src/main/java/com/smartexam/module/question/entity/QuestionOption.java`

**字段**: id, questionId, optionLabel, optionContent, isCorrect, sortOrder, createTime

#### 4.5.3 QuestionMapper.java

**路径**: `backend/src/main/java/com/smartexam/module/question/mapper/QuestionMapper.java`

**自定义方法**:
- `selectByRule(subjectId, chapter, type, difficulty)` - 按规则查询候选题目（用于抽题算法）

#### 4.5.4 QuestionService.java

**路径**: `backend/src/main/java/com/smartexam/module/question/service/QuestionService.java`

**核心方法**:

| 方法 | 说明 |
|------|------|
| `page(keyword, type, difficulty, pageNum, pageSize)` | 分页查询题目 |
| `getDetail(id)` | 获取题目详情（含选项列表） |
| `create(question)` | 创建题目（同时保存选项） |
| `update(question)` | 更新题目（先删旧选项再插入新选项） |
| `delete(id)` | 逻辑删除题目 |
| `importQuestions(file, subjectId)` | Excel批量导入题目 |

#### 4.5.5 QuestionController.java

**路径**: `backend/src/main/java/com/smartexam/module/question/controller/QuestionController.java`

**接口列表**:

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/questions` | 分页查询题目 |
| GET | `/api/questions/{id}` | 获取题目详情 |
| POST | `/api/questions` | 创建题目 |
| PUT | `/api/questions/{id}` | 更新题目 |
| DELETE | `/api/questions/{id}` | 删除题目 |
| POST | `/api/questions/import` | Excel批量导入 |

---

### 4.6 考试模块 (exam)

#### 4.6.1 实体类

**ExamPaper.java** - 试卷实体
- 字段: id, title, description, subjectId, subjectName, semesterId, totalScore, passScore, duration, startTime, endTime, shuffleQuestion, shuffleOption, showAnswer, maxAttempts, status, type, createBy, createTime, updateTime, deleted

**ExamPaperQuestion.java** - 试卷题目关联
- 字段: id, paperId, questionId, score, sortOrder, createTime

**ExamRule.java** - 组卷规则
- 字段: id, paperId, ruleConfig (JSON), createTime

**ExamRecord.java** - 考试记录
- 字段: id, paperId, userId, attemptNo, status, totalScore, startTime, endTime, submitTime, shuffleSeed, questionOrder, optionOrder, ipAddress, createTime, updateTime

**ExamAnswer.java** - 答题记录
- 字段: id, recordId, questionId, userAnswer, score, isCorrect, aiScore, aiComment, finalScore, gradingStatus, answerTime, createTime, updateTime

**ExamPaperClass.java** - 试卷班级关联
- 字段: id, paperId, classId, createTime

#### 4.6.2 ExamPaperService.java - 试卷服务

**路径**: `backend/src/main/java/com/smartexam/module/exam/service/ExamPaperService.java`

**核心方法**:

| 方法 | 说明 |
|------|------|
| `page(keyword, status, pageNum, pageSize, userId, role)` | 分页查询试卷。学生角色只返回其班级关联的已发布试卷 |
| `getDetail(id)` | 获取试卷详情 |
| `create(paper)` | 创建试卷（手动组卷） |
| `generate(paper, ruleConfig)` | 规则抽题组卷 |
| `update(paper)` | 更新试卷 |
| `publish(id)` | 发布试卷（状态改为PUBLISHED） |
| `delete(id)` | 删除试卷（同时删除关联的规则和题目） |
| `getPaperQuestions(paperId)` | 获取试卷题目列表（含选项和分值） |
| `addQuestion(paperId, questionId, score)` | 向试卷添加题目 |
| `updatePaperQuestion(paperId, pqId, score, sortOrder)` | 更新试卷题目分值 |
| `removeQuestion(paperId, pqId)` | 从试卷移除题目 |
| `assignClasses(paperId, classIds)` | 设置试卷关联班级 |
| `getPaperClassIds(paperId)` | 获取试卷关联的班级ID列表 |
| `canStudentAccessPaper(paperId, userId)` | 校验学生是否有权限参加考试 |

**班级过滤逻辑** (`page` 方法):
```java
if ("STUDENT".equals(role) && userId != null) {
    List<Long> userClassIds = classService.getUserClassIds(userId);
    if (userClassIds.isEmpty()) {
        // 学生不属于任何班级，返回空结果
        return emptyPage;
    }
    // 查询这些班级关联的试卷ID
    List<Long> paperIds = paperClassMapper...
    if (paperIds.isEmpty()) return emptyPage;
    wrapper.in(ExamPaper::getId, paperIds);
}
```

**班级权限校验** (`canStudentAccessPaper` 方法):
```java
public boolean canStudentAccessPaper(Long paperId, Long userId) {
    List<Long> paperClassIds = getPaperClassIds(paperId);
    if (paperClassIds.isEmpty()) return true; // 未指定班级则所有学生可访问
    List<Long> userClassIds = classService.getUserClassIds(userId);
    return paperClassIds.stream().anyMatch(userClassIds::contains);
}
```

#### 4.6.3 ExamPaperController.java - 试卷控制器

**路径**: `backend/src/main/java/com/smartexam/module/exam/controller/ExamPaperController.java`

**接口列表**:

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/papers` | 分页查询试卷 |
| GET | `/api/papers/{id}` | 获取试卷详情 |
| POST | `/api/papers` | 创建试卷 |
| POST | `/api/papers/generate` | 规则抽题组卷 |
| PUT | `/api/papers/{id}` | 更新试卷 |
| PUT | `/api/papers/{id}/publish` | 发布试卷 |
| DELETE | `/api/papers/{id}` | 删除试卷 |
| GET | `/api/papers/{paperId}/questions` | 获取试卷题目列表 |
| POST | `/api/papers/{paperId}/questions` | 向试卷添加题目 |
| PUT | `/api/papers/{paperId}/questions/{pqId}` | 更新试卷题目分值 |
| DELETE | `/api/papers/{paperId}/questions/{pqId}` | 从试卷移除题目 |
| PUT | `/api/papers/{id}/classes` | 设置试卷关联班级 |
| GET | `/api/papers/{id}/classes` | 获取试卷关联班级ID列表 |

#### 4.6.4 ExamService.java - 考试服务

**路径**: `backend/src/main/java/com/smartexam/module/exam/service/ExamService.java`

**核心方法**:

| 方法 | 说明 |
|------|------|
| `startExam(paperId)` | 开始考试，创建考试记录 |
| `getQuestionIds(recordId)` | 获取题目ID列表（用于答题卡） |
| `getQuestions(recordId, page, size)` | 获取试卷题目（分页） |
| `saveDraft(recordId, answers)` | 保存草稿到内存缓存 |
| `getDraft(recordId)` | 获取草稿 |
| `submitAnswer(recordId, questionId, answer)` | 提交单题答案 |
| `submitExam(recordId)` | 交卷 |

**startExam 流程**:
1. 校验试卷状态（必须是PUBLISHED）
2. 校验班级权限（调用 `canStudentAccessPaper`）
3. 校验考试时间（开始时间/结束时间）
4. 校验考试次数（不超过 maxAttempts）
5. 生成随机种子 `seed = System.nanoTime() + userId`
6. 查询组卷规则，执行抽题算法（或手动组卷）
7. 题目整体乱序（如果开启了 shuffleQuestion）
8. 构建题目顺序JSON和选项顺序JSON
9. 创建考试记录（exam_record）
10. 创建答题记录（exam_answer，每题一条）

**submitExam 流程**:
1. 校验考试记录状态（必须是IN_PROGRESS）
2. 从内存缓存同步草稿到数据库
3. 调用 `autoGradeObjectiveAnswers` 客观题自动判分
4. 更新记录状态为SUBMITTED
5. 清除内存缓存草稿

**autoGradeObjectiveAnswers 自动判分逻辑**:
```java
// 单选题: equalsIgnoreCase 比较
if ("SINGLE".equals(type)) {
    isCorrect = correctAnswer.equalsIgnoreCase(userAnswer.trim());
}
// 判断题: 标准化后比较（T/F → true/false）
else if ("JUDGE".equals(type)) {
    String userAns = normalizeJudgeAnswer(userAnswer);
    String correctAns = normalizeJudgeAnswer(correctAnswer);
    isCorrect = correctAns.equals(userAns);
}
// 多选题: 排序后比较
else if ("MULTI".equals(type)) {
    String sortedUser = sortAnswer(userAnswer);
    String sortedCorrect = sortAnswer(correctAnswer);
    isCorrect = sortedCorrect.equals(sortedUser);
}
```

#### 4.6.5 ExamController.java - 考试控制器

**路径**: `backend/src/main/java/com/smartexam/module/exam/controller/ExamController.java`

**接口列表**:

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/exam/start/{paperId}` | 开始考试 |
| GET | `/api/exam/records/{id}/question-ids` | 获取题目ID列表 |
| GET | `/api/exam/records/{id}/questions` | 获取试卷题目(分页) |
| POST | `/api/exam/records/{id}/answer` | 提交单题答案 |
| POST | `/api/exam/records/{id}/submit` | 交卷 |
| GET | `/api/exam/review/list` | 已批改试卷列表（试卷复查） |
| GET | `/api/exam/review/{recordId}` | 试卷复查详情 |
| POST | `/api/exam/draft` | 保存草稿 |
| GET | `/api/exam/draft/{recordId}` | 获取草稿 |

#### 4.6.6 QuestionSelector.java - 千人千卷抽题算法

**路径**: `backend/src/main/java/com/smartexam/module/exam/algorithm/QuestionSelector.java`

**作用**: 实现"千人千卷"防作弊的核心算法。

**算法流程**:
1. 解析规则配置JSON（每条规则包含 chapter, type, difficulty, count, score）
2. 对每条规则，查询3倍候选题目（`selectByRule`）
3. 使用 `Collections.shuffle(candidates, random)` 随机抽取目标数量
4. 所有规则的题目合并后整体乱序
5. 对每个题目的选项执行 Fisher-Yates 洗牌算法
6. 重新分配选项标识（A/B/C/D）并记录原始正确答案映射

**Fisher-Yates 洗牌算法**:
```java
private void shuffleOptions(List<QuestionOption> options, Random random) {
    for (int i = options.size() - 1; i > 0; i--) {
        int j = random.nextInt(i + 1);
        Collections.swap(options, i, j);
    }
}
```

**随机种子**: `seed = System.nanoTime() + userId`，保证每个学生的题目顺序和选项顺序都不同。

---

### 4.7 阅卷模块 (grading)

#### 4.7.1 GradingService.java - 阅卷服务

**路径**: `backend/src/main/java/com/smartexam/module/grading/service/GradingService.java`

**核心方法**:

| 方法 | 说明 |
|------|------|
| `getGradingPapers(pageNum, pageSize)` | 获取待阅卷试卷列表（按试卷分组统计） |
| `getPaperAnswers(paperId, pageNum, pageSize)` | 获取试卷答题列表 |
| `gradeAnswer(answerId, dto)` | 人工评分（覆盖AI分） |
| `triggerAiGrade(paperId)` | 触发AI批量阅卷 |
| `getPaperStudents(paperId)` | 获取试卷的学生提交列表 |
| `getRecordDetail(paperId, recordId)` | 获取答卷详情（题目+选项+答案） |
| `batchGrade(recordId, dto)` | 批量评分 |

**triggerAiGrade 流程**:
1. 查询试卷题目分值映射
2. 查询该试卷所有已提交的记录
3. 对每条记录，查询待批改的答案
4. 客观题（SINGLE/MULTI/JUDGE）：自动判分
5. 主观题（SUBJECTIVE/FILL）：调用 `aiGradingService.gradeSubjectiveAsync` 异步AI阅卷
6. 更新记录状态为GRADING

#### 4.7.2 GradingController.java

**路径**: `backend/src/main/java/com/smartexam/module/grading/controller/GradingController.java`

**接口列表**:

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/grading/papers` | 待阅卷试卷列表 |
| GET | `/api/grading/papers/{id}/answers` | 获取答题列表 |
| PUT | `/api/grading/answers/{id}` | 人工评分 |
| POST | `/api/grading/papers/{id}/ai-grade` | 触发AI批量阅卷 |
| GET | `/api/grading/papers/{id}/students` | 获取学生提交列表 |
| GET | `/api/grading/papers/{paperId}/records/{recordId}` | 获取答卷详情 |
| POST | `/api/grading/records/{recordId}/batch-grade` | 批量评分 |

#### 4.7.3 DTO 和 VO

**GradeAnswerDTO.java** - 人工评分请求
- `finalScore` (BigDecimal) - 最终分数

**BatchGradeDTO.java** - 批量评分请求
- `items` (List<GradeItem>) - 评分项列表
  - `answerId` (Long) - 答题记录ID
  - `finalScore` (BigDecimal) - 最终分数

**GradingPaperVO.java** - 待阅卷试卷视图
- paperId, paperTitle, subjectName, submitCount, pendingCount, gradedCount

**GradingPaperDetailVO.java** - 答卷详情视图
- recordId, userId, studentName, paperId, paperTitle, totalScore, studentScore, status, submitTime, questions (List<GradingQuestionItem>)

**GradingQuestionItem.java** - 题目详情项
- questionId, title, type, fullScore, referenceAnswer, explanation, options, answerId, userAnswer, autoScore, aiScore, aiComment, finalScore, gradingStatus, isCorrect

---

### 4.8 AI模块 (ai)

#### 4.8.1 AiGradingService.java - AI阅卷服务

**路径**: `backend/src/main/java/com/smartexam/module/ai/service/AiGradingService.java`

**作用**: 使用通义千问 DashScope API 进行主观题自动阅卷。

**配置项** (从 application-dev.yml 读取):
- `ali.qwen.api-key` - API密钥
- `ali.qwen.base-url` - API地址
- `ali.qwen.model` - 模型名称（dev: qwen-turbo, prod: qwen-plus）
- `ali.qwen.timeout` - 超时时间（15000ms）
- `ali.qwen.max-retries` - 最大重试次数（2次）

**核心方法**:

| 方法 | 说明 |
|------|------|
| `gradeSubjectiveAsync(answerId)` | 异步执行AI阅卷（`@Async("aiGradingExecutor")`） |
| `gradeSubjective(answerId)` | 执行单题AI阅卷 |

**gradeSubjective 流程**:
1. 获取答题记录和题目信息
2. 构建Prompt（系统提示词 + 用户提示词模板）
3. 调用 DashScope API（含重试逻辑，最多重试2次）
4. 解析返回的JSON（score, explanation, confidence）
5. 校验分数范围（0 ~ 满分）
6. 更新答题记录（aiScore, aiComment, finalScore, gradingStatus=AI_DONE）
7. 保存AI阅卷日志（ai_grading_log）
8. 如果调用失败，降级处理（0分 + 标记需人工复核）

**Prompt 设计**:
```
系统提示词: "你是一名严谨的大学专业课阅卷教师。请严格按照评分标准进行评分。"

用户提示词:
请根据【题目】、【参考答案/评分标准】与【学生作答】进行批改。

【题目】：{title}
【参考答案/评分标准】：{referenceAnswer}
【学生作答】：{studentAnswer}

请严格按照以下JSON格式返回，不要包含任何其他内容：
{"score": 分数, "explanation": "评分理由", "confidence": 置信度}

注意：
1. score为0到{maxScore}之间的数字（可以是小数）
2. confidence为0到1之间的数字，表示评分把握程度
3. explanation需详细说明给分理由和扣分点
```

**API调用方式**: 使用 Java HttpClient 调用 DashScope 的 OpenAI 兼容端点:
```
POST https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions
```

**降级策略**: 当AI调用失败（超时、网络错误、解析失败）时:
- aiScore 设为 0
- aiComment 设为 "AI阅卷失败，需人工复核"
- gradingStatus 保持 PENDING（等待教师人工批改）

#### 4.8.2 AiGradingLog.java - AI阅卷日志实体

**路径**: `backend/src/main/java/com/smartexam/module/ai/entity/AiGradingLog.java`

**字段**: id, answerId, questionId, modelName, prompt, response, aiScore, aiComment, confidence, tokenUsed, durationMs, status, errorMsg, retryCount, createTime

---

### 4.9 统计模块 (stats)

#### 4.9.1 StatsService.java - 统计服务

**路径**: `backend/src/main/java/com/smartexam/module/stats/service/StatsService.java`

**核心方法**:

| 方法 | 说明 |
|------|------|
| `getPaperReport(paperId)` | 获取试卷成绩报告 |
| `getPaperRank(paperId)` | 获取成绩排名 |
| `getStudentHistory(userId)` | 获取学生历史成绩 |

**getPaperReport 返回数据**:
- referenceCount - 参考人数
- passCount - 及格人数
- failCount - 不及格人数
- passRate - 及格率
- averageScore - 平均分
- maxScore - 最高分
- minScore - 最低分
- scoreDistribution - 分数分布（0-59, 60-69, 70-79, 80-89, 90-100）

#### 4.9.2 StatsController.java

**路径**: `backend/src/main/java/com/smartexam/module/stats/controller/StatsController.java`

**接口列表**:

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/stats/paper/{id}/report` | 试卷成绩报告 |
| GET | `/api/stats/paper/{id}/rank` | 成绩排名 |
| GET | `/api/stats/student/history` | 学生历史成绩 |

---

### 4.10 字典模块 (dict)

#### 4.10.1 SysDict.java - 字典实体

**路径**: `backend/src/main/java/com/smartexam/module/dict/entity/SysDict.java`

**字段**: id, type, code, name, sortOrder, status, remark, createTime, updateTime, deleted

**字典类型**:
- `CLASS` - 班级
- `MAJOR` - 专业
- `SUBJECT` - 科目
- `SEMESTER` - 学期

#### 4.10.2 DictService.java

**路径**: `backend/src/main/java/com/smartexam/module/dict/service/DictService.java`

**核心方法**:

| 方法 | 说明 |
|------|------|
| `page(keyword, type, pageNum, pageSize)` | 分页查询字典 |
| `getByType(type)` | 按类型查询字典列表 |
| `create(dict)` | 创建字典 |
| `update(dict)` | 更新字典 |
| `delete(id)` | 逻辑删除字典 |

#### 4.10.3 DictController.java

**路径**: `backend/src/main/java/com/smartexam/module/dict/controller/DictController.java`

**接口列表**:

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/dicts` | 分页查询字典 |
| GET | `/api/dicts/type/{type}` | 按类型查询 |
| POST | `/api/dicts` | 创建字典 |
| PUT | `/api/dicts/{id}` | 更新字典 |
| DELETE | `/api/dicts/{id}` | 删除字典 |

---

### 4.11 班级模块 (class_)

#### 4.11.1 SysClass.java - 班级实体

**路径**: `backend/src/main/java/com/smartexam/module/class_/entity/SysClass.java`

**字段**: id, name, description, createTime, updateTime, deleted

**瞬态字段**（`@TableField(exist = false)`）:
- teacherCount - 教师数量
- studentCount - 学生数量

#### 4.11.2 SysClassUser.java - 班级用户关联实体

**路径**: `backend/src/main/java/com/smartexam/module/class_/entity/SysClassUser.java`

**字段**: id, classId, userId, createTime

#### 4.11.3 ClassService.java - 班级服务

**路径**: `backend/src/main/java/com/smartexam/module/class_/service/ClassService.java`

**核心方法**:

| 方法 | 说明 |
|------|------|
| `page(keyword, pageNum, pageSize)` | 分页查询班级（含教师/学生数量） |
| `listAll()` | 获取所有班级列表 |
| `create(cls)` | 创建班级 |
| `update(cls)` | 更新班级 |
| `delete(id)` | 删除班级（同时删除关联） |
| `addClassUser(classId, userId)` | 为班级添加用户 |
| `removeClassUser(classId, userId)` | 移除班级用户 |
| `getClassUsers(classId, role)` | 获取班级下的用户（可按角色筛选） |
| `getAvailableUsers(role)` | 获取可添加的用户列表 |
| `getUserClassIds(userId)` | 获取用户所属班级ID列表 |

**fillCounts 方法**: 为班级列表填充教师/学生数量统计。

#### 4.11.4 ClassController.java

**路径**: `backend/src/main/java/com/smartexam/module/class_/controller/ClassController.java`

**接口列表**:

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/classes` | 分页查询班级 |
| GET | `/api/classes/all` | 获取所有班级（下拉选择用） |
| POST | `/api/classes` | 创建班级 |
| PUT | `/api/classes/{id}` | 更新班级 |
| DELETE | `/api/classes/{id}` | 删除班级 |
| GET | `/api/classes/{id}/users` | 获取班级成员 |
| POST | `/api/classes/{id}/users` | 添加成员到班级 |
| DELETE | `/api/classes/{id}/users/{userId}` | 从班级移除成员 |
| GET | `/api/classes/available-users` | 获取可添加的用户列表 |

---

### 4.12 工具类 (util)

#### 4.12.1 RedisUtil.java

**路径**: `backend/src/main/java/com/smartexam/util/RedisUtil.java`

**作用**: 内存缓存工具类（使用ConcurrentHashMap实现，替代Redis）。

**核心方法**:
- `set(key, value, timeout, timeUnit)` - 设置缓存
- `get(key)` - 获取缓存
- `delete(key)` - 删除缓存
- `hasKey(key)` - 判断key是否存在

---

## 5. 前端实现详解

### 5.1 入口文件 main.js

**路径**: `frontend/src/main.js`

**作用**: 应用入口，注册所有插件和全局组件。

**注册内容**:
- Element Plus（含中文语言包）
- 所有 Element Plus 图标（全局注册）
- Pinia 状态管理
- Vue Router
- 挂载到 `#app` DOM 节点

### 5.2 根组件 App.vue

**路径**: `frontend/src/App.vue`

**作用**: 根组件，仅包含 `<router-view />`，定义全局 CSS 样式重置。

### 5.3 路由配置 router/index.js

**路径**: `frontend/src/router/index.js`

**路由表**:

| 路径 | 组件 | 角色 | 说明 |
|------|------|------|------|
| `/login` | login/index.vue | 无 | 登录页 |
| `/student/dashboard` | student/Dashboard.vue | STUDENT | 学生首页 |
| `/student/exam-hall` | student/ExamHall.vue | STUDENT | 考试大厅 |
| `/student/exam-room/:recordId` | student/ExamRoom.vue | STUDENT | 答题页 |
| `/student/history` | student/History.vue | STUDENT | 历史成绩 |
| `/student/exam-review` | student/ErrorBook.vue | STUDENT | 试卷复查 |
| `/teacher/question-bank` | teacher/QuestionBank.vue | TEACHER | 题库管理 |
| `/teacher/paper-manage` | teacher/PaperManage.vue | TEACHER | 试卷管理 |
| `/teacher/grading` | teacher/GradingCenter.vue | TEACHER | 阅卷中心 |
| `/teacher/data-board` | teacher/DataBoard.vue | TEACHER | 数据看板 |
| `/admin/user-manage` | admin/UserManage.vue | ADMIN | 用户管理 |
| `/admin/class-manage` | admin/ClassManage.vue | ADMIN | 班级管理 |

**路由守卫逻辑**:
1. 不需要登录的页面：已登录则跳转到对应角色首页，未登录则放行
2. 需要登录但未登录：跳转到 `/login`
3. 需要特定角色但角色不匹配：跳转到当前用户角色的首页
4. 使用 NProgress 显示页面切换进度条

### 5.4 API 层 (api/)

#### 5.4.1 request.js - Axios 实例

**路径**: `frontend/src/api/request.js`

**作用**: 创建统一的 HTTP 客户端实例。

**配置**:
- baseURL: `/api`
- timeout: 30000ms
- Content-Type: `application/json`

**请求拦截器**:
- 自动从 userStore 读取 token
- 在请求头中添加 `Authorization: Bearer {token}`

**响应拦截器**:
- 成功响应（code=200）：直接返回 `res`（即 `{code, message, data}` 对象）
- 业务错误：弹出 `ElMessage.error` 提示
- HTTP 401：弹出重新登录对话框
- HTTP 403：提示"无权限访问"
- HTTP 500：显示服务器错误消息
- 网络断开：提示"网络已断开"，将请求加入离线队列
- 网络恢复：自动重传离线队列中的请求

#### 5.4.2 auth.js - 认证 API

**路径**: `frontend/src/api/auth.js`

| 函数 | 方法 | 路径 | 说明 |
|------|------|------|------|
| `login(data)` | POST | `/auth/login` | 用户登录 |
| `logout()` | POST | `/auth/logout` | 退出登录 |
| `getUserInfo()` | GET | `/auth/info` | 获取用户信息 |

#### 5.4.3 user.js - 用户 API

**路径**: `frontend/src/api/user.js`

| 函数 | 方法 | 路径 | 说明 |
|------|------|------|------|
| `getUserPage(params)` | GET | `/users` | 分页查询 |
| `createUser(data)` | POST | `/users` | 创建用户 |
| `updateUser(id, data)` | PUT | `/users/{id}` | 更新用户 |
| `deleteUser(id)` | DELETE | `/users/{id}` | 删除用户 |
| `resetPassword(id)` | PUT | `/users/{id}/reset-password` | 重置密码 |
| `toggleUserStatus(id)` | PUT | `/users/{id}/toggle-status` | 冻结/解冻 |
| `importUsers(file, role)` | POST | `/users/import` | Excel导入 |
| `updateProfile(data)` | PUT | `/users/profile` | 修改个人信息 |

#### 5.4.4 question.js - 题库 API

**路径**: `frontend/src/api/question.js`

| 函数 | 方法 | 路径 | 说明 |
|------|------|------|------|
| `getQuestionPage(params)` | GET | `/questions` | 分页查询 |
| `getQuestionDetail(id)` | GET | `/questions/{id}` | 题目详情 |
| `createQuestion(data)` | POST | `/questions` | 创建题目 |
| `updateQuestion(id, data)` | PUT | `/questions/{id}` | 更新题目 |
| `deleteQuestion(id)` | DELETE | `/questions/{id}` | 删除题目 |
| `importQuestions(file, subjectId)` | POST | `/questions/import` | Excel导入 |

#### 5.4.5 paper.js - 试卷 API

**路径**: `frontend/src/api/paper.js`

| 函数 | 方法 | 路径 | 说明 |
|------|------|------|------|
| `getPaperPage(params)` | GET | `/papers` | 分页查询 |
| `getPaperDetail(id)` | GET | `/papers/{id}` | 试卷详情 |
| `createPaper(data)` | POST | `/papers` | 创建试卷 |
| `generatePaper(data, ruleConfig)` | POST | `/papers/generate` | 规则组卷 |
| `updatePaper(id, data)` | PUT | `/papers/{id}` | 更新试卷 |
| `publishPaper(id)` | PUT | `/papers/{id}/publish` | 发布试卷 |
| `deletePaper(id)` | DELETE | `/papers/{id}` | 删除试卷 |
| `getPaperQuestions(paperId)` | GET | `/papers/{paperId}/questions` | 试卷题目 |
| `addPaperQuestion(paperId, questionId, score)` | POST | `/papers/{paperId}/questions` | 添加题目 |
| `updatePaperQuestion(paperId, pqId, score)` | PUT | `/papers/{paperId}/questions/{pqId}` | 更新分值 |
| `removePaperQuestion(paperId, pqId)` | DELETE | `/papers/{paperId}/questions/{pqId}` | 移除题目 |
| `assignClasses(paperId, classIds)` | PUT | `/papers/{paperId}/classes` | 设置班级 |
| `getPaperClassIds(paperId)` | GET | `/papers/{paperId}/classes` | 获取班级 |
| `getGradingPapers(params)` | GET | `/grading/papers` | 待阅卷列表 |
| `getPaperStudents(paperId)` | GET | `/grading/papers/{id}/students` | 学生列表 |
| `getRecordDetail(paperId, recordId)` | GET | `/grading/papers/{paperId}/records/{recordId}` | 答卷详情 |
| `batchGrade(recordId, data)` | POST | `/grading/records/{recordId}/batch-grade` | 批量评分 |
| `triggerAiGrade(paperId)` | POST | `/grading/papers/{id}/ai-grade` | AI阅卷 |

#### 5.4.6 exam.js - 考试 API

**路径**: `frontend/src/api/exam.js`

| 函数 | 方法 | 路径 | 说明 |
|------|------|------|------|
| `startExam(paperId)` | POST | `/exam/start/{paperId}` | 开始考试 |
| `getExamQuestionIds(recordId)` | GET | `/exam/records/{id}/question-ids` | 题目ID列表 |
| `getExamQuestions(recordId, page, size)` | GET | `/exam/records/{id}/questions` | 分页题目 |
| `submitAnswer(recordId, questionId, answer)` | POST | `/exam/records/{id}/answer` | 提交答案 |
| `submitExam(recordId)` | POST | `/exam/records/{id}/submit` | 交卷 |
| `getReviewList()` | GET | `/exam/review/list` | 已批改试卷列表 |
| `getReviewDetail(recordId)` | GET | `/exam/review/{recordId}` | 试卷复查详情 |
| `saveDraft(data)` | POST | `/exam/draft` | 保存草稿 |
| `getDraft(recordId)` | GET | `/exam/draft/{recordId}` | 获取草稿 |
| `checkAnswer(questionId, answer)` | POST | `/practice/check` | 即时判分 |
| `getErrorBook(params)` | GET | `/practice/error-book` | 错题本 |
| `reviewError(id)` | POST | `/practice/review/{id}` | 错题复习 |

#### 5.4.7 stats.js - 统计 API

**路径**: `frontend/src/api/stats.js`

| 函数 | 方法 | 路径 | 说明 |
|------|------|------|------|
| `getPaperReport(paperId)` | GET | `/stats/paper/{id}/report` | 成绩报告 |
| `getPaperRank(paperId)` | GET | `/stats/paper/{id}/rank` | 成绩排名 |
| `getStudentHistory()` | GET | `/stats/student/history` | 历史成绩 |

#### 5.4.8 dict.js - 字典 API

**路径**: `frontend/src/api/dict.js`

| 函数 | 方法 | 路径 | 说明 |
|------|------|------|------|
| `getDictPage(params)` | GET | `/dicts` | 分页查询 |
| `getDictByType(type)` | GET | `/dicts/type/{type}` | 按类型查询 |
| `createDict(data)` | POST | `/dicts` | 创建字典 |
| `updateDict(id, data)` | PUT | `/dicts/{id}` | 更新字典 |
| `deleteDict(id)` | DELETE | `/dicts/{id}` | 删除字典 |

#### 5.4.9 class.js - 班级 API

**路径**: `frontend/src/api/class.js`

| 函数 | 方法 | 路径 | 说明 |
|------|------|------|------|
| `getClassPage(params)` | GET | `/classes` | 分页查询 |
| `getClassAll()` | GET | `/classes/all` | 所有班级 |
| `createClass(data)` | POST | `/classes` | 创建班级 |
| `updateClass(id, data)` | PUT | `/classes/{id}` | 更新班级 |
| `deleteClass(id)` | DELETE | `/classes/{id}` | 删除班级 |
| `getClassUsers(classId, params)` | GET | `/classes/{id}/users` | 班级成员 |
| `addClassUser(classId, data)` | POST | `/classes/{id}/users` | 添加成员 |
| `removeClassUser(classId, userId)` | DELETE | `/classes/{id}/users/{userId}` | 移除成员 |
| `getAvailableUsers(params)` | GET | `/classes/available-users` | 可添加用户 |

### 5.5 状态管理 (store/)

#### 5.5.1 user.js - 用户状态

**路径**: `frontend/src/store/user.js`

**状态**:
- `token` (ref) - JWT Token，持久化到 localStorage
- `userInfo` (ref) - 用户信息 {userId, role, realName}，持久化到 localStorage

**计算属性**:
- `isLoggedIn` - 是否已登录
- `role` - 用户角色
- `realName` - 真实姓名

**Actions**:
- `login(loginData)` - 调用登录API，保存token和用户信息到localStorage
- `logout()` - 调用登出API，清除本地状态，跳转到登录页
- `fetchUserInfo()` - 刷新用户信息

#### 5.5.2 exam.js - 考试状态

**路径**: `frontend/src/store/exam.js`

**状态**:
- `recordId` - 考试记录ID
- `paperTitle` - 试卷标题
- `duration` - 考试时长（分钟）
- `totalScore` - 试卷总分
- `startTime` / `endTime` - 开始/结束时间
- `questionCount` - 题目总数
- `questions` - 当前页题目列表
- `questionIds` - 所有题目ID列表（用于答题卡）
- `answers` - 答案映射 {questionId: answer}
- `currentPage` / `pageSize` - 分页状态
- `remainingTime` - 剩余时间（秒）
- `timer` - 倒计时定时器

**计算属性**:
- `isFinished` - 考试是否结束
- `answeredCount` - 已答题数

**Actions**:
- `start(paperId)` - 开始考试，初始化所有状态
- `loadExam(recordId)` - 恢复考试状态
- `loadQuestions(page)` - 加载指定页题目
- `goToQuestion(index)` - 跳转到指定题号
- `setAnswer(questionId, answer)` - 设置答案
- `submitSingleAnswer(questionId)` - 提交单题答案到服务器
- `submit()` - 交卷
- `startTimer()` - 启动倒计时
- `saveDraft()` - 保存草稿到内存缓存（每30秒自动执行）
- `cleanup()` - 清理定时器和状态

### 5.6 工具函数 (utils/)

#### 5.6.1 format.js - 格式化工具

**路径**: `frontend/src/utils/format.js`

| 函数 | 说明 | 示例 |
|------|------|------|
| `formatDateTime(dateStr)` | 格式化日期时间 | `2024-01-15 14:30:00` |
| `formatDate(dateStr)` | 格式化日期 | `2024-01-15` |
| `formatSeconds(seconds)` | 秒转时分秒 | `01:30:00` |
| `formatFileSize(bytes)` | 文件大小 | `1.5 MB` |
| `formatRole(role)` | 角色转中文 | STUDENT→学生 |
| `formatQuestionType(type)` | 题型转中文 | SINGLE→单选题 |
| `formatDifficulty(level)` | 难度转中文 | 3→中等 |

#### 5.6.2 storage.js - 存储工具

**路径**: `frontend/src/utils/storage.js`

| 函数 | 说明 |
|------|------|
| `setStorage(key, value)` | 设置localStorage（自动加前缀`smart_exam_`） |
| `getStorage(key)` | 获取localStorage |
| `removeStorage(key)` | 删除localStorage |
| `clearStorage()` | 清空所有带前缀的localStorage |

### 5.7 页面组件 (views/)

#### 5.7.1 登录页 login/index.vue

**路径**: `frontend/src/views/login/index.vue`

**功能**:
- 居中卡片式登录表单，渐变背景
- 用户名/密码输入框，带验证规则
- 登录按钮（带loading状态）
- 显示测试账号信息（t1/123456 教师，s1/123456 学生）
- 登录成功后根据角色跳转到对应首页

#### 5.7.2 学生端页面

**student/Layout.vue** - 学生端布局
- 可折叠侧边栏，4个菜单项：个人中心、考试大厅、历史成绩、试卷复查
- 顶部导航栏：折叠按钮、面包屑、用户下拉菜单

**student/Dashboard.vue** - 学生首页
- 欢迎卡片（显示学生姓名）
- 快捷入口按钮（考试大厅、历史成绩）
- 最近5条考试成绩表格

**student/ExamHall.vue** - 考试大厅
- 分页表格显示可用试卷（标题、科目、总分、时长、考试时间、状态）
- 状态筛选下拉框（带防抖）
- "开始考试"按钮（已发布试卷可用），点击后确认并跳转到答题页

**student/ExamRoom.vue** - 答题页（最复杂的前端页面）
- 三栏布局：
  - 顶部：试卷标题、倒计时（<5分钟闪烁警告）、已答题数、交卷按钮
  - 右侧：答题卡（题号网格，已答绿色、当前蓝色）
  - 左侧：题目显示区（题型标签、分值、难度、题干HTML渲染、答题控件）
- 支持5种题型的答题控件：
  - 单选题：el-radio-group
  - 多选题：el-checkbox-group
  - 判断题：el-radio-group（正确/错误）
  - 填空题：el-input textarea
  - 主观题：el-input textarea
- 导航：点击答题卡、上一题/下一题按钮
- 自动提交：切换题目时自动提交当前答案
- 超时自动交卷

**student/History.vue** - 历史成绩
- 表格显示：试卷标题、得分（<60红色高亮）、提交时间

**student/ErrorBook.vue** - 试卷复查
- 已批改试卷列表（试卷标题、科目、得分、提交时间）
- 点击"查看详情"打开弹窗：左侧题目导航（颜色标注对/错），右侧完整试卷详情（只读）

#### 5.7.3 教师端页面

**teacher/Layout.vue** - 教师端布局
- 可折叠侧边栏，4个菜单项：题库管理、试卷管理、阅卷中心、数据看板

**teacher/QuestionBank.vue** - 题库管理
- 可筛选表格（题型、难度、关键词，带防抖）
- 创建/编辑对话框：
  - 题型选择器
  - 题干文本域
  - "一键识别选项"按钮（正则解析 A. B. C. D. 格式）
  - 难度评分（el-rate）
  - 分值输入
  - 动态选项列表（2-8个选项）
  - 题型相关答案输入（单选radio、多选checkbox、判断radio、填空/主观textarea）
  - 解析字段
- 查看详情对话框（el-descriptions）
- Excel批量导入

**teacher/PaperManage.vue** - 试卷管理（最复杂的教师页面）
- 试卷列表：标题、总分、时长、考试时间、状态、操作
- 创建/编辑对话框：
  - 试卷标题、科目、时长、总分、及格分、可考次数
  - 考试时间范围选择器
  - 乱序题目/选项开关
  - 指定班级（el-select multiple，从班级列表加载）
- 题目管理抽屉（70%宽度）：
  - 题目列表（序号、题ID、题干、题型、参考答案、难度、分值）
  - 分值可内联编辑（el-input-number）
  - 移除题目按钮
- 从题库添加题目对话框：
  - 搜索/筛选表单
  - 多选表格
  - 每题分值设置
  - 批量添加按钮
- 新增/编辑题目对话框（集成在试卷上下文中）

**teacher/GradingCenter.vue** - 阅卷中心
- 试卷列表：显示待阅卷/已阅卷数量
- "批改答卷"按钮打开大对话框（1200px, 70vh）：
  - 左侧：学生列表（显示批改状态）
  - 右侧：完整试卷展示
    - 每题显示：题型标签、分值、批改状态（自动/AI/教师）
    - 题干、选项（正确答案高亮）、学生答案、参考答案、解析
    - 客观题：自动评分（只读）
    - 主观题：AI评分/评语（如有）+ 人工评分输入框（步长0.5）
  - 批量提交评分按钮
- "AI阅卷"按钮：触发整张试卷的AI批改

**teacher/DataBoard.vue** - 数据看板
- 试卷选择下拉框
- 7个统计卡片：参考人数、及格人数、不及格人数、及格率、平均分、最高分、最低分
- 两个ECharts图表：
  - 饼图：及格/不及格比例
  - 柱状图：分数分布（渐变色）
- 图表自适应窗口大小变化

#### 5.7.4 管理员端页面

**admin/Layout.vue** - 管理员端布局
- 可折叠侧边栏，2个菜单项：用户管理、班级管理

**admin/UserManage.vue** - 用户管理
- 可筛选表格（角色、关键词，带防抖）
- 操作：编辑、重置密码（改为123456）、冻结/解冻、删除
- 创建/编辑对话框：用户名、姓名、角色、密码（仅创建时）、邮箱、手机
- Excel批量导入学生账号

**admin/ClassManage.vue** - 班级管理
- 分页表格：ID、班级名称、描述、教师数、学生数
- 创建/编辑对话框：名称、描述
- 成员管理抽屉（60%宽度）：
  - 角色筛选（全部/教师/学生）
  - 成员列表表格（用户ID、用户名、姓名、角色标签）
  - 移除成员按钮
- 添加成员对话框：
  - 角色筛选下拉框
  - 搜索按钮
  - 可选用户表格（多选）
  - 批量添加按钮

---

## 6. 核心业务流程

### 6.1 考试流程

```
学生进入考试大厅
    │
    ▼
选择试卷 → POST /api/exam/start/{paperId}
    │
    ├── 校验试卷状态（必须PUBLISHED）
    ├── 校验班级权限（学生必须属于指定班级）
    ├── 校验考试时间（在开始和结束时间之间）
    ├── 校验考试次数（不超过maxAttempts）
    │
    ▼
后端执行抽题算法（千人千卷）
    ├── 1. 按规则查询3倍候选题
    ├── 2. Collections.shuffle() 截取
    ├── 3. 题目整体乱序
    ├── 4. 选项 Fisher-Yates 乱序
    └── 5. 生成 exam_record + exam_answer
    │
    ▼
前端 Pinia 存储试卷状态
    │
    ▼
答题页面加载
    ├── 加载题目（分页，每页1题）
    ├── 启动倒计时 setInterval
    └── 启动草稿定时保存（每30秒）
    │
    ▼
学生答题
    ├── 切题时自动提交当前答案
    ├── 草稿保存到内存缓存
    └── 断网时 localStorage 缓存
    │
    ▼
交卷 → POST /api/exam/records/{id}/submit
    │
    ▼
后端处理
    ├── 同步内存缓存草稿到数据库
    ├── 客观题自动判分
    │   ├── 单选: equalsIgnoreCase
    │   ├── 判断: normalizeJudgeAnswer（T/F → true/false）
    │   └── 多选: sortAnswer 排序后比较
    ├── 更新记录状态为 SUBMITTED
    └── 清除内存缓存草稿
```

### 6.2 AI阅卷流程

```
教师点击"AI阅卷" → POST /api/grading/papers/{id}/ai-grade
    │
    ▼
查询试卷所有已提交记录
    │
    ▼
对每条记录的待批改答案：
    ├── 客观题 → 自动判分（同上）
    └── 主观题 → 异步AI阅卷
        │
        ▼
    构建 Prompt
        ├── 系统角色: "严谨的大学专业课阅卷教师"
        ├── 题目内容
        ├── 参考答案/评分标准
        └── 学生作答
        │
        ▼
    调用 DashScope API
        ├── 端点: dashscope.aliyuncs.com/compatible-mode/v1/chat/completions
        ├── 模型: qwen-turbo (dev) / qwen-plus (prod)
        ├── 超时: 15s
        ├── 重试: 最多2次
        └── 温度: 0.1（低随机性）
        │
        ▼
    解析返回 JSON
        ├── score: 分数（校验 0~满分）
        ├── explanation: 评分理由
        └── confidence: 置信度（0-1）
        │
        ▼
    写入数据库
        ├── exam_answer.ai_score
        ├── exam_answer.ai_comment
        ├── exam_answer.final_score
        ├── exam_answer.grading_status = AI_DONE
        └── ai_grading_log（完整调用记录）
        │
        ▼
    失败降级
        ├── ai_score = 0
        ├── ai_comment = "AI阅卷失败，需人工复核"
        └── grading_status = PENDING
```

### 6.3 班级管理与试卷指定流程

```
管理员创建班级
    │
    ▼
管理员为班级添加教师和学生
    │
    ▼
教师创建/编辑试卷 → 指定班级（多选）
    │
    ▼
教师发布试卷
    │
    ▼
学生登录 → 考试大厅
    │
    ├── 后端查询学生所属班级
    ├── 查询班级关联的试卷ID列表
    └── 只返回这些试卷
    │
    ▼
学生开始考试
    │
    └── 后端校验：学生班级 ∩ 试卷指定班级 ≠ ∅
```

---

## 7. 配置文件

### 7.1 application.yml（核心配置）

```yaml
spring:
  profiles:
    active: dev
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: GMT+8

mybatis-plus:
  mapper-locations: classpath*:/mapper/**/*.xml
  global-config:
    db-config:
      id-type: auto              # 主键自增
      logic-delete-field: deleted # 逻辑删除字段
      logic-delete-value: 1
      logic-not-delete-value: 0
  configuration:
    map-underscore-to-camel-case: true  # 下划线转驼峰

sa-token:
  token-name: Authorization
  token-prefix: Bearer
  timeout: 86400          # Token有效期24小时
  active-timeout: 7200    # 活跃超时2小时
  is-concurrent: true     # 允许并发登录
  is-share: true          # 共享Token
  token-style: uuid       # Token风格
```

### 7.2 application-dev.yml（开发环境）

```yaml
server:
  port: 8081

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/smart_exam
    username: root
    password: ${DB_PASSWORD:123456}

ali:
  qwen:
    api-key: ${QWEN_API_KEY:sk-xxx}
    model: qwen-turbo
    timeout: 15000
    max-retries: 2

draft:
  prefix: "exam:draft:"
  ttl: 86400
```

---

## 8. 启动与运行

### 8.1 环境要求

- JDK 17+
- Node.js 18+
- MySQL 8.0+
- Maven 3.8+

### 8.2 数据库初始化

```bash
# 执行 SQL 脚本
mysql -u root -p < sql/init.sql
```

### 8.3 启动后端

```bash
cd backend
mvn spring-boot:run
# 或
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

后端运行在 http://localhost:8081

### 8.4 启动前端

```bash
cd frontend
npm install
npm run dev
```

前端运行在 http://localhost:3000

### 8.5 API 文档

启动后端后访问: http://localhost:8081/swagger-ui.html

---

## 9. 完整文件清单

### 9.1 后端文件（76个Java文件）

```
backend/src/main/java/com/smartexam/
├── SmartExamApplication.java                    # 启动类
├── common/
│   ├── result/
│   │   ├── ResultVO.java                        # 统一响应封装
│   │   └── ResultCode.java                      # 错误码枚举
│   ├── exception/
│   │   ├── BizException.java                    # 业务异常
│   │   └── GlobalExceptionHandler.java          # 全局异常处理
│   └── config/
│       ├── CorsConfig.java                      # 跨域配置
│       ├── SaTokenConfig.java                   # Sa-Token鉴权
│       ├── MyBatisPlusConfig.java               # MyBatis-Plus配置
│       └── AsyncConfig.java                     # 异步线程池
├── module/
│   ├── auth/
│   │   ├── controller/AuthController.java       # 认证控制器
│   │   ├── service/AuthService.java             # 认证服务
│   │   └── dto/LoginDTO.java                    # 登录DTO
│   ├── user/
│   │   ├── entity/SysUser.java                  # 用户实体
│   │   ├── mapper/SysUserMapper.java            # 用户Mapper
│   │   ├── service/UserService.java             # 用户服务
│   │   └── controller/UserController.java       # 用户控制器
│   ├── question/
│   │   ├── entity/Question.java                 # 题目实体
│   │   ├── entity/QuestionOption.java           # 选项实体
│   │   ├── mapper/QuestionMapper.java           # 题目Mapper
│   │   ├── mapper/QuestionOptionMapper.java     # 选项Mapper
│   │   ├── service/QuestionService.java         # 题库服务
│   │   └── controller/QuestionController.java   # 题库控制器
│   ├── exam/
│   │   ├── entity/ExamPaper.java                # 试卷实体
│   │   ├── entity/ExamPaperQuestion.java        # 试卷题目关联
│   │   ├── entity/ExamRule.java                 # 组卷规则
│   │   ├── entity/ExamRecord.java               # 考试记录
│   │   ├── entity/ExamAnswer.java               # 答题记录
│   │   ├── entity/ExamPaperClass.java           # 试卷班级关联
│   │   ├── mapper/ExamPaperMapper.java
│   │   ├── mapper/ExamPaperQuestionMapper.java
│   │   ├── mapper/ExamRuleMapper.java
│   │   ├── mapper/ExamRecordMapper.java
│   │   ├── mapper/ExamAnswerMapper.java
│   │   ├── mapper/ExamPaperClassMapper.java
│   │   ├── algorithm/QuestionSelector.java      # 千人千卷抽题算法
│   │   ├── service/ExamPaperService.java        # 试卷服务
│   │   ├── service/ExamService.java             # 考试服务
│   │   ├── controller/ExamPaperController.java  # 试卷控制器
│   │   └── controller/ExamController.java       # 考试控制器
│   ├── grading/
│   │   ├── service/GradingService.java          # 阅卷服务
│   │   ├── controller/GradingController.java    # 阅卷控制器
│   │   ├── dto/GradeAnswerDTO.java              # 人工评分DTO
│   │   ├── dto/BatchGradeDTO.java               # 批量评分DTO
│   │   ├── vo/GradingPaperVO.java               # 待阅卷试卷VO
│   │   ├── vo/GradingPaperDetailVO.java         # 答卷详情VO
│   │   └── vo/GradingQuestionItem.java          # 题目详情项
│   ├── stats/
│   │   ├── service/StatsService.java            # 统计服务
│   │   └── controller/StatsController.java      # 统计控制器
│   ├── ai/
│   │   ├── entity/AiGradingLog.java             # AI日志实体
│   │   ├── mapper/AiGradingLogMapper.java       # AI日志Mapper
│   │   └── service/AiGradingService.java        # AI阅卷服务
│   ├── dict/
│   │   ├── entity/SysDict.java                  # 字典实体
│   │   ├── mapper/SysDictMapper.java            # 字典Mapper
│   │   ├── service/DictService.java             # 字典服务
│   │   └── controller/DictController.java       # 字典控制器
│   └── class_/
│       ├── entity/SysClass.java                 # 班级实体
│       ├── entity/SysClassUser.java             # 班级用户关联
│       ├── mapper/SysClassMapper.java           # 班级Mapper
│       ├── mapper/SysClassUserMapper.java       # 班级用户Mapper
│       ├── service/ClassService.java            # 班级服务
│       └── controller/ClassController.java      # 班级控制器
└── util/
    └── RedisUtil.java                           # 内存缓存工具类
```

### 9.2 前端文件（32个文件）

```
frontend/src/
├── main.js                                      # 入口文件
├── App.vue                                      # 根组件
├── router/index.js                              # 路由配置
├── store/
│   ├── user.js                                  # 用户状态
│   └── exam.js                                  # 考试状态
├── api/
│   ├── request.js                               # Axios实例
│   ├── auth.js                                  # 认证API
│   ├── user.js                                  # 用户API
│   ├── question.js                              # 题库API
│   ├── paper.js                                 # 试卷API
│   ├── exam.js                                  # 考试API
│   ├── stats.js                                 # 统计API
│   ├── dict.js                                  # 字典API
│   └── class.js                                 # 班级API
├── utils/
│   ├── format.js                                # 格式化工具
│   └── storage.js                               # 存储工具
└── views/
    ├── login/index.vue                          # 登录页
    ├── student/
    │   ├── Layout.vue                           # 学生端布局
    │   ├── Dashboard.vue                        # 学生首页
    │   ├── ExamHall.vue                         # 考试大厅
    │   ├── ExamRoom.vue                         # 答题页
    │   ├── History.vue                          # 历史成绩
    │   └── ErrorBook.vue                        # 试卷复查
    ├── teacher/
    │   ├── Layout.vue                           # 教师端布局
    │   ├── QuestionBank.vue                     # 题库管理
    │   ├── PaperManage.vue                      # 试卷管理
    │   ├── GradingCenter.vue                    # 阅卷中心
    │   └── DataBoard.vue                        # 数据看板
    └── admin/
        ├── Layout.vue                           # 管理员端布局
        ├── UserManage.vue                       # 用户管理
        ├── ClassManage.vue                      # 班级管理
        └── DictManage.vue                       # 字典管理(已弃用)
```

### 9.3 SQL 文件

```
sql/
└── init.sql                                     # 数据库初始化脚本（14张表+种子数据）
```

---

## 10. 逐文件详细文档

> 以下逐文件记录每个源码文件的完整内容、结构、方法和作用。

---

### 10.1 `backend/pom.xml` — Maven 项目配置

**路径**: `backend/pom.xml`
**作用**: 定义后端项目的依赖、构建配置和版本管理。

**父项目**: `spring-boot-starter-parent:3.2.5`

**核心依赖**:
| 依赖 | 版本 | 用途 |
|------|------|------|
| spring-boot-starter-web | (继承) | Web 服务 |
| spring-boot-starter-validation | (继承) | 参数校验 |
| spring-boot-starter-security | (继承) | BCrypt 密码加密 |
| mybatis-plus-spring-boot3-starter | 3.5.6 | ORM 框架 |
| mysql-connector-j | (继承) | MySQL 驱动 |
| sa-token-spring-boot3-starter | 1.38.0 | JWT 鉴权 |
| easyexcel | 3.3.4 | Excel 导入导出 |
| dashscope-sdk-java | 2.16.1 | 通义千问 AI SDK |
| springdoc-openapi-starter-webmvc-ui | 2.5.0 | Swagger API 文档 |
| hutool-all | 5.8.26 | Java 工具包 |
| lombok | (继承) | 简化代码 |

**构建配置**: 使用 `spring-boot-maven-plugin`，排除 Lombok。

---

### 10.2 `backend/src/main/resources/application.yml` — 核心配置文件

**路径**: `backend/src/main/resources/application.yml`
**作用**: Spring Boot 公共配置，被 dev/prod profile 继承。

**配置内容**:
- **Jackson**: 日期格式 `yyyy-MM-dd HH:mm:ss`，时区 GMT+8，忽略 null 字段
- **MyBatis-Plus**: mapper 路径 `classpath*:/mapper/**/*.xml`，实体别名包 `com.smartexam.module.*.entity`，主键自增，逻辑删除字段 `deleted`（0=未删，1=已删），驼峰映射开启，SQL 日志输出到控制台
- **Sa-Token**: Token 名 `Authorization`，前缀 `Bearer`，有效期 86400s（24h），活跃超时 7200s（2h），允许并发登录，共享 Token，UUID 格式
- **SpringDoc**: API 文档路径 `/v3/api-docs`，Swagger UI `/swagger-ui.html`，按模块分组（认证、用户、题库、试卷、考试、阅卷、统计、字典）
- **日志**: `com.smartexam` 包 debug 级别，Spring Web info 级别

---

### 10.3 `backend/src/main/resources/application-dev.yml` — 开发环境配置

**路径**: `backend/src/main/resources/application-dev.yml`
**作用**: 开发环境专用配置，不需要 Redis。

**配置内容**:
- **服务端口**: 8081
- **数据库**: MySQL `smart_exam`，用户名 root，密码从环境变量 `DB_PASSWORD` 读取，默认 `123456`
- **通义千问**: API Key 从 `QWEN_API_KEY` 环境变量读取，使用 `qwen-turbo` 模型，超时 15s，重试 2 次
- **文件上传**: 路径 `./uploads`，最大 10MB，允许 xlsx/xls
- **草稿配置**: 前缀 `exam:draft:`，TTL 86400s（改用数据库存储）
- **Sa-Token**: 内存模式（同 application.yml）
- **日志**: com.smartexam debug，Spring Web debug

---

### 10.4 `backend/src/main/resources/application-prod.yml` — 生产环境配置

**路径**: `backend/src/main/resources/application-prod.yml`
**作用**: 生产环境配置，启用 Redis。

**配置内容**:
- **服务端口**: 8080
- **数据库**: 从环境变量读取 host/port/username/password
- **Redis**: Lettuce 连接池，最大活跃 16，最大空闲 8，最小空闲 2
- **通义千问**: 使用 `qwen-plus` 模型（更强）
- **文件上传**: 路径 `/data/smart-exam/uploads`
- **日志**: info 级别，输出到 `/data/smart-exam/logs/app.log`

---

### 10.5 `frontend/package.json` — 前端项目配置

**路径**: `frontend/package.json`
**作用**: 定义前端项目的依赖和脚本。

**运行脚本**:
- `npm run dev` → `vite`（开发服务器）
- `npm run build` → `vite build`（生产构建）
- `npm run preview` → `vite preview`（预览构建产物）

**运行时依赖**:
| 依赖 | 版本 | 用途 |
|------|------|------|
| vue | ^3.4.21 | 前端框架 |
| vue-router | ^4.3.0 | 路由管理 |
| pinia | ^2.1.7 | 状态管理 |
| axios | ^1.6.8 | HTTP 请求 |
| element-plus | ^2.7.0 | UI 组件库 |
| @element-plus/icons-vue | ^2.3.1 | 图标库 |
| echarts | ^5.5.0 | 图表可视化 |
| lodash-es | ^4.17.21 | 工具函数 |
| nprogress | ^0.2.0 | 进度条 |

**开发依赖**:
| 依赖 | 版本 | 用途 |
|------|------|------|
| @vitejs/plugin-vue | ^5.0.4 | Vite Vue 插件 |
| vite | ^5.2.0 | 构建工具 |
| sass | ^1.72.0 | CSS 预处理器 |
| unplugin-auto-import | ^0.17.5 | 自动导入 API |
| unplugin-vue-components | ^0.26.0 | 自动注册组件 |

---

### 10.6 `frontend/vite.config.js` — Vite 构建配置

**路径**: `frontend/vite.config.js`
**作用**: 配置 Vite 开发服务器、插件和构建选项。

**配置内容**:
- **插件**: `vue()` + `AutoImport`（自动导入 vue/vue-router/pinia API）+ `Components`（自动注册 Element Plus 组件）
- **路径别名**: `@` → `src/`
- **开发服务器**: 端口 3000，代理 `/api` → `http://localhost:8081`
- **构建输出**: `dist/` 目录，禁用 sourcemap，chunk 警告阈值 1500KB
- **代码分割**: `element-plus` 和 `echarts` 单独分包

---

### 10.7 `frontend/index.html` — HTML 入口

**路径**: `frontend/index.html`
**作用**: SPA 入口 HTML 文件。

**内容**: 标准 HTML5 模板，`<div id="app">` 挂载点，引入 `/src/main.js` 模块。语言 `zh-CN`，标题"智能在线考试系统"。

---

### 10.8 `README.md` — 项目说明

**路径**: `README.md`
**作用**: 项目总览文档。

**内容**: 功能特性、技术栈、快速开始指南（环境准备→数据库初始化→启动后端→启动前端→访问应用）、测试账号、项目结构树、文档链接、核心模块说明、通义千问 API 配置、环境变量说明。

---

### 10.9 `sql/init.sql` — 数据库初始化脚本

**路径**: `sql/init.sql`
**作用**: 创建数据库、建表、插入种子数据。

**数据库**: `smart_exam`，字符集 `utf8mb4`，排序规则 `utf8mb4_general_ci`

**14 张表**:
| 表名 | 说明 | 关键字段 |
|------|------|----------|
| `sys_user` | 用户表 | username(唯一), password(BCrypt), role(STUDENT/TEACHER/ADMIN), status, class_id, major_id |
| `sys_dict` | 字典表 | type(CLASS/MAJOR/SUBJECT/SEMESTER), code(唯一), name |
| `question` | 题库表 | title(富文本), type(SINGLE/MULTI/JUDGE/FILL/SUBJECTIVE), difficulty(1-5), subject_id, score, answer, status(DRAFT/PUBLISHED/DISABLED) |
| `question_option` | 选项表 | question_id, option_label(A/B/C/D), option_content, is_correct |
| `exam_paper` | 试卷表 | title, subject_id, total_score, pass_score, duration, start_time, end_time, shuffle_question, shuffle_option, status(DRAFT/PUBLISHED/CLOSED), type(EXAM/PRACTICE) |
| `exam_rule` | 组卷规则表 | paper_id, rule_config(JSON) |
| `exam_paper_question` | 试卷题目关联 | paper_id, question_id, score, sort_order |
| `exam_record` | 考试记录 | paper_id, user_id, attempt_no, status(NOT_STARTED/IN_PROGRESS/SUBMITTED/GRADING/GRADED), total_score, shuffle_seed, question_order(JSON), option_order(JSON) |
| `exam_answer` | 答题记录 | record_id, question_id, user_answer, score, is_correct, ai_score, ai_comment, final_score, grading_status(PENDING/AI_DONE/TEACHER_DONE) |
| `ai_grading_log` | AI阅卷日志 | answer_id, model_name, prompt, response, ai_score, confidence, token_used, duration_ms, status(SUCCESS/FAILED/TIMEOUT/FALLBACK) |
| `error_book` | 错题本 | user_id, question_id, record_id, user_answer, correct_answer, error_count, mastered |
| `sys_class` | 班级表 | name, description |
| `sys_class_user` | 班级用户关联 | class_id, user_id(唯一组合) |
| `exam_paper_class` | 试卷班级关联 | paper_id, class_id(唯一组合) |

**种子数据**:
- 3 个用户: admin(管理员, admin123), t1(教师, 123456), s1(学生, 123456)
- 3 个班级字典: 2024级计算机1班/2班、软件工程1班
- 3 个专业字典: 计算机科学与技术、软件工程、人工智能
- 4 个科目字典: Java程序设计、数据库原理、数据结构与算法、计算机网络
- 3 个学期字典: 2024-2025第一/二学期、2025-2026第一学期

---

### 10.10 `SmartExamApplication.java` — 启动类

**路径**: `backend/src/main/java/com/smartexam/SmartExamApplication.java`
**作用**: Spring Boot 应用入口。

**注解**:
- `@SpringBootApplication` — 组合注解，启用自动配置、组件扫描
- `@MapperScan({"com.smartexam.module.user.mapper", "com.smartexam.module.question.mapper", "com.smartexam.module.exam.mapper", "com.smartexam.module.ai.mapper", "com.smartexam.module.dict.mapper", "com.smartexam.module.stats.mapper", "com.smartexam.module.grading.mapper", "com.smartexam.module.class_.mapper"})` — 扫描所有 MyBatis Mapper 接口
- `@EnableAsync` — 启用异步方法支持（用于 AI 阅卷异步执行）

**关键**: `@MapperScan` 必须包含所有模块的 mapper 包，否则对应的 Mapper Bean 不会被创建。

---

### 10.11 `common/config/CorsConfig.java` — 跨域配置

**路径**: `backend/src/main/java/com/smartexam/common/config/CorsConfig.java`
**作用**: 配置 CORS 跨域策略，允许前端开发服务器访问后端 API。

**配置内容**:
- `addAllowedOriginPattern("*")` — 允许所有来源（开发环境）
- `setAllowCredentials(true)` — 允许携带 Cookie
- `addAllowedHeader("*")` — 允许所有请求头
- `addAllowedMethod("*")` — 允许所有 HTTP 方法
- `setMaxAge(3600L)` — 预检请求缓存 1 小时
- `addExposedHeader("Authorization")` — 暴露 Authorization 响应头

---

### 10.12 `common/config/MyBatisPlusConfig.java` — MyBatis-Plus 配置

**路径**: `backend/src/main/java/com/smartexam/common/config/MyBatisPlusConfig.java`
**作用**: 配置分页插件和自动填充处理器。

**Bean**:
1. `mybatisPlusInterceptor()` — 注册 `PaginationInnerInterceptor(DbType.MYSQL)` 分页插件
2. `metaObjectHandler()` — 自动填充 `createTime`（插入时）和 `updateTime`（插入和更新时）

---

### 10.13 `common/config/RedisConfig.java` — Redis 配置

**路径**: `backend/src/main/java/com/smartexam/common/config/RedisConfig.java`
**作用**: Redis 配置类（开发环境已禁用，改用 `RedisUtil` 内存缓存）。

**内容**: 空配置类，仅 `@Configuration` 注解。

---

### 10.14 `common/config/SaTokenConfig.java` — Sa-Token 鉴权配置

**路径**: `backend/src/main/java/com/smartexam/common/config/SaTokenConfig.java`
**作用**: 配置 Sa-Token 拦截器，实现基于角色的 URL 权限控制。

**权限规则**:
| URL 模式 | 要求角色 | 说明 |
|----------|----------|------|
| `/api/auth/login` | 放行 | 登录接口 |
| `/swagger-ui/**`, `/v3/api-docs/**` | 放行 | API 文档 |
| `/static/**`, `/favicon.ico` | 放行 | 静态资源 |
| `/api/exam/**`, `/api/practice/**`, `/api/stats/student/**` | STUDENT | 学生专属 |
| `/api/questions/**`, `/api/grading/**`, `/api/stats/paper/**` | TEACHER | 教师专属 |
| `/api/papers/**` | STUDENT 或 TEACHER | 试卷接口（学生看列表，教师管理） |
| `/api/users/**`, `/api/classes/**` | ADMIN | 管理员专属 |
| `/api/dicts/**` | ADMIN 或 TEACHER | 字典接口 |
| `/**` | 需登录 | 其他接口 |

---

### 10.15 `common/config/SecurityConfig.java` — Spring Security 配置

**路径**: `backend/src/main/java/com/smartexam/common/config/SecurityConfig.java`
**作用**: 仅使用 BCrypt 密码编码器，不启用 Security 认证（由 Sa-Token 负责）。

**配置**:
- `passwordEncoder()` — 注册 `BCryptPasswordEncoder` Bean
- `filterChain()` — 禁用 CSRF，无状态会话，所有请求放行

---

### 10.16 `common/config/StpInterfaceImpl.java` — Sa-Token 权限实现

**路径**: `backend/src/main/java/com/smartexam/common/config/StpInterfaceImpl.java`
**作用**: 实现 Sa-Token 的 `StpInterface` 接口，提供角色和权限数据。

**方法**:
- `getPermissionList()` — 返回空列表（未使用细粒度权限）
- `getRoleList(loginId)` — 根据用户 ID 查询数据库，返回用户角色列表（如 `["STUDENT"]`）

---

### 10.17 `common/config/SwaggerConfig.java` — Swagger 配置

**路径**: `backend/src/main/java/com/smartexam/common/config/SwaggerConfig.java`
**作用**: 配置 SpringDoc OpenAPI/Swagger 文档。

**配置**:
- 标题: "智能在线考试系统 API"
- 版本: "1.0.0"
- 安全方案: HTTP Bearer Token (JWT)
- 全局安全要求: 所有接口需要 Authorization 头

---

### 10.18 `common/config/ThreadPoolConfig.java` — 线程池配置

**路径**: `backend/src/main/java/com/smartexam/common/config/ThreadPoolConfig.java`
**作用**: 配置 AI 阅卷专用线程池。

**Bean `aiGradingExecutor`**:
- 核心线程: 5
- 最大线程: 10
- 队列容量: 100
- 空闲存活: 60s
- 线程名前缀: `ai-grading-`
- 拒绝策略: `CallerRunsPolicy`（调用者线程执行）

---

### 10.19 `common/exception/BizException.java` — 业务异常

**路径**: `backend/src/main/java/com/smartexam/common/exception/BizException.java`
**作用**: 自定义业务异常类。

**构造方法**:
- `BizException(String message)` — 默认错误码 500
- `BizException(int code, String message)` — 自定义错误码
- `BizException(ResultCode resultCode)` — 使用枚举错误码
- `BizException(ResultCode resultCode, String message)` — 枚举错误码 + 自定义消息

---

### 10.20 `common/exception/GlobalExceptionHandler.java` — 全局异常处理

**路径**: `backend/src/main/java/com/smartexam/common/exception/GlobalExceptionHandler.java`
**作用**: 统一捕获并处理所有异常，返回标准 `ResultVO` 格式。

**处理的异常类型**:
| 异常 | HTTP 状态码 | 说明 |
|------|------------|------|
| `BizException` | (业务码) | 业务异常 |
| `MethodArgumentNotValidException` | 400 | @RequestBody 校验失败 |
| `ConstraintViolationException` | 400 | @RequestParam 校验失败 |
| `BindException` | 400 | 参数绑定失败 |
| `MissingServletRequestParameterException` | 400 | 缺少参数 |
| `HttpRequestMethodNotSupportedException` | 405 | 请求方法不支持 |
| `NoResourceFoundException` | 404 | 资源不存在 |
| `Exception` | 500 | 兜底异常 |

---

### 10.21 `common/result/ResultCode.java` — 错误码枚举

**路径**: `backend/src/main/java/com/smartexam/common/result/ResultCode.java`
**作用**: 定义统一的业务错误码。

**错误码分组**:
- **通用**: 200(成功), 500(失败), 400(参数错误), 401(未登录), 403(无权限), 404(不存在)
- **认证模块 1001-1999**: 登录失败、账号冻结、Token 无效/过期、验证码错误
- **用户模块 2001-2999**: 用户已存在、用户不存在、密码错误、导入格式错误
- **题库模块 3001-3999**: 题目不存在、导入失败、题型不合法、难度不合法
- **考试模块 4001-4999**: 试卷不存在/未发布/未开始/已结束、记录不存在/已提交、超出考试次数
- **阅卷模块 5001-5999**: 阅卷失败、分数不合法、答题记录不存在
- **AI 模块 6001-6999**: AI 调用失败/超时、解析失败、评分超出范围

---

### 10.22 `common/result/ResultVO.java` — 统一响应封装

**路径**: `backend/src/main/java/com/smartexam/common/result/ResultVO.java`
**作用**: 统一 API 响应格式。

**字段**: `code`(状态码), `message`(消息), `data`(数据), `timestamp`(时间戳)

**静态工厂方法**:
- `success()` / `success(data)` / `success(message, data)` — 成功响应
- `fail()` / `fail(message)` / `fail(code, message)` / `fail(ResultCode)` — 失败响应
- `of(code, message, data)` — 自定义响应

---

### 10.23 `util/RedisUtil.java` — 内存缓存工具

**路径**: `backend/src/main/java/com/smartexam/util/RedisUtil.java`
**作用**: 使用 `ConcurrentHashMap` 实现的内存缓存工具类（**未使用Redis**，开发环境直接使用内存存储）。

**核心方法**:
| 方法 | 说明 |
|------|------|
| `set(key, value)` | 设置缓存 |
| `set(key, value, timeout, unit)` | 设置缓存（带过期时间） |
| `get(key)` | 获取缓存（过期返回 null） |
| `delete(key)` | 删除缓存 |
| `expire(key, timeout, unit)` | 设置过期时间 |
| `getExpire(key)` | 获取剩余过期时间 |
| `hasKey(key)` | 判断 key 是否存在 |
| `increment(key)` | 递增 |
| `hashSet/hashGet/hashDelete` | Hash 操作 |

**过期清理**: 启动定时任务，每分钟清理一次过期数据。

---

### 10.24 `module/auth/controller/AuthController.java` — 认证控制器

**路径**: `backend/src/main/java/com/smartexam/module/auth/controller/AuthController.java`
**作用**: 处理登录、登出、获取用户信息请求。

**接口**:
| 方法 | URL | 说明 |
|------|-----|------|
| POST | `/api/auth/login` | 用户登录，接收 `LoginDTO`，返回 token、role、userId、realName |
| POST | `/api/auth/logout` | 退出登录，调用 `StpUtil.logout()` |
| GET | `/api/auth/info` | 获取当前用户信息，返回 id、username、realName、role 等 |

---

### 10.25 `module/auth/dto/LoginDTO.java` — 登录请求 DTO

**路径**: `backend/src/main/java/com/smartexam/module/auth/dto/LoginDTO.java`
**作用**: 登录请求参数封装。

**字段**: `username`(用户名，非空), `password`(密码，非空)

---

### 10.26 `module/auth/service/AuthService.java` — 认证服务

**路径**: `backend/src/main/java/com/smartexam/module/auth/service/AuthService.java`
**作用**: 处理登录逻辑和用户信息查询。

**方法**:
- `login(LoginDTO)` — 查询用户 → 校验密码(BCrypt) → 校验状态 → Sa-Token 登入 → 更新最后登录时间 → 返回 token 信息
- `getUserInfo()` — 获取当前登录用户完整信息

---

### 10.27 `module/user/controller/UserController.java` — 用户管理控制器

**路径**: `backend/src/main/java/com/smartexam/module/user/controller/UserController.java`
**作用**: 管理员管理用户账号。

**接口**:
| 方法 | URL | 说明 |
|------|-----|------|
| GET | `/api/users` | 分页查询用户 |
| POST | `/api/users` | 创建用户 |
| PUT | `/api/users/{id}` | 更新用户 |
| DELETE | `/api/users/{id}` | 删除用户 |
| POST | `/api/users/import` | Excel 批量导入用户 |
| PUT | `/api/users/{id}/reset-password` | 重置密码为 123456 |
| PUT | `/api/users/{id}/toggle-status` | 冻结/解冻用户 |
| PUT | `/api/users/profile` | 修改个人信息（当前用户） |

---

### 10.28 `module/user/dto/UserQueryDTO.java` — 用户查询 DTO

**路径**: `backend/src/main/java/com/smartexam/module/user/dto/UserQueryDTO.java`
**作用**: 用户查询参数封装。

**字段**: `role`, `keyword`(用户名/姓名), `status`, `classId`, `majorId`, `pageNum`(默认1), `pageSize`(默认10)

---

### 10.29 `module/user/entity/SysUser.java` — 用户实体

**路径**: `backend/src/main/java/com/smartexam/module/user/entity/SysUser.java`
**作用**: 对应 `sys_user` 表。

**字段**: `id`, `username`, `password`, `realName`, `role`, `gender`, `email`, `phone`, `avatar`, `classId`, `majorId`, `status`, `lastLoginTime`, `lastLoginIp`, `createTime`, `updateTime`, `deleted`

**注解**: `@TableId(type = IdType.AUTO)`, `@TableField(fill = FieldFill.INSERT/INSERT_UPDATE)`, `@TableLogic`

---

### 10.30 `module/user/mapper/SysUserMapper.java` — 用户 Mapper

**路径**: `backend/src/main/java/com/smartexam/module/user/mapper/SysUserMapper.java`
**作用**: 继承 `BaseMapper<SysUser>`，提供基本 CRUD。

---

### 10.31 `module/user/service/UserService.java` — 用户服务

**路径**: `backend/src/main/java/com/smartexam/module/user/service/UserService.java`
**作用**: 用户管理业务逻辑。

**方法**:
- `page(UserQueryDTO)` — 分页查询，支持角色/关键词/状态/班级/专业筛选
- `create(SysUser)` — 创建用户（检查用户名唯一，BCrypt 加密密码，默认密码 123456）
- `update(SysUser)` — 更新用户（不允许修改密码）
- `delete(Long)` — 逻辑删除用户
- `resetPassword(Long)` — 重置密码为 123456
- `toggleStatus(Long)` — 切换冻结/正常状态
- `updateProfile(SysUser)` — 修改个人信息（不允许改密码、角色、状态）
- `importUsers(MultipartFile, String)` — Excel 批量导入（TODO 未实现）

---

### 10.32 `module/question/controller/QuestionController.java` — 题库控制器

**路径**: `backend/src/main/java/com/smartexam/module/question/controller/QuestionController.java`
**作用**: 教师管理题库。

**接口**:
| 方法 | URL | 说明 |
|------|-----|------|
| GET | `/api/questions` | 分页查询题目 |
| GET | `/api/questions/{id}` | 获取题目详情（含选项） |
| POST | `/api/questions` | 创建题目 |
| PUT | `/api/questions/{id}` | 更新题目 |
| DELETE | `/api/questions/{id}` | 删除题目 |
| POST | `/api/questions/import` | Excel 批量导入题库 |

---

### 10.33 `module/question/dto/QuestionImportDTO.java` — 题目导入 DTO

**路径**: `backend/src/main/java/com/smartexam/module/question/dto/QuestionImportDTO.java`
**作用**: EasyExcel 导入映射。

**字段**: `title`(题干), `type`(题型), `difficulty`(难度), `chapter`(章节), `score`(分值), `options`(选项), `answer`(答案), `explanation`(解析), `tags`(标签)

---

### 10.34 `module/question/dto/QuestionQueryDTO.java` — 题目查询 DTO

**路径**: `backend/src/main/java/com/smartexam/module/question/dto/QuestionQueryDTO.java`
**作用**: 题目查询参数封装。

**字段**: `type`, `difficulty`, `subjectId`, `chapter`, `keyword`, `status`, `pageNum`, `pageSize`

---

### 10.35 `module/question/entity/Question.java` — 题目实体

**路径**: `backend/src/main/java/com/smartexam/module/question/entity/Question.java`
**作用**: 对应 `question` 表。

**字段**: `id`, `title`, `type`, `difficulty`, `subjectId`, `chapter`, `score`, `answer`, `explanation`, `tags`, `status`, `createBy`, `updateBy`, `createTime`, `updateTime`, `deleted`

**非数据库字段**: `options`(`List<QuestionOption>`, `@TableField(exist = false)`)

---

### 10.36 `module/question/entity/QuestionOption.java` — 选项实体

**路径**: `backend/src/main/java/com/smartexam/module/question/entity/QuestionOption.java`
**作用**: 对应 `question_option` 表。

**字段**: `id`, `questionId`, `optionLabel`, `optionContent`, `isCorrect`, `sortOrder`, `createTime`

**非数据库字段**: `originCorrect`（乱序后的原始正确标识，用于前端显示）

---

### 10.37 `module/question/listener/QuestionImportListener.java` — 导入监听器

**路径**: `backend/src/main/java/com/smartexam/module/question/listener/QuestionImportListener.java`
**作用**: EasyExcel 逐行解析监听器，实现批量导入题目。

**核心逻辑**:
1. `invoke()` — 逐行接收数据，校验字段（非空、枚举合法、难度范围 1-5、选择题必须有选项和答案）
2. 每 500 条批量插入 MySQL
3. `doAfterAllAnalysed()` — 存储剩余数据
4. `parseOptions()` — 解析选项字符串（格式: `A.内容|B.内容|C.内容|D.内容`），自动匹配正确答案
5. `convertTagsToJson()` — 将标签文本转为 JSON 数组格式

**内部类**: `ImportResult`(成功/失败计数), `ErrorDetail`(行号+原因)

---

### 10.38 `module/question/mapper/QuestionMapper.java` — 题目 Mapper

**路径**: `backend/src/main/java/com/smartexam/module/question/mapper/QuestionMapper.java`
**作用**: 继承 `BaseMapper<Question>`，提供自定义查询。

**自定义方法**:
- `selectByRule(subjectId, chapter, type, difficulty)` — 根据规则查询候选题目（用于抽题算法），只返回已发布且未被其他试卷使用的题目

---

### 10.39 `module/question/mapper/QuestionOptionMapper.java` — 选项 Mapper

**路径**: `backend/src/main/java/com/smartexam/module/question/mapper/QuestionOptionMapper.java`
**作用**: 继承 `BaseMapper<QuestionOption>`，提供基本 CRUD。

---

### 10.40 `module/question/service/QuestionService.java` — 题库服务

**路径**: `backend/src/main/java/com/smartexam/module/question/service/QuestionService.java`
**作用**: 题库管理业务逻辑。

**方法**:
- `page(QuestionQueryDTO)` — 分页查询，支持题型/难度/科目/章节/关键词/状态筛选
- `getDetail(Long)` — 获取题目详情，同时查询选项列表
- `create(Question)` — 创建题目（状态默认 DRAFT），保存选项（自动生成 A/B/C/D 标识）
- `update(Question)` — 更新题目，选项采用"先删后插"策略
- `delete(Long)` — 逻辑删除题目，同时删除关联选项
- `importQuestions(MultipartFile, Long)` — 使用 EasyExcel + `QuestionImportListener` 批量导入

---

### 10.41 `module/exam/algorithm/QuestionSelector.java` — 千人千卷抽题算法

**路径**: `backend/src/main/java/com/smartexam/module/exam/algorithm/QuestionSelector.java`
**作用**: 核心防作弊算法，实现题目和选项双重乱序。

**算法流程**:
1. 按规则查询 3 倍候选题目（`QuestionMapper.selectByRule`）
2. `Collections.shuffle(candidates, random)` 截取目标数量（杜绝 `ORDER BY RAND()`）
3. 题目整体乱序
4. 选项 Fisher-Yates 洗牌算法（O(n) 复杂度，保证均匀随机）
5. 记录 `originCorrectMap`（乱序后标识 → 原始标识映射）

**内部类**: `RuleItem`(规则配置), `SelectResult`(抽题结果含 questions/optionMap/originCorrectMap/scoreMap)

---

### 10.42 `module/exam/controller/ExamController.java` — 考试控制器

**路径**: `backend/src/main/java/com/smartexam/module/exam/controller/ExamController.java`
**作用**: 学生在线考试相关接口。

**接口**:
| 方法 | URL | 说明 |
|------|-----|------|
| POST | `/api/exam/start/{paperId}` | 开始考试 |
| GET | `/api/exam/records/{id}/question-ids` | 获取题目 ID 列表（答题卡） |
| GET | `/api/exam/records/{id}/questions` | 获取试卷题目（分页） |
| POST | `/api/exam/draft` | 保存草稿 |
| GET | `/api/exam/draft/{recordId}` | 获取草稿 |
| POST | `/api/exam/records/{id}/answer` | 提交单题答案 |
| POST | `/api/exam/records/{id}/submit` | 交卷 |
| GET | `/api/exam/review/list` | 已批改试卷列表（试卷复查） |
| GET | `/api/exam/review/{recordId}` | 试卷复查详情 |

---

### 10.43 `module/exam/controller/ExamPaperController.java` — 试卷管理控制器

**路径**: `backend/src/main/java/com/smartexam/module/exam/controller/ExamPaperController.java`
**作用**: 教师管理试卷。

**接口**:
| 方法 | URL | 说明 |
|------|-----|------|
| GET | `/api/papers` | 分页查询试卷（学生按班级过滤） |
| GET | `/api/papers/{id}` | 获取试卷详情 |
| POST | `/api/papers` | 创建试卷（手动） |
| POST | `/api/papers/generate` | 规则抽题组卷 |
| PUT | `/api/papers/{id}` | 更新试卷 |
| PUT | `/api/papers/{id}/publish` | 发布试卷 |
| DELETE | `/api/papers/{id}` | 删除试卷 |
| GET | `/api/papers/{paperId}/questions` | 获取试卷题目列表 |
| POST | `/api/papers/{paperId}/questions` | 向试卷添加题目 |
| PUT | `/api/papers/{paperId}/questions/{pqId}` | 更新试卷题目分值 |
| DELETE | `/api/papers/{paperId}/questions/{pqId}` | 从试卷移除题目 |
| PUT | `/api/papers/{id}/classes` | 设置试卷关联班级 |
| GET | `/api/papers/{id}/classes` | 获取试卷关联班级 ID 列表 |

---

### 10.44 `module/exam/controller/PracticeController.java` — 练习控制器

**路径**: `backend/src/main/java/com/smartexam/module/exam/controller/PracticeController.java`
**作用**: 学生练习模式。

**接口**:
| 方法 | URL | 说明 |
|------|-----|------|
| POST | `/api/practice/check` | 单题即时判分 |
| GET | `/api/practice/error-book` | 错题本列表 |
| POST | `/api/practice/review/{id}` | 错题复习 |

---

### 10.45 `module/exam/dto/DraftDTO.java` — 草稿保存 DTO

**路径**: `backend/src/main/java/com/smartexam/module/exam/dto/DraftDTO.java`
**字段**: `recordId`(考试记录 ID), `answers`(答案 Map: questionId → answer)

---

### 10.46 `module/exam/dto/ExamStartDTO.java` — 考试开始返回 DTO

**路径**: `backend/src/main/java/com/smartexam/module/exam/dto/ExamStartDTO.java`
**字段**: `recordId`, `paperTitle`, `duration`, `totalScore`, `startTime`, `endTime`, `questionCount`

---

### 10.47 `module/exam/dto/ExamSubmitDTO.java` — 考试提交 DTO

**路径**: `backend/src/main/java/com/smartexam/module/exam/dto/ExamSubmitDTO.java`
**字段**: `answers`(答案 Map: questionId → answer)

---

### 10.48 `module/exam/entity/ErrorBook.java` — 错题本实体

**路径**: `backend/src/main/java/com/smartexam/module/exam/entity/ErrorBook.java`
**对应表**: `error_book`
**字段**: `id`, `userId`, `questionId`, `recordId`, `userAnswer`, `correctAnswer`, `errorCount`, `lastErrorTime`, `reviewCount`, `lastReviewTime`, `mastered`, `createTime`, `updateTime`

---

### 10.49 `module/exam/entity/ExamAnswer.java` — 答题记录实体

**路径**: `backend/src/main/java/com/smartexam/module/exam/entity/ExamAnswer.java`
**对应表**: `exam_answer`
**字段**: `id`, `recordId`, `questionId`, `userAnswer`, `score`, `isCorrect`, `aiScore`, `aiComment`, `finalScore`, `gradingStatus`(PENDING/AI_DONE/TEACHER_DONE/AUTO_GRADED), `answerTime`, `createTime`, `updateTime`

---

### 10.50 `module/exam/entity/ExamPaper.java` — 试卷实体

**路径**: `backend/src/main/java/com/smartexam/module/exam/entity/ExamPaper.java`
**对应表**: `exam_paper`
**字段**: `id`, `title`, `description`, `subjectId`, `subjectName`, `semesterId`, `totalScore`, `passScore`, `duration`, `startTime`, `endTime`, `shuffleQuestion`, `shuffleOption`, `showAnswer`, `maxAttempts`, `status`(DRAFT/PUBLISHED/CLOSED), `type`(EXAM/PRACTICE), `createBy`, `createTime`, `updateTime`, `deleted`

---

### 10.51 `module/exam/entity/ExamPaperClass.java` — 试卷班级关联实体

**路径**: `backend/src/main/java/com/smartexam/module/exam/entity/ExamPaperClass.java`
**对应表**: `exam_paper_class`
**字段**: `id`, `paperId`, `classId`, `createTime`

---

### 10.52 `module/exam/entity/ExamPaperQuestion.java` — 试卷题目关联实体

**路径**: `backend/src/main/java/com/smartexam/module/exam/entity/ExamPaperQuestion.java`
**对应表**: `exam_paper_question`
**字段**: `id`, `paperId`, `questionId`, `score`, `sortOrder`, `createTime`

---

### 10.53 `module/exam/entity/ExamRecord.java` — 考试记录实体

**路径**: `backend/src/main/java/com/smartexam/module/exam/entity/ExamRecord.java`
**对应表**: `exam_record`
**字段**: `id`, `paperId`, `userId`, `attemptNo`, `status`(NOT_STARTED/IN_PROGRESS/SUBMITTED/GRADING/GRADED), `totalScore`, `startTime`, `endTime`, `submitTime`, `shuffleSeed`, `questionOrder`(JSON), `optionOrder`(JSON), `ipAddress`, `createTime`, `updateTime`

---

### 10.54 `module/exam/entity/ExamRule.java` — 组卷规则实体

**路径**: `backend/src/main/java/com/smartexam/module/exam/entity/ExamRule.java`
**对应表**: `exam_rule`
**字段**: `id`, `paperId`, `ruleConfig`(JSON), `createTime`

---

### 10.55 `module/exam/mapper/` — 考试模块 Mapper

7 个 Mapper 接口，均继承 `BaseMapper<T>`：
- `ErrorBookMapper` → `error_book`
- `ExamAnswerMapper` → `exam_answer`
- `ExamPaperClassMapper` → `exam_paper_class`
- `ExamPaperMapper` → `exam_paper`
- `ExamPaperQuestionMapper` → `exam_paper_question`
- `ExamRecordMapper` → `exam_record`
- `ExamRuleMapper` → `exam_rule`

---

### 10.56 `module/exam/service/ExamPaperService.java` — 试卷服务

**路径**: `backend/src/main/java/com/smartexam/module/exam/service/ExamPaperService.java`
**作用**: 试卷 CRUD、题目管理、班级关联。

**方法**:
- `page()` — 分页查询，学生角色自动按班级过滤（查询 `exam_paper_class` 关联表）
- `getDetail()` — 获取试卷详情
- `create()` — 创建试卷（状态 DRAFT）
- `generate()` — 规则抽题组卷（保存 `ExamRule`）
- `update()` — 更新试卷
- `publish()` — 发布试卷（状态改为 PUBLISHED）
- `delete()` — 删除试卷（同时删除规则和题目关联）
- `getPaperQuestions()` — 获取试卷题目列表（含选项）
- `addQuestion()` — 添加题目到试卷（自动计算排序号和总分）
- `updatePaperQuestion()` — 更新试卷题目分值
- `removeQuestion()` — 移除试卷题目（重新排列排序号）
- `assignClasses()` — 设置试卷关联班级（先删后插）
- `getPaperClassIds()` — 获取试卷关联班级 ID 列表
- `canStudentAccessPaper()` — 校验学生是否有权限参加考试

---

### 10.57 `module/exam/service/ExamService.java` — 考试服务

**路径**: `backend/src/main/java/com/smartexam/module/exam/service/ExamService.java`
**作用**: 在线考试核心逻辑。

**方法**:
- `startExam(paperId)` — 开始考试：校验试卷状态/时间/班级权限/考试次数 → 生成随机种子 → 规则抽题或手动组卷 → 题目/选项乱序 → 创建考试记录和答题记录
- `getQuestionIds(recordId)` — 获取题目 ID 列表（答题卡用）
- `getQuestions(recordId, page, size)` — 分页获取题目（含选项和已保存答案）
- `saveDraft(recordId, answers)` — 保存草稿到内存缓存
- `getDraft(recordId)` — 获取草稿
- `submitAnswer(recordId, questionId, answer)` — 提交单题答案
- `submitExam(recordId)` — 交卷：同步草稿 → 客观题自动判分 → 更新状态为 SUBMITTED → 清除草稿
- `autoGradeObjectiveAnswers()` — 客观题自动判分（单选/多选/判断）
- `normalizeJudgeAnswer()` — 标准化判断题答案（T/F → true/false）
- `getReviewList()` — 获取学生已批改试卷列表（status=GRADED），返回试卷标题、得分、提交时间
- `getReviewDetail(recordId)` — 获取试卷复查详情，校验记录归属和状态，复用 `GradingService.getRecordDetail()` 构建完整题目+答案+分数详情

**路径**: `backend/src/main/java/com/smartexam/module/exam/service/PracticeService.java`
**作用**: 练习模式和错题本管理。

**方法**:
- `checkAnswer(questionId, answer)` — 单题即时判分，答错自动加入错题本
- `getErrorBook(pageNum, pageSize)` — 获取错题本（分页，未掌握的题目）
- `reviewError(id)` — 错题复习（增加复习次数）

---

### 10.59 `module/exam/vo/PracticeCheckVO.java` — 练习判分结果 VO

**路径**: `backend/src/main/java/com/smartexam/module/exam/vo/PracticeCheckVO.java`
**字段**: `isCorrect`, `correctAnswer`, `explanation`

---

### 10.60 `module/class_/entity/SysClass.java` — 班级实体

**路径**: `backend/src/main/java/com/smartexam/module/class_/entity/SysClass.java`
**对应表**: `sys_class`
**字段**: `id`, `name`, `description`, `createTime`, `updateTime`, `deleted`
**非数据库字段**: `teacherCount`, `studentCount`

---

### 10.61 `module/class_/entity/SysClassUser.java` — 班级用户关联实体

**路径**: `backend/src/main/java/com/smartexam/module/class_/entity/SysClassUser.java`
**对应表**: `sys_class_user`
**字段**: `id`, `classId`, `userId`, `createTime`

---

### 10.62 `module/class_/mapper/SysClassMapper.java` & `SysClassUserMapper.java`

**路径**: `backend/src/main/java/com/smartexam/module/class_/mapper/`
**作用**: 继承 `BaseMapper<T>`，提供基本 CRUD。

---

### 10.63 `module/class_/service/ClassService.java` — 班级服务

**路径**: `backend/src/main/java/com/smartexam/module/class_/service/ClassService.java`
**作用**: 班级管理和成员管理。

**方法**:
- `page(keyword, pageNum, pageSize)` — 分页查询班级（填充教师/学生数量）
- `listAll()` — 获取所有班级（下拉选择用）
- `create/update/delete` — 班级 CRUD（删除时同时删除关联）
- `addClassUser(classId, userId)` — 添加成员到班级（已存在则跳过）
- `removeClassUser(classId, userId)` — 移除班级成员
- `getClassUsers(classId, role)` — 获取班级成员列表（可按角色筛选）
- `getAvailableUsers(role)` — 获取可添加的用户列表
- `getUserClassIds(userId)` — 获取用户所属班级 ID 列表

---

### 10.64 `module/class_/controller/ClassController.java` — 班级控制器

**路径**: `backend/src/main/java/com/smartexam/module/class_/controller/ClassController.java`
**接口**:
| 方法 | URL | 说明 |
|------|-----|------|
| GET | `/api/classes` | 分页查询班级 |
| GET | `/api/classes/all` | 获取所有班级 |
| POST | `/api/classes` | 创建班级 |
| PUT | `/api/classes/{id}` | 更新班级 |
| DELETE | `/api/classes/{id}` | 删除班级 |
| GET | `/api/classes/{id}/users` | 获取班级成员 |
| POST | `/api/classes/{id}/users` | 添加成员到班级 |
| DELETE | `/api/classes/{id}/users/{userId}` | 移除班级成员 |
| GET | `/api/classes/available-users` | 获取可添加的用户列表 |

---

### 10.65 `module/dict/entity/SysDict.java` — 字典实体

**路径**: `backend/src/main/java/com/smartexam/module/dict/entity/SysDict.java`
**对应表**: `sys_dict`
**字段**: `id`, `type`(CLASS/MAJOR/SUBJECT/SEMESTER), `code`, `name`, `sortOrder`, `status`, `remark`, `createTime`, `updateTime`, `deleted`

---

### 10.66 `module/dict/mapper/SysDictMapper.java` — 字典 Mapper

继承 `BaseMapper<SysDict>`。

---

### 10.67 `module/dict/service/DictService.java` — 字典服务

**方法**: `page(type, pageNum, pageSize)` 分页查询, `getByType(type)` 按类型查询, `create/update/delete` CRUD

---

### 10.68 `module/dict/controller/DictController.java` — 字典控制器

**接口**: GET `/api/dicts` 分页查询, GET `/api/dicts/type/{type}` 按类型查询, POST/PUT/DELETE CRUD

---

### 10.69 `module/grading/controller/GradingController.java` — 阅卷控制器

**接口**:
| 方法 | URL | 说明 |
|------|-----|------|
| GET | `/api/grading/papers` | 获取待阅卷试卷列表 |
| GET | `/api/grading/papers/{id}/answers` | 获取试卷答题列表 |
| PUT | `/api/grading/answers/{id}` | 人工评分 |
| POST | `/api/grading/papers/{id}/ai-grade` | 触发 AI 批量阅卷 |
| GET | `/api/grading/papers/{paperId}/students` | 获取学生提交列表 |
| GET | `/api/grading/papers/{paperId}/records/{recordId}/detail` | 获取答卷详情 |
| POST | `/api/grading/records/{recordId}/batch-grade` | 批量评分提交 |

---

### 10.70 `module/grading/dto/BatchGradeDTO.java` & `GradeAnswerDTO.java`

- `BatchGradeDTO`: `items`(`List<GradeItem>`), 内部类 `GradeItem`: `answerId`, `finalScore`, `comment`
- `GradeAnswerDTO`: `finalScore`, `comment`

---

### 10.71 `module/grading/service/GradingService.java` — 阅卷服务

**方法**:
- `getGradingPapers()` — 获取待阅卷试卷列表（按试卷分组统计）
- `getPaperAnswers()` — 获取试卷答题列表
- `gradeAnswer()` — 人工评分（校验分数不超过满分）
- `triggerAiGrade()` — 触发 AI 批量阅卷（主观题/填空题异步调用 AI，客观题自动判分）
- `getPaperStudents()` — 获取学生提交列表（含学生姓名）
- `getRecordDetail()` — 获取答卷详情（题目+选项+答案+评分信息）
- `batchGrade()` — 批量评分
- `updateRecordTotalScore()` — 更新考试记录总分（状态改为 GRADED）

---

### 10.72 `module/grading/vo/` — 阅卷 VO

- `GradingPaperVO`: `paperId`, `paperTitle`, `subjectName`, `submitCount`, `pendingCount`, `gradedCount`, `avgScore`
- `GradingPaperDetailVO`: `recordId`, `userId`, `studentName`, `paperId`, `paperTitle`, `totalScore`, `studentScore`, `status`, `submitTime`, `questions`(`List<GradingQuestionItem>`)
- `GradingQuestionItem`: `answerId`, `questionId`, `title`, `type`, `fullScore`, `options`, `userAnswer`, `referenceAnswer`, `explanation`, `autoScore`, `aiScore`, `aiComment`, `finalScore`, `gradingStatus`, `isCorrect`

---

### 10.73 `module/ai/entity/AiGradingLog.java` — AI 阅卷日志实体

**对应表**: `ai_grading_log`
**字段**: `id`, `answerId`, `questionId`, `modelName`, `prompt`, `response`, `aiScore`, `aiComment`, `confidence`, `tokenUsed`, `durationMs`, `status`(SUCCESS/FAILED/TIMEOUT/FALLBACK), `errorMsg`, `retryCount`, `createTime`

---

### 10.74 `module/ai/mapper/AiGradingLogMapper.java`

继承 `BaseMapper<AiGradingLog>`。

---

### 10.75 `module/ai/service/AiGradingService.java` — AI 阅卷服务

**路径**: `backend/src/main/java/com/smartexam/module/ai/service/AiGradingService.java`
**作用**: 使用通义千问 DashScope API 进行主观题阅卷。

**核心逻辑**:
1. `gradeSubjectiveAsync(answerId)` — 异步执行（`@Async("aiGradingExecutor")`）
2. `gradeSubjective(answerId)` — 获取答题记录 → 获取题目 → 构建 Prompt → 调用 API（含重试 2 次）→ 解析返回 → 更新分数
3. `callDashScopeApi(prompt)` — 使用 OpenAI 兼容格式调用 DashScope API，超时 15s
4. `parseAndUpdateScore()` — 解析 JSON 返回（score/explanation/confidence），校验分数范围
5. `extractJson()` — 提取 JSON（处理 AI 返回可能包含 ```json``` 标记的情况）
6. `fallbackToZero()` — 降级处理：0 分 + 标记需人工复核

**Prompt 设计**: 系统提示"你是一名严谨的大学专业课阅卷教师"，用户提示包含题目、参考答案、学生作答，要求返回 JSON 格式 `{score, explanation, confidence}`

---

### 10.76 `module/stats/controller/StatsController.java` — 统计控制器

**接口**:
| 方法 | URL | 说明 |
|------|-----|------|
| GET | `/api/stats/paper/{id}/report` | 获取试卷成绩报告 |
| GET | `/api/stats/paper/{id}/rank` | 获取成绩排名 |
| GET | `/api/stats/student/history` | 获取学生历史成绩 |

---

### 10.77 `module/stats/service/StatsService.java` — 统计服务

**方法**:
- `getPaperReport(paperId)` — 计算试卷成绩报告（参考人数、及格人数/率、平均/最高/最低分、分数段分布）
- `getPaperRank(paperId)` — 获取成绩排名（按分数降序）
- `getStudentHistory()` — 获取当前学生历史成绩

---

### 10.78 `module/stats/vo/PaperReportVO.java` — 试卷报告 VO

**字段**: `paperId`, `paperTitle`, `totalScore`, `passScore`, `totalCount`, `passCount`, `failCount`, `passRate`, `avgScore`, `maxScore`, `minScore`, `scoreDistribution`(`List<Map>`)

---

### 10.79 `frontend/src/main.js` — 前端入口

**作用**: 创建 Vue 应用，注册 Element Plus（中文 locale）、Pinia、Vue Router、Element Plus 图标。

---

### 10.80 `frontend/src/App.vue` — 根组件

**作用**: 仅包含 `<router-view />`，全局样式（reset、字体、NProgress 颜色）。

---

### 10.81 `frontend/src/router/index.js` — 路由配置

**路由结构**:
- `/login` — 登录页（不需要认证）
- `/student` — 学生端（需要 STUDENT 角色）
  - `dashboard`, `exam-hall`, `exam-room/:recordId`(hideLayout), `history`, `exam-review`
- `/teacher` — 教师端（需要 TEACHER 角色）
  - `question-bank`, `paper-manage`, `grading`, `data-board`
- `/admin` — 管理员端（需要 ADMIN 角色）
  - `user-manage`, `class-manage`
- `/` → 重定向到 `/login`
- `/:pathMatch(.*)*` → 404 重定向到 `/login`

**路由守卫**: NProgress 进度条 + 角色权限校验（已登录用户访问登录页自动跳转到对应角色首页）

---

### 10.82 `frontend/src/store/exam.js` — 考试状态管理

**状态**: `recordId`, `paperTitle`, `duration`, `totalScore`, `startTime`, `endTime`, `questionCount`, `questions`, `questionIds`, `answers`, `currentIndex`, `totalPages`, `currentPage`, `remaining`

**方法**:
- `start(paperId)` — 开始考试（加载题目、启动倒计时、启动草稿定时保存）
- `loadExam(rid)` — 加载已有考试（页面刷新场景）
- `loadQuestions(page)` — 加载题目（无刷新切题）
- `goToQuestion(index)` — 切换题目
- `setAnswer(questionId, answer)` — 保存答案
- `submitSingleAnswer(questionId, answer)` — 提交单题答案
- `saveDraft()` — 保存草稿到内存缓存（断网时降级到 localStorage）
- `submit()` — 交卷
- `formatTime()` — 格式化剩余时间

**定时器**: 倒计时每秒递减，到期自动交卷；草稿每 30 秒自动保存

---

### 10.83 `frontend/src/api/` — 前端 API 模块

9 个 API 模块，均使用 `./request` 相对导入：
- `request.js` — Axios 实例，baseURL `/api`，请求拦截器自动携带 Token，响应拦截器统一处理错误（401 跳登录、断网草稿暂存）
- `auth.js` — `login`, `logout`, `getUserInfo`
- `user.js` — `getUserPage`, `createUser`, `updateUser`, `deleteUser`, `resetPassword`, `toggleUserStatus`, `importUsers`, `updateProfile`
- `question.js` — `getQuestionPage`, `getQuestionDetail`, `createQuestion`, `updateQuestion`, `deleteQuestion`, `importQuestions`
- `paper.js` — `getPaperPage`, `getPaperDetail`, `createPaper`, `generatePaper`, `updatePaper`, `publishPaper`, `deletePaper`, `getGradingPapers`, `getPaperAnswers`, `gradeAnswer`, `triggerAiGrade`, `getPaperQuestions`, `addPaperQuestion`, `updatePaperQuestion`, `removePaperQuestion`, `assignClasses`, `getPaperClassIds`, `getPaperStudents`, `getRecordDetail`, `batchGrade`
- `exam.js` — `startExam`, `getExamQuestions`, `getExamQuestionIds`, `submitAnswer`, `saveDraft`, `getDraft`, `submitExam`, `checkAnswer`, `getErrorBook`, `reviewError`, `getReviewList`, `getReviewDetail`
- `stats.js` — `getPaperReport`, `getPaperRank`, `getStudentHistory`
- `dict.js` — `getDictPage`, `getDictByType`, `createDict`, `updateDict`, `deleteDict`
- `class.js` — `getClassPage`, `getClassAll`, `createClass`, `updateClass`, `deleteClass`, `getClassUsers`, `addClassUser`, `removeClassUser`, `getAvailableUsers`

---

### 10.84 `frontend/src/utils/format.js` — 格式化工具

**函数**: `formatDateTime`, `formatDate`, `formatSeconds`, `formatFileSize`, `formatRole`(角色中文名), `formatQuestionType`(题型中文名), `formatDifficulty`(难度中文名)

---

### 10.85 `frontend/src/utils/storage.js` — 存储工具

**函数**: `setStorage`, `getStorage`, `removeStorage`, `clearStorage`（带 `smart_exam_` 前缀的 localStorage 封装）

---

### 10.86 `frontend/src/views/admin/Layout.vue` — 管理员端布局

**作用**: Element Plus 容器布局，可折叠侧边栏（用户管理、班级管理），顶部面包屑和用户下拉菜单（退出登录）。

---

### 10.87 `frontend/src/views/admin/ClassManage.vue` — 班级管理

**作用**: 班级 CRUD 页面。分页表格（ID、名称、描述、教师数、学生数）+ 创建/编辑弹窗 + 成员管理抽屉（筛选角色、移除成员、批量添加用户）。

---

### 10.88 `frontend/src/views/admin/DictManage.vue` — 字典管理

**作用**: 字典 CRUD 页面。支持四种类型（CLASS/MAJOR/SUBJECT/SEMESTER），类型筛选下拉框 + 分页表格 + 创建/编辑弹窗。

---

### 10.89 `frontend/src/views/admin/UserManage.vue` — 用户管理

**作用**: 用户 CRUD 页面。角色/关键词筛选 + 分页表格 + 创建/编辑弹窗 + 重置密码/冻结解冻/删除操作 + Excel 批量导入。

---

### 10.90 `frontend/src/views/teacher/Layout.vue` — 教师端布局

**作用**: 与管理员布局类似，侧边栏四个导航项（题库管理、试卷管理、阅卷中心、数据看板）。

---

### 10.91 `frontend/src/views/teacher/DataBoard.vue` — 数据看板

**作用**: 试卷统计分析。选择试卷后显示统计卡片（参考/及格/不及格人数、及格率、平均/最高/最低分）+ ECharts 环形图（及格率）+ 柱状图（分数段分布）。

---

### 10.92 `frontend/src/views/teacher/GradingCenter.vue` — 阅卷中心

**作用**: 阅卷管理界面。待阅卷试卷列表（待阅/已阅数量）+ 弹窗（左侧学生列表，右侧题目+答案+评分输入框）+ 支持触发 AI 批量阅卷 + 批量评分提交。

---

### 10.93 `frontend/src/views/teacher/PaperManage.vue` — 试卷管理

**作用**: 试卷 CRUD + 题目管理。试卷列表 + 创建/编辑弹窗（标题、时长、分值、时间范围、乱序选项、指定班级）+ 抽屉管理试卷题目（从题库添加、新建题目、编辑、删除、调整分值）。

---

### 10.94 `frontend/src/views/teacher/QuestionBank.vue` — 题库管理

**作用**: 题目 CRUD + Excel 导入。支持五种题型（单选/多选/判断/填空/主观），类型/难度/关键词筛选（防抖搜索），创建/编辑弹窗根据题型动态渲染答案输入（单选按钮、多选框、文本域），自动识别选项功能（粘贴文本解析 A/B/C/D），Excel 批量导入。

---

### 10.95 `frontend/src/views/student/Layout.vue` — 学生端布局

**作用**: 与教师/管理员布局类似，侧边栏四个导航项（个人中心、考试大厅、历史成绩、试卷复查）。

---

### 10.96 `frontend/src/views/student/Dashboard.vue` — 学生首页

**作用**: 个人中心。欢迎卡片（学生姓名）+ 快捷入口按钮（考试大厅、历史成绩）+ 最近 5 次考试记录表格。

---

### 10.97 `frontend/src/views/student/ExamHall.vue` — 考试大厅

**作用**: 考试列表页面。可筛选分页表格（标题、科目、总分、时长、时间范围、状态），点击"开始考试"调用 `startExam` API，将考试数据写入 `useExamStore`，跳转到 ExamRoom。

---

### 10.98 `frontend/src/views/student/ExamRoom.vue` — 答题页

**作用**: 在线答题界面。全屏布局，顶部固定栏（试卷标题、倒计时<5分钟闪烁动画、已答数、交卷按钮），右侧答题卡网格导航，左侧题目区域支持单选/多选/判断（radio/checkbox）和填空/主观（textarea）。每答一题自动保存答案。页面刷新时通过 `loadExam(recordId)` 恢复状态。

---

### 10.99 `frontend/src/views/student/History.vue` — 历史成绩

**作用**: 考试历史记录表格（试卷标题、分数、提交时间），低于 60 分红色显示。

---

### 10.100 `frontend/src/views/student/ErrorBook.vue` — 试卷复查（非错题本）

**作用**: 学生查看已批改试卷详情。顶部表格展示已批改试卷列表（试卷标题、科目、得分、提交时间），点击"查看详情"打开弹窗。弹窗为双栏布局：左侧题目导航（颜色标注对/错/部分正确），右侧显示完整试卷详情（题型标签、题干、选项、学生答案、参考答案、解析、得分、AI评语），所有分数为只读。后端通过 `GET /api/exam/review/list` 和 `GET /api/exam/review/{recordId}` 获取数据，复用 `GradingService.getRecordDetail()` 构建详情。

> **注意**: 文件名虽为 `ErrorBook.vue`，但实际功能为"**试卷复查**"，不是"错题本"。学生端菜单中显示为"试卷复查"。

---

### 10.101 `frontend/src/views/login/index.vue` — 登录页

**作用**: 居中卡片式登录表单，渐变背景。用户名/密码输入 + 表单校验 + 登录后按角色跳转（STUDENT→/student, TEACHER→/teacher, ADMIN→/admin）。底部显示测试账号提示。

---

### 10.102 `docs/API.md` — API 接口文档

**作用**: REST API 参考文档。涵盖认证、用户、题库、试卷、考试、练习、阅卷、统计、字典模块的所有接口。Base URL `localhost:8080/api`，JWT 认证，统一 JSON 响应格式和错误码。

---

### 10.103 `docs/DEPLOY.md` — 部署指南

**作用**: 本地开发和生产环境部署指南。环境要求（JDK 17、Node 18、MySQL 8、Redis 7），本地开发步骤，通义千问 API Key 配置，生产打包部署（JAR + Nginx 反向代理），前后端联调顺序和排错清单，生产加固清单（SSL、Redis 密码、备份），监控和备份脚本。

---

### 10.104 `docs/TEST_GUIDE.md` — 测试指南

**作用**: 手动和集成测试指南。测试环境前提和测试账号，各模块功能测试用例（认证、题库、试卷、考试流程、阅卷、看板、用户管理、字典），核心逻辑测试（千人千卷乱序、实时倒计时、断网草稿持久化、AI 阅卷），Swagger API 测试流程，性能基准（并发登录、并发考试），测试报告模板。

---

## 11. 文件清单

文件清单：
- 项目配置文件: 9 个
- 后端 common 模块: 12 个
- 后端工具类: 1 个
- 后端启动类: 1 个
- 后端 auth 模块: 3 个
- 后端 user 模块: 5 个
- 后端 question 模块: 9 个
- 后端 exam 模块: 25 个
- 后端 class 模块: 6 个
- 后端 dict 模块: 4 个
- 后端 grading 模块: 7 个
- 后端 ai 模块: 3 个
- 后端 stats 模块: 3 个
- 前端入口与配置: 5 个
- 前端 API 模块: 9 个
- 前端工具类: 2 个
- 前端 admin 视图: 4 个
- 前端 teacher 视图: 5 个
- 前端 student 视图: 6 个
- 前端登录视图: 1 个
- 项目文档: 3 个

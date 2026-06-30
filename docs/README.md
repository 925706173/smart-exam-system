# 智能在线考试系统

SmartExam - 基于Vue 3 + Spring Boot的企业级在线考试平台

## 功能特性

- **千人千卷**：题目/选项双重乱序，杜绝抄袭
- **AI阅卷**：通义千问自动批改主观题
- **实时答题**：无刷新切题、倒计时、草稿防丢档
- **数据可视化**：ECharts统计大屏
- **Excel导入**：题库/账号批量导入
- **试卷复查**：学生查看已批改试卷详情
- **班级管理**：管理员管理班级，教师指定班级发布试卷

## 技术栈

### 前端
- Vue 3 (Composition API)
- Vite 5.x
- Pinia
- Vue Router 4
- Axios
- Element Plus
- ECharts 5

### 后端
- Spring Boot 3.2
- MyBatis-Plus
- Sa-Token (JWT鉴权)
- Spring Security (仅用于BCrypt密码加密)
- EasyExcel
- DashScope SDK (通义千问)

### 数据库与缓存
- MySQL 8.0
- 内存缓存 (ConcurrentHashMap，开发环境，未使用Redis)

## 快速开始

### 1. 环境准备

- JDK 17+
- Node.js 18+
- MySQL 8.0+
- Maven 3.8+

### 2. 数据库初始化

```bash
mysql -u root -p < sql/init.sql
```

### 3. 启动后端

```bash
cd backend
mvn spring-boot:run
```

### 4. 启动前端

```bash
cd frontend
npm install
npm run dev
```

### 5. 访问应用

- 前端：http://localhost:3000
- 后端API：http://localhost:8080
- Swagger文档：http://localhost:8080/swagger-ui.html

### 6. 测试账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | admin123 |
| 教师 | t1 | 123456 |
| 学生 | s1 | 123456 |

## 项目结构

```
smart-exam-system/
├── backend/                    # 后端工程
│   ├── pom.xml
│   └── src/main/java/com/smartexam/
│       ├── common/             # 公共模块
│       ├── module/             # 业务模块
│       └── util/               # 工具类
├── frontend/                   # 前端工程
│   ├── package.json
│   ├── vite.config.js
│   └── src/
│       ├── api/                # API封装
│       ├── router/             # 路由
│       ├── store/              # 状态管理
│       └── views/              # 页面组件
├── sql/                        # 数据库脚本
├── docs/                       # 项目文档
└── README.md
```

## 文档

- [API接口文档](docs/API.md)
- [部署指南](docs/DEPLOY.md)
- [测试指南](docs/TEST_GUIDE.md)

## 核心模块

### 学生端
- 个人中心
- 考试大厅
- 在线答题（无刷新切题/实时倒计时/草稿防丢档）
- 历史成绩
- 试卷复查（查看已批改试卷详情）

### 教师端
- 题库管理（CRUD/筛选/Excel导入）
- 试卷管理（手动组卷/规则抽题/发布）
- 阅卷中心（人工打分/AI辅助打分）
- 数据看板（ECharts可视化）

### 管理员端
- 师生账号批量管理
- 基础字典维护

### AI服务
- 主观题智能阅卷
- Prompt模板管理
- 打分日志与置信度记录

## 配置说明

### 通义千问API配置

```yaml
# application-dev.yml
ali:
  qwen:
    api-key: ${QWEN_API_KEY:sk-xxxxxxxx}
    base-url: https://dashscope.aliyuncs.com/api/v1
    model: qwen-turbo
    timeout: 15000
    max-retries: 2
```

### 环境变量

```bash
export DB_PASSWORD=your_password
export QWEN_API_KEY=sk-xxxxxxxx
```

## 许可证

MIT License

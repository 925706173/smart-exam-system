# 智能在线考试系统 - API接口文档

## 1. 接口规范

### 1.1 基础信息

- **Base URL**: `http://localhost:8080/api`
- **Content-Type**: `application/json`
- **字符编码**: UTF-8

### 1.2 认证方式

所有需要登录的接口需在请求头携带Token：

```
Authorization: Bearer {token}
```

### 1.3 统一响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": {},
  "timestamp": 1716211200000
}
```

### 1.4 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 400 | 参数错误 |
| 401 | 未登录或Token已过期 |
| 403 | 无权限访问 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |
| 1001 | 用户名或密码错误 |
| 1002 | 账号已被冻结 |
| 2001 | 用户名已存在 |
| 3001 | 题目不存在 |
| 4001 | 试卷不存在 |
| 4002 | 试卷未发布 |
| 4005 | 考试记录不存在 |
| 4006 | 已经交卷 |
| 6001 | AI调用失败 |

---

## 2. 认证模块 `/api/auth`

### 2.1 用户登录

**POST** `/api/auth/login`

**请求参数：**
```json
{
  "username": "t1",
  "password": "123456"
}
```

**响应示例：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...",
    "tokenName": "Authorization",
    "role": "TEACHER",
    "userId": 2,
    "realName": "张老师"
  },
  "timestamp": 1716211200000
}
```

### 2.2 退出登录

**POST** `/api/auth/logout`

**请求头：**
```
Authorization: Bearer {token}
```

**响应示例：**
```json
{
  "code": 200,
  "message": "success",
  "data": null,
  "timestamp": 1716211200000
}
```

### 2.3 获取当前用户信息

**GET** `/api/auth/info`

**响应示例：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 2,
    "username": "t1",
    "realName": "张老师",
    "role": "TEACHER",
    "gender": 1,
    "email": "teacher@example.com",
    "phone": "13800138000"
  },
  "timestamp": 1716211200000
}
```

---

## 3. 用户模块 `/api/users`

### 3.1 分页查询用户

**GET** `/api/users`

**查询参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| pageNum | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页数量，默认10 |
| role | String | 否 | 角色：STUDENT/TEACHER/ADMIN |
| keyword | String | 否 | 关键词（用户名/姓名） |
| status | Integer | 否 | 状态：0-冻结，1-正常 |

**响应示例：**
```json
{
  "code": 200,
  "data": {
    "records": [
      {
        "id": 3,
        "username": "s1",
        "realName": "王同学",
        "role": "STUDENT",
        "status": 1,
        "createTime": "2024-05-20 10:00:00"
      }
    ],
    "total": 1,
    "size": 10,
    "current": 1
  }
}
```

### 3.2 创建用户

**POST** `/api/users`

**请求参数：**
```json
{
  "username": "s2",
  "realName": "李同学",
  "role": "STUDENT",
  "password": "123456",
  "email": "student2@example.com",
  "phone": "13900139000"
}
```

### 3.3 更新用户

**PUT** `/api/users/{id}`

### 3.4 删除用户

**DELETE** `/api/users/{id}`

### 3.5 重置密码

**PUT** `/api/users/{id}/reset-password`

**说明：** 将密码重置为默认值 `123456`

### 3.6 冻结/解冻用户

**PUT** `/api/users/{id}/toggle-status`

### 3.7 Excel批量导入

**POST** `/api/users/import`

**请求类型：** `multipart/form-data`

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| file | File | 是 | Excel文件 |
| role | String | 是 | 用户角色 |

---

## 4. 题库模块 `/api/questions`

### 4.1 分页查询题目

**GET** `/api/questions`

**查询参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| pageNum | Integer | 否 | 页码 |
| pageSize | Integer | 否 | 每页数量 |
| type | String | 否 | 题型：SINGLE/MULTI/JUDGE/FILL/SUBJECTIVE |
| difficulty | Integer | 否 | 难度：1-5 |
| subjectId | Long | 否 | 科目ID |
| keyword | String | 否 | 关键词 |

### 4.2 获取题目详情

**GET** `/api/questions/{id}`

**响应示例：**
```json
{
  "code": 200,
  "data": {
    "id": 1,
    "title": "Java中哪个关键字用于定义常量？",
    "type": "SINGLE",
    "difficulty": 2,
    "score": 2.00,
    "answer": "C",
    "explanation": "final关键字用于定义常量",
    "options": [
      {"id": 1, "optionLabel": "A", "optionContent": "static", "isCorrect": 0},
      {"id": 2, "optionLabel": "B", "optionContent": "const", "isCorrect": 0},
      {"id": 3, "optionLabel": "C", "optionContent": "final", "isCorrect": 1},
      {"id": 4, "optionLabel": "D", "optionContent": "var", "isCorrect": 0}
    ]
  }
}
```

### 4.3 创建题目

**POST** `/api/questions`

**请求参数：**
```json
{
  "title": "Java中哪个关键字用于定义常量？",
  "type": "SINGLE",
  "difficulty": 2,
  "score": 2.00,
  "answer": "C",
  "explanation": "final关键字用于定义常量",
  "options": [
    {"optionLabel": "A", "optionContent": "static", "isCorrect": 0},
    {"optionLabel": "B", "optionContent": "const", "isCorrect": 0},
    {"optionLabel": "C", "optionContent": "final", "isCorrect": 1},
    {"optionLabel": "D", "optionContent": "var", "isCorrect": 0}
  ]
}
```

### 4.4 Excel批量导入

**POST** `/api/questions/import`

**请求类型：** `multipart/form-data`

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| file | File | 是 | Excel文件 |
| subjectId | Long | 是 | 科目ID |

**Excel模板格式：**

| 题干 | 题型 | 难度 | 章节 | 分值 | 选项 | 答案 | 解析 | 标签 |
|------|------|------|------|------|------|------|------|------|
| Java中哪个关键字... | SINGLE | 2 | 第1章 | 2 | A.static\|B.const\|C.final\|D.var | C | final定义常量 | 重点 |

**响应示例：**
```json
{
  "code": 200,
  "data": {
    "successCount": 50,
    "failCount": 2,
    "errorDetails": [
      {"rowNum": 15, "reason": "题型不合法"},
      {"rowNum": 23, "reason": "难度等级不合法"}
    ]
  }
}
```

---

## 5. 试卷模块 `/api/papers`

### 5.1 创建试卷

**POST** `/api/papers`

**请求参数：**
```json
{
  "title": "Java程序设计期末考试",
  "duration": 120,
  "totalScore": 100,
  "passScore": 60,
  "startTime": "2024-06-20 09:00:00",
  "endTime": "2024-06-20 11:00:00",
  "shuffleQuestion": 1,
  "shuffleOption": 1
}
```

### 5.2 规则抽题组卷

**POST** `/api/papers/generate`

**请求参数：**
```json
{
  "title": "Java单元测试",
  "duration": 60,
  "totalScore": 50,
  "passScore": 30
}
```

**查询参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| ruleConfig | String | 是 | 规则JSON |

**ruleConfig格式：**
```json
[
  {"chapter": "第1章", "type": "SINGLE", "difficulty": 2, "count": 5, "score": 2},
  {"chapter": "第2章", "type": "MULTI", "difficulty": 3, "count": 3, "score": 4},
  {"chapter": "第1章", "type": "JUDGE", "difficulty": 1, "count": 4, "score": 2}
]
```

### 5.3 发布试卷

**PUT** `/api/papers/{id}/publish`

---

## 6. 考试模块 `/api/exam`

### 6.1 开始考试

**POST** `/api/exam/start/{paperId}`

**响应示例：**
```json
{
  "code": 200,
  "data": {
    "recordId": 1,
    "paperTitle": "Java程序设计期末考试",
    "duration": 120,
    "totalScore": 100,
    "startTime": "2024-05-20 09:00:00",
    "endTime": "2024-05-20 11:00:00",
    "questionCount": 30
  }
}
```

### 6.2 获取试卷题目（无刷新切题）

**GET** `/api/exam/records/{id}/questions`

**查询参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | Integer | 否 | 页码，默认1 |
| size | Integer | 否 | 每页数量，默认10 |

**响应示例：**
```json
{
  "code": 200,
  "data": {
    "questions": [
      {
        "id": 5,
        "title": "下列哪个是Java的基本数据类型？",
        "type": "SINGLE",
        "score": 2,
        "options": [
          {"optionLabel": "A", "optionContent": "String"},
          {"optionLabel": "B", "optionContent": "int"},
          {"optionLabel": "C", "optionContent": "ArrayList"},
          {"optionLabel": "D", "optionContent": "HashMap"}
        ]
      }
    ],
    "answers": {"5": "B"},
    "total": 30,
    "page": 1,
    "pageSize": 10
  }
}
```

### 6.3 提交单题答案

**POST** `/api/exam/records/{id}/answer`

**查询参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| questionId | Long | 是 | 题目ID |
| answer | String | 是 | 答案 |

### 6.4 保存草稿

**POST** `/api/exam/draft`

**请求参数：**
```json
{
  "recordId": 1,
  "answers": {
    "5": "B",
    "6": "A,C",
    "7": "T"
  }
}
```

### 6.5 交卷

**POST** `/api/exam/records/{id}/submit`

---

## 7. 练习模块 `/api/practice`

### 7.1 单题即时判分

**POST** `/api/practice/check`

**查询参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| questionId | Long | 是 | 题目ID |
| answer | String | 是 | 学生答案 |

**响应示例：**
```json
{
  "code": 200,
  "data": {
    "isCorrect": false,
    "correctAnswer": "C",
    "explanation": "final关键字用于定义常量"
  }
}
```

### 7.2 错题本列表

**GET** `/api/practice/error-book`

**查询参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| pageNum | Integer | 否 | 页码 |
| pageSize | Integer | 否 | 每页数量 |

---

## 8. 阅卷模块 `/api/grading`

### 8.1 获取待阅卷试卷列表

**GET** `/api/grading/papers`

### 8.2 获取试卷答题列表

**GET** `/api/grading/papers/{id}/answers`

### 8.3 人工评分

**PUT** `/api/grading/answers/{id}`

**请求参数：**
```json
{
  "finalScore": 8.5,
  "comment": "分点清晰，论述完整"
}
```

### 8.4 触发AI批量阅卷

**POST** `/api/grading/papers/{id}/ai-grade`

**说明：** 异步调用通义千问对主观题进行自动批改

---

## 9. 统计模块 `/api/stats`

### 9.1 获取试卷成绩报告

**GET** `/api/stats/paper/{id}/report`

**响应示例：**
```json
{
  "code": 200,
  "data": {
    "paperId": 1,
    "paperTitle": "Java程序设计期末考试",
    "totalScore": 100,
    "passScore": 60,
    "totalCount": 50,
    "passCount": 42,
    "failCount": 8,
    "passRate": 84.00,
    "avgScore": 72.5,
    "maxScore": 98,
    "minScore": 35,
    "scoreDistribution": [
      {"label": "0-59", "count": 8},
      {"label": "60-69", "count": 10},
      {"label": "70-79", "count": 15},
      {"label": "80-89", "count": 12},
      {"label": "90-100", "count": 5}
    ]
  }
}
```

### 9.2 获取成绩排名

**GET** `/api/stats/paper/{id}/rank`

### 9.3 学生历史成绩

**GET** `/api/stats/student/history`

---

## 10. 字典模块 `/api/dicts`

### 10.1 按类型查询字典

**GET** `/api/dicts/type/{type}`

**type可选值：** CLASS（班级）、MAJOR（专业）、SUBJECT（科目）、SEMESTER（学期）

### 10.2 创建字典

**POST** `/api/dicts`

**请求参数：**
```json
{
  "type": "CLASS",
  "code": "CLASS_2024_04",
  "name": "2024级人工智能1班",
  "sortOrder": 4,
  "status": 1
}
```

---

## 11. Swagger文档

启动后端后访问：

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

可使用Swagger UI在线测试所有接口。

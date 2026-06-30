# 智能在线考试系统 - 部署指南

## 1. 环境要求

### 1.1 开发环境

| 软件 | 版本 | 说明 |
|------|------|------|
| JDK | 17+ | OpenJDK 或 Oracle JDK |
| Node.js | 18+ | LTS版本 |
| MySQL | 8.0+ | 主数据库 |
| Redis | 7.x+ | 缓存/草稿/限流 |
| Maven | 3.8+ | 后端构建 |
| IDE | IntelliJ IDEA / VS Code | 开发工具 |

### 1.2 生产环境

| 软件 | 版本 | 说明 |
|------|------|------|
| Linux | CentOS 7+ / Ubuntu 20+ | 服务器操作系统 |
| Nginx | 1.20+ | 反向代理/静态资源 |
| JDK | 17+ | 运行环境 |
| MySQL | 8.0 | 主从架构 |
| Redis | 7.x | 哨兵模式 |

---

## 2. 本地开发环境搭建

### 2.1 数据库初始化

```bash
# 1. 登录MySQL
mysql -u root -p

# 2. 执行初始化脚本
source /path/to/smart-exam-system/sql/init.sql

# 3. 验证表结构
USE smart_exam;
SHOW TABLES;
```

### 2.2 Redis启动

```bash
# Windows
redis-server

# Linux
sudo systemctl start redis

# 验证连接
redis-cli ping
# 返回 PONG 表示成功
```

### 2.3 后端启动

```bash
# 1. 进入后端目录
cd smart-exam-system/backend

# 2. 修改配置（可选）
# 编辑 src/main/resources/application-dev.yml
# 修改数据库密码、Redis连接等

# 3. 安装依赖并启动
mvn clean install
mvn spring-boot:run

# 4. 验证启动
# 访问 http://localhost:8080/swagger-ui.html
```

### 2.4 前端启动

```bash
# 1. 进入前端目录
cd smart-exam-system/frontend

# 2. 安装依赖
npm install

# 3. 启动开发服务器
npm run dev

# 4. 访问应用
# http://localhost:3000
```

### 2.5 测试账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | admin123 |
| 教师 | t1 | 123456 |
| 学生 | s1 | 123456 |

---

## 3. 通义千问API配置

### 3.1 申请API Key

1. 访问 [阿里云DashScope控制台](https://dashscope.console.aliyun.com/)
2. 开通通义千问服务
3. 创建API Key

### 3.2 配置方式

**方式一：环境变量（推荐）**

```bash
# Linux/Mac
export QWEN_API_KEY=sk-xxxxxxxxxxxxxxxxxxxxxxxx

# Windows
set QWEN_API_KEY=sk-xxxxxxxxxxxxxxxxxxxxxxxx
```

**方式二：修改配置文件**

```yaml
# backend/src/main/resources/application-dev.yml
ali:
  qwen:
    api-key: sk-xxxxxxxxxxxxxxxxxxxxxxxx  # 替换为实际API Key
```

### 3.3 API调试指南

**测试API连通性：**

```bash
curl -X POST https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation \
  -H "Authorization: Bearer sk-xxxxxxxx" \
  -H "Content-Type: application/json" \
  -d '{
    "model": "qwen-turbo",
    "input": {
      "messages": [
        {"role": "user", "content": "你好"}
      ]
    }
  }'
```

**常见问题：**

| 错误 | 原因 | 解决方案 |
|------|------|----------|
| 401 Unauthorized | API Key无效 | 检查Key是否正确 |
| 429 Too Many Requests | 请求频率超限 | 降低调用频率或升级配额 |
| 500 Internal Error | 服务端异常 | 稍后重试 |
| timeout | 网络超时 | 检查网络或增加timeout配置 |

---

## 4. 生产环境部署

### 4.1 后端打包

```bash
cd smart-exam-system/backend
mvn clean package -Pprod
# 生成 target/smart-exam-backend-1.0.0.jar
```

### 4.2 前端打包

```bash
cd smart-exam-system/frontend
npm run build
# 生成 dist/ 目录
```

### 4.3 服务器部署

**目录结构：**
```
/data/smart-exam/
├── backend/
│   ├── smart-exam-backend-1.0.0.jar
│   ├── application-prod.yml
│   └── start.sh
├── frontend/
│   └── dist/
├── uploads/
├── logs/
└── nginx/
    └── smart-exam.conf
```

**启动脚本 start.sh：**
```bash
#!/bin/bash
cd /data/smart-exam/backend

# JVM参数
JAVA_OPTS="-Xms512m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=256m"

# 环境变量
export DB_HOST=localhost
export DB_PORT=3306
export DB_USERNAME=smart_exam
export DB_PASSWORD=your_password
export REDIS_HOST=localhost
export REDIS_PORT=6379
export QWEN_API_KEY=sk-xxxxxxxx

# 启动应用
nohup java $JAVA_OPTS -jar smart-exam-backend-1.0.0.jar \
  --spring.profiles.active=prod \
  > /data/smart-exam/logs/app.log 2>&1 &

echo "应用已启动，PID: $!"
```

### 4.4 Nginx反向代理配置

```nginx
# /etc/nginx/conf.d/smart-exam.conf

upstream backend {
    server 127.0.0.1:8080;
    keepalive 32;
}

server {
    listen 80;
    server_name exam.yourdomain.com;  # 替换为实际域名

    # 前端静态资源
    location / {
        root /data/smart-exam/frontend/dist;
        index index.html;
        try_files $uri $uri/ /index.html;

        # 缓存静态资源
        location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff|woff2|ttf)$ {
            expires 7d;
            add_header Cache-Control "public, immutable";
        }
    }

    # 后端API代理
    location /api/ {
        proxy_pass http://backend;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;

        # 超时配置
        proxy_connect_timeout 30s;
        proxy_send_timeout 60s;
        proxy_read_timeout 60s;

        # 文件上传大小限制
        client_max_body_size 10m;
    }

    # Swagger文档（可选，生产环境建议关闭）
    location /swagger-ui/ {
        proxy_pass http://backend;
    }

    # Gzip压缩
    gzip on;
    gzip_types text/plain text/css application/json application/javascript text/xml application/xml application/xml+rss text/javascript;
    gzip_min_length 1024;
    gzip_comp_level 6;

    # 安全头
    add_header X-Frame-Options "SAMEORIGIN" always;
    add_header X-Content-Type-Options "nosniff" always;
    add_header X-XSS-Protection "1; mode=block" always;

    # SSL配置（推荐）
    # listen 443 ssl;
    # ssl_certificate /etc/nginx/ssl/cert.pem;
    # ssl_certificate_key /etc/nginx/ssl/key.pem;
}
```

**部署命令：**
```bash
# 1. 复制前端文件
cp -r frontend/dist/* /data/smart-exam/frontend/dist/

# 2. 启动后端
chmod +x /data/smart-exam/backend/start.sh
/data/smart-exam/backend/start.sh

# 3. 检查Nginx配置
sudo nginx -t

# 4. 重载Nginx
sudo nginx -s reload

# 5. 验证访问
curl http://exam.yourdomain.com
```

---

## 5. 前后端联调顺序

### 5.1 联调流程

```
1. 启动MySQL → 导入init.sql
2. 启动Redis
3. 启动后端 → 验证Swagger文档
4. 启动前端 → 访问localhost:3000
5. 测试登录 → 使用测试账号
6. 测试各模块功能
```

### 5.2 接口联调检查清单

| 模块 | 接口 | 测试方法 |
|------|------|----------|
| 认证 | POST /api/auth/login | Swagger或Postman |
| 用户 | GET /api/users | 前端页面操作 |
| 题库 | POST /api/questions/import | 上传Excel文件 |
| 试卷 | POST /api/papers | 前端创建试卷 |
| 考试 | POST /api/exam/start/{id} | 学生端开始考试 |
| 阅卷 | POST /api/grading/papers/{id}/ai-grade | 教师端触发AI阅卷 |

### 5.3 常见联调问题

| 问题 | 原因 | 解决方案 |
|------|------|----------|
| CORS跨域错误 | 前后端端口不同 | 检查Vite代理配置 |
| 401未登录 | Token未携带 | 检查Axios拦截器 |
| 接口404 | 路径不匹配 | 检查Controller路径 |
| 数据库连接失败 | 配置错误 | 检查application.yml |
| Redis连接失败 | Redis未启动 | 启动Redis服务 |

---

## 6. 生产环境检查清单

- [ ] 数据库已初始化，测试数据已清理
- [ ] Redis已配置密码
- [ ] API Key已配置为环境变量
- [ ] Nginx已配置SSL证书
- [ ] 防火墙已开放80/443端口
- [ ] 日志目录已创建并配置权限
- [ ] 上传目录已创建并配置权限
- [ ] JVM参数已根据服务器配置调整
- [ ] 数据库连接池参数已优化
- [ ] 已配置定时备份策略

---

## 7. 监控与维护

### 7.1 日志查看

```bash
# 应用日志
tail -f /data/smart-exam/logs/app.log

# Nginx日志
tail -f /var/log/nginx/access.log
tail -f /var/log/nginx/error.log
```

### 7.2 服务状态检查

```bash
# 检查Java进程
ps aux | grep java

# 检查端口占用
netstat -tlnp | grep 8080

# 检查Redis状态
redis-cli info server

# 检查MySQL状态
mysql -u root -p -e "SHOW STATUS LIKE 'Threads_connected';"
```

### 7.3 备份策略

```bash
# 数据库备份（每日）
mysqldump -u root -p smart_exam > /backup/smart_exam_$(date +%Y%m%d).sql

# 上传文件备份
tar -czf /backup/uploads_$(date +%Y%m%d).tar.gz /data/smart-exam/uploads/
```

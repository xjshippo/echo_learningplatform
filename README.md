# Echo 学习平台

> 郑州大学（ZZU）大学生创新创业训练计划项目 —— 基于微服务架构与协同过滤推荐算法的个性化学习平台

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.2.2-brightgreen)](https://spring.io/projects/spring-boot)  [![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-Hoxton.SR1-green)](https://spring.io/projects/spring-cloud)  [![Vue](https://img.shields.io/badge/Vue-2.x/3.x-blue)](https://vuejs.org/)  [![License](https://img.shields.io/badge/License-MIT-yellow)](LICENSE)

---

## 项目简介

Echo 学习平台是一个基于**微服务架构**的个性化在线学习系统，结合**协同过滤推荐算法**，为学生提供文章阅读、视频学习、资源下载、题库练习、社区讨论等一站式学习服务。前端采用微信小程序（uni-app + Vue2）和管理后台（Vue3 + Element Plus），后端基于 Spring Cloud Alibaba 微服务生态构建。

本项目作为**郑州大学大学生创新创业训练计划项目**，旨在探索微服务技术在智慧教育领域的应用，培养学生的工程实践与创新能力。

---

## 技术选型

### 后端技术

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 2.2.2 | 基础框架 |
| Spring Cloud | Hoxton.SR1 | 微服务治理 |
| Spring Cloud Alibaba | 2.1.0 | 微服务组件（Nacos） |
| Nacos | 2.0.3 | 服务注册与配置中心 |
| MyBatis | 2.1.1 | ORM 框架 |
| PageHelper | - | 分页插件 |
| Druid | - | 数据库连接池 |
| JWT (jjwt) | 0.9.1 | Token 身份认证 |
| MariaDB | 10.5 | 关系型数据库 |
| Redis | 6.2 | 缓存 |
| Swagger | - | API 文档 |
| WebSocket | - | 实时通信 |

### 前端技术

| 技术 | 说明 |
|------|------|
| uni-app + Vue2 + Vuex | 微信小程序端 |
| uView UI | 小程序 UI 组件库 |
| Vue3 + Vuex + Vue Router | 管理后台 |
| Element Plus | 后台 UI 组件库 |
| SCSS | CSS 预处理 |

---

## 项目架构

项目采用微服务架构设计，共拆分为 8 个独立微服务模块：

```
echo_learning_platform (父工程)
├── rc_personal      # 用户服务 (8081) - 登录/注册/个人信息
├── rc_indexStudy    # 首页服务 (8082) - 文章/视频/资源/推荐
├── rc_comment       # 评论服务 (8083) - 评论/回复/点赞
├── rc_question      # 题库服务 (8084) - 题目/题组/答题
├── rc_message       # 消息服务 (8085) - 通知/私信/WebSocket
├── rc_log           # 日志服务 (8086) - 操作日志记录
├── rc_order         # 订单服务 (8087) - 收藏/浏览记录
├── rc_manage        # 管理服务 (8091) - 后台管理/文章审核
└── rc_common        # 公共模块 - 通用工具类/POJO
```

**API 网关**: Nginx 反向代理 + CORS 处理

**服务架构图**:

```
微信小程序 / 管理后台
        │
        ▼
    Nginx (80)
        │
        ├── /personal  → rc_personal:8081
        ├── /index     → rc_indexStudy:8082
        ├── /comment   → rc_comment:8083
        ├── /question  → rc_question:8084
        ├── /message   → rc_message:8085
        ├── /log       → rc_log:8086
        ├── /order     → rc_order:8087
        ├── /manage    → rc_manage:8091
        └── /files     → 静态文件服务
```

---

## 核心功能

### 个性化推荐（协同过滤）

基于用户的协同过滤推荐算法，通过分析用户历史评分数据计算用户间相似度，预测用户对未评分资源的喜好程度，实现个性化文章与资源推荐。针对冷启动、稀疏性、可扩展性三大问题进行了算法优化。

### 文章 & 视频 & 资源

- 文章发布（富文本 + 图片上传）、点赞、评论、搜索
- 视频在线播放（支持进度追踪）
- 资源文件下载（支持断点续传、下载进度条）
- 分页懒加载

### 题库系统

- 单选题/多选题，按难度分类
- 答题进度条、动画切屏、防越界
- 自动阅卷，生成答题报告
- 错题本管理

### 社区 & 消息

- 学习社区发帖/回帖
- WebSocket 实时私信
- 系统通知、点赞通知、回复通知
- 文章审核（后台管理）

### 用户体系

- 微信一键登录 / 手机号验证码登录 / 账号密码登录
- JWT Token 身份认证
- 个人信息管理

---

## 项目部署

### 环境要求

- JDK 1.8+
- Maven 3.6+
- MariaDB 10.5+
- Redis 6.2+
- Nacos 2.0.3

### 快速开始

1. **克隆项目**
```bash
git clone https://github.com/question7892/echo_learningplatform.git
```

2. **初始化数据库**
```sql
-- 执行项目中的 SQL 脚本创建数据库和表结构
source schema.sql;
```

3. **修改配置**
修改各模块 `application.yml` 中的数据库连接、Redis 地址、Nacos 地址等配置。

4. **启动 Nacos**
```bash
# 单机模式启动
startup.cmd -m standalone
```

5. **编译打包**
```bash
mvn clean package -DskipTests
```

6. **启动服务**
```bash
java -jar rc_personal/target/rc_personal-1.0-SNAPSHOT.jar
java -jar rc_indexStudy/target/rc_indexStudy-1.0-SNAPSHOT.jar
# ... 依次启动各模块
```

---

## 项目成员

| 姓名 | 分工 |
|------|------|
| xxx | 项目负责人，后端架构设计、协同过滤算法实现 |
| xxx | 前端小程序开发、UI 设计 |
| xxx | 后台管理系统开发、数据库设计 |

---

## 致谢

本项目是**郑州大学大学生创新创业训练计划**的成果之一。感谢指导老师的悉心指导，感谢团队成员的共同努力。

在学习与开发过程中，参考了以下优秀资源：
- Spring 官方文档
- MyBatis 官方文档
- uni-app 官方文档
- 互联网公开的优质学习资源

---

## License

MIT License. 详见 [LICENSE](LICENSE) 文件。

# 学生管理系统

一个功能完整的学生信息管理系统，支持学生信息的增删改查操作。采用前后端分离架构，完全容器化部署。

## 🛠 技术栈

### 后端
- **框架**: Spring Boot 2.7.18
- **ORM**: Spring Data JPA (Hibernate)
- **数据库**: MySQL 8.0
- **API文档**: Swagger/OpenAPI
- **构建工具**: Maven

### 前端
- **框架**: Vue 2.6.14
- **UI组件库**: Element UI + TailwindCSS
- **HTTP客户端**: Axios
- **构建工具**: Vue CLI + Webpack
- **服务器**: Nginx

### 容器化
- **编排**: Docker Compose

## ✨ 功能特性

### 用户认证系统
- ✅ **用户注册**: 用户名、邮箱、密码
- ✅ **用户登录**: JWT Token 认证
- ✅ **权限管理**: 普通用户 / 管理员角色
- ✅ **登录状态保持**: Cookie + Vuex状态管理
- ✅ **路由守卫**: 未登录自动跳转登录页

### 后台管理系统
- ✅ **左侧导航菜单**: 工作台、学生管理、用户管理
- ✅ **顶部用户信息栏**: 用户名、角色、退出登录
- ✅ **工作台 (Dashboard)**: 统计卡片、快捷操作入口
- ✅ **响应式布局**: 适配桌面和移动端

### 学生管理 (CRUD)
- ✅ **添加学生**: 姓名、学号、性别、班级
- ✅ **查询学生**: 支持按姓名搜索
- ✅ **修改学生信息**
- ✅ **删除学生**
- ✅ **数据验证**: 学号唯一性校验

### 用户管理 (管理员)
- ✅ **查看用户列表**
- ✅ **修改用户角色**: 设置/取消管理员
- ✅ **删除用户**
- ✅ 学生信息的增删改查 (CRUD)
- ✅ 学生信息搜索与筛选
- ✅ 现代化UI设计 (渐变背景、卡片阴影、Hover效果)
- ✅ 表单验证和错误提示
- ✅ 响应式布局 (PC端和移动端)
- ✅ 完全容器化部署
- ✅ 数据库自动初始化 (Seed Data)

## 🚀 启动指南

### 前置要求
- Docker Desktop 已安装并启动
- 系统内存至少 4GB

### 一键启动

1. 克隆或下载本项目到本地

2. 在项目根目录执行：
```bash
docker compose up --build
```

3. 等待容器构建和启动完成（首次启动需要下载依赖，约 5-10 分钟）

4. 启动成功后，访问前端应用

## 🔗 服务地址

- **前端界面**: http://localhost:3000
- **后端API**: http://localhost:8080/api/students
- **Swagger文档**: http://localhost:8080/swagger-ui/
- **数据库**: localhost:3306
  - 用户名: `root`
  - 密码: `root123456`
  - 数据库: `student_db`

## 📊 初始化数据

系统启动后会自动初始化8条示例学生数据，包含：
- 姓名、学号、性别、班级信息
- 涵盖计算机科学、软件工程、人工智能、数据科学等班级

## 🧪 功能测试

### 查询功能
- 打开 http://localhost:3000，查看学生列表
- 初始化数据应自动加载显示

### 搜索功能
- 在搜索框输入学生姓名（如 "张三"）
- 点击搜索按钮或按回车键

### 添加功能
1. 点击"添加学生"按钮
2. 填写表单（姓名、学号、性别、班级）
3. 点击"确定"提交

### 编辑功能
1. 在学生列表中点击"编辑"按钮
2. 修改学生信息
3. 点击"确定"保存

### 删除功能
1. 在学生列表中点击"删除"按钮
2. 确认删除操作

## 📁 项目结构

```
681/
├── backend/                    # Spring Boot后端
│   ├── src/
│   │   ├── main/java/com/student/
│   │   │   ├── entity/        # 实体类
│   │   │   ├── repository/    # 数据访问层
│   │   │   ├── service/       # 业务逻辑层
│   │   │   ├── controller/    # 控制器
│   │   │   └── config/        # 配置类
│   │   └── resources/
│   │       ├── application.yml
│   │       └── data.sql       # 初始化数据
│   ├── Dockerfile
│   └── pom.xml
├── frontend/                   # Vue前端
│   ├── src/
│   │   ├── views/             # 页面组件
│   │   ├── api/               # API封装
│   │   └── utils/             # 工具函数
│   ├── Dockerfile
│   ├── nginx.conf
│   └── package.json
├── docker-compose.yml          # Docker编排配置
├── .dockerignore
├── .gitignore
└── README.md
```

## 🛑 停止服务

```bash
# 停止所有容器
docker compose down

# 停止并删除所有数据（包括数据库数据）
docker compose down -v
```

## 🔄 重启服务

```bash
# 重启所有容器
docker compose restart

# 重新构建并启动
docker compose up --build
```

## 📝 开发说明

### 查看日志
```bash
# 查看所有服务日志
docker compose logs

# 查看特定服务日志
docker compose logs frontend
docker compose logs backend
docker compose logs db
```

### 进入容器
```bash
# 进入数据库容器
docker exec -it student-mysql mysql -uroot -proot123456 student_db
```

## 🎨 UI设计特点

- **现代渐变背景**: 使用紫色渐变营造科技感
- **玻璃态设计**: 半透明卡片配合模糊效果
- **微交互动画**: Hover效果和状态反馈
- **圆角与阴影**: 提升视觉层次感
- **响应式布局**: 适配PC端和移动端

## 📄 许可证

MIT License
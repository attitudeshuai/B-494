# 校园失物招领系统 (Campus Lost and Found System)

这是一个基于 Vue 3 和 Spring Boot 构建的校园失物招领平台，旨在帮助师生高效地发布和寻找遗失物品。

## 🛠 技术栈

- **Frontend**: Vue 3 + Vite + Bootstrap 5
- **Backend**: Java 17 + Spring Boot 3.2.1 + MyBatis
- **Database**: MySQL 8.0
- **Deployment**: Docker Compose

## 🚀 快速启动 (Docker)

1. **确保环境准备就绪**：请确保本地已安装并运行 [Docker Desktop](https://www.docker.com/products/docker-desktop/)。
2. **启动项目**：在项目根目录下打开终端，执行以下命令：
   ```bash
   docker-compose up -d --build
   ```
   > 第一次构建可能需要几分钟时间下载依赖。

3. **查看日志** (可选)：
   ```bash
   docker-compose logs -f
   ```

## 🔌 Services (服务列表)

| 服务名称 | 地址 | 说明 |
| :--- | :--- | :--- |
| **Frontend** | [http://localhost:3000](http://localhost:3000) | 用户界面 |
| **Backend** | [http://localhost:8080](http://localhost:8080) | API 接口服务 |
| **MySQL** | localhost:3307 | 数据库 (开发调试用) |

## 🧪 Verification (基本验证方式)

1. **访问前端**：打开浏览器访问 [http://localhost:3000](http://localhost:3000)，应能看到系统首页。
2. **注册用户**：点击右上角“注册”，创建一个普通用户账号。
3. **用户登录**：使用注册的账号登录，尝试发布一条“我丢了东西”的信息。
4. **管理员登录**：访问 [http://localhost:3000/admin/login](http://localhost:3000/admin/login) (或通过URL直接跳转)，使用测试管理员账号登录后台。

## 👤 测试账号 (如无数据)

如果数据库为空，可以使用以下预置/后备账号：

- **管理员 (Admin)**
  - 用户名: `admin`
  - 密码: `123456`
  > *注：这是开发环境的后备管理员账号，拥有系统最高权限。*

- **普通用户 (User)**
  - 请直接在前端页面点击“注册”自行创建账号。

## 📷 功能介绍

- **失物/招领发布**：支持上传图片、地点、时间等详细信息。
- **搜索与筛选**：按关键词或分类快速查找物品。
- **私信沟通**：支持用户间实时私信对话，自动聚合对话记录。
- **个人中心**：管理自己发布的物品和历史消息。
- **后台管理**：管理员可审核帖子、管理用户状态（禁用/启用）及系统公告。

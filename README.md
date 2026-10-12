# 融合管理系统平台 · Fusion Management Platform

<div align="center">

**一套开箱即用的 SpringBoot 3 + MyBatis-Plus + Vue3 前后端分离管理系统脚手架**

后端标准三层架构 · 统一返回结果 · JWT 鉴权 · 前台用户端 + 后台管理端分离 · 数据库数据实时同步

![SpringBoot](https://img.shields.io/badge/SpringBoot-3.2.5-6DB33F?logo=springboot&logoColor=white)
![MyBatis-Plus](https://img.shields.io/badge/MyBatis--Plus-3.5.5-important)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql&logoColor=white)
![Vue](https://img.shields.io/badge/Vue-3.5-4FC08D?logo=vuedotjs&logoColor=white)
![Vite](https://img.shields.io/badge/Vite-5.4-646CFF?logo=vite&logoColor=white)
![Element Plus](https://img.shields.io/badge/Element%20Plus-2.8-409EFF?logo=element&logoColor=white)
![JDK](https://img.shields.io/badge/JDK-17%2B-007396?logo=openjdk&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-green)

</div>

---

## 一、这是什么（30 秒看懂）

一个**拿来就能二次开发的企业管理后台骨架**。它不绑定任何具体业务，只把「**后台系统一定会用到、每次重写都很烦**」的那部分东西一次性搭好：

| 你已经可以直接拿去用的能力 | 说明 |
| --- | --- |
|  双端登录 | 同一个登录页，「普通用户 / 管理员」滑块切换，两套账号体系、两张表、各自独立的 token |
|  路由级权限守卫 | 管理员进不了前台、用户进不了后台，越权访问自动拦截并重定向 |
|  JWT 无状态鉴权 | 拦截器解析 token → 写入 `UserContext`（ThreadLocal），Service 层随时取当前登录人 |
|  统一返回结果 | 所有接口一律 `{code, message, data, timestamp}`，前端只判断 `code === 200` |
|  统一异常处理 | 业务异常 / 参数校验异常 / 系统异常全部转成同一套结构，前端不用写 N 种兜底 |
|  数据库数据同步 | 后台所有增删改操作经 MyBatis-Plus 直写 MySQL，前端表格分页/关键字/状态筛选实时回查；登录自动回写 `last_login`，`create_time` / `update_time` 自动维护 |
|  逻辑删除 | 全局配置 `deleted` 字段，删除只是打标记，数据可恢复 |
|  在线接口文档 | 集成 Knife4j，`http://localhost:8080/doc.html` 直接调试 |
|  现成的漂亮 UI | 前台毛玻璃顶栏 + 后台红色渐变顶栏/侧边菜单，登录页滑块动画、记住账号、一键填充体验账号 |

如果你是**实习生 / 应届生 / 刚接手后台需求的开发**，这个项目的价值是：打开就能跑，照着 `用户管理` 这个模块抄一遍，就能做出 `订单管理` `商品管理` 等任意 CRUD 模块。

---

## 二、技术选型

### 后端 `fusion-backend`

| 技术 | 版本 | 用途 |
| --- | --- | --- |
| Spring Boot | 3.2.5 | 基础框架 |
| Java | 17（实测 21 正常） | 运行环境 |
| MyBatis-Plus | 3.5.5 | ORM，单表 CRUD 零 SQL |
| MySQL Connector/J | 随 Spring Boot 管理 | 驱动 |
| MySQL Server | 8.0+ | 数据库 |
| JWT（jjwt） | 0.11.5 | 登录令牌签发 / 校验 |
| Knife4j（OpenAPI 3） | 4.5.0 | 在线接口文档 |
| spring-boot-starter-validation | 随 Spring Boot 管理 | `jakarta.validation` 参数校验 |
| Lombok | 随 Spring Boot 管理 | 消除样板代码 |
| HikariCP | 内置 | 数据库连接池 |


### 前端 `fusion-frontend`

| 技术 | 版本 | 用途 |
| --- | --- | --- |
| Vue | 3.5 | 渐进式前端框架（`<script setup>` 组合式 API） |
| Vite | 5.4 | 构建工具，秒级热更新 |
| Vue Router | 4.4 | 路由 + 前置守卫做权限控制 |
| Pinia | 2.2 | 登录状态管理 |
| Axios | 1.7 | 请求封装 + 统一响应拦截 |
| Element Plus | 2.8 | UI 组件库 |
| @element-plus/icons-vue | 2.3 | 图标库 |

---

## 三、目录结构

```
fusion-management-platform/
├── README.md                       #  你正在看的这份说明
├── LICENSE                         # MIT 开源许可
├── .gitignore                      # 忽略 node_modules / target / dist / .idea
│
├── sql/
│   └── fusion_platform.sql         # 一键建库建表 + 初始数据（含默认账号）
│
├── fusion-backend/                 # ===== 后端：标准三层架构 =====
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/fusion/
│       │   ├── FusionApplication.java      # 启动类
│       │   │
│       │   ├── common/                     # ★ 公共层（跨模块复用）
│       │   │   ├── Result.java             #   统一返回结果封装
│       │   │   ├── ResultCode.java         #   统一状态码枚举（分段设计）
│       │   │   ├── PageResult.java         #   统一分页结果结构
│       │   │   ├── BusinessException.java  #   业务异常（可被统一处理）
│       │   │   ├── GlobalExceptionHandler.java # 全局异常处理器
│       │   │   ├── LoginUser.java          #   当前登录人对象
│       │   │   └── UserContext.java        #   ThreadLocal 上下文（取当前登录人）
│       │   │
│       │   ├── config/                     # 配置类
│       │   │   ├── MybatisPlusConfig.java  #   分页插件 + 逻辑删除
│       │   │   ├── WebMvcConfig.java       #   CORS 跨域 + 注册 AuthInterceptor
│       │   │   └── Knife4jConfig.java      #   接口文档分组配置
│       │   │
│       │   ├── interceptor/
│       │   │   └── AuthInterceptor.java    # ★  JWT 鉴权拦截器
│       │   │
│       │   ├── entity/                     # ┐ 数据实体（与表一一对应）
│       │   ├── mapper/                     # │ 数据访问层（继承 BaseMapper）
│       │   ├── service/ + service/impl/    # ├ 业务逻辑层（接口 + 实现）
│       │   ├── controller/                 # ┘ 控制层（只接收参数、封装结果）
│       │   │
│       │   ├── dto/                        # 入参对象（LoginDTO / RegisterDTO / PasswordDTO）
│       │   ├── vo/                         # 出参对象（LoginVO）
│       │   └── util/                       # JwtUtil / PasswordUtil
│       │
│       └── resources/
│           ├── application.yml             # ★ 数据源、JWT、CORS、MyBatis-Plus 配置
│           └── mapper/                     # 自定义 SQL（当前为空，复杂查询往这放）
│
└── fusion-frontend/                # ===== 前端 =====
    ├── index.html
    ├── package.json
    ├── vite.config.js                      # 含 /api → localhost:8080 开发代理
    └── src/
        ├── main.js                         # 入口：注册 Pinia / Router / ElementPlus
        ├── App.vue                         # 根组件
        ├── api/
        │   ├── request.js                  # ★ axios 封装（自动带 token、401 统一处理）
        │   ├── auth.js                     #   登录 / 注册 / 个人信息 / 改密
        │   ├── user.js                     #   用户 CRUD
        │   ├── admin.js                    #   管理员 CRUD
        │   └── dashboard.js                #   后台首页看板
        ├── router/index.js                 # ★ 路由表 + 全局前置守卫（权限）
        ├── store/user.js                   # Pinia：登录态持久化
        ├── utils/auth.js                   # localStorage 读写 token / 用户信息
        ├── assets/                         # 图片资源
        ├── styles/global.css               # CSS 变量 + 全局样式
        └── views/
            ├── Login.vue                   # ★ 高颜值登录页（身份切换滑块）
            ├── Register.vue                # 前台注册
            ├── 404.vue
            ├── front/                      # ---- 前台（普通用户） ----
            │   ├── Front.vue               #   布局：毛玻璃顶栏 + 内容区
            │   ├── Home.vue                #   首页（欢迎横幅 / 数据卡片 / 系统动态）
            │   ├── Workspace.vue           #   我的工作台（进度卡片）
            │   └── Profile.vue             #   个人中心（头像/昵称/性别/邮箱/手机）
            └── back/                       # ---- 后台管理（管理员） ----
                ├── Back.vue                #   布局：红色渐变顶栏 + 白色侧边菜单
                ├── Home.vue                #   数据看板（总用户数 / 启用数 / 管理员数）
                ├── Admin.vue               #   角色管理 → 管理员管理
                ├── User.vue                #   角色管理 → 用户管理（含头像列）
                ├── AdminPerson.vue         #   账号设置 → 个人信息
                └── Password.vue            #   账号设置 → 修改密码
```

---

## 四、核心设计（这部分是整个脚手架的灵魂）

### 1. 三层架构 + 两个横切组件

```
HTTP 请求
   │
   ▼
[Controller 表现层]  com.fusion.controller
   │   职责边界：只做「接收参数 / @Valid 参数校验 / Result 结果封装」
   │   ✗ 不允许写业务逻辑
   ▼
[Service 业务逻辑层] com.fusion.service (+ impl)
   │   职责边界：业务规则、事务控制(@Transactional)、抛 BusinessException
   ▼
[Mapper 数据访问层]  com.fusion.mapper
   │   职责边界：MyBatis-Plus BaseMapper，单表 CRUD 零 SQL（复杂查询才写 XML）
   ▼
  MySQL(fusion_platform)
```

两个横切组件：

- **`interceptor/AuthInterceptor`**：从 `Authorization: Bearer <token>` 取 JWT → 解析 → 写入 `UserContext`；请求结束后在 `afterCompletion` 里 `clear()`，**必须清理**，否则 Tomcat 线程复用会串号。
- **`common/GlobalExceptionHandler`**：`@RestControllerAdvice` 把所有异常（`BusinessException` / 参数校验 / 未知异常）统一转成 `Result` 结构返回，前端永远只会收到同一种 JSON。

### 2. 数据库数据同步机制

这是本脚手架的实用点之一，具体体现在：

- **写操作即时落库**：后台「用户管理 / 管理员管理」的每个操作（新增、修改、重置密码、删除）都由 Service 层经 MyBatis-Plus 直接写 MySQL，**不存在前端本地缓存脏数据**——操作成功后前端立刻回查分页接口，表格展示的始终是数据库最新状态。
- **登录回写**：任意账号登录成功，`AuthServiceImpl` 会 update 该账号的 `last_login` 字段，库存的是**上一次**登录时间。
- **审计字段自动维护**：`create_time` 取 `DEFAULT CURRENT_TIMESTAMP`，`update_time` 取 `ON UPDATE CURRENT_TIMESTAMP`，不需要在 Java 里手写时间。
- **逻辑删除**：MyBatis-Plus 全局配置 `logic-delete-field: deleted`，调用 `removeById()` 实际执行的是 `UPDATE ... SET deleted = 1`，数据仍在库里，随时可恢复。
- **多字段联合检索**：分页接口支持 `keyword` 同时模糊匹配 `username / nickname / phone / email` 四个字段，配合 `status` 精确筛选。

### 3. 统一返回结果

所有接口一律返回下面这个结构，前端只需判断 `code === 200`：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": { "token": "eyJhbGciOi...", "username": "user" },
  "timestamp": 1760188800000
}
```

对应 `com.fusion.common.Result<T>`，常用静态方法：

| 方法 | 说明 |
| --- | --- |
| `Result.success()` | 成功，无数据 |
| `Result.success(data)` | 成功，带数据 |
| `Result.success(message, data)` | 成功，自定义提示语 |
| `Result.error(ResultCode)` | 失败，用枚举 |
| `Result.error(code, message)` | 失败，自定义 |

### 4. 状态码分段设计

状态码统一在 `ResultCode` 枚举里维护，采用「HTTP 语义 + 业务码分段」，看数字就知道是哪类问题：

| 段 | 含义 | 具体码 |
| --- | --- | --- |
| `2xx` | 成功 | `200` 操作成功 |
| `4xx`/`5xx` | 通用错误 | `400` 参数不合法、`401` 未登录、`403` 无权限、`404` 资源不存在、`405` 请求方式不对、`500` 系统繁忙 |
| `1xxx` | 认证相关 | `1001` 账号或密码错误、`1002` 登录已过期、`1003` 凭证无效、`1004` 账号被禁用、`1005` 账号已注册、`1006` 原密码不正确、`1007` 账号不存在 |
| `2xxx` | 业务相关 | `2001` 数据不存在、`2002` 数据已存在、`2003` 操作失败、`2004` 内置超管不允许删除或禁用 |

> 前端 `request.js` 的响应拦截器会把 `401 / 1002 / 1003` 识别为登录失效，弹窗提示并自动跳回登录页（带回跳参数）。

### 5. JWT 鉴权流程

```
① 前端 POST /api/auth/login  {username, password, loginType: user|admin}
        │
        │  loginType 决定查哪张表
        ├── user  → sys_user
        └── admin → sys_admin
        ▼
② 后端校验：账号存在？密码 MD5 匹配？状态是否启用？
        │  （账号不存在 / 密码错误统一抛 1001，避免暴露账号是否存在）
        ▼
③ 签发 JWT（载荷含 id / username / nickname / type），默认 2 小时有效
        ▼
④ 之后每个请求  Authorization: Bearer <token>
        ▼
⑤ AuthInterceptor 解析 → UserContext.set(loginUser)
        ▼
⑥ Service 层 UserContext.get() 取当前登录人做鉴权 / 记录操作人
        ▼
⑦ afterCompletion → UserContext.clear()   ← 别忘了这一步
```

---

## 五、数据库设计

数据库名 `fusion_platform`，`utf8mb4` 编码，共 **2 张表**，脚本见 [`sql/fusion_platform.sql`](sql/fusion_platform.sql)（整段执行即可，会自动建库）。

### `sys_user` — 系统普通用户表

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `id` | BIGINT PK | 主键，自增 |
| `username` | VARCHAR(50) UNIQUE | 登录账号，唯一 |
| `password` | VARCHAR(64) | 登录密码（MD5 32 位小写） |
| `nickname` | VARCHAR(50) | 昵称 |
| `avatar` | VARCHAR(255) | 头像地址 |
| `email` / `phone` | VARCHAR | 邮箱 / 手机号 |
| `gender` | TINYINT | 性别：0 未知 / 1 男 / 2 女 |
| `status` | TINYINT | 状态：1 启用 / 0 禁用 |
| `last_login` | DATETIME | 最后登录时间 |
| `remark` | VARCHAR(255) | 备注 |
| `deleted` | TINYINT | 逻辑删除：0 正常 / 1 已删除 |
| `create_time` / `update_time` | DATETIME | 自动维护 |

> `password` 字段标注了 `@JsonProperty(access = WRITE_ONLY)`，**只入不出**，不会被 JSON 序列化返回给前端。

### `sys_admin` — 系统管理员表

字段与 `sys_user` 基本一致，区别是：

| 差异点 | 说明 |
| --- | --- |
| `role` | VARCHAR(20)，角色标识：`SUPER_ADMIN` 内置超级管理员 / `ADMIN` 普通管理员 |
| 无 `gender` 字段 | 管理员端不做性别维护 |
| 删除保护 | `SUPER_ADMIN` 不允许被删除或禁用（状态码 `2004`） |

---

## 六、快速开始

### 环境要求

- JDK **17+**（JDK 21 已验证可运行）
- Maven 3.6+（或用 IDEA 内置的）
- Node.js **18+**（建议 20 LTS）
- MySQL **8.0+**

### 1️⃣ 导入数据库

```bash
mysql -uroot -p < sql/fusion_platform.sql
```

或用 Navicat / DataGrip 直接打开 `sql/fusion_platform.sql` 整段执行。

脚本会 `DROP DATABASE IF EXISTS fusion_platform` 后重建，并插入初始账号；末尾还有一段 `SELECT` 校验语句，执行后可以直接看到两张表各有多少条数据。

### 2️⃣ 改数据库连接

编辑 `fusion-backend/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/fusion_platform?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true&rewriteBatchedStatements=true
    username: root
    password: 123456      # ← 改成你的 MySQL 密码
```

### 3️⃣ 启动后端

用 IDEA 打开 `fusion-backend` 目录，等 Maven 依赖下载完，运行 `FusionApplication`。命令行方式：

```bash
cd fusion-backend
mvn clean compile && mvn spring-boot:run
```

启动成功后控制台会打印：

```
============================================================
  融合管理系统平台 · 后端服务启动成功
  API  :  http://localhost:8080
  文档 :  http://localhost:8080/doc.html
============================================================
```

### 4️⃣ 启动前端

```bash
cd fusion-frontend
npm install        # 国内建议加 --registry=https://registry.npmmirror.com
npm run dev
```

浏览器打开 <http://localhost:5173>（`vite.config.js` 里 `open: true` 会自动打开）。

> 前端所有 `/api` 请求通过 Vite 代理转发到 `http://localhost:8080`，所以**没有跨域问题**，生产环境用 Nginx 做同样的转发即可。

### 5️⃣ 登录

登录页底部有「体验账号」卡片，**点一下就自动填充**。

| 身份 | 账号 | 密码 | 登录入口 | 登录后去向 |
| --- | --- | --- | --- | --- |
| **超级管理员** | `admin` | `admin` | 登录页切到「管理员」 | `/back/home` 后台管理中心 |
| 普通管理员 | `manager` | `admin` | 登录页切到「管理员」 | `/back/home` |
| **普通用户** | `user` | `123456` | 登录页切到「普通用户」 | `/front/home` 前台 |
| 普通用户 | `lisa` / `tom` | `123456` | 登录页切到「普通用户」 | `/front/home` |

---

## 七、页面一览

### 登录页 `/login`

- 左侧品牌区：渐变蓝紫背景 + 点阵纹理 + 平台能力介绍
- 右侧表单区：**「普通用户 / 管理员」身份切换滑块动画**、记住账号、一键填充体验账号、密码错误抖动反馈
- 未登录访问受保护路由，会被守卫拦截并带上 `redirect` 参数跳回登录页

### 前台 `front/`（普通用户）

| 路由 | 页面 | 内容 |
| --- | --- | --- |
| `/front/home` | 首页 | 欢迎横幅、数据卡片、平台能力、系统动态 |
| `/front/workspace` | 我的工作台 | 工作项卡片 + 进度条 |
| `/front/profile` | 个人中心 | 头像 / 昵称 / 性别 / 邮箱 / 手机号编辑 |

毛玻璃顶栏 + 右上角头像下拉菜单（个人中心 / 工作台 / 退出登录）。

### 后台 `back/`（管理员）

红色渐变顶栏 + 白色侧边菜单，菜单结构：

```
导航菜单
├── 后台首页            /back/home
└── 角色管理            （可展开）
    ├── 管理员管理       /back/admin   → sys_admin
    └── 用户管理         /back/user    → sys_user（表格带头像列）
账号设置
├── 个人信息            /back/adminPerson
└── 修改密码            /back/password
```

管理表格标配：**分页 + 关键字搜索 + 状态筛选 + 新增/编辑弹窗 + 重置密码 + 逻辑删除**。

---

## 八、接口清单

除 `POST /api/auth/login` 与 `POST /api/auth/register` 外，其余接口均需请求头 `Authorization: Bearer <token>`。

### 认证管理 `01`

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/api/auth/login` | 登录，body 传 `loginType`：`user` / `admin` |
| POST | `/api/auth/register` | 前台用户注册，**注册成功自动登录**，直接返回 token |
| GET | `/api/auth/info` | 当前登录人信息 |
| PUT | `/api/auth/profile` | 更新个人资料（昵称/邮箱/手机/头像/性别） |
| PUT | `/api/auth/password` | 修改密码（需校验原密码） |
| POST | `/api/auth/logout` | 退出（JWT 无状态，前端清 localStorage 即可，此接口用于埋点/审计） |

### 用户管理 `02`

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/api/user/page` | 分页查询，支持 `keyword`（匹配账号/昵称/手机/邮箱）与 `status` |
| GET | `/api/user/{id}` | 用户详情 |
| POST | `/api/user` | 新增用户 |
| PUT | `/api/user` | 修改用户（密码字段会被强制置空，走独立改密接口） |
| PUT | `/api/user/{id}/reset-password` | 重置密码，不传 `newPassword` 默认重置为 `123456` |
| DELETE | `/api/user/{id}` | 删除用户（**逻辑删除**） |
| GET | `/api/user/statistics` | 总数 / 启用数 / 禁用数 |

### 管理员管理 `03`

路径前缀 `/api/admin`，接口与用户管理同构（分页 / 详情 / 新增 / 修改 / 重置密码 / 删除），额外限制：**内置超级管理员不允许删除**。

### 数据看板 `04`

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/api/dashboard/overview` | 返回 `userTotal` / `userEnabled` / `adminTotal` / `adminEnabled` |

> 完整的可调试文档在启动后端后访问 <http://localhost:8080/doc.html>（Knife4j）。

---

## 九、二次开发：怎么加一个新模块

以「**添加商品管理**」为例，照抄 `用户管理` 即可，全程 6 步：

**后端**

1. `sql` 里建 `product` 表（记得带上 `deleted` / `create_time` / `update_time` 三个字段）
2. `entity/Product.java` — 实体类，`@TableName("product")`，id 用 `@TableId(type = IdType.AUTO)`
3. `mapper/ProductMapper.java` — `public interface ProductMapper extends BaseMapper<Product> {}`，**内容为空即可**
4. `service/ProductService.java` + `impl/ProductServiceImpl.java` — 继承 `ServiceImpl<ProductMapper, Product>`，写业务规则
5. `controller/ProductController.java` — 加 `@Tag` `@RestController` `@RequestMapping("/api/product")`，方法体一行 `Result.success(...)`

**前端**

6. `api/product.js` 写接口 → `views/back/Product.vue` 抄 `User.vue` 改字段 → 在 `router/index.js` 的 `/back` children 里加一条路由 → 在 `views/back/Back.vue` 的 `el-menu` 里加一个 `el-menu-item`

> 想改字段校验，加 `spring-boot-starter-validation` 的 `@NotBlank` `@Size` 等注解到 DTO，并在 Controller 参数上加 `@Valid`，异常会被全局处理器自动兜住。

---

## 十、常见问题

| 现象 | 排查方向 |
| --- | --- |
| 前端报「无法连接后端服务」 | 确认 SpringBoot 已启动在 8080；检查 `vite.config.js` 里代理 target 是否正确 |
| 登录后刷新页面回到登录页 | 检查浏览器 localStorage 是否被禁用；或 token 已过 2 小时有效期 |
| 表格一直没数据 | 确认已执行 `fusion_platform.sql`，且 `application.yml` 的数据源连的是同一个库 |
| 中文乱码 | 库表已是 `utf8mb4`；连接串必须带 `characterEncoding=utf8` |
| 管理员打开前台页面被弹回来 | **预期行为**。路由守卫会按 `loginType` 拦截并提示「当前身份无权访问该区域」 |
| 修改用户信息后密码被清空 | 正常。修改接口会把 `password` 强制置 null，密码必须走 `/auth/password` 或重置密码接口 |
| 406/415 或 JSON 解析失败 | 确认请求头是 `Content-Type: application/json;charset=UTF-8`（`request.js` 已统一处理） |
| Maven 依赖下载慢 | 给 Maven 配阿里云镜像；把 `<scope>runtime</scope>` 的驱动依赖当作已声明即可，不需要单独引 |

---

## 十一、生产化建议

脚手架为了**演示直观**刻意做了简化，上生产前请务必调整：

1. **密码算法**：当前是 MD5（`PasswordUtil`），MD5 已被证明不安全。换成 BCrypt / Argon2 加盐慢哈希，并同步更新 SQL 里的初始化密码值。
2. **JWT 密钥**：`application.yml` 的 `fusion.jwt.secret` 请改成随机长串，并用环境变量 / 配置中心注入，不要硬编码进仓库。
3. **Token 续期与踢下线**：当前是固定 2 小时过期、服务端无状态。可引入 Redis 做「滑动续期 + 主动踢下线 + 单设备登录」。
4. **权限细化**：当前只有 `user` / `admin` 两种登录类型。真实项目建议扩展 RBAC（角色 / 权限 / 菜单 三张表 + 关联表）。
5. **跨域收紧**：`fusion.cors.allowed-origins` 现在写的是开发端口，生产要改成实际前端域名，别用 `*`。
6. **接口限流与审计**：登录接口建议加验证码 + 失败次数限流；操作日志建议用 AOP 统一记录。
7. **敏感字段**：`password` 已用 `@JsonProperty(access = WRITE_ONLY)` 只入不出，新增实体时请延续这个习惯。

---

## 十二、许可证

本项目基于 [MIT License](LICENSE) 开源，可自由使用、修改、商用，保留署名即可。

---

## 十三、致谢

站在这些开源项目的肩膀上：[Spring Boot](https://spring.io/projects/spring-boot) · [MyBatis-Plus](https://baomidou.com/) · [Vue.js](https://vuejs.org/) · [Vite](https://vitejs.dev/) · [Element Plus](https://element-plus.org/) · [Knife4j](https://doc.xiaominfo.com/) · [JJWT](https://github.com/jwtk/jjwt)

---

<div align="center">

如果这个脚手架帮你省下了重复搭框架的时间，点个 ⭐ Star 就是最大的鼓励。

</div>

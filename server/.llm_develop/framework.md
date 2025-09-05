# PigHand-AIO 项目架构与开发规范

## 技术栈

- **基础框架**：Spring Boot
- **ORM 框架**：MyBatis-Flex
- **API 文档**：SpringDoc (OpenAPI 3)
- **数据校验**：Hibernate Validator
- **工具库**：Lombok, Jackson
- **日志框架**：SLF4J + Logback
- **测试框架**：JUnit 5 + Mockito

## 系统架构

### 分层架构

项目采用经典的多层架构设计：

1. **控制层 (Controller)**：负责接收请求、参数校验和结果返回
2. **服务层 (Service)**：负责业务逻辑处理
3. **数据访问层 (Mapper)**：负责数据库操作

### 模块划分

系统按业务领域划分为多个模块：

- **base**：基础模块，包含用户、角色、权限等基础功能
- **CMS**：内容管理系统模块
- **ECommerce**：电子商务模块
- **IoT**：物联网模块
- **MKT**：营销模块
- **common**：公共组件模块
- **system**：系统管理模块

## 核心设计理念

### 基类设计

系统提供了一系列基类，用于简化开发：

- **BaseController**：控制器基类，提供通用的请求处理方法
- **BaseServiceImpl**：服务层基类，提供通用的业务逻辑处理方法和数据库操作能力
- **BaseMapper**：数据访问层基类，提供通用的数据库操作方法
- **BaseDomain**：领域模型基类，提供通用的实体属性和方法

### 统一响应

系统使用 `Result` 类封装所有 API 响应，确保响应格式的一致性。

### 自定义注解

系统定义了一系列自定义注解，用于简化开发：

- **@RestController**：扩展 Spring 的 @RestController 注解，增加了文档相关属性
- **@Post/@Get/@Put/@Delete**：HTTP 方法注解，简化 RESTful API 开发
- **@Authorization**：权限控制注解

### 上下文管理

系统使用 `Context` 类管理请求上下文，提供了获取当前登录用户、应用 ID 等方法。

## 开发规范

### 功能开发文件清单

创建一个完整功能模块时，通常需要创建以下文件：

| 文件类型 | 必需性 | 命名规范 | 说明文档 |
|---------|--------|----------|----------|
| **Controller** | 必需 | `XxxController` | `.llm_develop/coding/controller.md` |
| **Service** | 可选* | `XxxService` | `.llm_develop/coding/serviceImpl.md` |
| **Domain** | 必需 | `XxxDomain` | `.llm_develop/coding/domain.md` |
| **VO** | 必需 | `XxxVO` | `.llm_develop/coding/vo.md` |
| **Mapper** | 必需 | `XxxMapper` | `.llm_develop/coding/mapper.md` |
| **Mapper XML** | 可选 | `XxxMapper.xml` | `.llm_develop/coding/resources-mapper.md` |

> **注意**：Service 层非必须，如果业务逻辑简单，可以直接在 Controller 中调用 Mapper。但如果需要 Service 层，建议直接创建实现类并命名为 `XxxService`，无需创建接口。

### 开发流程

1. **创建数据库表**
2. **创建 Domain 实体类** - 对应数据库表结构
3. **创建 Mapper 接口** - 数据访问层
4. **创建 VO 类** - 用于接收请求参数和返回结果
5. **创建 Service 类**（可选）- 业务逻辑处理
6. **创建 Controller 类** - API 接口层
7. **编写单元测试**

### 核心规范要点

- **分层职责**：Controller 负责参数校验和结果返回，Service 负责业务逻辑，Mapper 负责数据访问
- **命名统一**：使用统一的后缀命名（Controller、Service、Domain、VO、Mapper）
- **继承基类**：Service 继承 `BaseServiceImpl`，Mapper 继承 `BaseMapper`
- **注解使用**：合理使用 `@Validated`、`@Transactional`、`@Service` 等注解
- **异常处理**：使用 `ThrowPrompt` 抛出业务异常，全局异常处理器统一处理

详细的编码规范和示例请参考 `.llm_develop/coding/` 目录下的对应文档。

## 安全设计

系统使用 Spring Security 进行安全控制，支持多种认证方式：

- 用户名密码认证
- 手机号认证
- 微信认证

## 扩展设计

系统支持多种扩展点：

- **用户扩展**：通过 UserExtension 表存储用户的扩展信息
- **应用扩展**：支持多应用集成
- **平台扩展**：支持微信、抖音等多平台集成
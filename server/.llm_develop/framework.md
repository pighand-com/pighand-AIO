# PigHand-AIO 项目架构

## 技术栈

- **基础框架**：Spring Boot
- **ORM 框架**：MyBatis-Flex
- **API 文档**：SpringDoc (OpenAPI 3)
- **数据校验**：Hibernate Validator
- **工具库**：Lombok, Jackson

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
- **BaseService/BaseServiceImpl**：服务层基类，提供通用的业务逻辑处理方法
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

## 数据模型设计

### 领域模型 (Domain)

领域模型对应数据库表结构，使用 MyBatis-Flex 的注解进行映射。

### 视图对象 (VO)

视图对象用于接收请求参数和返回响应结果，通常继承自对应的领域模型，并添加额外的属性和校验注解。

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
# PigHand AIO 小程序项目框架介绍

## 项目概述

PigHand AIO 是一个基于 uni-app 框架开发的多端小程序项目，主要包含电商、营销、基础功能等模块。项目采用 Vue 3 + TypeScript 技术栈，支持微信小程序等多个平台。

## 技术栈

- **框架**: uni-app (支持多端开发)
- **前端**: Vue 3 + TypeScript
- **UI组件**: uni-ui + ThorUI (自定义组件库)
- **状态管理**: 基于 uni-app 的本地存储
- **网络请求**: 封装的 request 模块
- **构建工具**: HBuilderX

## 项目结构

```
client-applet/
├── .llm_develop/          # AI开发辅助文档
│   ├── coding/            # 开发示例和说明文档
│   └── framework.md       # 项目框架介绍(本文件)
├── api/                   # API接口定义
│   ├── index.ts          # 接口模块导出
│   ├── user.ts           # 用户相关接口
│   ├── order.ts          # 订单相关接口
│   ├── ticket.ts         # 票务相关接口
│   ├── theme.ts          # 主题相关接口
│   ├── banner.ts         # 轮播图接口
│   ├── common.ts         # 通用接口
│   ├── distribution.ts   # 分销相关接口
│   ├── store.ts          # 门店相关接口
│   ├── lottery.ts        # 抽奖相关接口
│   └── login.ts          # 登录相关接口
├── common/               # 通用工具和配置
│   ├── constant.ts       # 常量配置
│   ├── request.ts        # 网络请求封装
│   ├── login.ts          # 登录逻辑
│   ├── storage.ts        # 本地存储封装
│   ├── share.ts          # 分享功能
│   └── upload.ts         # 文件上传
├── components/           # 组件库
│   ├── custom-button/    # 自定义按钮组件
│   ├── status-bar-gradient/ # 状态栏渐变组件
│   ├── tab-bar/          # 自定义底部导航
│   ├── thorui/           # ThorUI组件库
│   └── user/             # 用户相关组件
├── pages/                # 页面文件
│   ├── base/             # 基础功能页面
│   │   ├── pages/        # 页面文件
│   │   └── static/       # 静态资源
│   ├── ECommerce/        # 电商模块页面
│   │   └── pages/        # 电商相关页面
│   └── MKT/              # 营销模块页面
│       └── pages/        # 营销相关页面
├── static/               # 全局静态资源
├── types/                # TypeScript类型定义
├── uni_modules/          # uni-app插件模块
├── App.vue               # 应用入口组件
├── main.js               # 应用入口文件
├── pages.json            # 页面路由配置
├── manifest.json         # 应用配置清单
├── package.json          # 项目依赖配置
└── tsconfig.json         # TypeScript配置
```

## 核心模块说明

### 1. API接口层 (api/)

项目采用模块化的API接口管理方式：

- **统一导出**: `api/index.ts` 统一导出所有接口模块
- **模块分离**: 按业务功能分离接口文件
- **类型安全**: 使用 TypeScript 确保接口类型安全
- **统一封装**: 基于 `common/request.ts` 的网络请求封装

### 2. 通用工具层 (common/)

- **request.ts**: 网络请求封装，包含错误处理、token管理、响应拦截
- **constant.ts**: 全局常量配置，包含API地址、存储键名等
- **storage.ts**: 本地存储封装，提供统一的存储接口
- **login.ts**: 登录逻辑封装

### 3. 组件层 (components/)

- **自定义组件**: 项目特定的业务组件
- **ThorUI**: 第三方UI组件库
- **uni-ui**: uni-app官方组件库
- **组件复用**: 支持跨页面组件复用

### 4. 页面层 (pages/)

按业务模块组织页面结构：

- **base/**: 基础功能（首页、我的、门店、客服等）
- **ECommerce/**: 电商功能（订单、票务、分销等）
- **MKT/**: 营销功能（抽奖等）

### 5. 路由配置 (pages.json)

- **页面注册**: 所有页面必须在此注册
- **TabBar配置**: 底部导航栏配置
- **全局样式**: 全局页面样式配置
- **easycom**: 组件自动导入配置

## 开发一个新功能的完整流程

### 第一步：需求分析和设计

1. **确定功能模块**: 确定功能属于哪个业务模块（base/ECommerce/MKT）
2. **设计API接口**: 确定需要的后端接口
3. **设计页面结构**: 确定需要创建的页面和组件
4. **确定路由配置**: 确定页面路径和导航方式

### 第二步：创建或修改文件清单

根据功能复杂度，通常需要创建或修改以下文件：

#### 必需文件：

1. **API接口文件** - `api/[module].ts`
   - 查看示例：[api-interface.md](./coding/api-interface.md)
   - 定义与后端交互的接口方法

2. **页面文件** - `pages/[module]/pages/[page-name].vue`
   - 查看示例：[page-component.md](./coding/page-component.md)
   - 实现页面UI和业务逻辑

3. **路由配置** - `pages.json`
   - 查看示例：[route-config.md](./coding/route-config.md)
   - 注册新页面路由

#### 可选文件：

4. **自定义组件** - `components/[component-name]/[component-name].vue`
   - 查看示例：[custom-component.md](./coding/custom-component.md)
   - 创建可复用的业务组件

5. **类型定义** - `types/[module].ts`
   - 查看示例：[type-definition.md](./coding/type-definition.md)
   - 定义TypeScript类型

6. **通用工具** - `common/[utility].ts`
   - 查看示例：[common-utility.md](./coding/common-utility.md)
   - 添加通用工具函数

7. **静态资源** - `static/` 或 `pages/[module]/static/`
   - 查看示例：[static-assets.md](./coding/static-assets.md)
   - 添加图片、图标等静态资源

### 第三步：开发实施

1. **创建API接口**: 在对应模块的API文件中添加接口方法
2. **创建页面组件**: 实现页面UI和交互逻辑
3. **配置路由**: 在pages.json中注册页面
4. **测试功能**: 确保功能正常运行
5. **优化和调试**: 性能优化和bug修复

### 第四步：集成和部署

1. **更新API导出**: 在`api/index.ts`中导出新的接口模块
2. **更新组件注册**: 如有新组件，确保正确注册
3. **测试完整流程**: 端到端功能测试
4. **构建和发布**: 使用HBuilderX构建和发布

## 开发规范

### 1. 命名规范

- **文件名**: 使用kebab-case（短横线分隔）
- **组件名**: 使用PascalCase（大驼峰）
- **变量名**: 使用camelCase（小驼峰）
- **常量名**: 使用UPPER_SNAKE_CASE（大写下划线）

### 2. 代码规范

- **使用TypeScript**: 所有新代码使用TypeScript
- **组件化开发**: 复用性强的UI封装为组件
- **API统一管理**: 所有接口调用通过api模块
- **错误处理**: 统一的错误处理和用户提示

### 3. 目录规范

- **按模块组织**: 相关文件放在同一模块目录下
- **静态资源**: 模块特定资源放在模块目录，通用资源放在根static目录
- **组件分类**: 业务组件和通用组件分别管理

## 开发功能文件清单

开发一个新功能时，根据功能复杂度，通常需要创建或修改以下文件：

### 必需文件

1. **API接口文件** - `api/[module].ts`
   - 定义与后端交互的接口方法
   - 开发说明：[api-interface.md](./coding/api-interface.md)

2. **页面文件** - `pages/[module]/pages/[page-name].vue`
   - 实现页面UI和业务逻辑
   - 开发说明：[page-component.md](./coding/page-component.md)

3. **路由配置** - `pages.json`
   - 注册新页面路由
   - 开发说明：[route-config.md](./coding/route-config.md)

### 可选文件

4. **自定义组件** - `components/[component-name]/[component-name].vue`
   - 创建可复用的业务组件
   - 开发说明：[custom-component.md](./coding/custom-component.md)

5. **通用工具** - `common/[utility].ts`
   - 添加通用工具函数
   - 开发说明：[common-utility.md](./coding/common-utility.md)

6. **类型定义** - `types/[module].ts`
   - 定义TypeScript类型

7. **静态资源** - `static/` 或 `pages/[module]/static/`
   - 添加图片、图标等静态资源

### 开发流程

1. **创建API接口**：在对应模块的API文件中添加接口方法
2. **创建页面组件**：实现页面UI和交互逻辑
3. **配置路由**：在pages.json中注册页面
4. **更新API导出**：在`api/index.ts`中导出新的接口模块
5. **测试功能**：确保功能正常运行

## 总结

本项目采用模块化、组件化的开发方式，通过清晰的目录结构和统一的开发规范，确保代码的可维护性和可扩展性。开发新功能时，按照既定的流程和规范，可以快速、高效地完成开发任务。
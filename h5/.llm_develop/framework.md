# 项目开发框架规范

## 项目架构概述

本项目是一个基于 Vue 3 + TypeScript + Element Plus 的后台管理系统，采用模块化架构设计。

### 技术栈

-   **前端框架**: Vue 3 + TypeScript
-   **UI 组件库**: Element Plus
-   **路由管理**: Vue Router
-   **HTTP 请求**: Axios
-   **构建工具**: Vite
-   **样式**: Tailwind CSS
-   **图标**: @icon-park/vue-next

### 项目目录结构

```
src/
├── api/           # API接口定义
├── assets/        # 静态资源
├── common/        # 公共工具和配置
├── components/    # 通用组件
├── pages/         # 页面组件
├── routers/       # 路由配置
└── main.ts        # 入口文件
```

## 新页面开发流程

### 开发步骤

1. **创建 API 接口文件** - 定义后端接口调用
2. **添加路由配置** - 在 routes.ts 中注册新路由
3. **创建页面组件** - 实现具体的页面逻辑和 UI
4. **更新 API 导出** - 在 api/index.ts 中导出新接口

### 页面类型

-   **单页应用页面** (`page_type_single`): 带侧边菜单的管理页面
-   **多页应用页面** (`page_type_multi`): 独立页面（如登录页）

## 核心组件和工具

> **组件库使用说明**: 项目提供了丰富的通用组件和工具函数，具体的组件使用文档请查看 [components.md](./components.md)

### DataManager 组件

-   提供完整的 CRUD 操作界面
-   集成搜索、表格、详情抽屉功能
-   支持自定义操作按钮和表格列

### provideForm 工具

-   统一的表单配置管理
-   支持搜索表单、数据表格、详情表单的配置
-   提供丰富的表单控件类型

### 通用组件

-   `PSearch`: 搜索组件
-   `PDataTable`: 数据表格组件
-   `PDrawer`: 抽屉组件
-   `FormItems`: 表单项组件

## 命名规范

### 文件命名

-   **Vue 组件文件**: PascalCase（如：UserManagement.vue）
-   **TypeScript 文件**: camelCase（如：userManagement.ts）
-   **路由路径**: kebab-case（如：/user-management）
-   **路由名称**: snake_case（如：user_management）

### 变量和函数命名

```typescript
// 普通变量 - camelCase
const userName = 'admin';
const isLoading = false;

// 常量 - UPPER_SNAKE_CASE
const API_BASE_URL = 'http://localhost:3000';

// 事件处理函数 - handle + 动作
const handleClick = () => {};
const handleSubmit = () => {};

// 布尔值判断函数 - is/has/can + 形容词
const isValid = () => {};
const hasPermission = () => {};
```

## 代码组织规范

### Vue 组件结构

```vue
<template>
    <!-- 模板内容 -->
</template>

<script lang="ts" setup>
// 1. 导入依赖
import { ref, reactive, computed, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { api } from '@/api';

// 2. 定义接口和类型
interface FormData {
    name: string;
    email: string;
}

// 3. 定义响应式数据
const formData = reactive<FormData>({
    name: '',
    email: ''
});

// 4. 计算属性
const isFormValid = computed(() => {
    return formData.name && formData.email;
});

// 5. 方法定义
const handleSubmit = async () => {
    // 实现逻辑
};

// 6. 生命周期钩子
onMounted(() => {
    // 初始化逻辑
});
</script>

<style scoped>
/* 组件样式 */
</style>
```

### API 文件结构

```typescript
// 1. 导入依赖
import request from '@/common/request';

// 2. 定义接口类型
interface CreateUserParam {
    name: string;
    email: string;
}

// 3. 定义基础URL
const baseUrl = 'user';

// 4. 导出API对象
export default {
    create: async (param: CreateUserParam) =>
        await request.post(`${baseUrl}`, param),

    find: async (id: number) => await request.get(`${baseUrl}/${id}`),

    query: async (param?: any) => await request.get(`${baseUrl}`, param),

    update: async (param: any) =>
        await request.put(`${baseUrl}/${param.id}`, param),

    del: async (id: number) => await request.del(`${baseUrl}/${id}`)
};
```

## 样式规范

### Tailwind CSS 使用

项目使用 Tailwind CSS，优先使用工具类：

```vue
<template>
    <div
        class="flex items-center justify-between p-4 bg-white rounded-lg shadow">
        <h2 class="text-lg font-semibold text-gray-800">标题</h2>
        <button
            class="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">
            按钮
        </button>
    </div>
</template>
```

### BEM 命名法（自定义样式）

```css
/* Block */
.user-card {
}

/* Element */
.user-card__header {
}
.user-card__body {
}

/* Modifier */
.user-card--active {
}
.user-card--disabled {
}
```

## Git 提交规范

### 提交消息格式

```
<type>(<scope>): <subject>

<body>

<footer>
```

### 类型说明

-   **feat**: 新功能
-   **fix**: 修复 bug
-   **docs**: 文档更新
-   **style**: 代码格式调整
-   **refactor**: 代码重构
-   **test**: 测试相关
-   **chore**: 构建过程或辅助工具的变动

### 示例

```
feat(user): 添加用户管理页面

- 实现用户列表展示
- 添加用户创建功能
- 支持用户状态切换

Closes #123
```

## 错误处理规范

### API 错误处理

```typescript
try {
    const result = await api.createUser(userData);
    ElMessage.success('用户创建成功');
    return result;
} catch (error) {
    console.error('创建用户失败:', error);
    ElMessage.error('创建用户失败，请重试');
    throw error;
}
```

### 表单验证错误

```typescript
const validateForm = (formData: FormData): string[] => {
    const errors: string[] = [];

    if (!formData.name) {
        errors.push('用户名不能为空');
    }

    if (!formData.email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email)) {
        errors.push('请输入有效的邮箱地址');
    }

    return errors;
};
```

## 详细目录结构

### src/api/ - API 接口层

```
api/
├── index.ts              # 统一导出文件
├── common.ts             # 公共接口（验证码、文件上传等）
├── login.ts              # 登录相关接口
├── user.ts               # 用户管理接口
├── banner.ts             # Banner管理接口
├── article.ts            # 文章管理接口
├── category.ts           # 分类管理接口
├── lottery.ts            # 抽奖活动接口
├── order.ts              # 订单管理接口
├── ticket.ts             # 票务管理接口
├── theme.ts              # 主题管理接口
├── store.ts              # 门店管理接口
├── assets.ts             # 素材管理接口
├── tenant.ts             # 租户管理接口
├── role.ts               # 角色管理接口
├── distributionGoodsRule.ts      # 分销商品规则接口
├── distributionSalesperson.ts    # 分销资格接口
└── distributionSales.ts          # 分销销售接口
```

### src/components/ - 通用组件

```
components/
├── Button.vue            # 按钮组件
├── DataManager.vue       # 数据管理组件（核心）
├── DataTable.vue         # 数据表格组件
├── Dialog.vue            # 对话框组件
├── Divider.vue           # 分隔符组件
├── Drawer.vue            # 抽屉组件
├── FormItem.vue          # 表单项组件
├── FormItems.vue         # 表单项集合组件
├── ImageView.vue         # 图片查看组件
├── Menu.vue              # 菜单组件
├── Search.vue            # 搜索组件
└── TEditor.vue           # 富文本编辑器组件
```

### src/pages/ - 页面组件

```
pages/
├── base/                 # 基础功能页面
│   ├── Login.vue         # 登录页面
│   ├── User.vue          # 用户管理
│   └── Store.vue         # 门店管理
├── CMS/                  # 内容管理页面
│   └── Banner.vue        # Banner管理
├── ECommerce/            # 电商相关页面
│   ├── Theme.vue         # 主题管理
│   ├── Ticket.vue        # 票务管理
│   ├── TicketValidation.vue  # 票务核销
│   └── Order.vue         # 订单管理
├── MKT/                  # 营销活动页面
│   └── Lottery.vue       # 抽奖管理
├── distribution/         # 分销相关页面
│   ├── DistributionGoodsRule.vue     # 商品分销规则
│   └── DistributionSalesperson.vue   # 分销资格管理
└── common/               # 通用页面
    └── Assets.vue        # 素材管理
```

### src/common/ - 公共模块

```
common/
├── constant.ts           # 常量定义
├── request.ts            # HTTP请求封装
├── storage.ts            # 本地存储工具
├── provideForm.ts        # 表单配置工具
└── cos.ts                # 对象存储工具
```

## 开发工具配置

### VSCode 推荐插件

```json
{
    "recommendations": [
        "vue.volar",
        "vue.vscode-typescript-vue-plugin",
        "bradlc.vscode-tailwindcss",
        "esbenp.prettier-vscode",
        "dbaeumer.vscode-eslint"
    ]
}
```

### Prettier 配置

```json
{
    "semi": true,
    "singleQuote": true,
    "tabWidth": 4,
    "trailingComma": "none",
    "printWidth": 80,
    "endOfLine": "lf"
}
```

## 性能优化

### 组件懒加载

```typescript
// 路由懒加载
const UserManagement = () => import('@/pages/User/UserManagement.vue');

// 组件懒加载
const HeavyComponent = defineAsyncComponent(
    () => import('@/components/HeavyComponent.vue')
);
```

### 数据缓存

```typescript
// 缓存API响应
const cache = new Map();

const getCachedData = async (key: string, fetcher: () => Promise<any>) => {
    if (cache.has(key)) {
        return cache.get(key);
    }

    const data = await fetcher();
    cache.set(key, data);
    return data;
};
```

## 安全规范

### 输入验证

```typescript
// 前端验证
const sanitizeInput = (input: string): string => {
    return input.trim().replace(/<script[^>]*>.*?<\/script>/gi, '');
};
```

### 权限控制

```typescript
// 路由守卫
router.beforeEach((to, from, next) => {
    const token = getToken();
    const requiresAuth = to.meta.requiresAuth;

    if (requiresAuth && !token) {
        next({ name: 'login' });
    } else {
        next();
    }
});
```

## 开发注意事项

1. **权限控制**: 所有页面默认需要登录验证（`requiresAuth: true`）
2. **图标选择**: 从 @icon-park/vue-next 中选择合适的图标
3. **响应式设计**: 确保页面在不同屏幕尺寸下正常显示
4. **错误处理**: 使用统一的错误处理机制
5. **代码复用**: 优先使用现有组件和工具函数
6. **性能优化**: 使用路由懒加载和组件懒加载
7. **安全规范**: 输入验证、权限控制、敏感信息处理

---

# 开发文件清单和参考指南

## 需要修改的文件类型及参考文档

### 1. API 接口文件 (`src/api/*.ts`)

**参考文档**: `02-API接口开发指南.md`

### 2. 路由配置文件 (`src/routers/*.ts`)

**参考文档**: `01-路由配置指南.md`

-   `src/routers/routes.ts` - 路由配置定义
-   `src/routers/index.ts` - 路由实例和守卫

### 3. 页面组件文件 (`src/pages/**/*.vue`)

**参考文档**: `03-页面组件开发指南.md`

### 4. 通用组件文件 (`src/components/*.vue`)

**参考文档**: `04-通用组件使用指南.md`

### 5. 表单配置文件 (`src/common/provideForm.ts`)

**参考文档**: `05-表单配置指南.md`

### 6. 公共工具文件 (`src/common/*.ts`)

**参考文档**: `06-项目结构和规范.md`

-   `constant.ts` - 常量定义
-   `request.ts` - HTTP 请求封装
-   `storage.ts` - 本地存储工具
-   `cos.ts` - 对象存储工具

### 7. 统一导出文件 (`src/api/index.ts`)

**参考文档**: `02-API接口开发指南.md`

## 开发流程总结

### 新增功能模块的完整步骤：

1. **创建 API 接口** (`02-API接口开发指南.md`)

    - 在 `src/api/` 下创建对应的 `.ts` 文件
    - 定义接口类型和 API 方法
    - 在 `src/api/index.ts` 中导出

2. **配置路由** (`01-路由配置指南.md`)

    - 在 `src/routers/routes.ts` 中添加路由配置
    - 设置权限和元信息
    - 使用懒加载导入组件

3. **创建页面组件** (`03-页面组件开发指南.md`)

    - 在 `src/pages/` 对应模块下创建 `.vue` 文件
    - 使用 DataManager 组件或自定义实现
    - 遵循组件结构规范

4. **配置表单** (`05-表单配置指南.md`)

    - 使用 provideForm 工具配置表单
    - 定义搜索、表格、详情表单
    - 设置验证规则

5. **使用通用组件** (`04-通用组件使用指南.md`)
    - 优先使用现有通用组件
    - 根据需要自定义组件行为
    - 保持组件 API 一致性

### 示例参考：

-   **标准 CRUD 页面**: `src/pages/CMS/Banner.vue`
-   **标准 API 接口**: `src/api/banner.ts`
-   **路由配置**: `src/routers/routes.ts`

通过遵循这个框架规范和文件清单，可以确保项目代码的一致性、可维护性和可扩展性。

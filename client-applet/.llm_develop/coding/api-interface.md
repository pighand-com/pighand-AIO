# API接口文件开发说明

## 概述

API接口文件负责定义与后端服务的交互方法，统一管理所有网络请求。每个业务模块对应一个API文件，通过 `common/request.ts` 进行统一的网络请求处理。

## 文件位置

```
api/
├── index.ts          # 统一导出所有API模块
├── user.ts           # 用户相关接口
├── order.ts          # 订单相关接口
├── ticket.ts         # 票务相关接口
├── [module].ts       # 其他业务模块接口
```

## 开发规范

### 1. 文件结构

```typescript
import request from '@/common/request';

// 定义接口参数类型（可选）
interface ModuleParams {
    // 参数定义
}

interface ModuleData {
    // 数据定义
}

export default {
    // 接口方法定义
};
```

### 2. 命名规范

- **文件名**: 使用小写字母和短横线，如 `user.ts`、`order-detail.ts`
- **方法名**: 使用动词，如 `query`、`find`、`create`、`update`、`delete`
- **参数名**: 使用camelCase，如 `userId`、`orderData`

### 3. 常用方法模式

```typescript
export default {
    // 查询列表 - GET请求
    query: async (params: any) => await request.get(`module`, params),
    
    // 获取详情 - GET请求
    find: async (id: string) => await request.get(`module/${id}`),
    
    // 创建数据 - POST请求
    create: async (data: any) => await request.post(`module`, data),
    
    // 更新数据 - PUT请求
    update: async (id: string, data: any) => await request.put(`module/${id}`, data),
    
    // 删除数据 - DELETE请求
    delete: async (id: string) => await request.del(`module/${id}`),
    
    // 自定义操作 - POST请求
    customAction: async (id: string, params: any) => await request.post(`module/${id}/action`, params)
};
```

## 代码示例

### 基本API模块结构

```typescript
import request from '@/common/request';

// 定义接口参数类型
interface QueryParams {
    page?: number;
    size?: number;
    keyword?: string;
}

export default {
    // 查询列表
    query: async (params: QueryParams) => await request.get(`module`, params),
    
    // 获取详情
    find: async (id: string) => await request.get(`module/${id}`),
    
    // 创建数据
    create: async (data: any) => await request.post(`module`, data),
    
    // 更新数据
    update: async (id: string, data: any) => await request.put(`module/${id}`, data),
    
    // 删除数据
    delete: async (id: string) => await request.del(`module/${id}`)
};
```

### 用户模块示例 (api/user.ts)

```typescript
import request from '@/common/request';

export default {
    // 获取用户信息
    getUserInfo: async () => await request.get(`user/info`),
    
    // 更新用户信息
    updateSelf: async (data: any) => await request.put(`user/self`, data),
    
    // 绑定手机号
    bindPhone: async (code: string) => await request.post(`user/bind/phone`, { code })
};
```

## 在页面中使用API

### 1. 导入API模块

```typescript
// 导入单个模块
import { user } from '@/api';

// 或者导入多个模块
import { user, order, product } from '@/api';
```

### 2. 在组件中调用

```vue
<script setup>
import { ref, onMounted } from 'vue'
import { user, order } from '@/api'

const userInfo = ref({})
const orderList = ref([])
const loading = ref(false)

// 获取用户信息
const getUserInfo = async () => {
    try {
        loading.value = true
        const result = await user.getUserInfo()
        userInfo.value = result.data
    } catch (error) {
        console.error('获取用户信息失败:', error)
    } finally {
        loading.value = false
    }
}

// 获取订单列表
const getOrderList = async () => {
    try {
        const result = await order.query({ page: 1, size: 10 })
        orderList.value = result.data.list
    } catch (error) {
        console.error('获取订单列表失败:', error)
    }
}

// 页面加载时获取数据
onMounted(() => {
    getUserInfo()
    getOrderList()
})
</script>
```

## 错误处理

API调用的错误处理已在 `common/request.ts` 中统一处理，包括：

- **网络错误**: 自动显示错误提示
- **认证错误**: 自动跳转登录页面
- **业务错误**: 显示后端返回的错误信息

如需自定义错误处理，可以使用 try-catch：

```typescript
try {
    const result = await api.someMethod()
    // 处理成功结果
} catch (error) {
    // 自定义错误处理
    console.error('操作失败:', error.message)
}
```

## 注意事项

1. **统一导出**: 新增API模块后，需要在 `api/index.ts` 中导出
2. **类型定义**: 建议为复杂的参数和返回值定义TypeScript类型
3. **注释说明**: 为每个API方法添加清晰的注释说明
4. **错误处理**: 合理使用try-catch处理可能的错误
5. **参数验证**: 在调用API前进行必要的参数验证

## 更新API导出

当创建新的API模块时，需要在 `api/index.ts` 中添加导出：

```typescript
import user from './user';
import order from './order';
import product from './product';  // 新增的模块

export {
    user,
    order,
    product  // 导出新模块
};
```
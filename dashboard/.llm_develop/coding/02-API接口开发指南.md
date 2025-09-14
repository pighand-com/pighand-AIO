# API接口开发指南

## 目录结构
```
src/api/
├── index.ts          # 统一导出文件
├── common.ts         # 公共接口
├── login.ts          # 登录相关
├── user.ts           # 用户管理
├── banner.ts         # Banner管理
├── order.ts          # 订单管理
└── ...               # 其他业务模块
```

## 标准API文件结构

### 基础模板

```typescript
import request from '@/common/request';

// 定义接口基础URL
const baseUrl = 'module-name';

// 导出接口对象
export default {
    // 创建
    create: async (param: any) => await request.post(`${baseUrl}`, param),
    
    // 查询单个
    find: async (id: any) => await request.get(`${baseUrl}/${id}`),
    
    // 查询列表
    query: async (param?: any) => {
        return await request.get(`${baseUrl}`, param);
    },
    
    // 更新
    update: async (param: any) =>
        await request.put(`${baseUrl}/${param.id}`, param),
    
    // 删除
    del: async (id: any) => await request.del(`${baseUrl}/${id}`)
};
```

## 创建新API文件的步骤

### 步骤1：创建API文件

在 `src/api/` 目录下创建新的TypeScript文件，文件名使用camelCase命名。

#### 示例：创建商品管理API

**文件路径**: `src/api/product.ts`

```typescript
import request from '@/common/request';

const baseUrl = 'product';

export default {
    // 基础CRUD操作
    create: async (param: any) => await request.post(`${baseUrl}`, param),
    find: async (id: any) => await request.get(`${baseUrl}/${id}`),
    query: async (param?: any) => {
        return await request.get(`${baseUrl}`, param);
    },
    update: async (param: any) =>
        await request.put(`${baseUrl}/${param.id}`, param),
    del: async (id: any) => await request.del(`${baseUrl}/${id}`),
    
    // 业务特定接口
    updateStatus: async (id: any, status: number) => 
        await request.put(`${baseUrl}/${id}/status`, { status }),
    
    getCategories: async () => 
        await request.get(`${baseUrl}/categories`),
    
    batchUpdate: async (ids: any[], data: any) => 
        await request.put(`${baseUrl}/batch`, { ids, ...data })
};
```

### 步骤2：在index.ts中导出

**文件路径**: `src/api/index.ts`

```typescript
// 添加导入
import product from './product';

// 添加到导出列表
export {
    common,
    user,
    login,
    // ... 其他已有模块
    product,  // 新增
};
```

## 常用接口模式

### 1. 标准CRUD接口

```typescript
export default {
    create: async (param: any) => await request.post(`${baseUrl}`, param),
    find: async (id: any) => await request.get(`${baseUrl}/${id}`),
    query: async (param?: any) => await request.get(`${baseUrl}`, param),
    update: async (param: any) => await request.put(`${baseUrl}/${param.id}`, param),
    del: async (id: any) => await request.del(`${baseUrl}/${id}`)
};
```

### 2. 状态管理接口

```typescript
export default {
    // ... 基础CRUD
    
    // 启用/禁用
    enable: async (id: any) => await request.put(`${baseUrl}/${id}/enable`),
    disable: async (id: any) => await request.put(`${baseUrl}/${id}/disable`),
    
    // 上架/下架
    online: async (id: any) => await request.put(`${baseUrl}/${id}/online`),
    offline: async (id: any) => await request.put(`${baseUrl}/${id}/offline`),
    
    // 审核
    approve: async (id: any) => await request.put(`${baseUrl}/${id}/approve`),
    reject: async (id: any, reason?: string) => 
        await request.put(`${baseUrl}/${id}/reject`, { reason })
};
```

### 3. 批量操作接口

```typescript
export default {
    // ... 基础CRUD
    
    // 批量删除
    batchDelete: async (ids: any[]) => 
        await request.del(`${baseUrl}/batch`, { data: { ids } }),
    
    // 批量更新
    batchUpdate: async (ids: any[], data: any) => 
        await request.put(`${baseUrl}/batch`, { ids, ...data }),
    
    // 批量导入
    import: async (file: File) => {
        const formData = new FormData();
        formData.append('file', file);
        return await request.post(`${baseUrl}/import`, formData, {
            headers: { 'Content-Type': 'multipart/form-data' }
        });
    },
    
    // 导出
    export: async (param?: any) => 
        await request.get(`${baseUrl}/export`, param, { responseType: 'blob' })
};
```

### 4. 关联数据接口

```typescript
export default {
    // ... 基础CRUD
    
    // 获取关联选项
    getOptions: async () => await request.get(`${baseUrl}/options`),
    
    // 获取统计数据
    getStatistics: async (param?: any) => 
        await request.get(`${baseUrl}/statistics`, param),
    
    // 获取子项列表
    getChildren: async (parentId: any, param?: any) => 
        await request.get(`${baseUrl}/${parentId}/children`, param)
};
```

## HTTP方法使用规范

### GET请求
- 用于查询数据
- 参数通过query string传递
- 支持分页、排序、筛选

```typescript
// 查询列表
query: async (param?: any) => await request.get(`${baseUrl}`, param),

// 查询单个
find: async (id: any) => await request.get(`${baseUrl}/${id}`),

// 带参数查询
search: async (keyword: string, page: number = 1) => 
    await request.get(`${baseUrl}/search`, { keyword, page })
```

### POST请求
- 用于创建新资源
- 数据通过请求体传递

```typescript
// 创建
create: async (param: any) => await request.post(`${baseUrl}`, param),

// 复杂查询（当参数过多时）
advancedQuery: async (param: any) => 
    await request.post(`${baseUrl}/query`, param)
```

### PUT请求
- 用于更新资源
- 通常需要完整的资源数据

```typescript
// 更新
update: async (param: any) => 
    await request.put(`${baseUrl}/${param.id}`, param),

// 状态更新
updateStatus: async (id: any, status: any) => 
    await request.put(`${baseUrl}/${id}/status`, { status })
```

### DELETE请求
- 用于删除资源

```typescript
// 删除单个
del: async (id: any) => await request.del(`${baseUrl}/${id}`),

// 批量删除
batchDelete: async (ids: any[]) => 
    await request.del(`${baseUrl}/batch`, { data: { ids } })
```

## 错误处理

项目使用统一的错误处理机制，在 `src/common/request.ts` 中已经配置。

### 常见错误处理

```typescript
// API调用示例
try {
    const result = await API.product.create(formData);
    if (result) {
        // 成功处理
        ElMessage.success('创建成功');
    }
} catch (error) {
    // 错误已在request.ts中统一处理
    console.error('创建失败:', error);
}
```

## 类型定义建议

### 定义接口参数类型

```typescript
// 定义类型接口
interface ProductCreateParam {
    name: string;
    price: number;
    categoryId: number;
    description?: string;
    images?: string[];
}

interface ProductQueryParam {
    page?: number;
    size?: number;
    keyword?: string;
    categoryId?: number;
    status?: number;
}

// 使用类型
export default {
    create: async (param: ProductCreateParam) => 
        await request.post(`${baseUrl}`, param),
    
    query: async (param?: ProductQueryParam) => 
        await request.get(`${baseUrl}`, param)
};
```

## 完整示例

### 客户管理API示例

**文件路径**: `src/api/customer.ts`

```typescript
import request from '@/common/request';

const baseUrl = 'customer';

export default {
    // 基础CRUD
    create: async (param: any) => await request.post(`${baseUrl}`, param),
    find: async (id: any) => await request.get(`${baseUrl}/${id}`),
    query: async (param?: any) => await request.get(`${baseUrl}`, param),
    update: async (param: any) => await request.put(`${baseUrl}/${param.id}`, param),
    del: async (id: any) => await request.del(`${baseUrl}/${id}`),
    
    // 业务接口
    updateStatus: async (id: any, status: number) => 
        await request.put(`${baseUrl}/${id}/status`, { status }),
    
    getGroups: async () => await request.get(`${baseUrl}/groups`),
    
    addToGroup: async (customerId: any, groupId: any) => 
        await request.post(`${baseUrl}/${customerId}/groups/${groupId}`),
    
    removeFromGroup: async (customerId: any, groupId: any) => 
        await request.del(`${baseUrl}/${customerId}/groups/${groupId}`),
    
    getOrderHistory: async (customerId: any, param?: any) => 
        await request.get(`${baseUrl}/${customerId}/orders`, param),
    
    export: async (param?: any) => 
        await request.get(`${baseUrl}/export`, param, { responseType: 'blob' })
};
```

## 注意事项

1. **命名规范**: API文件名使用camelCase，与后端接口路径保持一致
2. **错误处理**: 依赖统一的错误处理机制，无需在每个接口中重复处理
3. **类型安全**: 建议为复杂参数定义TypeScript接口
4. **接口文档**: 为复杂接口添加注释说明
5. **版本管理**: 如需支持多版本API，可在baseUrl中包含版本信息
6. **缓存策略**: 对于不经常变化的数据，可以考虑添加缓存机制

通过遵循这些规范，可以创建出结构清晰、易于维护的API接口文件。
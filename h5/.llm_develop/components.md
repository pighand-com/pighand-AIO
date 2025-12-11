# Dashboard 组件使用文档

## 核心组件

### 1. DataManager 数据管理器

`DataManager` 是最高级别的组件，集成了搜索、表格、详情抽屉等功能。

#### 基本用法

```vue
<template>
    <PDataManager
        :handle-query="handleQuery"
        :handle-find="handleFind"
        :handle-delete="handleDelete"
        :handle-create="handleCreate"
        :handle-update="handleUpdate">
    </PDataManager>
</template>

<script setup>
import { api } from '@/api';
import provideForm from '@/common/provideForm';

provideForm([
    {
        label: 'ID',
        prop: 'id',
        isPrimaryKey: true,
        isTable: true
    },
    {
        label: '名称',
        prop: 'name',
        isSearch: true,
        isTable: true,
        isDetail: true,
        rules: [{ required: true, message: '名称必填' }]
    }
]);
</script>
```

#### 属性配置

| 属性             | 类型     | 默认值 | 说明                            |
| ---------------- | -------- | ------ | ------------------------------- |
| isShowSearch     | Boolean  | true   | 是否显示搜索组件                |
| isShowDataTable  | Boolean  | true   | 是否显示数据表格                |
| isShowDrawer     | Boolean  | true   | 是否显示详情抽屉                |
| handleQuery      | Function | -      | 查询数据处理函数                |
| handleFind       | Function | -      | 查找单条数据处理函数            |
| handleDelete     | Function | -      | 删除数据处理函数                |
| handleCreate     | Function | -      | 创建数据处理函数                |
| handleUpdate     | Function | -      | 更新数据处理函数                |
| handleFormatData | Function | -      | 数据格式化处理函数              |
| drawerProps      | Object   | {}     | 传递给 Drawer 组件的属性对象    |
| dataTableProps   | Object   | {}     | 传递给 DataTable 组件的属性对象 |
| searchProps      | Object   | {}     | 传递给 Search 组件的属性对象    |

### 2. Search 搜索组件

搜索组件支持基础搜索和更多搜索功能。

#### 特性

-   自动布局响应式
-   支持更多搜索折叠展开
-   内置搜索、重置、更多按钮
-   支持自定义插槽

#### Search 组件属性

| 属性        | 类型     | 默认值 | 说明                               |
| ----------- | -------- | ------ | ---------------------------------- |
| handleQuery | Function | -      | 查询处理函数，用于执行搜索查询操作 |

### 3. DataTable 数据表格

功能丰富的数据表格组件。

#### 特性

-   支持图片预览
-   支持链接跳转
-   支持数据复制
-   支持批量操作
-   支持自定义格式化
-   内置分页功能

#### DataTable 组件属性

| 属性             | 类型          | 默认值             | 说明                                                                       |
| ---------------- | ------------- | ------------------ | -------------------------------------------------------------------------- |
| add              | Boolean       | true               | 是否显示添加按钮                                                           |
| tableOperation   | Array/Boolean | ['edit', 'delete'] | 表格操作列配置，可以是数组指定显示的操作按钮，或者布尔值控制是否显示操作列 |
| operationWidth   | Number/String | -                  | 操作列宽度                                                                 |
| handleQuery      | Function      | -                  | 查询数据处理函数                                                           |
| handleFind       | Function      | -                  | 查询单条数据处理函数                                                       |
| handleDelete     | Function      | -                  | 删除数据处理函数                                                           |
| handleFormatData | Function      | -                  | 数据格式化处理函数                                                         |
| batchActions     | Array         | []                 | 批量操作配置数组                                                           |

#### 批量操作配置

```vue
<template>
    <PDataTable :batch-actions="batchActions" :handle-query="handleQuery">
    </PDataTable>
</template>

<script setup>
const batchActions = [
    {
        name: '批量删除',
        handler: (selections) => {
            console.log('选中的数据:', selections);
        }
    }
];
</script>
```

### 4. Drawer 详情抽屉

用于显示和编辑详情数据的抽屉组件。

#### 特性

-   支持创建和编辑模式
-   自动表单验证
-   支持自定义尺寸
-   支持浮动内容区域

#### Drawer 组件属性

| 属性             | 类型     | 默认值 | 说明               |
| ---------------- | -------- | ------ | ------------------ |
| title            | String   | -      | 抽屉标题           |
| size             | String   | '30%'  | 抽屉宽度           |
| handleCreate     | Function | -      | 创建数据处理函数   |
| handleUpdate     | Function | -      | 更新数据处理函数   |
| handleQuery      | Function | -      | 查询数据处理函数   |
| handleFormatData | Function | -      | 数据格式化处理函数 |

### 5. FormItems 表单项集合

根据配置自动渲染表单项的组件。

#### FormItems 组件属性

| 属性        | 类型                        | 说明                                                            |
| ----------- | --------------------------- | --------------------------------------------------------------- |
| formColumns | Array<FormColumnsInterface> | 表单列配置数组，定义表单的每一列的配置信息                      |
| formModel   | Object                      | 表单数据模型，用于双向绑定表单数据                              |
| onWhere     | 'search' \| 'detail'        | 表单所在位置标识，可选值：'search'(搜索表单)/'detail'(详情表单) |

#### 支持的表单类型

-   input: 文本输入框
-   number: 数字输入框
-   select: 下拉选择器
-   checkbox: 复选框
-   radio: 单选框
-   switch: 开关
-   datePicker: 日期选择器
-   dateTimePicker: 日期时间选择器
-   datePickerRange: 日期范围选择器
-   dateTimePickerRange: 日期时间范围选择器
-   uploadImage: 图片上传
-   uploadImageList: 多图片上传
-   uploadFile: 文件上传
-   uploadFileList: 多文件上传
-   cascader: 级联选择器
-   editor: 富文本编辑器

### 6. FormItem 单个表单项

渲染单个表单项的基础组件，支持所有表单类型和验证规则。

#### FormItem 组件属性

| 属性           | 类型                 | 说明                                                            |
| -------------- | -------------------- | --------------------------------------------------------------- |
| formColumnItem | FormColumnsInterface | 表单列配置对象                                                  |
| formModel      | Object               | 表单数据模型，用于双向绑定表单数据                              |
| onWhere        | 'search' \| 'detail' | 表单所在位置标识，可选值：'search'(搜索表单)/'detail'(详情表单) |

## provideForm 工具函数

`provideForm` 是核心工具函数，用于配置表单结构和提供数据管理功能。

### 基本用法

```javascript
import provideForm from '@/common/provideForm';

const {
    formColumns,
    tableDataModel,
    searchFormModel,
    detailFormModel,
    queryTableData,
    isTableDataLoading,
    isOpenDetail
} = provideForm([
    {
        label: '用户名',
        prop: 'username',
        isSearch: true,
        isTable: true,
        isDetail: true,
        domType: 'input',
        rules: [{ required: true, message: '用户名必填' }]
    }
]);
```

### FormColumnsInterface 配置项

#### 基础配置

| 属性          | 类型       | 说明           |
| ------------- | ---------- | -------------- |
| label         | string     | 表单项标签     |
| prop          | string     | 表单项属性名   |
| suffix        | string     | 表单项后缀     |
| help          | string/any | 帮助信息       |
| isPrimaryKey  | boolean    | 是否为主键     |
| default       | any        | 默认值         |
| searchDefault | any        | 搜索表单默认值 |
| detailDefault | any        | 详情表单默认值 |
| domType       | string     | 组件类型       |
| placeholder   | string     | 占位符         |
| hidden        | boolean    | 是否隐藏       |

#### 搜索相关

| 属性              | 类型    | 说明                 |
| ----------------- | ------- | -------------------- |
| isSearch          | boolean | 是否用于搜索         |
| isSearchMore      | boolean | 是否在更多搜索中显示 |
| searchPlaceholder | string  | 搜索表单占位符       |

#### 表格相关

| 属性             | 类型          | 说明                          |
| ---------------- | ------------- | ----------------------------- |
| isTable          | boolean       | 是否在表格中显示              |
| tableType        | string        | 表格列类型 (input/image/link) |
| tableFormat      | Function      | 表格格式化方法                |
| tableWidth       | string/number | 表格列宽度                    |
| tableAlign       | string        | 表格列对齐方式                |
| isTableCopyValue | boolean       | 是否显示复制按钮              |

#### 详情相关

| 属性              | 类型             | 说明             |
| ----------------- | ---------------- | ---------------- |
| isDetail          | boolean/Function | 是否在详情中显示 |
| isCreate          | boolean          | 是否在新建时显示 |
| isUpdate          | boolean          | 是否在更新时显示 |
| rules             | Array            | 校验规则         |
| onDetailChange    | Function         | 详情表单变更回调 |
| detailPlaceholder | string           | 详情表单占位符   |

#### 组件特定配置

| 属性            | 类型              | 说明                                    |
| --------------- | ----------------- | --------------------------------------- |
| inputType       | string            | input 组件类型 (text/password/textarea) |
| uploadPath      | string            | 上传路径                                |
| uploadAcceptMap | Object            | 上传文件类型映射                        |
| componentProps  | Object            | 组件扩展属性                            |
| divider         | DividerConfigType | 分隔符配置                              |

#### 数据源配置

```javascript
{
    label: '角色',
    prop: 'roleIds',
    domType: 'select',
    domData: async (key, domDataSet, formData) => {
        const result = await api.getRoles({ name: key })
        return result.records.map(item => ({
            label: item.name,
            value: item.id
        }))
    },
    componentProps: {
        multiple: true
    }
}
```

### 返回值说明

| 属性                  | 类型     | 说明                   |
| --------------------- | -------- | ---------------------- |
| formColumns           | Array    | 表单列配置数组         |
| formRules             | Object   | 表单验证规则           |
| tableDataModel        | Object   | 表格数据模型           |
| searchFormModel       | Object   | 搜索表单数据模型       |
| detailFormModel       | Object   | 详情表单数据模型       |
| queryTableData        | Function | 查询表格数据方法       |
| queryTableDataDefault | Function | 查询表格首页数据方法   |
| isTableDataLoading    | Ref      | 表格数据加载状态       |
| isDetailDataLoading   | Ref      | 详情数据加载状态       |
| isOpenDetail          | Ref      | 是否打开详情           |
| pageSize              | Ref      | 分页大小               |
| pageNumber            | Ref      | 当前页码               |
| domDataSet            | Object   | 组件数据集合           |
| getDomData            | Object   | 获取组件数据的方法集合 |
| watchDetailForm       | Function | 监听详情表单数据变化   |

## 使用示例

### 1. 基础 CRUD 页面

```vue
<template>
    <PDataManager
        :handle-query="user.query"
        :handle-find="user.find"
        :handle-delete="user.del"
        :handle-create="user.create"
        :handle-update="user.update">
    </PDataManager>
</template>

<script setup>
import { user } from '@/api';
import provideForm from '@/common/provideForm';

provideForm([
    {
        label: 'ID',
        prop: 'id',
        isPrimaryKey: true,
        isTable: true
    },
    {
        label: '用户名',
        prop: 'username',
        isSearch: true,
        isTable: true,
        isDetail: true,
        rules: [{ required: true, message: '用户名必填' }]
    },
    {
        label: '密码',
        prop: 'password',
        isDetail: true,
        inputType: 'password',
        isUpdate: false,
        rules: [{ required: true, message: '密码必填' }]
    }
]);
</script>
```

### 2. 自定义抽屉尺寸

```vue
<template>
    <PDataManager
        :handle-query="article.query"
        :handle-find="article.find"
        :handle-delete="article.del"
        :handle-create="article.create"
        :handle-update="article.update"
        :drawer-props="{ size: '60%' }">
    </PDataManager>
</template>
```

### 3. 自定义表格操作

```vue
<template>
    <PDataManager
        :handle-query="order.query"
        :data-table-props="{
            add: false,
            tableOperation: []
        }">
        <template #table-column-operation="{ row }">
            <el-button
                v-if="row.status === 'pending'"
                size="small"
                type="danger"
                @click="handleRefund(row)">
                退款
            </el-button>
        </template>
    </PDataManager>
</template>
```

### 4. 自定义表单项

```vue
<template>
    <PDataManager :handle-query="article.query">
        <template #drawer-2-after>
            <el-form-item label="分类" prop="categories">
                <el-cascader
                    :options="allCategories"
                    v-model="detailFormModel['categories']"
                    clearable />
            </el-form-item>
        </template>
    </PDataManager>
</template>

<script setup>
const { detailFormModel } = provideForm([...])
</script>
```

### 5. 表格数据格式化

```javascript
{
    label: '角色',
    prop: 'roleIds',
    isTable: true,
    tableFormat: (value, row, item) => {
        if (!row?.roles || row.roles.length === 0) {
            return '-'
        }
        return h('div',
            { style: 'display: flex; flex-wrap: wrap; gap: 4px;' },
            row.roles.map(role =>
                h(ElTag, { size: 'small', type: 'info' }, role.name)
            )
        )
    }
}
```

### 6. 异步数据源

```javascript
{
    label: '角色',
    prop: 'roleIds',
    domType: 'select',
    domData: async (key) => {
        const result = await role.query({ name: key })
        return result.records.map(item => ({
            label: item.name,
            value: item.id
        }))
    },
    componentProps: {
        multiple: true
    }
}
```

## 插槽系统

### DataManager 插槽

-   `header-operation`: 表格头部操作区域
-   `table-column-operation`: 表格行操作列
-   `detail-float`: 详情抽屉浮动内容
-   `{index}-before`: 表单项前置插槽
-   `{prop}-before`: 指定属性前置插槽
-   `{prop}-after`: 指定属性后置插槽
-   `{index}-after`: 表单项后置插槽

### 插槽命名规则

-   搜索表单: `search-{prop}-before/after`
-   详情表单: `detail-{prop}-before/after` 或 `drawer-{index}-before/after`
-   表格: `table-{prop}-before/after`

## 最佳实践

### 1. 配置优先级

-   `searchPlaceholder` > `placeholder`
-   `detailPlaceholder` > `placeholder`
-   `searchDefault` > `default`
-   `detailDefault` > `default`

### 2. 性能优化

-   异步数据源会自动缓存初始数据
-   使用 `clearDomDataCache` 清除缓存
-   合理使用 `hidden` 属性控制显示

### 3. 表单验证

-   只有 `isDetail: true` 的字段才会应用验证规则
-   支持 Element Plus 的所有验证规则
-   可以通过 `rules` 属性自定义验证

### 4. 权限控制

-   使用 `isCreate` 和 `isUpdate` 控制字段在不同操作下的显示
-   通过 `hidden` 属性动态控制字段显示
-   可以在组件级别控制功能开关

## 高级功能

### 1. 插槽命名转换

DataManager 组件会自动处理插槽名称转换：

-   `search-xxx` 插槽会转换为 `xxx` 传递给 Search 组件
-   `table-xxx` 插槽会转换为 `xxx` 传递给 DataTable 组件
-   `drawer-xxx` 插槽会转换为 `xxx` 传递给 Drawer 组件

### 2. 表格数据自动格式化

表格支持多种数据类型的自动格式化：

-   日期时间类型自动格式化为 `YYYY-MM-DD HH:mm:ss`
-   日期类型自动格式化为 `YYYY-MM-DD`
-   下拉选择器数据自动转换为对应的 label 显示
-   支持嵌套属性访问（如 `user.name`）

### 3. 上传组件功能

-   支持系统上传和云服务上传（腾讯云 COS）
-   支持上传进度显示
-   支持文件类型映射配置
-   支持图片预览、下载、删除操作

### 4. 表单验证增强

-   支持动态显示/隐藏字段的验证
-   创建和更新模式下的字段权限控制
-   表单数据变更监听和回调

## 注意事项

1. `isPrimaryKey` 用于标识主键字段，默认为 'id'
2. 日期范围选择器会自动处理参数名称（添加 'Range' 后缀）
3. 上传组件需要配置 `uploadPath` 属性
4. 异步数据源函数接收三个参数：`key`（搜索关键字）、`domDataSet`（数据集合）、`formData`（表单数据）
5. 表格格式化函数接收三个参数：`value`（当前值）、`row`（行数据）、`item`（列配置）
6. 组件支持通过 `componentProps` 传递额外的 Element Plus 组件属性
7. 搜索表单中的 `radio` 类型会自动转换为 `select` 类型
8. 表格操作列可以通过 `tableOperation` 数组精确控制显示的按钮

## 更新日志

### v1.0.0

-   初始版本发布
-   支持完整的 CRUD 操作
-   提供丰富的表单组件类型
-   支持自定义插槽和格式化

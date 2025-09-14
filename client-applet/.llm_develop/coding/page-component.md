# 页面组件开发说明

## 概述

页面组件是uni-app应用的核心，负责实现具体的业务功能和用户界面。每个页面都是一个Vue组件，采用Vue 3的Composition API进行开发。

## 文件位置

```
pages/
├── base/                 # 基础功能页面
│   └── pages/
│       ├── index.vue     # 首页
│       ├── mine.vue      # 我的页面
│       ├── store.vue     # 门店页面
│       └── ...
├── ECommerce/            # 电商模块页面
│   └── pages/
│       ├── order-list.vue    # 订单列表
│       ├── order-detail.vue  # 订单详情
│       ├── ticket-list.vue   # 票务列表
│       └── ...
└── MKT/                  # 营销模块页面
    └── pages/
        ├── lottery.vue   # 抽奖页面
        └── ...
```

## 开发规范

### 1. 文件结构

```vue
<template>
  <!-- 页面UI结构 -->
</template>

<script setup>
// 导入依赖
import { ref, reactive, onMounted, onShow } from 'vue'
import { apiModule } from '@/api'

// 响应式数据定义
const data = ref({})
const state = reactive({})

// 生命周期钩子
onMounted(() => {
  // 页面挂载时执行
})

onShow(() => {
  // 页面显示时执行（uni-app特有）
})

// 方法定义
const handleAction = () => {
  // 处理逻辑
}
</script>

<style scoped>
/* 页面样式 */
</style>
```

### 2. 命名规范

- **文件名**: 使用kebab-case，如 `order-list.vue`、`user-profile.vue`
- **组件名**: 在页面中使用PascalCase，如 `OrderList`、`UserProfile`
- **变量名**: 使用camelCase，如 `orderList`、`userInfo`
- **方法名**: 使用动词开头，如 `handleSubmit`、`loadData`

### 3. 生命周期使用

```javascript
import { onMounted, onShow, onHide, onUnload } from 'vue'

// 页面挂载（仅执行一次）
onMounted(() => {
  console.log('页面挂载')
})

// 页面显示（每次显示都执行）
onShow(() => {
  console.log('页面显示')
  // 刷新数据
})

// 页面隐藏
onHide(() => {
  console.log('页面隐藏')
})

// 页面卸载
onUnload(() => {
  console.log('页面卸载')
  // 清理资源
})
```

## 代码示例

### 基本页面结构

```vue
<template>
  <view class="page">
    <!-- 页面内容 -->
    <view v-if="loading" class="loading">加载中...</view>
    <view v-else>
      <!-- 数据展示 -->
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted, onShow } from 'vue'
import { apiModule } from '@/api'

// 响应式数据
const loading = ref(false)
const data = ref({})

// 获取数据
const loadData = async () => {
  try {
    loading.value = true
    const result = await apiModule.query()
    data.value = result.data
  } catch (error) {
    console.error('获取数据失败:', error)
  } finally {
    loading.value = false
  }
}

// 生命周期
onMounted(() => {
  loadData()
})

onShow(() => {
  // 页面显示时执行
})
</script>

<style scoped>
.page {
  padding: 40rpx;
}
</style>
```

### 列表页面示例

```vue
<template>
  <view class="list-page">
    <!-- 列表内容 -->
    <scroll-view 
      scroll-y 
      @scrolltolower="loadMore"
      :refresher-enabled="true"
      @refresherrefresh="onRefresh"
    >
      <view v-for="item in list" :key="item.id" class="list-item">
        {{ item.name }}
      </view>
    </scroll-view>
  </view>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { api } from '@/api'

const list = ref([])
const pagination = reactive({ page: 1, size: 10 })
const hasMore = ref(true)

// 获取列表数据
const getList = async (isRefresh = false) => {
  if (isRefresh) {
    pagination.page = 1
    list.value = []
  }
  
  const result = await api.query(pagination)
  const newList = result.data.list
  
  if (isRefresh) {
    list.value = newList
  } else {
    list.value.push(...newList)
  }
  
  hasMore.value = list.value.length < result.data.total
}

// 下拉刷新
const onRefresh = () => getList(true)

// 加载更多
const loadMore = () => {
  if (hasMore.value) {
    pagination.page++
    getList()
  }
}
</script>
```

## 页面间传参

### 1. URL参数传递

```javascript
// 跳转时传参
uni.navigateTo({
  url: `/pages/detail?id=123&type=product`
})

// 接收参数
const pages = getCurrentPages()
const currentPage = pages[pages.length - 1]
const { id, type } = currentPage.options
```

### 2. 全局数据传递

```javascript
// 设置全局数据
getApp().globalData.selectedProduct = productData

// 获取全局数据
const selectedProduct = getApp().globalData.selectedProduct
```

## 注意事项

1. **生命周期**: 合理使用onShow和onMounted，onShow每次显示都会执行
2. **内存管理**: 及时清理定时器、监听器等资源
3. **错误处理**: 对API调用进行适当的错误处理
4. **用户体验**: 添加loading状态和空状态处理
5. **性能优化**: 避免在模板中使用复杂计算，使用computed或方法
6. **响应式设计**: 使用rpx单位确保多端适配
7. **无障碍访问**: 为重要元素添加适当的aria标签
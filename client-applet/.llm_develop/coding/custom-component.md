# 自定义组件开发说明

## 概述

自定义组件是项目中可复用的UI单元，用于封装特定的业务逻辑和界面元素。通过组件化开发，可以提高代码复用性、维护性和开发效率。

## 文件位置

```
components/
├── custom-button/           # 自定义按钮组件
│   └── custom-button.vue
├── status-bar-gradient/     # 状态栏渐变组件
│   └── status-bar-gradient.vue
├── tab-bar/                 # 自定义底部导航
│   └── tab-bar.vue
├── user/                    # 用户相关组件
│   ├── avatar.vue          # 用户头像组件
│   ├── check.vue           # 用户验证组件
│   ├── login.vue           # 登录组件
│   └── phone.vue           # 手机号组件
└── [component-name]/        # 新组件目录
    └── [component-name].vue
```

## 开发规范

### 1. 组件结构

```vue
<template>
  <!-- 组件UI结构 -->
</template>

<script setup>
// 导入依赖
import { ref, computed, watch } from 'vue'

// 定义Props
const props = defineProps({
  // props定义
})

// 定义Emits
const emit = defineEmits([
  // 事件定义
])

// 响应式数据
const data = ref({})

// 计算属性
const computedValue = computed(() => {
  // 计算逻辑
})

// 监听器
watch(() => props.value, (newVal) => {
  // 监听逻辑
})

// 方法定义
const handleAction = () => {
  // 处理逻辑
}
</script>

<style scoped>
/* 组件样式 */
</style>
```

### 2. 命名规范

- **组件目录**: 使用kebab-case，如 `custom-button`、`user-avatar`
- **组件文件**: 与目录同名，如 `custom-button.vue`
- **Props**: 使用camelCase，如 `buttonType`、`isDisabled`
- **Events**: 使用kebab-case，如 `button-click`、`value-change`
- **CSS类名**: 使用BEM命名法或kebab-case

## 代码示例

### 基本组件结构

```vue
<template>
  <view class="custom-component" :class="componentClasses">
    <!-- 组件内容 -->
    <slot>{{ text }}</slot>
  </view>
</template>

<script setup>
import { computed } from 'vue'

// Props定义
const props = defineProps({
  text: {
    type: String,
    default: ''
  },
  type: {
    type: String,
    default: 'default',
    validator: (value) => ['default', 'primary'].includes(value)
  },
  disabled: {
    type: Boolean,
    default: false
  }
})

// Events定义
const emit = defineEmits(['click'])

// 计算样式类
const componentClasses = computed(() => {
  return {
    [`component--${props.type}`]: true,
    'component--disabled': props.disabled
  }
})

// 事件处理
const handleClick = (event) => {
  if (!props.disabled) {
    emit('click', event)
  }
}
</script>

<style scoped>
.custom-component {
  padding: 20rpx;
  border-radius: 8rpx;
}

.component--default {
  background-color: #f5f5f5;
  color: #333;
}

.component--primary {
  background-color: #AC9157;
  color: #fff;
}

.component--disabled {
  opacity: 0.5;
}
</style>
```

### 自定义按钮组件示例

```vue
<template>
  <button 
    class="custom-button"
    :class="buttonClasses"
    :disabled="disabled || loading"
    @click="handleClick"
  >
    <view v-if="loading" class="loading">⟳</view>
    <slot>{{ text }}</slot>
  </button>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  type: {
    type: String,
    default: 'default',
    validator: (value) => ['default', 'primary'].includes(value)
  },
  text: String,
  disabled: Boolean,
  loading: Boolean
})

const emit = defineEmits(['click'])

const buttonClasses = computed(() => ({
  [`button--${props.type}`]: true,
  'button--loading': props.loading
}))

const handleClick = (event) => {
  if (!props.disabled && !props.loading) {
    emit('click', event)
  }
}
</script>

<style scoped>
.custom-button {
  padding: 16rpx 32rpx;
  border-radius: 8rpx;
  border: none;
}

.button--default {
  background-color: #f5f5f5;
  color: #333;
}

.button--primary {
  background-color: #AC9157;
  color: #fff;
}

.loading {
  animation: rotate 1s linear infinite;
}

@keyframes rotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}
</style>
```

## 组件使用方法

### 1. 在页面中使用组件

```vue
<template>
  <view class="page">
    <!-- 使用自定义按钮 -->
    <custom-button 
      type="primary" 
      size="large" 
      text="提交订单"
      :loading="submitting"
      @click="handleSubmit"
    />
    
    <!-- 使用用户头像 -->
    <user-avatar 
      :src="userInfo.avatar"
      :text="userInfo.name"
      size="large"
      status="online"
      :badge="unreadCount"
      clickable
      @click="showUserProfile"
    />
    
    <!-- 使用自定义导航栏 -->
    <custom-navigation-bar 
      title="订单详情"
      background-color="#AC9157"
      text-color="#ffffff"
      @left-click="goBack"
    >
      <template #right>
        <text @click="showMenu">菜单</text>
      </template>
    </custom-navigation-bar>
  </view>
</template>

<script setup>
import { ref } from 'vue'

const submitting = ref(false)
const userInfo = ref({
  avatar: '/static/avatar.jpg',
  name: '张三'
})
const unreadCount = ref(5)

const handleSubmit = () => {
  submitting.value = true
  // 提交逻辑
}

const showUserProfile = () => {
  // 显示用户资料
}

const goBack = () => {
  // 返回逻辑
}

const showMenu = () => {
  // 显示菜单
}
</script>
```

### 2. 组件自动导入配置

在 `pages.json` 中配置easycom：

```json
{
  "easycom": {
    "autoscan": true,
    "custom": {
      "custom-(.*)": "@/components/custom-$1/custom-$1.vue",
      "user-(.*)": "@/components/user/$1.vue"
    }
  }
}
```

## 组件开发最佳实践

### 1. Props设计原则

- **类型检查**: 为所有props定义类型和默认值
- **验证器**: 为枚举类型的props添加验证器
- **命名规范**: 使用camelCase命名
- **文档注释**: 为复杂props添加注释说明

### 2. 事件设计原则

- **语义化命名**: 事件名要清晰表达含义
- **参数传递**: 传递必要的事件参数
- **事件冒泡**: 合理控制事件冒泡

### 3. 样式设计原则

- **BEM命名**: 使用BEM命名法或统一的命名规范
- **作用域样式**: 使用scoped避免样式污染
- **响应式设计**: 使用rpx单位确保多端适配
- **主题支持**: 支持主题色和暗黑模式

### 4. 可访问性

- **语义化标签**: 使用语义化的HTML标签
- **键盘导航**: 支持键盘操作
- **屏幕阅读器**: 添加适当的aria属性

## 注意事项

1. **组件注册**: 确保组件在easycom中正确配置
2. **性能优化**: 避免在组件中进行复杂计算
3. **内存泄漏**: 及时清理定时器和监听器
4. **平台兼容**: 考虑不同平台的兼容性
5. **版本管理**: 组件API变更时要考虑向后兼容
6. **文档维护**: 及时更新组件使用文档
7. **测试覆盖**: 为组件编写单元测试
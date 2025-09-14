# 通用工具开发说明

## 概述

通用工具模块提供项目中常用的工具函数和配置，包括网络请求、本地存储、常量配置、登录逻辑、文件上传、分享功能等。这些工具函数可以在整个项目中复用，提高开发效率和代码一致性。

## 文件位置

```
common/
├── constant.ts           # 常量配置
├── request.ts            # 网络请求封装
├── storage.ts            # 本地存储封装
├── login.ts              # 登录逻辑
├── upload.ts             # 文件上传
├── share.ts              # 分享功能
├── utils.ts              # 通用工具函数
├── validator.ts          # 数据验证
├── format.ts             # 数据格式化
└── [utility].ts          # 其他工具模块
```

## 核心工具模块

### 1. 常量配置 (common/constant.ts)

```typescript
// 环境配置
interface EnvironmentConfig {
  api: Record<string, string>
  APPLICATION_ID: string
  local_storage_token: string
  local_storage_user_info: string
}

const constant: EnvironmentConfig = {
  // API地址配置
  api: {
    release: 'https://api.production.com/',
    develop: 'http://127.0.0.1:9080/'
  },
  
  APPLICATION_ID: '1',
  
  // 本地存储键名
  local_storage_token: 'token',
  local_storage_user_info: 'user_info'
}

// 业务常量
export const BUSINESS_CONSTANTS = {
  DEFAULT_PAGE_SIZE: 10,
  MAX_FILE_SIZE: 10 * 1024 * 1024, // 10MB
  
  // 正则表达式
  REGEX: {
    PHONE: /^1[3-9]\d{9}$/,
    EMAIL: /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  }
}

export default constant
```

### 2. 网络请求封装 (common/request.ts)

```typescript
import constant from './constant'
import { getToken } from './storage'

// 获取完整URL
const getUrl = (url: string): string => {
  const envVersion = __wxConfig?.envVersion || 'develop'
  return constant.api[envVersion] + url
}

// 通用请求方法
const request = async (
  method: 'GET' | 'POST' | 'PUT' | 'DELETE',
  url: string,
  data?: any
): Promise<any> => {
  const token = getToken()
  const headers: Record<string, any> = {
    'Content-Type': 'application/json'
  }
  
  if (token) {
    headers['Authorization'] = `Bearer ${token}`
  }
  
  try {
    const response = await uni.request({
      url: getUrl(url),
      method,
      header: headers,
      data,
      timeout: 30000
    })
    
    if (response.statusCode === 200) {
      return response.data
    } else {
      throw new Error(`请求失败: ${response.statusCode}`)
    }
  } catch (error) {
    console.error('请求错误:', error)
    uni.showToast({
      title: '网络请求失败',
      icon: 'error'
    })
    throw error
  }
}

export default {
  get: (url: string, params?: any) => request('GET', url, params),
  post: (url: string, data?: any) => request('POST', url, data),
  put: (url: string, data?: any) => request('PUT', url, data),
  del: (url: string, params?: any) => request('DELETE', url, params)
}
```

### 3. 本地存储封装 (common/storage.ts)

```typescript
import constant from './constant'

// 设置存储数据
const setStorage = (key: string, value: any): void => {
  try {
    const data = typeof value === 'object' ? JSON.stringify(value) : String(value)
    uni.setStorageSync(key, data)
  } catch (error) {
    console.error('设置存储失败:', error)
  }
}

// 获取存储数据
const getStorage = <T = any>(key: string): T | null => {
  try {
    const data = uni.getStorageSync(key)
    if (!data) return null
    
    try {
      return JSON.parse(data)
    } catch {
      return data as T
    }
  } catch (error) {
    console.error('获取存储失败:', error)
    return null
  }
}

// Token相关
export const setToken = (token: string): void => {
  setStorage(constant.local_storage_token, token)
}

export const getToken = (): string | null => {
  return getStorage<string>(constant.local_storage_token)
}

// 用户信息相关
export const setUserInfo = (userInfo: any): void => {
  setStorage(constant.local_storage_user_info, userInfo)
}

export const getUserInfo = <T = any>(): T | null => {
  return getStorage<T>(constant.local_storage_user_info)
}

export default {
  setStorage,
  getStorage,
  setToken,
  getToken,
  setUserInfo,
  getUserInfo
}
```

### 4. 通用工具函数 (common/utils.ts)

```typescript
// 防抖函数
export const debounce = (func: Function, wait: number) => {
  let timeout: any = null
  return (...args: any[]) => {
    if (timeout) clearTimeout(timeout)
    timeout = setTimeout(() => func(...args), wait)
  }
}

// 深拷贝
export const deepClone = <T>(obj: T): T => {
  if (obj === null || typeof obj !== 'object') return obj
  if (obj instanceof Date) return new Date(obj.getTime()) as any
  if (obj instanceof Array) return obj.map(item => deepClone(item)) as any
  
  const clonedObj = {} as any
  for (const key in obj) {
    if (obj.hasOwnProperty(key)) {
      clonedObj[key] = deepClone(obj[key])
    }
  }
  return clonedObj
}

// 数组去重
export const uniqueArray = <T>(arr: T[]): T[] => {
  return [...new Set(arr)]
}

// 时间格式化
export const formatTime = (time: number | Date, format = 'YYYY-MM-DD HH:mm:ss'): string => {
  const date = typeof time === 'number' ? new Date(time) : time
  
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  const seconds = String(date.getSeconds()).padStart(2, '0')
  
  return format
    .replace('YYYY', String(year))
    .replace('MM', month)
    .replace('DD', day)
    .replace('HH', hours)
    .replace('mm', minutes)
    .replace('ss', seconds)
}

// 金额格式化
export const formatMoney = (amount: number, currency = '¥'): string => {
  return currency + amount.toFixed(2)
}

// 复制到剪贴板
export const copyToClipboard = (text: string): Promise<void> => {
  return new Promise((resolve, reject) => {
    uni.setClipboardData({
      data: text,
      success: () => {
        uni.showToast({ title: '复制成功', icon: 'success' })
        resolve()
      },
      fail: reject
    })
  })
}
export const navigateTo = (url: string, params?: Record<string, any>): void => {
  let fullUrl = url
  if (params) {
    const queryString = buildUrlParams(params)
    fullUrl += (url.includes('?') ? '&' : '?') + queryString
  }
  
  uni.navigateTo({ url: fullUrl })
}

export const redirectTo = (url: string, params?: Record<string, any>): void => {
  let fullUrl = url
  if (params) {
    const queryString = buildUrlParams(params)
    fullUrl += (url.includes('?') ? '&' : '?') + queryString
  }
  
  uni.redirectTo({ url: fullUrl })
}

export const switchTab = (url: string): void => {
  uni.switchTab({ url })
}

export const navigateBack = (delta = 1): void => {
  uni.navigateBack({ delta })
}
```

### 5. 数据验证工具 (common/validator.ts)

```typescript
import { BUSINESS_CONSTANTS } from './constant'

// 验证结果接口
interface ValidationResult {
  valid: boolean
  message?: string
}

// 验证规则接口
interface ValidationRule {
  required?: boolean
  min?: number
  max?: number
  pattern?: RegExp
  custom?: (value: any) => boolean | string
  message?: string
}

// 基础验证函数
export const isRequired = (value: any): boolean => {
  if (typeof value === 'string') return value.trim().length > 0
  if (Array.isArray(value)) return value.length > 0
  return value !== null && value !== undefined
}

export const isEmail = (email: string): boolean => {
  return BUSINESS_CONSTANTS.REGEX.EMAIL.test(email)
}

export const isPhone = (phone: string): boolean => {
  return BUSINESS_CONSTANTS.REGEX.PHONE.test(phone)
}

export const isIdCard = (idCard: string): boolean => {
  return BUSINESS_CONSTANTS.REGEX.ID_CARD.test(idCard)
}

export const isPassword = (password: string): boolean => {
  return BUSINESS_CONSTANTS.REGEX.PASSWORD.test(password)
}

export const isUrl = (url: string): boolean => {
  try {
    new URL(url)
    return true
  } catch {
    return false
  }
}

export const isNumber = (value: any): boolean => {
  return !isNaN(Number(value)) && isFinite(Number(value))
}

export const isInteger = (value: any): boolean => {
  return Number.isInteger(Number(value))
}

export const isPositive = (value: number): boolean => {
  return value > 0
}

export const isInRange = (value: number, min: number, max: number): boolean => {
  return value >= min && value <= max
}

// 字符串长度验证
export const isMinLength = (value: string, min: number): boolean => {
  return value.length >= min
}

export const isMaxLength = (value: string, max: number): boolean => {
  return value.length <= max
}

export const isLengthInRange = (value: string, min: number, max: number): boolean => {
  return value.length >= min && value.length <= max
}

// 数组验证
export const isArrayNotEmpty = (arr: any[]): boolean => {
  return Array.isArray(arr) && arr.length > 0
}

export const isArrayMinLength = (arr: any[], min: number): boolean => {
  return Array.isArray(arr) && arr.length >= min
}

export const isArrayMaxLength = (arr: any[], max: number): boolean => {
  return Array.isArray(arr) && arr.length <= max
}

// 文件验证
export const isValidFileSize = (size: number, maxSize = BUSINESS_CONSTANTS.MAX_FILE_SIZE): boolean => {
  return size <= maxSize
}

export const isValidImageType = (filename: string): boolean => {
  const ext = filename.split('.').pop()?.toLowerCase()
  return ext ? BUSINESS_CONSTANTS.ALLOWED_IMAGE_TYPES.includes(ext) : false
}

export const isValidVideoType = (filename: string): boolean => {
  const ext = filename.split('.').pop()?.toLowerCase()
  return ext ? BUSINESS_CONSTANTS.ALLOWED_VIDEO_TYPES.includes(ext) : false
}

// 综合验证器
export const validate = (value: any, rules: ValidationRule[]): ValidationResult => {
  for (const rule of rules) {
    // 必填验证
    if (rule.required && !isRequired(value)) {
      return {
        valid: false,
        message: rule.message || '此字段为必填项'
      }
    }
    
    // 如果值为空且非必填，跳过其他验证
    if (!isRequired(value) && !rule.required) {
      continue
    }
    
    // 最小长度验证
    if (rule.min !== undefined) {
      if (typeof value === 'string' && !isMinLength(value, rule.min)) {
        return {
          valid: false,
          message: rule.message || `最少需要${rule.min}个字符`
        }
      }
      if (typeof value === 'number' && value < rule.min) {
        return {
          valid: false,
          message: rule.message || `值不能小于${rule.min}`
        }
      }
    }
    
    // 最大长度验证
    if (rule.max !== undefined) {
      if (typeof value === 'string' && !isMaxLength(value, rule.max)) {
        return {
          valid: false,
          message: rule.message || `最多允许${rule.max}个字符`
        }
      }
      if (typeof value === 'number' && value > rule.max) {
        return {
          valid: false,
          message: rule.message || `值不能大于${rule.max}`
        }
      }
    }
    
    // 正则验证
    if (rule.pattern && !rule.pattern.test(String(value))) {
      return {
        valid: false,
        message: rule.message || '格式不正确'
      }
    }
    
    // 自定义验证
    if (rule.custom) {
      const result = rule.custom(value)
      if (typeof result === 'string') {
        return {
          valid: false,
          message: result
        }
      }
      if (!result) {
        return {
          valid: false,
          message: rule.message || '验证失败'
        }
      }
    }
  }
  
  return { valid: true }
}

// 表单验证器
export const validateForm = (
  data: Record<string, any>,
  rules: Record<string, ValidationRule[]>
): { valid: boolean; errors: Record<string, string> } => {
  const errors: Record<string, string> = {}
  let valid = true
  
  Object.keys(rules).forEach(field => {
    const fieldRules = rules[field]
    const fieldValue = data[field]
    const result = validate(fieldValue, fieldRules)
    
    if (!result.valid) {
      valid = false
      errors[field] = result.message || '验证失败'
    }
  })
  
  return { valid, errors }
}

// 常用验证规则预设
export const VALIDATION_RULES = {
  required: { required: true, message: '此字段为必填项' },
  email: { pattern: BUSINESS_CONSTANTS.REGEX.EMAIL, message: '请输入有效的邮箱地址' },
  phone: { pattern: BUSINESS_CONSTANTS.REGEX.PHONE, message: '请输入有效的手机号码' },
  password: { pattern: BUSINESS_CONSTANTS.REGEX.PASSWORD, message: '密码至少8位，包含大小写字母和数字' },
  idCard: { pattern: BUSINESS_CONSTANTS.REGEX.ID_CARD, message: '请输入有效的身份证号码' }
}
```

## 使用示例

### 1. 在页面中使用工具函数

```vue
<script setup>
import { ref } from 'vue'
import { 
  debounce, 
  formatMoney, 
  formatRelativeTime, 
  copyToClipboard,
  navigateTo
} from '@/common/utils'
import { validateForm, VALIDATION_RULES } from '@/common/validator'
import { setUserInfo, getUserInfo } from '@/common/storage'

// 使用防抖
const searchKeyword = ref('')
const handleSearch = debounce((keyword) => {
  console.log('搜索:', keyword)
}, 500)

// 使用格式化
const price = ref(12345.67)
const formattedPrice = computed(() => formatMoney(price.value))

// 使用验证
const formData = ref({
  email: '',
  phone: '',
  password: ''
})

const validationRules = {
  email: [VALIDATION_RULES.required, VALIDATION_RULES.email],
  phone: [VALIDATION_RULES.required, VALIDATION_RULES.phone],
  password: [VALIDATION_RULES.required, VALIDATION_RULES.password]
}

const handleSubmit = () => {
  const { valid, errors } = validateForm(formData.value, validationRules)
  if (!valid) {
    console.log('验证错误:', errors)
    return
  }
  // 提交表单
}

// 使用存储
const saveUserData = () => {
  const userData = { name: '张三', age: 25 }
  setUserInfo(userData)
}

const loadUserData = () => {
  const userData = getUserInfo()
  console.log('用户数据:', userData)
}
</script>
```

### 2. 创建新的工具模块

```typescript
// common/date-utils.ts
import { formatTime } from './utils'

// 获取今天的开始时间
export const getStartOfDay = (date = new Date()): Date => {
  const start = new Date(date)
  start.setHours(0, 0, 0, 0)
  return start
}

// 获取今天的结束时间
export const getEndOfDay = (date = new Date()): Date => {
  const end = new Date(date)
  end.setHours(23, 59, 59, 999)
  return end
}

// 获取本周的开始时间
export const getStartOfWeek = (date = new Date()): Date => {
  const start = new Date(date)
  const day = start.getDay()
  const diff = start.getDate() - day + (day === 0 ? -6 : 1)
  start.setDate(diff)
  start.setHours(0, 0, 0, 0)
  return start
}

// 计算两个日期之间的天数
export const getDaysBetween = (date1: Date, date2: Date): number => {
  const oneDay = 24 * 60 * 60 * 1000
  return Math.round(Math.abs((date1.getTime() - date2.getTime()) / oneDay))
}

// 格式化日期范围
export const formatDateRange = (startDate: Date, endDate: Date): string => {
  const start = formatTime(startDate, 'MM-DD')
  const end = formatTime(endDate, 'MM-DD')
  return `${start} 至 ${end}`
}
```

## 最佳实践

1. **模块化设计**: 按功能将工具函数分组到不同文件中
2. **类型安全**: 为所有工具函数添加TypeScript类型定义
3. **错误处理**: 在工具函数中添加适当的错误处理
4. **性能优化**: 对频繁调用的函数进行性能优化
5. **单元测试**: 为工具函数编写单元测试
6. **文档注释**: 为复杂的工具函数添加详细注释
7. **向后兼容**: 工具函数API变更时保持向后兼容

## 注意事项

1. **平台兼容性**: 确保工具函数在不同平台上正常工作
2. **内存管理**: 避免在工具函数中创建内存泄漏
3. **异步处理**: 正确处理异步操作和Promise
4. **边界条件**: 考虑各种边界条件和异常情况
5. **性能影响**: 避免在工具函数中进行重复计算
6. **依赖管理**: 合理管理工具函数之间的依赖关系
# 路由配置开发说明

## 概述

`pages.json` 是uni-app的页面路由配置文件，用于配置页面路径、窗口样式、tabBar等。所有页面都必须在此文件中注册才能被访问。

## 文件位置

```
project-root/
└── pages.json          # 页面路由配置文件
```

## 基本结构

```json
{
  "pages": [
    // 页面路径配置
  ],
  "globalStyle": {
    // 全局样式配置
  },
  "tabBar": {
    // 底部导航配置
  },
  "condition": {
    // 启动模式配置
  },
  "easycom": {
    // 组件自动导入配置
  }
}
```

## 页面配置 (pages)

### 1. 基本页面注册

```json
{
  "pages": [
    {
      "path": "pages/base/pages/index",
      "style": {
        "navigationBarTitleText": "首页",
        "enablePullDownRefresh": true
      }
    },
    {
      "path": "pages/ECommerce/pages/order-list",
      "style": {
        "navigationBarTitleText": "订单列表",
        "navigationBarBackgroundColor": "#AC9157",
        "navigationBarTextStyle": "white"
      }
    }
  ]
}
```

### 2. 页面样式配置选项

```json
{
  "style": {
    "navigationBarTitleText": "页面标题",
    "navigationBarBackgroundColor": "#ffffff",
    "navigationBarTextStyle": "black",
    "backgroundColor": "#ffffff",
    "backgroundTextStyle": "dark",
    "enablePullDownRefresh": false,
    "onReachBottomDistance": 50,
    "navigationStyle": "default",
    "disableScroll": false,
    "usingComponents": {}
  }
}
```

## 全局样式配置 (globalStyle)

```json
{
  "globalStyle": {
    "navigationBarTextStyle": "black",
    "navigationBarTitleText": "PigHand AIO",
    "navigationBarBackgroundColor": "#ffffff",
    "backgroundColor": "#f5f5f5",
    "backgroundTextStyle": "dark",
    "enablePullDownRefresh": false,
    "rpxCalcMaxDeviceWidth": 960,
    "rpxCalcBaseDeviceWidth": 375
  }
}
```

## TabBar配置

### 1. 基本TabBar配置

```json
{
  "tabBar": {
    "color": "#7A7E83",
    "selectedColor": "#AC9157",
    "borderStyle": "black",
    "backgroundColor": "#ffffff",
    "position": "bottom",
    "list": [
      {
        "pagePath": "pages/base/pages/index",
        "text": "首页",
        "iconPath": "static/tab-bar-index-inactive.png",
        "selectedIconPath": "static/tab-bar-index-activity.png"
      },
      {
        "pagePath": "pages/ECommerce/pages/ticket-list",
        "text": "票务",
        "iconPath": "static/tab-bar-ticket-inactive.png",
        "selectedIconPath": "static/tab-bar-ticket-activity.png"
      },
      {
        "pagePath": "pages/base/pages/customer-service",
        "text": "客服",
        "iconPath": "static/tab-bar-service-inactive.png",
        "selectedIconPath": "static/tab-bar-service-activity.png"
      },
      {
        "pagePath": "pages/base/pages/mine",
        "text": "我的",
        "iconPath": "static/tab-bar-mine-inactive.png",
        "selectedIconPath": "static/tab-bar-mine-activity.png"
      }
    ]
  }
}
```

### 2. TabBar配置说明

- **color**: 未选中时的文字颜色
- **selectedColor**: 选中时的文字颜色
- **backgroundColor**: TabBar背景色
- **borderStyle**: TabBar边框颜色
- **position**: TabBar位置（bottom/top）
- **list**: TabBar项目列表（2-5个）

## 组件自动导入配置 (easycom)

```json
{
  "easycom": {
    "autoscan": true,
    "custom": {
      "tui-(.*)": "@/components/thorui/tui-$1/tui-$1.vue",
      "custom-(.*)": "@/components/custom-$1/custom-$1.vue"
    }
  }
}
```

## 完整示例

### 基本配置示例

```json
{
  "pages": [
    {
      "path": "pages/base/pages/index",
      "style": {
        "navigationBarTitleText": "首页",
        "enablePullDownRefresh": true
      }
    },
    {
      "path": "pages/ECommerce/pages/order-list",
      "style": {
        "navigationBarTitleText": "订单列表",
        "navigationBarBackgroundColor": "#AC9157",
        "navigationBarTextStyle": "white"
      }
    }
  ],
  "globalStyle": {
    "navigationBarTextStyle": "black",
    "navigationBarTitleText": "PigHand AIO",
    "navigationBarBackgroundColor": "#ffffff",
    "backgroundColor": "#f5f5f5"
  },
  "tabBar": {
    "color": "#7A7E83",
    "selectedColor": "#AC9157",
    "backgroundColor": "#ffffff",
    "list": [
      {
        "pagePath": "pages/base/pages/index",
        "text": "首页",
        "iconPath": "static/tab-bar-index-inactive.png",
        "selectedIconPath": "static/tab-bar-index-activity.png"
      },
      {
        "pagePath": "pages/base/pages/mine",
        "text": "我的",
        "iconPath": "static/tab-bar-mine-inactive.png",
        "selectedIconPath": "static/tab-bar-mine-activity.png"
      }
    ]
  },
  "easycom": {
    "autoscan": true,
    "custom": {
      "custom-(.*)": "@/components/custom-$1/custom-$1.vue"
    }
  }
}
```

## 开发流程

### 1. 添加新页面

当创建新页面时，需要在pages.json中注册：

```json
{
  "pages": [
    // 现有页面...
    {
      "path": "pages/ECommerce/pages/product-detail",
      "style": {
        "navigationBarTitleText": "商品详情",
        "navigationBarBackgroundColor": "#AC9157",
        "navigationBarTextStyle": "white"
      }
    }
  ]
}
```

### 2. 修改TabBar

如果需要修改底部导航：

```json
{
  "tabBar": {
    "list": [
      // 现有tab项...
      {
        "pagePath": "pages/new/pages/new-page",
        "text": "新功能",
        "iconPath": "static/new-inactive.png",
        "selectedIconPath": "static/new-active.png"
      }
    ]
  }
}
```

### 3. 配置页面样式

根据页面需求配置样式：

```json
{
  "style": {
    "navigationBarTitleText": "页面标题",
    "navigationBarBackgroundColor": "#AC9157",
    "navigationBarTextStyle": "white",
    "enablePullDownRefresh": true,
    "onReachBottomDistance": 50,
    "backgroundColor": "#f5f5f5"
  }
}
```

## 常用配置模板

### 1. 列表页面配置

```json
{
  "path": "pages/module/pages/list",
  "style": {
    "navigationBarTitleText": "列表页面",
    "enablePullDownRefresh": true,
    "onReachBottomDistance": 50,
    "backgroundColor": "#f5f5f5"
  }
}
```

### 2. 详情页面配置

```json
{
  "path": "pages/module/pages/detail",
  "style": {
    "navigationBarTitleText": "详情页面",
    "navigationBarBackgroundColor": "#AC9157",
    "navigationBarTextStyle": "white"
  }
}
```

### 3. 表单页面配置

```json
{
  "path": "pages/module/pages/form",
  "style": {
    "navigationBarTitleText": "表单页面",
    "disableScroll": true
  }
}
```

### 4. 自定义导航栏页面

```json
{
  "path": "pages/module/pages/custom-nav",
  "style": {
    "navigationStyle": "custom",
    "backgroundColor": "#AC9157"
  }
}
```

## 页面跳转方式

### 1. 普通页面跳转

```javascript
// 保留当前页面，跳转到应用内的某个页面
uni.navigateTo({
  url: '/pages/ECommerce/pages/order-detail?id=123'
})

// 关闭当前页面，跳转到应用内的某个页面
uni.redirectTo({
  url: '/pages/base/pages/login'
})

// 跳转到tabBar页面，并关闭其他所有非tabBar页面
uni.switchTab({
  url: '/pages/base/pages/index'
})

// 关闭所有页面，打开到应用内的某个页面
uni.reLaunch({
  url: '/pages/base/pages/index'
})

// 关闭当前页面，返回上一页面或多级页面
uni.navigateBack({
  delta: 1
})
```

### 2. 带参数跳转

```javascript
// 跳转时传递参数
uni.navigateTo({
  url: '/pages/detail?id=123&type=product&name=商品名称'
})

// 在目标页面接收参数
const pages = getCurrentPages()
const currentPage = pages[pages.length - 1]
const { id, type, name } = currentPage.options
```

## 注意事项

1. **页面路径**: 页面路径不需要加.vue后缀
2. **首页设置**: pages数组中的第一个页面为应用首页
3. **TabBar限制**: TabBar最少2个，最多5个tab项
4. **图标规范**: TabBar图标建议使用81x81px的PNG图片
5. **路径规范**: 所有路径都是相对于项目根目录的相对路径
6. **样式继承**: 页面样式会继承globalStyle中的配置
7. **条件编译**: 可以使用条件编译为不同平台配置不同的样式

## 条件编译示例

```json
{
  "pages": [
    {
      "path": "pages/base/pages/index",
      "style": {
        "navigationBarTitleText": "首页",
        // #ifdef MP-WEIXIN
        "navigationBarBackgroundColor": "#AC9157",
        // #endif
        // #ifdef H5
        "navigationBarBackgroundColor": "#ffffff",
        // #endif
        "enablePullDownRefresh": true
      }
    }
  ]
}
```

## 调试技巧

1. **使用condition**: 在开发时可以配置condition快速跳转到指定页面
2. **页面栈管理**: 注意页面栈的层级，避免页面栈过深
3. **TabBar调试**: 使用uni.switchTab()跳转TabBar页面
4. **参数传递**: 复杂参数建议使用全局数据或缓存传递
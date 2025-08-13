<!--
    自定义TabBar组件
    功能：
    1. 基于pages.json配置自动生成底部导航栏
    2. 支持路由切换和当前页面状态同步
    3. 提供点击切换和路由监听功能
-->
<template>
	<tui-tabbar :current="tabBarCurrent" :tabBar="tabBarConfig" selectedColor="#AC9157" @click="tabbarSwitch"></tui-tabbar>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import pageJson from '@/pages.json';

/**
 * 从pages.json中读取tabBar配置并转换为组件所需格式
 * 自动添加路径前缀"/"确保路径格式正确
 */
const tabBarConfig = ref((pageJson?.tabBar?.list || []).map(item => {
    return {
        pagePath: '/' + item.pagePath,      // 页面路径
        text: item.text,                   // 显示文本
        iconPath: '/' + item.iconPath,     // 未选中图标
        selectedIconPath: '/' + item.selectedIconPath  // 选中图标
    }
}))

// 当前选中的tab索引
const tabBarCurrent = ref(0)

/**
 * 根据当前路由更新tabBarCurrent
 * 通过getCurrentPages()获取当前页面栈，匹配路径更新选中状态
 */
const updateTabBarCurrent = () => {
    const currentPages = getCurrentPages()
    const currentPage = currentPages[currentPages.length - 1]
    const currentPath = '/' + currentPage.route
    
    const index = tabBarConfig.value.findIndex(item => item.pagePath === currentPath)
    if (index > -1) {
        tabBarCurrent.value = index
    }
}

/**
 * 隐藏系统TabBar
 * 只有在TabBar页面才需要隐藏系统TabBar
 */
const hideSystemTabBar = () => {
    try {
        uni.hideTabBar()
    } catch (error) {
        console.warn('隐藏TabBar失败:', error)
    }
}

/**
 * 组件挂载时的初始化逻辑
 */
onMounted(() => {
    // 初始化时更新一次当前选中状态
    updateTabBarCurrent()
    
    hideSystemTabBar()
    
    // 监听路由变化 - 使用before拦截器提前更新
    uni.addInterceptor('switchTab', {
        invoke: (args) => {
            // 在路由跳转前立即更新tabBarCurrent
            const targetIndex = tabBarConfig.value.findIndex(item => item.pagePath === args.url)
            if (targetIndex > -1) {
                tabBarCurrent.value = targetIndex
            }
        },
        success: () => {
            updateTabBarCurrent()
            // 切换到TabBar页面后延迟隐藏系统TabBar
            setTimeout(() => {
                hideSystemTabBar()
            }, 100)
        }
    })
})

/**
 * tab点击切换处理函数
 * @param {Object} e - 点击事件对象，包含index属性
 */
const tabbarSwitch = (e) => {
    // 立即更新当前选中状态，避免异步更新，看起来像卡顿
    tabBarCurrent.value = e.index
    
    uni.switchTab({
        url: tabBarConfig.value[e.index].pagePath
    });
}
</script>

<style lang="scss" scoped>
</style>

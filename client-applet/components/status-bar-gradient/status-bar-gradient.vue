<template>
	<view class="status-bar-gradient" :style="gradientStyle"></view>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'

// 获取系统信息
const systemInfo = ref({})
const safeAreaHeight = ref('44px') // 默认安全区高度

onMounted(() => {
	try {
		systemInfo.value = uni.getSystemInfoSync()
		// 计算安全区高度：状态栏高度 + 导航栏高度
		const statusBarHeight = systemInfo.value.statusBarHeight || 20
		const navHeight = 44 // 导航栏默认高度
		safeAreaHeight.value = `${statusBarHeight + navHeight}px`
	} catch (e) {
		console.warn('获取系统信息失败，使用默认安全区高度', e)
	}
})

// 定义props
const props = defineProps({
	// 渐变颜色，支持rgb、rgba、hex等格式
	color: {
		type: String,
		default: 'white'
	},
	// 顶部不透明度
	topOpacity: {
		type: Number,
		default: 0.8
	},
	// 中间不透明度
	middleOpacity: {
		type: Number,
		default: 0.4
	},
	// 遮罩高度，默认为安全区高度
	height: {
		type: String,
		default: ''
	}
})

// 计算渐变样式
const gradientStyle = computed(() => {
	const { color, topOpacity, middleOpacity, height } = props
	// 如果没有传入高度，使用安全区高度
	const finalHeight = height || safeAreaHeight.value
	
	// 处理颜色值，转换为rgba格式
	let rgbaColor
	if (color === 'white') {
		rgbaColor = '255, 255, 255'
	} else if (color === 'black') {
		rgbaColor = '0, 0, 0'
	} else if (color.startsWith('#')) {
		// 处理hex颜色
		const hex = color.slice(1)
		const r = parseInt(hex.slice(0, 2), 16)
		const g = parseInt(hex.slice(2, 4), 16)
		const b = parseInt(hex.slice(4, 6), 16)
		rgbaColor = `${r}, ${g}, ${b}`
	} else if (color.startsWith('rgb')) {
		// 提取rgb值
		const match = color.match(/\d+/g)
		if (match && match.length >= 3) {
			rgbaColor = `${match[0]}, ${match[1]}, ${match[2]}`
		} else {
			rgbaColor = '255, 255, 255' // 默认白色
		}
	} else {
		// 默认白色
		rgbaColor = '255, 255, 255'
	}
	
	return {
		height: finalHeight,
		background: `linear-gradient(to bottom, rgba(${rgbaColor}, ${topOpacity}) 0%, rgba(${rgbaColor}, ${middleOpacity}) 50%, transparent 100%)`
	}
})
</script>

<style scoped>
.status-bar-gradient {
	position: absolute;
	top: 0;
	left: 0;
	right: 0;
	pointer-events: none;
	z-index: 2;
}
</style>
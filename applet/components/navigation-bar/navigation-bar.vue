<template>
	<view class="navigation-bar" :style="'position: relative; height: ' + navigationBarHeight + 'px'">
		
		<view class="title"
			:style="'height: ' + navigationBarHeight + 'px; line-height: ' + navigationBarHeight + 'px;'"
			v-if="title">
			{{ title }}
		</view>
		
		<view class="back" @click="onBack">
			<image src="../static/images/back.png" mode="widthFix"></image>
		</view>
	</view>
</template>

<script setup>
import { ref, onMounted } from 'vue'

// 定义 props
const props = defineProps({
	title: {
		type: String,
		default: ''
	},
	pages: {
		type: Array,
		default: () => []
	}
})

// 响应式数据
const navigationBarHeight = ref(0)

// 生命周期钩子
onMounted(() => {
	const statusBarHeight = uni.getSystemInfoSync().statusBarHeight
	const MenuButton = wx.getMenuButtonBoundingClientRect()
	navigationBarHeight.value = MenuButton.height + (MenuButton.top - statusBarHeight) * 2
})

// 方法
const onBack = () => {
	let pages = getCurrentPages()
	
	if (pages.length > 1) {
		uni.navigateBack({
			delta: -1
		})
	} else {
		uni.switchTab({
			url: '/pages/base/pages/index'
		})
	}
}
</script>

<style lang="scss" scoped>
	.navigation-bar {
		width: 100%;
		
		.title {
			color: #242424ff;
			font-size: 32rpx;
			font-weight: 600;
			position: absolute;
			width: 100%;
			top: 0;
			text-align: center;
			pointer-events: none;
		}
		
		.back {
			height: 100%;
			width: 80rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			
			image {
				width: 40rpx;
				height: 40rpx;
			}
		}
	}
</style>

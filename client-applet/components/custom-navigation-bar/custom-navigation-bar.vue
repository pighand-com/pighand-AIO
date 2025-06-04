<template>
	<view class="navigation-bar" :style="'position: relative; height: ' + (navigationBarHeight + statusBarHeight) + 'px'">
		<!-- 状态栏空位 -->
		<view class="status-bar" :style="'height: ' + statusBarHeight + 'px'"></view>
		
		<view class="title"
			:style="'height: ' + navigationBarHeight + 'px; line-height: ' + navigationBarHeight + 'px;'"
			v-if="title">
			{{ title }}
		</view>
		
		<view class="back" @click="onBack" v-if="back">
			<image src="./static/back.png" mode="widthFix"></image>
		</view>
	</view>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const props = defineProps({
	title: {
		type: String,
		default: ''
	},
	back: {
		type: Boolean,
		default: false
	}
})

const navigationBarHeight = ref(0)
const statusBarHeight = ref(0)

onMounted(() => {
	const systemInfo = uni.getSystemInfoSync()
	statusBarHeight.value = systemInfo.statusBarHeight
	const MenuButton = wx.getMenuButtonBoundingClientRect()
	navigationBarHeight.value = MenuButton.height + (MenuButton.top - statusBarHeight.value) * 2
})

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
		
		.status-bar {
			width: 100%;
			background-color: transparent;
		}
		
		.title {
			color: #242424ff;
			font-size: 32rpx;
			font-weight: 600;
			position: absolute;
			width: 100%;
			top: var(--status-bar-height, 0px);
			text-align: center;
			pointer-events: none;
		}
		
		.back {
			height: 100%;
			width: 80rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			position: absolute;
			top: var(--status-bar-height, 0px);
			
			image {
				width: 40rpx;
				height: 40rpx;
			}
		}
	}
</style>

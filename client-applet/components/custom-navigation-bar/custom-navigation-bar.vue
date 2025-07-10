<template>
	<view class="navigation-bar" 
		:style="'height: ' + (navigationBarHeight + statusBarHeight) + 'px; position: ' + props.position + '; --status-bar-height: ' + statusBarHeight + 'px; --navigation-bar-height: ' + navigationBarHeight + 'px;'">
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

/**
 * defineProps 入参说明：
 * @property {String} title 导航栏标题文本
 * @property {Boolean} back 是否显示返回按钮
 * @property {String} backPath 自定义返回路径，优先跳转该路径
 * @property {String} position 定位方式，默认 absolute，可选 fixed/static
 */
const props = defineProps({
	title: {
		type: String,
		default: ''
	},
	back: {
		type: Boolean,
		default: false
	},
	backPath: {
		type: String,
		default: ''
	},
	position: {
		type: String,
		default: 'absolute'
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
	if (props.backPath) {
		uni.navigateTo({
			url: props.backPath
		})
		return
	}
	
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
		top: 0;
		left: 0;
		width: 100%;
		z-index: 1000;
		
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
			height: var(--navigation-bar-height, 44px);
			width: 80rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			position: absolute;
			top: var(--status-bar-height, 0px);
			left: 0;
			z-index: 1001;
			
			image {
				width: 40rpx;
				height: 40rpx;
			}
		}
	}
</style>

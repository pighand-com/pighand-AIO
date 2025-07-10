<template>
	<view 
		:class="['custom-btn', { 'auto-height': !height }]"
		@click="handleClick"
		:style="{
			width: width,
			height: height,
			borderRadius: borderRadius
		}"
	>
		<image 
			src="/static/button-bg.png" 
			class="btn-bg-image" 
			:mode="height ? 'aspectFill' : 'widthFix'"
		></image>
		<text 
			class="btn-text"
			:style="{
				fontSize: fontSize,
				lineHeight: fontSize,
				color: textColor
			}"
		>{{ text }}</text>
	</view>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'

// 定义props
const props = defineProps({
	text: {
		type: String,
		default: '按钮'
	},
	width: {
		type: String,
		default: '300rpx'
	},
	height: {
		type: String,
		default: ''
	},
	borderRadius: {
		type: String,
		default: '0'
	},
	fontSize: {
		type: String,
		default: '32rpx'
	},
	textColor: {
		type: String,
		default: '#ffffff'
	}
})

// 定义事件
const emit = defineEmits(['click'])

// 处理点击事件
const handleClick = () => {
	emit('click')
}
</script>

<style scoped>
.custom-btn {
	position: relative;
	overflow: hidden;
	box-shadow: 0 4rpx 12rpx rgba(255, 107, 53, 0.3);
	display: flex;
	align-items: center;
	justify-content: center;
}

.btn-bg-image {
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	z-index: 1;
}

/* 当没有设置高度时，让容器高度自适应图片 */
.custom-btn.auto-height {
	height: auto;
	min-height: 60rpx; /* 设置最小高度确保文字显示 */
}

.custom-btn.auto-height .btn-bg-image {
	position: static;
	width: 100%;
	height: auto;
}

.custom-btn.auto-height .btn-text {
	position: absolute;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
	z-index: 2;
}

.btn-text {
	position: relative;
	z-index: 2;
	font-weight: bold;
	width: 100%;
}
</style>
<template>
	<view class="page">
		<custom-navigation-bar />

		<!-- 背景图片 -->
		<view class="bg-container" v-if="bgImageExists">
			<image class="bg-image" src="../static/mine-bg.png" mode="widthFix" @error="onBgImageError"></image>
			<view class="bg-gradient"></view>
		</view>
		
		<!-- 用户信息区域 -->
		<view class="user-info">
			<!-- 头像 -->
			<user-check :item="['login', 'avatar']">
				<view class="avatar-container">
					<image class="avatar" :src="userInfo?.avatar || defaultAvatar" mode="aspectFill"></image>
				</view>
			</user-check>
			
			<!-- 手机号 -->
			<view class="phone-container">
				<user-check :item="['login', 'phone']">
					<text class="phone-text">{{ userInfo?.phone || '获取手机号' }}</text>
				</user-check>
			</view>
		</view>
		
		<!-- 订单区域 -->
		<view class="order-section">
			<user-check :item="['login']">
				<view class="order-box" @click="goToOrder">
					<text class="order-text">我的订单</text>
					<text class="order-arrow">></text>
				</view>
			</user-check>
		</view>
		
		<tab-bar />
	</view>
</template>

<script setup>
import { ref, computed } from 'vue'
import { getUserInfo } from '@/common/storage'

import defaultAvatar from '../static/default-avatar.jpg'


const userInfo = ref(getUserInfo())
const bgImageExists = ref(true)

// 监听storage变化
uni.$on('storage-changed', () => {
    userInfo.value = getUserInfo()
})

// 背景图片加载失败处理
function onBgImageError() {
    bgImageExists.value = false
}

// 跳转到订单页面
function goToOrder() {
	uni.navigateTo({
		url: '/pages/order/index'
	})
}
</script>

<style scoped>
.page {
	position: relative;
	min-height: 100vh;
	background: #000;
}

.bg-container {
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	z-index: 0;
}

.bg-image {
	width: 100%;
	height: auto;
	display: block;
}

.bg-gradient {
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	background: linear-gradient(to bottom, transparent 0%, #000 100%);
}

.user-info {
	display: flex;
    align-items: center;
    padding: 0 40rpx;
}

.avatar-container {
	margin-right: 30rpx;
}

.avatar {
	width: 120rpx;
	height: 120rpx;
	border-radius: 60rpx;
	border: 4rpx solid rgba(255, 255, 255, 0.3);
}

.phone-container {
	flex: 1;
}

.phone-text {
	font-size: 32rpx;
	color: #fff;
	font-weight: 500;
}

.order-section {
	padding: 48rpx;
}

.order-box {
	background: rgba(255, 255, 255, 0.9);
	border-radius: 20rpx;
	padding: 40rpx 30rpx;
	display: flex;
	justify-content: space-between;
	align-items: center;
	box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.1);
}

.order-text {
	font-size: 32rpx;
	color: #333;
	font-weight: 500;
}

.order-arrow {
	font-size: 32rpx;
	color: #999;
	font-weight: bold;
}
</style>
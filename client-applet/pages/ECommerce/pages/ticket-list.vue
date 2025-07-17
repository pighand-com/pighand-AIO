<template>
	<view class="content">
        <custom-navigation-bar position="flex" title="票务列表" />

		<!-- 票务列表 -->
		<view class="ticket-list">
			<view class="ticket-item" 
				  v-for="ticket in ticketList" 
				  :key="ticket.id">
				<!-- 封面图 -->
				<view class="ticket-cover" v-if="ticket.posterUrl">
					<image :src="ticket.posterUrl" 
						   class="cover-image" 
						   mode="aspectFill"></image>
				</view>
				
				<!-- 票务信息 -->
				<view class="ticket-info">
					<text class="ticket-name">{{ ticket.name }}</text>
					
					<!-- 价格信息 -->
					<view class="price-info">
						<!-- 有原价且与现价不同时显示原价 -->
						<text class="original-price" 
							  v-if="ticket.originalPrice && ticket.originalPrice !== ticket.currentPrice">
							¥{{ formatPrice(ticket.originalPrice) }}
						</text>
						<!-- 现价 -->
						<text class="current-price">
							¥{{ formatPrice(ticket.currentPrice) }}
						</text>
					</view>
				</view>
				
				<!-- 立即购买按钮 -->
				<view class="buy-button" @click="goToDetail(ticket.id)">
					<text class="buy-text">立即购买</text>
				</view>
			</view>
		</view>
		
		<!-- 空状态 -->
		<view class="empty-state" v-if="ticketList.length === 0 && !isLoading">
			<text class="empty-text">暂无票务信息</text>
		</view>
		
		<tab-bar />
		
		<!-- 加载状态 -->
		<view class="loading-state" v-if="isLoading">
			<text class="loading-text">加载中...</text>
		</view>
	</view>
</template>

<script setup>
import { onLoad } from '@dcloudio/uni-app'
import { ref } from 'vue'
import Decimal from 'decimal.js'
import { ticket as ticketAPI } from '@/api'

// 票务列表数据
const ticketList = ref([])
const isLoading = ref(false)

onLoad((options) => {
	getTicketList()
})

// 获取票务列表
const getTicketList = async () => {
	isLoading.value = true
	try {
		const res = await ticketAPI.query({})
		ticketList.value = res.records || []
	} catch (error) {
		console.error('获取票务列表失败:', error)
		uni.showToast({
			title: '获取票务列表失败',
			icon: 'none'
		})
	} finally {
		isLoading.value = false
	}
}

// 格式化价格（分转元）
const formatPrice = (price) => {
	if (!price) return '0'
	return new Decimal(price).div(100).toNumber()
}

// 跳转到票务详情
const goToDetail = (ticketId) => {
	uni.navigateTo({
		url: `/pages/ECommerce/pages/ticket-detail?id=${ticketId}`
	})
}
</script>

<style scoped>
.content {
	background-color: #f5f5f5;
	position: relative;
	height: 100vh;
	display: flex;
	flex-direction: column;
}

.ticket-list {
	padding: 20rpx;
	flex: 1;
	overflow-y: auto;
	padding-bottom: calc(100rpx + env(safe-area-inset-bottom) + 20rpx);
}

.ticket-item {
	display: flex;
	align-items: center;
	background-color: #fff;
	border-radius: 20rpx;
	padding: 30rpx;
	margin-bottom: 20rpx;
	box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);
	transition: all 0.3s ease;
}

.ticket-item:active {
	transform: scale(0.98);
	background-color: #f8f8f8;
}

.ticket-cover {
	width: 240rpx;
	height: 140rpx;
	border-radius: 12rpx;
	overflow: hidden;
	margin-right: 40rpx;
	flex-shrink: 0;
}

.ticket-item:not(:has(.ticket-cover)) .ticket-info {
	margin-left: 0;
}

.cover-image {
	width: 100%;
	height: 100%;
}

.ticket-info {
	flex: 1;
	display: flex;
	flex-direction: column;
	justify-content: center;
}

.ticket-name {
	font-size: 32rpx;
	font-weight: bold;
	color: #333;
	margin-bottom: 15rpx;
	line-height: 1.4;
}

.price-info {
	display: flex;
	align-items: center;
	gap: 15rpx;
}

.original-price {
	font-size: 24rpx;
	color: #999;
	text-decoration: line-through;
}

.current-price {
	font-size: 28rpx;
	font-weight: bold;
	color: #ff4444;
}

.buy-button {
	padding: 15rpx 25rpx;
	background: linear-gradient(135deg, #ff8c00 0%, #ff6b00 100%);
	border-radius: 25rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	flex-shrink: 0;
	box-shadow: 0 4rpx 15rpx rgba(255, 140, 0, 0.3);
	transition: all 0.3s ease;
}

.buy-button:active {
	transform: scale(0.95);
}

.buy-text {
	font-size: 24rpx;
	color: #ffffff;
	font-weight: bold;
}

.empty-state {
	display: flex;
	align-items: center;
	justify-content: center;
	height: 400rpx;
}

.empty-text {
	font-size: 28rpx;
	color: #999;
}

.loading-state {
	display: flex;
	align-items: center;
	justify-content: center;
	height: 400rpx;
}

.loading-text {
	font-size: 28rpx;
	color: #999;
}
</style>
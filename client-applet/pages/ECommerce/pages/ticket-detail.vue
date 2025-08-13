<template>
	<custom-navigation-bar :back="true" />

	<view class="content">
		<!-- 票务封面 -->
		<view class="ticket-poster" v-if="ticketDetail.posterUrl">
			<image :src="ticketDetail.posterUrl" class="poster-image" mode="widthFix"></image>
		</view>
		
		<!-- 票务信息 -->
		<view class="ticket-info-section">
			<text class="ticket-name" v-if="ticketDetail.name">{{ ticketDetail.name }}</text>
			
			<!-- 价格信息 -->
			<view class="price-section">
				<!-- 有原价且与现价不同时显示原价 -->
				<text class="original-price" 
					  v-if="ticketDetail.originalPrice && ticketDetail.originalPrice !== ticketDetail.currentPrice">
					¥{{ formatPrice(ticketDetail.originalPrice) }}
				</text>
				<!-- 现价 -->
				<text class="current-price">
					¥{{ formatPrice(ticketDetail.currentPrice) }}
				</text>
			</view>
			
			<!-- 详细描述 -->
			<view class="description-section" v-if="ticketDetail.details">
				<text class="section-title">详细描述</text>
				<text class="description-text">{{ ticketDetail.details }}</text>
			</view>
		</view>
		
		<!-- 底部操作栏 -->
		<view class="bottom-action-bar">
			<!-- 数量选择 -->
			<view class="quantity-section">
				<text class="quantity-label">数量</text>
				<view class="quantity-control">
					<view class="quantity-btn" @click="decreaseQuantity">
						<text class="btn-text">-</text>
					</view>
					<text class="quantity-text">{{ quantity }}</text>
					<view class="quantity-btn" @click="increaseQuantity">
						<text class="btn-text">+</text>
					</view>
				</view>
			</view>
			
			<!-- 购买按钮 -->
			<user-check :item="['login', 'phone']">
				<view class="buy-section">
					<view class="total-price">
						<text class="price-label">合计</text>
						<text class="price-value">¥{{ totalPrice }}</text>
					</view>
					<view class="buy-button" :class="{ 'loading': isPurchasing }" @click="purchaseTicket">
						<text class="buy-text" v-if="!isPurchasing">立即购买</text>
						<text class="buy-text" v-else>购买中...</text>
					</view>
				</view>
			</user-check>
		</view>
		
		<!-- 全屏loading遮罩 -->
		<view class="loading-overlay" v-if="isLoading">
			<view class="loading-content">
				<view class="loading-spinner"></view>
				<text class="loading-text">购买中...</text>
			</view>
		</view>
	</view>
</template>

<script setup>
import { onLoad, onShareAppMessage, onShareTimeline } from '@dcloudio/uni-app'
import { ref, computed, watch } from 'vue'
import Decimal from 'decimal.js'
import { ticket as ticketAPI, order as orderAPI } from '@/api'
import { getFromSalesId } from '@/common/storage'
import { createShareInfo } from '@/common/share'

// 票务详情数据
const ticketDetail = ref({})
const quantity = ref(1)
const totalPrice = ref(0)
const isPurchasing = ref(false) // 购票loading状态
const isLoading = ref(false) // 全屏loading状态

// 监听数量变化
watch([quantity], () => {
	// 当数量变化时，重新计算价格
	calculatePrice()
})

onLoad((options) => {
	const { id } = options
	if (id) {
		getTicketDetail(id)
	}
})

// 获取票务详情
const getTicketDetail = async (id) => {
	try {
		const res = await ticketAPI.find(id)
		ticketDetail.value = res
		calculatePrice()
	} catch (error) {
		console.error('获取票务详情失败:', error)
		uni.showToast({
			title: '获取票务详情失败',
			icon: 'none'
		})
		setTimeout(() => {
			uni.navigateBack()
		}, 1500)
	}
}

// 格式化价格（分转元）
const formatPrice = (price) => {
	if (!price) return '0'
	return new Decimal(price).div(100).toNumber()
}

// 增加数量
const increaseQuantity = () => {
	quantity.value++
}

// 减少数量
const decreaseQuantity = () => {
	if (quantity.value > 1) {
		quantity.value--
	}
}

// 计算价格
const calculatePrice = () => {
	if (!ticketDetail.value.currentPrice) {
		totalPrice.value = 0
		return
	}
	
	const price = new Decimal(ticketDetail.value.currentPrice).div(100)
	const calculatedPrice = price.mul(quantity.value).toNumber()
	totalPrice.value = calculatedPrice
}

// 购买票务（复用theme.vue的购买逻辑）
const purchaseTicket = async () => {
	if (isPurchasing.value || isLoading.value) return // 防止重复点击
	
	isPurchasing.value = true
	isLoading.value = true // 显示全屏loading
	
	try {
		// 获取FromSalesId作为salespersonId
		const fromSalesId = getFromSalesId()
		
		const orderData = {
			outTradePlatform: 'wechat_applet',
			amountPaid: new Decimal(totalPrice.value).mul(100).toNumber(),
			orderSku: [{
				ticketId: ticketDetail.value.id,
				quantity: quantity.value,
			}]
		}
		
		// 如果有FromSalesId，则添加salespersonId
		if (fromSalesId) {
			orderData.salespersonId = fromSalesId
		}
		
		const result = await orderAPI.create(orderData)

		const { nonceStr, prepayId, signType, paySign, timeStamp } = result
		uni.requestPayment({
			provider:'wxpay',
			timeStamp,
			nonceStr,
			package:`prepay_id=${prepayId}`,
			signType: signType,
			paySign: paySign,
			success(responseData) {
				isPurchasing.value = false
				isLoading.value = false
				
				if(responseData.errMsg === 'requestPayment:ok'){
					uni.showToast({
						title: '支付成功！',
						icon: 'success',
						duration: 2000
					})
					setTimeout(() => {
						uni.navigateTo({
							url:`/pages/ECommerce/pages/order-list?tradeStatus=40`
						})
					}, 2000)
				}else{
					uni.showToast({
						title: '支付取消',
						icon: 'none',
						duration: 2000
					})
					setTimeout(() => {
						uni.navigateTo({
							url:`/pages/ECommerce/pages/order-list?tradeStatus=10`
						})
					}, 2000)
				}
			},
			fail(error) {
				isPurchasing.value = false
				isLoading.value = false
				
				uni.showToast({
					title: '支付失败',
					icon: 'error',
					duration: 2000
				})
				setTimeout(() => {
					uni.navigateTo({
						url:`/pages/ECommerce/pages/order-list?tradeStatus=10`
					})
				}, 2000)
			}
		})
	} catch (error) {
		isPurchasing.value = false
		isLoading.value = false
		
		console.error('购票失败:', error)
			uni.showToast({
				title: '购票失败，请重试',
				icon: 'error',
				duration: 2000
			})
		}
	}

// 分享给朋友
onShareAppMessage(() => {
	return createShareInfo({
		subtitle: ticketDetail.value.name,
		imageUrl: ticketDetail.value.posterUrl,
		params: { id: ticketDetail.value.id }
	})
})

// 分享到朋友圈
onShareTimeline(() => {
	return createShareInfo({
		subtitle: ticketDetail.value.name,
		imageUrl: ticketDetail.value.posterUrl,
		params: { id: ticketDetail.value.id }
	})
})
</script>

<style scoped>
.content {
	background-color: white;
	padding-bottom: 160rpx; /* 为底部操作栏留出空间 */
}

.ticket-poster {
	width: 100%;
	position: relative;
	overflow: hidden;
}

.poster-image {
	width: 100%;
}

.ticket-info-section {
	padding: 30rpx;
	background-color: #fff;
	margin-top: 20rpx;
	margin-bottom: 20rpx;
}

.ticket-name {
	font-size: 36rpx;
	font-weight: bold;
	color: #333;
	display: block;
	margin-bottom: 20rpx;
	line-height: 1.4;
}

.price-section {
	display: flex;
	align-items: center;
	gap: 15rpx;
	margin-bottom: 30rpx;
}

.original-price {
	font-size: 28rpx;
	color: #999;
	text-decoration: line-through;
}

.current-price {
	font-size: 36rpx;
	font-weight: bold;
	color: #d4c5a0;
}

.description-section {
	margin-top: 30rpx;
}

.section-title {
	font-size: 32rpx;
	font-weight: bold;
	color: #333;
	display: block;
	margin-bottom: 15rpx;
}

.description-text {
	font-size: 28rpx;
	line-height: 1.6;
	color: #666;
	display: block;
}

/* 底部操作栏 */
.bottom-action-bar {
	position: fixed;
	bottom: 0;
	left: 0;
	right: 0;
	background: linear-gradient(135deg, #2c2c2c 0%, #3a3a3a 100%);
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 30rpx 30rpx env(safe-area-inset-bottom);
	box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.1);
	z-index: 1000;
}

.quantity-section {
	display: flex;
	align-items: center;
	gap: 20rpx;
}

.quantity-label {
	font-size: 28rpx;
	color: #ff8c00;
}

.quantity-control {
	display: flex;
	align-items: center;
	gap: 20rpx;
}

.quantity-btn {
	width: 50rpx;
	height: 50rpx;
	border-radius: 50%;
	background-color: rgba(255, 140, 0, 0.2);
	border: 2rpx solid #ff8c00;
	display: flex;
	align-items: center;
	justify-content: center;
	transition: all 0.3s ease;
}

.quantity-btn:active {
	background-color: rgba(255, 140, 0, 0.4);
	transform: scale(0.95);
}

.btn-text {
	font-weight: bold;
	color: #ff8c00;
	font-size: 24rpx;
}

.quantity-text {
	font-size: 28rpx;
	font-weight: bold;
	color: #ff8c00;
	min-width: 40rpx;
	text-align: center;
}

.buy-section {
	display: flex;
	align-items: center;
	gap: 30rpx;
}

.total-price {
	display: flex;
	flex-direction: column;
	align-items: flex-end;
}

.price-label {
	font-size: 24rpx;
	color: #ff6b00;
	margin-bottom: 5rpx;
}

.price-value {
	font-size: 32rpx;
	font-weight: bold;
	color: #ff8c00;
}

.buy-button {
	width: 200rpx;
	height: 70rpx;
	background: linear-gradient(135deg, #ff8c00 0%, #ff6b00 100%);
	border-radius: 35rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	box-shadow: 0 4rpx 15rpx rgba(255, 140, 0, 0.3);
	transition: all 0.3s ease;
}

.buy-button:active {
	transform: scale(0.98);
}

.buy-button.loading {
	opacity: 0.7;
	pointer-events: none;
	background: linear-gradient(135deg, #ff6b00 0%, #e55a00 100%);
}

.buy-text {
	font-size: 28rpx;
	font-weight: bold;
	color: #ffffff;
}

/* 全屏loading遮罩样式 */
.loading-overlay {
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	background-color: rgba(0, 0, 0, 0.7);
	display: flex;
	align-items: center;
	justify-content: center;
	z-index: 9999;
}

.loading-content {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	width: 200rpx;
	height: 200rpx;
	padding: 40rpx;
	background-color: rgba(255, 255, 255, 0.95);
	border-radius: 20rpx;
	box-shadow: 0 8rpx 30rpx rgba(0, 0, 0, 0.3);
}

.loading-spinner {
	width: 80rpx;
	height: 80rpx;
	border: 6rpx solid #f3f3f3;
	border-top: 6rpx solid #d4c5a0;
	border-radius: 50%;
	animation: spin 1s linear infinite;
	margin-bottom: 30rpx;
}

@keyframes spin {
	0% { transform: rotate(0deg); }
	100% { transform: rotate(360deg); }
}

.loading-text {
	font-size: 28rpx;
	color: #333;
	font-weight: 500;
	text-align: center;
}
</style>
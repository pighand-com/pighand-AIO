<template>
	<view class="content">
		<custom-navigation-bar :back="true" />
		
		<!-- 主题海报 -->
		<view class="theme-poster" v-if="themeDetail.posterUrl">
			<image :src="themeDetail.posterUrl" class="poster-image" mode="widthFix"></image>
			<!-- 状态栏渐变遮罩 -->
			<status-bar-gradient />
		</view>
		
		<!-- 主题内容 -->
		<view class="theme-content">
			<text class="theme-title" v-if="themeDetail.title">{{ themeDetail.title }}</text>
			<rich-text class="theme-description" v-if="themeDetail.pictureDescription" :nodes="themeDetail.pictureDescription"></rich-text>
		</view>
		
		<!-- 底部操作栏 -->
		<view class="bottom-action-bar">
			<!-- 咨询客服 - 只有当serviceQrUrl有值时才显示 -->
			<view class="customer-service" @click="contactService" v-if="themeDetail.serviceQrUrl">
				<image src="/static/icon-customer-service.png" class="service-icon"></image>
				<text class="service-text">咨询客服</text>
			</view>
			
			<!-- 购买按钮 -->
			<user-check :item="['login', 'phone']">
				<view class="buy-button" @click="showTicketModal">
					<text class="buy-text">即刻购票</text>
				</view>
			</user-check>
		</view>
		
		<!-- 购票弹窗 -->
		<view class="ticket-modal" v-if="isModalVisible" @click="hideModal">
			<view class="modal-content" @click.stop>
				<!-- 票务选择 -->
				<view class="ticket-selection-container">
					<view class="ticket-selection" @scroll="onTicketScroll" :scroll-y="true">
						<view class="ticket-item" 
							  v-for="(ticket, index) in ticketList" 
							  :key="ticket.id"
							  :class="{ 'selected': selectedTicketIndex === index }"
							  @click="selectTicket(index)">
							<view class="ticket-info">
								<text class="ticket-name">{{ ticket.name }}</text>
								<text class="ticket-price">¥ {{ ticket.currentPrice ? new Decimal(ticket.currentPrice).div(100).toNumber() : 0 }}</text>
							</view>
							<view class="ticket-desc-container">
								<text class="ticket-desc" :class="{ 'expanded': expandedTickets[index] }">
									{{ getDisplayText(ticket.details, index) }}
								</text>
								<text class="expand-btn" 
									  v-if="isTextTruncated(ticket.details)"
									  @click.stop="toggleExpand(index)">
									{{ expandedTickets[index] ? '收起' : '展开' }}
								</text>
							</view>
						</view>
					</view>
					<!-- 滚动提示 -->
					<view class="scroll-indicator" v-if="showScrollIndicator">
						<text class="scroll-text">向下滑动查看更多票种</text>
						<view class="scroll-arrow">↓</view>
					</view>
					<!-- 底部渐变遮罩 -->
					<view class="scroll-gradient" v-if="canScrollDown"></view>
				</view>
				
				<!-- 购买数量 -->
				<view class="quantity-section">
					<text class="section-title">购买票数</text>
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
				
				<!-- 底部操作 -->
				<view class="modal-footer">
					<view class="total-price">
						<text class="price-label">合计：</text>
						<text class="price-value">¥ {{ totalPrice }}</text>
					</view>
					<view class="purchase-btn" :class="{ 'loading': isPurchasing }" @click="purchaseTicket">
						<text class="purchase-text" v-if="!isPurchasing">即刻购票</text>
						<text class="purchase-text" v-else>购票中...</text>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 客服二维码弹窗 -->
		<view class="qr-modal" v-if="isQrModalVisible" @click="hideQrModal">
			<view class="qr-modal-content" @click.stop>
				<view class="qr-image-container">
					<image :src="themeDetail.serviceQrUrl" mode="aspectFit" class="qr-image" show-menu-by-longpress></image>
				</view>
				<text class="qr-tip">长按保存二维码，微信扫码联系客服</text>
			</view>
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
import { onLoad } from '@dcloudio/uni-app'
import { ref, computed, watch } from 'vue'
import Decimal from 'decimal.js';
import { theme as themeAPI, ticket as ticketAPI, order as orderAPI } from '@/api'
import { getFromSalesId } from '@/common/storage'

// 主题详情数据
const themeDetail = ref({})

// 弹窗相关
const isModalVisible = ref(false)
const isQrModalVisible = ref(false) // 新增：二维码弹窗显示状态
const ticketList = ref([])
const selectedTicketIndex = ref(0)
const quantity = ref(1)
const totalPrice = ref(0)
const isPurchasing = ref(false) // 购票loading状态
const isLoading = ref(false) // 全屏loading状态
const showScrollIndicator = ref(false) // 滚动提示显示状态
const canScrollDown = ref(false) // 是否可以向下滚动
const expandedTickets = ref({}) // 票务详情展开状态
const MAX_DESC_LENGTH = 50 // 描述文本最大显示长度

// 监听数量变化
watch([quantity, selectedTicketIndex], () => {
	// 当数量或选择的票种变化时，重新计算价格
	calculatePrice()
})

onLoad((options) => {
	const { id } = options
	if (id) {
		getThemeDetail(id)
	}
})

// 获取主题详情
const getThemeDetail = async (id) => {
	try {
		const res = await themeAPI.find(id)
		themeDetail.value = res
	} catch (error) {
		console.error('获取主题详情失败:', error)
	}
}

// 获取票务列表
const getTicketList = async () => {
	const res = await ticketAPI.query({ themeId: themeDetail.value.id })
	ticketList.value = res.records || []
	selectedTicketIndex.value = 0
	// 获取票务列表后立即计算价格
	if (ticketList.value.length > 0) {
		calculatePrice()
		// 检查是否需要显示滚动提示
		checkScrollIndicator()
	}
}

// 检查是否需要显示滚动提示
const checkScrollIndicator = () => {
	// 如果票务列表超过3个，显示滚动提示
	if (ticketList.value.length > 3) {
		showScrollIndicator.value = true
		canScrollDown.value = true
		// 3秒后自动隐藏提示
		setTimeout(() => {
			showScrollIndicator.value = false
		}, 3000)
	}
}

// 处理票务列表滚动事件
const onTicketScroll = (e) => {
	const { scrollTop, scrollHeight, clientHeight } = e.detail
	// 隐藏滚动提示
	if (showScrollIndicator.value) {
		showScrollIndicator.value = false
	}
	// 判断是否可以继续向下滚动
	canScrollDown.value = scrollTop + clientHeight < scrollHeight - 10
}

// 判断文本是否需要截断
const isTextTruncated = (text) => {
	return text && text.length > MAX_DESC_LENGTH
}

// 获取显示的文本内容
const getDisplayText = (text, index) => {
	if (!text) return ''
	if (!isTextTruncated(text)) return text
	if (expandedTickets.value[index]) return text
	return text.substring(0, MAX_DESC_LENGTH) + '...'
}

// 切换文本展开/收起状态
const toggleExpand = (index) => {
	expandedTickets.value[index] = !expandedTickets.value[index]
}

// 咨询客服 - 修改为显示二维码
const contactService = () => {
	if (themeDetail.value.serviceQrUrl) {
		isQrModalVisible.value = true
	} else {
		uni.showToast({
			title: '客服暂不可用',
			icon: 'none'
		})
	}
}

// 新增：隐藏二维码弹窗
const hideQrModal = () => {
	isQrModalVisible.value = false
}

// 显示购票弹窗
const showTicketModal = () => {
	isModalVisible.value = true
	getTicketList()
}

// 隐藏弹窗
const hideModal = () => {
	isModalVisible.value = false
	// 隐藏弹窗时重置所有展开状态
	expandedTickets.value = {}
}

// 选择票务
const selectTicket = (index) => {
	selectedTicketIndex.value = index
	// 先收起所有已展开的票务详情
	expandedTickets.value = {}
	// 如果票务详情需要截断，自动展开当前选中的票务
	const ticket = ticketList.value[index]
	if (ticket && isTextTruncated(ticket.details)) {
		expandedTickets.value[index] = true
	}
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

// 计算价格（统一处理价格计算逻辑）
const calculatePrice = async () => {
	// 基础价格计算
	if (ticketList.value.length === 0) {
		totalPrice.value = 0
		return
	}
	const selectedTicket = ticketList.value[selectedTicketIndex.value]
	if (!selectedTicket) {
		totalPrice.value = 0
		return
	}
	
	// 构建价格计算参数
	const params = {
		ticketId: selectedTicket.id,
		quantity: quantity.value
	}
	
	// TODO: 调用后端接口计算实时价格
	// 可以在这里添加后端接口调用，例如：
	// try {
	//   const res = await priceAPI.calculate(params)
	//   totalPrice.value = res.totalPrice
	//   return
	// } catch (error) {
	//   console.error('计算价格失败:', error)
	//   // 接口调用失败时使用本地计算
	// }
	
	// 本地价格计算（可作为接口调用失败的备选方案）
	const price = selectedTicket?.currentPrice ? new Decimal(selectedTicket.currentPrice).div(100) : new Decimal(0)
	const calculatedPrice = price.mul(quantity.value).toNumber()
	
	// 更新totalPrice的值
	totalPrice.value = calculatedPrice
	
	// 调试日志
	console.log('计算价格:', {
		...params,
		totalPrice: calculatedPrice
	})
}

// 购买票务
const purchaseTicket = async () => {
	if (isPurchasing.value || isLoading.value) return // 防止重复点击
	
	isPurchasing.value = true
	isLoading.value = true // 显示全屏loading
	
	try {
		const selectedTicket = ticketList.value[selectedTicketIndex.value]

		// 获取FromSalesId作为salespersonId
		const fromSalesId = getFromSalesId()
		
		const orderData = {
			outTradePlatform: 'wechat_applet',
			amountPaid: new Decimal(totalPrice.value).mul(100).toNumber(),
			orderSku: [{
				ticketId: selectedTicket.id,
				quantity: quantity.value,
			}]
		}
		
		// 如果有FromSalesId，则添加salespersonId
		if (fromSalesId) {
			orderData.salespersonId = fromSalesId
		}
		
		const result = await orderAPI.create(orderData);

		const { nonceStr, prepayId, signType, paySign, timeStamp } = result;
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

</script>

<style scoped>
.content {
	background-color: #f5f5f5;
	padding-bottom: 160rpx; /* 为底部操作栏留出空间 */
}

.theme-poster {
	width: 100%;
	height: 720rpx;
	position: relative;
	overflow: hidden;
}

.poster-image {
	width: 100%;
}



.theme-content {
	padding: 30rpx 0;
	background-color: #fff;
	margin-top: 20rpx;
	margin-bottom: 20rpx; /* 增加底部间距 */
}

.theme-title {
	font-size: 36rpx;
	font-weight: bold;
	color: #333;
	display: block;
	margin-bottom: 20rpx;
}

.theme-description {
	font-size: 28rpx;
	line-height: 1.6;
	display: block;
}

/* 底部操作栏 */
.bottom-action-bar {
	position: fixed;
	bottom: 0;
	left: 0;
	right: 0;
	background: linear-gradient(135deg, #4a4a3a 0%, #6b6b4a 100%);
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 30rpx 30rpx env(safe-area-inset-bottom);
	box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.1);
	z-index: 1000;
}

.customer-service {
	display: flex;
	flex-direction: row;
	align-items: center;
	justify-content: center;
	width: 180rpx;
	height: 80rpx;
}

.service-icon {
	width: 64rpx;
	height: 64rpx;
	margin-right: 12rpx;
}

.service-text {
	font-size: 26rpx;
	color: #d4c5a0;
}

.buy-button {
	width: 300rpx;
	height: 80rpx;
	background: linear-gradient(135deg, #d4c5a0 0%, #b8a082 100%);
	border-radius: 40rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	box-shadow: 0 4rpx 15rpx rgba(212, 197, 160, 0.3);
}

.buy-text {
	font-size: 32rpx;
	font-weight: bold;
	color: #4a4a3a;
}

/* 购票弹窗 */
.ticket-modal {
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	background-color: rgba(0, 0, 0, 0.6);
	display: flex;
	align-items: flex-end;
	z-index: 2000;
}

.modal-content {
	width: 100%;
	max-height: 80vh;
	background: linear-gradient(135deg, #4a4a3a 0%, #6b6b4a 100%);
	border-radius: 40rpx 40rpx 0 0;
	padding: 40rpx 30rpx env(safe-area-inset-bottom);
	color: #d4c5a0;
	display: flex;
	flex-direction: column;
}

.modal-title {
	font-size: 36rpx;
	font-weight: bold;
	color: #d4c5a0;
}

/* 票务选择容器 */
.ticket-selection-container {
	flex: 1;
	position: relative;
	margin-bottom: 40rpx;
}

/* 票务选择 */
.ticket-selection {
	height: 100%;
	max-height: 50vh;
	overflow-y: auto;
	padding-right: 10rpx;
}

.ticket-item {
	background-color: rgba(212, 197, 160, 0.1);
	border: 2rpx solid transparent;
	border-radius: 20rpx;
	padding: 30rpx;
	margin-bottom: 20rpx;
	transition: all 0.3s ease;
}

.ticket-item.selected {
	background-color: rgba(212, 197, 160, 0.2);
	border-color: #d4c5a0;
	box-shadow: 0 4rpx 15rpx rgba(212, 197, 160, 0.2);
}

.ticket-info {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 15rpx;
}

.ticket-name {
	font-size: 32rpx;
	font-weight: bold;
	color: #d4c5a0;
}

.ticket-price {
	font-size: 36rpx;
	font-weight: bold;
	color: #d4c5a0;
}

.ticket-desc-container {
	display: flex;
	flex-direction: column;
	gap: 8rpx;
}

.ticket-desc {
	font-size: 24rpx;
	color: #b8a082;
	line-height: 1.4;
	word-break: break-all;
	transition: all 0.3s ease;
}

.ticket-desc.expanded {
	max-height: none;
}

.expand-btn {
	font-size: 22rpx;
	color: #d4c5a0;
	text-decoration: underline;
	align-self: flex-start;
	padding: 4rpx 0;
	transition: all 0.3s ease;
}

.expand-btn:active {
	color: #b8a082;
	transform: scale(0.95);
}

/* 购买数量 */
.quantity-section {
	margin-bottom: 40rpx;
	flex-shrink: 0;
}

.section-title {
	font-size: 28rpx;
	color: #d4c5a0;
	margin-bottom: 20rpx;
	display: block;
}

.quantity-control {
	display: flex;
	align-items: center;
	justify-content: center;
	gap: 40rpx;
}

.quantity-btn {
	width: 60rpx;
	height: 60rpx;
	border-radius: 50%;
	background-color: rgba(212, 197, 160, 0.2);
	border: 2rpx solid #d4c5a0;
	display: flex;
	align-items: center;
	justify-content: center;
	transition: all 0.3s ease;
}

.quantity-btn:active {
	background-color: rgba(212, 197, 160, 0.4);
	transform: scale(0.95);
}

.btn-text {
	font-weight: bold;
	color: #d4c5a0;
}

.quantity-text {
	font-size: 36rpx;
	font-weight: bold;
	color: #d4c5a0;
	min-width: 60rpx;
	text-align: center;
}

/* 底部操作 */
.modal-footer {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding-top: 30rpx;
	border-top: 2rpx solid rgba(212, 197, 160, 0.2);
	flex-shrink: 0;
}

.total-price {
	display: flex;
	flex-direction: column;
	align-items: flex-start;
}

.price-label {
	font-size: 24rpx;
	color: #b8a082;
	margin-bottom: 8rpx;
}

.price-value {
	font-size: 36rpx;
	font-weight: bold;
	color: #d4c5a0;
}

.purchase-btn {
	width: 300rpx;
	height: 80rpx;
	background: linear-gradient(135deg, #d4c5a0 0%, #b8a082 100%);
	border-radius: 40rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	box-shadow: 0 4rpx 15rpx rgba(212, 197, 160, 0.3);
	transition: all 0.3s ease;
}

.purchase-btn:active {
	transform: scale(0.98);
}

.purchase-btn.loading {
	opacity: 0.7;
	pointer-events: none;
	background: linear-gradient(135deg, #b8a082 0%, #a08f72 100%);
}

.purchase-text {
	font-size: 28rpx;
	font-weight: bold;
	color: #4a4a3a;
}

/* 客服二维码弹窗样式 */
.qr-modal {
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	background-color: rgba(0, 0, 0, 0.6);
	display: flex;
	align-items: center;
	justify-content: center;
	z-index: 2000;
}

.qr-modal-content {
	width: 550rpx;
	padding: 40rpx;
	position: relative;
	display: flex;
	flex-direction: column;
	align-items: center;
}

.qr-image-container {
	display: flex;
	justify-content: center;
	margin-bottom: 30rpx;
}

.qr-image {
	width: 450rpx;
	height: 450rpx;
	border-radius: 20rpx;
}

.qr-tip {
	font-size: 28rpx;
	color: #ffffff;
	font-weight: bold;
	text-align: center;
	display: block;
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

/* 滚动提示样式 */
.scroll-indicator {
	position: absolute;
	bottom: 20rpx;
	left: 50%;
	transform: translateX(-50%);
	display: flex;
	flex-direction: column;
	align-items: center;
	background: rgba(0, 0, 0, 0.6);
	border-radius: 30rpx;
	padding: 15rpx 25rpx;
	z-index: 10;
	animation: fadeInOut 3s ease-in-out;
}

.scroll-text {
	font-size: 22rpx;
	color: #ffffff;
	margin-bottom: 8rpx;
}

.scroll-arrow {
	font-size: 24rpx;
	color: #ffffff;
	animation: bounce 1.5s infinite;
}

/* 底部渐变遮罩 */
.scroll-gradient {
	position: absolute;
	bottom: 0;
	left: 0;
	right: 0;
	height: 60rpx;
	background: linear-gradient(to top, rgba(74, 74, 58, 0.8) 0%, transparent 100%);
	pointer-events: none;
	z-index: 5;
}

/* 动画效果 */
@keyframes fadeInOut {
	0% { opacity: 0; transform: translateX(-50%) translateY(10rpx); }
	20% { opacity: 1; transform: translateX(-50%) translateY(0); }
	80% { opacity: 1; transform: translateX(-50%) translateY(0); }
	100% { opacity: 0; transform: translateX(-50%) translateY(-10rpx); }
}

@keyframes bounce {
	0%, 20%, 50%, 80%, 100% { transform: translateY(0); }
	40% { transform: translateY(-8rpx); }
	60% { transform: translateY(-4rpx); }
}
</style>

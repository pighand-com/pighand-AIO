<template>
	<view class="page">
		<custom-navigation-bar :back="true" position="flex" title="核销" />
		
		<view class="content">
			<!-- 当前扫码票 -->
			<view class="current-ticket-section" v-if="currentTicket">
				<view class="section-title">
					<text class="title-text">当前扫码票</text>
				</view>
				<view class="ticket-card">
					<view class="ticket-header">
						<text class="ticket-name">{{ currentTicket.name }}</text>
						<view class="ticket-controls">
							<view class="control-btn" @click="decreaseCurrentTicket">
								<text class="control-text">-</text>
							</view>
							<text class="ticket-count">{{ currentTicketCount }}</text>
							<view class="control-btn" @click="increaseCurrentTicket">
								<text class="control-text">+</text>
							</view>
						</view>
					</view>
					
					<view class="ticket-ids-text">
						<view class="ticket-id-row">
							<text class="ticket-label">票号（{{ currentTicket.ids.length }}张）</text>
							<text 
								:class="['ticket-number', { 'highlighted': 0 < currentTicketCount }]"
							>{{ currentTicket.ids[0] }}</text>
						</view>
						<view 
							v-for="(id, index) in currentTicket.ids.slice(1)" 
							:key="id"
							class="ticket-id-additional"
						>
							<text class="ticket-label"></text>
							<text 
								:class="['ticket-number', { 'highlighted': index + 1 < currentTicketCount }]"
							>{{ id }}</text>
						</view>
					</view>
				</view>
			</view>
			
			<!-- 其他可用票种 -->
			<view class="current-ticket-section" v-if="otherTickets.length > 0">
				<view class="section-title">
					<text class="title-text">其他可用票种</text>
				</view>
				<view 
					v-for="(ticket, ticketIndex) in otherTickets" 
					:key="ticketIndex"
					class="ticket-card other-ticket-card"
				>
					<view class="ticket-header">
						<text class="ticket-name">{{ ticket.name }}</text>
						<view class="ticket-controls">
							<view class="control-btn" @click="decreaseOtherTicket(ticketIndex)">
								<text class="control-text">-</text>
							</view>
							<text class="ticket-count">{{ otherTicketCounts[ticketIndex] || 0 }}</text>
							<view class="control-btn" @click="increaseOtherTicket(ticketIndex)">
								<text class="control-text">+</text>
							</view>
						</view>
					</view>
					
					<view class="ticket-ids-text">
						<view class="ticket-id-row" v-if="ticket.ids.length > 0">
							<text class="ticket-label">票号（{{ ticket.ids.length }}张）</text>
							<text 
								:class="['ticket-number', { 'highlighted': 0 < (otherTicketCounts[ticketIndex] || 0) }]"
							>{{ ticket.ids[0] }}</text>
						</view>
						<view 
							v-for="(id, index) in ticket.ids.slice(1)" 
							:key="id"
							class="ticket-id-additional"
						>
							<text class="ticket-label"></text>
							<text 
								:class="['ticket-number', { 'highlighted': index + 1 < (otherTicketCounts[ticketIndex] || 0) }]"
							>{{ id }}</text>
						</view>
					</view>
				</view>
			</view>
			
			<!-- 确认核销按钮 -->
			<view class="confirm-section">
				<view class="confirm-btn" @click="confirmVerification">
					<text class="confirm-text">确认核销</text>
				</view>
			</view>
		</view>
	</view>
</template>

<script setup>
import { onLoad } from '@dcloudio/uni-app'
import { ref } from 'vue'
import { ticket as ticketAPI } from '@/api'

const ticketData = ref([])
const currentTicket = ref(null)
const otherTickets = ref([])
const currentTicketCount = ref(1)
const otherTicketCounts = ref({})

// 页面加载时获取传递的数据
onLoad((options) => {
	if (options.data) {
		try {
			const data = JSON.parse(decodeURIComponent(options.data))
			ticketData.value = data
			
			// 设置当前票（第一个）
			if (data.length > 0) {
				currentTicket.value = data[0]
				currentTicketCount.value = 1
			}
			
			// 设置其他票
			if (data.length > 1) {
				otherTickets.value = data.slice(1)
				// 初始化其他票的计数
				data.slice(1).forEach((ticket, index) => {
					otherTicketCounts.value[index] = 0
				})
			}
		} catch (error) {
			console.error('解析票务数据失败:', error)
			uni.showToast({
				title: '数据解析失败',
				icon: 'none'
			})
		}
	}
})

// 增加当前票数量
const increaseCurrentTicket = () => {
	if (currentTicket.value && currentTicketCount.value < currentTicket.value.ids.length) {
		currentTicketCount.value++
	}
}

// 减少当前票数量
const decreaseCurrentTicket = () => {
	if (currentTicketCount.value > 1) {
		currentTicketCount.value--
	}
}

// 增加其他票数量
const increaseOtherTicket = (index) => {
	const ticket = otherTickets.value[index]
	if (ticket && (otherTicketCounts.value[index] || 0) < ticket.ids.length) {
		otherTicketCounts.value[index] = (otherTicketCounts.value[index] || 0) + 1
	}
}

// 减少其他票数量
const decreaseOtherTicket = (index) => {
	if ((otherTicketCounts.value[index] || 0) > 0) {
		otherTicketCounts.value[index] = (otherTicketCounts.value[index] || 0) - 1
	}
}

// 确认核销
const confirmVerification = async () => {
	uni.showLoading({
		title: '核销中...'
	})
	
	// 收集需要核销的票ID
	const verificationList = []
	
	// 添加当前票的选中ID
	if (currentTicket.value && currentTicketCount.value > 0) {
		for (let i = 0; i < currentTicketCount.value; i++) {
			verificationList.push({ id: currentTicket.value.ids[i], validationCount: 1 })
		}
	}
	
	// 添加其他票的选中ID
	otherTickets.value.forEach((ticket, index) => {
		const count = otherTicketCounts.value[index] || 0
		for (let i = 0; i < count; i++) {
			verificationList.push({ id: ticket.ids[i], validationCount: 1 })
		}
	})
	
	if (verificationList.length === 0) {
		uni.hideLoading()
		uni.showToast({
			title: '请选择要核销的票',
			icon: 'none'
		})
		return
	}
	
	// 调用核销API
	await ticketAPI.validation(verificationList)
	
	uni.hideLoading()
	uni.showToast({
		title: '核销成功',
		icon: 'success'
	})
	
	// 延迟返回上一页
	setTimeout(() => {
		uni.navigateBack()
	}, 1500)
	
}
</script>

<style scoped>
.page {
	background-color: #f5f5f5;
	min-height: 100vh;
}

.content {
	padding: 32rpx;
}

.current-ticket-section {
	margin-bottom: 48rpx;
	background: #fff;
	border-radius: 24rpx;
	box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
	padding: 32rpx;
}

.section-title {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 0 8rpx;
}

.title-text {
	font-size: 36rpx;
	font-weight: 600;
	color: #333;
}

.ticket-card {
	padding: 24rpx;
	background-color: aliceblue;
	border-radius: 12rpx;
	margin-top: 24rpx;
}

.other-ticket-card {
	background-color: #f7f4f4;
}

.ticket-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 24rpx;
	padding-bottom: 24rpx;
	border-bottom: 2rpx solid #f0f0f0;
}

.ticket-name {
	font-size: 32rpx;
	font-weight: 600;
	color: #333;
}

.ticket-controls {
	display: flex;
	align-items: center;
	gap: 24rpx;
}

.control-btn {
	width: 60rpx;
	height: 60rpx;
	border-radius: 50%;
	background: linear-gradient(135deg, #ff8c00 0%, #ff6b00 100%);
	display: flex;
	align-items: center;
	justify-content: center;
	transition: all 0.3s ease;
	box-shadow: 0 4rpx 15rpx rgba(255, 140, 0, 0.3);
}

.control-btn:active {
	transform: scale(0.95);
	background: linear-gradient(135deg, #ff6b00 0%, #ff5500 100%);
}

.control-text {
	font-size: 32rpx;
	color: #fff;
	font-weight: 600;
}

.ticket-count {
	font-size: 32rpx;
	font-weight: 600;
	color: #333;
	min-width: 60rpx;
	text-align: center;
}

.ticket-ids-text {
	display: flex;
	flex-direction: column;
	gap: 8rpx;
}

.ticket-id-row {
	display: flex;
	align-items: baseline;
}

.ticket-label {
	font-size: 28rpx;
	color: #666;
	font-weight: 600;
	width: 160rpx;
	flex-shrink: 0;
}

.ticket-number {
	font-size: 28rpx;
	color: #666;
	font-weight: 600;
	transition: all 0.3s ease;
}

.ticket-number.highlighted {
	color: #ff8c00;
	font-weight: 600;
}

.ticket-id-additional {
	display: flex;
}

.confirm-section {
	padding: 32rpx 0;
}

.confirm-btn {
	width: 100%;
	height: 96rpx;
	background: linear-gradient(135deg, #ff8c00 0%, #ff6b00 100%);
	border-radius: 24rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	box-shadow: 0 8rpx 24rpx rgba(255, 140, 0, 0.3);
	transition: all 0.3s ease;
}

.confirm-btn:active {
	transform: scale(0.98);
	box-shadow: 0 4rpx 12rpx rgba(255, 140, 0, 0.4);
}

.confirm-text {
	font-size: 36rpx;
	color: #fff;
	font-weight: 600;
}
</style>
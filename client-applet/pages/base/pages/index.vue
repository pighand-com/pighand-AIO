<template>
	<view class="content">
		<!-- 橙色渐变背景 -->
		<view class="gradient-bg"></view>

		<custom-navigation-bar position="flex" />
		
		<!-- Banner轮播图 -->
		<view class="banner-container" v-if="bannerList.length > 0">
			<swiper 
				class="banner-swiper" 
				:autoplay="true" 
				:interval="3000" 
				:duration="500"
				:indicator-dots="true"
				indicator-color="rgba(255, 255, 255, 0.5)"
				indicator-active-color="#ffffff"
			>
				<swiper-item v-for="(banner, index) in bannerList" :key="index">
					<image 
						:src="banner.imageUrl || banner.image" 
						class="banner-image" 
						mode="aspectFill"
						@click="handleBannerClick(banner)"
					></image>
				</swiper-item>
			</swiper>
		</view>
		
		<!-- 功能图标区域 -->
		<view class="function-icons">
			<view class="icon-item" @click="showQueuePopup">
				<view class="icon-wrapper">
					<image class="icon" src="../static/icon-queue.png" mode="aspectFit"></image>
				</view>
				<text class="icon-text">排队</text>
			</view>
			<view class="icon-item" @click="goToStore">
				<view class="icon-wrapper">
					<image class="icon" src="../static/icon-theme.png" mode="aspectFit"></image>
				</view>
				<text class="icon-text">主题介绍</text>
			</view>
			<view class="icon-item" @click="showGameTip">
				<view class="icon-wrapper">
					<image class="icon" src="../static/icon-game.png" mode="aspectFit"></image>
				</view>
				<text class="icon-text">游戏</text>
			</view>
		</view>
		
		<!-- 门票展示区域 -->
		<view class="ticket-section">
			<view class="section-title">门票</view>
			<view class="ticket-list">
				<view class="ticket-item" 
					  v-for="ticket in displayTickets" 
					  :key="ticket.id"
					  @click="goToTicketDetail(ticket.id)">
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
					
					<!-- 抢购按钮 -->
					<view class="buy-button">
						<text class="buy-text">立即购买</text>
					</view>
				</view>
			</view>
			<view class="show-all" @click="goToTicketList">
				<text class="show-all-text">显示全部</text>
			</view>
		</view>
		
		<!-- 游戏项目区域 -->
		<view class="game-section">
			<view class="section-title">游戏项目</view>
			<scroll-view class="game-scroll" scroll-x="true" show-scrollbar="false">
				<view class="game-list">
					<view class="game-item" 
						  v-for="theme in themeList" 
						  :key="theme.id">
						<view class="game-cover">
							<image :src="theme.posterUrl" 
								   class="game-image" 
								   mode="aspectFill"></image>
						</view>
						<text class="game-name">{{ theme.themeName || theme.name }}</text>
					</view>
				</view>
			</scroll-view>
		</view>
		
		<!-- 活动区域 -->
		<view class="activity-section" v-if="activityList.length > 0">
			<view class="section-title">活动</view>
			<view class="activity-container">
				<swiper class="activity-swiper" :autoplay="true" :interval="4000" :duration="500">
					<swiper-item v-for="(activity, index) in activityList" :key="index">
						<image 
							:src="activity.imageUrl || activity.image" 
							class="activity-image" 
							mode="aspectFill"
							@click="handleBannerClick(activity)"
						></image>
					</swiper-item>
				</swiper>
			</view>
		</view>
	</view>

	<!-- 排队时间弹出层 -->
	<uni-popup ref="queuePopup" type="center" :mask-click="true">
		<view class="queue-popup">
			<view class="queue-popup-header">
				<text class="queue-popup-title">排队时间</text>
			</view>
			<view class="queue-popup-content">
				<view v-if="themeList.length === 0" class="no-data">
					<text>暂无主题数据</text>
				</view>
				<view v-else class="queue-list">
					<view v-for="(theme, index) in themeList" :key="index" class="queue-item">
						<view class="theme-info">
							<text class="theme-name">{{ theme.themeName || theme.name || '未知主题' }}</text>
							<text class="queue-duration">{{ formatQueueDuration(theme.queueDuration || 0) }}</text>
						</view>
					</view>
				</view>
			</view>
		</view>
	</uni-popup>

	<tab-bar />
</template>

<script setup>
import { ref, computed } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { banner as bannerAPI, theme as themeAPI, ticket as ticketAPI } from '@/api'
import { setFromSalesId } from '@/common/storage'
import Decimal from 'decimal.js'

const bannerList = ref([])
const themeList = ref([])
const ticketList = ref([])
const activityList = ref([])
const queuePopup = ref(null)

// 显示的门票列表（从第3个开始显示2条）
const displayTickets = computed(() => {
	return ticketList.value.slice(2, 4)
})

// 格式化排队时间显示
const formatQueueDuration = (minutes) => {
	if (minutes >= 60) {
		const hours = Math.floor(minutes / 60)
		const remainingMinutes = minutes % 60
		if (remainingMinutes === 0) {
			return `${hours}小时`
		} else {
			return `${hours}小时${remainingMinutes}分钟`
		}
	} else {
		return `${minutes}分钟`
	}
}

// 格式化价格（分转元）
const formatPrice = (price) => {
	if (!price) return '0'
	return new Decimal(price).div(100).toNumber()
}

// 获取banner列表
const getBannerList = async () => {
	try {
		const res = await bannerAPI.query({ group: 'banner' })
		if (res && res.records) {
			bannerList.value = res.records.filter(item => item.group === 'banner')
		}
	} catch (error) {
		console.error('获取banner列表失败:', error)
	}
}

// 获取活动列表
const getActivityList = async () => {
	try {
		const res = await bannerAPI.query({ group: 'activity' })
		if (res && res.records) {
			activityList.value = res.records.filter(item => item.group === 'activity')
		}
	} catch (error) {
		console.error('获取活动列表失败:', error)
	}
}

// 获取门票列表
const getTicketList = async () => {
	try {
		const res = await ticketAPI.query({})
		if (res && res.records) {
			ticketList.value = res.records
		}
	} catch (error) {
		console.error('获取门票列表失败:', error)
	}
}

// 获取主题列表
const getThemeList = async () => {
	try {
		const res = await themeAPI.query()
		if (res && res.records) {
			themeList.value = res.records
		}
	} catch (error) {
		console.error('获取主题列表失败:', error)
	}
}

// 跳转到商店页面
const goToStore = () => {
	uni.navigateTo({
		url: '/pages/base/pages/store?id=1'
	})
}

// 显示游戏提示
const showGameTip = () => {
	uni.showToast({
		title: '即将开放，敬请期待',
		icon: 'none'
	})
}

// 跳转到门票详情
const goToTicketDetail = (ticketId) => {
	uni.navigateTo({
		url: `/pages/ECommerce/pages/ticket-detail?id=${ticketId}`
	})
}

// 跳转到门票列表
const goToTicketList = () => {
	uni.switchTab({
		url: '/pages/ECommerce/pages/ticket-list'
	})
}

// 显示排队时间弹出层
const showQueuePopup = async () => {
	// 重新获取最新的主题数据
	await getThemeList()
	queuePopup.value.open()
}

// 关闭排队时间弹出层
const closeQueuePopup = () => {
	queuePopup.value.close()
}

// Banner点击事件处理
const handleBannerClick = (banner) => {
	if (!banner.redirectionPath) {
		return
	}
	
	const redirectionPath = banner.redirectionPath
	
	// 如果是http开头，打开webview
	if (redirectionPath.startsWith('http')) {
		const title = banner.title || banner.name || '网页浏览'
		uni.navigateTo({
			url: `/pages/base/pages/webview?url=${encodeURIComponent(redirectionPath)}&title=${encodeURIComponent(title)}`
		})
	}
	// 如果是/pages开头，跳转小程序页面
	else if (redirectionPath.startsWith('/pages')) {
		uni.navigateTo({
			url: redirectionPath
		})
	}
	// 其他情况，尝试作为页面路径处理
	else {
		try {
			uni.navigateTo({
				url: redirectionPath
			})
		} catch (error) {
			console.warn('无效的跳转路径:', redirectionPath, error)
		}
	}
}

// 页面加载时获取参数
onLoad((options) => {
	const { scene = ''} = options;
	// 检查是否有sales参数
	const sales = scene && decodeURIComponent(scene).split('=')[1]
	if(sales) {
		setFromSalesId(sales)
	}
	
	// 获取数据
	getBannerList()
	getActivityList()
	getThemeList()
	getTicketList()
})

</script>

<style scoped>
.content {
	background-color: white;
	position: relative;
	min-height: 100vh;
	padding-bottom: calc(100rpx + env(safe-area-inset-bottom));
	overflow-x: hidden;
}

/* 橙色渐变背景 */
.gradient-bg {
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 640rpx;
	background: linear-gradient(180deg, #ff8c00 0%, rgba(255, 140, 0, 0) 100%);
	z-index: 1;
}

/* Banner区域 */
.banner-container {
	width: 100%;
	height: 400rpx;
	position: relative;
	z-index: 2;
	padding: 20rpx 30rpx;
	box-sizing: border-box;
}

.banner-swiper {
	width: 100%;
	height: 100%;
	border-radius: 20rpx;
	overflow: hidden;
}

.banner-image {
	width: 100%;
	height: 100%;
	border-radius: 20rpx;
}

/* 轮播指示点样式 - 通过swiper属性设置 */

/* 功能图标区域 */
.function-icons {
	display: flex;
	justify-content: space-around;
	position: relative;
	z-index: 3;
}

.icon-item {
	display: flex;
	flex-direction: column;
	align-items: center;
	gap: 15rpx;
}

.icon-wrapper {
	width: 100rpx;
	height: 100rpx;
	background-color: #ffffff;
	border-radius: 60rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	box-shadow: 0 8rpx 30rpx rgba(0, 0, 0, 0.1);
	transition: all 0.3s ease;
}

.icon-wrapper:active {
	transform: scale(0.95);
}

.icon {
	font-size: 60rpx;
}

.icon-text {
	font-size: 28rpx;
	color: #333;
	font-weight: 500;
}

/* 通用区域标题 */
.section-title {
	font-size: 36rpx;
	font-weight: bold;
	color: #333;
	padding: 30rpx 30rpx 20rpx;
}

/* 门票展示区域 */
.ticket-section {
	margin-top: 20rpx;
	background-color: #ffffff;
	border-radius: 20rpx 20rpx 0 0;
	position: relative;
	z-index: 3;
}

.ticket-list {
	padding: 0 20rpx;
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
	border: 1rpx solid #f0f0f0;
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

.show-all {
	text-align: center;
}

.show-all-text {
	font-size: 28rpx;
	color: #999999;
	font-weight: 500;
}

/* 游戏项目区域 */
.game-section {
	margin-top: 20rpx;
	background-color: #ffffff;
	border-radius: 20rpx;
	position: relative;
	z-index: 3;
}

.game-scroll {
	padding: 0 20rpx 30rpx;
}

/* 隐藏横向滚动条 */
.game-scroll ::-webkit-scrollbar {
	display: none;
}

.game-scroll {
	-ms-overflow-style: none;
	scrollbar-width: none;
}

.game-list {
	display: flex;
	gap: 20rpx;
	padding-right: 20rpx;
}

.game-item {
	display: flex;
	flex-direction: column;
	align-items: center;
	gap: 15rpx;
	flex-shrink: 0;
}

.game-cover {
	width: 200rpx;
	height: 120rpx;
	border-radius: 12rpx;
	overflow: hidden;
}

.game-image {
	width: 100%;
	height: 100%;
}

.game-name {
	font-size: 26rpx;
	color: #333;
	font-weight: 500;
	text-align: center;
	width: 200rpx;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}

/* 活动区域 */
.activity-section {
	margin-top: 20rpx;
	background-color: #ffffff;
	border-radius: 20rpx;
	position: relative;
	z-index: 3;
	margin-bottom: 20rpx;
}

.activity-container {
	padding: 0 20rpx 30rpx;
}

.activity-swiper {
	width: 100%;
	height: 300rpx;
	border-radius: 20rpx;
	overflow: hidden;
}

.activity-image {
	width: 100%;
	height: 100%;
	border-radius: 80rpx;
}

/* 排队时间弹出层样式 */
.queue-popup {
	width: 600rpx;
	background-color: #ffffff;
	border-radius: 20rpx;
	overflow: hidden;
}

.queue-popup-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 30rpx 40rpx;
	background-color: #f8f9fa;
	border-bottom: 1rpx solid #e9ecef;
}

.queue-popup-title {
	font-size: 36rpx;
	font-weight: bold;
	color: #333333;
}

.close-icon {
	font-size: 40rpx;
	color: #666666;
	line-height: 1;
}

.queue-popup-content {
	padding: 40rpx;
	max-height: 800rpx;
	overflow-y: auto;
}

.no-data {
	text-align: center;
	padding: 80rpx 0;
	color: #999999;
	font-size: 28rpx;
}

.queue-list {
	display: flex;
	flex-direction: column;
	gap: 20rpx;
}

.queue-item {
	padding: 30rpx;
	background-color: #f8f9fa;
	border-radius: 12rpx;
	border: 1rpx solid #e9ecef;
}

.theme-info {
	display: flex;
	justify-content: space-between;
	align-items: center;
}

.theme-name {
	font-size: 32rpx;
	color: #333333;
	font-weight: 500;
	flex: 1;
}

.queue-duration {
	font-size: 28rpx;
	color: #007aff;
	font-weight: bold;
}
</style>

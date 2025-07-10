<template>
	<view class="content">
		<!-- Banner轮播图 -->
		<view class="banner-container" v-if="bannerList.length > 0">
			<swiper class="banner-swiper" :autoplay="true" :interval="3000" :duration="500">
				<swiper-item v-for="(banner, index) in bannerList" :key="index">
					<image :src="banner.imageUrl || banner.image" class="banner-image" mode="aspectFill"></image>
				</swiper-item>
			</swiper>
		</view>
		
		<!-- 主题购买按钮 -->
		<view class="theme-container">
			<custom-button 
				text="立即购买" 
				width="360rpx" 
				fontSize="48rpx"
				@click="handleBuyNow" 
			/>
		</view>
		
		<!-- 查看排队时间按钮 -->
		<view class="queue-button-container">
			<custom-button 
				text="查看排队时间" 
				width="280rpx" 
				fontSize="32rpx"
				@click="showQueuePopup" 
			/>
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
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { banner as bannerAPI, theme as themeAPI } from '@/api'
import { setFromSalesId } from '@/common/storage'

const bannerList = ref([])
const themeList = ref([])
const queuePopup = ref(null)

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

// 获取banner列表
const getBannerList = async () => {
	try {
		const res = await bannerAPI.query()
		if (res && res.records) {
			bannerList.value = res.records
		}
	} catch (error) {
		console.error('获取banner列表失败:', error)
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

// 立即购买按钮点击事件
const handleBuyNow = () => {
	const themeId = themeList.value[0].id
	uni.navigateTo({
		url: `/pages/ECommerce/pages/theme?id=${themeId}`
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
	getThemeList()
})

</script>

<style>
.content {
	padding: 0;
	position: relative;
	height: calc(100vh - 100rpx);
}

.banner-container {
	width: 100%;
	height: calc(100vh - 100rpx);
	position: absolute;
	top: 0;
	left: 0;
	z-index: 1;
}

.banner-swiper {
	width: 100%;
	height: 100%;
}

.banner-image {
	width: 100%;
	height: 100%;
	border-radius: 0;
}

/* 轮播指示点样式 */
.banner-swiper ::v-deep .uni-swiper-dot {
	background-color: rgba(255, 255, 255, 0.5);
}

.banner-swiper ::v-deep .uni-swiper-dot-active {
	background-color: #ffffff;
}

/* 主题购买按钮样式 */
.theme-container {
	position: fixed;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
	z-index: 10;
	text-align: center;
}

/* 按钮样式已移至组件内部 */

/* 查看排队时间按钮样式 */
.queue-button-container {
	position: fixed;
	top: calc(50% + 120rpx);
	left: 50%;
	transform: translateX(-50%);
	z-index: 10;
	text-align: center;
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

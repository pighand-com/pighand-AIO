<template>
	<view class="content">
		<!-- 橙色渐变背景 -->
		<view class="gradient-bg"></view>

		<!-- 自定义导航栏 -->
		<custom-navigation-bar :back="true" position="inherit" />

		<!-- 活动状态区域 -->
		<view class="activity-status-section">
			<view class="activity-status-card">
				<view v-if="isParticipated" class="activity-status">
				<view v-if="isAllLocationsCompleted" class="status-title">打卡已完成</view>
				<view v-else class="status-title">打卡进行中</view>
				<view v-if="!isAllLocationsCompleted && countdown > 0" class="countdown-time">{{ countdownDisplay }}</view>
				<view v-else-if="!isAllLocationsCompleted" class="status-ended">打卡已结束</view>
			</view>
				<view v-else class="activity-status">
					<view class="status-title">请前台扫码参加活动</view>
				</view>
			</view>
		</view>



		<!-- 打卡地点列表 -->
		<view class="locations-section">
			<view class="section-title">打卡地点</view>
			<view class="locations-grid">
				<view 
					v-for="location in allLocations" 
					:key="location.id"
					class="location-item"
					:class="getLocationStatusClass(location)"
				>
					<view class="location-icon-container" :class="getLocationIconClass(location.id)">
						<image class="location-icon" src="/static/icon-location.svg" mode="aspectFit"></image>
					</view>
					<view class="location-text">{{ location.name }}</view>
				</view>
			</view>
		</view>

		<!-- 打卡状态查询 -->
		<view class="status-section" v-if="checkInDetail.id">
			<view class="section-title">今日打卡情况</view>
			<view class="status-list">
				<view class="status-item" v-for="record in todayRecords" :key="record.id">
					<view class="user-info">
						<text class="user-name">{{ record.userName || '匿名用户' }}</text>
						<text class="check-time">{{ formatDateTime(record.createdAt) }}</text>
					</view>
				</view>
				<view class="no-records" v-if="todayRecords.length === 0">
					<text class="no-records-text">今日暂无打卡记录</text>
				</view>
			</view>
		</view>

		<!-- 用户二维码区域 -->
		<view class="qrcode-section" v-if="shouldShowQrCode()">
			<view class="qrcode-container">
				<view class="section-title">我的打卡二维码</view>
				<canvas 
					canvas-id="qrcode" 
					class="qrcode-canvas"
					:style="{ width: qrcodeSize + 'px', height: qrcodeSize + 'px' }"
				></canvas>
				<text class="qrcode-tip">扫码完成打卡</text>
			</view>
		</view>

		<!-- 登录弹窗 -->
		<user-check v-if="showLoginModal" :item="['login']">
			<view></view>
		</user-check>
	</view>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, watch } from 'vue'
import { onLoad, onUnload } from '@dcloudio/uni-app'
import { checkInLocation as checkInLocationApi } from '@/api'
import { getToken, getUserInfo } from '@/common/storage'
import login from '@/common/login'
import UQRCode from 'uqrcodejs' // 使用UQRCode库生成二维码

// 响应式数据
const checkInDetail = ref({})
const userStatus = ref({})
const todayRecords = ref([])
const allLocations = ref([]) // 所有打卡地点
const loading = ref(false)
const countdown = ref(0)
const countdownTimer = ref(null)
const refreshTimer = ref(null)
const qrcodeSize = ref(200)
const isParticipated = ref(false)
const endTime = ref(null)
const showLoginModal = ref(false) // 控制登录弹窗显示

// 监听登录状态变化
watch(() => getToken(), async (newToken, oldToken) => {
	// 当从无token变为有token时，说明登录成功
	if (!oldToken && newToken && showLoginModal.value) {
		// 关闭登录弹窗
		showLoginModal.value = false
		// 登录成功后参加活动
		await joinActivity()
		// 重新加载页面数据
		await loadPageData()
		// 开始自动刷新
		startAutoRefresh()
	}
})

// 监听storage变化事件
uni.$on('storage-changed', async () => {
	const token = getToken()
	if (token && showLoginModal.value) {
		// 关闭登录弹窗
		showLoginModal.value = false
		// 登录成功后参加活动
		await joinActivity()
		// 重新加载页面数据
		await loadPageData()
		// 开始自动刷新
		startAutoRefresh()
	}
})

/**
 * 处理用户登录
 */
const handleUserLogin = async () => {
	try {
		// 调用登录功能
		await login()
		
		// 登录成功后自动参加活动
		const token = getToken()
		if (token) {
			// 登录成功后参加活动
			await joinActivity()
			// 重新加载页面数据
			await loadPageData()
			// 开始自动刷新
			startAutoRefresh()
		}
	} catch (error) {
		console.error('登录失败:', error)
		uni.showToast({
			title: '登录失败，请重试',
			icon: 'error'
		})
	}
}

// 计算属性
const countdownDisplay = computed(() => {
	if (countdown.value <= 0) return '00:00:00'
	
	const hours = Math.floor(countdown.value / 3600)
	const minutes = Math.floor((countdown.value % 3600) / 60)
	const seconds = countdown.value % 60
	
	return `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`
})

// 是否已完成
const isCompleted = computed(() => {
	return userStatus.value.status === 'completed'
})

// 是否所有打卡点都已完成
const isAllLocationsCompleted = computed(() => {
	// 如果没有参加活动或没有地点数据，返回false
	if (!isParticipated.value || allLocations.value.length === 0) {
		return false
	}
	// 检查是否所有地点都已打卡
	return allLocations.value.every(location => hasCheckedInToday(location.id))
})

// 是否超时
const isTimeout = computed(() => {
	return countdown.value <= 0 && checkInDetail.value.endTime && new Date() > new Date(checkInDetail.value.endTime)
})

// 检查某个地点今日是否已打卡
const isLocationCheckedToday = (locationId) => {
	return todayRecords.value.some(record => record.locationId === locationId)
}

// 获取地点打卡状态样式
const getLocationCheckStatus = (locationId) => {
	return isLocationCheckedToday(locationId) ? 'checked' : 'unchecked'
}

// 页面加载
// TODO: 优化逻辑，先检查状态，在判断是否加入
onLoad(async (options) => {
	console.log('页面参数:', options)
	
	// 检查是否从二维码进入
	const { scene = '' } = options;
	const fromQrCode = scene && decodeURIComponent(scene).includes('fromQrCode=true')
	
	if (fromQrCode) {
		// 只有扫码进入才参加活动，需要先检查登录状态
		await handleJoinActivity()
	}
	
	// 加载页面数据
	await loadPageData()
	
	// 根据参加状态决定是否开始自动刷新
	startAutoRefresh()
})

/**
 * 处理参加活动（包含登录检查）
 */
const handleJoinActivity = async () => {
	// 检查登录状态
	const token = getToken()
	if (!token) {
		// 未登录，显示登录确认弹窗
		uni.showModal({
			title: '提示',
			content: '参加活动需要先登录，是否立即登录？',
			success: async (res) => {
				if (res.confirm) {
					// 用户确认登录，直接调用登录功能
					await handleUserLogin()
				}
			}
		})
		return
	}
	
	// 已登录，直接参加活动
	await joinActivity()
}

// 页面卸载
onUnload(() => {
	clearCountdownTimer()
	if (refreshTimer.value) {
		clearInterval(refreshTimer.value)
		refreshTimer.value = null
	}
})

// 组件卸载
onUnmounted(() => {
	clearCountdownTimer()
	if (refreshTimer.value) {
		clearInterval(refreshTimer.value)
		refreshTimer.value = null
	}
	// 清理事件监听
	uni.$off('storage-changed')
})

/**
 * 参加活动
 */
const joinActivity = async () => {
	try {
		loading.value = true
		const result = await checkInLocationApi.join(true)
		
		// 新的返回格式：直接返回CheckInUserDomain对象
		if (result && result.id) {
			isParticipated.value = true
			endTime.value = result.endTime
			userStatus.value = result
			
			// 开始倒计时
			if (endTime.value) {
				startCountdown()
			}
			
			// 生成二维码
			generateQRCode()
			
			// 参加成功后启动自动刷新
			startAutoRefresh()
		} else {
			uni.showToast({
				title: '参加失败',
				icon: 'error'
			})
		}
	} catch (error) {
		console.error('参加活动失败:', error)
		uni.showToast({
			title: '参加失败，请重试',
			icon: 'error'
		})
	} finally {
		loading.value = false
	}
}

/**
 * 加载页面数据
 */
const loadPageData = async () => {
	try {
		loading.value = true
		
		// 检查登录状态
		const token = getToken()
		
		if (!token) {
			// 未登录，只加载打卡点数据用于展示
			isParticipated.value = false
			const locationsResult = await checkInLocationApi.getLocations()
			allLocations.value = locationsResult.records || []
			todayRecords.value = []
			return
		}
		
		// 已登录，检查用户参加状态
		const statusResult = await checkInLocationApi.getActivityStatus()
		
		// 根据状态判断是否已参加活动
		if (statusResult && statusResult.joined) {
			isParticipated.value = true
			userStatus.value = statusResult
			endTime.value = statusResult.endTime
			
			// 如果已参加，开始倒计时
			if (endTime.value) {
				startCountdown()
			}
			
			// 生成二维码
			generateQRCode()
			
			// 加载打卡地点数据
			const locationsResult = await checkInLocationApi.getLocations()
			allLocations.value = locationsResult.records || []
			
			// 加载今日打卡记录
			await loadTodayRecords()
		} else {
			// 未参加活动，只加载地点数据用于展示
			isParticipated.value = false
			const locationsResult = await checkInLocationApi.getLocations()
			allLocations.value = locationsResult.records || []
			todayRecords.value = []
		}
		
	} catch (error) {
		console.error('加载数据失败:', error)
		uni.showToast({
			title: '加载失败，请重试',
			icon: 'error'
		})
	} finally {
		loading.value = false
	}
}

/**
 * 开始自动刷新
 * 只有已参加活动且活动未结束时才进行自动刷新今日打卡记录
 */
const startAutoRefresh = () => {
	// 清除之前的定时器
	if (refreshTimer.value) {
		clearInterval(refreshTimer.value)
		refreshTimer.value = null
	}
	
	// 只有已参加活动时才开始自动刷新今日打卡记录
	if (isParticipated.value && countdown.value > 0) {
		refreshTimer.value = setInterval(() => {
			// 检查活动是否还在进行中
			if (countdown.value > 0) {
				// 只刷新今日打卡记录，不刷新其他数据
				loadTodayRecords()
			} else {
				// 活动结束，停止自动刷新
				clearInterval(refreshTimer.value)
				refreshTimer.value = null
			}
		}, 5000) // 每5秒刷新一次今日打卡记录
	}
}

/**
 * 加载今日打卡记录
 * 调用后端getTodayRecords接口获取当前用户的今日打卡记录
 */
const loadTodayRecords = async () => {
	const res = await checkInLocationApi.getTodayRecords()
	todayRecords.value = res || []
}

/**
 * 开始倒计时
 */
const startCountdown = () => {
	clearCountdownTimer()
	
	if (!endTime.value) return
	
	const updateCountdown = () => {
		const now = new Date().getTime()
		const targetTime = new Date(endTime.value).getTime()
		const diff = Math.max(0, Math.floor((targetTime - now) / 1000))
		
		countdown.value = diff
		
		if (diff > 0) {
			countdownTimer.value = setTimeout(updateCountdown, 1000)
		} else {
			// 时间到了，重新加载数据
			loadPageData()
		}
	}
	
	updateCountdown()
}

/**
 * 清除倒计时定时器
 */
const clearCountdownTimer = () => {
	if (countdownTimer.value) {
		clearTimeout(countdownTimer.value)
		countdownTimer.value = null
	}
}

/**
 * 生成用户二维码
 */
const generateQRCode = () => {
	const userInfo = getUserInfo()
	if (!userInfo || !userInfo.id) return
	
	// 二维码内容
	const qrData = JSON.stringify({
		userId: userInfo.id,
		type: 'checkIn'
	})
	
	// 绘制二维码到canvas
	drawQRCode(qrData)
}

/**
 * 绘制二维码到canvas
 * @param {string} data 二维码数据
 */
const drawQRCode = (data) => {
	try {
		const qr = new UQRCode()
		
		// 设置二维码数据
		qr.data = data
		// 设置二维码大小，必须与canvas设置的宽高一致
		qr.size = qrcodeSize.value
		
		// 生成二维码
		qr.make()
		
		// 获取canvas上下文并绘制
		const ctx = uni.createCanvasContext('qrcode')
		qr.canvasContext = ctx
		qr.drawCanvas()
	} catch (error) {
		console.error('二维码生成失败', error)
	}
}

/**
 * 获取倒计时标签
 */
const getCountdownLabel = () => {
	const now = new Date()
	const beginTime = new Date(checkInDetail.value.beginTime)
	const endTime = new Date(checkInDetail.value.endTime)
	
	if (now < beginTime) {
		return '距离开始还有'
	} else if (now >= beginTime && now <= endTime) {
		return '距离结束还有'
	} else {
		return '已结束'
	}
}

/**
 * 检查某个地点今日是否已打卡
 * @param {string} locationId 地点ID
 * @returns {boolean}
 */
const hasCheckedInToday = (locationId) => {
	// 根据后端返回的今日打卡记录，检查是否包含指定地点ID
	// 注意：后端返回的字段名是 localtionId（拼写错误），不是 locationId
	return todayRecords.value.some(record => record.localtionId === locationId || record.localtionId === String(locationId))
}

/**
 * 获取地点状态样式类
 * @param {Object} location 地点对象
 * @returns {string}
 */
const getLocationStatusClass = (location) => {
	if (hasCheckedInToday(location.id)) {
		return 'location-checked' // 已打卡 - 橙色
	}
	return 'location-unchecked' // 未打卡 - 灰色
}

/**
 * 获取地点图标样式类
 * @param {string} locationId 地点ID
 * @returns {string}
 */
const getLocationIconClass = (locationId) => {
	// 如果未参加活动，或已参加但该地点未打卡，显示灰色
	if (!isParticipated.value || !hasCheckedInToday(locationId)) {
		return 'icon-unchecked' // 灰色
	}
	return 'icon-checked' // 橙色
}

/**
 * 判断是否显示二维码
 * 条件：已参加活动且未到期且未全部打完
 */
const shouldShowQrCode = () => {
	// 已参加活动且倒计时未结束且未全部打完时显示二维码
	if (!isParticipated.value || countdown.value <= 0) {
		return false
	}
	
	// 检查是否全部打完
	const allCheckedIn = allLocations.value.every(location => hasCheckedInToday(location.id))
	return !allCheckedIn
}

/**
 * 格式化日期时间
 * @param {string} timeStr 时间字符串
 */
const formatDateTime = (timeStr) => {
	if (!timeStr) return ''
	
	const date = new Date(timeStr)
	const month = String(date.getMonth() + 1).padStart(2, '0')
	const day = String(date.getDate()).padStart(2, '0')
	const hours = String(date.getHours()).padStart(2, '0')
	const minutes = String(date.getMinutes()).padStart(2, '0')
	
	return `${month}-${day} ${hours}:${minutes}`
}
</script>

<style lang="scss" scoped>
.content {
	min-height: 100vh;
	background: #f5f5f5;
	position: relative;
}

/* 橙色渐变背景 */
.gradient-bg {
	position: fixed;
	top: 0;
	left: 0;
	width: 100%;
	height: 50vh;
	background: linear-gradient(180deg, #ff8c00 0%, rgba(255, 140, 0, 0.6) 30%, rgba(255, 140, 0, 0.3) 60%, rgba(255, 140, 0, 0) 100%);
	z-index: 1;
}

/* 活动状态区域 */
.activity-status-section {
	padding: 30rpx;
	position: relative;
	z-index: 3;
}

.activity-status-card {
	background: linear-gradient(135deg, rgba(255, 140, 0, 0.1) 0%, rgba(255, 107, 0, 0.1) 100%);
	border-radius: 20rpx;
	border: 2rpx solid rgba(255, 140, 0, 0.3);
	padding: 40rpx 20rpx;
	text-align: center;
}

.activity-status {
	display: flex;
	flex-direction: column;
	align-items: center;
	gap: 20rpx;
}

.status-title {
	font-size: 32rpx;
	font-weight: bold;
	color: #333;
}

.countdown-time {
	font-size: 48rpx;
	font-weight: bold;
	color: #ff6b00;
	font-family: 'Courier New', monospace;
}

.status-ended {
	font-size: 28rpx;
	color: #999;
}



/* 地点列表区域 */
.locations-section {
	padding: 0 30rpx 30rpx;
	position: relative;
	z-index: 3;
}

.locations-grid {
	display: grid;
	grid-template-columns: repeat(3, 1fr);
	gap: 30rpx;
	margin-top: 20rpx;
}

.location-item {
	display: flex;
	flex-direction: column;
	align-items: center;
	gap: 15rpx;
	padding: 20rpx 10rpx;
	text-align: center;
	transition: all 0.3s ease;
}

.location-icon-container {
	display: flex;
	align-items: center;
	justify-content: center;
	width: 80rpx;
	height: 80rpx;
	border-radius: 50%;
	background: #f5f5f5;
	transition: all 0.3s ease;
}

.location-icon-container.icon-checked {
	background: rgba(51, 51, 51, 0.1);
	color: #333;
}

.location-icon-container.icon-unchecked {
	background: #f5f5f5;
	color: #999;
}

.location-icon {
	width: 48rpx;
	height: 48rpx;
	filter: brightness(0) saturate(100%);
}

.location-icon-container.icon-checked .location-icon {
	filter: brightness(0) saturate(100%) invert(20%) sepia(0%) saturate(0%) hue-rotate(0deg) brightness(0%) contrast(100%);
}

.location-icon-container.icon-unchecked .location-icon {
	filter: brightness(0) saturate(100%) invert(60%) sepia(0%) saturate(0%) hue-rotate(0deg) brightness(100%) contrast(100%);
}

.location-text {
	font-size: 24rpx;
	color: #999;
	font-weight: 500;
	line-height: 1.4;
	max-width: 120rpx;
	word-break: break-all;
}

.location-item.location-checked .location-text {
	color: #333;
	font-weight: 600;
}

/* 状态区域 */
.status-section {
	padding: 0 30rpx 30rpx;
	position: relative;
	z-index: 3;
}

.section-title {
	font-size: 32rpx;
	font-weight: bold;
	color: #333;
	margin-bottom: 20rpx;
}

.status-list {
	background: #fff;
	border-radius: 20rpx;
	padding: 20rpx;
	box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);
}

.status-item {
	padding: 20rpx 0;
	border-bottom: 1rpx solid #f0f0f0;
}

.status-item:last-child {
	border-bottom: none;
}

.user-info {
	display: flex;
	justify-content: space-between;
	align-items: center;
}

.user-name {
	font-size: 28rpx;
	color: #333;
	font-weight: 500;
}

.check-time {
	font-size: 24rpx;
	color: #999;
}

.no-records {
	text-align: center;
	padding: 40rpx 20rpx;
}

.no-records-text {
	font-size: 28rpx;
	color: #999;
}

/* 二维码区域 */
.qrcode-section {
	padding: 30rpx;
	position: relative;
	z-index: 3;
	display: flex;
	flex-direction: column;
	align-items: center;
}

.qrcode-container {
	background: #fff;
	border-radius: 20rpx;
	padding: 50rpx;
	text-align: center;
	box-shadow: 0 8rpx 30rpx rgba(0, 0, 0, 0.1);
	display: flex;
	flex-direction: column;
	align-items: center;
	max-width: 400rpx;
	width: 100%;
}

.qrcode-canvas {
	border: 2rpx solid #f0f0f0;
	border-radius: 15rpx;
	margin-bottom: 30rpx;
	box-shadow: 0 4rpx 15rpx rgba(0, 0, 0, 0.05);
}

.qrcode-tip {
	font-size: 28rpx;
	color: #666;
	display: block;
	font-weight: 500;
}

/* 操作按钮区域 */
.action-section {
	padding: 0 30rpx 30rpx;
	position: relative;
	z-index: 3;
}

.action-buttons {
	display: flex;
	gap: 20rpx;
}

.action-btn {
	flex: 1;
	height: 88rpx;
	border-radius: 44rpx;
	font-size: 32rpx;
	font-weight: bold;
	border: none;
	transition: all 0.3s ease;
}

.action-btn.primary {
	background: linear-gradient(135deg, #ff8c00 0%, #ff6b00 100%);
	color: #fff;
}

.action-btn.primary:disabled {
	background: #ccc;
	color: #999;
}

.action-btn.secondary {
	background: #fff;
	color: #ff8c00;
	border: 2rpx solid #ff8c00;
}

.action-btn:active:not(:disabled) {
	transform: scale(0.98);
}

/* 加载和空状态 */
.loading-section,
.empty-section {
	padding: 100rpx 30rpx;
	text-align: center;
	position: relative;
	z-index: 3;
}

.loading-text,
.empty-text {
	font-size: 28rpx;
	color: #999;
}
</style>
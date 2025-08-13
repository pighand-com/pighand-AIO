<template>
	<view class="content">
		<!-- 橙色渐变背景 -->
		<view class="gradient-bg"></view>

		<!-- 自定义导航栏 -->
		<custom-navigation-bar :back="true" position="inherit" />

		<!-- 封面图 -->
		<view class="cover-container" v-if="lotteryDetail.coverUrl">
			<image 
				:src="lotteryDetail.coverUrl" 
				class="cover-image" 
				mode="aspectFill"
			></image>
		</view>

		<!-- 标题 -->
		<view class="title-section">
			<text class="lottery-title">{{ lotteryDetail.title }}</text>
			<text class="lottery-description" v-if="lotteryDetail.description">{{ lotteryDetail.description }}</text>
		</view>

		<!-- 奖品信息 -->
		<view class="prizes-section" v-if="lotteryDetail.participate && lotteryDetail.participate.prizes">
			<view class="prizes-list">
				<view 
					class="prize-item" 
					v-for="(prize, index) in lotteryDetail.participate.prizes" 
					:key="prize.id"
				>
					<view class="prize-info">
						<view class="prize-header">
							<text class="prize-name">{{ lotteryDetail.participate.prizes.length === 1 ? '奖品：' : numberToChinese(index + 1) + '等奖：' }}{{ prize.name }}</text>
							<text class="prize-flag">x</text>
							<text class="prize-quota">{{ prize.lotteryQuota }} 份</text>
						</view>
						<text class="prize-description" v-if="prize.description">{{ prize.description }}</text>
					</view>
					<view class="prize-image" v-if="prize.imageUrl">
						<image :src="prize.imageUrl" class="prize-img" mode="aspectFill"></image>
					</view>
				</view>
			</view>
		</view>

		<!-- 开奖时间 -->
		<view class="draw-time-section">
			<view class="draw-time-block">
				<view class="countdown-container" v-if="countdown.show">
					<text class="countdown-label">{{ getCountdownLabel() }}</text>
					<view class="countdown-time">
						<view class="time-unit" v-if="countdown.days > 0">
							<text class="time-number">{{ countdown.days }}</text>
							<text class="time-text">天</text>
						</view>
						<view class="time-unit">
							<text class="time-number">{{ countdown.hours }}</text>
							<text class="time-text">时</text>
						</view>
						<view class="time-unit">
							<text class="time-number">{{ countdown.minutes }}</text>
							<text class="time-text">分</text>
						</view>
						<view class="time-unit">
							<text class="time-number">{{ countdown.seconds }}</text>
							<text class="time-text">秒</text>
						</view>
					</view>
				</view>
				<view class="draw-time-fixed" v-else>
					<text class="draw-time-status">{{ getDrawTimeStatusText() }}</text>
				</view>
				<text class="draw-time-date" v-if="!countdown.show && lotteryDetail.isEnd">开奖时间 {{ formatTime(lotteryDetail.drawTime) }}</text>
				<text class="draw-time-date" v-if="!countdown.show && lotteryDetail.isBegin && !lotteryDetail.isEnd">{{ formatTime(lotteryDetail.drawTime) }}</text>
			</view>
		</view>

		<!-- 活动时间 -->
		<view class="activity-time-section">
			<view class="activity-time-content">
				<text class="activity-time-title">活动时间</text>
				<view class="time-range">
					<text class="time-value">{{ formatTime(lotteryDetail.beginTime) }}</text>
					<text class="time-separator">-</text>
					<text class="time-value">{{ formatTime(lotteryDetail.endTime) }}</text>
				</view>
			</view>
		</view>

		<!-- 参与按钮 -->
		<view class="participate-section">
			<!-- 根据活动状态和登录状态设置用户检查 -->
			<user-check :item="getCheckItems()" :disabled="getButtonDisabled()">
				<view 
					class="participate-button" 
					:class="{ 
						'participated': lotteryDetail.isParticipate,
						'disabled': getButtonDisabled()
					}"
					@click="handleParticipate"
				>
					<text class="participate-text">
						{{ getParticipateButtonText() }}
					</text>
				</view>
			</user-check>
		</view>

		<!-- 参与人数统计 -->
		<view class="participants-stats">
			<text class="stats-text" @click="goToParticipantsList">
				已有 {{ lotteryDetail.participateCount || 0 }} 人参与，查看全部 >
			</text>
		</view>

		<!-- 参与人员头像列表 -->
		<view class="participants-avatars" v-if="participantsList.length > 0">
			<view 
				class="avatar-item" 
				v-for="(participant, index) in displayParticipants" 
				:key="participant.id"
			>
				<image 
					:src="participant.userExtension?.profile || '/static/default-avatar.jpg'" 
					class="avatar-image" 
					mode="aspectFill"
				></image>
			</view>
		</view>
	</view>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { onLoad, onHide, onShow, onShareAppMessage, onShareTimeline } from '@dcloudio/uni-app'
import { lottery as lotteryAPI } from '@/api'
import { getToken } from '@/common/storage'
import { createShareInfo } from '@/common/share'

const lotteryId = ref('')
const lotteryDetail = ref({})
const participantsList = ref([])
const loading = ref(false)
const countdownTimer = ref(null)
const countdown = ref({
	show: false,
	days: 0,
	hours: 0,
	minutes: 0,
	seconds: 0,
	status: ''
})

// 显示的参与者列表（最多6个）
const maxParticipants = 6
const displayParticipants = computed(() => {
	return participantsList.value.slice(0, maxParticipants)
})

// 兼容iOS的日期解析函数
const parseDate = (dateStr) => {
	if (!dateStr) return null
	// 将 "yyyy-MM-dd HH:mm:ss" 格式转换为 "yyyy/MM/dd HH:mm:ss" 格式
	const formattedStr = dateStr.replace(/-/g, '/')
	return new Date(formattedStr)
}

// 格式化时间
const formatTime = (timeStr) => {
	if (!timeStr) return ''
	return timeStr.replace(/:\d{2}$/, '') // 去掉秒数
}

// 数字转中文大写
const numberToChinese = (num) => {
	const chineseNumbers = ['一', '二', '三', '四', '五', '六', '七', '八', '九', '十']
	if (num <= 10) {
		return chineseNumbers[num - 1]
	}
	return num.toString()
}

// 计算倒计时
const calculateCountdown = () => {
	if (!lotteryDetail.value.drawTime || !lotteryDetail.value.beginTime) return
	
	const now = new Date().getTime()
	const beginTime = parseDate(lotteryDetail.value.beginTime).getTime()
	
	// 未开始：显示开始倒计时
	if (!lotteryDetail.value.isBegin) {
		const diff = beginTime - now
		if (diff <= 0) {
			countdown.value.show = false
			return
		}
		countdown.value.show = true
		countdown.value.days = Math.floor(diff / (1000 * 60 * 60 * 24))
		countdown.value.hours = Math.floor((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60))
		countdown.value.minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60))
		countdown.value.seconds = Math.floor((diff % (1000 * 60)) / 1000)
		return
	}
	
	// 进行中：显示结束倒计时
	if (lotteryDetail.value.isBegin && !lotteryDetail.value.isEnd) {
		const endTime = parseDate(lotteryDetail.value.endTime).getTime()
		const diff = endTime - now
		if (diff <= 0) {
			countdown.value.show = false
			clearInterval(countdownTimer.value)
			return
		}
		countdown.value.show = true
		countdown.value.days = Math.floor(diff / (1000 * 60 * 60 * 24))
		countdown.value.hours = Math.floor((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60))
		countdown.value.minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60))
		countdown.value.seconds = Math.floor((diff % (1000 * 60)) / 1000)
		return
	}
	
	// 已结束：不显示倒计时
	if (lotteryDetail.value.isEnd) {
		countdown.value.show = false
		clearInterval(countdownTimer.value)
	}
}

// 启动倒计时
const startCountdown = () => {
	calculateCountdown()
	countdownTimer.value = setInterval(calculateCountdown, 1000)
}

// 停止倒计时
const stopCountdown = () => {
	if (countdownTimer.value) {
		clearInterval(countdownTimer.value)
		countdownTimer.value = null
	}
}

// 获取抽奖详情
const getLotteryDetail = async () => {
	try {
		loading.value = true
		const res = await lotteryAPI.getDetail(lotteryId.value)
		if (res) {
			lotteryDetail.value = res
			// 获取详情后启动倒计时
			startCountdown()
		}
	} catch (error) {
		console.error('获取抽奖详情失败:', error)
		uni.showToast({
			title: '获取抽奖详情失败',
			icon: 'none'
		})
	} finally {
		loading.value = false
	}
}

// 获取参与人员列表
const getParticipantsList = async () => {
	try {
		const res = await lotteryAPI.getParticipants(lotteryId.value, maxParticipants, 1)
		if (res) {
			participantsList.value = res.records
		}
	} catch (error) {
		console.error('获取参与人员列表失败:', error)
	}
}

// 获取参与按钮文本
const getParticipateButtonText = () => {
	const isLogin = !!getToken()
	
	// 未开始
	if (!lotteryDetail.value.isBegin) {
		return '参与抽奖'
	}
	
	// 进行中
	if (lotteryDetail.value.isBegin && !lotteryDetail.value.isEnd) {
		if (!isLogin) {
			return '登录参加'
		}
		return lotteryDetail.value.isParticipate ? '已参加' : '参加'
	}
	
	// 已结束
	if (lotteryDetail.value.isEnd) {
		if (!isLogin) {
			return '登录查看结果'
		}
		return lotteryDetail.value.isParticipate ? '已参加' : '参加'
	}
	
	// 默认处理
	return '参与抽奖'
}

// 获取倒计时标签文本
const getCountdownLabel = () => {
	// 未开始：显示距离开始还有
	if (!lotteryDetail.value.isBegin) {
		return '距离开始还有'
	}
	
	// 进行中：显示距离结束还有
	if (lotteryDetail.value.isBegin && !lotteryDetail.value.isEnd) {
		return '距离结束还有'
	}
	
	// 默认
	return '距离开奖还有'
}

// 获取开奖时间状态文本
const getDrawTimeStatusText = () => {
	const isLogin = !!getToken()
	
	// 已结束时的逻辑
	if (lotteryDetail.value.isEnd) {
		// 未开奖（drawStatus !== 30）
		if (lotteryDetail.value.drawStatus !== 30) {
			return '开奖中'
		}
		
		// 已开奖（drawStatus === 30）
		if (!isLogin) {
			return '已开奖'
		}
		
		// 已登录，根据userPrizeId显示奖品名称
		const userPrizeId = lotteryDetail.value.userPrizeId
		
		// 如果userPrizeId为null或undefined，显示未中奖
		if (!userPrizeId) {
			return '未中奖'
		}
		
		// 在奖品列表中查找匹配的奖品
		const prizes = lotteryDetail.value.participate?.prizes || []
		const matchedPrize = prizes.find(prize => prize.id === userPrizeId)
		
		// 如果找到匹配的奖品，返回奖品名称，否则返回未中奖
		return matchedPrize ? matchedPrize.name : '未中奖'
	}

	return '已开奖'
}

// 获取用户检查项
const getCheckItems = () => {
	const isLogin = !!getToken()
	
	// 进行中，未登录校验['login', 'phone']
	if (lotteryDetail.value.isBegin && !lotteryDetail.value.isEnd && !isLogin) {
		return ['login', 'phone']
	}
	
	// 结束，未登录，校验['login']
	if (lotteryDetail.value.isEnd && !isLogin) {
		return ['login']
	}
	
	// 其他情况不需要检查
	return []
}

// 获取按钮是否禁用
const getButtonDisabled = () => {
	// 未开始，不可点击
	if (!lotteryDetail.value.isBegin) {
		return true
	}

	if(!getToken()) {
		return false
	}
	
	// 进行中，已参与不可点击
	if (lotteryDetail.value.isBegin && !lotteryDetail.value.isEnd && lotteryDetail.value.isParticipate) {
		return true
	}
	
	// 结束，不可点击
	if (lotteryDetail.value.isEnd) {
		return true
	}
	
	return false
}

// 参与抽奖
const handleParticipate = async () => {
	// 如果按钮被禁用，不允许操作
	if (getButtonDisabled()) {
		return
	}
	
	// 如果已参与，不可重复参与
	if (lotteryDetail.value.isParticipate) {
		return
	}
	
	// 只有在进行中且未参与时才能参与
	if (!lotteryDetail.value.isBegin || lotteryDetail.value.isEnd) {
		return
	}

	try {
		loading.value = true
		await lotteryAPI.participate(lotteryId.value)
		uni.showToast({
			title: '参与成功',
			icon: 'success'
		})
		// 重新获取详情和参与人员列表
		await getLotteryDetail()
		await getParticipantsList()
	} catch (error) {
		console.error('参与抽奖失败:', error)
		uni.showToast({
			title: '参与失败，请重试',
			icon: 'none'
		})
	} finally {
		loading.value = false
	}
}

// 跳转到参与人员列表页面
const goToParticipantsList = () => {
	uni.navigateTo({
		url: `/pages/MKT/pages/lottery-participants?id=${lotteryId.value}`
	})
}

// 监听登录状态变化
const handleStorageChange = () => {
	// 登录后刷新页面数据
	getLotteryDetail()
	if(participantsList.value?.length < maxParticipants) {
		getParticipantsList()
	}
}

// 页面加载
onLoad((options) => {
	if (options.id) {
		lotteryId.value = options.id
		getLotteryDetail()
		getParticipantsList()
	} else {
		uni.showToast({
			title: '缺少抽奖ID',
			icon: 'none'
		})
		setTimeout(() => {
			uni.navigateBack()
		}, 1500)
	}
})

// 页面挂载时监听storage变化
onMounted(() => {
	uni.$on('storage-changed', handleStorageChange)
})

// 页面显示时重新启动倒计时
onShow(() => {
	if (lotteryDetail.value.id) {
		startCountdown()
	}
})

// 页面隐藏时停止倒计时
onHide(() => {
	stopCountdown()
})

// 页面卸载时清理定时器和事件监听
onUnmounted(() => {
	stopCountdown()
	uni.$off('storage-changed', handleStorageChange)
})

// 分享给朋友
onShareAppMessage(() => {
	return createShareInfo({
		imageUrl: lotteryDetail.value.coverUrl,
		title: lotteryDetail.value.title,
	})
})

// 分享到朋友圈
onShareTimeline(() => {
	return createShareInfo({
		imageUrl: lotteryDetail.value.coverUrl,
		title: lotteryDetail.value.title,
	})
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
	height: 100%;
	background: linear-gradient(180deg, #ff8c00 0%, rgba(255, 140, 0, 0.6) 30%, rgba(255, 140, 0, 0.3) 60%, rgba(255, 140, 0, 0) 100%);
	z-index: 1;
}

/* 封面图 */
.cover-container {
	width: 100%;
	height: 400rpx;
	position: relative;
	z-index: 2;
	padding: 20rpx 30rpx;
	box-sizing: border-box;
}

.cover-image {
	width: 100%;
	height: 100%;
	border-radius: 20rpx;
}

/* 标题区域 */
.title-section {
	padding: 30rpx;
	position: relative;
	z-index: 3;
	text-align: left;
}

.lottery-title {
	font-size: 40rpx;
	font-weight: bold;
	color: #333;
	display: block;
	margin-bottom: 20rpx;
}

.lottery-description {
	font-size: 28rpx;
	color: #666;
	line-height: 1.5;
	display: block;
}

/* 通用区域标题 */
.section-title {
	font-size: 32rpx;
	font-weight: bold;
	color: #333;
	padding: 20rpx 30rpx;
	position: relative;
	z-index: 3;
}

/* 奖品信息区域 */
.prizes-section {
	position: relative;
	z-index: 3;
}

.prizes-list {
	padding: 0 30rpx;
}

.prize-item {
	display: flex;
	align-items: center;
}

.prize-info {
	flex: 1;
}

.prize-header {
	display: flex;
	align-items: center;
	margin-bottom: 10rpx;
	gap: 16px;
}

.prize-name {
	font-size: 32rpx;
	font-weight: bold;
	color: #333;
}

.prize-flag {
	font-size: 32rpx;
	font-weight: bold;
	color: #666;
}

.prize-quota {
	font-size: 32rpx;
	font-weight: bold;
	color: #666;
}

.prize-description {
	font-size: 26rpx;
	color: #666;
	line-height: 1.4;
}

.prize-image {
	width: 120rpx;
	height: 120rpx;
	border-radius: 12rpx;
	overflow: hidden;
	margin-left: 20rpx;
	flex-shrink: 0;
}

.prize-img {
	width: 100%;
	height: 100%;
}

/* 活动时间区域 */
.activity-time-section {
	position: relative;
	z-index: 3;
}

.activity-time-content {
	display: flex;
	align-items: center;
	justify-content: start;
	padding: 20rpx 30rpx 30rpx;
}

.activity-time-title {
	font-size: 28rpx;
	color: #666;
	padding-right: 16rpx;
}

.time-range {
	display: flex;
	align-items: center;
}

.time-value {
	font-size: 28rpx;
	color: #666;
}

.time-separator {
	font-size: 28rpx;
	color: #666;
	margin: 0 10rpx;
}

/* 开奖时间区域 */
.draw-time-section {
	margin-top: 32rpx;
	position: relative;
	z-index: 3;
	padding: 0 30rpx;
}

.draw-time-block {
	text-align: center;
	padding: 40rpx 20rpx 30rpx;
	background: linear-gradient(135deg, rgba(255, 140, 0, 0.1) 0%, rgba(255, 107, 0, 0.1) 100%);
	border-radius: 20rpx;
	border: 2rpx solid rgba(255, 140, 0, 0.3);
}

.countdown-container {
	margin-bottom: 20rpx;
}

.countdown-label {
	font-size: 28rpx;
	color: #666;
	display: block;
	margin-bottom: 20rpx;
}

.countdown-time {
	display: flex;
	justify-content: center;
	align-items: center;
	gap: 20rpx;
}

.time-unit {
	display: flex;
	align-items: flex-end;
	gap: 4rpx;
}

.time-number {
	font-size: 48rpx;
	color: #ff6b00;
	font-weight: bold;
	line-height: 1;
}

.time-text {
	font-size: 24rpx;
	color: #999;
	line-height: 1;
	padding-bottom: 2rpx;
}

.draw-time-fixed {
	margin-bottom: 20rpx;
}

.draw-time-status {
	font-size: 36rpx;
	color: #ff6b00;
	font-weight: bold;
	display: block;
}

.draw-time-date {
	font-size: 24rpx;
	color: #999;
	display: block;
}

/* 参与按钮区域 */
.participate-section {
	padding: 40rpx 30rpx;
	position: relative;
	z-index: 3;
}

.participate-button {
	width: 100%;
	height: 100rpx;
	background: linear-gradient(135deg, #ff8c00 0%, #ff6b00 100%);
	border-radius: 50rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	box-shadow: 0 8rpx 30rpx rgba(255, 140, 0, 0.3);
	transition: all 0.3s ease;
}

.participate-button.participated {
	background: #ccc;
	box-shadow: none;
}

.participate-button.disabled {
	background: #ccc;
	box-shadow: none;
	cursor: not-allowed;
}

.participate-button:active:not(.participated):not(.disabled) {
	transform: scale(0.98);
}

.participate-text {
	font-size: 32rpx;
	color: #ffffff;
	font-weight: bold;
}

/* 参与人数统计 */
.participants-stats {
	text-align: center;
	padding: 0 30rpx 20rpx;
	position: relative;
	z-index: 3;
}

.stats-text {
	font-size: 26rpx;
	color: #666;
}

/* 参与人员头像列表 */
.participants-avatars {
	position: relative;
	z-index: 3;
	display: flex;
    justify-content: center;
}

.avatar-item {
	width: 64rpx;
	height: 64rpx;
	border-radius: 50rpx;
	overflow: hidden;
	border: 3rpx solid #fff;
	box-shadow: 0 4rpx 15rpx rgba(0, 0, 0, 0.1);
	margin: 0 8rpx;
}

.avatar-image {
	width: 100%;
	height: 100%;
}
</style>
<template>
	<view class="content">
		<!-- 橙色渐变背景 -->
		<view class="gradient-bg"></view>

		<!-- 自定义导航栏 -->
		<custom-navigation-bar title="参与用户" :back="true" position="relative" />

		<!-- 参与人数统计 -->
		<view class="participants-stats">
			<text class="participants-count">共 {{ totalCount }} 人参与</text>
		</view>

		<!-- 参与人员列表 -->
		<view class="participants-container">
			<view class="participants-list" v-if="participantsList.length > 0">
				<view 
					class="avatar-item" 
					v-for="(participant, index) in participantsList" 
					:key="participant.id"
				>
					<image 
						:src="participant.userExtension?.profile || '/static/default-avatar.jpg'" 
						class="avatar-image" 
						mode="aspectFill"
					></image>
				</view>
			</view>

			<!-- 空状态 -->
			<view class="empty-state" v-if="participantsList.length === 0 && !loading">
				<text class="empty-text">暂无参与用户</text>
			</view>

			<!-- 加载更多 -->
			<view class="load-more" v-if="hasMore && participantsList.length > 0">
				<text class="load-more-text" @click="loadMore">{{ loadingMore ? '加载中...' : '加载更多' }}</text>
			</view>

			<!-- 没有更多 -->
			<view class="no-more" v-if="!hasMore && participantsList.length > 0">
				<text class="no-more-text">没有更多了</text>
			</view>
		</view>
	</view>
</template>

<script setup>
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { lottery as lotteryAPI } from '@/api'

const lotteryId = ref('')
const participantsList = ref([])
const loading = ref(false)
const loadingMore = ref(false)
const hasMore = ref(true)
const currentPage = ref(1)
const pageSize = ref(80)
const totalCount = ref(0)

// 格式化时间
const formatTime = (timeStr) => {
	if (!timeStr) return ''
	const date = new Date(timeStr)
	const now = new Date()
	const diff = now - date
	const minutes = Math.floor(diff / (1000 * 60))
	const hours = Math.floor(diff / (1000 * 60 * 60))
	const days = Math.floor(diff / (1000 * 60 * 60 * 24))

	if (days > 0) {
		return `${days}天前`
	} else if (hours > 0) {
		return `${hours}小时前`
	} else if (minutes > 0) {
		return `${minutes}分钟前`
	} else {
		return '刚刚'
	}
}

// 获取参与人员列表
const getParticipantsList = async (isLoadMore = false) => {
	try {
		if (isLoadMore) {
			loadingMore.value = true
		} else {
			loading.value = true
		}

		const res = await lotteryAPI.getParticipants(lotteryId.value, pageSize.value, currentPage.value)
		if (res) {
			if (isLoadMore) {
				participantsList.value = [...participantsList.value, ...res.records]
			} else {
				participantsList.value = res.records
				totalCount.value = res.page.totalRow
			}

			// 判断是否还有更多数据
			if (res.records.length < pageSize.value) {
				hasMore.value = false
			} else {
				currentPage.value++
			}
		} else {
			hasMore.value = false
		}
	} catch (error) {
		console.error('获取参与人员列表失败:', error)
		if (!isLoadMore) {
			uni.showToast({
				title: '获取参与人员列表失败',
				icon: 'none'
			})
		}
	} finally {
		loading.value = false
		loadingMore.value = false
	}
}

// 加载更多
const loadMore = () => {
	if (!loadingMore.value && hasMore.value) {
		getParticipantsList(true)
	}
}



// 页面加载
onLoad((options) => {
	if (options.id) {
		lotteryId.value = options.id
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
</script>

<style scoped>
.content {
	background-color: white;
	position: relative;
	min-height: 100vh;
	overflow-x: hidden;
}

/* 橙色渐变背景 */
.gradient-bg {
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 100vh;
	background: linear-gradient(180deg, #ff8c00 0%, rgba(255, 140, 0, 0.6) 30%, rgba(255, 140, 0, 0.3) 60%, rgba(255, 140, 0, 0) 100%);
	z-index: 1;
	pointer-events: none;
}

/* 参与人员容器 */
.participants-container {
	position: relative;
	z-index: 3;
	padding: 20rpx 30rpx;
}

/* 参与人员列表 */
.participants-list {
	display: flex;
	flex-wrap: wrap;
	justify-content: center;
	gap: 20rpx;
	padding: 20rpx 0;
}

.avatar-item {
	width: 64rpx;
	height: 64rpx;
	border-radius: 50rpx;
	overflow: hidden;
	border: 3rpx solid #fff;
	box-shadow: 0 4rpx 15rpx rgba(0, 0, 0, 0.1);
}

.avatar-image {
	width: 100%;
	height: 100%;
}

/* 参与人数统计 */
.participants-stats {
	position: relative;
	z-index: 2;
	text-align: center;
	padding: 40rpx 0 20rpx;
}

.participants-count {
	font-size: 28rpx;
	color: #666;
}

/* 空状态 */
.empty-state {
	text-align: center;
	padding: 100rpx 0;
}

.empty-text {
	font-size: 28rpx;
	color: #999;
}

/* 加载更多 */
.load-more {
	text-align: center;
	padding: 40rpx 0;
}

.load-more-text {
	font-size: 28rpx;
	color: #ff8c00;
	text-decoration: underline;
}

/* 没有更多 */
.no-more {
	text-align: center;
	padding: 40rpx 0;
}

.no-more-text {
	font-size: 26rpx;
	color: #999;
}
</style>
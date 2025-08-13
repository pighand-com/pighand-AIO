<template>
	<view class="content">
		<!-- 自定义导航栏 -->
		<custom-navigation-bar :back="true" position="inherit" title="抽奖活动" />

		<!-- 抽奖列表 -->
		<view class="lottery-list">
			<view 
				class="lottery-card" 
				v-for="lottery in lotteryList" 
				:key="lottery.id"
			>
				<!-- 封面图片 -->
				<view class="card-cover">
					<image 
						:src="lottery.coverUrl || '/static/default-cover.jpg'" 
						class="cover-image" 
						mode="aspectFill"
					></image>
				</view>

				<!-- 卡片内容 -->
				<view class="card-content">
					<!-- 标题 -->
					<text class="lottery-title">{{ lottery.title }}</text>
					
					<!-- 描述信息 -->
					<text class="lottery-description" v-if="lottery.description">
						{{ lottery.description }}
					</text>

					<!-- 参与按钮 -->
					<view 
						class="participate-button"
						@click="goToLotteryDetail(lottery.id)"
					>
						<text class="participate-text">参与抽奖</text>
					</view>
				</view>
			</view>
		</view>

		<!-- 加载更多 -->
		<view class="load-more" v-if="loading">
			<text class="load-text">加载中...</text>
		</view>

		<!-- 暂无数据 -->
		<view class="no-data" v-if="!loading && lotteryList.length === 0">
			<text class="no-data-text">暂无抽奖活动</text>
		</view>
	</view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { lottery as lotteryAPI } from '@/api'

const lotteryList = ref([])
const loading = ref(false)

// 获取抽奖列表
const getLotteryList = async () => {
	try {
		loading.value = true
		const response = await lotteryAPI.query()
		if (response) {
			lotteryList.value = response.records
		}
	} catch (error) {
		console.error('获取抽奖列表失败:', error)
		uni.showToast({
			title: '获取数据失败',
			icon: 'none'
		})
	} finally {
		loading.value = false
	}
}

// 跳转到抽奖详情页
const goToLotteryDetail = (lotteryId) => {
	uni.navigateTo({
		url: `/pages/MKT/pages/lottery?id=${lotteryId}`
	})
}

// 页面加载
onLoad(() => {
	getLotteryList()
})

// 组件挂载
onMounted(() => {
	getLotteryList()
})
</script>

<style lang="scss" scoped>
.content {
	min-height: 100vh;
	background: #f5f5f5;
	position: relative;
}

.lottery-list {
	padding: 20rpx;
	position: relative;
	z-index: 2;
}

.lottery-card {
	background: #fff;
	border-radius: 20rpx;
	margin-bottom: 30rpx;
	overflow: hidden;
	box-shadow: 0 8rpx 30rpx rgba(0, 0, 0, 0.1);
	transition: all 0.3s ease;
}

.lottery-card:active {
	transform: scale(0.98);
}

.card-cover {
	width: 100%;
	height: 400rpx;
	position: relative;
	overflow: hidden;
}

.cover-image {
	width: 100%;
	height: 100%;
	object-fit: cover;
}

.card-content {
	padding: 30rpx;
}

.lottery-title {
	font-size: 36rpx;
	font-weight: bold;
	color: #333;
	line-height: 1.4;
	margin-bottom: 20rpx;
	display: block;
	text-align: center;
}

.lottery-description {
	font-size: 28rpx;
	color: #666;
	line-height: 1.5;
	margin-bottom: 30rpx;
	display: block;
	overflow: hidden;
	text-overflow: ellipsis;
	display: -webkit-box;
	-webkit-line-clamp: 2;
	-webkit-box-orient: vertical;
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
	margin-top: 40rpx;
}

.participate-button:active {
	transform: scale(0.98);
}

.participate-text {
	color: #fff;
	font-size: 32rpx;
	font-weight: bold;
}

.load-more {
	padding: 40rpx;
	text-align: center;
}

.load-text {
	color: #999;
	font-size: 28rpx;
}

.no-data {
	padding: 100rpx 40rpx;
	text-align: center;
}

.no-data-text {
	color: #999;
	font-size: 32rpx;
}
</style>
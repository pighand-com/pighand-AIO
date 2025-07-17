<template>
	<view class="webview-container">
		<!-- 自定义导航栏 -->
		<custom-navigation-bar position="flex" :title="pageTitle" />
		
		<!-- WebView内容区域 -->
		<view class="webview-content">
			<web-view 
				:src="webviewUrl" 
				@message="handleMessage"
				@error="handleError"
				@load="handleLoad"
				@loading="handleLoading"
			></web-view>
		</view>
		
		<!-- 加载状态 -->
		<view v-if="isLoading" class="loading-container">
			<uni-load-more status="loading" :content-text="loadingText"></uni-load-more>
		</view>
		
		<!-- 错误状态 -->
		<view v-if="hasError" class="error-container">
			<view class="error-content">
				<text class="error-icon">⚠️</text>
				<text class="error-title">页面加载失败</text>
				<text class="error-message">{{ errorMessage }}</text>
				<custom-button 
					text="重新加载" 
					width="200rpx" 
					fontSize="28rpx"
					@click="reloadWebview" 
				/>
			</view>
		</view>
	</view>
</template>

<script setup>
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'

// 响应式数据
const webviewUrl = ref('')
const pageTitle = ref('网页浏览')
const isLoading = ref(false)
const hasError = ref(false)
const errorMessage = ref('')

// 加载文本配置
const loadingText = ref({
	contentdown: '上拉显示更多',
	contentrefresh: '正在加载...',
	contentnomore: '没有更多数据了'
})

// WebView事件处理
const handleMessage = (event) => {
	// 处理来自WebView的消息
	const data = event.detail.data
	if (data && data.length > 0) {
		const message = data[0]
		// 根据消息类型处理不同逻辑
		switch (message.type) {
			case 'title':
				pageTitle.value = message.title || '网页浏览'
				break
			case 'close':
				uni.navigateBack()
				break
			default:
				console.log('未知消息类型:', message)
		}
	}
}

const handleError = (event) => {
	console.error('WebView加载错误:', event)
	isLoading.value = false
	hasError.value = true
	errorMessage.value = '网页加载失败，请检查网络连接或稍后重试'
}

const handleLoad = (event) => {
	isLoading.value = false
	hasError.value = false
}

const handleLoading = (event) => {
	isLoading.value = true
	hasError.value = false
}

// 重新加载WebView
const reloadWebview = () => {
	hasError.value = false
	isLoading.value = true
	// 通过重新设置URL来触发重新加载
	const currentUrl = webviewUrl.value
	webviewUrl.value = ''
	setTimeout(() => {
		webviewUrl.value = currentUrl
	}, 100)
}

// 页面生命周期
onLoad((options) => {
	// 获取传入的URL和标题
	if (options.url) {
		webviewUrl.value = decodeURIComponent(options.url)
	}
	
	if (options.title) {
		pageTitle.value = decodeURIComponent(options.title)
	}
	
	// 如果没有传入URL，显示错误
	if (!webviewUrl.value) {
		hasError.value = true
		errorMessage.value = '缺少必要的URL参数'
		return
	}
	
	// 开始加载
	isLoading.value = true
})
</script>

<style scoped>
.webview-container {
	width: 100%;
	height: 100vh;
	display: flex;
	flex-direction: column;
	background-color: #ffffff;
}

.webview-content {
	flex: 1;
	width: 100%;
	position: relative;
	overflow: hidden;
}

.webview-content web-view {
	width: 100%;
	height: 100%;
}

.loading-container {
	position: absolute;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
	z-index: 10;
	background-color: rgba(255, 255, 255, 0.9);
	border-radius: 12rpx;
	padding: 40rpx;
}

.error-container {
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	display: flex;
	align-items: center;
	justify-content: center;
	background-color: #f8f9fa;
	z-index: 10;
}

.error-content {
	display: flex;
	flex-direction: column;
	align-items: center;
	padding: 60rpx 40rpx;
	text-align: center;
}

.error-icon {
	font-size: 80rpx;
	margin-bottom: 30rpx;
}

.error-title {
	font-size: 36rpx;
	font-weight: bold;
	color: #333333;
	margin-bottom: 20rpx;
}

.error-message {
	font-size: 28rpx;
	color: #666666;
	line-height: 1.5;
	margin-bottom: 40rpx;
	max-width: 500rpx;
}
</style>
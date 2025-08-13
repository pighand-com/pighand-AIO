<template>
	<view class="content">
		<custom-navigation-bar :back="true" />
		
		<view class="store-content">
			<rich-text class="store-description" :nodes="storeDetail.introduce"></rich-text>
		</view>
	</view>
</template>

<script setup>
import { onLoad, onShareAppMessage, onShareTimeline } from '@dcloudio/uni-app'
import { ref } from 'vue'
import { store as storeAPI } from '@/api'
import { createShareInfo } from '@/common/share'

// 主题详情数据
const storeDetail = ref({})

onLoad((options) => {
	const { id } = options
	if (id) {
		getStoreDetail(id)
	}
})

// 获取详情
const getStoreDetail = async (id) => {
	try {
		const res = await storeAPI.find(id)
		storeDetail.value = res
	} catch (error) {
		console.error('获取详情失败:', error)
	}
}

// 分享给朋友
onShareAppMessage(() => {
	return createShareInfo({
		params: { id: storeDetail.value.id }
	})
})

// 分享到朋友圈
onShareTimeline(() => {
	return createShareInfo({
		params: { id: storeDetail.value.id }
	})
})

</script>

<style scoped>
.content {
	background-color: #f9f4c9;
	padding-bottom: 160rpx; /* 为底部操作栏留出空间 */
}

.store-poster {
	width: 100%;
	height: 720rpx;
	position: relative;
	overflow: hidden;
}

.poster-image {
	width: 100%;
}



.store-content {
}

.store-title {
	font-size: 36rpx;
	font-weight: bold;
	color: #333;
	display: block;
	margin-bottom: 20rpx;
}

.store-description {
	font-size: 28rpx;
	line-height: 1.6;
	display: block;
}

</style>

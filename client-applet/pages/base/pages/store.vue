<template>
	<view class="content">
		<custom-navigation-bar :back="true" />
		
		<view class="store-content">
			<rich-text class="store-description" :nodes="storeDetail.introduce"></rich-text>
		</view>
	</view>
</template>

<script setup>
import { onLoad } from '@dcloudio/uni-app'
import { ref } from 'vue'
import { store as storeAPI } from '@/api'

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

</script>

<style scoped>
.content {
	background-color: #f5f5f5;
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
	padding: 30rpx 0;
	background-color: #fff;
	margin-top: 20rpx;
	margin-bottom: 20rpx; /* 增加底部间距 */
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

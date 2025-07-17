 <template>
	<view class="customer-service-page">
		<custom-navigation-bar title="客服" />
		<view class="content">
			<view class="service-info">
				<text class="title">联系客服</text>
				<text class="desc">如有任何问题，请联系我们的客服团队</text>
			</view>
			<button class="service-btn" open-type="contact">
				<image class="service-icon" src="/static/icon-customer-service.png" mode="aspectFit"></image>
				<text class="service-text">联系在线客服</text>
			</button>
		</view>
        
		<tab-bar />
	</view>
</template>

<script>
export default {
	name: 'CustomerService',
	data() {
		return {
			
		}
	},
	methods: {
		
	}
}
</script>

<style scoped>
.customer-service-page {
	min-height: 100vh;
	background-color: #f8f8f8;
}

.content {
	padding: 40rpx;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	min-height: calc(100vh - 200rpx);
}

.service-info {
	text-align: center;
	margin-bottom: 80rpx;
}

.title {
	font-size: 36rpx;
	font-weight: bold;
	color: #333;
	display: block;
	margin-bottom: 20rpx;
}

.desc {
	font-size: 28rpx;
	color: #666;
	display: block;
}

.service-btn {
	width: 300rpx;
	background-color: #AC9157;
	border-radius: 60rpx;
	border: none;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	box-shadow: 0 4rpx 12rpx rgba(172, 145, 87, 0.3);
    padding: 24rpx 0;
}

.service-btn::after {
	border: none;
}

.service-icon {
	width: 40rpx;
	height: 40rpx;
	margin-bottom: 8rpx;
}

.service-text {
    padding-top: 8rpx;
	font-size: 28rpx;
    line-height: 28rpx;
	color: #fff;
	font-weight: 500;
}
</style>
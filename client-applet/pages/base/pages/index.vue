<template>
	<view class="content">
		<image class="logo" src="../static/logo.png"></image>

		
		<user-check>
			<button @click="test">登录</button>
		</user-check>
		
	</view>
		<tab-bar />
</template>

<script setup>

	    import { ref } from 'vue'
		
		function test() {
			console.log(11111)
		}

</script>

<style>
	.content {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
	}

	.logo {
		height: 200rpx;
		width: 200rpx;
		margin-top: 200rpx;
		margin-left: auto;
		margin-right: auto;
		margin-bottom: 50rpx;
	}

	.text-area {
		display: flex;
		justify-content: center;
	}

	.title {
		font-size: 36rpx;
		color: #8f8f94;
	}
</style>

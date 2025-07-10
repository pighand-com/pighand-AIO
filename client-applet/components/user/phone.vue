<template>
	<view class="phone-wrapper">
		<slot></slot>
		<button v-if="!phoneRef"
			class="overlay-button" 
			open-type="getPhoneNumber" 
			@getphonenumber="finishGetPhoneNumber"
		></button>
	</view>
</template>

<script setup>
import { ref, onUnmounted } from 'vue'
import { user } from '@/api'
import {getUserInfo, setUserInfo} from '@/common/storage'

const phoneRef = ref(getUserInfo().phone);

// 监听storage变化
const handleStorageChange = () => {
    phoneRef.value = getUserInfo().phone;
}

uni.$on('storage-changed', handleStorageChange)

// 组件卸载时移除事件监听
onUnmounted(() => {
    uni.$off('storage-changed', handleStorageChange)
})

			
const finishGetPhoneNumber = async(params) => {
	uni.showLoading({
		title: '绑定手机号中...'
	});
	const { detail: { errMsg, code, } } = params;

	if (errMsg === 'getPhoneNumber:fail user deny') {
		return;
	}

	const userInfo = await user.bindPhoneWechat(code);
	
	setUserInfo({
		...getUserInfo(),
		phone: userInfo.phone
	});
	uni.hideLoading();
}

</script>

<style scoped>
.phone-wrapper {
	position: relative;
}

.overlay-button {
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	opacity: 0;
	z-index: 1;
}
</style>

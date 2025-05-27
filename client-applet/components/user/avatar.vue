<template>
    <view class="avatar-wrapper">
        <slot></slot>

        <button v-if="!avatar" class="overlay-button" open-type="chooseAvatar" @chooseavatar="finishChooseAvatar"></button>
    </view>
</template>

<script setup>
import {getUserInfo,setUserInfo} from '@/common/storage'
import upload from '@/common/upload'
import * as API from '@/api/index';
const {avatar} = getUserInfo()

const finishChooseAvatar = async (params) => {
	uni.showLoading({
		title: "头像上传中"
	});
	
	const filePath = params.detail.avatarUrl;
	const fileExt = filePath.split('.')[1];
	const contentType = 'image/jpeg';
	
	const fileBuffer = uni.getFileSystemManager().readFileSync(filePath)

	const url = await upload(fileBuffer, fileExt, contentType, 'avatar');
	await API.user.updateSelf({
		extension: {
			profile: url
		}
	})
	
	setUserInfo({
		...getUserInfo(),
		profile: url
	});
	
	uni.hideLoading();
}
</script>

<style scoped>
.avatar-wrapper {
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
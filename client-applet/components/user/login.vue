<template>
    <view class="login-wrapper">
        <slot></slot>

        <button v-if="!isLogin" class="overlay-button" @click="beginLogin"></button>
    </view>
</template>

<script setup>
import login from '@/common/login'
import { getToken } from '@/common/storage'

const isLogin = getToken() && props.item.includes('login')

function beginLogin() {
    login()
}
</script>

<style scoped>
.login-wrapper {
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
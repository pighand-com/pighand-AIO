<template>
    <view class="login-wrapper">
        <user-login v-if="!isLogin"><slot/></user-login>
        <user-phone v-else-if="!isPhone">{{ userInfo.phone }}<slot/></user-phone>
        <user-avatar v-else-if="!isAvatar">{{ userInfo.avatar }}<slot/></user-avatar>
		<view v-else><slot/></view>
    </view>
</template>

<script setup>
import {getToken, getUserInfo} from '@/common/storage'
import { ref, computed, onUnmounted } from 'vue'

// 将状态转换为响应式
const token = ref(getToken())
const userInfo = ref(getUserInfo())

const props = defineProps({
    item: {
        type: Array,
        default: () => ['login', 'phone', 'avatar']
    }
})

// 监听storage变化
uni.$on('storage-changed', () => {
    token.value = getToken()
    userInfo.value = getUserInfo()
})

// 组件卸载时移除事件监听
onUnmounted(() => {
    uni.$off('storage-changed')
})

// 使用计算属性来依赖响应式数据
const isLogin = computed(() => token.value && userInfo.value && props.item.includes('login'))
const isPhone = computed(() => userInfo.value?.phone && props.item.includes('phone'))
const isAvatar = computed(() => userInfo.value?.avatar && props.item.includes('avatar'))
</script>
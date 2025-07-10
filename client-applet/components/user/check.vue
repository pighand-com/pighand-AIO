<template>
    <view class="login-wrapper">
        <user-login v-if="needLogin"><slot/></user-login>
        <user-phone v-else-if="needPhone">{{ userInfo.phone }}<slot/></user-phone>
        <user-avatar v-else-if="needAvatar">{{ userInfo.avatar }}<slot/></user-avatar>
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
const handleStorageChange = () => {
    token.value = getToken()
    userInfo.value = getUserInfo()
}

uni.$on('storage-changed', handleStorageChange)

// 组件卸载时移除事件监听
onUnmounted(() => {
    uni.$off('storage-changed', handleStorageChange)
})

// 使用计算属性来依赖响应式数据
const needLogin = computed(() => (!token.value || !userInfo?.value) && props.item.includes('login'))
const needPhone = computed(() => !userInfo.value?.phone && props.item.includes('phone'))
const needAvatar = computed(() => !userInfo.value?.avatar && props.item.includes('avatar'))
</script>
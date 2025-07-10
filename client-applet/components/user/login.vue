<template>
    <view class="login-wrapper">
        <slot></slot>

        <button v-if="!isLogin" class="overlay-button" @click="beginLogin"></button>
    </view>
</template>

<script setup>
import login from '@/common/login'
import { getToken } from '@/common/storage'
import { ref, computed, onUnmounted } from 'vue'

const props = defineProps({
    item: {
        type: Array,
        default: () => ['login']
    }
})

// 使用ref创建响应式变量
const token = ref(getToken())

// 监听storage变化
const handleStorageChange = () => {
    token.value = getToken()
}

uni.$on('storage-changed', handleStorageChange)

// 组件卸载时移除事件监听
onUnmounted(() => {
    uni.$off('storage-changed', handleStorageChange)
})

// 使用计算属性计算登录状态
const isLogin = computed(() => token.value && props.item.includes('login'))

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
<template>
    <router-view
        v-if="$route.matched[0]?.meta?.pageType === constant.page_type_multi">
    </router-view>

    <div
        v-else-if="
            $route.matched[0]?.meta?.pageType === constant.page_type_single
        "
        class="dashboard">
        <div class="bg">
            <div class="g1"></div>
            <div class="g2"></div>
        </div>
        <div class="menu"><Menu /></div>
        <div class="page">
            <router-view></router-view>
        </div>
    </div>
</template>

<script setup lang="ts">
import Menu from './components/Menu.vue';
import constant from '@/common/constant';
import { getApplicationInfo } from '@/common/storage';

// 初始化应用信息
const initApplicationInfo = () => {
    const appInfo = getApplicationInfo();
    if (appInfo?.name) {
        document.title = appInfo.name;
    }
};
initApplicationInfo();
</script>

<style>
:root {
    --p-color-primary: #4682f6;
    --p-color-background: #f6f8fa;
    --p-color-white: #f8f9fa;
    --p-color-dark: #17191a;

    --el-text-color-primary: var(--p-color-dark) !important;
    --el-menu-bg-color: transparent !important;
    --el-menu-hover-bg-color: transparent !important;
    --el-menu-item-height: 48px !important;
    --el-menu-active-color: var(--p-color-white) !important;
    /* --el-color-primary: var(--p-color-primary) !important; */
}

#app {
    width: 100%;
    height: 100%;
    background-color: var(--p-color-background);
    color: var(--p-color-dark);
}

.dashboard {
    width: 100%;
    height: 100%;
    display: flex;

    .bg {
        position: absolute;
        width: 100%;
        height: 100%;
        top: 0;
        left: 0;

        .g1 {
            width: 20%;
            height: 40%;
            position: absolute;
            top: 0;
            left: 0;
            transform: translate(-50%, -50%);
            background: radial-gradient(
                    ellipse at center,
                    rgba(175, 207, 255, 1) 0,
                    rgba(175, 207, 255, 0.5) 40%,
                    transparent 60%
                )
                no-repeat;
        }

        .g2 {
            width: 40%;
            height: 80%;
            position: absolute;
            top: 0;
            left: 0;
            transform: translate(-50%, -50%);
            background: radial-gradient(
                    ellipse at center,
                    rgb(200, 200, 247) 0,
                    rgba(200, 200, 247, 0.5) 40%,
                    transparent 60%
                )
                no-repeat;
        }
    }

    .menu {
        height: 100%;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        position: relative;
        margin-left: 8px;
        width: min-content;
        align-items: center;
    }

    .page {
        position: relative;
        padding: 24px;
        width: 100%;
        overflow-y: scroll;
        position: relative;
    }
}
</style>

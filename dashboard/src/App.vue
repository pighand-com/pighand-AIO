<template>
    <el-config-provider :locale="zhCn">
        <router-view
            v-if="
                $route.matched[0]?.meta?.pageType === constant.page_type_multi
            ">
        </router-view>

        <div
            v-else-if="
                $route.matched[0]?.meta?.pageType === constant.page_type_single
            "
            class="dashboard">
            <div class="bg">
                <div class="g1"></div>
                <div class="g2"></div>
                <div class="g3"></div>
                <div class="g4"></div>
            </div>
            <div class="single-menu"><Menu /></div>
            <div class="single-page">
                <router-view></router-view>
            </div>
        </div>
    </el-config-provider>
</template>

<script setup lang="ts">
import Menu from './components/Menu.vue';
import constant from '@/common/constant';
import { getApplicationInfo } from '@/common/storage';
import zhCn from 'element-plus/es/locale/lang/zh-cn';

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
    --p-color-background: #f6f8fada;
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
        overflow: hidden;

        .g1 {
            width: 60%;
            height: 60%;
            position: absolute;
            top: 0;
            left: 0;
            transform: translate(-50%, -50%) rotate(128deg);
            background: radial-gradient(
                    ellipse 60% 70% at 40% 50%,
                    rgba(147, 197, 253, 1) 0,
                    rgba(147, 197, 253, 0.8) 20%,
                    rgba(147, 197, 253, 0.5) 35%,
                    rgba(147, 197, 253, 0.3) 45%,
                    rgba(147, 197, 253, 0.1) 60%,
                    transparent 75%
                )
                no-repeat;
        }

        .g2 {
            width: 50%;
            height: 70%;
            position: absolute;
            top: 0;
            left: 20%;
            transform: translate(-50%, -30%) rotate(140deg);
            background: radial-gradient(
                    ellipse 70% 60% at 50% 40%,
                    rgba(216, 180, 254, 1) 0,
                    rgba(216, 180, 254, 0.8) 15%,
                    rgba(216, 180, 254, 0.6) 30%,
                    rgba(216, 180, 254, 0.3) 45%,
                    rgba(216, 180, 254, 0.1) 60%,
                    transparent 70%
                )
                no-repeat;
        }

        .g3 {
            width: 80%;
            height: 90%;
            position: absolute;
            bottom: 0;
            right: 0;
            transform: translate(50%, 50%) rotate(-15deg);
            background: radial-gradient(
                    ellipse at center,
                    rgba(255, 198, 255, 1) 0,
                    rgba(255, 198, 255, 0.8) 20%,
                    rgba(255, 198, 255, 0.5) 30%,
                    rgba(255, 198, 255, 0.3) 45%,
                    rgba(255, 198, 255, 0.1) 55%,
                    transparent 70%
                )
                no-repeat;
        }

        .g4 {
            width: 60%;
            height: 70%;
            position: absolute;
            bottom: 0;
            right: 10%;
            transform: translate(30%, 40%) rotate(25deg);
            background: radial-gradient(
                    ellipse at center,
                    rgba(162, 223, 251, 1) 0,
                    rgba(162, 223, 251, 0.8) 15%,
                    rgba(162, 223, 251, 0.6) 25%,
                    rgba(162, 223, 251, 0.4) 40%,
                    rgba(162, 223, 251, 0.2) 50%,
                    rgba(162, 223, 251, 0.1) 60%,
                    transparent 75%
                )
                no-repeat;
        }
    }

    .single-menu {
        height: 100%;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        position: relative;
        margin-left: 8px;
        width: min-content;
        align-items: center;
    }

    .single-page {
        position: relative;
        padding: 24px;
        width: 100%;
        overflow-y: scroll;
    }
}
</style>

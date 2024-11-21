<template>
    <router-view
        v-if="$route.matched[0]?.meta?.pageType === constant.page_type_multi">
    </router-view>

    <div
        v-else-if="
            $route.matched[0]?.meta?.pageType === constant.page_type_single
        "
        class="dashboard">
        <div class="menu"><Menu /></div>
        <div class="page">
            <router-view></router-view>
        </div>
    </div>
</template>

<script setup lang="ts">
import Menu from './components/Menu.vue';

import constant from '@/common/constant';
</script>

<style>
#app {
    width: 100%;
    height: 100%;
    background-color: rgb(245, 245, 245);
}

.dashboard {
    width: 100%;
    height: 100%;
    display: flex;
}

.menu {
    height: 100%;
}

.page {
    padding: 24px;
    width: 100%;
    overflow-y: scroll;
}

.el-menu {
    overflow-y: scroll !important;
}
</style>

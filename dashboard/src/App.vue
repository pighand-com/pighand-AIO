<template>
    <router-view
        v-if="
            Object.keys($route.matched[0]?.components || {}).includes(
                constant.page_type_multi
            )
        "
        :name="constant.page_type_multi">
    </router-view>

    <div
        v-else-if="
            Object.keys($route.matched[0]?.components || {}).includes(
                constant.page_type_single
            )
        "
        class="dashboard">
        <div class="menu"><Menu /></div>
        <div class="page">
            <router-view :name="constant.page_type_single"></router-view>
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
}

.el-menu {
    overflow-y: scroll !important;
}
</style>

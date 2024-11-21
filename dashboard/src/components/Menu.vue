<template>
    <el-menu
        class="el-menu-vertical"
        :collapse="isCollapse"
        :default-active="$route.path"
        router>
        <div
            class="info"
            :style="
                isCollapse
                    ? { justifyContent: 'center' }
                    : { justifyContent: 'space-between' }
            ">
            <el-popover :width="240" v-if="!isCollapse" :show-after="500">
                <template #reference>
                    <div class="avatar">
                        <el-avatar
                            :size="32"
                            src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
                        {{ userInfo?.username }}
                    </div>
                </template>
                <template #default>
                    <div class="user-info">
                        <div class="user-detail">
                            <el-avatar
                                :size="60"
                                src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
                                style="margin-bottom: 8px" />
                            <div class="user-detail-text">
                                <div class="user-detail-text-title">
                                    {{ userInfo?.username }}
                                </div>
                                <div>
                                    {{ constant.role_mapping[userInfo?.role] }}
                                </div>
                            </div>
                        </div>
                        <div class="user-buttons">
                            <el-button class="user-button" type="primary" plain
                                >修改密码</el-button
                            >
                            <el-button
                                class="user-button"
                                type="danger"
                                @click="confirmEvent"
                                plain
                                >退出</el-button
                            >
                        </div>
                    </div>
                </template>
            </el-popover>

            <el-button @click="changeCollapse" type="primary" link>
                <el-icon v-if="isCollapse" size="20"><Expand /></el-icon>
                <el-icon v-if="!isCollapse" size="20"><Fold /></el-icon>
            </el-button>
        </div>

        <el-menu-item
            v-for="(item, index) in menus"
            :key="index"
            :index="item.path">
            <el-icon><component :is="item.icon"></component></el-icon>
            <template #title>{{ item.title }}</template>
        </el-menu-item>
    </el-menu>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { Fold, Expand } from '@element-plus/icons-vue';
import router from '@/routers/index';
import routes from '@/routers/routes';
import { clearToken, getUserInfo } from '@/common/storage';
import constant from '@/common/constant';

const isCollapse = ref(false);
const menus = ref(
    routes
        .filter((item) => item.meta?.pageType === constant.page_type_single)
        .map((item, index) => {
            return {
                path: item.path,
                title: item.title,
                icon: item.meta.icon,
                index
            };
        })
);
const userInfo: any = getUserInfo();

/**
 * 退出
 */
const confirmEvent = () => {
    clearToken();

    router.push('/login');
};

const changeCollapse = () => {
    isCollapse.value = !isCollapse.value;
};
</script>

<style scoped>
.info {
    padding: 8px 16px;
    height: 40px;
    background: #fcfcfc;
    display: flex;
}

.avatar {
    display: flex;
    align-items: center;
    font-weight: bolder;
    color: rgb(64, 158, 255);
}

.user-info {
    gap: 16px;
    display: flex;
    flex-direction: column;
}

.user-detail {
    display: flex;
}

.user-detail-text {
    padding-left: 16px;
    display: flex;
    flex-direction: column;
    justify-content: center;
}

.user-detail-text-title {
    font-size: 24px;
    font-weight: bolder;
}

.user-buttons {
    display: flex;
    justify-content: space-between;
}

.user-button {
    width: 96px;
}

.el-menu-vertical {
    height: 100%;
    position: relative;
    box-shadow: 0px 0px 12px rgba(0, 0, 0, 0.12);
    border-right: 0;
}
.el-menu-vertical:not(.el-menu--collapse) {
    width: 200px;
}
</style>

<template>
    <div class="logo">
        <img src="@/assets/logo.png" alt="logo" />
    </div>

    <el-menu
        class="el-menu-vertical"
        :collapse="isCollapse"
        :default-active="$route.path"
        router>
        <template v-for="(item, index) in menus" :key="index">
            <!-- 没有子菜单的情况 -->
            <el-menu-item v-if="!item.children?.length" :index="item.path">
                <el-icon><component :is="item.icon"></component></el-icon>
                <template #title>{{ item.title }}</template>
            </el-menu-item>

            <!-- 有子菜单的情况 -->
            <el-sub-menu v-else :index="item.path">
                <template #title>
                    <el-icon><component :is="item.icon"></component></el-icon>
                    <span>{{ item.title }}</span>
                </template>
                <el-menu-item
                    v-for="(child, childIndex) in item.children"
                    :key="childIndex"
                    :index="`${item.path}/${child.path}`">
                    <el-icon v-if="child.icon"
                        ><component :is="child.icon"></component
                    ></el-icon>
                    <template #title>{{ child.title }}</template>
                </el-menu-item>
            </el-sub-menu>
        </template>
    </el-menu>

    <div class="user-info" :class="{ 'is-collapse': isCollapse }">
        <el-popover :show-after="500" width="auto">
            <template #reference>
                <div class="avatar">
                    <el-avatar
                        :size="32"
                        src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
                    <div v-show="!isCollapse" class="username">
                        {{ userInfo?.username }}
                    </div>
                </div>
            </template>
            <template #default>
                <div class="user-info-popover">
                    <div class="user-detail">
                        <el-avatar
                            :size="60"
                            src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
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

        <el-button
            @click="changeCollapse"
            :icon="isCollapse ? ArrowRightBold : ArrowLeftBold"
            color="var(--p-color-dark)"
            circle
            size="small" />
    </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { ArrowRightBold, ArrowLeftBold } from '@element-plus/icons-vue';
import router from '@/routers/index';
import routes from '@/routers/routes';
import { clearToken, getUserInfo } from '@/common/storage';
import constant from '@/common/constant';
import { ElCollapseTransition } from 'element-plus';

const isCollapse = ref(false);
const menus = ref(
    routes
        .filter((item) => item.meta?.pageType === constant.page_type_single)
        .map((item) => {
            return {
                path: item.path,
                title: item.title,
                icon: item.meta.icon,
                children: item.children?.map((child) => ({
                    path: child.path,
                    title: child.title,
                    icon: child.meta?.icon
                }))
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
.logo {
    display: flex;
    justify-content: center;
    padding: 24px 0;
    width: 100%;

    img {
        width: 24px;
    }
}

.user-info {
    padding: 12px 8px;
    background: #fff;
    border-radius: 16px;
    margin-bottom: 8px;
    border: solid 1px #e4e4e7;
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 16px;
    width: fit-content;
}

.user-info.is-collapse {
    flex-direction: column;
}

.avatar {
    display: flex;
    align-items: center;
    gap: 8px;
    cursor: pointer;
}

.username {
    font-size: 14px;
    color: var(--el-text-color-primary);
}

.user-info-popover {
    gap: 16px;
    display: flex;
    flex-direction: column;

    .user-detail {
        display: flex;

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
    }

    .user-buttons {
        display: flex;
        justify-content: space-between;

        .user-button {
            width: 96px;
        }
    }
}

.el-menu-vertical {
    height: 100%;
    position: relative;
    background: transparent;
    border: 0;
    color: var(--p-color-dark);
}

.el-menu-item {
    height: 48px;
    margin: 4px 0;
    border-radius: 32px;
}

.el-menu-item:hover {
    border: solid 1px var(--p-color-dark);
    background: var(--p-color-white);
}

.el-menu-item.is-active {
    background: var(--p-color-dark) !important;
    color: var(--p-color-white) !important;
}

.el-sub-menu {
    li {
        padding-left: 20px !important;
        margin-left: 20px !important;
    }
}

.el-menu--collapse {
    width: 48px;

    li {
        padding-left: 0px !important;
        margin-left: 0px !important;
        display: flex;
        justify-content: center;
    }

    :deep(.el-menu-tooltip__trigger) {
        display: flex !important;
        justify-content: center !important;
    }

    .is-active {
        background: var(--p-color-dark) !important;
        border-radius: 48px;
    }
}
</style>

<style>
.el-menu--popup {
    padding: 8px !important;
}
</style>

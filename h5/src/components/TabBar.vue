<template>
    <div class="tabbar">
        <div 
            v-for="tab in tabs" 
            :key="tab.name"
            class="tab-item"
            :class="{ active: currentTab === tab.name }"
            @click="switchTab(tab.name)"
        >
            <div class="tab-icon">
                <component :is="tab.icon" :size="24" />
            </div>
            <span class="tab-label">{{ tab.label }}</span>
        </div>
    </div>
</template>

<script lang="ts" setup>
import { computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { Home, ApplicationTwo, User } from '@icon-park/vue-next';

const router = useRouter();
const route = useRoute();

// 底部导航配置
const tabs = [
    {
        name: 'home',
        label: '首页',
        icon: Home,
        path: '/home'
    },
    {
        name: 'category',
        label: '分类',
        icon: ApplicationTwo,
        path: '/classification'
    },
    {
        name: 'profile',
        label: '我的',
        icon: User,
        path: '/profile'
    }
];

/**
 * 当前激活的tab
 */
const currentTab = computed(() => {
    const currentPath = route.path;
    const activeTab = tabs.find(tab => currentPath.startsWith(tab.path));
    return activeTab?.name || 'home';
});

/**
 * 切换tab
 * @param tabName tab名称
 */
const switchTab = (tabName: string) => {
    const tab = tabs.find(t => t.name === tabName);
    if (tab && route.path !== tab.path) {
        router.push(tab.path);
    }
};
</script>

<style scoped>
.tabbar {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    height: 60px;
    background: white;
    border-top: 1px solid #eee;
    display: flex;
    align-items: center;
    justify-content: space-around;
    z-index: 1000;
    padding-bottom: env(safe-area-inset-bottom);
    padding-top: 8px;
}

.tab-item {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    transition: all 0.2s;
    padding: 8px 4px;
}

.tab-item:active {
    transform: scale(0.95);
}

.tab-icon {
    margin-bottom: 4px;
    color: #999;
    transition: color 0.2s;
}

.tab-label {
    font-size: 12px;
    color: #999;
    transition: color 0.2s;
}

.tab-item.active .tab-icon,
.tab-item.active .tab-label {
    color: var(--p-color-green-primary);
}

/* 适配iPhone X及以上设备的安全区域 */
@supports (padding: max(0px)) {
    .tabbar {
        padding-bottom: max(8px, env(safe-area-inset-bottom));
        padding-top: 8px;
    }
}
</style>
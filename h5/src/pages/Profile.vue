<template>
    <div class="profile-page">
        <!-- 用户信息区域 -->
        <div class="user-info-section">
            <div class="user-avatar">
                <img :src="userInfo.avatar || '/default-avatar.jpg'" :alt="userInfo.nickname" />
            </div>
            <div class="user-details">
                <h3 class="user-name">{{ userInfo.nickname || userInfo.username || '未知用户' }}</h3>
                <p class="user-department">{{ userInfo.department || '暂无部门信息' }}</p>
            </div>
        </div>

        <!-- 功能菜单区域 -->
        <div class="menu-section">
            <div class="menu-item" @click="goToDownloadList">
                <div class="menu-icon">
                    <Download />
                </div>
                <div class="menu-content">
                    <span class="menu-title">我的下载</span>
                    <span class="menu-subtitle">查看下载记录</span>
                </div>
                <div class="menu-arrow">
                    <Right />
                </div>
            </div>

            <div class="menu-item" @click="goToFavoriteList">
                <div class="menu-icon">
                    <Star />
                </div>
                <div class="menu-content">
                    <span class="menu-title">我的收藏</span>
                    <span class="menu-subtitle">查看收藏的素材</span>
                </div>
                <div class="menu-arrow">
                    <Right />
                </div>
            </div>

            <div class="menu-item" @click="goToSettings">
                <div class="menu-icon">
                    <Setting />
                </div>
                <div class="menu-content">
                    <span class="menu-title">设置</span>
                    <span class="menu-subtitle">个人设置</span>
                </div>
                <div class="menu-arrow">
                    <Right />
                </div>
            </div>
        </div>

        <!-- 统计信息区域 -->
        <div class="stats-section">
            <div class="stats-item">
                <div class="stats-number">{{ stats.downloadCount || 0 }}</div>
                <div class="stats-label">下载数量</div>
            </div>
            <div class="stats-item">
                <div class="stats-number">{{ stats.favoriteCount || 0 }}</div>
                <div class="stats-label">收藏数量</div>
            </div>
            <div class="stats-item">
                <div class="stats-number">{{ stats.viewCount || 0 }}</div>
                <div class="stats-label">浏览数量</div>
            </div>
        </div>

        <!-- 退出登录按钮 -->
        <div class="logout-section">
            <el-button type="danger" plain @click="handleLogout" class="logout-btn">
                退出登录
            </el-button>
        </div>
    </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessageBox, ElMessage } from 'element-plus';
import { Download, Star, Setting, Right } from '@icon-park/vue-next';
import { getUserInfo } from '@/common/storage';
import { useAuth, Permission } from '@/common/auth';
import * as API from '@/api';

const router = useRouter();
const { logout, hasPermission, getCurrentUser } = useAuth();

// 响应式数据
const userInfo = ref<any>({});
const stats = ref({
    downloadCount: 0,
    favoriteCount: 0,
    viewCount: 0
});

/**
 * 跳转到下载记录页面
 */
const goToDownloadList = () => {
    router.push({ name: 'downloadList' });
};

/**
 * 跳转到收藏列表页面
 */
const goToFavoriteList = () => {
    router.push({ name: 'favoriteList' });
};

/**
 * 跳转到设置页面
 */
const goToSettings = () => {
    router.push({ name: 'settings' });
};

/**
 * 处理退出登录
 */
const handleLogout = async () => {
    try {
        await ElMessageBox.confirm(
            '确定要退出登录吗？',
            '提示',
            {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
            }
        );

        // 调用退出登录API
        await API.login.logout();
        
        // 使用权限管理模块的退出登录方法
        logout();
        
        ElMessage.success('已退出登录');
    } catch (error) {
        console.error('退出登录失败:', error);
        // 即使API调用失败，也执行本地退出
        logout();
        ElMessage.success('已退出登录');
    }
};

/**
 * 加载用户统计信息
 */
const loadUserStats = async () => {
    try {
        // 获取用户信息
        const userResponse = await API.login.getCurrentUser();
        if (userResponse.data) {
            userInfo.value = { ...userInfo.value, ...userResponse.data };
        }
        
        // 获取收藏数量
        const favoriteResponse = await API.assetsFavourite.query({ size: 1 });
        const favoriteCount = favoriteResponse.total || 0;
        
        // 更新统计信息
        stats.value = {
            downloadCount: userInfo.value.downloadCount || 0,
            favoriteCount: favoriteCount,
            viewCount: userInfo.value.viewCount || 0
        };
    } catch (error) {
        console.error('加载用户统计失败:', error);
        // 使用默认值
        stats.value = {
            downloadCount: 0,
            favoriteCount: 0,
            viewCount: 0
        };
    }
};

/**
 * 初始化页面数据
 */
const initData = () => {
    // 获取用户信息
    userInfo.value = getUserInfo() || {};
    
    // 加载统计信息
    loadUserStats();
};

onMounted(() => {
    initData();
});
</script>

<style scoped>
.profile-page {
    background-color: #f5f5f5;
    min-height: 100vh;
    padding-bottom: 80px; /* 为底部导航留出空间 */
}

/* 用户信息区域 */
.user-info-section {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    padding: 40px 20px 30px;
    display: flex;
    align-items: center;
    color: white;
}

.user-avatar {
    width: 80px;
    height: 80px;
    border-radius: 50%;
    overflow: hidden;
    margin-right: 20px;
    border: 3px solid rgba(255, 255, 255, 0.3);
}

.user-avatar img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.user-details {
    flex: 1;
}

.user-name {
    margin: 0 0 8px 0;
    font-size: 22px;
    font-weight: 600;
}

.user-department {
    margin: 0;
    font-size: 14px;
    opacity: 0.9;
}

/* 功能菜单区域 */
.menu-section {
    background: white;
    margin: 16px;
    border-radius: 12px;
    overflow: hidden;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.menu-item {
    display: flex;
    align-items: center;
    padding: 20px;
    border-bottom: 1px solid #f0f0f0;
    cursor: pointer;
    transition: background-color 0.2s;
}

.menu-item:last-child {
    border-bottom: none;
}

.menu-item:hover {
    background-color: #f8f9fa;
}

.menu-item:active {
    background-color: #f0f0f0;
}

.menu-icon {
    width: 40px;
    height: 40px;
    border-radius: 8px;
    background: #f0f8ff;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 16px;
    font-size: 20px;
    color: #409eff;
}

.menu-content {
    flex: 1;
}

.menu-title {
    display: block;
    font-size: 16px;
    color: #333;
    margin-bottom: 4px;
}

.menu-subtitle {
    font-size: 12px;
    color: #999;
}

.menu-arrow {
    color: #ccc;
    font-size: 14px;
}

/* 统计信息区域 */
.stats-section {
    background: white;
    margin: 16px;
    border-radius: 12px;
    padding: 20px;
    display: flex;
    justify-content: space-around;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.stats-item {
    text-align: center;
}

.stats-number {
    font-size: 24px;
    font-weight: 600;
    color: #409eff;
    margin-bottom: 4px;
}

.stats-label {
    font-size: 12px;
    color: #999;
}

/* 退出登录区域 */
.logout-section {
    padding: 20px;
}

.logout-btn {
    width: 100%;
    height: 48px;
    font-size: 16px;
    border-radius: 24px;
}
</style>
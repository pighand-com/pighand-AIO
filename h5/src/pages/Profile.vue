<template>
    <div class="profile-page">
        <!-- 用户信息区域 --> 
        <div class="user-info-section">
            <div class="user-avatar" @click="handleAvatarClick">
                <img :src="avatarUrl" :alt="userInfo.nickname || '用户头像'" />
                <div class="avatar-overlay">
                    <span class="avatar-edit-text">点击更换</span>
                </div>
            </div>
            <div class="user-details">
                <h3 class="user-name">{{ userInfo?.extension?.name || userInfo.username || '未知用户' }}</h3>
                <p class="user-department">{{ departmentInfo }}</p>
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
                    <span class="menu-title">个人信息</span>
                    <span class="menu-subtitle">查看个人信息</span>
                </div>
                <div class="menu-arrow">
                    <Right />
                </div>
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
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessageBox, ElMessage } from 'element-plus';
import { Download, Star, Setting, Right } from '@icon-park/vue-next';
import { getUserInfo, setUserInfo } from '@/common/storage';
import { useAuth, Permission } from '@/common/auth';
import * as API from '@/api';
import defaultAvatarUrl from '@/assets/default-avatar.svg';
import { uploadFile, selectImageFile } from '@/utils/upload';

const router = useRouter();
const { logout, hasPermission, getCurrentUser } = useAuth();

// 响应式数据
const userInfo = ref<any>({});
const departmentInfo = ref<string>('暂无部门信息');

/**
 * 获取用户头像URL，如果没有头像则使用默认头像
 */
const avatarUrl = computed(() => {
    return userInfo.value.extension?.profile || userInfo.value.avatar || defaultAvatarUrl;
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
 * 处理头像点击上传
 */
const handleAvatarClick = async () => {
    try {
        // 选择图片文件
        const file = await selectImageFile('image/*');
        
        // 显示上传进度
        ElMessage.info('正在上传头像...');
        
        // 上传文件到COS
        const avatarUrl = await uploadFile(file, 'avatar', (progress) => {
            console.log('上传进度:', progress + '%');
        });
        
        // 更新用户信息
        const updateData = {
            extension: {
                ...userInfo.value.extension,
                profile: avatarUrl
            }
        };
        
        await API.user.updateUserInfo(updateData);
        
        // 更新本地用户信息
        userInfo.value.extension = {
            ...userInfo.value.extension,
            profile: avatarUrl
        };
        
        // 将更新后的用户信息回写到localStorage
        const currentUserInfo = getUserInfo() || {};
        currentUserInfo.extension = {
            ...currentUserInfo.extension,
            profile: avatarUrl
        };
        setUserInfo(currentUserInfo);
        
        ElMessage.success('头像更新成功');
    } catch (error) {
        console.error('头像上传失败:', error);
        ElMessage.error('头像上传失败，请重试');
    }
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

        // 直接使用权限管理模块的退出登录方法
        logout();
        
        ElMessage.success('已退出登录');
    } catch (error) {
        console.error('退出登录失败:', error);
        // 即使出现错误，也执行本地退出
        logout();
        ElMessage.success('已退出登录');
    }
};

/**
 * 获取组织架构信息
 */
const fetchDepartmentInfo = async () => {
    try {
        const response = await API.orgDepartment.getMyDepartmentSimple();
        if (response) {
            // 解析嵌套的组织架构数据，将child结构转换为部门链数组
            const departments: any[] = [];
            let current = response;
            
            // 遍历嵌套结构，构建部门链
            while (current) {
                departments.push({
                    id: current.id,
                    name: current.name
                });
                current = current.child;
            }
            
            // 只显示倒数2级，用"-"分割
            if (departments.length >= 2) {
                const lastTwo = departments.slice(-2);
                departmentInfo.value = lastTwo.map((dept: any) => dept.name).join(' - ');
            } else if (departments.length === 1) {
                departmentInfo.value = departments[0].name;
            } else {
                departmentInfo.value = '暂无部门信息';
            }
        } else {
            departmentInfo.value = '暂无部门信息';
        }
    } catch (error) {
        console.error('获取组织架构信息失败:', error);
        departmentInfo.value = '暂无部门信息';
    }
};

/**
 * 初始化页面数据
 */
const initData = async () => {
    // 获取用户信息
    userInfo.value = getUserInfo() || {};
    
    // 获取组织架构信息
    await fetchDepartmentInfo();
};

onMounted(() => {
    initData();
});
</script>

<style scoped>
.profile-page {
    background-color: #f8fafb;
    min-height: 100vh;
    padding-bottom: 80px; /* 为底部导航留出空间 */
}

/* 用户信息区域 */
.user-info-section {
    background: linear-gradient(135deg, var(--p-color-green-primary, #5ebd31) 0%, #4ade80 100%);
    padding: 40px 20px 30px;
    display: flex;
    align-items: center;
    color: white;
    border-radius: 0 0 24px 24px;
    box-shadow: 0 4px 20px rgba(94, 189, 49, 0.2);
}

.user-avatar {
    width: 80px;
    height: 80px;
    border-radius: 50%;
    overflow: hidden;
    margin-right: 20px;
    border: 3px solid rgba(255, 255, 255, 0.3);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    position: relative;
    cursor: pointer;
    transition: transform 0.2s ease;
}

.user-avatar:hover {
    transform: scale(1.05);
}

.user-avatar img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.avatar-overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    align-items: center;
    justify-content: center;
    opacity: 0;
    transition: opacity 0.2s ease;
    border-radius: 50%;
}

.user-avatar:hover .avatar-overlay {
    opacity: 1;
}

.avatar-edit-text {
    color: white;
    font-size: 12px;
    font-weight: 500;
    text-align: center;
}

.user-details {
    flex: 1;
}

.user-name {
    margin: 0 0 8px 0;
    font-size: 22px;
    font-weight: 600;
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.user-department {
    margin: 0;
    font-size: 14px;
    opacity: 0.9;
}

/* 功能菜单区域 */
.menu-section {
    background: white;
    margin: 20px 16px;
    border-radius: 16px;
    overflow: hidden;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.menu-item {
    display: flex;
    align-items: center;
    padding: 20px;
    border-bottom: 1px solid #f5f5f5;
    cursor: pointer;
    transition: all 0.3s ease;
}

.menu-item:last-child {
    border-bottom: none;
}

.menu-item:hover {
    background-color: rgba(94, 189, 49, 0.05);
    transform: translateX(4px);
}

.menu-item:active {
    background-color: rgba(94, 189, 49, 0.1);
}

.menu-icon {
    width: 44px;
    height: 44px;
    border-radius: 12px;
    background: linear-gradient(135deg, rgba(94, 189, 49, 0.1) 0%, rgba(74, 222, 128, 0.1) 100%);
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 16px;
    font-size: 22px;
    color: var(--p-color-green-primary, #5ebd31);
    transition: all 0.3s ease;
}

.menu-item:hover .menu-icon {
    background: linear-gradient(135deg, rgba(94, 189, 49, 0.2) 0%, rgba(74, 222, 128, 0.2) 100%);
    transform: scale(1.05);
}

.menu-content {
    flex: 1;
}

.menu-title {
    display: block;
    font-size: 16px;
    color: #333;
    margin-bottom: 4px;
    font-weight: 500;
}

.menu-subtitle {
    font-size: 13px;
    color: #999;
}

.menu-arrow {
    color: #ccc;
    font-size: 16px;
    transition: all 0.3s ease;
}

.menu-item:hover .menu-arrow {
    color: var(--p-color-green-primary, #5ebd31);
    transform: translateX(4px);
}

/* 退出登录区域 */
.logout-section {
    padding: 20px 16px;
}

.logout-btn {
    width: 100%;
    height: 48px;
    font-size: 16px;
    border-radius: 24px;
    border: 2px solid #ff4757;
    color: #ff4757;
    background: white;
    font-weight: 500;
    transition: all 0.3s ease;
    box-shadow: 0 2px 8px rgba(255, 71, 87, 0.1);
}

.logout-btn:hover {
    background: #ff4757;
    color: white;
    transform: translateY(-2px);
    box-shadow: 0 4px 16px rgba(255, 71, 87, 0.3);
}

.logout-btn:active {
    transform: translateY(0);
}
</style>
<template>
    <div class="favorite-list-page">
        <!-- 统一的头部区域 -->
        <div class="header-section">
            <!-- 导航条 -->
            <div class="nav-bar">
                <div class="nav-left">
                    <div class="back-btn" @click="goBack">
                        <svg class="back-icon" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M15 18L9 12L15 6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                    </div>
                </div>
                <div class="nav-center">
                    <h1 class="nav-title">我的收藏</h1>
                </div>
                <div class="nav-right">
                    <!-- 预留右侧操作区域 -->
                </div>
            </div>

            <!-- 筛选区域 -->
            <div class="filter-section">
                <!-- 筛选条件 -->
                <div class="filter-container">
                    <div class="filter-tabs">
                        <div 
                            v-for="tab in filterTabs" 
                            :key="tab.value"
                            class="filter-tab"
                            :class="{ active: searchForm.assetsType === tab.value }"
                            @click="selectFilterTab(tab.value)"
                        >
                            {{ tab.label }}
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- 收藏列表 -->
        <div class="favorite-content">
            <div v-if="loading" class="loading-container">
                <el-loading :loading="true" />
            </div>
            
            <div v-else-if="favoriteList.length === 0" class="empty-container">
                <p class="empty-text">暂无收藏记录</p>
            </div>
            
            <div v-else class="assets-grid">
                <div 
                    v-for="item in favoriteList" 
                    :key="item.id"
                    class="asset-item"
                    @click="goToAssetDetail(item)"
                >
                    <!-- 图片素材 -->
                    <div v-if="getAssetType(item.assetsType) === 'image'" class="asset-thumbnail">
                        <img :src="getCoverImage(item)" :alt="item.title" />
                        <!-- 文件类型标签 -->
                        <div class="file-type-badge">
                            <component :is="getAssetTypeIcon(getAssetType(item.assetsType))" class="type-icon" />
                            <span class="file-format">{{ getFileFormat(item.fileFormat) }}</span>
                        </div>
                    </div>
                    
                    <!-- 视频素材 -->
                    <div v-else-if="getAssetType(item.assetsType) === 'video'" class="asset-thumbnail video-thumbnail">
                        <img :src="getCoverImage(item)" :alt="item.title" />
                        <div v-if="item.duration" class="duration">
                            {{ formatDuration(item.duration) }}
                        </div>
                        <!-- 文件类型标签 -->
                        <div class="file-type-badge">
                            <component :is="getAssetTypeIcon(getAssetType(item.assetsType))" class="type-icon" />
                            <span class="file-format">{{ getFileFormat(item.fileFormat) }}</span>
                        </div>
                    </div>
                    
                    <!-- 文档素材 -->
                    <div v-else class="asset-thumbnail doc-thumbnail">
                        <img v-if="getCoverImage(item)" :src="getCoverImage(item)" :alt="item.title" />
                        <FileDoc v-else class="doc-icon" />
                        <!-- 文件类型标签 -->
                        <div class="file-type-badge">
                            <component :is="getAssetTypeIcon(getAssetType(item.assetsType))" class="type-icon" />
                            <span class="file-format">{{ getFileFormat(item.fileFormat) }}</span>
                        </div>
                    </div>
                    
                    <div class="asset-info">
                        <p class="asset-title">{{ item.title }}</p>
                        <div class="favorite-time">
                            收藏：{{ formatDate(item.createdAt) }}
                        </div>
                    </div>
                </div>
            </div>

            <!-- 加载状态和提示 -->
            <div class="load-status">
                <!-- 加载更多按钮 -->
                <div v-if="hasMore && !loading && favoriteList.length > 0" class="load-more">
                    <el-button @click="loadMore" :loading="loadingMore">
                        {{ loadingMore ? '加载中...' : '加载更多' }}
                    </el-button>
                </div>
                
                <!-- 加载中状态 -->
                <div v-if="loadingMore" class="loading-more">
                    <div class="loading-spinner"></div>
                    <span>正在加载更多...</span>
                </div>
                
                <!-- 没有更多内容提示 -->
                <div v-if="!hasMore && favoriteList.length > 0" class="no-more">
                    <span>没有更多内容了</span>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';
import { Picture, Video, FileDoc } from '@icon-park/vue-next';
import * as API from '@/api';

const router = useRouter();

// 响应式数据
const loading = ref(false);
const loadingMore = ref(false);
const favoriteList = ref([]);
const hasMore = ref(true);
const currentPage = ref(1);

// 搜索表单
const searchForm = reactive({
    assetsType: null, // 素材类型：10图片 20视频 30文档
});

// 筛选选项
const filterTabs = [
    { label: '全部', value: null },
    { label: '图片', value: 10 },
    { label: '视频', value: 20 },
    { label: '文档', value: 30 }
];

/**
 * 获取素材类型图标
 * @param type 素材类型
 */
const getAssetTypeIcon = (type: string) => {
    const iconMap = {
        image: Picture,
        video: Video,
        doc: FileDoc
    };
    return iconMap[type] || FileDoc;
};

/**
 * 将数字类型转换为字符串类型
 * @param assetsType 数字类型：10图片 20视频 30文档
 */
const getAssetType = (assetsType: number) => {
    const typeMap = {
        10: 'image',
        20: 'video', 
        30: 'doc'
    };
    return typeMap[assetsType] || 'doc';
};

/**
 * 获取文件格式显示文本
 * @param fileFormat 文件格式
 */
const getFileFormat = (fileFormat: string) => {
    if (!fileFormat) return '';
    return fileFormat.toUpperCase();
};

/**
 * 格式化日期
 * @param dateStr 日期字符串
 */
const formatDate = (dateStr: string) => {
    if (!dateStr) return '';
    const date = new Date(dateStr);
    return date.toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
    });
};

/**
 * 选择筛选标签
 * @param assetsType 素材类型
 */
const selectFilterTab = (assetsType: number | null) => {
    searchForm.assetsType = assetsType;
    resetAndSearch();
};

/**
 * 返回上一页
 */
const goBack = () => {
    router.back();
};

/**
 * 重置并搜索
 */
const resetAndSearch = () => {
    currentPage.value = 1;
    favoriteList.value = [];
    hasMore.value = true;
    loadFavoriteList();
};

/**
 * 加载更多
 */
const loadMore = () => {
    if (hasMore.value && !loadingMore.value) {
        currentPage.value++;
        loadFavoriteList(true);
    }
};

/**
 * 滚动监听函数
 */
const handleScroll = () => {
    // 防抖处理
    if (loadingMore.value || !hasMore.value) return;
    
    const scrollTop = window.pageYOffset || document.documentElement.scrollTop;
    const windowHeight = window.innerHeight;
    const documentHeight = document.documentElement.scrollHeight;
    
    // 当滚动到距离底部100px时触发加载更多
    if (scrollTop + windowHeight >= documentHeight - 100) {
        loadMore();
    }
};

/**
 * 跳转到素材详情
 * @param item 收藏记录项
 */
const goToAssetDetail = (item: any) => {
    router.push({
        name: 'assetDetail',
        params: { 
            id: item.assetsId, 
            type: getAssetType(item.assetsType)
        }
    });
};

/**
 * 格式化时长
 * @param duration 时长（秒）
 */
const formatDuration = (duration: number) => {
    const minutes = Math.floor(duration / 60);
    const seconds = duration % 60;
    return `${minutes}:${seconds.toString().padStart(2, '0')}`;
};

/**
 * 获取封面图片URL，优先使用coverUrl，如果没有则fallback到url
 * @param item 素材项
 */
const getCoverImage = (item: any) => {
    return item.coverUrl || item.url || '';
};

/**
 * 加载收藏列表
 * @param isLoadMore 是否为加载更多
 */
const loadFavoriteList = async (isLoadMore = false) => {
    try {
        if (isLoadMore) {
            loadingMore.value = true;
        } else {
            loading.value = true;
        }

        const params: any = {
            pageNumber: currentPage.value,
            pageSize: 20,
        };

        // 如果有选择素材类型，则添加到参数中
        if (searchForm.assetsType !== null) {
            params.assetsType = searchForm.assetsType;
        }

        // 调用收藏列表API
        const response = await API.assetsFavourite.getFavoriteList(params);

        const newItems = response.records || [];
        
        if (isLoadMore) {
            favoriteList.value.push(...newItems);
        } else {
            favoriteList.value = newItems;
        }

        // 更新分页信息
        if (response.page) {
            // 判断是否还有更多数据
            hasMore.value = currentPage.value < response.page.totalPage;
        } else {
            // 兼容旧的判断方式
            hasMore.value = newItems.length >= 20;
        }

    } catch (error) {
        console.error('加载收藏列表失败:', error);
    } finally {
        loading.value = false;
        loadingMore.value = false;
    }
};

/**
 * 初始化页面数据
 */
const initData = () => {
    // 重置滚动位置到顶部
    window.scrollTo(0, 0);
    
    // 加载数据
    loadFavoriteList();
};

onMounted(() => {
    initData();
    // 添加滚动监听
    window.addEventListener('scroll', handleScroll);
});

onUnmounted(() => {
    // 清理滚动监听
    window.removeEventListener('scroll', handleScroll);
});
</script>

<style scoped>
.favorite-list-page {
    background-color: #f5f5f5;
    min-height: 100vh;
    padding-bottom: 80px;
}

/* 统一的头部区域样式 */
.header-section {
    background: white;
    position: sticky;
    top: 0;
    z-index: 100;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
    border-radius: 0 0 16px 16px;
    overflow: hidden;
}

/* 导航条样式 */
.nav-bar {
    display: flex;
    align-items: center;
    justify-content: space-between;
    height: 56px;
    padding: 0 16px;
    background: linear-gradient(135deg, #ffffff 0%, #f8fffe 100%);
}

.nav-left {
    flex: 0 0 40px;
    width: 40px;
    display: flex;
    align-items: center;
    justify-content: center;
}

.nav-center {
    flex: 1;
    display: flex;
    justify-content: center;
    align-items: center;
}

.nav-right {
    flex: 0 0 40px;
    width: 40px;
    display: flex;
    align-items: center;
    justify-content: center;
}

.back-btn {
    width: 40px;
    height: 40px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    transition: all 0.3s ease;
    background: transparent;
}

.back-btn:hover {
    transform: scale(1.1);
}

.back-btn:active {
    transform: scale(0.9);
}

.back-icon {
    width: 24px;
    height: 24px;
    color: var(--p-color-green-primary);
}

.nav-title {
    margin: 0;
    font-size: 18px;
    font-weight: 600;
    color: #333;
    text-align: center;
}

/* 筛选区域样式 */
.filter-section {
    padding: 12px 16px 16px;
    background: linear-gradient(135deg, #f0fdf4 0%, #ffffff 100%);
}

.filter-container {
    margin-bottom: 8px;
}

.filter-tabs {
    display: flex;
    gap: 8px;
    justify-content: center;
    flex-wrap: wrap;
}

.filter-tab {
    padding: 8px 16px;
    border-radius: 20px;
    background: rgba(94, 189, 49, 0.1);
    color: var(--p-color-green-primary);
    font-size: 14px;
    font-weight: 500;
    cursor: pointer;
    transition: all 0.3s ease;
    border: 2px solid transparent;
    min-width: 60px;
    text-align: center;
}

.filter-tab.active {
    background: linear-gradient(135deg, var(--p-color-green-primary) 0%, #4ade80 100%);
    color: white;
    border-color: var(--p-color-green-primary);
    box-shadow: 0 4px 12px rgba(94, 189, 49, 0.3);
    transform: translateY(-2px);
}

.filter-tab:hover {
    background: rgba(94, 189, 49, 0.2);
    transform: translateY(-1px);
    box-shadow: 0 2px 8px rgba(94, 189, 49, 0.2);
}

.filter-tab.active:hover {
    background: linear-gradient(135deg, var(--p-color-green-hover) 0%, #22c55e 100%);
    transform: translateY(-2px);
}

/* 内容区域 */
.favorite-content {
    padding: 20px 16px;
    background: linear-gradient(135deg, #f8fffe 0%, #f5f5f5 100%);
    min-height: calc(100vh - 200px);
}

.loading-container {
    height: 200px;
    display: flex;
    align-items: center;
    justify-content: center;
    background: white;
    border-radius: 16px;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
}

.empty-container {
    text-align: center;
    padding: 60px 20px;
}

.empty-text {
    color: #999;
    font-size: 16px;
    margin: 0;
}

/* 素材网格 */
.assets-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;
}

.asset-item {
    background: white;
    border-radius: 12px;
    overflow: hidden;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
    cursor: pointer;
    transition: all 0.3s ease;
    border: 2px solid transparent;
}

.asset-item:hover {
    transform: translateY(-4px);
    box-shadow: 0 8px 24px rgba(94, 189, 49, 0.15);
    border-color: rgba(94, 189, 49, 0.2);
}

.asset-item:active {
    transform: translateY(-2px) scale(0.98);
}

.asset-thumbnail {
    position: relative;
    width: 100%;
    height: 120px;
    overflow: hidden;
}

.asset-thumbnail img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

/* 文件类型标签样式 */
.file-type-badge {
    position: absolute;
    top: 8px;
    right: 8px;
    background: rgba(94, 189, 49, 0.5);
    color: white;
    padding: 4px 8px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    gap: 4px;
    font-size: 10px;
    font-weight: 500;
    backdrop-filter: blur(4px);
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    z-index: 3;
}

.type-icon {
    font-size: 12px;
}

.file-format {
    font-size: 10px;
    font-weight: 600;
}

.duration {
    position: absolute;
    bottom: 8px;
    left: 8px;
    background: rgba(0, 0, 0, 0.7);
    color: white;
    padding: 2px 6px;
    border-radius: 4px;
    font-size: 12px;
}

.doc-thumbnail {
    display: flex;
    align-items: center;
    justify-content: center;
    background: #f0f0f0;
}

.doc-icon {
    font-size: 48px;
    color: #666;
}

.asset-info {
    padding: 12px;
}

.asset-title {
    margin: 0 0 4px 0;
    font-size: 14px;
    color: #333;
    line-height: 1.4;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
}

.favorite-time {
    font-size: 12px;
    color: #999;
    margin-top: 4px;
    line-height: 1.4;
    word-break: break-all;
    white-space: normal;
}

/* 加载状态容器 */
.load-status {
    text-align: center;
    margin-top: 24px;
    padding: 24px;
}

/* 加载更多按钮 */
.load-more {
    margin-bottom: 12px;
}

.load-more .el-button {
    width: 200px;
    height: 44px;
    border-radius: 22px;
    background: linear-gradient(135deg, var(--p-color-green-primary) 0%, #4ade80 100%);
    border: none;
    color: white;
    font-weight: 500;
    font-size: 15px;
    transition: all 0.3s ease;
    box-shadow: 0 4px 12px rgba(94, 189, 49, 0.3);
}

.load-more .el-button:hover {
    background: linear-gradient(135deg, var(--p-color-green-hover) 0%, #22c55e 100%);
    transform: translateY(-2px);
    box-shadow: 0 6px 16px rgba(94, 189, 49, 0.4);
}

.load-more .el-button:active {
    transform: translateY(0);
}

/* 加载中状态 */
.loading-more {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 12px;
    color: var(--p-color-green-primary);
    font-size: 15px;
    font-weight: 500;
    padding: 16px;
    background: rgba(94, 189, 49, 0.1);
    border-radius: 12px;
    margin: 0 auto;
    max-width: 200px;
}

.loading-spinner {
    width: 20px;
    height: 20px;
    border: 3px solid rgba(94, 189, 49, 0.2);
    border-top: 3px solid var(--p-color-green-primary);
    border-radius: 50%;
    animation: spin 1s linear infinite;
}

@keyframes spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
}

/* 没有更多内容提示 */
.no-more {
    color: #999;
    font-size: 15px;
    padding: 20px 0;
    text-align: center;
}
</style>
<template>
    <div class="assets-list-page">
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
                    <h1 class="nav-title">{{ pageTitle }}</h1>
                </div>
                <div class="nav-right">
                    <!-- 预留右侧操作区域 -->
                </div>
            </div>

            <!-- 搜索栏（非专辑页面显示） -->
            <div v-if="!isFromCollection" class="search-section">
                <div class="search-container">
                    <div class="search-input-wrapper">
                        <el-input
                            v-model="searchForm.keyword"
                            placeholder="请输入关键词搜索"
                            :prefix-icon="Search"
                            @keyup.enter="handleSearch"
                            clearable
                            class="search-input"
                        />
                        <el-button type="primary" @click="handleSearch" class="search-btn">
                            搜索
                        </el-button>
                    </div>
                </div>
            </div>

            <!-- 筛选和排序区域 -->
            <div class="filter-section">
                <!-- 筛选条件 -->
                <div class="filter-container">
                    <div class="filter-tabs">
                        <div 
                            v-for="tab in filterTabs" 
                            :key="tab.value"
                            class="filter-tab"
                            :class="{ active: searchForm.type === tab.value }"
                            @click="selectFilterTab(tab.value)"
                        >
                            {{ tab.label }}
                        </div>
                    </div>
                </div>

                <!-- 排序条件（仅专辑页面显示） -->
                <div v-if="isFromCollection" class="sort-container">
                    <div class="sort-tabs">
                        <div 
                            v-for="sort in sortOptions" 
                            :key="sort.value"
                            class="sort-tab"
                            :class="{ active: searchForm.sortBy === sort.value }"
                            @click="selectSortOption(sort.value)"
                        >
                            {{ sort.label }}
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- 素材列表 -->
        <div class="assets-content">
            <div v-if="loading" class="loading-container">
                <el-loading :loading="true" />
            </div>
            
            <div v-else-if="assetsList.length === 0" class="empty-container">
                <p class="empty-text">没有更多内容了</p>
            </div>
            
            <div v-else class="assets-grid">
                <div 
                    v-for="asset in assetsList" 
                    :key="asset.id"
                    class="asset-item"
                    @click="goToAssetDetail(asset)"
                >
                    <!-- 图片素材 -->
                    <div v-if="asset.assetType === 'image'" class="asset-thumbnail">
                        <img :src="asset.coverUrl" :alt="asset.title" />
                        <!-- 文件类型标签 -->
                        <div class="file-type-badge">
                            <component :is="getAssetTypeIcon(asset.assetType)" class="type-icon" />
                            <span class="file-format">{{ getFileFormat(asset.fileFormat) }}</span>
                        </div>
                    </div>
                    
                    <!-- 视频素材 -->
                    <div v-else-if="asset.assetType === 'video'" class="asset-thumbnail video-thumbnail">
                        <img :src="asset.coverUrl" :alt="asset.title" />
                        <div v-if="asset.duration" class="duration">
                            {{ formatDuration(asset.duration) }}
                        </div>
                        <!-- 文件类型标签 -->
                        <div class="file-type-badge">
                            <component :is="getAssetTypeIcon(asset.assetType)" class="type-icon" />
                            <span class="file-format">{{ getFileFormat(asset.fileFormat) }}</span>
                        </div>
                    </div>
                    
                    <!-- 文档素材 -->
                    <div v-else class="asset-thumbnail doc-thumbnail">
                        <img v-if="asset.coverUrl" :src="asset.coverUrl" :alt="asset.title" />
                        <FileDoc v-else class="doc-icon" />
                        <!-- 文件类型标签 -->
                        <div class="file-type-badge">
                            <component :is="getAssetTypeIcon(asset.assetType)" class="type-icon" />
                            <span class="file-format">{{ getFileFormat(asset.fileFormat) }}</span>
                        </div>
                    </div>
                    
                    <div class="asset-info">
                        <p class="asset-title">{{ asset.title }}</p>
                    </div>
                </div>
            </div>

            <!-- 加载状态和提示 -->
            <div class="load-status">
                <!-- 加载更多按钮 -->
                <div v-if="hasMore && !loading && assetsList.length > 0" class="load-more">
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
                <div v-if="!hasMore && assetsList.length > 0" class="no-more">
                    <span>没有更多内容了</span>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts" setup>
import { ref, reactive, computed, onMounted, onUnmounted, watch } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { Search, Picture, Video, FileDoc, Play, ArrowLeft } from '@icon-park/vue-next';
import * as API from '@/api';

const router = useRouter();
const route = useRoute();

// 响应式数据
const loading = ref(false);
const loadingMore = ref(false);
const assetsList = ref([]);
const hasMore = ref(true);
const currentPage = ref(1);
const totalRow = ref(0); // 总数量
const totalPage = ref(0); // 总页数

// 搜索表单
const searchForm = reactive({
    keyword: '',
    type: 'all',
    categoryId: null,
    collectionId: null,
    sortBy: 'comprehensive',
    classificationId: null // 新增分类ID字段
});

// 筛选选项
const filterTabs = [
    { label: '全部', value: 'all' },
    { label: '图片', value: 'image' },
    { label: '视频', value: 'video' },
    { label: '课件', value: 'doc' }
];

// 排序选项
const sortOptions = [
    { label: '综合排序', value: 'comprehensive' },
    { label: '热门下载', value: 'download' }
];

/**
 * 是否来自专辑页面
 */
const isFromCollection = computed(() => {
    return !!route.query.collectionId;
});

/**
 * 页面标题
 */
const pageTitle = computed(() => {
    if (isFromCollection.value) {
        // 如果有专辑名称，显示专辑名称，否则显示默认的"专辑素材"
        const collectionName = route.query.collectionName as string;
        return collectionName || '专辑素材';
    }
    return '素材库';
});

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
 * 获取文件格式显示文本
 * @param fileFormat 文件格式
 */
const getFileFormat = (fileFormat: string) => {
    if (!fileFormat) return '';
    return fileFormat.toUpperCase();
};

/**
 * 选择筛选标签
 * @param type 素材类型
 */
const selectFilterTab = (type: string) => {
    searchForm.type = type;
    // 根据类型设置 classificationId
    if (type === 'image') {
        searchForm.classificationId = 1;
    } else if (type === 'video') {
        searchForm.classificationId = 2;
    } else if (type === 'doc') {
        searchForm.classificationId = 3;
    } else {
        searchForm.classificationId = null; // 全部类型
    }
    resetAndSearch();
};

/**
 * 选择排序选项
 * @param sortBy 排序方式
 */
const selectSortOption = (sortBy: string) => {
    searchForm.sortBy = sortBy;
    resetAndSearch();
};

/**
 * 返回上一页
 */
const goBack = () => {
    router.back();
};

/**
 * 处理搜索
 */
const handleSearch = () => {
    resetAndSearch();
};

/**
 * 重置并搜索
 */
const resetAndSearch = () => {
    currentPage.value = 1;
    assetsList.value = [];
    hasMore.value = true;
    loadAssets();
};

/**
 * 加载更多
 */
const loadMore = () => {
    if (hasMore.value && !loadingMore.value) {
        currentPage.value++;
        loadAssets(true);
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
 * @param asset 素材信息
 */
const goToAssetDetail = (asset: any) => {
    router.push({
        name: 'assetDetail',
        params: { 
            id: asset.id, 
            type: asset.assetType || 'image' // 直接传递 assetType 值
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
 * 加载素材列表
 * @param isLoadMore 是否为加载更多
 */
const loadAssets = async (isLoadMore = false) => {
    try {
        if (isLoadMore) {
            loadingMore.value = true;
        } else {
            loading.value = true;
        }

        const params: any = {
            pageNumber: currentPage.value,
            pageSize: 20,
            keyword: searchForm.keyword || undefined,
            classificationId: searchForm.classificationId || undefined,
            collectionId: searchForm.collectionId || undefined,
            categoryId: searchForm.categoryId || undefined,
        };

        // 根据排序选项决定是否传递sort参数
        // 热门下载时传sort=download，综合排序时不传
        if (searchForm.sortBy === 'download') {
            params.sort = 'download';
        }

        // 使用新的聚合接口
        const response = await API.assets.aggregate(params);

        const newAssets = response.records || [];
        
        if (isLoadMore) {
            assetsList.value.push(...newAssets);
        } else {
            assetsList.value = newAssets;
        }

        // 更新分页信息
        if (response.page) {
            totalRow.value = response.page.totalRow || 0;
            totalPage.value = response.page.totalPage || 0;
            // 判断是否还有更多数据
            hasMore.value = currentPage.value < totalPage.value;
        } else {
            // 兼容旧的判断方式
            hasMore.value = newAssets.length >= 20;
        }

    } catch (error) {
        console.error('加载素材列表失败:', error);
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
    
    // 从路由参数获取搜索条件
    searchForm.keyword = route.query.keyword as string || '';
    searchForm.collectionId = route.query.collectionId as string || null;

    const typeParam = route.query.type as string || 'all';
    const classificationId = route.query.classificationId as string || null;
    
    if (typeParam === '1') {
        searchForm.type = 'image';
    } else if (typeParam === '2') {
        searchForm.type = 'video';
    } else if (typeParam === '3') {
        searchForm.type = 'doc';
    } else {
        searchForm.type = 'all';
    }

    // 根据type参数设置对应的classificationId
    const type = searchForm.type;
    if(classificationId) {
        searchForm.classificationId = classificationId;
    } else if (type === 'image') {
        searchForm.classificationId = 1;
    } else if (type === 'video') {
        searchForm.classificationId = 2;
    } else if (type === 'doc') {
        searchForm.classificationId = 3;
    } else {
        searchForm.classificationId = null; // 全部类型
    }
    
    // 加载数据
    loadAssets();
};

// 监听路由变化
watch(() => route.query, () => {
    initData();
}, { immediate: false });

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
.assets-list-page {
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
    flex: 0 0 40px; /* 固定宽度与返回按钮一致，确保左右等宽占位 */
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
    flex: 0 0 40px; /* 与左侧保持一致宽度用于占位，标题即可居中 */
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

/* 搜索区域样式 */
.search-section {
    padding: 12px 16px;
    background: linear-gradient(135deg, #f8fffe 0%, #f0fdf4 100%);
    border-top: 1px solid rgba(94, 189, 49, 0.1);
}

.search-container {
    display: flex;
    align-items: stretch;
    width: 100%;
}

.search-input-wrapper {
    flex: 1;
    display: flex;
    border-radius: 24px;
    overflow: hidden;
    border: 2px solid rgba(94, 189, 49, 0.2);
    background: white;
    transition: all 0.3s ease;
    box-shadow: 0 2px 8px rgba(94, 189, 49, 0.1);
    align-items: center;
}

.search-input-wrapper:focus-within {
    border-color: var(--p-color-green-primary);
    box-shadow: 0 4px 16px rgba(94, 189, 49, 0.2);
    transform: translateY(-1px);
}

.search-input {
    flex: 1;
    min-width: 0;
}

.search-input :deep(.el-input__wrapper) {
    border: none;
    border-radius: 0;
    height: 44px;
    box-shadow: none;
    background: transparent;
    padding: 0 16px;
}

.search-input :deep(.el-input__wrapper):focus-within {
    border: none;
    box-shadow: none;
}

.search-input :deep(.el-input__inner) {
    font-size: 15px;
}

.search-btn {
    border-radius: 22px;
    background: linear-gradient(135deg, var(--p-color-green-primary) 0%, #4ade80 100%);
    border: none;
    height: 40px;
    padding: 0 24px;
    margin-right: 2px;
    white-space: nowrap;
    flex-shrink: 0;
    font-weight: 500;
    transition: all 0.3s ease;
}

.search-btn:hover {
    background: linear-gradient(135deg, var(--p-color-green-hover) 0%, #22c55e 100%);
    transform: translateX(2px);
}

/* 筛选和排序区域样式 */
.filter-section {
    padding: 12px 16px 16px;
    background: linear-gradient(135deg, #f0fdf4 0%, #ffffff 100%);
}

.filter-container {
    margin-bottom: 8px;
}

.sort-container {
    border-top: 1px solid rgba(94, 189, 49, 0.1);
    padding-top: 12px;
}

.filter-tabs, .sort-tabs {
    display: flex;
    gap: 8px;
    justify-content: center;
    flex-wrap: wrap;
}

.filter-tab, .sort-tab {
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

.filter-tab.active, .sort-tab.active {
    background: linear-gradient(135deg, var(--p-color-green-primary) 0%, #4ade80 100%);
    color: white;
    border-color: var(--p-color-green-primary);
    box-shadow: 0 4px 12px rgba(94, 189, 49, 0.3);
    transform: translateY(-2px);
}

.filter-tab:hover, .sort-tab:hover {
    background: rgba(94, 189, 49, 0.2);
    transform: translateY(-1px);
    box-shadow: 0 2px 8px rgba(94, 189, 49, 0.2);
}

.filter-tab.active:hover, .sort-tab.active:hover {
    background: linear-gradient(135deg, var(--p-color-green-hover) 0%, #22c55e 100%);
    transform: translateY(-2px);
}

/* 内容区域 */
.assets-content {
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

.asset-meta {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    font-size: 12px;
    color: #999;
}

.download-count {
    color: var(--p-color-green-primary);
    font-weight: 500;
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
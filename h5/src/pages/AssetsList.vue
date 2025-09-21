<template>
    <div class="assets-list-page">
        <!-- 搜索栏 -->
        <div v-if="!isFromCollection" class="search-bar">
            <el-input
                v-model="searchForm.keyword"
                placeholder="请输入关键词搜索"
                :prefix-icon="Search"
                @keyup.enter="handleSearch"
                clearable
            >
                <template #suffix>
                    <el-button type="primary" @click="handleSearch" class="search-btn">
                        搜索
                    </el-button>
                </template>
            </el-input>
        </div>

        <!-- 筛选条件 -->
        <div class="filter-bar">
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
        <div v-if="isFromCollection" class="sort-bar">
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

        <!-- 素材列表 -->
        <div class="assets-content">
            <div v-if="loading" class="loading-container">
                <el-loading :loading="true" />
            </div>
            
            <div v-else-if="assetsList.length === 0" class="empty-container">
                <div class="empty-icon">
                    <Picture />
                </div>
                <p class="empty-text">暂无素材</p>
            </div>
            
            <div v-else class="assets-grid">
                <div 
                    v-for="asset in assetsList" 
                    :key="asset.id"
                    class="asset-item"
                    @click="goToAssetDetail(asset)"
                >
                    <!-- 图片素材 -->
                    <div v-if="asset.type === 'image'" class="asset-thumbnail">
                        <img :src="asset.thumbnailUrl || asset.imageUrl" :alt="asset.title" />
                    </div>
                    
                    <!-- 视频素材 -->
                    <div v-else-if="asset.type === 'video'" class="asset-thumbnail video-thumbnail">
                        <img :src="asset.thumbnailUrl" :alt="asset.title" />
                        <div class="play-icon">
                            <Play />
                        </div>
                        <div v-if="asset.duration" class="duration">
                            {{ formatDuration(asset.duration) }}
                        </div>
                    </div>
                    
                    <!-- 文档素材 -->
                    <div v-else class="asset-thumbnail doc-thumbnail">
                        <FileDoc class="doc-icon" />
                    </div>
                    
                    <div class="asset-info">
                        <p class="asset-title">{{ asset.title }}</p>
                        <div class="asset-meta">
                            <span class="asset-type">{{ getAssetTypeLabel(asset.type) }}</span>
                            <span v-if="asset.downloadCount" class="download-count">
                                {{ asset.downloadCount }}次下载
                            </span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 加载更多 -->
            <div v-if="hasMore && !loading" class="load-more">
                <el-button @click="loadMore" :loading="loadingMore">
                    {{ loadingMore ? '加载中...' : '加载更多' }}
                </el-button>
            </div>
        </div>
    </div>
</template>

<script lang="ts" setup>
import { ref, reactive, computed, onMounted, watch } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { Search, Picture, Video, FileDoc, Play } from '@icon-park/vue-next';
import * as API from '@/api';

const router = useRouter();
const route = useRoute();

// 响应式数据
const loading = ref(false);
const loadingMore = ref(false);
const assetsList = ref([]);
const hasMore = ref(true);
const currentPage = ref(1);

// 搜索表单
const searchForm = reactive({
    keyword: '',
    type: 'all',
    categoryId: null,
    collectionId: null,
    sortBy: 'comprehensive'
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
 * 选择筛选标签
 * @param type 素材类型
 */
const selectFilterTab = (type: string) => {
    searchForm.type = type;
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
 * 跳转到素材详情
 * @param asset 素材信息
 */
const goToAssetDetail = (asset: any) => {
    router.push({
        name: 'assetDetail',
        params: { 
            id: asset.id, 
            type: asset.type 
        }
    });
};

/**
 * 获取素材类型标签
 * @param type 素材类型
 */
const getAssetTypeLabel = (type: string) => {
    const typeMap = {
        image: '图片',
        video: '视频',
        doc: '课件'
    };
    return typeMap[type] || '未知';
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

        const params = {
            current: currentPage.value,
            size: 20,
            ...searchForm
        };

        // 根据类型调用不同的API
        let response;
        if (searchForm.type === 'image') {
            response = await API.assetsImage.query(params);
        } else if (searchForm.type === 'video') {
            response = await API.assetsVideo.query(params);
        } else if (searchForm.type === 'doc') {
            response = await API.assetsDoc.query(params);
        } else {
            // 全部类型，需要合并多个API的结果
            const [imageRes, videoRes, docRes] = await Promise.all([
                API.assetsImage.query(params),
                API.assetsVideo.query(params),
                API.assetsDoc.query(params)
            ]);
            
            const allAssets = [
                ...(imageRes.data?.records || imageRes.data || []).map(item => ({ ...item, type: 'image' })),
                ...(videoRes.data?.records || videoRes.data || []).map(item => ({ ...item, type: 'video' })),
                ...(docRes.data?.records || docRes.data || []).map(item => ({ ...item, type: 'doc' }))
            ];
            
            response = {
                data: {
                    records: allAssets,
                    total: allAssets.length
                }
            };
        }

        const newAssets = response.data?.records || response.data || [];
        
        if (isLoadMore) {
            assetsList.value.push(...newAssets);
        } else {
            assetsList.value = newAssets;
        }

        // 判断是否还有更多数据
        hasMore.value = newAssets.length >= 20;

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
    // 从路由参数获取搜索条件
    searchForm.keyword = route.query.keyword as string || '';
    searchForm.type = route.query.type as string || 'all';
    searchForm.categoryId = route.query.categoryId as string || null;
    searchForm.collectionId = route.query.collectionId as string || null;
    
    // 加载数据
    loadAssets();
};

// 监听路由变化
watch(() => route.query, () => {
    initData();
}, { immediate: false });

onMounted(() => {
    initData();
});
</script>

<style scoped>
.assets-list-page {
    background-color: #f5f5f5;
    min-height: 100vh;
    padding-bottom: 80px;
}

/* 搜索栏样式 */
.search-bar {
    padding: 16px;
    background: white;
    border-bottom: 1px solid #eee;
}

.search-input {
    border-radius: 25px;
}

.search-input :deep(.el-input__wrapper) {
    border-radius: 25px;
    padding-right: 0;
}

.search-btn {
    border-radius: 0 25px 25px 0;
    border-left: none;
}

/* 筛选条件样式 */
.filter-bar, .sort-bar {
    background: white;
    padding: 12px 16px;
    border-bottom: 1px solid #eee;
}

.filter-tabs, .sort-tabs {
    display: flex;
    gap: 8px;
}

.filter-tab, .sort-tab {
    padding: 8px 16px;
    border-radius: 20px;
    background: #f5f5f5;
    color: #666;
    font-size: 14px;
    cursor: pointer;
    transition: all 0.2s;
}

.filter-tab.active, .sort-tab.active {
    background: #409eff;
    color: white;
}

.filter-tab:hover, .sort-tab:hover {
    background: #e3f2fd;
}

.filter-tab.active:hover, .sort-tab.active:hover {
    background: #409eff;
}

/* 内容区域 */
.assets-content {
    padding: 16px;
}

.loading-container {
    height: 200px;
    display: flex;
    align-items: center;
    justify-content: center;
}

.empty-container {
    text-align: center;
    padding: 60px 20px;
}

.empty-icon {
    font-size: 64px;
    color: #ddd;
    margin-bottom: 16px;
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
    gap: 12px;
}

.asset-item {
    background: white;
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    cursor: pointer;
    transition: transform 0.2s;
}

.asset-item:active {
    transform: scale(0.95);
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

.video-thumbnail .play-icon {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    color: white;
    font-size: 24px;
    background: rgba(0, 0, 0, 0.5);
    border-radius: 50%;
    padding: 8px;
}

.duration {
    position: absolute;
    bottom: 8px;
    right: 8px;
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
    margin: 0 0 8px 0;
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
    justify-content: space-between;
    align-items: center;
    font-size: 12px;
    color: #999;
}

.asset-type {
    background: #f0f8ff;
    color: #409eff;
    padding: 2px 6px;
    border-radius: 4px;
}

/* 加载更多 */
.load-more {
    text-align: center;
    margin-top: 20px;
}

.load-more .el-button {
    width: 200px;
    border-radius: 20px;
}
</style>
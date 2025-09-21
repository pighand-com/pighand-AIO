<template>
    <div class="records-list-page">
        <!-- 页面标题 -->
        <div class="page-header">
            <div class="header-left">
                <el-button @click="goBack" :icon="ArrowLeft" circle />
            </div>
            <h1 class="page-title">{{ pageTitle }}</h1>
            <div class="header-right"></div>
        </div>

        <!-- 筛选条件 -->
        <div class="filter-bar">
            <div class="filter-tabs">
                <div 
                    v-for="tab in filterTabs" 
                    :key="tab.value"
                    class="filter-tab"
                    :class="{ active: currentFilter === tab.value }"
                    @click="selectFilterTab(tab.value)"
                >
                    {{ tab.label }}
                </div>
            </div>
        </div>

        <!-- 记录列表 -->
        <div class="records-content">
            <div v-if="loading" class="loading-container">
                <el-loading :loading="true" />
            </div>
            
            <div v-else-if="recordsList.length === 0" class="empty-container">
                <div class="empty-icon">
                    <Picture />
                </div>
                <p class="empty-text">{{ emptyText }}</p>
            </div>
            
            <div v-else class="records-grid">
                <div 
                    v-for="record in recordsList" 
                    :key="record.id"
                    class="record-item"
                    @click="goToAssetDetail(record.asset)"
                >
                    <!-- 图片素材 -->
                    <div v-if="record.asset.type === 'image'" class="asset-thumbnail">
                        <img :src="record.asset.thumbnailUrl || record.asset.imageUrl" :alt="record.asset.title" />
                    </div>
                    
                    <!-- 视频素材 -->
                    <div v-else-if="record.asset.type === 'video'" class="asset-thumbnail video-thumbnail">
                        <img :src="record.asset.thumbnailUrl" :alt="record.asset.title" />
                        <div class="play-icon">
                            <Play />
                        </div>
                        <div v-if="record.asset.duration" class="duration">
                            {{ formatDuration(record.asset.duration) }}
                        </div>
                    </div>
                    
                    <!-- 文档素材 -->
                    <div v-else class="asset-thumbnail doc-thumbnail">
                        <FileDoc class="doc-icon" />
                    </div>
                    
                    <div class="asset-info">
                        <p class="asset-title">{{ record.asset.title }}</p>
                        <div class="asset-meta">
                            <span class="asset-type">{{ getAssetTypeLabel(record.asset.type) }}</span>
                            <span class="record-time">{{ formatDate(record.createTime) }}</span>
                        </div>
                    </div>

                    <!-- 操作按钮 -->
                    <div class="record-actions">
                        <el-button 
                            v-if="recordType === 'favorite'"
                            @click.stop="removeFromFavorite(record)"
                            type="danger"
                            size="small"
                            :icon="Delete"
                            circle
                        />
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
import { ElMessage, ElMessageBox } from 'element-plus';
import { 
    ArrowLeft, 
    Picture, 
    Video, 
    FileDoc, 
    Play, 
    Delete 
} from '@icon-park/vue-next';
import * as API from '@/api';

const router = useRouter();
const route = useRoute();

// 响应式数据
const loading = ref(false);
const loadingMore = ref(false);
const recordsList = ref([]);
const hasMore = ref(true);
const currentPage = ref(1);
const currentFilter = ref('all');

// 记录类型（favorite 或 download）
const recordType = computed(() => route.params.type as string);

// 页面标题
const pageTitle = computed(() => {
    return recordType.value === 'favorite' ? '我的收藏' : '我的下载';
});

// 空状态文本
const emptyText = computed(() => {
    return recordType.value === 'favorite' ? '暂无收藏记录' : '暂无下载记录';
});

// 筛选选项
const filterTabs = [
    { label: '全部', value: 'all' },
    { label: '图片', value: 'image' },
    { label: '视频', value: 'video' },
    { label: '课件', value: 'doc' }
];

/**
 * 返回上一页
 */
const goBack = () => {
    router.back();
};

/**
 * 选择筛选标签
 * @param type 素材类型
 */
const selectFilterTab = (type: string) => {
    currentFilter.value = type;
    resetAndLoad();
};

/**
 * 重置并加载
 */
const resetAndLoad = () => {
    currentPage.value = 1;
    recordsList.value = [];
    hasMore.value = true;
    loadRecords();
};

/**
 * 加载更多
 */
const loadMore = () => {
    if (hasMore.value && !loadingMore.value) {
        currentPage.value++;
        loadRecords(true);
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
 * 格式化日期
 * @param date 日期字符串
 */
const formatDate = (date: string) => {
    if (!date) return '';
    const now = new Date();
    const recordDate = new Date(date);
    const diffTime = now.getTime() - recordDate.getTime();
    const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24));
    
    if (diffDays === 0) {
        return '今天';
    } else if (diffDays === 1) {
        return '昨天';
    } else if (diffDays < 7) {
        return `${diffDays}天前`;
    } else {
        return recordDate.toLocaleDateString('zh-CN');
    }
};

/**
 * 从收藏中移除
 * @param record 记录信息
 */
const removeFromFavorite = async (record: any) => {
    try {
        await ElMessageBox.confirm(
            '确定要取消收藏这个素材吗？',
            '提示',
            {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }
        );

        await API.user.update({
            id: 'current',
            action: 'removeFavorite',
            assetId: record.asset.id,
            assetType: record.asset.type
        });

        // 从列表中移除
        recordsList.value = recordsList.value.filter(item => item.id !== record.id);
        
        ElMessage.success('已取消收藏');
    } catch (error) {
        if (error !== 'cancel') {
            console.error('取消收藏失败:', error);
            ElMessage.error('操作失败，请重试');
        }
    }
};

/**
 * 加载记录列表
 * @param isLoadMore 是否为加载更多
 */
const loadRecords = async (isLoadMore = false) => {
    try {
        if (isLoadMore) {
            loadingMore.value = true;
        } else {
            loading.value = true;
        }

        const params = {
            current: currentPage.value,
            size: 20,
            type: currentFilter.value === 'all' ? undefined : currentFilter.value,
            action: recordType.value === 'favorite' ? 'getFavorites' : 'getDownloads'
        };

        const response = await API.user.query(params);
        const newRecords = response.data?.records || response.data || [];
        
        if (isLoadMore) {
            recordsList.value.push(...newRecords);
        } else {
            recordsList.value = newRecords;
        }

        // 判断是否还有更多数据
        hasMore.value = newRecords.length >= 20;

    } catch (error) {
        console.error('加载记录列表失败:', error);
        ElMessage.error('加载失败，请重试');
    } finally {
        loading.value = false;
        loadingMore.value = false;
    }
};

/**
 * 初始化页面数据
 */
const initData = () => {
    loadRecords();
};

// 监听路由变化
watch(() => route.params.type, () => {
    initData();
}, { immediate: false });

onMounted(() => {
    initData();
});
</script>

<style scoped>
.records-list-page {
    background-color: #f5f5f5;
    min-height: 100vh;
    padding-bottom: 80px;
}

/* 页面头部 */
.page-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 12px 16px;
    background: white;
    border-bottom: 1px solid #eee;
}

.header-left,
.header-right {
    width: 40px;
}

.page-title {
    margin: 0;
    font-size: 18px;
    color: #333;
    font-weight: 500;
}

/* 筛选条件样式 */
.filter-bar {
    background: white;
    padding: 12px 16px;
    border-bottom: 1px solid #eee;
}

.filter-tabs {
    display: flex;
    gap: 8px;
}

.filter-tab {
    padding: 8px 16px;
    border-radius: 20px;
    background: #f5f5f5;
    color: #666;
    font-size: 14px;
    cursor: pointer;
    transition: all 0.2s;
}

.filter-tab.active {
    background: #409eff;
    color: white;
}

.filter-tab:hover {
    background: #e3f2fd;
}

.filter-tab.active:hover {
    background: #409eff;
}

/* 内容区域 */
.records-content {
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

/* 记录网格 */
.records-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 12px;
}

.record-item {
    position: relative;
    background: white;
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    cursor: pointer;
    transition: transform 0.2s;
}

.record-item:active {
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

.record-time {
    color: #999;
}

/* 操作按钮 */
.record-actions {
    position: absolute;
    top: 8px;
    right: 8px;
    z-index: 10;
}

.record-actions .el-button {
    background: rgba(255, 255, 255, 0.9);
    border: none;
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
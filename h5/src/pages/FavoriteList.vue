<template>
    <div class="favorite-list-page">
        <!-- 页面标题 -->
        <div class="page-header">
            <h2>我的收藏</h2>
        </div>

        <!-- 筛选条件 -->
        <div class="filter-section">
            <el-tabs v-model="activeTab" @tab-change="handleTabChange">
                <el-tab-pane label="全部" name="all"></el-tab-pane>
                <el-tab-pane label="图片" name="image"></el-tab-pane>
                <el-tab-pane label="视频" name="video"></el-tab-pane>
                <el-tab-pane label="课件" name="doc"></el-tab-pane>
            </el-tabs>
        </div>

        <!-- 收藏列表 -->
        <div class="favorites-content">
            <!-- 加载状态 -->
            <div v-if="loading" class="loading-container">
                <el-skeleton :rows="6" animated />
            </div>

            <!-- 空状态 -->
            <div v-else-if="favoriteList.length === 0" class="empty-state">
                <div class="empty-icon">
                    <Heart />
                </div>
                <p class="empty-text">暂无收藏内容</p>
                <el-button type="primary" @click="goToHome">去首页看看</el-button>
            </div>

            <!-- 收藏网格 -->
            <div v-else class="favorites-grid">
                <div 
                    v-for="item in favoriteList" 
                    :key="item.id"
                    class="favorite-item"
                    @click="goToAssetDetail(item)"
                >
                    <!-- 素材缩略图 -->
                    <div class="asset-thumbnail">
                        <img 
                            v-if="item.assetType === 'image'" 
                            :src="item.asset.thumbnailUrl || item.asset.imageUrl" 
                            :alt="item.asset.title"
                            @error="handleImageError"
                        />
                        <div v-else-if="item.assetType === 'video'" class="video-thumb">
                            <img 
                                :src="item.asset.thumbnailUrl" 
                                :alt="item.asset.title"
                                @error="handleImageError"
                            />
                            <div class="play-overlay">
                                <Play theme="filled" />
                            </div>
                        </div>
                        <div v-else class="doc-thumb">
                            <FileDoc />
                        </div>
                        
                        <!-- 取消收藏按钮 -->
                        <div class="favorite-action" @click.stop="removeFavorite(item)">
                            <Heart theme="filled" />
                        </div>
                    </div>

                    <!-- 素材信息 -->
                    <div class="asset-info">
                        <p class="asset-title">{{ item.asset.title }}</p>
                        <div class="asset-meta">
                            <span class="asset-type">{{ getAssetTypeLabel(item.assetType) }}</span>
                            <span class="favorite-time">{{ formatTime(item.createTime) }}</span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 加载更多 -->
            <div v-if="hasMore && !loading" class="load-more">
                <el-button @click="loadMore" :loading="loadingMore">加载更多</el-button>
            </div>
        </div>
    </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Heart, Play, FileDoc } from '@icon-park/vue-next';
import * as API from '@/api';

const router = useRouter();

// 响应式数据
const loading = ref(false);
const loadingMore = ref(false);
const activeTab = ref('all');
const favoriteList = ref([]);
const hasMore = ref(true);
const currentPage = ref(1);
const pageSize = 20;

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
 * 格式化时间
 * @param time 时间字符串
 */
const formatTime = (time: string) => {
    if (!time) return '';
    const date = new Date(time);
    const now = new Date();
    const diff = now.getTime() - date.getTime();
    const days = Math.floor(diff / (1000 * 60 * 60 * 24));
    
    if (days === 0) {
        return '今天';
    } else if (days === 1) {
        return '昨天';
    } else if (days < 7) {
        return `${days}天前`;
    } else {
        return date.toLocaleDateString();
    }
};

/**
 * 处理图片加载错误
 * @param event 错误事件
 */
const handleImageError = (event: Event) => {
    const target = event.target as HTMLImageElement;
    target.src = '/default-thumbnail.png'; // 设置默认图片
};

/**
 * 切换标签页
 * @param tabName 标签名称
 */
const handleTabChange = (tabName: string) => {
    activeTab.value = tabName;
    currentPage.value = 1;
    favoriteList.value = [];
    hasMore.value = true;
    loadFavorites();
};

/**
 * 跳转到首页
 */
const goToHome = () => {
    router.push({ name: 'home' });
};

/**
 * 跳转到素材详情
 * @param item 收藏项
 */
const goToAssetDetail = (item: any) => {
    router.push({
        name: 'assetDetail',
        params: { 
            id: item.assetId, 
            type: item.assetType 
        }
    });
};

/**
 * 取消收藏
 * @param item 收藏项
 */
const removeFavorite = async (item: any) => {
    try {
        await ElMessageBox.confirm(
            '确定要取消收藏吗？',
            '提示',
            {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
            }
        );

        await API.assetsFavourite.del(item.id);
        
        // 从列表中移除
        const index = favoriteList.value.findIndex(fav => fav.id === item.id);
        if (index > -1) {
            favoriteList.value.splice(index, 1);
        }
        
        ElMessage.success('已取消收藏');
    } catch (error) {
        if (error !== 'cancel') {
            console.error('取消收藏失败:', error);
            ElMessage.error('操作失败，请重试');
        }
    }
};

/**
 * 加载收藏列表
 */
const loadFavorites = async () => {
    try {
        loading.value = currentPage.value === 1;
        loadingMore.value = currentPage.value > 1;
        
        const params: any = {
            current: currentPage.value,
            size: pageSize
        };
        
        // 根据标签页筛选类型
        if (activeTab.value !== 'all') {
            params.assetType = activeTab.value;
        }
        
        const response = await API.assetsFavourite.query(params);
        const data = response.data?.records || response.data || [];
        
        if (currentPage.value === 1) {
            favoriteList.value = data;
        } else {
            favoriteList.value.push(...data);
        }
        
        // 判断是否还有更多数据
        hasMore.value = data.length === pageSize;
        
    } catch (error) {
        console.error('加载收藏列表失败:', error);
        ElMessage.error('加载失败，请重试');
    } finally {
        loading.value = false;
        loadingMore.value = false;
    }
};

/**
 * 加载更多
 */
const loadMore = () => {
    currentPage.value++;
    loadFavorites();
};

onMounted(() => {
    loadFavorites();
});
</script>

<style scoped>
.favorite-list-page {
    background-color: #f5f5f5;
    min-height: 100vh;
    padding-bottom: 80px;
}

/* 页面标题 */
.page-header {
    background: white;
    padding: 16px 20px;
    border-bottom: 1px solid #eee;
}

.page-header h2 {
    margin: 0;
    font-size: 18px;
    font-weight: 600;
    color: #333;
}

/* 筛选条件 */
.filter-section {
    background: white;
    padding: 0 20px;
    border-bottom: 1px solid #eee;
}

.filter-section :deep(.el-tabs__header) {
    margin: 0;
}

.filter-section :deep(.el-tabs__nav-wrap::after) {
    display: none;
}

/* 收藏内容 */
.favorites-content {
    padding: 16px;
}

/* 加载状态 */
.loading-container {
    background: white;
    padding: 20px;
    border-radius: 8px;
}

/* 空状态 */
.empty-state {
    background: white;
    padding: 60px 20px;
    text-align: center;
    border-radius: 8px;
}

.empty-icon {
    font-size: 64px;
    color: #ddd;
    margin-bottom: 16px;
}

.empty-text {
    font-size: 16px;
    color: #999;
    margin: 0 0 24px 0;
}

/* 收藏网格 */
.favorites-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
    gap: 16px;
}

.favorite-item {
    background: white;
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    cursor: pointer;
    transition: transform 0.2s;
}

.favorite-item:hover {
    transform: translateY(-2px);
}

/* 素材缩略图 */
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

.video-thumb {
    position: relative;
    width: 100%;
    height: 100%;
}

.play-overlay {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 40px;
    height: 40px;
    background: rgba(0, 0, 0, 0.6);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    color: white;
    font-size: 16px;
}

.doc-thumb {
    width: 100%;
    height: 100%;
    background: #f8f9fa;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 48px;
    color: #666;
}

/* 收藏操作按钮 */
.favorite-action {
    position: absolute;
    top: 8px;
    right: 8px;
    width: 32px;
    height: 32px;
    background: rgba(255, 255, 255, 0.9);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #ff4757;
    font-size: 16px;
    cursor: pointer;
    transition: all 0.2s;
}

.favorite-action:hover {
    background: white;
    transform: scale(1.1);
}

/* 素材信息 */
.asset-info {
    padding: 12px;
}

.asset-title {
    font-size: 14px;
    font-weight: 500;
    color: #333;
    margin: 0 0 8px 0;
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
    background: #f0f0f0;
    padding: 2px 6px;
    border-radius: 4px;
}

/* 加载更多 */
.load-more {
    text-align: center;
    margin-top: 20px;
}

/* 响应式设计 */
@media (max-width: 768px) {
    .favorites-grid {
        grid-template-columns: repeat(auto-fill, minmax(140px, 1fr));
        gap: 12px;
    }
    
    .asset-thumbnail {
        height: 100px;
    }
    
    .favorites-content {
        padding: 12px;
    }
}
</style>
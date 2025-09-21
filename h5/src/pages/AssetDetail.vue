<template>
    <div class="asset-detail-page">
        <!-- 素材展示区域 -->
        <div class="asset-media">
            <!-- 图片素材 -->
            <div v-if="assetDetail.type === 'image'" class="image-container">
                <img :src="assetDetail.imageUrl" :alt="assetDetail.title" @click="previewImage" />
            </div>
            
            <!-- 视频素材 -->
            <div v-else-if="assetDetail.type === 'video'" class="video-container">
                <video 
                    :src="assetDetail.videoUrl" 
                    :poster="assetDetail.thumbnailUrl"
                    controls
                    preload="metadata"
                    @loadedmetadata="onVideoLoaded"
                >
                    您的浏览器不支持视频播放
                </video>
            </div>
            
            <!-- 文档素材 -->
            <div v-else class="doc-container">
                <div class="doc-preview">
                    <FileDoc class="doc-icon" />
                    <p class="doc-name">{{ assetDetail.title }}</p>
                    <p class="doc-size">{{ formatFileSize(assetDetail.fileSize) }}</p>
                </div>
            </div>
        </div>

        <!-- 素材信息 -->
        <div class="asset-info">
            <h1 class="asset-title">{{ assetDetail.title }}</h1>
            
            <div class="asset-meta">
                <div class="meta-item">
                    <span class="meta-label">类型：</span>
                    <span class="meta-value">{{ getAssetTypeLabel(assetDetail.type) }}</span>
                </div>
                <div class="meta-item">
                    <span class="meta-label">大小：</span>
                    <span class="meta-value">{{ formatFileSize(assetDetail.fileSize) }}</span>
                </div>
                <div v-if="assetDetail.duration" class="meta-item">
                    <span class="meta-label">时长：</span>
                    <span class="meta-value">{{ formatDuration(assetDetail.duration) }}</span>
                </div>
                <div class="meta-item">
                    <span class="meta-label">下载次数：</span>
                    <span class="meta-value">{{ assetDetail.downloadCount || 0 }}次</span>
                </div>
                <div class="meta-item">
                    <span class="meta-label">上传时间：</span>
                    <span class="meta-value">{{ formatDate(assetDetail.createTime) }}</span>
                </div>
            </div>

            <!-- 素材描述 -->
            <div v-if="assetDetail.description" class="asset-description">
                <h3>素材介绍</h3>
                <p>{{ assetDetail.description }}</p>
            </div>

            <!-- 标签 -->
            <div v-if="assetDetail.tags && assetDetail.tags.length" class="asset-tags">
                <h3>标签</h3>
                <div class="tags-list">
                    <span v-for="tag in assetDetail.tags" :key="tag" class="tag">
                        {{ tag }}
                    </span>
                </div>
            </div>
        </div>

        <!-- 作者信息 -->
        <div v-if="assetDetail.author" class="author-info">
            <h3>作者信息</h3>
            <div class="author-card">
                <div class="author-avatar">
                    <img v-if="assetDetail.author.avatar" :src="assetDetail.author.avatar" :alt="assetDetail.author.name" />
                    <div v-else class="default-avatar">
                        <User />
                    </div>
                </div>
                <div class="author-details">
                    <p class="author-name">{{ assetDetail.author.name }}</p>
                    <p v-if="assetDetail.author.department" class="author-department">{{ assetDetail.author.department }}</p>
                    <p v-if="assetDetail.author.description" class="author-description">{{ assetDetail.author.description }}</p>
                </div>
            </div>
        </div>

        <!-- 相关推荐 -->
        <div v-if="relatedAssets.length" class="related-assets">
            <h3>相关推荐</h3>
            <div class="related-list">
                <div 
                    v-for="asset in relatedAssets" 
                    :key="asset.id"
                    class="related-item"
                    @click="goToAssetDetail(asset)"
                >
                    <div class="related-thumbnail">
                        <img v-if="asset.type === 'image'" :src="asset.thumbnailUrl || asset.imageUrl" :alt="asset.title" />
                        <img v-else-if="asset.type === 'video'" :src="asset.thumbnailUrl" :alt="asset.title" />
                        <div v-else class="doc-thumb">
                            <FileDoc />
                        </div>
                    </div>
                    <p class="related-title">{{ asset.title }}</p>
                </div>
            </div>
        </div>

        <!-- 底部操作栏 -->
        <div class="bottom-actions">
            <div class="action-left">
                <div class="action-btn" @click="goHome">
                    <Home />
                    <span>首页</span>
                </div>
                <div class="action-btn" :class="{ active: isFavorited }" @click="toggleFavorite">
                    <Heart v-if="isFavorited" theme="filled" />
                    <Heart v-else />
                    <span>{{ isFavorited ? '已收藏' : '收藏' }}</span>
                </div>
            </div>
            <div class="action-right">
                <el-button type="primary" @click="downloadAsset" :loading="downloading">
                    <Download />
                    {{ downloading ? '下载中...' : '立即下载' }}
                </el-button>
                <el-button @click="shareAsset">
                    <Share />
                    分享
                </el-button>
            </div>
        </div>

        <!-- 图片预览 -->
        <el-image-viewer
            v-if="showImageViewer"
            :url-list="[assetDetail.imageUrl]"
            @close="showImageViewer = false"
        />
    </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted, nextTick } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { ElMessage, ElImageViewer } from 'element-plus';
import { 
    FileDoc, 
    User, 
    Home, 
    Heart, 
    Download, 
    Share 
} from '@icon-park/vue-next';
import { useAuth, Permission } from '@/common/auth';
import * as API from '@/api';

const router = useRouter();
const route = useRoute();
const { checkPermission } = useAuth();

// 响应式数据
const loading = ref(false);
const downloading = ref(false);
const assetDetail = ref<any>({});
const relatedAssets = ref([]);
const isFavorited = ref(false);
const showImageViewer = ref(false);

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
 * 格式化文件大小
 * @param size 文件大小（字节）
 */
const formatFileSize = (size: number) => {
    if (!size) return '未知';
    
    const units = ['B', 'KB', 'MB', 'GB'];
    let index = 0;
    let fileSize = size;
    
    while (fileSize >= 1024 && index < units.length - 1) {
        fileSize /= 1024;
        index++;
    }
    
    return `${fileSize.toFixed(1)} ${units[index]}`;
};

/**
 * 格式化时长
 * @param duration 时长（秒）
 */
const formatDuration = (duration: number) => {
    if (!duration) return '未知';
    
    const hours = Math.floor(duration / 3600);
    const minutes = Math.floor((duration % 3600) / 60);
    const seconds = duration % 60;
    
    if (hours > 0) {
        return `${hours}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
    }
    return `${minutes}:${seconds.toString().padStart(2, '0')}`;
};

/**
 * 格式化日期
 * @param date 日期字符串
 */
const formatDate = (date: string) => {
    if (!date) return '未知';
    return new Date(date).toLocaleDateString('zh-CN');
};

/**
 * 预览图片
 */
const previewImage = () => {
    showImageViewer.value = true;
};

/**
 * 视频加载完成
 */
const onVideoLoaded = (event: Event) => {
    const video = event.target as HTMLVideoElement;
    if (!assetDetail.value.duration && video.duration) {
        assetDetail.value.duration = Math.floor(video.duration);
    }
};

/**
 * 返回首页
 */
const goHome = () => {
    router.push('/');
};

/**
 * 切换收藏状态
 */
const toggleFavorite = async () => {
    // 检查收藏权限
    if (!checkPermission(Permission.ASSET_FAVORITE)) {
        return;
    }
    
    try {
        const assetId = route.params.id as string;
        const assetType = route.params.type as string;
        
        if (isFavorited.value) {
            // 取消收藏
            await API.assetsFavourite.del(assetId);
            isFavorited.value = false;
            ElMessage.success('已取消收藏');
        } else {
            // 添加收藏
            await API.assetsFavourite.create({
                assetId,
                assetType
            });
            isFavorited.value = true;
            ElMessage.success('收藏成功');
        }
    } catch (error) {
        console.error('收藏操作失败:', error);
        ElMessage.error('操作失败，请重试');
    }
};

/**
 * 下载素材
 */
const downloadAsset = async () => {
    // 检查下载权限
    if (!checkPermission(Permission.ASSET_DOWNLOAD)) {
        return;
    }
    
    try {
        downloading.value = true;
        
        const assetId = route.params.id as string;
        const assetType = route.params.type as string;
        
        // 调用下载API
        let downloadResponse;
        if (assetType === 'image') {
            downloadResponse = await API.assetsImage.download(assetId);
        } else if (assetType === 'video') {
            downloadResponse = await API.assetsVideo.download(assetId);
        } else {
            downloadResponse = await API.assetsDoc.download(assetId);
        }
        
        if (downloadResponse && downloadResponse.downloadUrl) {
            // 创建下载链接
            const link = document.createElement('a');
            link.href = downloadResponse.downloadUrl;
            link.download = assetDetail.value.title || 'download';
            link.target = '_blank';
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
            
            // 更新下载次数
            assetDetail.value.downloadCount = (assetDetail.value.downloadCount || 0) + 1;
            
            ElMessage.success('下载开始');
        } else {
            ElMessage.error('下载链接不存在');
        }
    } catch (error) {
        console.error('下载失败:', error);
        ElMessage.error('下载失败，请重试');
    } finally {
        downloading.value = false;
    }
};

/**
 * 加载收藏状态
 */
const loadFavoriteStatus = async () => {
    try {
        const assetId = route.params.id as string;
        
        const response = await API.assetsFavourite.query({
            assetId: assetId
        });
        
        isFavorited.value = response.data && response.data.length > 0;
    } catch (error) {
        console.error('加载收藏状态失败:', error);
    }
};

/**
 * 分享素材
 */
const shareAsset = async () => {
    try {
        const shareUrl = window.location.href;
        
        if (navigator.share) {
            // 使用原生分享API
            await navigator.share({
                title: assetDetail.value.title,
                text: assetDetail.value.description || '分享一个素材',
                url: shareUrl
            });
        } else {
            // 复制链接到剪贴板
            await navigator.clipboard.writeText(shareUrl);
            ElMessage.success('链接已复制到剪贴板');
        }
    } catch (error) {
        console.error('分享失败:', error);
        ElMessage.success('链接已复制到剪贴板');
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
 * 加载素材详情
 */
const loadAssetDetail = async () => {
    try {
        loading.value = true;
        
        const assetId = route.params.id as string;
        const assetType = route.params.type as string;
        
        // 根据类型调用不同的API
        let response;
        if (assetType === 'image') {
            response = await API.assetsImage.find(assetId);
        } else if (assetType === 'video') {
            response = await API.assetsVideo.find(assetId);
        } else {
            response = await API.assetsDoc.find(assetId);
        }
        
        assetDetail.value = { ...response.data, type: assetType };
        
        // 加载收藏状态
        await loadFavoriteStatus();
        
        // 加载相关推荐
        await loadRelatedAssets();
        
    } catch (error) {
        console.error('加载素材详情失败:', error);
        ElMessage.error('加载失败，请重试');
    } finally {
        loading.value = false;
    }
};

/**
 * 加载相关推荐
 */
const loadRelatedAssets = async () => {
    try {
        const assetType = route.params.type as string;
        
        // 根据类型获取相关素材
        let response;
        if (assetType === 'image') {
            response = await API.assetsImage.query({ size: 6 });
        } else if (assetType === 'video') {
            response = await API.assetsVideo.query({ size: 6 });
        } else {
            response = await API.assetsDoc.query({ size: 6 });
        }
        
        const assets = response.data?.records || response.data || [];
        relatedAssets.value = assets
            .filter(asset => asset.id !== route.params.id)
            .slice(0, 5)
            .map(asset => ({ ...asset, type: assetType }));
            
    } catch (error) {
        console.error('加载相关推荐失败:', error);
    }
};

onMounted(() => {
    loadAssetDetail();
});
</script>

<style scoped>
.asset-detail-page {
    background-color: #f5f5f5;
    min-height: 100vh;
    padding-bottom: 80px;
}

/* 素材展示区域 */
.asset-media {
    background: white;
    padding: 16px;
}

.image-container {
    text-align: center;
}

.image-container img {
    max-width: 100%;
    max-height: 300px;
    border-radius: 8px;
    cursor: pointer;
}

.video-container {
    text-align: center;
}

.video-container video {
    width: 100%;
    max-height: 300px;
    border-radius: 8px;
}

.doc-container {
    text-align: center;
    padding: 40px 20px;
}

.doc-preview {
    display: inline-block;
    padding: 20px;
    background: #f8f9fa;
    border-radius: 8px;
    border: 2px dashed #ddd;
}

.doc-icon {
    font-size: 64px;
    color: #666;
    margin-bottom: 12px;
}

.doc-name {
    margin: 0 0 8px 0;
    font-size: 16px;
    color: #333;
    font-weight: 500;
}

.doc-size {
    margin: 0;
    font-size: 14px;
    color: #999;
}

/* 素材信息 */
.asset-info {
    background: white;
    margin-top: 8px;
    padding: 16px;
}

.asset-title {
    margin: 0 0 16px 0;
    font-size: 20px;
    color: #333;
    line-height: 1.4;
}

.asset-meta {
    margin-bottom: 20px;
}

.meta-item {
    display: flex;
    margin-bottom: 8px;
    font-size: 14px;
}

.meta-label {
    color: #666;
    width: 80px;
    flex-shrink: 0;
}

.meta-value {
    color: #333;
    flex: 1;
}

.asset-description h3,
.asset-tags h3 {
    margin: 0 0 12px 0;
    font-size: 16px;
    color: #333;
}

.asset-description p {
    margin: 0;
    line-height: 1.6;
    color: #666;
}

.tags-list {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
}

.tag {
    padding: 4px 12px;
    background: #f0f8ff;
    color: #409eff;
    border-radius: 16px;
    font-size: 12px;
}

/* 作者信息 */
.author-info {
    background: white;
    margin-top: 8px;
    padding: 16px;
}

.author-info h3 {
    margin: 0 0 12px 0;
    font-size: 16px;
    color: #333;
}

.author-card {
    display: flex;
    gap: 12px;
}

.author-avatar {
    width: 48px;
    height: 48px;
    border-radius: 50%;
    overflow: hidden;
    flex-shrink: 0;
}

.author-avatar img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.default-avatar {
    width: 100%;
    height: 100%;
    background: #f0f0f0;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #999;
    font-size: 24px;
}

.author-details {
    flex: 1;
}

.author-name {
    margin: 0 0 4px 0;
    font-size: 16px;
    color: #333;
    font-weight: 500;
}

.author-department {
    margin: 0 0 8px 0;
    font-size: 14px;
    color: #666;
}

.author-description {
    margin: 0;
    font-size: 14px;
    color: #999;
    line-height: 1.4;
}

/* 相关推荐 */
.related-assets {
    background: white;
    margin-top: 8px;
    padding: 16px;
}

.related-assets h3 {
    margin: 0 0 12px 0;
    font-size: 16px;
    color: #333;
}

.related-list {
    display: flex;
    gap: 12px;
    overflow-x: auto;
    padding-bottom: 8px;
}

.related-item {
    flex-shrink: 0;
    width: 100px;
    cursor: pointer;
}

.related-thumbnail {
    width: 100px;
    height: 80px;
    border-radius: 6px;
    overflow: hidden;
    background: #f0f0f0;
    margin-bottom: 8px;
}

.related-thumbnail img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.doc-thumb {
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #999;
    font-size: 24px;
}

.related-title {
    margin: 0;
    font-size: 12px;
    color: #333;
    line-height: 1.3;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
}

/* 底部操作栏 */
.bottom-actions {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    background: white;
    padding: 12px 16px;
    border-top: 1px solid #eee;
    display: flex;
    justify-content: space-between;
    align-items: center;
    z-index: 1000;
}

.action-left {
    display: flex;
    gap: 20px;
}

.action-btn {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 4px;
    cursor: pointer;
    color: #666;
    font-size: 12px;
    transition: color 0.2s;
}

.action-btn.active {
    color: #ff4757;
}

.action-btn:active {
    transform: scale(0.95);
}

.action-right {
    display: flex;
    gap: 8px;
}

.action-right .el-button {
    border-radius: 20px;
    font-size: 14px;
    padding: 8px 16px;
}

.action-right .el-button .i-icon {
    margin-right: 4px;
}
</style>
<template>
    <div class="asset-detail-page">
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
                    <h1 class="nav-title">{{ assetDetail.title }}</h1>
                </div>
                <div class="nav-right">
                    <!-- 预留右侧操作区域 -->
                </div>
            </div>
        </div>

        <!-- 素材展示区域 -->
        <div class="asset-media-section">
            <!-- 图片素材 -->
            <div v-if="getAssetType() === 'image'" class="media-container image-container">
                <img :src="assetDetail.url" :alt="assetDetail.title" class="cover-image" />
                <div class="media-overlay">
                    <div class="file-type-badge">
                        <Picture class="type-icon" />
                        <span class="file-format">{{ assetDetail.fileFormat?.toUpperCase() }}</span>
                    </div>
                </div>
            </div>
            
            <!-- 视频素材 -->
            <div v-else-if="getAssetType() === 'video'" class="media-container video-container">
                <div class="video-wrapper" @click="toggleVideoPlay">
                    <video 
                        v-if="assetDetail.url"
                        ref="videoPlayer"
                        :src="assetDetail.url" 
                        :poster="assetDetail.coverUrl"
                        preload="metadata"
                        @loadedmetadata="onVideoLoaded"
                        @play="isVideoPlaying = true"
                        @pause="isVideoPlaying = false"
                        class="video-player cover-image"
                    >
                        您的浏览器不支持视频播放
                    </video>
                    <img v-else-if="assetDetail.coverUrl" :src="assetDetail.coverUrl" :alt="assetDetail.title" class="cover-image" />
                    
                    <!-- 播放按钮覆盖层 -->
                    <div v-if="!isVideoPlaying" class="play-overlay">
                        <div class="play-button">
                            <Play class="play-icon" />
                        </div>
                    </div>
                </div>
                <div class="media-overlay">
                    <div class="file-type-badge">
                        <Video class="type-icon" />
                        <span class="file-format">{{ assetDetail.fileFormat?.toUpperCase() }}</span>
                    </div>
                </div>
            </div>
            
            <!-- 文档素材 -->
            <div v-else class="media-container doc-container">
                <img :src="assetDetail.coverUrl" :alt="assetDetail.title" class="cover-image" />
                <div class="media-overlay">
                    <div class="file-type-badge">
                        <Picture class="type-icon" />
                        <span class="file-format">{{ assetDetail.fileFormat?.toUpperCase() }}</span>
                    </div>
                </div>
            </div>
        </div>

        <!-- 素材信息卡片 -->
        <div class="info-card">
            <h1 class="asset-title">{{ assetDetail.title }}</h1>
            
            <div class="asset-meta">
                <div class="meta-row">
                    <div class="meta-item">
                        <span class="meta-label">大小</span>
                        <span class="meta-value">{{ formatFileSize(assetDetail.fileSize) }}</span>
                    </div>
                    <div v-if="assetDetail.resolutionRatio" class="meta-item">
                        <span class="meta-label">分辨率</span>
                        <span class="meta-value">{{ assetDetail.resolutionRatio }}</span>
                    </div>
                </div>
                
                <div class="meta-row">
                    <div class="meta-item">
                        <span class="meta-label">浏览次数</span>
                        <span class="meta-value">{{ assetDetail.viewCount || 0 }}次</span>
                    </div>
                    <div class="meta-item">
                        <span class="meta-label">下载次数</span>
                        <span class="meta-value">{{ assetDetail.downloadCount || 0 }}次</span>
                    </div>
                </div>
                <div class="meta-row">
                    <div class="meta-item">
                        <span class="meta-label">上传时间</span>
                        <span class="meta-value">{{ formatDate(assetDetail.createdAt) }}</span>
                    </div>
                    <div v-if="assetDetail.updatedAt !== assetDetail.createdAt" class="meta-item">
                        <span class="meta-label">更新时间</span>
                        <span class="meta-value">{{ formatDate(assetDetail.updatedAt) }}</span>
                    </div>
                </div>
                <div v-if="getAuthorName()" class="meta-row">
                    <div class="meta-item">
                        <span class="meta-label">作者</span>
                        <span class="meta-value">{{ getAuthorName() }}</span>
                    </div>
                </div>
            </div>
            
            <!-- 分隔线 -->
            <div v-if="assetDetail.description" class="divider"></div>
            
            <!-- 作品简介 -->
            <div v-if="assetDetail.description" class="asset-description">
                作品简介：{{ assetDetail.description }}
            </div>
        </div>

        <!-- 底部操作栏 -->
        <div class="bottom-actions">
            <div class="action-left">
                <div class="action-btn" @click="goHome">
                    <Home class="action-icon" />
                    <span>首页</span>
                </div>
                <div class="action-btn" :class="{ active: isFavourited }" @click="toggleFavorite">
                    <Star v-if="isFavourited" theme="filled" class="action-icon" />
                    <Star v-else class="action-icon" />
                    <span>{{ isFavourited ? '已收藏' : '收藏' }}</span>
                </div>
            </div>
            <div class="action-right">
                <el-button type="primary" @click="downloadAsset" :loading="downloading" class="download-btn">
                    <Download class="btn-icon" />
                    {{ downloading ? '下载中...' : '立即下载' }}
                </el-button>
                <el-button @click="shareAsset" class="share-btn">
                    <Share class="btn-icon" />
                    分享
                </el-button>
            </div>
        </div>


    </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted, nextTick } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { ElMessage } from 'element-plus';
import { 
    FileDoc, 
    Picture,
    Video,
    Play,
    Home, 
    Star,
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
const isFavourited = ref(false);

const isVideoPlaying = ref(false);
const videoPlayer = ref<HTMLVideoElement | null>(null);

/**
 * 返回上一页
 */
const goBack = () => {
    router.back();
};

/**
 * 获取素材类型
 */
const getAssetType = () => {
    // 直接从路由参数获取 assetType
    const assetType = route.params.type as string;
    return assetType || 'image'; // 默认为图片
};

/**
 * 将字符串类型转换为数字类型
 * @param assetsType 素材类型字符串
 * @returns 素材类型数字
 */
const getAssetsTypeNumber = (assetsType: string): number => {
    const typeMap: { [key: string]: number } = {
        'image': 10,
        'video': 20,
        'doc': 30
    };
    return typeMap[assetsType] || 10;
};



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
 * 格式化文件大小
 * @param size 文件大小（字节）
 */
const formatFileSize = (size: number) => {
    if (!size || size === 0) return '0 B';
    
    const units = ['B', 'KB', 'MB', 'GB'];
    let index = 0;
    let fileSize = size;
    
    while (fileSize >= 1024 && index < units.length - 1) {
        fileSize /= 1024;
        index++;
    }
    
    // 对于字节，显示整数；其他单位显示一位小数
    if (index === 0) {
        return `${Math.round(fileSize)} ${units[index]}`;
    } else {
        return `${fileSize.toFixed(1)} ${units[index]}`;
    }
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
 * @param timestamp 时间戳（毫秒）
 */
const formatDate = (timestamp: number) => {
    if (!timestamp) return '未知';
    return new Date(timestamp).toLocaleDateString('zh-CN');
};

/**
 * 获取作者名称（creatorDepartment第3级的name）
 * @returns 作者名称
 */
const getAuthorName = () => {
    if (!assetDetail.value.creatorDepartment) return '';
    
    let current = assetDetail.value.creatorDepartment;
    let level = 1;
    
    // 遍历到第3级
    while (current && level < 3) {
        if (current.child) {
            current = current.child;
            level++;
        } else {
            break;
        }
    }
    
    // 如果到达第3级，返回name，否则返回空字符串
    return level === 3 && current ? current.name : '';
};



/**
 * 切换视频播放状态
 */
const toggleVideoPlay = () => {
    if (videoPlayer.value) {
        if (isVideoPlaying.value) {
            videoPlayer.value.pause();
        } else {
            videoPlayer.value.play();
        }
    }
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
    try {
        const assetId = route.params.id as string;
        const assetType = getAssetType();
        const assetsTypeNumber = getAssetsTypeNumber(assetType);
        
        if (isFavourited.value) {
            // 取消收藏
            await API.assetsFavourite.removeFavorite(assetId, assetsTypeNumber);
            isFavourited.value = false;
            ElMessage.success('已取消收藏');
        } else {
            // 添加收藏
            await API.assetsFavourite.addFavorite(assetId, assetsTypeNumber);
            isFavourited.value = true;
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
    try {
        downloading.value = true;
        
        const assetId = route.params.id as string;
        const assetType = getAssetType();
        
        // 获取素材类型对应的数字编码
        const getAssetTypeCode = () => {
            switch (assetType) {
                case 'image': return 10;
                case 'video': return 20;
                case 'doc': return 30;
                default: return 10;
            }
        };
        
        // 1. 创建下载记录（失败不影响实际下载）
        try {
            await API.assetsDownload.createDownloadRecord(assetId, getAssetTypeCode());
        } catch (recordError) {
            console.warn('创建下载记录失败:', recordError);
        }
        
        // 2. 下载文件
        if (assetDetail.value.url) {
            // 从URL中提取文件扩展名
            const getFileExtension = (url: string) => {
                try {
                    const cleanUrl = url.split('?')[0].split('#')[0];
                    const lastDotIndex = cleanUrl.lastIndexOf('.');
                    if (lastDotIndex > -1) {
                        return cleanUrl.substring(lastDotIndex);
                    }
                    return '';
                } catch {
                    return '';
                }
            };
            
            // 构建下载文件名：title + 扩展名
            const fileExtension = getFileExtension(assetDetail.value.url);
            const fileName = assetDetail.value.title 
                ? `${assetDetail.value.title}${fileExtension}` 
                : `download${fileExtension}`;
            
            try {
                // 用 fetch + blob 下载，避免服务端强制文件名
                const res = await fetch(assetDetail.value.url, { mode: 'cors' });
                const blob = await res.blob();
                const blobUrl = URL.createObjectURL(blob);

                const link = document.createElement('a');
                link.href = blobUrl;
                link.download = fileName;
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);

                URL.revokeObjectURL(blobUrl);

                // 更新下载次数
                assetDetail.value.downloadCount = (assetDetail.value.downloadCount || 0) + 1;
                ElMessage.success('下载开始');
            } catch (fetchErr) {
                console.error('文件下载失败:', fetchErr);
                ElMessage.error('下载失败，请重试');
            }
        } else {
            ElMessage.error('素材文件不存在');
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
        const assetType = getAssetType();
        const assetsTypeNumber = getAssetsTypeNumber(assetType);
        
        const response = await API.assetsFavourite.checkFavorite(assetId, assetsTypeNumber);
        
        // 新接口返回 { isFavourited: boolean } 格式
        isFavourited.value = response.isFavourited || false;
    } catch (error) {
        console.error('加载收藏状态失败:', error);
        // 出错时默认为未收藏状态
        isFavourited.value = false;
    }
};

/**
 * 复制文本到剪贴板
 * @param text 要复制的文本
 */
const copyToClipboard = (text: string): Promise<void> => {
    return new Promise((resolve, reject) => {
        if (navigator.clipboard && window.isSecureContext) {
            navigator.clipboard.writeText(text)
                .then(() => resolve())
                .catch(err => reject(err));
        } else {
            // Fallback for older browsers or non-secure contexts
            const textArea = document.createElement('textarea');
            textArea.value = text;
            textArea.style.position = 'fixed';
            textArea.style.left = '-9999px';
            textArea.style.top = '-9999px';
            document.body.appendChild(textArea);
            textArea.focus();
            textArea.select();
            try {
                const successful = document.execCommand('copy');
                if (successful) {
                    resolve();
                } else {
                    reject(new Error('Fallback: Copying text command was unsuccessful'));
                }
            } catch (err) {
                reject(err);
            }
            document.body.removeChild(textArea);
        }
    });
};

/**
 * 分享素材
 */
const shareAsset = async () => {
    try {
        const shareUrl = window.location.href;
        await copyToClipboard(shareUrl);
        ElMessage.success('链接已复制到剪贴板');
    } catch (error) {
        console.error('分享失败:', error);
        ElMessage.error('复制失败，请重试');
    }
};



/**
 * 加载素材详情
 */
const loadAssetDetail = async () => {
    try {
        loading.value = true;
        
        const assetId = route.params.id as string;
        const assetType = getAssetType();
        
        let response;
        // 根据类型调用不同的接口
        if (assetType === 'image') {
            response = await API.assetsImage.find(assetId);
        } else if (assetType === 'video') {
            response = await API.assetsVideo.find(assetId);
        } else if (assetType === 'doc') {
            response = await API.assetsDoc.find(assetId);
        } else {
            throw new Error('未知的素材类型');
        }
        
        assetDetail.value = response.data || response;
        
        // 加载收藏状态
        await loadFavoriteStatus();
        
    } catch (error) {
        console.error('加载素材详情失败:', error);
        ElMessage.error('加载失败，请重试');
    } finally {
        loading.value = false;
    }
};



onMounted(() => {
    loadAssetDetail();
});
</script>

<style scoped>
/* 页面容器 */
.asset-detail-page {
    min-height: 100vh;
    background-color: #f5f5f5;
    padding-bottom: 100px; /* 为底部操作栏留出空间 */
}

/* 头部区域 */
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

.nav-left, .nav-right {
    flex: 0 0 40px; /* 固定等宽占位，保证标题居中 */
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

/* 素材展示区域 */
.asset-media-section {
    margin: 16px;
    background: white;
    border-radius: 16px;
    overflow: hidden;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.media-container {
    position: relative;
    height: 200px;
    display: flex;
    align-items: center;
    justify-content: center;
    background: #f8f9fa;
}

.media-overlay {
    position: absolute;
    top: 12px;
    right: 12px;
}

.file-type-badge {
    display: flex;
    align-items: center;
    gap: 4px;
    padding: 6px 12px;
    background: rgba(0, 0, 0, 0.7);
    color: white;
    border-radius: 20px;
    font-size: 12px;
    font-weight: 500;
}

.type-icon {
    font-size: 14px;
}

/* 信息卡片 */
.info-card {
    margin: 16px;
    background: white;
    border-radius: 16px;
    padding: 20px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.asset-title {
    margin: 0 0 16px 0;
    font-size: 24px;
    font-weight: 600;
    color: #333;
    line-height: 1.3;
}

.divider {
    height: 1px;
    background-color: #e5e5e5;
    margin: 12px 0 12px 0;
}

.asset-description {
    margin: 12px 0 0 0;
    font-size: 14px;
    color: #666;
    line-height: 1.6;
}

.asset-meta {
    padding-top: 12px;
}

.meta-row {
    display: flex;
    gap: 20px;
    margin-bottom: 8px;
}

.meta-row:last-child {
    margin-bottom: 0;
}

.meta-item {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 2px;
}

.meta-label {
    font-size: 12px;
    color: #999;
    font-weight: 500;
}

.meta-value {
    font-size: 14px;
    color: #333;
    font-weight: 500;
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
    border-radius: 8px;
    cursor: pointer;
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.video-container {
    text-align: center;
}

.video-wrapper {
    position: relative;
    width: 100%;
    height: 100%;
    cursor: pointer;
}

.video-player {
    width: 100%;
    height: 100%;
    object-fit: cover;
    border-radius: 12px;
}

.play-overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    display: flex;
    align-items: center;
    justify-content: center;
    background: rgba(0, 0, 0, 0.3);
    border-radius: 12px;
    transition: opacity 0.3s;
}

.play-button {
    width: 60px;
    height: 60px;
    background: rgba(255, 255, 255, 0.9);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.3s;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.play-button:hover {
    background: white;
    transform: scale(1.1);
}

.play-icon {
    font-size: 24px;
    color: #333;
    margin-left: 2px; /* 视觉居中调整 */
}

.doc-container img {
    width: 100%;
    height: 100%;
}

.doc-container {
    text-align: center;
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
    margin: 0;
    font-size: 20px;
    color: #333;
    line-height: 1.4;
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
    background: rgba(255, 255, 255, 0.95);
    backdrop-filter: blur(10px);
    padding: 16px 0;
    border-top: 1px solid #f0f0f0;
    display: flex;
    justify-content: space-between;
    align-items: center;
    z-index: 1000;
    box-shadow: 0 -2px 20px rgba(0, 0, 0, 0.08);
}

.action-left {
    display: flex;
    width: 100%;
    justify-content: space-evenly;
}

.action-btn {
    display: flex;
    flex-direction: column;
    align-items: center;
    cursor: pointer;
    color: #333;
    font-size: 12px;
    font-weight: 500;
    transition: all 0.2s;
}

.action-btn:hover {
    color: var(--p-color-green-primary);
}

.action-btn:hover .action-icon {
    color: var(--p-color-green-primary);
}

.action-btn.active {
    color: var(--p-color-green-primary) !important;
}

.action-btn.active .action-icon {
    color: var(--p-color-green-primary) !important;
}

.action-btn.active span {
    color: var(--p-color-green-primary) !important;
}

.action-icon {
    font-size: 28px;
    color: inherit;
    transition: color 0.2s;
}

.action-right {
    display: flex;
    gap: 12px;
    padding-right: 16px;
}

.action-right .el-button {
    border-radius: 24px;
    font-size: 15px;
    height: 48px;
    padding: 0 24px;
    font-weight: 500;
    border: none;
    transition: all 0.3s ease;
}

.download-btn {
    background: linear-gradient(135deg, var(--p-color-green-primary) 0%, #4ade80 100%);
    color: white;
    box-shadow: 0 4px 12px rgba(94, 189, 49, 0.3);
}

.download-btn:hover {
    background: linear-gradient(135deg, var(--p-color-green-hover) 0%, #22c55e 100%);
    transform: translateY(-2px);
    box-shadow: 0 6px 16px rgba(94, 189, 49, 0.4);
}

.share-btn {
    background: rgba(94, 189, 49, 0.1);
    color: var(--p-color-green-primary);
}

.share-btn:hover {
    background: rgba(94, 189, 49, 0.2);
}

.btn-icon {
    font-size: 16px;
    margin-right: 6px;
}

.action-right .el-button .i-icon {
    margin-right: 4px;
}
</style>
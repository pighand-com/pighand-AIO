<template>
    <div class="home-page">
        <!-- 搜索栏 -->
        <div class="search-bar">
            <div class="search-container">
                <h2 class="site-title">科普资料库</h2>
                <el-input
                    v-model="searchKeyword"
                    placeholder="请输入关键词搜索"
                    class="search-input"
                    @click="goToSearch"
                    readonly
                >
                    <template #suffix>
                        <el-button type="primary" @click="goToSearch" class="search-btn">
                            搜索
                        </el-button>
                    </template>
                </el-input>
            </div>
        </div>

        <!-- 顶部3个图标 -->
        <div class="top-icons-section">
            <div class="top-icons-grid">
                <div 
                    class="top-icon-item"
                    @click="goToAssetsList({ id: 1, type: 'image' })"
                >
                    <div class="top-icon">
                        <Picture />
                    </div>
                    <span class="top-icon-name">图片</span>
                </div>
                <div 
                    class="top-icon-item"
                    @click="goToAssetsList({ id: 2, type: 'video' })"
                >
                    <div class="top-icon">
                        <Video />
                    </div>
                    <span class="top-icon-name">视频</span>
                </div>
                <div 
                    class="top-icon-item"
                    @click="goToAssetsList({ id: 3, type: 'doc' })"
                >
                    <div class="top-icon">
                        <FileDoc />
                    </div>
                    <span class="top-icon-name">课件</span>
                </div>
            </div>
        </div>

        <!-- 精选列表 -->
        <div class="section">
            <div class="section-header">
                <h3>精选专题</h3>
            </div>
            <div class="featured-scroll">
                <div 
                    v-for="collection in collections" 
                    :key="collection.id"
                    class="featured-item"
                    @click="goToCollectionDetail(collection)"
                >
                    <div class="featured-image">
                        <img :src="collection.coverUrl || '/placeholder-image.jpg'" :alt="collection.name" />
                        <div class="featured-overlay">
                            {{ collection.name }}
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- 时片精选 -->
        <div class="section">
            <div class="section-header">
                <h3>图片精选</h3>
                <span class="more-btn" @click="goToAssetsList({ type: 'image' })">查看更多 >></span>
            </div>
            <div class="assets-grid">
                <div 
                    v-for="image in featuredImages" 
                    :key="image.id"
                    class="asset-item"
                    @click="goToAssetDetail(image)"
                >
                    <img :src="image.url" :alt="image.title" />
                    <p class="asset-title">{{ image.title }}</p>
                </div>
            </div>
        </div>

        <!-- 视频精选 -->
        <div class="section">
            <div class="section-header">
                <h3>视频精选</h3>
                <span class="more-btn" @click="goToAssetsList({ type: 'video' })">查看更多 >></span>
            </div>
            <div class="assets-grid">
                <div 
                    v-for="video in featuredVideos" 
                    :key="video.id"
                    class="asset-item"
                    @click="goToAssetDetail(video)"
                >
                    <div class="video-thumbnail">
                        <img :src="video.coverUrl" :alt="video.title" />
                    </div>
                    <p class="asset-title">{{ video.title }}</p>
                </div>
            </div>
        </div>

        <!-- 课件精选 -->
        <div class="section">
            <div class="section-header">
                <h3>课件精选</h3>
                <span class="more-btn" @click="goToAssetsList({ type: 'doc' })">查看更多 >></span>
            </div>
            <div class="assets-grid">
                <div 
                    v-for="doc in featuredDocs" 
                    :key="doc.id"
                    class="asset-item"
                    @click="goToAssetDetail(doc)"
                >
                    <div class="doc-thumbnail">
                        <img v-if="doc.coverUrl" :src="doc.coverUrl" :alt="doc.title" />
                        <FileDoc v-else class="doc-icon" />
                    </div>
                    <p class="asset-title">{{ doc.title }}</p>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { Picture, Video, FileDoc, Play } from '@icon-park/vue-next';
import { useAuth, Permission } from '@/common/auth';
import * as API from '@/api';

const router = useRouter();
const { checkPermission } = useAuth();

// 响应式数据
const searchKeyword = ref('');
const collections = ref([]);
const featuredImages = ref([]);
const featuredVideos = ref([]);
const featuredDocs = ref([]);

/**
 * 跳转到搜索页面
 */
const goToSearch = () => {
    if (!checkPermission(Permission.ASSET_VIEW)) {
        return;
    }
    router.push({
        name: 'assetsList',
        query: { keyword: searchKeyword.value }
    });
};

/**
 * 跳转到素材列表页面
 * @param params 查询参数
 */
const goToAssetsList = (params: any) => {
    if (!checkPermission(Permission.ASSET_VIEW)) {
        return;
    }
    router.push({
        name: 'assetsList',
        query: params
    });
};

/**
 * 跳转到专题详情页面
 * @param collection 专题信息
 */
const goToCollectionDetail = (collection: any) => {
    router.push({
        name: 'assetsList',
        query: { collectionId: collection.id, collectionName: collection.name }
    });
};

/**
 * 跳转到素材详情页面
 * @param asset 素材信息
 */
const goToAssetDetail = (asset: any) => {
    if (!checkPermission(Permission.ASSET_VIEW)) {
        return;
    }
    router.push({
        name: 'assetDetail',
        params: { id: asset.id, type: asset.type || 'image' }
    });
};

/**
 * 加载精选专题数据
 */
const loadCollections = async () => {
    try {
        const response = await API.assetsCollection.query();
        collections.value = response || [];
    } catch (error) {
        console.error('加载精选专题失败:', error);
    }
};

/**
 * 加载精选图片
 */
const loadFeaturedImages = async () => {
    try {
        const response = await API.assetsImage.query({ handpick: true });
        featuredImages.value = response?.records || response || [];
    } catch (error) {
        console.error('加载精选图片失败:', error);
        featuredImages.value = [];
    }
};

/**
 * 加载精选视频
 */
const loadFeaturedVideos = async () => {
    try {
        const response = await API.assetsVideo.query({ handpick: true });
        featuredVideos.value = response?.records || response || [];
    } catch (error) {
        console.error('加载精选视频失败:', error);
        featuredVideos.value = [];
    }
};

/**
 * 加载精选课件
 */
const loadFeaturedDocs = async () => {
    try {
        const response = await API.assetsDoc.query({ handpick: true });
        featuredDocs.value = response?.records || response || [];
    } catch (error) {
        console.error('加载精选课件失败:', error);
        featuredDocs.value = [];
    }
};

/**
 * 初始化页面数据
 */
const initData = async () => {
    await Promise.all([
        loadCollections(),
        loadFeaturedImages(),
        loadFeaturedVideos(),
        loadFeaturedDocs()
    ]);
};

onMounted(() => {
    initData();
});
</script>

<style scoped>
.home-page {
    padding: 0;
    background-color: #f8fafb;
    min-height: 100vh;
}

/* 搜索栏样式 */
.search-bar {
    width: 100%;
    margin: 0 0 20px 0;
    background: white;
    padding: 20px 16px;
    border-radius: 0 0 24px 24px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    position: sticky;
    top: 0;
    z-index: 100;
}

.search-container {
    display: flex;
    align-items: center;
    gap: 16px;
}

.site-title {
    margin: 0;
    font-size: 20px;
    font-weight: 600;
    color: #333;
    white-space: nowrap;
    flex-shrink: 0;
}

.search-input {
    border-radius: 30px;
    flex: 1;
}

.search-input :deep(.el-input__wrapper) {
    border-radius: 30px;
    padding-right: 0;
}

.search-btn {
    border-radius: 0 30px 30px 0;
    border-left: none;
    background-color: var(--p-color-green-primary);
    border-color: var(--p-color-green-primary);
}

.search-btn:hover {
    background-color: var(--p-color-green-hover);
    border-color: var(--p-color-green-hover);
}

/* 顶部图标样式 */
.top-icons-section {
    margin-bottom: 24px;
    padding: 0 16px;
}

.top-icons-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 16px;
    padding: 20px;
    background: white;
    border-radius: 20px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.top-icon-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    cursor: pointer;
    transition: transform 0.2s;
}

.top-icon-item:active {
    transform: scale(0.95);
}

.top-icon {
    width: 50px;
    height: 50px;
    border-radius: 50%;
    background: linear-gradient(135deg, var(--p-color-green-light) 0%, var(--p-color-green-primary) 30%, var(--p-color-green-hover) 70%, var(--p-color-green-dark) 100%);
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 24px;
    color: white;
    margin-bottom: 10px;
    box-shadow: 0 4px 12px rgba(94, 189, 49, 0.3);
}

.top-icon-name {
    font-size: 13px;
    font-weight: 500;
    color: #333;
    text-align: center;
}

/* 区块样式 */
.section {
    margin-bottom: 24px;
    padding: 0 16px;
}

.section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12px;
}

.section-header h3 {
    margin: 0;
    font-size: 16px;
    font-weight: 500;
    color: #666;
}

.more-btn {
    font-size: 14px;
    color: var(--p-color-green-primary);
    cursor: pointer;
}

/* 精选列表横向滚动 */
.featured-scroll {
    display: flex;
    gap: 8px;
    overflow-x: auto;
    padding-bottom: 8px;
    scrollbar-width: none; /* Firefox */
    -ms-overflow-style: none; /* IE and Edge */
}

.featured-scroll::-webkit-scrollbar {
    display: none; /* Chrome, Safari and Opera */
}

.featured-item {
    flex-shrink: 0;
    width: 30%;
    cursor: pointer;
    border: 2px solid white;
    border-radius: 12px;
    overflow: hidden;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.featured-image {
    position: relative;
    width: 100%;
    height: 72px;
    border-radius: 8px;
    overflow: hidden;
}

.featured-image img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.featured-overlay {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    height: 100%;
    padding: 8px 6px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-weight: 600;
    font-size: 18px;
    color: #333;
    background: linear-gradient(to top, rgba(255, 255, 255, 0.8), rgba(255, 255, 255, 0.1), transparent);
    text-shadow: 1px 1px 2px rgba(255, 255, 255, 0.8);
    text-align: center;
    line-height: 1.2;
}

/* 素材网格样式 */
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

.asset-item img {
    width: 100%;
    height: 120px;
    object-fit: cover;
}

.video-thumbnail {
    position: relative;
}

.play-icon {
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

.doc-thumbnail {
    height: 120px;
    display: flex;
    align-items: center;
    justify-content: center;
    background: #f0f0f0;
    overflow: hidden;
}

.doc-thumbnail img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.doc-icon {
    font-size: 48px;
    color: #666;
}

.asset-title {
    padding: 12px;
    margin: 0;
    font-size: 14px;
    color: #333;
    line-height: 1.4;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
}
</style>
<template>
    <div class="assets-video-page">
        <!-- 搜索区域 -->
        <PSearch :handle-query="queryData" />

        <!-- 添加按钮 -->
        <div class="action-section mt-4 mb-4">
            <el-button type="primary" :icon="Plus" @click="handleAdd">
                添加
            </el-button>
        </div>

        <!-- 卡片列表 -->
        <div class="card-grid" v-loading="loading">
            <div
                v-for="item in videoList"
                :key="item.id"
                class="video-card">
                <!-- 视频区域 -->
                <div class="video-container">
                    <video
                        v-if="item.url"
                        :src="item.url"
                        :poster="item.coverUrl"
                        controls
                        preload="metadata"
                        class="card-video">
                        您的浏览器不支持视频播放。
                    </video>
                    <div v-else class="video-placeholder">
                        <el-icon size="48"><Play /></el-icon>
                        <span>暂无视频</span>
                    </div>
                    <!-- 文件格式标签 -->
                    <div class="format-tag" v-if="item.fileFormat">
                        {{ item.fileFormat.toUpperCase() }}
                    </div>
                </div>
                
                <!-- 内容区域 -->
                <div class="card-content">
                    <!-- 标题和操作按钮 -->
                    <div class="title-actions">
                        <h3 class="card-title" :class="{ 'disabled-title': item.status === 20 || item.status === null, 'featured-title': item.handpick === true }">
                            <Star v-if="(item.status === 10) && item.handpick === true" theme="filled" size="12" fill="#f7ba2a" class="featured-icon" />
                            <Forbid v-if="item.status === 20 || item.status === null" theme="outline" size="12" fill="#909399" class="disabled-icon" />
                            <el-tooltip :content="item.title" placement="top" effect="light" :disabled="!isTitleOverflow(item.title)">
                                <span class="title-text">{{ item.title }}</span>
                            </el-tooltip>
                        </h3>
                        <div class="action-buttons">
                            <!-- 编辑按钮 -->
                            <el-tooltip content="编辑" placement="top" effect="light">
                                <el-button 
                                    type="text" 
                                    size="small" 
                                    @click="handleEdit(item)"
                                    class="action-btn edit-btn">
                                    <Edit theme="outline" size="16" fill="#409eff" />
                                </el-button>
                            </el-tooltip>
                            
                            <!-- 精选按钮 -->
                            <el-tooltip v-if="item.status === 10 && (item.handpick === false || item.handpick === null)" content="精选" placement="top" effect="light">
                                <el-button 
                                    type="text" 
                                    size="small" 
                                    @click="handleSetHandpick(item, true)"
                                    class="action-btn handpick-btn">
                                    <Star theme="outline" size="16" fill="#f7ba2a" strokeLinejoin="bevel" />
                                </el-button>
                            </el-tooltip>
                            <el-tooltip v-if="item.status === 10 && item.handpick === true" content="取消精选" placement="top" effect="light">
                                <el-button 
                                    type="text" 
                                    size="small" 
                                    @click="handleSetHandpick(item, false)"
                                    class="action-btn cancel-handpick-btn">
                                    <Star theme="filled" size="16" fill="#f7ba2a" strokeLinejoin="bevel" />
                                </el-button>
                            </el-tooltip>
                            
                            <!-- 上下架按钮 -->
                            <el-tooltip v-if="item.status === 10" content="下架" placement="top" effect="light">
                                <el-button 
                                    type="text" 
                                    size="small" 
                                    @click="handleOffShelf(item)"
                                    class="action-btn off-shelf-btn">
                                    <Download theme="outline" size="16" fill="#8b5cf6" />
                                </el-button>
                            </el-tooltip>
                            <el-tooltip v-if="item.status === 20 || item.status === null" content="上架" placement="top" effect="light">
                                <el-button 
                                    type="text" 
                                    size="small" 
                                    @click="handleOnShelf(item)"
                                    class="action-btn on-shelf-btn">
                                    <Upload theme="outline" size="16" fill="#10b981" />
                                </el-button>
                            </el-tooltip>
                            
                            <!-- 删除按钮 -->
                            <el-tooltip content="删除" placement="top" effect="light">
                                <el-button 
                                    type="text" 
                                    size="small" 
                                    @click="handleDelete(item)"
                                    class="action-btn delete-btn">
                                    <Delete theme="outline" size="16" fill="#f56c6c" />
                                </el-button>
                            </el-tooltip>
                        </div>
                    </div>
                    
                    <!-- 展开按钮 -->
                    <div v-if="!expandedCards.includes(item.id)" class="expand-section">
                        <el-button
                            type="text"
                            @click="toggleExpand(item.id)"
                            class="expand-btn">
                            <Down 
                                 class="expand-icon"
                                 theme="outline" 
                                 size="16" />
                        </el-button>
                    </div>
                    
                    <!-- 详细信息 -->
                    <div v-if="expandedCards.includes(item.id)" class="detail-info">
                        <!-- 描述信息（如果有的话，单独一行） -->
                        <div class="info-item full-width" v-if="item.description">
                            <span class="label">描述：</span>
                            <span class="value">{{ item.description }}</span>
                        </div>

                        <!-- 分类信息（如果有的话，单独一行） -->
                        <div class="info-item full-width" v-if="item.classification">
                            <span class="label">分类：</span>
                            <span class="value">{{ item.classification.name }}</span>
                        </div>

                        <!-- 专辑信息（如果有的话，单独一行） -->
                        <div class="info-item full-width" v-if="item.collections && item.collections.length > 0">
                            <span class="label">专辑：</span>
                            <div class="collections-container">
                                <el-tag 
                                    v-for="collection in item.collections" 
                                    :key="collection.id"
                                    size="small"
                                    type="info"
                                    class="collection-tag">
                                    {{ collection.collectionName }}
                                </el-tag>
                            </div>
                        </div>

                        <!-- 基础信息（两列布局） -->
                        <div class="info-grid">
                            <div class="info-item">
                                <span class="label">文件大小：</span>
                                <span class="value">{{ formatFileSize(item.fileSize) }}</span>
                            </div>
                            <div class="info-item">
                                <span class="label">分辨率：</span>
                                <span class="value">{{ item.resolutionRatio || '-' }}</span>
                            </div>
                            <div class="info-item">
                                <span class="label">浏览量：</span>
                                <span class="value">{{ item.viewCount || 0 }}</span>
                            </div>
                            <div class="info-item">
                                <span class="label">下载量：</span>
                                <span class="value">{{ item.downloadCount || 0 }}</span>
                            </div>

                            <div class="info-item" v-if="item.creator">
                                <span class="label">创建人：</span>
                                <span class="value">{{ item.creator.name }}</span>
                            </div>
                        </div>

                        <!-- 时间信息（两列布局） -->
                        <div class="info-grid">
                            <div class="info-item">
                                <span class="label">创建时间：</span>
                                <span class="value">{{ formatDateTime(item.createdAt) }}</span>
                            </div>
                            <div class="info-item">
                                <span class="label">更新时间：</span>
                                <span class="value">{{ formatDateTime(item.updatedAt) }}</span>
                            </div>
                        </div>

                        <!-- 文件链接（单独一行） -->
                        <div class="info-item full-width" v-if="item.url">
                            <span class="label">文件链接：</span>
                            <div class="url-container">
                                <el-button 
                                    type="text" 
                                    size="small" 
                                    @click="copyUrl(item.url)"
                                    class="copy-btn">
                                    <Copy theme="outline" size="14" fill="#409eff" />
                                </el-button>
                                <a 
                                    :href="item.url" 
                                    target="_blank" 
                                    class="url-link"
                                    :title="item.url">
                                    {{ item.url }}
                                </a>
                            </div>
                        </div>
                        
                        <!-- 收起按钮 -->
                        <div class="collapse-section">
                            <el-button
                                type="text"
                                @click="toggleExpand(item.id)"
                                class="collapse-btn">
                                <Up 
                                     class="collapse-icon"
                                     theme="outline" 
                                     size="16" />
                            </el-button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- 空状态 -->
        <el-empty v-if="!loading && videoList.length === 0" description="暂无数据" />

        <!-- 分页 -->
        <div class="pagination-section" v-if="total > 0">
            <el-pagination
                v-model:current-page="pagination.pageNumber"
                v-model:page-size="pagination.pageSize"
                :page-sizes="[12, 24, 48, 96]"
                :total="total"
                layout="total, sizes, prev, pager, next, jumper"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange" />
        </div>

        <!-- Drawer组件 -->
        <PDrawer
            :title="getDetailOperation(detailFormModel).op === 'create' ? '添加视频' : '编辑视频'"
            :handle-create="handleCreate"
            :handle-update="handleUpdate"
            :handle-query="queryData"
            :handle-format-data="handleFormatData"
            size="60%" />
        
        <!-- 分类和专辑数据插槽 -->
        <template v-if="isOpenDetail">
            <div style="display: none;">
                {{ loadVideoClassifications() }}
                {{ loadCollections() }}
            </div>
        </template>
    </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, reactive } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Plus, Edit, Delete, Down, Copy, Forbid, Up, Upload, Download, Star, Play } from '@icon-park/vue-next';
import { assetsVideo, assetsClassification, assetsCollection } from '@/api/index.ts';
import provideForm from '@/common/provideForm';
import { formatFileSize } from '@/common/utils';
import moment from 'moment';

// 配置provideForm
const { searchFormModel, isOpenDetail, detailFormModel, getDetailOperation, watchDetailForm } = provideForm([
    {
        label: 'id',
        prop: 'id',
        isDetail: true,
        isPrimaryKey: true,
        hidden: true
    },
    {
        label: '视频标题',
        prop: 'title',
        isSearch: true,
        isSearchMore: false,
        searchPlaceholder: '模糊搜索',
        domType: 'input',
        isDetail: true,
        isCreate: true,
        isUpdate: true,
        rules: [
            { required: true, message: '视频标题必填', trigger: 'blur' },
            { max: 100, message: '最大长度为100', trigger: 'blur' }
        ]
    },
    {
        label: '视频文件',
        prop: 'url',
        isDetail: true,
        isCreate: true,
        isUpdate: true,
        domType: 'uploadFile',
        uploadPath: 'assets/video',
        rules: [
            { required: true, message: '视频文件必填', trigger: 'blur' }
        ],
        componentProps: {
            accept: 'video/*'
        },
    },
    {
        label: '视频封面',
        prop: 'coverUrl',
        isDetail: true,
        isCreate: true,
        isUpdate: true,
        domType: 'uploadImage',
        uploadPath: 'assets/video/cover',
        rules: [
            { required: true, message: '视频封面必填', trigger: 'blur' }
        ]
    },
    {
        label: '描述',
        prop: 'description',
        isDetail: true,
        isCreate: true,
        isUpdate: true,
        domType: 'input',
        componentProps: {
            rows: 3,
            placeholder: '请输入描述'
        },
        rules: [
            { max: 500, message: '最大长度为500', trigger: 'blur' }
        ]
    },
    {
        label: '分类',
        prop: 'classificationId',
        isDetail: true,
        isCreate: true,
        isUpdate: true,
        domType: 'select',
        domData: async (key) => {
            const result = await assetsClassification.query({ parentId: 2, name: key });
            return result.records.map((item) => ({
                label: item.name,
                value: item.id
            }));
        },
        componentProps: {
            clearable: true,
            placeholder: '请选择分类'
        },
        rules: [
            { required: true, message: '分类必填', trigger: 'blur' }
        ]
    },
    {
        label: '专辑',
        prop: 'collectionIds',
        isDetail: true,
        isCreate: true,
        isUpdate: true,
        domType: 'select',
        domData: async (key) => {
            const result = await assetsCollection.query({ status: 1, name: key });
            return result.records.map((item) => ({
                label: item.name,
                value: item.id
            }));
        },
        componentProps: {
            multiple: true,
            clearable: true,
            placeholder: '请选择专辑'
        }
    },
    {
        label: '是否精选',
        prop: 'handpick',
        isSearch: true,
        isSearchMore: false,
        domType: 'select',
        domData: [
            { label: '全部', value: '' },
            { label: '否', value: 0 },
            { label: '是', value: 1 }
        ]
    },
    {
        label: '文件格式',
        prop: 'fileFormat',
        isDetail: true,
        domType: 'input',
        componentProps: {
            disabled: true
        }
    },
    {
        label: '文件大小',
        prop: 'fileSize',
        isDetail: true,
        hidden: true,
        domType: 'input',
    },
    {
        label: '文件大小',
        prop: 'fileSizeFormat',
        isDetail: true,
        domType: 'input',
        componentProps: {
            disabled: true
        }
    },
    {
        label: '分辨率',
        prop: 'resolutionRatio',
        isDetail: true,
        domType: 'input',
        componentProps: {
            disabled: true
        }
    },
    {
        label: '状态',
        prop: 'status',
        isSearch: true,
        isSearchMore: false,
        domType: 'select',
        domData: [
            {
                label: '上架',
                value: 10
            },
            {
                label: '下架',
                value: 20
            }
        ],
        rules: [
            { required: true, message: '状态必填', trigger: 'blur' }
        ]
    },
    {
        label: '创建时间',
        prop: 'createdAt',
        isSearch: false,
        isSearchMore: false,
        domType: 'dateTimePickerRange'
    }
]);

// 回填文件属性
watchDetailForm((newVal) => {
    detailFormModel['fileFormat'] = newVal['fileFormat'] || newVal['url_fileFormat'];
    detailFormModel['fileSize'] = newVal['fileSize'] || newVal['url_fileSize'];
    detailFormModel['resolutionRatio'] = newVal['resolutionRatio'] || newVal['url_resolutionRatio'];

    detailFormModel['fileSizeFormat'] = formatFileSize(newVal['fileSize'] || newVal['url_fileSize']);
})

// 响应式数据
const total = ref(0);
const loading = ref(false);
const videoClassifications = ref([]);
const allCollections = ref([]);
const videoList = ref([]);
const expandedCards = ref([]);

// 分页信息
const pagination = reactive({
    pageNumber: 1,
    pageSize: 12
});

// 将数据转换为树形结构
function convertToTreeFormat(data) {
    // 检查数据是否为有效数组
    if (!Array.isArray(data) || data.length === 0) {
        return [];
    }
    
    const idMap = new Map();
    const result = [];

    // 创建所有节点的映射
    for (const item of data) {
        idMap.set(item.id, { ...item, children: [] });
    }

    // 构建树形结构
    for (const item of idMap.values()) {
        if (item.parentId === null || item.parentId === undefined) {
            result.push(item);
        } else {
            const parent = idMap.get(item.parentId);
            if (parent) {
                parent.children.push(item);
            } else {
                // 如果根据parentId找不到父节点，则提升为顶级节点
                result.push(item);
            }
        }
    }

    return result;
}

// 加载视频分类数据
const loadVideoClassifications = async () => {
    const classifications = await assetsClassification.query({ parentId: 2 });
    videoClassifications.value = convertToTreeFormat(classifications.records || []);
};

// 加载专辑数据
const loadCollections = async () => {
    const collections = await assetsCollection.query({ status: 1 });
    allCollections.value = collections.records;
};

// 文件大小格式化函数已移至 @/common/utils

// 格式化日期时间
const formatDateTime = (value) => {
    if (!value) return '-';
    return moment(value).format('YYYY-MM-DD HH:mm:ss');
};

// 检查标题是否溢出
const isTitleOverflow = (title) => {
    return title && title.length > 20;
};

// 切换卡片展开状态
const toggleExpand = (id) => {
    const index = expandedCards.value.indexOf(id);
    if (index > -1) {
        expandedCards.value.splice(index, 1);
    } else {
        expandedCards.value.push(id);
    }
};

// 复制URL
const copyUrl = async (url) => {
    try {
        await navigator.clipboard.writeText(url);
        ElMessage.success('链接已复制到剪贴板');
    } catch (err) {
        ElMessage.error('复制失败');
    }
};

// 查询数据
const queryData = async () => {
    loading.value = true;
    try {
        const params = {
            ...searchFormModel,
            pageNumber: pagination.pageNumber,
            pageSize: pagination.pageSize
        };
        
        const result = await assetsVideo.query(params);
        videoList.value = result.records || [];
        total.value = result.page?.totalRow || 0;
    } catch (error) {
        ElMessage.error('查询失败');
        console.error('查询视频数据失败:', error);
    } finally {
        loading.value = false;
    }
};

// 处理分页大小变化
const handleSizeChange = (val) => {
    pagination.pageSize = val;
    pagination.pageNumber = 1;
    queryData();
};

// 处理当前页变化
const handleCurrentChange = (val) => {
    pagination.pageNumber = val;
    queryData();
};

// 添加视频
const handleAdd = () => {
    // 触发添加操作
    detailFormModel.value = {};
    isOpenDetail.value = true;
};

// 编辑视频
const handleEdit = (item) => {
    const detail = {
        ...item,
        collectionIds: (item.collections || []).map(item => item.collectionId)
    }
    Object.assign(detailFormModel, detail);
    isOpenDetail.value = true;
};

// 设置精选状态
const handleSetHandpick = async (item, handpick) => {
    try {
        const action = handpick === true ? '精选' : '取消精选';
        await ElMessageBox.confirm(
            `确定要${action}视频"${item.title}"吗？`,
            '提示',
            {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'info'
            }
        );
        
        if (handpick === true) {
            await assetsVideo.setHandpick(item.id);
        } else {
            await assetsVideo.cancelHandpick(item.id);
        }
        ElMessage.success(`${action}成功`);
        queryData();
    } catch (error) {
        if (error !== 'cancel') {
            const action = handpick === true ? '精选' : '取消精选';
            ElMessage.error(`${action}失败`);
        }
    }
};

// 下架
const handleOffShelf = async (item) => {
    try {
        await ElMessageBox.confirm(
            `确定要下架视频"${item.title}"吗？`,
            '提示',
            {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }
        );
        
        await assetsVideo.offShelf(item.id);
        ElMessage.success('下架成功');
        queryData();
    } catch (error) {
        if (error !== 'cancel') {
            ElMessage.error('下架失败');
        }
    }
};

// 上架
const handleOnShelf = async (item) => {
    try {
        await ElMessageBox.confirm(
            `确定要上架视频"${item.title}"吗？`,
            '提示',
            {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'info'
            }
        );
        
        await assetsVideo.onShelf(item.id);
        ElMessage.success('上架成功');
        queryData();
    } catch (error) {
        if (error !== 'cancel') {
            ElMessage.error('上架失败');
        }
    }
};

// 删除视频
const handleDelete = async (item) => {
    try {
        await ElMessageBox.confirm(
            `确定要删除视频"${item.title}"吗？删除后无法恢复。`,
            '提示',
            {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }
        );
        
        await assetsVideo.del(item.id);
        ElMessage.success('删除成功');
        queryData();
    } catch (error) {
        if (error !== 'cancel') {
            ElMessage.error('删除失败');
        }
    }
};

// 创建视频
const handleCreate = async (data) => {
    try {
        await assetsVideo.create(data);
        ElMessage.success('创建成功');
        queryData();
        return true;
    } catch (error) {
        ElMessage.error('创建失败');
        return false;
    }
};

// 更新视频
const handleUpdate = async (data) => {
    try {
        await assetsVideo.update(data);
        ElMessage.success('更新成功');
        queryData();
        return true;
    } catch (error) {
        ElMessage.error('更新失败');
        return false;
    }
};

// 格式化数据
const handleFormatData = (data) => {
    // 处理专辑数据
    if (data.collectionIds && Array.isArray(data.collectionIds)) {
        data.collectionIds = data.collectionIds.map(id => parseInt(id));
    }
    return data;
};

// 初始化
onMounted(() => {
    queryData();
    loadVideoClassifications();
    loadCollections();
});
</script>

<style scoped>
.assets-video-page {
    padding: 20px;
}

.action-section {
    display: flex;
    justify-content: flex-start;
    align-items: center;
}

.card-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
    max-width: 100%;
    gap: 20px;
    margin-bottom: 20px;
    align-items: start;
}

/* 确保一排最多4个 */
@media (min-width: 1200px) {
    .card-grid {
        grid-template-columns: repeat(4, 1fr);
    }
}

@media (min-width: 900px) and (max-width: 1199px) {
    .card-grid {
        grid-template-columns: repeat(3, 1fr);
    }
}

@media (min-width: 600px) and (max-width: 899px) {
    .card-grid {
        grid-template-columns: repeat(2, 1fr);
    }
}

.video-card {
    background: linear-gradient(135deg,
            rgba(255, 255, 255, 0.9) 0%,
            rgba(255, 255, 255, 0.6) 100%);
    backdrop-filter: blur(10px);
    -webkit-backdrop-filter: blur(10px);
    border-radius: 24px;
    box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.07);
    overflow: hidden;
    transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.video-card:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}

.video-container {
    position: relative;
    width: 100%;
    height: 180px;
    overflow: hidden;
    padding: 8px;
}

.card-video {
    width: 100%;
    height: 100%;
    object-fit: cover;
    border-radius: 16px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.video-placeholder {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    color: #909399;
    gap: 8px;
    width: 100%;
    height: 100%;
    background: #f5f7fa;
    border-radius: 16px;
}

.format-tag {
    position: absolute;
    top: 14px;
    right: 14px;
    background: rgba(0, 0, 0, 0.6);
    color: white;
    padding: 4px 8px;
    border-radius: 4px;
    font-size: 10px;
    font-weight: 600;
    letter-spacing: 0.5px;
    z-index: 1;
    backdrop-filter: blur(4px);
}

.card-content {
    padding: 8px 16px;
}

.title-actions {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 12px;
}

.card-title {
    font-size: 16px;
    font-weight: 600;
    color: #333;
    margin: 0;
    line-height: 1.4;
    flex: 1;
    margin-right: 8px;
    min-width: 0;
}

.disabled-title {
    color: #909399 !important;
    display: flex;
    align-items: center;
    gap: 4px;
}

.featured-title {
    display: flex;
    align-items: center;
    gap: 4px;
}

.title-text {
    display: inline-block;
    max-width: 100%;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.featured-icon,
.disabled-icon {
    flex-shrink: 0;
}

.action-buttons {
    display: flex;
    gap: 2px;
    flex-shrink: 0;
}

.action-btn {
    padding: 4px;
    margin-left: 0;
    border-radius: 4px;
    transition: all 0.2s ease;
}

.edit-btn:hover {
    background-color: #e6f7ff;
}

.edit-btn:hover svg {
    fill: #1890ff !important;
}

.handpick-btn:hover {
    background-color: #fef9e7;
}

.handpick-btn:hover svg {
    fill: #d48806 !important;
}

.cancel-handpick-btn:hover {
    background-color: #fef9e7;
}

.cancel-handpick-btn:hover svg {
    fill: #d48806 !important;
}

.off-shelf-btn:hover {
    background-color: #f3f0ff;
}

.off-shelf-btn:hover svg {
    fill: #7c3aed !important;
}

.on-shelf-btn:hover {
    background-color: #ecfdf5;
}

.on-shelf-btn:hover svg {
    fill: #059669 !important;
}

.delete-btn:hover {
    background-color: #fff2f0;
}

.delete-btn:hover svg {
    fill: #ff4d4f !important;
}

.expand-section {
    display: flex;
    justify-content: center;
}

.expand-btn {
    padding: 2px 8px;
    border-radius: 4px;
    transition: all 0.2s ease;
    color: #909399 !important;
    height: 12px;
}

.expand-btn svg {
    fill: #909399 !important;
}

.expand-btn:hover {
    transform: scale(1.3);
    color: #1a1b1d !important;
}

.expand-btn:hover svg {
    fill: #1a1b1d !important;
}

.expand-icon {
    transition: transform 0.2s ease;
}

.collapse-section {
    display: flex;
    justify-content: center;
    margin-top: 6px;
}

.collapse-btn {
    padding: 4px 8px;
    border-radius: 4px;
    transition: all 0.2s ease;
    height: 12px;
    color: #909399 !important;
}

.collapse-btn svg {
    fill: #909399 !important;
}

.collapse-btn:hover {
    color: #1a1b1d !important;
    transform: scale(1.3);
}

.collapse-btn:hover svg {
    fill: #1a1b1d !important;
}

.collapse-icon {
    transition: transform 0.2s ease;
}

.detail-info {
    margin-top: 8px;
}

.info-grid {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 6px 12px;
    margin-bottom: 8px;
}

.info-item {
    display: flex;
    font-size: 13px;
    line-height: 1.3;
    min-height: 18px;
}

.info-item.full-width {
    grid-column: 1 / -1;
    margin-bottom: 6px;
}

.info-item .label {
    color: #666;
    min-width: 60px;
    flex-shrink: 0;
    font-size: 12px;
}

.info-item .value {
    color: #333;
    flex: 1;
    word-break: break-all;
    font-size: 12px;
}

.collections-container {
    display: flex;
    flex-wrap: wrap;
    gap: 6px;
    align-items: flex-start;
}

.collection-tag {
    margin: 0;
    font-size: 11px;
    border-radius: 4px;
}

.url-container {
    display: flex;
    align-items: flex-start;
    gap: 8px;
    flex: 1;
    overflow: hidden;
}

.copy-btn {
    padding: 2px 0;
    min-width: auto;
    height: auto;
    border: none;
    background: none;
}

.copy-btn:hover {
    background-color: #f0f9ff;
    border-radius: 4px;
}

.url-link {
    color: #409eff;
    text-decoration: none;
    font-size: 12px;
    flex: 1;
    line-height: 1.4;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.url-link:hover {
    text-decoration: underline;
}

.pagination-section {
    display: flex;
    justify-content: center;
    margin-top: 20px;
}

@media (max-width: 768px) {
    .card-grid {
        grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
        gap: 16px;
    }
    
    .info-grid {
        grid-template-columns: 1fr;
    }
}

@media (max-width: 480px) {
    .card-grid {
        grid-template-columns: 1fr;
    }
}
</style>
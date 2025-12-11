<template>
    <div class="classification-page">
        <!-- 搜索栏 -->
        <div class="search-bar">
            <el-input
                v-model="searchKeyword"
                placeholder="搜索分类"
                :prefix-icon="Search"
                @input="filterClassifications"
                clearable
            />
        </div>

        <div class="classification-content">
            <!-- 左侧一级分类 -->
            <div class="primary-classifications">
                <div
                    v-for="classification in primaryClassifications"
                    :key="classification.id"
                    class="primary-classification-item"
                    :class="{ active: selectedPrimaryId === classification.id }"
                    @click="selectPrimaryClassification(classification)"
                >
                    <div class="classification-icon">
                        <Picture v-if="classification.name.includes('图片')" />
                        <Video v-else-if="classification.name.includes('视频')" />
                        <FileDoc v-else />
                    </div>
                    <span class="classification-name">{{ classification.name }}</span>
                </div>
            </div>

            <!-- 右侧二级分类 -->
            <div class="secondary-classifications">
                <div class="secondary-cards">
                    <div
                        v-for="subClassification in filteredSecondaryClassifications"
                        :key="subClassification.id"
                        class="secondary-classification-card"
                        @click="goToAssetsList(subClassification)"
                    >
                        <span class="card-classification-name">{{ subClassification.name }}</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { Search, Picture, Video, FileDoc } from '@icon-park/vue-next';
import * as API from '@/api';

const router = useRouter();

// 响应式数据
const searchKeyword = ref('');
const allClassifications = ref([]); // 存储所有分类数据
const classificationTree = ref([]); // 存储树形结构的分类数据
const primaryClassifications = ref([]);
const secondaryClassifications = ref([]);
const selectedPrimaryId = ref(null);
const selectedPrimaryClassification = ref(null);

/**
 * 构建树形结构
 * @param classifications 扁平的分类数据
 * @returns 树形结构的分类数据
 */
const buildClassificationTree = (classifications: any[]) => {
    const tree: any[] = [];
    const map = new Map();

    // 先将所有分类放入map中，以id为key
    classifications.forEach(classification => {
        map.set(classification.id, { ...classification, children: [] });
    });

    // 构建树形结构
    classifications.forEach(classification => {
        const node = map.get(classification.id);
        if (classification.parentId === null || classification.parentId === undefined || classification.parentId === 0) {
            // 一级分类（根节点）
            tree.push(node);
        } else {
            // 二级分类，添加到对应的父节点
            const parent = map.get(classification.parentId);
            if (parent) {
                parent.children.push(node);
            }
        }
    });

    return tree;
};

/**
 * 保存选中状态到sessionStorage
 */
const saveSelectedState = () => {
    if (selectedPrimaryClassification.value) {
        const state = {
            selectedPrimaryId: selectedPrimaryId.value,
            selectedPrimaryClassification: selectedPrimaryClassification.value
        };
        sessionStorage.setItem('classification_selected_state', JSON.stringify(state));
    }
};

/**
 * 从sessionStorage恢复选中状态
 */
const restoreSelectedState = () => {
    try {
        const savedState = sessionStorage.getItem('classification_selected_state');
        if (savedState) {
            const state = JSON.parse(savedState);
            
            // 查找对应的分类是否还存在
            const foundClassification = primaryClassifications.value.find(
                classification => classification.id === state.selectedPrimaryId
            );
            
            if (foundClassification) {
                selectedPrimaryId.value = state.selectedPrimaryId;
                selectedPrimaryClassification.value = foundClassification;
                secondaryClassifications.value = foundClassification.children || [];
                return true;
            }
        }
    } catch (error) {
        console.error('恢复选中状态失败:', error);
    }
    return false;
};

/**
 * 过滤后的二级分类
 */
const filteredSecondaryClassifications = computed(() => {
    if (!searchKeyword.value) {
        return secondaryClassifications.value;
    }

    // 搜索所有二级分类中匹配的项
    const allSecondaryClassifications = [];
    classificationTree.value.forEach(primaryClassification => {
        if (primaryClassification.children) {
            primaryClassification.children.forEach(secondaryClassification => {
                if (secondaryClassification.name.toLowerCase().includes(searchKeyword.value.toLowerCase())) {
                    allSecondaryClassifications.push({
                        ...secondaryClassification,
                        parentClassification: primaryClassification
                    });
                }
            });
        }
    });

    // 如果搜索到结果，自动选中第一个匹配项的父级分类
    if (allSecondaryClassifications.length > 0 && allSecondaryClassifications[0].parentClassification) {
        const firstParent = allSecondaryClassifications[0].parentClassification;
        if (selectedPrimaryId.value !== firstParent.id) {
            selectedPrimaryId.value = firstParent.id;
            selectedPrimaryClassification.value = firstParent;
            secondaryClassifications.value = firstParent.children || [];
        }
    }

    // 返回当前选中一级分类下的匹配结果
    return secondaryClassifications.value.filter(classification =>
        classification.name.toLowerCase().includes(searchKeyword.value.toLowerCase())
    );
});

/**
 * 选择一级分类
 * @param classification 一级分类
 */
const selectPrimaryClassification = (classification: any) => {
    selectedPrimaryId.value = classification.id;
    selectedPrimaryClassification.value = classification;
    // 从树形结构中获取二级分类
    secondaryClassifications.value = classification.children || [];
    
    // 保存选中状态到sessionStorage
    saveSelectedState();
};

/**
 * 过滤分类（前端搜索）
 */
const filterClassifications = () => {
    // 搜索功能已通过computed实现
};

/**
 * 跳转到素材列表页面
 * @param classification 分类信息（二级分类）
 */
const goToAssetsList = (classification: any) => {
    // 确保有选中的一级分类
    if (!selectedPrimaryClassification.value) {
        console.error('未选中一级分类');
        return;
    }

    // 在跳转前保存当前选中状态
    saveSelectedState();

    console.log('跳转参数:', {
        type: selectedPrimaryClassification.value.id, // 一级分类的id
        classificationId: classification.id // 点击的二级分类的id
    });

    router.push({
        name: 'assetsList',
        query: {
            type: selectedPrimaryClassification.value.id, // 一级分类的id
            classificationId: classification.id // 点击的二级分类的id
        }
    });
};

/**
 * 加载所有分类数据并构建树形结构
 */
const loadPrimaryClassifications = async () => {
    try {
        // 获取全部分类数据
        const response = await API.assetsClassification.query();
        allClassifications.value = response || [];

        console.log('原始分类数据:', allClassifications.value);

        // 构建树形结构
        classificationTree.value = buildClassificationTree(allClassifications.value);

        console.log('树形结构数据:', classificationTree.value);

        // 提取一级分类（根节点）
        primaryClassifications.value = classificationTree.value;

        // 尝试恢复之前的选中状态，如果失败则默认选择第一个分类
        if (primaryClassifications.value.length > 0) {
            const restored = restoreSelectedState();
            if (!restored) {
                selectPrimaryClassification(primaryClassifications.value[0]);
            }
        }
    } catch (error) {
        console.error('加载分类数据失败:', error);
    }
};



onMounted(() => {
    loadPrimaryClassifications();
});
</script>

<style scoped>
.classification-page {
    height: 100vh;
    background-color: #f5f5f5;
    display: flex;
    flex-direction: column;
}

/* 搜索栏样式 */
.search-bar {
    padding: 16px;
    background: white;
    border-bottom: 1px solid #eee;
}

.search-bar :deep(.el-input__wrapper):focus-within {
    border-color: var(--p-color-green-primary);
    box-shadow: 0 0 0 1px var(--p-color-green-primary);
}

/* 内容区域 */
.classification-content {
    flex: 1;
    display: flex;
    overflow: hidden;
}

/* 左侧一级分类 */
.primary-classifications {
    width: 120px;
    background: white;
    border-right: 1px solid #eee;
    overflow-y: auto;
}

.primary-classification-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 20px 12px;
    border-bottom: 1px solid #f0f0f0;
    cursor: pointer;
    transition: background-color 0.2s;
}

.primary-classification-item:hover {
    background-color: #f8f9fa;
}

.primary-classification-item.active {
    background-color: #f0f9e8;
    border-right: 3px solid var(--p-color-green-primary);
}

.classification-icon {
    font-size: 24px;
    color: #666;
    margin-bottom: 8px;
}

.primary-classification-item.active .classification-icon {
    color: var(--p-color-green-primary);
}

.classification-name {
    font-size: 12px;
    color: #333;
    text-align: center;
    line-height: 1.2;
}

.primary-classification-item.active .classification-name {
    color: var(--p-color-green-primary);
    font-weight: 500;
}

/* 右侧二级分类 */
.secondary-classifications {
    flex: 1;
    background: white;
    overflow-y: auto;
}

.secondary-cards {
    padding: 16px;
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
    align-content: flex-start;
}

.secondary-classification-card {
    background: white;
    border: 1px solid #e5e7eb;
    border-radius: 8px;
    padding: 12px 16px;
    cursor: pointer;
    transition: all 0.2s ease;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.08);
    display: inline-flex;
    align-items: center;
    justify-content: center;
    min-width: fit-content;
    white-space: nowrap;
}

.secondary-classification-card:hover {
    border-color: var(--p-color-green-primary);
    box-shadow: 0 4px 12px rgba(94, 189, 49, 0.15);
    transform: translateY(-2px);
}

.secondary-classification-card:active {
    transform: translateY(0);
    box-shadow: 0 2px 8px rgba(94, 189, 49, 0.2);
}

.card-classification-name {
    font-size: 14px;
    color: #333;
    font-weight: 500;
    line-height: 1.3;
}

/* 滚动条样式 */
.primary-classifications::-webkit-scrollbar,
.secondary-classifications::-webkit-scrollbar {
    width: 4px;
}

.primary-classifications::-webkit-scrollbar-thumb,
.secondary-classifications::-webkit-scrollbar-thumb {
    background: #ddd;
    border-radius: 2px;
}

.primary-classifications::-webkit-scrollbar-track,
.secondary-classifications::-webkit-scrollbar-track {
    background: transparent;
}
</style>
<template>
    <div class="category-page">
        <!-- 搜索栏 -->
        <div class="search-bar">
            <el-input
                v-model="searchKeyword"
                placeholder="搜索分类"
                :prefix-icon="Search"
                @input="filterCategories"
                clearable
            />
        </div>

        <div class="category-content">
            <!-- 左侧一级分类 -->
            <div class="primary-categories">
                <div 
                    v-for="category in primaryCategories" 
                    :key="category.id"
                    class="primary-category-item"
                    :class="{ active: selectedPrimaryId === category.id }"
                    @click="selectPrimaryCategory(category)"
                >
                    <div class="category-icon">
                        <Picture v-if="category.name.includes('图片')" />
                        <Video v-else-if="category.name.includes('视频')" />
                        <FileDoc v-else />
                    </div>
                    <span class="category-name">{{ category.name }}</span>
                </div>
            </div>

            <!-- 右侧二级分类 -->
            <div class="secondary-categories">
                <div class="secondary-cards">
                    <div 
                        v-for="subCategory in filteredSecondaryCategories" 
                        :key="subCategory.id"
                        class="secondary-category-card"
                        @click="goToAssetsList(subCategory)"
                    >
                        <span class="card-category-name">{{ subCategory.name }}</span>
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
const allCategories = ref([]); // 存储所有分类数据
const categoryTree = ref([]); // 存储树形结构的分类数据
const primaryCategories = ref([]);
const secondaryCategories = ref([]);
const selectedPrimaryId = ref(null);
const selectedPrimaryCategory = ref(null);

/**
 * 构建树形结构
 * @param categories 扁平的分类数据
 * @returns 树形结构的分类数据
 */
const buildCategoryTree = (categories: any[]) => {
    const tree: any[] = [];
    const map = new Map();
    
    // 先将所有分类放入map中，以id为key
    categories.forEach(category => {
        map.set(category.id, { ...category, children: [] });
    });
    
    // 构建树形结构
    categories.forEach(category => {
        const node = map.get(category.id);
        if (category.parentId === null || category.parentId === undefined || category.parentId === 0) {
            // 一级分类（根节点）
            tree.push(node);
        } else {
            // 二级分类，添加到对应的父节点
            const parent = map.get(category.parentId);
            if (parent) {
                parent.children.push(node);
            }
        }
    });
    
    return tree;
};

/**
 * 过滤后的二级分类
 */
const filteredSecondaryCategories = computed(() => {
    if (!searchKeyword.value) {
        return secondaryCategories.value;
    }
    
    // 搜索所有二级分类中匹配的项
    const allSecondaryCategories = [];
    categoryTree.value.forEach(primaryCategory => {
        if (primaryCategory.children) {
            primaryCategory.children.forEach(secondaryCategory => {
                if (secondaryCategory.name.toLowerCase().includes(searchKeyword.value.toLowerCase())) {
                    allSecondaryCategories.push({
                        ...secondaryCategory,
                        parentCategory: primaryCategory
                    });
                }
            });
        }
    });
    
    // 如果搜索到结果，自动选中第一个匹配项的父级分类
    if (allSecondaryCategories.length > 0 && allSecondaryCategories[0].parentCategory) {
        const firstParent = allSecondaryCategories[0].parentCategory;
        if (selectedPrimaryId.value !== firstParent.id) {
            selectedPrimaryId.value = firstParent.id;
            selectedPrimaryCategory.value = firstParent;
            secondaryCategories.value = firstParent.children || [];
        }
    }
    
    // 返回当前选中一级分类下的匹配结果
    return secondaryCategories.value.filter(category => 
        category.name.toLowerCase().includes(searchKeyword.value.toLowerCase())
    );
});

/**
 * 选择一级分类
 * @param category 一级分类
 */
const selectPrimaryCategory = (category: any) => {
    selectedPrimaryId.value = category.id;
    selectedPrimaryCategory.value = category;
    // 从树形结构中获取二级分类
    secondaryCategories.value = category.children || [];
};

/**
 * 过滤分类（前端搜索）
 */
const filterCategories = () => {
    // 搜索功能已通过computed实现
};

/**
 * 跳转到素材列表页面
 * @param category 分类信息
 */
const goToAssetsList = (category: any) => {
    router.push({
        name: 'assetsList',
        query: { 
            categoryId: category.id,
            categoryName: category.name
        }
    });
};

/**
 * 加载所有分类数据并构建树形结构
 */
const loadPrimaryCategories = async () => {
    try {
        // 获取全部分类数据
        const response = await API.assetsClassification.query();
        allCategories.value = response || [];
        
        console.log('原始分类数据:', allCategories.value);
        
        // 构建树形结构
        categoryTree.value = buildCategoryTree(allCategories.value);
        
        console.log('树形结构数据:', categoryTree.value);
        
        // 提取一级分类（根节点）
        primaryCategories.value = categoryTree.value;
        
        // 默认选择第一个分类
        if (primaryCategories.value.length > 0) {
            selectPrimaryCategory(primaryCategories.value[0]);
        }
    } catch (error) {
        console.error('加载分类数据失败:', error);
    }
};



onMounted(() => {
    loadPrimaryCategories();
});
</script>

<style scoped>
.category-page {
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

/* 内容区域 */
.category-content {
    flex: 1;
    display: flex;
    overflow: hidden;
}

/* 左侧一级分类 */
.primary-categories {
    width: 120px;
    background: white;
    border-right: 1px solid #eee;
    overflow-y: auto;
}

.primary-category-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 20px 12px;
    border-bottom: 1px solid #f0f0f0;
    cursor: pointer;
    transition: background-color 0.2s;
}

.primary-category-item:hover {
    background-color: #f8f9fa;
}

.primary-category-item.active {
    background-color: #f0f9e8;
    border-right: 3px solid var(--p-color-green-primary);
}

.category-icon {
    font-size: 24px;
    color: #666;
    margin-bottom: 8px;
}

.primary-category-item.active .category-icon {
    color: var(--p-color-green-primary);
}

.category-name {
    font-size: 12px;
    color: #333;
    text-align: center;
    line-height: 1.2;
}

.primary-category-item.active .category-name {
    color: var(--p-color-green-primary);
    font-weight: 500;
}

/* 右侧二级分类 */
.secondary-categories {
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

.secondary-category-card {
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

.secondary-category-card:hover {
    border-color: var(--p-color-green-primary);
    box-shadow: 0 4px 12px rgba(94, 189, 49, 0.15);
    transform: translateY(-2px);
}

.secondary-category-card:active {
    transform: translateY(0);
    box-shadow: 0 2px 8px rgba(94, 189, 49, 0.2);
}

.card-category-name {
    font-size: 14px;
    color: #333;
    font-weight: 500;
    line-height: 1.3;
}

/* 滚动条样式 */
.primary-categories::-webkit-scrollbar,
.secondary-categories::-webkit-scrollbar {
    width: 4px;
}

.primary-categories::-webkit-scrollbar-thumb,
.secondary-categories::-webkit-scrollbar-thumb {
    background: #ddd;
    border-radius: 2px;
}

.primary-categories::-webkit-scrollbar-track,
.secondary-categories::-webkit-scrollbar-track {
    background: transparent;
}
</style>
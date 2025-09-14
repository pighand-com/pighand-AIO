<template>
    <!-- 树形结构区域 -->
    <div class="global-container">
        <el-card>
            <div class="tree-header mb-4 flex justify-between items-center">
                <!-- 左侧筛选区域 -->
                <div class="flex items-center gap-3">
                    <el-input
                        v-model="filterText"
                        placeholder="输入关键词筛选分类"
                        clearable
                        size="small"
                        @input="handleFilter">
                        <template #prefix>
                            <el-icon><Search /></el-icon>
                        </template>
                    </el-input>
                </div>
                
                <!-- 右侧操作区域 -->
                <div class="flex items-center gap-2">
                    <el-button 
                        color="#409eff" 
                        size="small" 
                        plain
                        @click="toggleExpandCollapse">
                        <el-icon>
                            <Minus v-if="isAllExpanded" />
                            <Plus v-else />
                        </el-icon>
                        <span style="margin-left: 4px;">{{ isAllExpanded ? '收起全部' : '展开全部' }}</span>
                    </el-button>
                </div>
            </div>
            
            <el-tree
                ref="treeRef"
                :data="treeData"
                :props="treeProps"
                :filter-node-method="filterNode"
                node-key="id"
                default-expand-all
                :expand-on-click-node="false"
                :expanded-keys="expandedKeys"
                class="classification-tree">
                <template #default="{ node, data }">
                    <div class="tree-node">
                        <div class="node-content">
                            <span class="node-label">{{ data.name }}</span>
                            <el-tag v-if="data.inlay" type="warning" size="small">内置</el-tag>
                            <span class="node-code" v-if="data.code">({{ data.code }})</span>
                        </div>
                        <div class="node-actions">
                            <el-button
                                v-if="getNodeLevel(data) < 2"
                                type="primary"
                                size="small"
                                text
                                @click="handleAdd(data)">
                                添加子分类
                            </el-button>
                            <el-button
                                type="warning"
                                size="small"
                                text
                                @click="handleEdit(data)">
                                编辑
                            </el-button>
                            <el-button
                                v-if="!data.inlay"
                                type="danger"
                                size="small"
                                text
                                @click="handleDelete(data)">
                                删除
                            </el-button>
                        </div>
                    </div>
                </template>
            </el-tree>
        </el-card>
    </div>

    <!-- 编辑对话框 -->
    <el-dialog
        v-model="dialogVisible"
        :title="dialogTitle"
        width="600px"
        :close-on-click-modal="false">
        <el-form
            ref="formRef"
            :model="formData"
            :rules="formRules"
            label-width="100px">
            <el-form-item label="分类名称" prop="name">
                <el-input
                    v-model="formData.name"
                    placeholder="请输入分类名称"
                    maxlength="50"
                    show-word-limit />
            </el-form-item>
            
            <el-form-item label="上级分类" prop="parentId">
                <el-tree-select
                    v-model="formData.parentId"
                    :data="allClassifications"
                    :props="{ value: 'id', label: 'name', children: 'children' }"
                    check-strictly
                    clearable
                    placeholder="请选择上级分类"
                    :disabled="isEditMode && formData.id === selectedParentId" />
            </el-form-item>
        </el-form>
        
        <template #footer>
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
                确定
            </el-button>
        </template>
    </el-dialog>
</template>

<script lang="ts" setup>
import { ref, onMounted, nextTick, computed, watch } from 'vue';
import { ElMessage, ElMessageBox, ElTree } from 'element-plus';
import { Search, Plus, Minus } from '@icon-park/vue-next';
import { assetsClassification } from '@/api/index.ts';

// 响应式数据
const treeRef = ref<InstanceType<typeof ElTree>>();
const formRef = ref();
const allClassifications = ref([]);
const treeData = ref([]);
const dialogVisible = ref(false);
const submitLoading = ref(false);
const isEditMode = ref(false);
const selectedParentId = ref(null);
const expandedKeys = ref<string[]>([]);
const loading = ref(false);
const filterText = ref('');
const isAllExpanded = ref(true); // 跟踪当前展开状态，默认为true因为树形组件默认展开全部



// 编辑表单
const formData = ref({
    id: null,
    name: '',
    parentId: null,
    inlay: false,
});

// 树形组件配置
const treeProps = {
    children: 'children',
    label: 'name'
};

// 表单验证规则
const formRules = {
    name: [
        { required: true, message: '分类名称必填', trigger: 'blur' },
        { max: 50, message: '最大长度为50', trigger: 'blur' }
    ],

};

// 计算属性
const dialogTitle = computed(() => {
    return isEditMode.value ? '编辑分类' : '新增分类';
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
            }
        }
    }

    return result;
}

// 查询处理函数
const handleQuery = async () => {
    try {
        loading.value = true;
        const response = await assetsClassification.query();
        
        if (response) {
            const treeFormat = convertToTreeFormat(response.records);
            allClassifications.value = treeFormat;
            treeData.value = treeFormat;
        }
    } catch (error) {
        console.error('查询失败:', error);
        ElMessage.error('查询失败');
    } finally {
        loading.value = false;
    }
};



// 树形节点过滤方法
const filterNode = (value: string, data: any) => {
    if (!value) return true;
    return data.name.includes(value);
};

// 处理筛选输入
const handleFilter = (value: string) => {
    if (treeRef.value) {
        treeRef.value.filter(value);
    }
};

// 监听筛选文本变化
watch(filterText, (val) => {
    handleFilter(val);
});

// 切换展开/收起状态
const toggleExpandCollapse = () => {
    if (isAllExpanded.value) {
        collapseAll();
    } else {
        expandAll();
    }
};

// 展开全部
const expandAll = () => {
    if (treeRef.value) {
        // 获取所有节点的key
        const keys = [];
        const getAllKeys = (nodes) => {
            nodes.forEach(node => {
                keys.push(node.id);
                if (node.children && node.children.length > 0) {
                    getAllKeys(node.children);
                }
            });
        };
        getAllKeys(treeData.value);
        
        // 使用el-tree的方法来展开所有节点
        keys.forEach(key => {
            treeRef.value.store.nodesMap[key] && treeRef.value.store.nodesMap[key].expand();
        });
        expandedKeys.value = keys;
        isAllExpanded.value = true;
    }
};

// 收起全部
const collapseAll = () => {
    if (treeRef.value) {
        // 获取所有节点的key
        const keys = [];
        const getAllKeys = (nodes) => {
            nodes.forEach(node => {
                keys.push(node.id);
                if (node.children && node.children.length > 0) {
                    getAllKeys(node.children);
                }
            });
        };
        getAllKeys(treeData.value);
        
        // 使用el-tree的方法来收起所有节点
        keys.forEach(key => {
            treeRef.value.store.nodesMap[key] && treeRef.value.store.nodesMap[key].collapse();
        });
        expandedKeys.value = [];
        isAllExpanded.value = false;
    }
};

// 计算节点层级深度
const getNodeLevel = (node) => {
    let level = 1; // 根节点为第1级
    let currentNode = node;
    
    // 通过parentId向上查找，计算层级
    while (currentNode.parentId) {
        level++;
        // 在allClassifications中查找父节点
        currentNode = findNodeById(allClassifications.value, currentNode.parentId);
        if (!currentNode) break;
    }
    
    return level;
};

// 在树形数据中查找指定ID的节点
const findNodeById = (nodes, id) => {
    for (const node of nodes) {
        if (node.id === id) {
            return node;
        }
        if (node.children && node.children.length > 0) {
            const found = findNodeById(node.children, id);
            if (found) return found;
        }
    }
    return null;
};

// 新增分类
const handleAdd = (parentNode = null) => {
    // 检查层级限制
    if (parentNode && getNodeLevel(parentNode) >= 2) {
        ElMessage.warning('最多只能添加到2级分类');
        return;
    }
    
    isEditMode.value = false;
    selectedParentId.value = parentNode?.id || null;
    formData.value = {
        id: null,
        name: '',
        parentId: parentNode?.id || null,
        inlay: false,
    };
    dialogVisible.value = true;
    nextTick(() => {
        formRef.value?.clearValidate();
    });
};

// 编辑分类
const handleEdit = async (node) => {
    try {
        isEditMode.value = true;
        selectedParentId.value = node.parentId;
        const detail = await assetsClassification.find(node.id);
        formData.value = {
            id: detail.id,
            name: detail.name,
            parentId: detail.parentId,
            inlay: detail.inlay || false,
        };
        dialogVisible.value = true;
        nextTick(() => {
            formRef.value?.clearValidate();
        });
    } catch (error) {
        ElMessage.error('获取分类详情失败');
        console.error('获取分类详情失败:', error);
    }
};

// 删除分类
const handleDelete = async (node) => {
    // 检查是否为内置分类
    if (node.inlay) {
        ElMessage.error('内置分类不允许删除');
        return;
    }
    
    // 检查是否有子分类
    if (node.children && node.children.length > 0) {
        ElMessage.error('该分类下存在子分类，无法删除');
        return;
    }
    
    try {
        await ElMessageBox.confirm(
            `确定要删除分类"${node.name}"吗？`,
            '删除确认',
            {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }
        );
        
        await assetsClassification.del(node.id);
        ElMessage.success('删除成功');
        await handleQuery();
    } catch (error) {
        if (error !== 'cancel') {
            ElMessage.error('删除失败');
            console.error('删除失败:', error);
        }
    }
};

// 提交表单
const handleSubmit = async () => {
    try {
        await formRef.value?.validate();
        submitLoading.value = true;
        
        if (isEditMode.value) {
            await assetsClassification.update(formData.value);
            ElMessage.success('更新成功');
        } else {
            await assetsClassification.create(formData.value);
            ElMessage.success('创建成功');
        }
        
        dialogVisible.value = false;
        await handleQuery();
    } catch (error) {
        if (error !== false) { // 表单验证失败时error为false
            ElMessage.error(isEditMode.value ? '更新失败' : '创建失败');
            console.error('提交失败:', error);
        }
    } finally {
        submitLoading.value = false;
    }
};

// 初始化
onMounted(() => {
    handleQuery();
});


</script>

<style scoped>
:deep(.el-card) {
    background: none;
}

:deep(.el-tree) {
    background: none;
}

.tree-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: 1px solid #ebeef5;
    padding-bottom: 16px;
}

.tree-header h3 {
    margin: 0;
    color: #303133;
    font-size: 18px;
    font-weight: 600;
}

.classification-tree {
    margin-top: 16px;
}

.tree-node {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    padding: 8px 12px;
    border-radius: 6px;
    transition: all 0.3s ease;
}

.tree-node:hover {
    background-color: #f5f7fa;
}

.node-content {
    display: flex;
    align-items: center;
    gap: 8px;
    flex: 1;
}

.node-label {
    font-size: 14px;
    color: #303133;
    font-weight: 500;
}

.node-code {
    font-size: 12px;
    color: #909399;
    font-style: italic;
}

.node-actions {
    display: flex;
    gap: 4px;
    opacity: 0;
    transition: opacity 0.3s ease;
}

.tree-node:hover .node-actions {
    opacity: 1;
}

.node-actions .el-button {
    padding: 4px 8px;
    font-size: 12px;
    border: none;
}

.node-actions .el-button:hover {
    background-color: rgba(64, 158, 255, 0.1);
}

.node-actions .el-button.is-type-danger:hover {
    background-color: rgba(245, 108, 108, 0.1);
}

/* 自定义树形组件样式 */
:deep(.el-tree-node__content) {
    height: auto;
    padding: 0;
}

:deep(.el-tree-node__expand-icon) {
    padding: 6px;
    color: #c0c4cc;
}

:deep(.el-tree-node__expand-icon.expanded) {
    transform: rotate(90deg);
}

:deep(.el-tree-node__expand-icon:hover) {
    color: #409eff;
}

/* 对话框样式优化 */
.el-dialog__body {
    padding: 20px;
}

.el-form-item {
    margin-bottom: 20px;
}

.el-card__body {
    padding: 20px;
}

/* 响应式设计 */
@media (max-width: 768px) {
    .tree-header {
        flex-direction: column;
        gap: 12px;
        align-items: flex-start;
    }
    
    .tree-header h3 {
        font-size: 16px;
    }
    
    .node-actions {
        opacity: 1;
    }
}
</style>
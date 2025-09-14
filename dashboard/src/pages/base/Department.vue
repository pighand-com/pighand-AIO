<template>
    <div class="department-management">
        <!-- 左侧部门树 -->
        <div class="department-tree">
            <div class="tree-header">
                <h3>部门管理</h3>
                <el-button type="primary" size="small" @click="handleAddDepartment">
                    <el-icon><Plus /></el-icon>
                    添加部门
                </el-button>
            </div>
            <el-tree
                ref="treeRef"
                :data="departmentTree"
                :props="treeProps"
                node-key="id"
                :expand-on-click-node="false"
                :highlight-current="true"
                @node-click="handleNodeClick"
                class="department-tree-content">
                <template #default="{ node, data }">
                    <div class="tree-node">
                        <span class="node-label">{{ node.label }}</span>
                        <div class="node-actions">
                            <el-button
                                type="text"
                                size="small"
                                @click.stop="handleEditDepartment(data)">
                                编辑
                            </el-button>
                            <el-button
                                type="text"
                                size="small"
                                @click.stop="handleDeleteDepartment(data)">
                                删除
                            </el-button>
                        </div>
                    </div>
                </template>
            </el-tree>
        </div>

        <!-- 右侧员工列表 -->
        <div class="employee-list">
            <div class="list-header">
                <h3>{{ selectedDepartment?.name || '请选择部门' }}</h3>
                <div class="header-actions" v-if="selectedDepartment">
                    <el-input
                        v-model="searchKeyword"
                        placeholder="搜索用户"
                        size="small"
                        style="width: 200px; margin-right: 10px;"
                        @input="handleSearchUsers">
                        <template #prefix>
                            <el-icon><Search /></el-icon>
                        </template>
                    </el-input>
                    <el-button type="primary" size="small" @click="showAddUserDialog = true">
                        <el-icon><User /></el-icon>
                        添加员工
                    </el-button>
                </div>
            </div>
            
            <div class="employee-content" v-if="selectedDepartment">
                <el-table :data="departmentUsers" style="width: 100%">
                    <el-table-column prop="id" label="ID" />
                    <el-table-column prop="username" label="用户名" />
                    <el-table-column prop="phone" label="电话" />
                    <el-table-column prop="email" label="邮箱" />
                    <el-table-column label="操作">
                        <template #default="{ row }">
                            <el-button
                                type="text"
                                size="small"
                                @click="handleRemoveUser(row)">
                                移除
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
            
            <div class="empty-state" v-else>
                <el-empty description="请选择部门查看员工列表" />
            </div>
        </div>
    </div>

    <!-- 部门编辑对话框 -->
    <el-dialog
        v-model="showDepartmentDialog"
        :title="departmentDialogTitle"
        width="400px">
        <el-form :model="departmentForm" label-width="80px">
            <el-form-item label="部门名称" required>
                <el-input v-model="departmentForm.name" placeholder="请输入部门名称" />
            </el-form-item>
            <el-form-item label="上级部门">
                <el-select v-model="departmentForm.parentId" placeholder="请选择上级部门" clearable style="width: 100%">
                    <el-option
                        v-for="dept in flatDepartmentList"
                        :key="dept.id"
                        :label="dept.name"
                        :value="dept.id"
                        :disabled="dept.id === departmentForm.id">
                    </el-option>
                </el-select>
            </el-form-item>
        </el-form>
        <template #footer>
            <el-button @click="showDepartmentDialog = false">取消</el-button>
            <el-button type="primary" @click="handleSaveDepartment">确定</el-button>
        </template>
    </el-dialog>

    <!-- 添加员工对话框 -->
    <el-dialog
        v-model="showAddUserDialog"
        title="添加员工"
        width="600px">
        <div class="add-user-content">
            <el-input
                v-model="userSearchKeyword"
                placeholder="搜索用户名、电话或邮箱"
                @input="handleSearchAvailableUsers"
                style="margin-bottom: 15px;">
                <template #prefix>
                    <el-icon><Search /></el-icon>
                </template>
            </el-input>
            
            <el-table
                :data="availableUsers"
                style="width: 100%"
                max-height="300px">
                <el-table-column prop="id" label="ID" width="80" />
                <el-table-column prop="username" label="用户名" />
                <el-table-column prop="phone" label="电话" />
                <el-table-column prop="email" label="邮箱" />
                <el-table-column label="操作" width="120">
                    <template #default="{ row }">
                        <el-button
                            type="primary"
                            size="small"
                            @click="handleAddUser(row)">
                            添加
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <template #footer>
            <el-button @click="showAddUserDialog = false">关闭</el-button>
        </template>
    </el-dialog>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted, computed } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Plus, Search, User } from '@icon-park/vue-next';
import { department, user } from '@/api/index';

// 响应式数据
const treeRef = ref();
const departmentList = ref([]);
const selectedDepartment = ref(null);
const departmentUsers = ref([]);
const availableUsers = ref([]);
const searchKeyword = ref('');
const userSearchKeyword = ref('');

// 对话框状态
const showDepartmentDialog = ref(false);
const showAddUserDialog = ref(false);
const isEditMode = ref(false);

// 表单数据
const departmentForm = reactive({
    id: null,
    name: '',
    parentId: null
});

// 树形结构配置
const treeProps = {
    children: 'children',
    label: 'name'
};

// 计算属性
const departmentTree = computed(() => {
    return departmentList.value;
});

// 扁平化部门列表，用于父级部门选择
const flatDepartmentList = computed(() => {
    const flatten = (departments) => {
        let result = [];
        departments.forEach(dept => {
            result.push(dept);
            if (dept.children && dept.children.length > 0) {
                result = result.concat(flatten(dept.children));
            }
        });
        return result;
    };
    return flatten(departmentList.value);
});

const departmentDialogTitle = computed(() => {
    return isEditMode.value ? '编辑部门' : '添加部门';
});

// 生命周期
onMounted(() => {
    loadDepartments();
});

// 方法定义
const loadDepartments = async () => {
    try {
        const result = await department.getDepartmentTree();
        departmentList.value = result || [];
    } catch (error) {
        ElMessage.error('加载部门列表失败');
    }
};

const handleNodeClick = async (data) => {
    selectedDepartment.value = data;
    await loadDepartmentUsers(data.id);
};

const loadDepartmentUsers = async (departmentId) => {
    try {
        const result = await department.getDepartmentUsers(departmentId);
        departmentUsers.value = result || [];
    } catch (error) {
        ElMessage.error('加载部门员工失败');
    }
};

const handleAddDepartment = () => {
    isEditMode.value = false;
    departmentForm.id = null;
    departmentForm.name = '';
    // 默认选择当前选中的部门作为上级部门
    departmentForm.parentId = selectedDepartment.value?.id || null;
    showDepartmentDialog.value = true;
};

const handleEditDepartment = (data) => {
    isEditMode.value = true;
    departmentForm.id = data.id;
    departmentForm.name = data.name;
    departmentForm.parentId = data.parentId || null;
    showDepartmentDialog.value = true;
};

const handleDeleteDepartment = async (data) => {
    try {
        await ElMessageBox.confirm(
            `确定要删除部门 "${data.name}" 吗？`,
            '确认删除',
            {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }
        );
        
        await department.del(data.id);
        ElMessage.success('删除成功');
        await loadDepartments();
        
        // 如果删除的是当前选中的部门，清空右侧内容
        if (selectedDepartment.value?.id === data.id) {
            selectedDepartment.value = null;
            departmentUsers.value = [];
        }
    } catch (error) {
        if (error !== 'cancel') {
            ElMessage.error('删除失败');
        }
    }
};

const handleSaveDepartment = async () => {
    if (!departmentForm.name.trim()) {
        ElMessage.warning('请输入部门名称');
        return;
    }
    
    try {
        if (isEditMode.value) {
            await department.update(departmentForm);
            ElMessage.success('更新成功');
        } else {
            await department.create({ 
                name: departmentForm.name,
                parentId: departmentForm.parentId 
            });
            ElMessage.success('创建成功');
        }
        
        showDepartmentDialog.value = false;
        await loadDepartments();
    } catch (error) {
        ElMessage.error(isEditMode.value ? '更新失败' : '创建失败');
    }
};

const handleSearchUsers = () => {
    // 这里可以实现本地搜索或者重新请求接口
    // 暂时实现本地搜索
    if (!searchKeyword.value.trim()) {
        if (selectedDepartment.value) {
            loadDepartmentUsers(selectedDepartment.value.id);
        }
        return;
    }
    
    const keyword = searchKeyword.value.toLowerCase();
    departmentUsers.value = departmentUsers.value.filter(user => 
        user.username?.toLowerCase().includes(keyword) ||
        user.phone?.includes(keyword) ||
        user.email?.toLowerCase().includes(keyword)
    );
};

const handleSearchAvailableUsers = async () => {
    if (!selectedDepartment.value) return;
    
    try {
        const result = await department.searchAvailableUsers(
            selectedDepartment.value.id,
            userSearchKeyword.value
        );
        availableUsers.value = result || [];
    } catch (error) {
        ElMessage.error('搜索用户失败');
    }
};

const handleAddUser = async (userData) => {
    try {
        await department.addUserToDepartment(selectedDepartment.value.id, [userData.id]);
        ElMessage.success('添加员工成功');
        
        // 刷新部门员工列表
        await loadDepartmentUsers(selectedDepartment.value.id);
        
        // 刷新可用用户列表
        await handleSearchAvailableUsers();
    } catch (error) {
        ElMessage.error('添加员工失败');
    }
};

const handleRemoveUser = async (userData) => {
    try {
        await ElMessageBox.confirm(
            `确定要将 "${userData.username}" 从部门中移除吗？`,
            '确认移除',
            {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }
        );
        
        await department.removeUserFromDepartment(selectedDepartment.value.id, userData.id);
        ElMessage.success('移除成功');
        
        // 刷新部门员工列表
        await loadDepartmentUsers(selectedDepartment.value.id);
    } catch (error) {
        if (error !== 'cancel') {
            ElMessage.error('移除失败');
        }
    }
};

// 监听添加用户对话框打开，自动搜索可用用户
const handleAddUserDialogOpen = () => {
    userSearchKeyword.value = '';
    handleSearchAvailableUsers();
};

// 监听对话框状态变化
const watchAddUserDialog = (newVal) => {
    if (newVal) {
        handleAddUserDialogOpen();
    }
};

// 使用 watch 监听对话框状态
import { watch } from 'vue';
watch(showAddUserDialog, watchAddUserDialog);
</script>

<style scoped>
.department-management {
    display: flex;
    height: 100%;
    gap: 20px;
}

.department-tree {
    width: 300px;
    border: 1px solid #e4e7ed;
    border-radius: 4px;
    background: white;
}

.tree-header {
    padding: 15px;
    border-bottom: 1px solid #e4e7ed;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.tree-header h3 {
    margin: 0;
    font-size: 16px;
    color: #303133;
}

.department-tree-content {
    padding: 10px;
    max-height: calc(100vh - 200px);
    overflow-y: auto;
}

.tree-node {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    padding-right: 10px;
}

.node-label {
    flex: 1;
}

.node-actions {
    display: none;
}

.tree-node:hover .node-actions {
    display: block;
}

.employee-list {
    flex: 1;
    border: 1px solid #e4e7ed;
    border-radius: 4px;
    background: white;
}

.list-header {
    padding: 15px;
    border-bottom: 1px solid #e4e7ed;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.list-header h3 {
    margin: 0;
    font-size: 16px;
    color: #303133;
}

.header-actions {
    display: flex;
    align-items: center;
}

.employee-content {
    padding: 15px;
}

.empty-state {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 300px;
}

.add-user-content {
    max-height: 400px;
}
</style>
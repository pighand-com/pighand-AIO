<template>
    <div class="settings-page">
        <!-- 顶部导航栏 -->
        <div class="header">
            <div class="header-left" @click="goBack">
                <Left />
            </div>
            <div class="header-title">设置</div>
            <div class="header-right"></div>
        </div>

        <!-- 组织架构信息 -->
        <div class="org-section">
            <div class="section-title">组织架构信息</div>
            <div class="org-info">
                <div v-if="loading" class="loading">
                    <el-icon class="is-loading">
                        <Loading />
                    </el-icon>
                    <span>加载中...</span>
                </div>
                <div v-else-if="orgStructure.length > 0" class="org-list">
                    <div 
                        v-for="(item, index) in orgStructure" 
                        :key="index" 
                        class="org-item"
                    >
                        <div class="org-label">{{ item.label }}：</div>
                        <div class="org-value">{{ item.value || '暂无' }}</div>
                    </div>
                </div>
                <div v-else class="no-data">
                    <div class="no-data-icon">
                        <Setting />
                    </div>
                    <div class="no-data-text">暂无组织架构信息</div>
                </div>
            </div>
        </div>


    </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { Left, Setting, Loading } from '@icon-park/vue-next';
import * as API from '@/api';

const router = useRouter();

// 响应式数据
const loading = ref(false);
const orgStructure = ref<Array<{ label: string; value: string }>>([]);

/**
 * 返回上一页
 */
const goBack = () => {
    router.back();
};

/**
 * 获取组织架构信息
 */
const fetchOrgStructure = async () => {
    loading.value = true;
    try {
        const response = await API.orgDepartment.getMyDepartmentSimple();
        if (response && response) {
            // 将嵌套的组织架构数据转换为数组
            const departments: any[] = [];
            let current = response;
            
            // 遍历嵌套结构，提取所有层级的部门信息
            while (current) {
                departments.push({
                    id: current.id,
                    name: current.name
                });
                current = current.child;
            }
            
            // 根据层级显示组织架构信息
            const orgLabels = ['所属城市', '所属单位', '所属科室'];
            const result: Array<{ label: string; value: string }> = [];
            
            departments.forEach((dept: any, index: number) => {
                if (index < orgLabels.length) {
                    result.push({
                        label: orgLabels[index],
                        value: dept.name || ''
                    });
                }
            });
            
            // 如果层级不足4级，补充空项
            for (let i = departments.length; i < orgLabels.length; i++) {
                result.push({
                    label: orgLabels[i],
                    value: ''
                });
            }
            
            orgStructure.value = result;
        }
    } catch (error) {
        console.error('获取组织架构信息失败:', error);
        ElMessage.error('获取组织架构信息失败');
    } finally {
        loading.value = false;
    }
};

onMounted(() => {
    fetchOrgStructure();
});
</script>

<style scoped>
.settings-page {
    background-color: #f8fafb;
    min-height: 100vh;
}

/* 顶部导航栏 */
.header {
    background: white;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 12px 16px;
    border-bottom: 1px solid #f0f0f0;
    position: sticky;
    top: 0;
    z-index: 100;
}

.header-left {
    width: 40px;
    height: 40px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    border-radius: 8px;
    transition: all 0.3s ease;
    color: #333;
    font-size: 20px;
}

.header-left:hover {
    background-color: rgba(94, 189, 49, 0.1);
    color: var(--p-color-green-primary, #5ebd31);
}

.header-title {
    font-size: 18px;
    font-weight: 600;
    color: #333;
}

.header-right {
    width: 40px;
}

/* 组织架构信息区域 */
.org-section {
    margin: 20px 16px;
}

.section-title {
    font-size: 16px;
    font-weight: 600;
    color: #333;
    margin-bottom: 12px;
    padding-left: 4px;
}

.org-info {
    background: white;
    border-radius: 12px;
    padding: 20px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.loading {
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 40px 0;
    color: #999;
    font-size: 14px;
}

.loading .el-icon {
    margin-right: 8px;
    font-size: 18px;
}

.org-list {
    display: flex;
    flex-direction: column;
    gap: 16px;
}

.org-item {
    display: flex;
    align-items: center;
    padding: 12px 0;
    border-bottom: 1px solid #f5f5f5;
}

.org-item:last-child {
    border-bottom: none;
}

.org-label {
    font-size: 14px;
    color: #666;
    min-width: 80px;
    font-weight: 500;
}

.org-value {
    font-size: 14px;
    color: #333;
    flex: 1;
    font-weight: 400;
}

.no-data {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 40px 0;
    color: #999;
}

.no-data-icon {
    font-size: 48px;
    margin-bottom: 12px;
    opacity: 0.5;
}

.no-data-text {
    font-size: 14px;
}


</style>
<template>
    <PDataManager
        :handle-query="checkInActivity.getList"
        :handle-find="checkInActivity.getDetail"
        :handle-delete="checkInActivity.del"
        :handle-create="checkInActivity.create"
        :handle-update="checkInActivity.update"
        :drawer-props="{
            size: '70%'
        }"
        :data-table-props="{
            tableOperation: ['delete']
        }"
        >
        
        <!-- 自定义操作按钮插槽 -->
        <template #table-column-operation="{ row }">
            <el-button
                size="small"
                type="primary"
                :icon="Download"
                @click="handleGenerateQrcode(row)"
                :loading="qrcodeLoading[row.id]">
                下载小程序码
            </el-button>
        </template>

    </PDataManager>


</template>

<script setup lang="ts">
import { ref, reactive } from 'vue';
import { ElMessage } from 'element-plus';
import { Download } from '@icon-park/vue-next';
import checkInActivity from '@/api/checkInActivity';
import provideForm from '@/common/provideForm';

// 二维码相关状态
const qrcodeLoading = reactive<Record<string, boolean>>({});

// 使用provideForm配置表单字段
provideForm(
    [
        {
            label: 'ID',
            prop: 'id',
            isDetail: true,
            isPrimaryKey: true,
            hidden: true,
            isTable: true
        },
        {
            label: '活动名称',
            prop: 'name',
            isTable: true,
            isDetail: true,
            isSearch: true,
            domType: 'input',
            rules: [
                {
                    required: true,
                    message: '活动名称必填',
                    trigger: 'blur'
                },
                {
                    max: 100,
                    message: '最大长度100个字符',
                    trigger: 'blur'
                }
            ]
        },
        {
            label: '活动时长（分钟）',
            prop: 'time',
            isTable: true,
            isDetail: true,
            domType: 'number',
            suffix: '分钟'
        },
        {
            label: '开始时间',
            prop: 'beginTime',
            isTable: true,
            isDetail: true,
            domType: 'dateTimePicker'
        },
        {
            label: '结束时间',
            prop: 'endTime',
            isTable: true,
            isDetail: true,
            domType: 'dateTimePicker'
        }
    ]
);

/**
 * 下载活动小程序码
 * @param row 活动数据行
 */
const handleGenerateQrcode = async (row: any) => {
    const id = row.id;
    const activityName = row.name || 'unknown';

    try {
        qrcodeLoading[row.id] = true;
        const response = await checkInActivity.generateQRCode(id);

        // 将base64转换为blob
        const base64Data = response.replace(/^data:image\/[a-z]+;base64,/, '');
        const byteCharacters = atob(base64Data);
        const byteNumbers = new Array(byteCharacters.length);
        for (let i = 0; i < byteCharacters.length; i++) {
            byteNumbers[i] = byteCharacters.charCodeAt(i);
        }
        const byteArray = new Uint8Array(byteNumbers);
        const blob = new Blob([byteArray], { type: 'image/png' });

        // 创建下载链接
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = `${activityName}_小程序码.png`;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        window.URL.revokeObjectURL(url);

        ElMessage.success('小程序码下载成功');
    } catch (error) {
        console.error('下载小程序码失败:', error);
        ElMessage.error('下载小程序码失败');
    } finally {
        qrcodeLoading[row.id] = false;
    }
};

</script>
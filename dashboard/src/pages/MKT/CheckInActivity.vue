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
        }">
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
            <el-button
                size="small"
                type="success"
                @click="openStatsDialog(row)">
                统计
            </el-button>
        </template>
    </PDataManager>

    <el-dialog v-model="statsDialogVisible" title="活动统计" width="600px">
        <div style="margin-bottom: 12px;">
            <el-date-picker v-model="selectedDate" type="date" value-format="YYYY-MM-DD" format="YYYY-MM-DD" placeholder="选择日期" @change="fetchStats" />
        </div>
        <el-card shadow="never" style="margin-bottom: 12px;">
            <div>当日参与总人数：{{ statsData.totalParticipants ?? 0 }}</div>
        </el-card>
        <el-table :data="statsData.locations || []" v-loading="statsLoading" size="small">
            <el-table-column prop="locationName" label="打卡点" min-width="200" />
            <el-table-column prop="checkInCount" label="打卡人数" width="120" />
        </el-table>
        <template #footer>
            <el-button @click="statsDialogVisible = false">关闭</el-button>
        </template>
    </el-dialog>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue';
import { ElMessage } from 'element-plus';
import { Download } from '@icon-park/vue-next';
import checkInActivity from '@/api/checkInActivity';
import provideForm from '@/common/provideForm';

const qrcodeLoading = reactive<Record<string, boolean>>({});

const statsDialogVisible = ref(false);
const statsLoading = ref(false);
const currentActivityId = ref<number | null>(null);
const selectedDate = ref<string>((() => {
    const d = new Date();
    const y = d.getFullYear();
    const m = `${d.getMonth() + 1}`.padStart(2, '0');
    const day = `${d.getDate()}`.padStart(2, '0');
    return `${y}-${m}-${day}`;
})());
const statsData = reactive<{ totalParticipants?: number; locations?: Array<{ locationId: number; locationName: string; checkInCount: number }>; }>({});

const formatTime = (val: any) => {
    if (Array.isArray(val) && val.length >= 2) {
        const h = String(val[0]).padStart(2, '0');
        const m = String(val[1]).padStart(2, '0');
        const s = String(val[2] ?? 0).padStart(2, '0');
        return `${h}:${m}:${s}`;
    }
    if (typeof val === 'string') return val;
    return '';
};

const { detailFormModel, watchDetailForm } = provideForm([
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
        label: '开始时间 ',
        prop: 'beginTime',
        isTable: true,
        isDetail: true,
        domType: 'timePicker',
        tableFormat: (v: any) => formatTime(v),
        componentProps: { format: 'HH:mm:ss', 'value-format': 'HH:mm:ss' }
    },
    {
        label: '结束时间',
        prop: 'endTime',
        isTable: true,
        isDetail: true,
        domType: 'timePicker',
        tableFormat: (v: any) => formatTime(v),
        componentProps: { format: 'HH:mm:ss', 'value-format': 'HH:mm:ss' }
    }
]);

watchDetailForm((n) => {
    if (n.beginTime) n.beginTime = formatTime(n.beginTime);
    if (n.endTime) n.endTime = formatTime(n.endTime);
});

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

const openStatsDialog = (row: any) => {
    currentActivityId.value = row.id;
    statsDialogVisible.value = true;
    fetchStats();
};

const fetchStats = async () => {
    if (!currentActivityId.value || !selectedDate.value) return;
    try {
        statsLoading.value = true;
        const res = await checkInActivity.getStats(currentActivityId.value, selectedDate.value);
        statsData.totalParticipants = res.totalParticipants ?? 0;
        statsData.locations = res.locations ?? [];
    } catch (e) {
        ElMessage.error('获取统计数据失败');
    } finally {
        statsLoading.value = false;
    }
};
</script>

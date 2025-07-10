<template>
    <PDataManager
        :handle-query="distributionSalesperson.query"
        :handle-find="distributionSalesperson.find"
        :handle-delete="distributionSalesperson.del"
        :handle-create="distributionSalesperson.create"
        :handle-update="distributionSalesperson.update">
        <template #table-column-operation="{ row }">
            <el-button
                v-if="row.status === 0"
                size="small"
                type="success"
                :icon="Play"
                @click="enable(row)"
                >启用</el-button
            >
            <el-button
                v-if="row.status === 10"
                size="small"
                type="warning"
                :icon="Pause"
                @click="disable(row)"
                >禁用</el-button
            >
            <el-button
                size="small"
                type="primary"
                :icon="Download"
                @click="downloadQrCode(row)"
                >下载小程序码</el-button
            >
            <el-button size="small" type="info" @click="showSalesRecords(row)"
                >分销记录</el-button
            >
        </template>
    </PDataManager>

    <!-- 分销记录弹窗 -->
    <el-dialog
        v-model="salesRecordsVisible"
        title="分销记录"
        width="80%"
        :before-close="closeSalesRecordsDialog">
        <!-- 状态统计 -->
        <div class="statistics-container">
            <el-row justify="space-around">
                <el-col
                    :span="4"
                    v-for="(count, status) in statusStatistics"
                    :key="status">
                    <div style="margin-bottom: 16px">
                        <el-card
                            class="statistics-card"
                            :class="`status-${status}`">
                            <div class="statistics-item">
                                <div class="statistics-label">
                                    {{ formatStatus(status) }}
                                </div>
                                <div
                                    class="statistics-value"
                                    :class="`value-${status}`">
                                    {{
                                        status === 'withdrawAmount'
                                            ? new Decimal(Math.abs(count))
                                                  .div(100)
                                                  .toFixed(2)
                                            : new Decimal(count)
                                                  .div(100)
                                                  .toFixed(2)
                                    }}
                                </div>
                            </div>
                        </el-card>
                    </div>
                </el-col>
            </el-row>
        </div>
        <el-table :data="salesRecordsData" border>
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="order.sn" label="订单号" />
            <el-table-column prop="tickets" label="票">
                <template #default="{ row }">
                    <span v-if="row.tickets && row.tickets.length > 0">
                        {{
                            row.tickets
                                .map(
                                    (ticket, index) =>
                                        `${ticket.name} x ${
                                            row.orderSku && row.orderSku[index]
                                                ? row.orderSku[index].quantity
                                                : 0
                                        }`
                                )
                                .join(', ')
                        }}
                    </span>
                    <span v-else>-</span>
                </template>
            </el-table-column>
            <el-table-column prop="frozenAmount" label="冻结金额">
                <template #default="{ row }">
                    <span
                        :class="{
                            'amount-frozen':
                                row.frozenAmount && row.frozenAmount > 0
                        }">
                        {{
                            row.frozenAmount
                                ? new Decimal(row.frozenAmount)
                                      .div(100)
                                      .toFixed(2)
                                : '-'
                        }}
                    </span>
                </template>
            </el-table-column>
            <el-table-column prop="settledAmount" label="结算金额">
                <template #default="{ row }">
                    <span
                        :class="{
                            'amount-settled':
                                row.settledAmount && row.settledAmount > 0,
                            'amount-withdraw':
                                row.settledAmount && row.settledAmount < 0
                        }">
                        {{
                            row.settledAmount
                                ? new Decimal(row.settledAmount)
                                      .div(100)
                                      .toFixed(2)
                                : '-'
                        }}
                    </span>
                </template>
            </el-table-column>
            <el-table-column prop="refundAmount" label="退款金额">
                <template #default="{ row }">
                    <span
                        :class="{
                            'amount-refund':
                                row.refundAmount && row.refundAmount > 0
                        }">
                        {{
                            row.refundAmount
                                ? new Decimal(row.refundAmount)
                                      .div(100)
                                      .toFixed(2)
                                : '-'
                        }}
                    </span>
                </template>
            </el-table-column>
            <el-table-column prop="type" label="类型">
                <template #default="{ row }">
                    <el-tag :type="getTypeTagType(row.type)">{{
                        formatType(row.type)
                    }}</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="createdAt" label="创建时间">
                <template #default="{ row }">
                    {{ formatDateTime(row.createdAt) }}
                </template>
            </el-table-column>
        </el-table>
        <template #footer>
            <span class="dialog-footer">
                <el-button
                    type="primary"
                    @click="showSettlementDialog"
                    :disabled="!settledDetailIds.length"
                    >结算</el-button
                >
                <el-button @click="closeSalesRecordsDialog">关闭</el-button>
            </span>
        </template>
    </el-dialog>

    <!-- 结算对话框 -->
    <el-dialog
        v-model="settlementVisible"
        title="结算确认"
        width="450px"
        :close-on-click-modal="false"
        class="settlement-dialog">
        <div class="settlement-content">
            <!-- 金额展示卡片 -->
            <div class="amount-card">
                <span>结算金额</span>
                <span style="font-weight: bold; font-size: xx-large">
                    ¥{{
                        typeof settlementForm.settledAmount === 'number'
                            ? settlementForm.settledAmount.toFixed(2)
                            : settlementForm.settledAmount || '0.00'
                    }}
                </span>
            </div>
        </div>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="settlementVisible = false">取消</el-button>
                <el-button type="primary" @click="handleSettlement"
                    >确认结算</el-button
                >
            </span>
        </template>
    </el-dialog>
</template>

<script lang="ts" setup>
import { h, ref } from 'vue';
import { distributionSalesperson, distributionSales, user } from '@/api';
import provideForm from '@/common/provideForm';
import { Play, Pause, Download } from '@icon-park/vue-next';
import { ElTag, ElMessage } from 'element-plus';
import Decimal from 'decimal.js';

// 分销记录弹窗相关
const salesRecordsVisible = ref(false);
const salesRecordsData = ref([]);
const statusStatistics = ref<{
    frozenAmount?: number;
    settledAmount?: number;
    withdrawAmount?: number;
    refundAmount?: number;
    incomingAmount?: number;
}>({});
const currentSalespersonId = ref(null);
const settledDetailIds = ref([]);

// 结算弹窗相关
const settlementVisible = ref(false);
const settlementForm = ref({
    settledAmount: null
});

const { getDetailOperation, queryTableData } = provideForm([
    {
        label: 'id',
        prop: 'id',
        isDetail: true,
        isPrimaryKey: true,
        hidden: true
    },
    {
        label: '手机号',
        prop: 'userPhone',
        isSearch: true,
        domType: 'input'
    },
    {
        label: '手机号',
        prop: 'userId',
        isTable: true,
        isDetail: true,
        domType: 'select',
        domData: async (key) => {
            const result = await user.query({ phone: key });
            return result.records.map((item) => ({
                label: item.phone,
                value: item.id
            }));
        },
        tableFormat: (_value, _row, _item) => {
            return _row.user.phone || '';
        },
        rules: [
            {
                required: true,
                message: '手机号必填',
                trigger: 'blur'
            }
        ]
    },
    {
        label: '状态',
        prop: 'status',
        isTable: true,
        isSearch: true,
        domType: 'select',
        domData: [
            {
                label: '禁用',
                value: 0
            },
            {
                label: '启用',
                value: 10
            }
        ],
        tableFormat: (_value, _row, _item) => {
            const type = _value === 0 ? 'danger' : 'success';
            const text = _value === 0 ? '禁用' : '启用';
            return h(ElTag, { type: type }, text);
        }
    }
]);

// 启用
const enable = async (row) => {
    const id = getDetailOperation(row).primaryValue;

    await distributionSalesperson.enable(id);

    await queryTableData(async (params: any) => {
        return await distributionSalesperson.query({
            ...params
        });
    });
};

// 禁用
const disable = async (row) => {
    const id = getDetailOperation(row).primaryValue;

    await distributionSalesperson.disable(id);

    await queryTableData(async (params: any) => {
        return await distributionSalesperson.query({
            ...params
        });
    });
};

// 下载小程序码
const downloadQrCode = async (row) => {
    const id = getDetailOperation(row).primaryValue;
    const phone = row.user.phone || 'unknown';

    try {
        const response = await distributionSalesperson.qrCode(id);

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
        link.download = `${phone}.png`;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        window.URL.revokeObjectURL(url);
    } catch (error) {
        console.error('下载小程序码失败:', error);
    }
};

// 显示分销记录
const showSalesRecords = async (row) => {
    const id = getDetailOperation(row).primaryValue;
    currentSalespersonId.value = id;

    try {
        // 获取分销记录
        const response = await distributionSales.query(id);
        salesRecordsData.value = (response.records || response || []).map(
            (item) => {
                return {
                    ...item,
                    amount: new Decimal(item.frozenAmount || 0)
                        .plus(item.settledAmount || 0)
                        .plus(item.refundAmount || 0)
                        .toNumber()
                };
            }
        );

        // 获取状态统计数据
        const statsResponse = await distributionSales.statistics(id);
        // 为所有状态提供默认值0，金额从分转换为元，按指定顺序排列
        statusStatistics.value = {
            frozenAmount: new Decimal(
                statsResponse?.frozenAmount || 0
            ).toNumber(), // 冻结中
            incomingAmount: new Decimal(
                statsResponse?.incomingAmount || 0
            ).toNumber(), // 入账中
            settledAmount: new Decimal(
                statsResponse?.settledAmount || 0
            ).toNumber(), // 待结算
            withdrawAmount: new Decimal(
                statsResponse?.withdrawAmount || 0
            ).toNumber(), // 已结算
            refundAmount: new Decimal(
                statsResponse?.refundAmount || 0
            ).toNumber() // 订单退款
        };

        // 获取可结算的详情ID列表
        settledDetailIds.value = statsResponse?.settledDetailIds || [];

        salesRecordsVisible.value = true;
    } catch (error) {
        console.error('获取分销记录失败:', error);
        ElMessage.error('获取分销记录失败');
    }
};

// 关闭分销记录弹窗
const closeSalesRecordsDialog = () => {
    salesRecordsVisible.value = false;
    salesRecordsData.value = [];
    statusStatistics.value = {};
    currentSalespersonId.value = null;
    settledDetailIds.value = [];
};

// 显示结算对话框
const showSettlementDialog = () => {
    // 默认设置为全部待结算金额
    settlementForm.value.settledAmount = new Decimal(
        statusStatistics.value.settledAmount || 0
    )
        .div(100)
        .toNumber();
    settlementVisible.value = true;
};

// 处理结算
const handleSettlement = async () => {
    if (
        !settlementForm.value.settledAmount ||
        settlementForm.value.settledAmount <= 0
    ) {
        ElMessage.error('请输入有效的结算金额');
        return;
    }

    if (!currentSalespersonId.value) {
        ElMessage.error('未找到销售员信息');
        return;
    }

    try {
        // 将金额转为负数，并转换为分
        const settledAmount = -Math.abs(
            new Decimal(settlementForm.value.settledAmount).mul(100).toNumber()
        );
        const success = await distributionSales.settlement(
            currentSalespersonId.value,
            settledAmount,
            settledDetailIds.value
        );

        if (success) {
            ElMessage.success('结算成功');
        }

        settlementVisible.value = false;

        // 刷新分销记录
        await showSalesRecords({ id: currentSalespersonId.value });
    } catch (error) {
        console.error('结算失败:', error);
        ElMessage.error('结算失败');
    }
};

// 格式化状态
const formatStatus = (statusKey: string) => {
    const statusMap = {
        frozenAmount: '冻结中',
        incomingAmount: '入账中',
        settledAmount: '待结算',
        withdrawAmount: '已结算',
        refundAmount: '订单退款'
    };
    return statusMap[statusKey] || '未知状态';
};

// 格式化类型
const formatType = (type: number) => {
    const typeMap = {
        10: '分销',
        20: '结算'
    };
    return typeMap[type] || '未知类型';
};

// 获取类型标签类型
const getTypeTagType = (type: number) => {
    const typeMap = {
        10: 'primary',
        20: 'success'
    };
    return typeMap[type] || 'info';
};

// 格式化时间戳
const formatDateTime = (timestamp: number) => {
    if (!timestamp) return '-';
    const date = new Date(timestamp);
    return date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
    });
};
</script>

<style scoped>
.statistics-container {
    padding: 12px;
    background-color: #f8f9fa;
    border-radius: 6px;
    margin-bottom: 20px;
}

.statistics-card {
    text-align: center;
    border-radius: 6px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;
}

.statistics-card:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.statistics-item {
    padding: 8px 6px;
}

.statistics-label {
    font-size: 12px;
    color: #666;
    margin-bottom: 4px;
    font-weight: bold;
}

.statistics-value {
    font-size: 32px;
    font-weight: bold;
    transition: color 0.3s ease;
}

/* 冻结中 - 蓝灰色主题 */
.status-frozenAmount {
    border-left: 4px solid #909399;
}

.value-frozenAmount {
    color: #909399;
}

.status-frozenAmount:hover .value-frozenAmount {
    color: #73767a;
}

/* 待结算 - 橙色主题 */
.status-settledAmount {
    border-left: 4px solid #e6a23c;
}

.value-settledAmount {
    color: #e6a23c;
}

.status-settledAmount:hover .value-settledAmount {
    color: #cf9236;
}

/* 已结算 - 绿色主题 */
.status-withdrawAmount {
    border-left: 4px solid #67c23a;
}

.value-withdrawAmount {
    color: #67c23a;
}

.status-withdrawAmount:hover .value-withdrawAmount {
    color: #5daf34;
}

/* 订单退款 - 红色主题 */
.status-refundAmount {
    border-left: 4px solid #f56c6c;
}

.value-refundAmount {
    color: #f56c6c;
}

.status-refundAmount:hover .value-refundAmount {
    color: #f45656;
}

/* 入账中 - 青蓝色主题（介于冻结中和待结算之间） */
.status-incomingAmount {
    border-left: 4px solid #17a2b8;
}

.value-incomingAmount {
    color: #17a2b8;
}

.status-incomingAmount:hover .value-incomingAmount {
    color: #138496;
}

/* 表格中金额的颜色样式 */
.amount-frozen {
    color: #909399;
    font-weight: 600;
}

.amount-settled {
    color: #e6a23c;
    font-weight: 600;
}

.amount-withdraw {
    color: #67c23a;
    font-weight: 600;
}

.amount-refund {
    color: #f56c6c;
    font-weight: 600;
}
</style>

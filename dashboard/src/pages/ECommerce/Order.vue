<template>
    <PDataManager
        :handle-query="order.query"
        :handle-find="order.find"
        :handle-delete="order.del"
        :handle-create="order.create"
        :handle-update="order.update"
        :data-table-props="{
            add: false,
            tableOperation: []
        }">
        <template #table-column-operation="{ row }">
            <el-button
                v-if="row.refundStatus === 11"
                size="small"
                type="danger"
                :loading="refundLoading"
                @click="handleRefund(row)"
                >退款</el-button
            >
        </template>
    </PDataManager>
</template>

<script lang="ts" setup>
import { order } from '@/api';
import provideForm from '@/common/provideForm';
import { CheckOne, CloseOne, Time } from '@icon-park/vue-next';
import { h, ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';

// 退款loading状态
const refundLoading = ref(false);

// 交易状态枚举
const tradeStatusMap = {
    10: '待支付',
    20: '已支付',
    30: '待收货',
    40: '确认收货',
    50: '超时取消',
    51: '全部退款'
};

// 退款处理函数
const handleRefund = async (row: any) => {
    try {
        await ElMessageBox.confirm('确定要申请退款吗？', '退款确认', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        });

        refundLoading.value = true;
        await order.refund(row.orderTrade?.id);
        ElMessage.success('退款申请提交成功');

        // 刷新表格数据
        await queryTableData(async (params: any) => {
            return await order.query({
                ...params
            });
        });
    } catch (error: any) {
        if (error !== 'cancel') {
            ElMessage.error(error.message || '退款申请失败');
        }
    } finally {
        refundLoading.value = false;
    }
};

const { queryTableData, isTableDataLoading } = provideForm([
    {
        label: 'id',
        prop: 'id',
        isDetail: true,
        isPrimaryKey: true,
        hidden: true
    },
    {
        label: '订单号',
        prop: 'sn',
        isTable: true,
        isSearch: true,
        searchPlaceholder: '请输入订单号'
    },
    {
        label: '交易单号',
        prop: 'orderTrade.sn',
        isTable: true,
        isSearch: true,
        searchPlaceholder: '请输入订单号'
    },
    {
        label: '商品名',
        prop: 'goodsName',
        isTable: true,
        tableFormat: (_value, _row, _item) => {
            return _row.ticket?.[0].name || '';
        }
    },
    {
        label: '交易状态',
        prop: 'tradeStatus',
        isTable: true,
        isSearch: true,
        domType: 'select',
        domData: Object.entries(tradeStatusMap).map(([value, label]) => ({
            label,
            value: parseInt(value)
        })),
        tableFormat: (value) => {
            const status = tradeStatusMap[value as keyof typeof tradeStatusMap];
            return h(
                'span',
                { style: 'display: flex; align-items: center; gap: 4px;' },
                [status || '未知状态']
            );
        }
    },
    {
        label: '应付金额',
        prop: 'amountPayable',
        isTable: true,
        tableFormat: (value) => {
            return value ? `¥ ${value / 100}` : '-';
        }
    },
    {
        label: '实付金额',
        prop: 'amountPaid',
        isTable: true,
        domType: 'number',
        tableFormat: (value) => {
            return value ? `¥ ${value / 100}` : '-';
        }
    },
    {
        label: '退款金额',
        prop: 'refundAmount',
        isTable: true,
        domType: 'number',
        tableFormat: (value) => {
            return value ? `¥ ${value / 100}` : '-';
        }
    },
    {
        label: '下单人',
        prop: 'userPhone',
        isTable: true,
        isSearch: true
    },
    {
        label: '下单时间',
        prop: 'createdAt',
        isTable: true,
        isSearch: true,
        domType: 'dateTimePickerRange'
    },
    {
        label: '订单备注',
        prop: 'remark'
    }
]);
</script>

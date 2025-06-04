<template>
    <PDataManager
        :handle-query="order.query"
        :handle-find="order.find"
        :handle-delete="order.del"
        :handle-create="order.create"
        :handle-update="order.update">
    </PDataManager>
</template>

<script lang="ts" setup>
import { order } from '@/api';
import provideForm from '@/common/provideForm';
import { CheckOne, CloseOne, Time } from '@icon-park/vue-next';
import { h } from 'vue';

// 交易状态枚举
const tradeStatusMap = {
    10: '待支付',
    20: '已支付',
    30: '待收货',
    40: '确认收货',
    50: '超时取消',
    51: '全部退款'
};

// 退款状态枚举
const refundStatusMap = {
    10: '不可退款',
    11: '可退款',
    20: '退款申请',
    30: '退款中',
    40: '已退款'
};

// 评价状态枚举
const evaluationStatusMap = {
    10: '不可评价',
    20: '待评价',
    30: '已评价'
};

provideForm([
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
            const iconMap = {
                10: () => h(Time, { theme: 'outline', fill: '#faad14' }),
                20: () => h(CheckOne, { theme: 'outline', fill: '#52c41a' }),
                30: () => h(CheckOne, { theme: 'outline', fill: '#1890ff' }),
                40: () => h(CheckOne, { theme: 'outline', fill: '#52c41a' }),
                50: () => h(CloseOne, { theme: 'outline', fill: '#ff4d4f' }),
                51: () => h(CloseOne, { theme: 'outline', fill: '#ff4d4f' })
            };
            return h(
                'span',
                { style: 'display: flex; align-items: center; gap: 4px;' },
                [
                    iconMap[value as keyof typeof iconMap]?.(),
                    status || '未知状态'
                ]
            );
        }
    },
    {
        label: '退款状态',
        prop: 'refundStatus',
        isTable: true,
        isSearch: true,
        domType: 'select',
        domData: Object.entries(refundStatusMap).map(([value, label]) => ({
            label,
            value: parseInt(value)
        })),
        tableFormat: (value) => {
            return (
                refundStatusMap[value as keyof typeof refundStatusMap] ||
                '未知状态'
            );
        }
    },
    {
        label: '评价状态',
        prop: 'evaluationStatus',
        isTable: true,
        domType: 'select',
        domData: Object.entries(evaluationStatusMap).map(([value, label]) => ({
            label,
            value: parseInt(value)
        })),
        tableFormat: (value) => {
            return (
                evaluationStatusMap[
                    value as keyof typeof evaluationStatusMap
                ] || '未知状态'
            );
        }
    },
    {
        label: '应付金额',
        prop: 'amountPayable',
        isTable: true,
        tableFormat: (value) => {
            return value ? `¥ ${value / 100}` : '¥ 0.00';
        }
    },
    {
        label: '实付金额',
        prop: 'amountPaid',
        isTable: true,
        domType: 'number',
        tableFormat: (value) => {
            return value ? `¥ ${value / 100}` : '¥ 0.00';
        }
    },
    {
        label: '下单人',
        prop: 'creator.name',
        isTable: true
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

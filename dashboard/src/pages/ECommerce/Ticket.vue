<template>
    <PDataManager
        :handle-query="ticket.query"
        :handle-find="ticket.find"
        :handle-delete="ticket.del"
        :handle-create="ticket.create"
        :handle-update="ticket.update"
        :handle-format-data="formatData">
    </PDataManager>
</template>

<script lang="ts" setup>
import { h } from 'vue';
import Decimal from 'decimal.js';
import { ticket, theme } from '@/api';
import provideForm from '@/common/provideForm';
import { CrossRingTwo } from '@icon-park/vue-next';

provideForm([
    {
        label: 'id',
        prop: 'id',
        isDetail: true,
        isPrimaryKey: true,
        hidden: true
    },
    {
        label: '主题',
        prop: 'themeId',
        isTable: true,
        isDetail: true,
        isSearch: true,
        domType: 'select',
        domData: async (key) => {
            const result = await theme.query({ name: key });
            return result.records.map((item) => ({
                label: item.themeName,
                value: item.id
            }));
        },
        tableFormat: (_value, _row, _item) => {
            return _row.theme?.themeName || '';
        },
        rules: [
            {
                required: true,
                message: '主题必填',
                trigger: 'blur'
            }
        ]
    },
    {
        label: '名称',
        prop: 'name',
        isSearch: true,
        isSearchMore: false,
        searchPlaceholder: '模糊搜索',
        isTable: true,
        isDetail: true,
        rules: [
            {
                required: true,
                message: '名称必填',
                trigger: 'blur'
            },
            {
                max: 32,
                message: '名称最大长度为32',
                trigger: 'blur'
            }
        ]
    },
    {
        label: '描述',
        prop: 'details',
        isDetail: true,
        domType: 'input',
        inputType: 'textarea',
        rules: [
            {
                max: 500,
                message: '描述最大长度为500',
                trigger: 'blur'
            }
        ]
    },
    {
        label: '原价',
        prop: 'originalPrice',
        isTable: true,
        isDetail: true,
        domType: 'number',
        tableFormat: (value) => {
            return value ? `¥ ${value}` : '';
        },
        rules: [
            {
                required: true,
                message: '原价必填',
                trigger: 'blur'
            }
        ]
    },
    {
        label: '现价',
        prop: 'currentPrice',
        isTable: true,
        isDetail: true,
        domType: 'number',
        tableFormat: (value) => {
            return value ? `¥ ${value}` : '';
        },
        rules: [
            {
                required: true,
                message: '现价必填',
                trigger: 'blur'
            }
        ]
    },
    {
        label: '库存',
        prop: 'stock',
        isTable: true,
        isDetail: true,
        domType: 'number',
        tableFormat: (value) => {
            return value
                ? `${value} 张`
                : h(CrossRingTwo, {
                      title: '无限'
                  });
        },
        rules: [
            {
                type: 'number',
                min: 0,
                message: '库存必须为非负数',
                trigger: 'blur'
            }
        ]
    },
    {
        label: '核销次数',
        prop: 'validationCount',
        isTable: true,
        isDetail: true,
        domType: 'number',
        rules: [
            {
                type: 'number',
                min: 0,
                message: '核销次数必须为非负数',
                trigger: 'blur'
            }
        ],
        tableFormat: (value) => {
            return value
                ? `${value} 次`
                : h(CrossRingTwo, {
                      title: '无限'
                  });
        }
    },
    {
        label: '创建人',
        prop: 'creator.name',
        isTable: true,
        isDetail: false
    },
    {
        label: '创建时间',
        prop: 'createdAt',
        isTable: true,
        isSearch: true,
        domType: 'dateTimePickerRange'
    },
    {
        label: '更新时间',
        prop: 'updatedAt',
        isTable: true,
        domType: 'dateTimePickerRange'
    }
]);

const formatData = ({
    queryData,
    saveData
}: {
    queryData: any;
    saveData: any;
}) => {
    if (queryData?.currentPrice) {
        queryData.currentPrice = new Decimal(queryData.currentPrice).div(100);
    }
    if (queryData?.originalPrice) {
        queryData.originalPrice = new Decimal(queryData.originalPrice).div(100);
    }

    if (saveData?.currentPrice) {
        saveData.currentPrice = new Decimal(saveData.currentPrice).mul(100);
    }
    if (saveData?.originalPrice) {
        saveData.originalPrice = new Decimal(saveData.originalPrice).mul(100);
    }
};
</script>

<template>
    <PDataManager
        :handle-query="ticket.query"
        :handle-find="ticket.find"
        :handle-delete="ticket.del"
        :handle-create="ticket.create"
        :handle-update="ticket.update">
    </PDataManager>
</template>

<script lang="ts" setup>
import { ticket, theme } from '@/api';
import provideForm from '@/common/provideForm';
import { CrossRingTwo } from '@icon-park/vue-next';
import { h } from 'vue';

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
        tableFormat: (_value, row, _item) => {
            return row.theme?.name || '';
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
        label: '原价（分）',
        prop: 'originalPrice',
        isTable: true,
        isDetail: true,
        domType: 'number',
        tableFormat: (value) => {
            return value ? `¥ ${(value / 100).toFixed(2)}` : '';
        },
        rules: [
            {
                type: 'number',
                min: 0,
                message: '原价必须为非负数',
                trigger: 'blur'
            }
        ]
    },
    {
        label: '现价（分）',
        prop: 'currentPrice',
        isTable: true,
        isDetail: true,
        domType: 'number',
        tableFormat: (value) => {
            return value ? `¥ ${(value / 100).toFixed(2)}` : '';
        },
        rules: [
            {
                type: 'number',
                min: 0,
                message: '现价必须为非负数',
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
            return value ? `${value} 次` : '';
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
</script>

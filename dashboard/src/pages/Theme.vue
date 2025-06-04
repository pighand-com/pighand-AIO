<template>
    <PDataManager
        :handle-query="theme.query"
        :handle-find="theme.find"
        :handle-delete="theme.del"
        :handle-create="theme.create"
        :handle-update="theme.update"
        :drawer-props="{
            size: '60%'
        }">
    </PDataManager>
</template>

<script lang="ts" setup>
import { theme } from '@/api/index.ts';
import provideForm from '@/common/provideForm';

provideForm([
    {
        label: 'id',
        prop: 'id',
        isDetail: true,
        isPrimaryKey: true,
        hidden: true
    },
    {
        label: '主题名称',
        prop: 'themeName',
        isSearch: true,
        isSearchMore: false,
        searchPlaceholder: '模糊搜索主题名称',
        isTable: true,
        tableAlign: 'left',
        isDetail: true,
        rules: [
            {
                required: true,
                message: '主题名称必填',
                trigger: 'blur'
            }
        ]
    },
    {
        label: '主题简介',
        prop: 'themeIntroductione',
        isTable: false,
        isDetail: true,
        domType: 'input',
        inputType: 'textarea',
        componentProps: {
            row: 5,
            maxlength: 500
        },
        rules: [
            {
                max: 500,
                message: '主题简介不能超过500字符',
                trigger: 'blur'
            }
        ]
    },
    {
        label: '主题封面',
        prop: 'posterUrl',
        isTable: false,
        isDetail: true,
        domType: 'uploadImage',
        tableType: 'image',
        rules: [
            {
                required: true,
                message: '主题封面必填',
                trigger: 'blur'
            }
        ],
        uploadPath: 'theme'
    },
    {
        label: '主题描述',
        prop: 'pictureDescription',
        isDetail: true,
        domType: 'editor'
    },
    {
        label: '原价（元）',
        prop: 'originalPrice',
        isTable: true,
        isDetail: true,
        domType: 'number',
        tableFormat: (value) => {
            return value ? `¥${(value / 100).toFixed(2)}` : '-';
        },
        rules: [
            {
                required: true,
                message: '原价必填',
                trigger: 'blur'
            },
            {
                type: 'number',
                min: 0,
                message: '原价不能小于0',
                trigger: 'blur'
            }
        ]
    },
    {
        label: '现价（元）',
        prop: 'presentPrice',
        isTable: true,
        isDetail: true,
        domType: 'number',
        tableFormat: (value) => {
            return value ? `¥${(value / 100).toFixed(2)}` : '-';
        },
        rules: [
            {
                required: true,
                message: '现价必填',
                trigger: 'blur'
            },
            {
                type: 'number',
                min: 0,
                message: '现价不能小于0',
                trigger: 'blur'
            }
        ]
    },
    {
        label: '最少人数',
        prop: 'minPeople',
        isTable: true,
        isDetail: true,
        domType: 'number',
        rules: [
            {
                required: true,
                message: '最少人数必填',
                trigger: 'blur'
            },
            {
                type: 'number',
                min: 1,
                message: '最少人数不能小于1',
                trigger: 'blur'
            }
        ]
    },
    {
        label: '最大人数',
        prop: 'maxPeople',
        isTable: true,
        isDetail: true,
        domType: 'number',
        rules: [
            {
                required: true,
                message: '最大人数必填',
                trigger: 'blur'
            },
            {
                type: 'number',
                min: 1,
                message: '最大人数不能小于1',
                trigger: 'blur'
            }
        ]
    },
    {
        label: '时长（分钟）',
        prop: 'duration',
        isTable: true,
        isDetail: true,
        domType: 'number',
        tableFormat: (value) => {
            return value ? `${value}分钟` : '-';
        },
        rules: [
            {
                required: true,
                message: '时长必填',
                trigger: 'blur'
            },
            {
                type: 'number',
                min: 1,
                message: '时长不能小于1分钟',
                trigger: 'blur'
            }
        ]
    },
    {
        label: '创建时间',
        prop: 'createdAt',
        isTable: true,
        domType: 'dateTimePicker'
    }
]);
</script>

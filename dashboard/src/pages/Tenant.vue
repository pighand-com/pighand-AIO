<template>
    <PDataManager :handle-query="tenant.query" :handle-find="tenant.find" :handle-delete="tenant.del"
        :handle-create="tenant.create" :handle-update="tenant.update">
    </PDataManager>
</template>

<script lang="ts" setup>
import { tenant } from '@/api';
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
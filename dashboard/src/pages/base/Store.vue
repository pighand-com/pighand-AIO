<template>
    <PDataManager
        :handle-query="store.query"
        :handle-find="store.find"
        :handle-delete="store.del"
        :handle-create="store.create"
        :handle-update="store.update">
    </PDataManager>
</template>

<script lang="ts" setup>
import { store, tenant } from '@/api';
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
        label: '简介',
        prop: 'introduce',
        isTable: false,
        isDetail: true,
        domType: 'editor'
    },
    {
        label: '商户',
        prop: 'tenantId',
        isTable: true,
        isDetail: true,
        isSearch: true,
        domType: 'select',
        domData: async (key) => {
            const result = await tenant.query({ name: key });
            return result.records.map((item) => ({
                label: item.name,
                value: item.id
            }));
        },
        tableFormat: (_value, _row, _item) => {
            return _row.tenant?.name || '';
        },
        rules: [
            {
                required: true,
                message: '商户必填',
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

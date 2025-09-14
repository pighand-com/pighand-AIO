<template>
    <PDataManager
        :handle-query="assetsCollection.query"
        :handle-find="assetsCollection.find"
        :handle-delete="assetsCollection.del"
        :handle-create="assetsCollection.create"
        :handle-update="assetsCollection.update">
    </PDataManager>
</template>

<script lang="ts" setup>
import { assetsCollection } from '@/api/index.ts';
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
        label: '专辑名称',
        prop: 'name',
        isSearch: true,
        searchPlaceholder: '模糊搜索',
        isTable: true,
        isDetail: true,
        rules: [
            {
                required: true,
                message: '专辑名称必填',
                trigger: 'blur'
            },
            {
                max: 50,
                message: '最大长度为50',
                trigger: 'blur'
            }
        ],
    },
    {
        label: '专辑封面',
        prop: 'coverUrl',
        isTable: true,
        isDetail: true,
        domType: 'uploadImage',
        tableType: 'image',
        uploadPath: 'collection',
    },
    {
        label: '创建人',
        prop: 'creator.name',
        isTable: true,
        isDetail: false,
    },
    {
        label: '创建时间',
        prop: 'createdAt',
        isTable: true,
        domType: 'dateTimePicker',
    },
    {
        label: '更新时间',
        prop: 'updatedAt',
        isTable: true,
        domType: 'dateTimePicker',
    }
]);
</script>
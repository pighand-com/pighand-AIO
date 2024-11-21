<template>
    <PDataManager
        :handle-query="user.query"
        :handle-find="user.find"
        :handle-delete="user.del"
        :handle-create="user.create"
        :handle-update="user.update">
    </PDataManager>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { user } from '@/api/index.ts';
import provideForm from '@/common/provideForm';

const { detailFormModel } = provideForm([
    {
        label: 'id',
        prop: 'id',
        isSearch: true,
        isSearchMore: false,
        isDetail: true,
        isPrimaryKey: true,
        hidden: true
    },
    {
        label: '姓名',
        prop: 'name',
        isSearch: true,
        isSearchMore: false,
        searchPlaceholder: '模糊搜索',
        isTable: true,
        isDetail: true,
        rules: [
            {
                required: true,
                message: '姓名必填',
                trigger: 'blur'
            }
        ]
    }
]);
</script>

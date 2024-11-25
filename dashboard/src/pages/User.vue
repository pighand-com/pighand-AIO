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
        label: 'ID',
        prop: 'id',
        isSearch: true,
        isSearchMore: false,
        isPrimaryKey: true,
        isTable: true
    },
    {
        label: '电话',
        prop: 'phone',
        isSearch: true,
        isSearchMore: false,
        isTable: true
    },
    {
        label: '账号',
        prop: 'username',
        isSearch: true,
        isSearchMore: false,
        isTable: true
    }
]);
</script>

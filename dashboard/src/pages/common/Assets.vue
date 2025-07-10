<template>
    <PDataManager :handle-query="assets.query" :handle-find="assets.find" :handle-delete="assets.del"
        :handle-create="assets.create" :handle-update="assets.update" :data-table-props="{
            batchActions: [
                {
                    name: '批量删除',
                    handler: handleBatchDelete
                },
            ]
        }">
    </PDataManager>
</template>

<script lang="ts" setup>
import { assets } from '@/api';
import provideForm from '@/common/provideForm';

const { queryTableDataDefault } = provideForm([
    {
        label: 'id',
        prop: 'id',
        isDetail: true,
        isPrimaryKey: true,
        hidden: true
    },
    {
        label: '标签',
        prop: 'tag',
        isSearch: true,
        searchPlaceholder: '模糊搜索',
        isTable: true,
        isDetail: true,
        rules: [
            {
                max: 32,
                message: '最大长度为32',
                trigger: 'blur'
            }
        ],
        tableWidth: 160
    },
    {
        label: 'URL',
        prop: 'url',
        isTable: true,
        domType: 'uploadFile',
        tableType: 'link',
        isTableCopyValue: true,
    },
    {
        label: '文件',
        prop: 'urls',
        domType: 'uploadFileList',
        uploadPath: 'assets',
        isDetail: true,
        isUpdate: false,
    },
    {
        label: '创建人',
        prop: 'creator.name',
        isTable: true,
        isDetail: false,
        tableWidth: 120
    },
    {
        label: '创建时间',
        prop: 'createdAt',
        isTable: true,
        isSearch: true,
        domType: 'dateTimePickerRange',
        tableWidth: 120
    },
    {
        label: '更新时间',
        prop: 'updatedAt',
        isTable: true,
        domType: 'dateTimePickerRange',
        tableWidth: 120
    }
]);

// 批量删除处理函数
const handleBatchDelete = (selection: any[]) => {
    const ids = selection.map(item => item.id);
    ElMessageBox.confirm('确认删除选中的资源吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(() => {
        assets.batchDelete(ids).then(() => {
            queryTableDataDefault(assets.query);

            ElMessage.success('删除成功');
        });
    }).catch(() => { });
};
</script>
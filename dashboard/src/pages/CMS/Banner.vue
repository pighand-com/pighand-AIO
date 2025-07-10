<template>
    <PDataManager
        :handle-query="banner.query"
        :handle-find="banner.find"
        :handle-delete="banner.del"
        :handle-create="banner.create"
        :handle-update="banner.update">
        <template #table-column-operation="{ row }">
            <el-button
                v-if="row.status === 0"
                size="small"
                type="success"
                :icon="InboxOut"
                @click="online(row)"
                >上架</el-button
            >
            <el-button
                v-if="row.status === 10"
                size="small"
                type="warning"
                :icon="InboxIn"
                @click="offline(row)"
                >下架</el-button
            >
        </template>
    </PDataManager>
</template>

<script lang="ts" setup>
import { h } from 'vue';
import { banner } from '@/api';
import provideForm from '@/common/provideForm';
import { InboxOut, InboxIn } from '@icon-park/vue-next';
import { ElTag } from 'element-plus';

const { getDetailOperation, queryTableData } = provideForm([
    {
        label: 'id',
        prop: 'id',
        isDetail: true,
        isPrimaryKey: true,
        hidden: true
    },
    {
        label: '描述',
        prop: 'description',
        isTable: true,
        isDetail: true
    },
    {
        label: '图片',
        prop: 'imageUrl',
        isTable: true,
        isDetail: true,
        domType: 'uploadImage',
        tableType: 'image',
        rules: [
            {
                required: true,
                message: '图片必填',
                trigger: 'blur'
            }
        ],
        uploadPath: 'banner'
    },
    {
        label: '跳转地址',
        prop: 'redirectionPath',
        isTable: true,
        isDetail: true
    },
    {
        label: '状态',
        prop: 'status',
        isTable: true,
        isSearch: true,
        domType: 'select',
        domData: [
            {
                label: '下架',
                value: 0
            },
            {
                label: '上架',
                value: 10
            }
        ],
        tableFormat: (_value, _row, _item) => {
            const type = _value === 0 ? 'danger' : 'success';
            const text = _value === 0 ? '下架' : '上架';
            return h(ElTag, { type: type }, text);
        }
    }
]);

// 上架
const online = async (row) => {
    const id = getDetailOperation(row).primaryValue;

    await banner.online(id);

    await queryTableData(async (params: any) => {
        return await banner.query({
            ...params
        });
    });
};

// 下架
const offline = async (row) => {
    const id = getDetailOperation(row).primaryValue;

    await banner.offline(id);

    await queryTableData(async (params: any) => {
        return await banner.query({
            ...params
        });
    });
};
</script>

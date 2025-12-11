<template>
    <PDataManager
        :handle-query="user.query"
        :handle-find="find"
        :handle-delete="user.del"
        :handle-create="user.create"
        :handle-update="user.update">
    </PDataManager>
</template>

<script lang="ts" setup>
import { h } from 'vue';
import { user, role } from '@/api/index.ts';
import provideForm from '@/common/provideForm';
import { ElTag } from 'element-plus';

provideForm([
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
        isTable: true,
        isDetail: true,
        domType: 'input'
    },
    {
        label: '密码',
        prop: 'password',
        isDetail: true,
        domType: 'input',
        isUpdate: false,
        rules: [
            {
                required: true,
                message: '密码必填',
                trigger: 'blur'
            }
        ]
    }
    // {
    //     label: '角色',
    //     prop: 'roleIds',
    //     isTable: true,
    //     isDetail: true,
    //     domType: 'select',
    //     domData: async (key) => {
    //         const result = await role.query({ name: key });
    //         return result.records.map((item) => ({
    //             label: item.name,
    //             value: item.id
    //         }));
    //     },
    //     componentProps: {
    //         multiple: true
    //     },
    //     tableFormat: (_value, _row, _item) => {
    //         if (!_row?.roles || _row.roles.length === 0) {
    //             return '-';
    //         }
    //         return h(
    //             'div',
    //             { style: 'display: flex; flex-wrap: wrap; gap: 4px;' },
    //             _row.roles.map((role) =>
    //                 h(
    //                     ElTag,
    //                     {
    //                         size: 'small',
    //                         type: 'info',
    //                         effect: 'plain',
    //                         round: true
    //                     },
    //                     role.name
    //                 )
    //             )
    //         );
    //     },
    //     rules: [
    //         {
    //             required: true,
    //             message: '角色必填',
    //             trigger: 'blur'
    //         }
    //     ]
    // }
]);

const find = async (id: any) => {
    const detail = await user.find(id);

    detail.roleIds = detail.roles.map((role) => role.roleId);
    return detail;
};
</script>

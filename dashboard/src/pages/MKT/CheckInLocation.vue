<template>
    <PDataManager
        :handle-query="checkInLocation.query"
        :handle-find="checkInLocation.find"
        :handle-delete="checkInLocation.del"
        :handle-create="checkInLocation.create"
        :handle-update="checkInLocation.update"
        :drawer-props="{
            size: '60%'
        }">

        

    </PDataManager>


</template>

<script setup lang="ts">
import checkInLocation from '@/api/checkInLocation';
import provideForm from '@/common/provideForm';

// 使用provideForm配置表单字段
const { detailFormModel, watchDetailForm } = provideForm(
    [
        {
            label: 'ID',
            prop: 'id',
            isDetail: true,
            isPrimaryKey: true,
            hidden: true,
            isTable: true
        },
        {
            label: '地点名称',
            prop: 'name',
            isTable: true,
            isDetail: true,
            isSearch: true,
            domType: 'input',
            rules: [
                {
                    required: true,
                    message: '地点名称必填',
                    trigger: 'blur'
                },
                {
                    max: 50,
                    message: '最大长度50个字符',
                    trigger: 'blur'
                }
            ]
        },

    ]
);




</script>

<style scoped>
.dialog-footer {
    margin-top: 20px;
    text-align: center;
}
</style>
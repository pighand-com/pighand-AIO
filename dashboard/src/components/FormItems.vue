<!-- eslint-disable vue/no-mutating-props -->
<template>
    <el-row :gutter="20" v-if="props.onWhere === 'search'">
        <el-col
            :xs="24"
            :md="item.domType === 'datePickerRange' ? 12 : 12"
            :lg="item.domType === 'datePickerRange' ? 8 : 6"
            :xl="item.domType === 'datePickerRange' ? 4 : 3"
            v-for="(item, index) in props.formColumns"
            :key="index">

            <slot :name="`${onWhere}-${index}-before`" />
            <slot :name="`${onWhere}-${item.prop}-before`" />
            <FormItem :formColumnItem="formatFormColumn(item)" :formModel="props.formModel" />
            <slot :name="`${onWhere}-${item.prop}-after`" />
            <slot :name="`${onWhere}-${index}-after`" />

            <slot v-if="index === props.formColumns.length - 1" :name="`${onWhere}-last`" />
        </el-col>
    </el-row>

    <span v-else v-for="(item, index) in props.formColumns" :key="index">

        <slot :name="`${onWhere}-${index}-before`" />
        <slot :name="`${onWhere}-${item.prop}-before`" />
        <FormItem :formColumnItem="formatFormColumn(item)" :formModel="props.formModel" />
        <slot :name="`${onWhere}-${item.prop}-after`" />
        <slot :name="`${onWhere}-${index}-after`" />
        
        <slot v-if="index === props.formColumns.length - 1" :name="`${onWhere}-last`" />
    </span>
</template>

<script lang="ts" setup>
import {
    FormColumnsInterface,
} from '@/common/provideForm';
import FormItem from '@/components/FormItem.vue';

/**
 * 组件属性定义
 * @property {Array<FormColumnsInterface>} formColumns - 表单列配置数组,定义表单的每一列的配置信息
 * @property {Object} formModel - 表单数据模型,用于双向绑定表单数据
 * @property {String} onWhere - 表单所在位置标识,可选值:'search'(搜索表单)/'detail'(详情表单)
 */
const props = defineProps({
    formColumns: Array<FormColumnsInterface>,
    formModel: Object,
    onWhere: String
});

let placeholder = 'placeholder';
if (props.onWhere === 'search') {
    placeholder = 'searchPlaceholder';
} else {
    placeholder = 'detailPlaceholder';
}

// 格式化formColumns
const formatFormColumn = (item: FormColumnsInterface): FormColumnsInterface => {
    const formattedItem = { ...item };
    
    formattedItem.placeholder = item[placeholder]
        || item.placeholder 
        || (['select','radio'].includes(item.domType) && props.onWhere === 'search' ? '全部' : '');

    if(props.onWhere === 'search' && formattedItem.domType === 'radio') {
        formattedItem.domType = 'select';
    }
    
    return formattedItem;
}
</script>

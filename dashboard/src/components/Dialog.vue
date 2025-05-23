<!-- eslint-disable vue/no-mutating-props -->
<template>
    <el-dialog :model-value="isDialogDetail" :title="title" :before-close="onClose" width="32%">
        <el-form v-loading="loading" :model="dialogFormModel" :disabled="loading" :rules="dialogFormRules"
            ref="dialogForm" label-position="right" label-width="120px"
            :style="'display: flex; justify-content: ' + itemAlign">
            <slot />
        </el-form>
        <template #footer>
            <el-button v-if="!loading" @click="onClose">取消</el-button>
            <el-button type="primary" :loading="loading" @click="onSubmit()">保存</el-button>
        </template>
    </el-dialog>
</template>

<script lang="ts" setup>
import { ref, inject, watch } from 'vue';
import { ProvideFormInterface } from '@/common/provideForm';

/**
 * 组件属性定义
 * @property {String} title - 对话框标题
 * @property {Boolean} isDialogDetail - 是否显示对话框,默认为false
 * @property {String} itemAlign - 表单项对齐方式,默认为'left'
 * @property {Object} dialogFormModel - 对话框表单数据模型
 * @property {Object} dialogFormRules - 对话框表单验证规则
 * @property {Function} handleSubmit - 提交表单处理函数
 * @property {Function} handleQuery - 查询数据处理函数
 * @property {Function} handleReset - 重置表单处理函数
 */
const props = defineProps({
    title: String,
    isDialogDetail: {
        type: Boolean,
        default: false
    },
    itemAlign: {
        type: String,
        default: 'left'
    },
    dialogFormModel: Object,
    dialogFormRules: Object,
    handleSubmit: Function,
    handleQuery: Function,
    handleReset: Function
});

const emits = defineEmits(['update:isDialogDetail']);

const loading = ref(false);
const dialogForm = ref();
const oldData = ref({ ...(props.dialogFormModel || {}) });

const provideForm: ProvideFormInterface = inject('provideForm');
const { pageNumber, queryTableData, getDetailOperation, detailFormModel } =
    provideForm;

// const formStyle = {
//     display: 'flex',
//     justifyContent: itemAlign
// };

/**
 * 页面打开，记录旧数据，用于判断内容是否修改
 */
watch(
    () => props.isDialogDetail,
    (val) => {
        if (val) {
            oldData.value = { ...(props.dialogFormModel || {}) };
        }
    }
);

/**
 * 关闭或取消
 */
const onClose = () => {
    if (loading.value) {
        return;
    }

    // 判断内容忽略空值
    const ignore = (_key: any, value: any) => {
        if (value === null || value === '') {
            return undefined;
        }
        return value;
    };

    // 内容未修改直接关闭
    if (
        JSON.stringify(oldData.value, ignore) ===
        JSON.stringify(props.dialogFormModel, ignore)
    ) {
        emits('update:isDialogDetail', false);
        dialogForm.value.resetFields();
        if (props.handleReset) {
            props.handleReset();
        }
        return;
    }

    // 有修改先提示
    ElMessageBox.confirm('数据已修改，是否放弃?', {
        cancelButtonText: '返回',
        confirmButtonText: '放弃修改',
        confirmButtonClass: 'el-button--danger'
    }).then(() => {
        emits('update:isDialogDetail', false);
        dialogForm.value.resetFields();

        if (props.handleReset) {
            props.handleReset();
        }
    });
};

/**
 * 点击保存按钮
 */
const onSubmit = async () => {
    if (!props.handleSubmit) return;

    const { op, primaryKey } = getDetailOperation(detailFormModel);

    await dialogForm.value.validate(async (valid, _fields) => {
        if (valid) {
            loading.value = true;

            // 只传修改过的数据
            const onlyUpdateData: any = {};
            Object.keys(props.dialogFormModel).forEach((key) => {
                if (
                    props.dialogFormModel[key] !== oldData.value[key] ||
                    key === primaryKey
                ) {
                    onlyUpdateData[key] = props.dialogFormModel[key];
                }
            });

            await props.handleSubmit(onlyUpdateData);

            loading.value = false;
            emits('update:isDialogDetail', false);
            dialogForm.value.resetFields();

            if (props.handleReset) {
                props.handleReset();
            }

            // 保存后查询
            if (props.handleQuery) {
                await queryTableData(async (params: any) => {
                    pageNumber.value = 1;

                    return await props.handleQuery({
                        ...params,
                        pageNumber: 1
                    });
                });
            }
        }
    });
};
</script>

<style lang="scss" scoped>
:deep(.el-dialog__body) {
    padding: auto 80px;
}

:deep(.el-input) {
    width: 240px !important;
}
</style>

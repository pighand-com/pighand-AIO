<!-- eslint-disable vue/no-mutating-props -->
<template>
    <div>
        <el-drawer
            :model-value="isOpenDetail"
            :title="title"
            :before-close="onClose"
            :size="size"
            direction="rtl">
            <div class="drawer-bg"></div>
            <el-form
                v-loading="isDetailDataLoading"
                :model="detailFormModel"
                :disabled="loading"
                :rules="formRules"
                ref="detailForm"
                label-position="right"
                label-width="auto">
                <FormItems
                    :form-model="detailFormModel"
                    :form-columns="
                        formColumns.filter((item) => getIsDetail(item))
                    "
                    :on-where="'detail'">
                    <template
                        v-for="(item, index) in formColumns.filter((item) =>
                            getIsDetail(item)
                        )"
                        :key="index"
                        #[`detail-${index}-before`]>
                        <slot :name="`${index}-before`" />
                    </template>

                    <template
                        v-for="(item, index) in formColumns.filter((item) =>
                            getIsDetail(item)
                        )"
                        :key="index"
                        #[`detail-${item.prop}-before`]>
                        <slot :name="`${item.prop}-before`" />
                    </template>

                    <template
                        v-for="(item, index) in formColumns.filter((item) =>
                            getIsDetail(item)
                        )"
                        :key="index"
                        #[`detail-${item.prop}-after`]>
                        <slot :name="`${item.prop}-after`" />
                    </template>

                    <template
                        v-for="(item, index) in formColumns.filter((item) =>
                            getIsDetail(item)
                        )"
                        :key="index"
                        #[`detail-${index}-after`]>
                        <slot :name="`${index}-after`" />
                    </template>

                    <template #detail-last>
                        <slot name="detail-last" />
                    </template>
                </FormItems>

                <slot />
            </el-form>
            <template #footer>
                <el-button
                    v-if="!isDetailDataLoading && !loading"
                    @click="onClose"
                    >取消</el-button
                >
                <el-button
                    v-if="!isDetailDataLoading"
                    type="primary"
                    :loading="loading"
                    @click="onSubmit()"
                    >保存</el-button
                >
            </template>
        </el-drawer>
    </div>
</template>

<script lang="ts" setup>
import { ref, watch, inject, nextTick } from 'vue';
import { ElMessageBox } from 'element-plus';
import FormItems from '@/components/FormItems.vue';
import { ProvideFormInterface } from '@/common/provideForm';

/**
 * 组件属性定义
 * @property {String} title - 抽屉标题
 * @property {Function} handleCreate - 创建数据处理函数
 * @property {Function} handleUpdate - 更新数据处理函数
 * @property {Function} handleQuery - 查询数据处理函数
 * @property {String} size - 抽屉宽度,默认为30%
 */
const props = defineProps({
    title: String,
    handleCreate: Function,
    handleUpdate: Function,
    handleQuery: Function,
    size: {
        type: String,
        default: '30%'
    }
});

const provideForm: ProvideFormInterface = inject('provideForm');
const {
    isDetailDataLoading,
    pageNumber,
    formColumns,
    formRules,
    isOpenDetail,
    detailFormModel,
    getDetailOperation,
    queryTableData,
    detailDefaultValue,
    getDomData,
    domDataSet
} = provideForm;

const loading = ref(false);
const oldData = ref({ ...(detailFormModel || {}) });
const detailForm = ref();

/**
 * 打开页面，并且加载完成时。记录原数据，用于判断是否修改
 */
watch(
    () => isOpenDetail.value && !isDetailDataLoading.value,
    (newValue) => {
        if (!newValue) {
            return;
        }

        // 设置默认值
        nextTick(() => {
            Object.keys(detailDefaultValue).forEach((key) => {
                detailFormModel[key] = detailDefaultValue[key];
            });

            oldData.value = { ...(detailFormModel || {}) };
        });
    }
);

/**
 * 判断字段是否是详情
 */
const getIsDetail = (formColumnItem: any) => {
    const { isDetail, isCreate, isUpdate, label, prop, domDataInit } =
        formColumnItem;

    const operation = getDetailOperation(detailFormModel).op;

    if (
        (operation === 'create' && !isCreate) ||
        (operation === 'update' && !isUpdate) ||
        isDetail === undefined ||
        isDetail === false
    ) {
        return false;
    }

    let detail = isDetail;

    // 判断isDetail是否是函数
    if (typeof isDetail === 'function') {
        detail = isDetail(detailFormModel);
    }

    // 如果需要显示，并数据的类型是方法，先通过方法获取初始值
    if (detail) {
        if (getDomData[label + prop]) {
            getDomData[label + prop]().then(() => {
                domDataInit(domDataSet[prop], detailFormModel);
            });
        } else if (domDataInit) {
            domDataInit(domDataSet[prop], detailFormModel);
        }
    }

    return detail;
};

/**
 * 由于form中有些隐藏或动态显示的字段。使用resetFields无法重置。所以需要手动重置
 */
const resetFields = () => {
    Object.keys(detailFormModel).forEach((key) => {
        detailFormModel[key] =
            formColumns.find((item) => item.prop === key)?.domType === 'editor'
                ? ''
                : null;
    });
};

/**
 * 关闭或取消
 */
const onClose = () => {
    if (loading.value) {
        return;
    }
    const { op } = getDetailOperation(detailFormModel);

    // 判断内容忽略空值
    const ignore = (_key: any, value: any) => {
        if (value === null || value === '') {
            return undefined;
        }

        // 创建忽略默认值
        if (op === 'create') {
            if (value === detailDefaultValue[_key]) {
                return undefined;
            }
        }

        return value;
    };

    // 内容未修改直接关闭
    if (
        JSON.stringify(oldData.value, ignore) ===
        JSON.stringify(detailFormModel, ignore)
    ) {
        isOpenDetail.value = false;
        resetFields();
        return;
    }

    // 有修改先提示
    ElMessageBox.confirm('数据已修改，是否放弃?', {
        cancelButtonText: '返回',
        confirmButtonText: '放弃修改',
        confirmButtonClass: 'el-button--danger'
    }).then(() => {
        resetFields();
    });
};

/**
 * 点击保存按钮
 */
const onSubmit = async () => {
    const { op, primaryKey } = getDetailOperation(detailFormModel);

    if (
        (op === 'create' && !props.handleCreate) ||
        (op == 'update' && !props.handleUpdate)
    ) {
        return;
    }

    await detailForm.value.validate(async (valid, _fields) => {
        if (valid) {
            loading.value = true;

            // 只传修改过的数据
            const onlyUpdateData: any = {};
            Object.keys(detailFormModel).forEach((key) => {
                if (
                    detailFormModel[key] !== oldData.value[key] ||
                    key === primaryKey
                ) {
                    onlyUpdateData[key] = detailFormModel[key];
                }
            });

            if (op === 'create') {
                await props.handleCreate(onlyUpdateData);
            } else {
                await props.handleUpdate(onlyUpdateData);
            }

            loading.value = false;
            isOpenDetail.value = false;
            resetFields();

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

<style scoped>
.drawer-bg {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;

    background: linear-gradient(
        to right,
        rgba(183, 223, 255, 0.1) 0%,
        rgba(147, 129, 255, 0.15) 25%,
        rgba(190, 144, 255, 0.2) 50%,
        rgba(126, 188, 255, 0.25) 75%
    );
    mask-image: linear-gradient(to top, transparent, black);
    -webkit-mask-image: linear-gradient(to top, transparent, black);
    width: 100%;
    height: 24%;
}

.el-form {
    position: relative;
}

:deep(.el-overlay) {
    background: #e3e3e396;
}

:deep(.el-drawer) {
    margin: 16px !important;
    height: calc(100% - 32px) !important;
    border-radius: 16px !important;
    overflow: hidden !important;
    border: solid 1px #fff;

    box-shadow: 0px 20px 60px 20px rgba(0, 0, 0, 0.06),
        0px 16px 40px rgba(0, 0, 0, 0.08),
        0px 10px 20px -10px rgba(0, 0, 0, 0.1);
}

:deep(.el-drawer__header),
:deep(el-drawer__body),
:deep(.el-drawer__footer) {
    position: relative !important;
    z-index: 1 !important;
}
</style>

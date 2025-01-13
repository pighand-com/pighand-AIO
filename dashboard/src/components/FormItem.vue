<!-- eslint-disable vue/no-mutating-props -->
<template>
    <PDivider :config="formColumnItem.divider" />

    <el-form-item :style="{ width: '100%' }" v-show="!formColumnItem.hidden" :label="formColumnItem.label"
        :prop="formColumnItem.prop">

        <el-input v-if="!formColumnItem.domType || formColumnItem.domType === 'input'"
            v-bind="formColumnItem.componentProps || {}" v-model="formModel[formColumnItem.prop]"
            :type="formColumnItem.inputType || 'text'" :show-password="formColumnItem.inputType === 'password'"
            :placeholder="formColumnItem.placeholder" autocomplete="off" clearable />

        <el-input-number v-if="formColumnItem.domType === 'number'" v-bind="formColumnItem.componentProps || {}"
            v-model="formModel[formColumnItem.prop]" clearable />

        <el-select v-if="formColumnItem.domType === 'select'" v-bind="formColumnItem.componentProps || {}"
            v-model="formModel[formColumnItem.prop]" :placeholder="formColumnItem.placeholder"
            :remote-method="getDomData[formColumnItem.label + formColumnItem.prop]"
            :remote="!!getDomData[formColumnItem.label + formColumnItem.prop]"
            :loading="domDataSetLoading[formColumnItem.label + formColumnItem.prop]" remote-show-suffix filterable
            clearable>
            <el-option v-for="domDataItem in domDataSet[formColumnItem.prop]" :key="domDataItem.value"
                :label="domDataItem.label" :value="domDataItem.value" />
        </el-select>

        <el-radio-group v-if="formColumnItem.domType === 'radio'" v-bind="formColumnItem.componentProps || {}"
            v-model="formModel[formColumnItem.prop]">
            <el-radio v-for="domDataItem in domDataSet[formColumnItem.prop]" :key="domDataItem.value"
                :label="domDataItem.label" :value="domDataItem.value">
                {{ domDataItem.label }}
            </el-radio>
        </el-radio-group>

        <el-date-picker v-if="
            ['datePicker', 'datePickerRange', 'dateTimePickerRange', 'dateTimePicker'].includes(formColumnItem.domType)
        " v-bind="formColumnItem.componentProps || {}" v-model="formModel[formColumnItem.prop]" :type="formColumnItem.domType === 'datePicker'
            ? 'date'
            : formColumnItem.domType === 'datePickerRange'
                ? 'daterange'
                : formColumnItem.domType === 'dateTimePicker'
                    ? 'datetime'
                    : 'datetimerange'" range-separator="-"
            :value-format="formColumnItem.domType.startsWith('dateTime') ? 'YYYY-MM-DD HH:mm:ss' : 'YYYY-MM-DD'"
            :placeholder="formColumnItem.placeholder"
            :shortcuts="['datePickerRange', 'dateTimePickerRange'].includes(formColumnItem.domType) ? shortcuts : undefined"
            clearable>
        </el-date-picker>

        <div v-for="(imageItem, imageIndex) in (
            (formColumnItem.domType === 'uploadImage'
                ? [formModel[formColumnItem.prop]]
                : formColumnItem.domType === 'uploadImageList'
                    ? formModel[formColumnItem.prop]
                    : []) || []
        ).filter((formImageItem) => formImageItem)" :key="'image_' + imageIndex" class="upload-image-list">
            <img :src="imageItem" class="image" />

            <span class="upload-operation">
                <span @click.stop @click="
                    handlePictureCardPreview(
                        formColumnItem.prop,
                        imageIndex,
                        formColumnItem.domType
                    )
                    " :style="{ cursor: 'pointer' }">
                    <el-icon>
                        <ZoomIn />
                    </el-icon>
                </span>
                <span @click.stop @click="
                    handleDownload(
                        formColumnItem.prop,
                        imageIndex,
                        formColumnItem.domType
                    )
                    " :style="{ cursor: 'pointer' }">
                    <el-icon>
                        <Download />
                    </el-icon>
                </span>
                <span @click.stop @click="
                    handleRemove(
                        formColumnItem.prop,
                        imageIndex,
                        formColumnItem.domType
                    )
                    " :style="{ cursor: 'pointer' }">
                    <el-icon>
                        <Delete />
                    </el-icon>
                </span>
            </span>
        </div>
        <el-upload v-if="
            (formColumnItem.domType === 'uploadImage' &&
                !formModel[formColumnItem.prop]) ||
            formColumnItem.domType === 'uploadImageList'
        " v-bind="formColumnItem.componentProps || {}" v-loading="uploadImageLoading[formColumnItem.prop]"
            class="image-uploader" :show-file-list="false" :http-request="(options) => uploadServer(options, formColumnItem.prop)
                " accept="image/png,image/jpeg,image/jpg">
            <el-icon class="image-uploader-icon">
                <Plus />
            </el-icon>
        </el-upload>

        <el-upload v-if="formColumnItem.domType === 'uploadFile'" v-loading="uploadImageLoading[formColumnItem.prop]"
            v-bind="formColumnItem.componentProps || {}" v-model:file-list="uploadFile[formColumnItem.prop]"
            :show-file-list="true" :limit="1" :accept="formColumnItem.uploadAcceptMap
                ? formColumnItem.uploadAcceptMap.map[
                formModel[formColumnItem.uploadAcceptMap.key]
                ]
                : '*'
                " :http-request="(options) => uploadServer(options, formColumnItem.prop)
                    ">
            <template #trigger>
                <el-button type="primary">选择文件</el-button>
            </template>
        </el-upload>

        <PEditor v-model="formModel[formColumnItem.prop]" v-if="formColumnItem.domType === 'editor'"
            :upload-path="formColumnItem.uploadPath" />
    </el-form-item>

    <PImageView :image-url="dialogImageUrl" v-model:image-visible="dialogVisible"></PImageView>
</template>

<script lang="ts" setup>
import { ref, inject, watch } from 'vue';
import {
    FormColumnsInterface,
    ProvideFormInterface
} from '@/common/provideForm';
import { Plus, Download, Delete, ZoomIn } from '@icon-park/vue-next';

import cos from '@/common/cos.ts';
import { common } from '@/api';

/**
 * 组件属性定义
 * @property {FormColumnsInterface} formColumns - 表单列配置数组,定义表单的每一列的配置信息
 * @property {Object} formModel - 表单数据模型,用于双向绑定表单数据
 */
const props = defineProps<{
    formColumnItem: FormColumnsInterface,
    formModel: Object,
}>();

const dialogImageUrl = ref('');
const dialogVisible = ref(false);
const uploadImageLoading = ref({});

const uploadFile = ref({});

const provideForm: ProvideFormInterface = inject('provideForm');
const { domDataSet, getDomData, domDataSetLoading } = provideForm;

if (props.formColumnItem.domType === 'uploadFile') {
    watch(
        () => props.formModel[props.formColumnItem.prop],
        (value) => {
            if (value) {
                const uploadFileRef = uploadFile.value;
                uploadFileRef[props.formColumnItem.prop] = [
                    {
                        name: value.split('/').pop(),
                        url: value
                    }
                ];
                uploadFile.value = uploadFileRef;
            } else {
                uploadFile.value = {};
            }
        }
    );
}

/**
 * 上传到cos
 * @param options 操作对象
 * @param prop form属性
 * @param domType dom类型 uploadImage-对象 uploadImageList-数组
 */
const uploadCos = async (options, prop, domType, path) => {
    uploadImageLoading.value[prop] = true;

    const result = await cos.upload(options.file, path);
    if (result) {
        if (domType === 'uploadImageList') {
            // eslint-disable-next-line vue/no-mutating-props
            props.formModel[prop] = [...(props.formModel[prop] || []), result];
        } else {
            // eslint-disable-next-line vue/no-mutating-props
            props.formModel[prop] = result;
        }
    }

    uploadImageLoading.value[prop] = false;
};

/**
 * 上传到server
 * @param options 操作对象
 * @param prop form属性
 * @param domType dom类型 uploadImage-对象 uploadImageList-数组
 */
const uploadServer = async (options, prop) => {
    uploadImageLoading.value[prop] = true;

    const result = await common.uploadToServer(options.file);
    if (result) {
        // eslint-disable-next-line vue/no-mutating-props
        props.formModel[prop] = result[0];
    }

    uploadImageLoading.value[prop] = false;
};

const handlePictureCardPreview = (prop, index, domType) => {
    if (domType === 'uploadImageList') {
        dialogImageUrl.value = props.formModel[prop][index];
    } else {
        dialogImageUrl.value = props.formModel[prop];
    }
    dialogVisible.value = true;
};

const handleRemove = (prop, index, domType) => {
    if (domType === 'uploadImageList') {
        const newData = props.formModel[prop];
        newData.splice(index, 1);

        // eslint-disable-next-line vue/no-mutating-props
        props.formModel[prop] = newData;
    } else {
        // eslint-disable-next-line vue/no-mutating-props
        props.formModel[prop] = '';
    }
};

const handleDownload = (prop, index, domType) => {
    let fileUrl;
    if (domType === 'uploadImageList') {
        fileUrl = props.formModel[prop][index];
    } else {
        fileUrl = props.formModel[prop];
    }

    const fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);

    const a = document.createElement('a');
    a.href = fileUrl;
    a.download = fileName;
    a.click();
};

const shortcuts = [
    {
        text: '今天',
        value: () => {
            const end = new Date()
            const start = new Date()
            return [start, end]
        }
    },
    {
        text: '最近24小时',
        value: () => {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24)
            return [start, end]
        }
    },
    {
        text: '昨天',
        value: () => {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24)
            end.setTime(end.getTime() - 3600 * 1000 * 24)
            return [start, end]
        }
    },
    {
        text: '最近一周',
        value: () => {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            return [start, end]
        }
    },
    {
        text: '最近一月',
        value: () => {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
            return [start, end]
        }
    },
    {
        text: '最近三月',
        value: () => {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
            return [start, end]
        }
    }
]
</script>

<style lang="scss" scoped>
.upload-image-list {
    position: relative;
    margin: 0px 16px 16px 0px;
    border: 1px dashed var(--el-border-color);
    border-radius: 6px;

    :deep(.image) {
        border-radius: 6px;
        width: 178px;
        max-height: 369px;
        display: block;

        &:hover {
            border-color: var(--el-color-primary);
        }
    }

    .upload-operation {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        border-radius: 6px;
        display: flex;
        align-items: center;
        justify-content: space-evenly;
        color: #fff;
        opacity: 0;
        font-size: 20px;

        &:hover {
            background: rgba(0, 0, 0, 0.5);
            opacity: 1;
            transition: opacity 0.3s;
        }
    }
}

.image-uploader {
    :deep(.el-upload) {
        border: 1px dashed var(--el-border-color);
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
        transition: var(--el-transition-duration-fast);
    }

    :deep(.el-upload:hover) {
        border-color: var(--el-color-primary);
    }
}

.el-icon.image-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    text-align: center;
}
</style>

<style lang="scss">
.upload-image-preview {
    max-width: 90%;

    .el-dialog__header {
        display: flex;
    }

    .el-dialog__body {
        padding: 8px 0px 0px 0px;
        display: flex;
    }

    img {
        width: 100% !important;
    }
}
</style>

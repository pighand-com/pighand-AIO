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

            <el-form-item
                :style="{ width: '100%' }"
                v-show="!item.hidden"
                :label="item.label"
                :prop="item.prop">
                <el-input
                    v-if="!item.domType || item.domType === 'input'"
                    v-bind="item.componentProps || {}"
                    v-model="formModel[item.prop]"
                    :type="item.inputType || 'text'"
                    :show-password="item.inputType === 'password'"
                    :placeholder="item[placeholder] || item.placeholder"
                    autocomplete="off"
                    clearable />

                <el-input-number
                    v-if="item.domType === 'number'"
                    v-bind="item.componentProps || {}"
                    v-model="formModel[item.prop]"
                    clearable />

                <el-select
                    v-if="item.domType === 'select'"
                    v-bind="item.componentProps || {}"
                    v-model="formModel[item.prop]"
                    :placeholder="
                        item[placeholder] ||
                        item.placeholder ||
                        onWhere === 'search'
                            ? '全部'
                            : ''
                    "
                    :remote-method="getDomData[item.label + item.prop]"
                    :remote="!!getDomData[item.label + item.prop]"
                    :loading="domDataSetLoading[item.label + item.prop]"
                    remote-show-suffix
                    filterable
                    clearable>
                    <el-option
                        v-for="domDataItem in domDataSet[item.prop]"
                        :key="domDataItem.value"
                        :label="domDataItem.label"
                        :value="domDataItem.value" />
                </el-select>

                <el-date-picker
                    v-if="item.domType === 'dateTimePicker'"
                    v-bind="item.componentProps || {}"
                    v-model="formModel[item.prop]"
                    type="datetime"
                    :placeholder="
                        item[placeholder] ||
                        item.placeholder ||
                        onWhere === 'search'
                            ? '全部'
                            : ''
                    "
                    clearable>
                </el-date-picker>

                <el-date-picker
                    v-if="
                        item.domType === 'datePicker' ||
                        item.domType === 'datePickerRange'
                    "
                    v-bind="item.componentProps || {}"
                    v-model="formModel[item.prop]"
                    :type="item.domType === 'datePicker' ? 'date' : 'daterange'"
                    range-separator="-"
                    value-format="YYYY-MM-DD"
                    :placeholder="
                        item[placeholder] ||
                        item.placeholder ||
                        onWhere === 'search'
                            ? '全部'
                            : ''
                    "
                    clearable>
                </el-date-picker>

                <div
                    v-for="(imageItem, imageIndex) in (
                        (item.domType === 'uploadImage'
                            ? [formModel[item.prop]]
                            : item.domType === 'uploadImageList'
                            ? formModel[item.prop]
                            : []) || []
                    ).filter((formImageItem) => formImageItem)"
                    :key="'image_' + imageIndex"
                    class="upload-image-list">
                    <img :src="imageItem" class="image" />

                    <span class="upload-operation">
                        <span
                            @click.stop
                            @click="
                                handlePictureCardPreview(
                                    item.prop,
                                    imageIndex,
                                    item.domType
                                )
                            "
                            :style="{ cursor: 'pointer' }">
                            <el-icon><ZoomIn /></el-icon>
                        </span>
                        <span
                            @click.stop
                            @click="
                                handleDownload(
                                    item.prop,
                                    imageIndex,
                                    item.domType
                                )
                            "
                            :style="{ cursor: 'pointer' }">
                            <el-icon><Download /></el-icon>
                        </span>
                        <span
                            @click.stop
                            @click="
                                handleRemove(
                                    item.prop,
                                    imageIndex,
                                    item.domType
                                )
                            "
                            :style="{ cursor: 'pointer' }">
                            <el-icon><Delete /></el-icon>
                        </span>
                    </span>
                </div>
                <el-upload
                    v-if="
                        (item.domType === 'uploadImage' &&
                            !formModel[item.prop]) ||
                        item.domType === 'uploadImageList'
                    "
                    v-bind="item.componentProps || {}"
                    v-loading="uploadImageLoading[item.prop]"
                    class="image-uploader"
                    :show-file-list="false"
                    :http-request="
                        (options) => uploadServer(options, item.prop)
                    "
                    accept="image/png,image/jpeg,image/jpg">
                    <el-icon class="image-uploader-icon"><Plus /></el-icon>
                </el-upload>

                <el-upload
                    v-if="item.domType === 'uploadFile'"
                    v-loading="uploadImageLoading[item.prop]"
                    v-bind="item.componentProps || {}"
                    v-model:file-list="uploadFile[item.prop]"
                    :show-file-list="true"
                    :limit="1"
                    :accept="
                        item.uploadAcceptMap
                            ? item.uploadAcceptMap.map[
                                  formModel[item.uploadAcceptMap.key]
                              ]
                            : '*'
                    "
                    :http-request="
                        (options) => uploadServer(options, item.prop)
                    ">
                    <template #trigger>
                        <el-button type="primary">选择文件</el-button>
                    </template>
                </el-upload>

                <PEditor
                    v-model="formModel[item.prop]"
                    v-if="item.domType === 'editor'"
                    :upload-path="item.uploadPath" />
            </el-form-item>

            <slot :name="`${onWhere}-${index}-after`" />
        </el-col>
    </el-row>

    <span v-else v-for="(item, index) in props.formColumns" :key="index">
        <slot :name="`${onWhere}-${index}-before`" />

        <el-form-item
            v-show="!item.hidden"
            :label="item.label"
            :prop="item.prop">
            <el-input
                v-if="!item.domType || item.domType === 'input'"
                v-bind="item.componentProps || {}"
                v-model="formModel[item.prop]"
                :type="item.inputType || 'text'"
                :show-password="item.inputType === 'password'"
                :placeholder="item[placeholder] || item.placeholder"
                autocomplete="off"
                clearable />

            <el-input-number
                v-if="item.domType === 'number'"
                v-bind="item.componentProps || {}"
                v-model="formModel[item.prop]"
                clearable />

            <el-select
                v-if="item.domType === 'select'"
                v-bind="item.componentProps || {}"
                v-model="formModel[item.prop]"
                :placeholder="
                    item[placeholder] ||
                    item.placeholder ||
                    onWhere === 'search'
                        ? '全部'
                        : ''
                "
                :remote-method="getDomData[item.label + item.prop]"
                :remote="!!getDomData[item.label + item.prop]"
                :loading="domDataSetLoading[item.label + item.prop]"
                remote-show-suffix
                filterable
                clearable>
                <el-option
                    v-for="domDataItem in domDataSet[item.prop]"
                    :key="domDataItem.value"
                    :label="domDataItem.label"
                    :value="domDataItem.value" />
            </el-select>

            <el-date-picker
                v-if="item.domType === 'dateTimePicker'"
                v-bind="item.componentProps || {}"
                v-model="formModel[item.prop]"
                type="datetime"
                :placeholder="
                    item[placeholder] ||
                    item.placeholder ||
                    onWhere === 'search'
                        ? '全部'
                        : ''
                "
                clearable>
            </el-date-picker>

            <el-date-picker
                v-if="
                    item.domType === 'datePicker' ||
                    item.domType === 'datePickerRange'
                "
                v-bind="item.componentProps || {}"
                v-model="formModel[item.prop]"
                :type="item.domType === 'datePicker' ? 'date' : 'daterange'"
                range-separator="-"
                value-format="YYYY-MM-DD"
                :placeholder="
                    item[placeholder] ||
                    item.placeholder ||
                    onWhere === 'search'
                        ? '全部'
                        : ''
                "
                clearable>
            </el-date-picker>

            <div
                v-for="(imageItem, imageIndex) in (
                    (item.domType === 'uploadImage'
                        ? [formModel[item.prop]]
                        : item.domType === 'uploadImageList'
                        ? formModel[item.prop]
                        : []) || []
                ).filter((formImageItem) => formImageItem)"
                :key="'image_' + imageIndex"
                class="upload-image-list">
                <img :src="imageItem" class="image" />

                <span class="upload-operation">
                    <span
                        @click.stop
                        @click="
                            handlePictureCardPreview(
                                item.prop,
                                imageIndex,
                                item.domType
                            )
                        "
                        :style="{ cursor: 'pointer' }">
                        <el-icon><ZoomIn /></el-icon>
                    </span>
                    <span
                        @click.stop
                        @click="
                            handleDownload(item.prop, imageIndex, item.domType)
                        "
                        :style="{ cursor: 'pointer' }">
                        <el-icon><Download /></el-icon>
                    </span>
                    <span
                        @click.stop
                        @click="
                            handleRemove(item.prop, imageIndex, item.domType)
                        "
                        :style="{ cursor: 'pointer' }">
                        <el-icon><Delete /></el-icon>
                    </span>
                </span>
            </div>
            <el-upload
                v-if="
                    (item.domType === 'uploadImage' && !formModel[item.prop]) ||
                    item.domType === 'uploadImageList'
                "
                v-bind="item.componentProps || {}"
                v-loading="uploadImageLoading[item.prop]"
                class="image-uploader"
                :show-file-list="false"
                :http-request="(options) => uploadServer(options, item.prop)"
                accept="image/png,image/jpeg,image/jpg">
                <el-icon class="image-uploader-icon"><Plus /></el-icon>
            </el-upload>

            <el-upload
                v-if="item.domType === 'uploadFile'"
                v-loading="uploadImageLoading[item.prop]"
                v-bind="item.componentProps || {}"
                v-model:file-list="uploadFile[item.prop]"
                :show-file-list="true"
                :limit="1"
                :accept="
                    item.uploadAcceptMap
                        ? item.uploadAcceptMap.map[
                              formModel[item.uploadAcceptMap.key]
                          ]
                        : '*'
                "
                :http-request="(options) => uploadServer(options, item.prop)">
                <template #trigger>
                    <el-button type="primary">选择文件</el-button>
                </template>
            </el-upload>

            <PEditor
                v-model="formModel[item.prop]"
                v-if="item.domType === 'editor'"
                :upload-path="item.uploadPath" />
        </el-form-item>

        <slot :name="`${onWhere}-${index}-after`" />
    </span>

    <PImageView
        :image-url="dialogImageUrl"
        v-model:image-visible="dialogVisible"></PImageView>
</template>

<script lang="ts" setup>
import { ref, inject, watch } from 'vue';
import {
    FormColumnsInterface,
    ProvideFormInterface
} from '@/common/provideForm';
import { Plus, Download, Delete, ZoomIn } from '@element-plus/icons-vue';

import cos from '@/common/cos.ts';
import { common } from '@/api/index.ts';

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

const dialogImageUrl = ref('');
const dialogVisible = ref(false);
const uploadImageLoading = ref({});

const uploadFile = ref({});

const provideForm: ProvideFormInterface = inject('provideForm');
const { domDataSet, getDomData, domDataSetLoading } = provideForm;

let placeholder = 'placeholder';
if (props.onWhere === 'search') {
    placeholder = 'searchPlaceholder';
} else {
    placeholder = 'detailPlaceholder';
}

props.formColumns.forEach((item) => {
    if (item.domType === 'uploadFile') {
        watch(
            () => props.formModel[item.prop],
            (value) => {
                if (value) {
                    const uploadFileRef = uploadFile.value;
                    uploadFileRef[item.prop] = [
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
});

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

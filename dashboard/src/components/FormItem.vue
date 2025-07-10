<!-- eslint-disable vue/no-mutating-props -->
<template>
    <PDivider :config="formColumnItem.divider" />

    <el-form-item
        :style="{ width: '100%' }"
        v-show="!formColumnItem.hidden"
        :label="formColumnItem.label"
        :prop="formColumnItem.prop">
        <el-input
            v-if="!formColumnItem.domType || formColumnItem.domType === 'input'"
            v-bind="formColumnItem.componentProps || {}"
            v-model="formModel[formColumnItem.prop]"
            :type="formColumnItem.inputType || 'text'"
            :show-password="formColumnItem.inputType === 'password'"
            :placeholder="formColumnItem.placeholder"
            autocomplete="off"
            clearable />

        <el-input-number
            v-if="formColumnItem.domType === 'number'"
            v-bind="formColumnItem.componentProps || {}"
            v-model="formModel[formColumnItem.prop]"
            clearable />

        <el-select
            v-if="formColumnItem.domType === 'select'"
            v-bind="formColumnItem.componentProps || {}"
            v-model="formModel[formColumnItem.prop]"
            :placeholder="formColumnItem.placeholder"
            :remote-method="
                getDomData[formColumnItem.label + formColumnItem.prop]
            "
            :remote="!!getDomData[formColumnItem.label + formColumnItem.prop]"
            :loading="
                domDataSetLoading[formColumnItem.label + formColumnItem.prop]
            "
            :filter-method="
                getDomData[formColumnItem.label + formColumnItem.prop]
                    ? undefined
                    : (_query) => {}
            "
            @focus="
                () => {
                    // 获得焦点时加载初始数据
                    if (
                        getDomData[
                            formColumnItem.label + formColumnItem.prop
                        ] &&
                        (!domDataSet[formColumnItem.prop] ||
                            domDataSet[formColumnItem.prop].length === 0)
                    ) {
                        getDomData[formColumnItem.label + formColumnItem.prop](
                            ''
                        );
                    }
                }
            "
            @change="
                (value) => {
                    // 执行用户自定义的change回调
                    if (formColumnItem.onDetailChange) {
                        formColumnItem.onDetailChange(value, formModel);
                    }
                }
            "
            @clear="
                () => {
                    if (
                        clearDomDataCache[
                            formColumnItem.label + formColumnItem.prop
                        ]
                    ) {
                        clearDomDataCache[
                            formColumnItem.label + formColumnItem.prop
                        ]();
                        // 清除后重新加载初始数据
                        getDomData[formColumnItem.label + formColumnItem.prop](
                            ''
                        );
                    }
                }
            "
            @blur="
                () => {
                    // 失去焦点时清空搜索结果，重新加载初始数据
                    if (
                        clearDomDataCache[
                            formColumnItem.label + formColumnItem.prop
                        ]
                    ) {
                        clearDomDataCache[
                            formColumnItem.label + formColumnItem.prop
                        ]();
                        getDomData[formColumnItem.label + formColumnItem.prop](
                            ''
                        );
                    }
                }
            "
            remote-show-suffix
            filterable
            clearable>
            <el-option
                v-for="domDataItem in domDataSet[formColumnItem.prop]"
                :key="domDataItem.value"
                :label="domDataItem.label"
                :value="domDataItem.value" />
        </el-select>

        <el-radio-group
            v-if="formColumnItem.domType === 'radio'"
            v-bind="formColumnItem.componentProps || {}"
            v-model="formModel[formColumnItem.prop]">
            <el-radio
                v-for="domDataItem in domDataSet[formColumnItem.prop]"
                :key="domDataItem.value"
                :label="domDataItem.label"
                :value="domDataItem.value">
                {{ domDataItem.label }}
            </el-radio>
        </el-radio-group>

        <el-date-picker
            v-if="
                [
                    'datePicker',
                    'datePickerRange',
                    'dateTimePickerRange',
                    'dateTimePicker'
                ].includes(formColumnItem.domType)
            "
            v-bind="formColumnItem.componentProps || {}"
            v-model="formModel[formColumnItem.prop]"
            :type="
                formColumnItem.domType === 'datePicker'
                    ? 'date'
                    : formColumnItem.domType === 'datePickerRange'
                    ? 'daterange'
                    : formColumnItem.domType === 'dateTimePicker'
                    ? 'datetime'
                    : 'datetimerange'
            "
            range-separator="-"
            :value-format="
                formColumnItem.domType.startsWith('dateTime')
                    ? 'YYYY-MM-DD HH:mm:ss'
                    : 'YYYY-MM-DD'
            "
            :placeholder="formColumnItem.placeholder"
            :shortcuts="
                ['datePickerRange', 'dateTimePickerRange'].includes(
                    formColumnItem.domType
                )
                    ? shortcuts
                    : undefined
            "
            clearable>
        </el-date-picker>

        <div
            v-for="(imageItem, imageIndex) in (
                (formColumnItem.domType === 'uploadImage'
                    ? [formModel[formColumnItem.prop]]
                    : formColumnItem.domType === 'uploadImageList'
                    ? formModel[formColumnItem.prop]
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
                            formColumnItem.prop,
                            imageIndex,
                            formColumnItem.domType
                        )
                    "
                    :style="{ cursor: 'pointer' }">
                    <el-icon>
                        <ZoomIn />
                    </el-icon>
                </span>
                <span
                    @click.stop
                    @click="
                        handleDownload(
                            formColumnItem.prop,
                            imageIndex,
                            formColumnItem.domType
                        )
                    "
                    :style="{ cursor: 'pointer' }">
                    <el-icon>
                        <Download />
                    </el-icon>
                </span>
                <span
                    @click.stop
                    @click="
                        handleRemove(
                            formColumnItem.prop,
                            imageIndex,
                            formColumnItem.domType
                        )
                    "
                    :style="{ cursor: 'pointer' }">
                    <el-icon>
                        <Delete />
                    </el-icon>
                </span>
            </span>
        </div>

        <el-upload
            v-if="
                (formColumnItem.domType === 'uploadImage' &&
                    !formModel[formColumnItem.prop]) ||
                formColumnItem.domType === 'uploadImageList'
            "
            v-bind="formColumnItem.componentProps || {}"
            v-loading="uploadImageLoading[formColumnItem.prop]"
            class="image-uploader"
            :show-file-list="false"
            :http-request="
                (options) =>
                    upload(
                        options,
                        formColumnItem.prop,
                        formColumnItem.domType,
                        formColumnItem.uploadPath
                    )
            "
            accept="image/png,image/jpeg,image/jpg">
            <el-icon class="image-uploader-icon">
                <Plus />
            </el-icon>
        </el-upload>

        <el-upload
            v-if="
                ['uploadFile', 'uploadFileList'].includes(
                    formColumnItem.domType
                )
            "
            v-loading="uploadImageLoading[formColumnItem.prop]"
            v-bind="formColumnItem.componentProps || {}"
            v-model:file-list="uploadFile[formColumnItem.prop]"
            :show-file-list="false"
            :limit="formColumnItem.domType === 'uploadFileList' ? 0 : 1"
            :drag="formColumnItem.domType === 'uploadFileList'"
            :multiple="formColumnItem.domType === 'uploadFileList'"
            :accept="
                formColumnItem.uploadAcceptMap
                    ? formColumnItem.uploadAcceptMap.map[
                          formModel[formColumnItem.uploadAcceptMap.key]
                      ]
                    : '*'
            "
            :http-request="
                (options) =>
                    upload(
                        options,
                        formColumnItem.prop,
                        formColumnItem.domType,
                        formColumnItem.uploadPath
                    )
            "
            class="w-full">
            <div v-if="formColumnItem.domType === 'uploadFileList'">
                <div class="el-icon--upload flex justify-center">
                    <UploadOne />
                </div>
                <div class="el-upload__text">
                    将文件拖到此处，或 <em>点击上传</em>
                </div>
            </div>
            <template v-if="formColumnItem.domType === 'uploadFile'" #trigger>
                <el-button type="primary">选择文件</el-button>
            </template>
            <template #tip>
                <template v-if="uploadFile[formColumnItem.prop]?.length">
                    <div
                        v-for="file in uploadFile[formColumnItem.prop]"
                        :key="file.uid"
                        class="flex justify-center align-center items-center mt-1">
                        <el-progress
                            :text-inside="true"
                            :stroke-width="20"
                            :percentage="
                                uploadProgress[
                                    formColumnItem.prop + '_' + file.uid
                                ]
                            "
                            :status="
                                uploadProgress[
                                    formColumnItem.prop + '_' + file.uid
                                ] === 100
                                    ? 'success'
                                    : ''
                            "
                            class="upload-progress w-full mr-1">
                            <span class="flex items-center">
                                <strong
                                    v-if="
                                        uploadProgress[
                                            formColumnItem.prop + '_' + file.uid
                                        ] < 100
                                    ">
                                    ({{
                                        uploadProgress[
                                            formColumnItem.prop + '_' + file.uid
                                        ]
                                    }}%)
                                </strong>
                                <span class="ml-1">{{ file.name }}</span>
                            </span>
                        </el-progress>
                        <DeleteThree
                            v-if="
                                uploadProgress[
                                    formColumnItem.prop + '_' + file.uid
                                ] === 100
                            "
                            class="cursor-pointer"
                            style="color: var(--p-color-danger)"
                            @click="
                                handleFileRemove(formColumnItem.prop, file)
                            " />
                    </div>
                </template>
            </template>
        </el-upload>

        <PEditor
            v-model="formModel[formColumnItem.prop]"
            v-if="formColumnItem.domType === 'editor'"
            :upload-path="formColumnItem.uploadPath" />

        <span
            v-if="formColumnItem.suffix && onWhere === 'detail'"
            class="form-item-suffix"
            >{{ formColumnItem.suffix }}</span
        >
    </el-form-item>

    <PImageView
        :image-url="dialogImageUrl"
        v-model:image-visible="dialogVisible"></PImageView>
</template>

<script lang="ts" setup>
import { ref, inject, watch, onUnmounted } from 'vue';
import {
    FormColumnsInterface,
    ProvideFormInterface
} from '@/common/provideForm';
import {
    Plus,
    Download,
    Delete,
    ZoomIn,
    UploadOne,
    DeleteThree
} from '@icon-park/vue-next';

import cos from '@/common/cos.ts';
import { common } from '@/api';
import { getApplicationInfo } from '@/common/storage';

/**
 * 组件属性定义
 * @property {FormColumnsInterface} formColumns - 表单列配置数组,定义表单的每一列的配置信息
 * @property {Object} formModel - 表单数据模型,用于双向绑定表单数据
 */
const props = defineProps<{
    formColumnItem: FormColumnsInterface;
    formModel: Object;
    onWhere: 'search' | 'detail';
}>();

const dialogImageUrl = ref('');
const dialogVisible = ref(false);
const uploadImageLoading = ref({});

// 上传文件
const uploadFile = ref({});
// 上传进度
const uploadProgress = ref({});

const provideForm: ProvideFormInterface = inject('provideForm');
const { domDataSet, getDomData, domDataSetLoading, clearDomDataCache } =
    provideForm;

// 监听 formModel 变化，当表单数据被清空时，清空上传相关状态
watch(
    () => props.formModel[props.formColumnItem.prop],
    (newValue) => {
        if (
            !newValue &&
            ['uploadFile', 'uploadFileList'].includes(
                props.formColumnItem.domType
            )
        ) {
            // 清空上传文件列表
            uploadFile.value[props.formColumnItem.prop] = [];
            // 清空对应的上传进度
            Object.keys(uploadProgress.value).forEach((key) => {
                if (key.startsWith(props.formColumnItem.prop + '_')) {
                    delete uploadProgress.value[key];
                }
            });
        }
    }
);

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
 * 上传
 * @param options 操作对象
 * @param prop form属性
 * @param domType dom类型 uploadImage-对象 uploadImageList-数组
 */
const upload = async (options, prop, domType, path) => {
    uploadImageLoading.value[prop] = true;

    const applicationInfo = getApplicationInfo();

    let fileUrls = [];
    if (applicationInfo.uploadType === 'system') {
        // 上传到server
        fileUrls = await common.uploadToServer(
            options.file,
            (progress: number) => {
                uploadProgress.value[prop + '_' + options.file.uid] = progress;
            }
        );
    } else {
        // 上传到云服务
        // 获取上传url
        const uploadUrlInfo = await common.getUploadUrls([
            {
                extension: options.file.type.split('/')[1],
                path: path
            }
        ]);

        // 上传失败
        if (!uploadUrlInfo || uploadUrlInfo.urls.length === 0) {
            uploadImageLoading.value[prop] = false;
            // 删除上传文件列表中的文件
            if (uploadFile.value[prop]) {
                uploadFile.value[prop] = uploadFile.value[prop].filter(
                    (file) => file.uid !== options.file.uid
                );
            }
            return;
        }

        for (const uploadUrl of uploadUrlInfo.urls) {
            switch (applicationInfo.uploadType) {
                case 'tencent_cloud_cos':
                    await cos.upload(
                        options.file,
                        uploadUrl.uploadUrl,
                        uploadUrlInfo.headers,
                        (progress: number) => {
                            uploadProgress.value[
                                prop + '_' + options.file.uid
                            ] = progress;
                        }
                    );

                    fileUrls.push(uploadUrl.url);
                    break;
                default:
                    ElMessage.error('暂不支持当前上传类型');
                    break;
            }
        }
    }

    if (['uploadImageList', 'uploadFileList'].includes(domType)) {
        // eslint-disable-next-line vue/no-mutating-props
        props.formModel[prop] = [...(props.formModel[prop] || []), ...fileUrls];
    } else {
        // eslint-disable-next-line vue/no-mutating-props
        props.formModel[prop] = fileUrls[0];
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

const handleFileRemove = (prop, file) => {
    if (uploadFile.value[prop]) {
        uploadFile.value[prop] = uploadFile.value[prop].filter(
            (item) => item.uid !== file.uid
        );
        // 同时清除对应的进度记录
        delete uploadProgress.value[prop + '_' + file.uid];
    }
};

const shortcuts = [
    {
        text: '今天',
        value: () => {
            const end = new Date();
            const start = new Date();
            return [start, end];
        }
    },
    {
        text: '最近24小时',
        value: () => {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24);
            return [start, end];
        }
    },
    {
        text: '昨天',
        value: () => {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24);
            end.setTime(end.getTime() - 3600 * 1000 * 24);
            return [start, end];
        }
    },
    {
        text: '最近一周',
        value: () => {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            return [start, end];
        }
    },
    {
        text: '最近一月',
        value: () => {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            return [start, end];
        }
    },
    {
        text: '最近三月',
        value: () => {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            return [start, end];
        }
    }
];
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

.upload-progress {
    :deep(.el-progress-bar__inner) {
        text-align: left;
    }
}

.form-item-suffix {
    padding-left: 12px;
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

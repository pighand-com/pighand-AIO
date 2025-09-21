<template>
    <el-dialog
        class="upload-image-preview"
        :model-value="imageVisible"
        :before-close="handleClose">
        <template #header>
            <el-tooltip
                :show-after="1000"
                content="复制"
                placement="bottom"
                effect="light">
                <span>
                    <el-button
                        link
                        color="#409eff"
                        dark
                        :icon="Copy"
                        @click="handleCopyUrl(imageUrl)"></el-button>
                </span> </el-tooltip
            >&nbsp;
            <el-text size="small" truncated>{{ imageUrl }}</el-text>
        </template>
        <img w-full :src="imageUrl" alt="Preview Image" />
    </el-dialog>
</template>

<script lang="ts" setup>
import { Copy } from '@icon-park/vue-next';

/**
 * 组件属性定义
 * @property {String} imageUrl - 图片URL地址
 * @property {Boolean} imageVisible - 是否显示图片预览对话框,默认为false
 */
defineProps({
    imageUrl: String,
    imageVisible: {
        type: Boolean,
        default: false
    }
});

const emits = defineEmits(['update:imageVisible']);

const handleCopyUrl = (url) => {
    navigator.clipboard.writeText(url);
};

const handleClose = () => {
    emits('update:imageVisible', false);
};
</script>

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

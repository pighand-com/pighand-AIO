<template>
    <el-divider v-if="config" :style="{ marginTop: '48px' }" :border-style="dividerStyle" :content-position="dividerContentPosition">
        <div style="display: flex; align-items: center;">
            <template v-if="dividerIcon">
                <component :is="dividerIcon" style="width: 16px; margin-right: 4px;"></component>
            </template>
            {{ dividerContent }}
        </div>
    </el-divider>
</template>

<script lang="ts" setup>
import { DividerConfigType } from '@/common/provideForm';
const props = defineProps<{
    config: DividerConfigType
}>();


const divider = props.config;
let dividerStyle = 'double';
let dividerContentPosition: "right" | "center" | "left"  = 'left';
let dividerContent = '';
let dividerIcon: any = null;
if(divider) {
    dividerStyle = typeof divider === 'object' && divider.style || dividerStyle;
    dividerContentPosition = typeof divider === 'object' && divider.contentPosition || dividerContentPosition;
    dividerContent = typeof divider === 'object' ? divider.content : typeof divider === 'string' ? divider : dividerContent;
    dividerIcon = typeof divider === 'object' ? divider.icon : null;
}
</script>
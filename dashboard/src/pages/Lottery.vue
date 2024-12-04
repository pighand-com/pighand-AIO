<template>
    <PDataManager
        :handle-query="lottery.query"
        :handle-find="lottery.find"
        :handle-delete="lottery.del"
        :handle-create="lottery.create"
        :handle-update="lottery.update"
        :drawer-props="{
            size: '60%'
        }">
        <template #[`detail-last`]>
            <!-- 新增助力增加类型单选按钮 -->
            <el-form-item label="助力增加类型" prop="helpAddType">
                <el-radio-group v-model="detailFormModel.helpAddType">
                    <el-radio :value="'times'">按次数</el-radio>
                    <el-radio :value="'config'">按配置</el-radio>
                </el-radio-group>
            </el-form-item>

            <!-- 按次数显示输入框 -->
            <template v-if="detailFormModel.helpAddType === 'times'">
                <el-form-item label="每次助力增加次数" prop="helpAddByTimes">
                    <el-input-number v-model="detailFormModel.helpAddByTimes" :min="1" />
                </el-form-item>
            </template>

            <!-- 按配置显示输入框 -->
            <template v-if="detailFormModel.helpAddType === 'config'">
                <el-form-item label="助力增加配置">
                    <div v-for="(config, index) in detailFormModel.helpConfigs" :key="index" class="help-config-item">
                        <span>到达</span>
                        <el-input-number 
                            v-model="config.reachPeople" 
                            :min="1" 
                            :controls="false" />
                        <span>人增加</span>
                        <el-input-number 
                            v-model="config.addTimes" 
                            :min="1" 
                            :controls="false" />
                        <span>次</span>
                        <el-button v-if="index === detailFormModel.helpConfigs.length - 1" 
                            type="primary" 
                            circle
                            size="small"
                            @click="addHelpConfig">
                            <el-icon><Plus /></el-icon>
                        </el-button>
                        <el-button v-if="detailFormModel.helpConfigs.length > 1" 
                            type="danger" 
                            circle
                            size="small"
                            @click="removeHelpConfig(index)">
                            <el-icon><Minus /></el-icon>
                        </el-button>
                    </div>
                </el-form-item>
            </template>

            <PDivider :config="{ content: '奖品配置', icon: Lollipop }" />

            <!-- 当抽奖类型为 participate 时显示额外字段 -->
            <template v-if="detailFormModel.lotteryType === 'participate'">
                <el-form-item label="奖品名称" prop="prizeName">
                    <el-input v-model="detailFormModel.prizeName" />
                </el-form-item>
                <el-form-item label="奖品图片" prop="prizeImageUrl">
                    <el-upload
                        v-model="detailFormModel.prizeImageUrl"
                        action="/upload"
                        accept="image/*" />
                </el-form-item>
                <!-- 其他 LotteryParticipatePrizeDomain 相关字段 -->
            </template>
        </template>
    </PDataManager>
</template>

<script lang="ts" setup>
import { Plus, Minus, Lollipop, Share, Help } from '@element-plus/icons-vue';
import { lottery } from '@/api/index.ts';
import provideForm from '@/common/provideForm';

const { detailFormModel, watchDetailForm } = provideForm([
    {
        label: 'id',
        prop: 'id',
        isDetail: true,
        isPrimaryKey: true,
        hidden: true
    },
    {
        label: '抽奖类型',
        prop: 'lotteryType',
        domType: 'radio',
        domData: [
            {
                label: '参与',
                value: 'participate'
            }
        ],
        isSearch: true,
        isTable: true,
        isDetail: true,
        detailDefault: 'participate'
    },
    {
        label: '开始时间',
        prop: 'beginTime',
        isTable: true,
        isDetail: true,
        domType: 'dateTimePicker'
    },
    {
        label: '结束时间',
        prop: 'endTime',
        isTable: true,
        isDetail: true,
        domType: 'dateTimePicker'
    },
    {
        label: '背景图片',
        prop: 'backgroundUrls',
        isDetail: true,
        domType: 'uploadImageList',
        uploadPath: 'lottery/background'
    },
    {
        label: '更多按钮图片',
        prop: 'moreButtonUrl',
        isDetail: true,
        domType: 'uploadImage',
        uploadPath: 'lottery/button',
    },
    {
        label: '更多信息外部链接',
        prop: 'moreExternalUrl',
        isTable: true,
        isDetail: true
    },
    {
        label: '分享标题',
        prop: 'shareTitle',
        isTable: true,
        isDetail: true,
        rules: [
            {
                max: 32,
                message: '最大长度32个字符',
                trigger: 'blur'
            }
        ],
        divider: {
            content: '分享配置',
            icon: Share
        }
    },
    {
        label: '分享图片',
        prop: 'shareImageUrl',
        isDetail: true,
        domType: 'uploadImage',
        uploadPath: 'lottery/share'
    },
    {
        label: '助力海报图',
        prop: 'helpPosterUrl',
        isDetail: true,
        domType: 'uploadImage',
        uploadPath: 'lottery/poster'
    },
    {
        label: '是否已通知',
        prop: 'showResult',
        isTable: true,
    },
    {
        label: '结果通知链接',
        prop: 'resultNotifyUrl',
        isTable: true,
    },
    {
        label: '最多被助力次数',
        prop: 'maxHelpMe',
        isTable: true,
        isDetail: true,
        domType: 'number',
        divider: {
            content: '助力配置',
            icon: Help
        }
    },
    {
        label: '最多助力他人次数',
        prop: 'maxHelpOther',
        isTable: true,
        isDetail: true,
        domType: 'number'
    },
], {
    detailDefaultValue: {
        helpAddType: 'times',
        helpAddByTimes: 1,
        helpConfigs: [{
            reachPeople: 1,
            addTimes: 1
        }]
    }
});

// 添加新方法
const addHelpConfig = () => {
    detailFormModel.helpConfigs.push({
        reachPeople: 1,
        addTimes: 1
    });
};

const removeHelpConfig = (index: number) => {
    detailFormModel.helpConfigs.splice(index, 1);
};

// 监听助力增加类型的变化
watchDetailForm((newVal, oldVal) => {
    // if (newVal.helpAddType === 'times') {
    //     detailFormModel.helpConfigs = [{
    //         reachPeople: 1,
    //         addTimes: 1
    //     }];
    // } else if (newVal.helpAddType === 'config') {
    //     detailFormModel.helpAddByTimes = 0;
    // }
});

</script>

<style scoped>
.help-config-item {
    display: flex;
    align-items: center;
    margin-bottom: 10px;
    width: 100%;
}
.help-config-item .el-button {
    margin-left: 10px;
}
.help-config-item :deep(.el-input-number) {
    width: 80px;
    margin: 0 4px;
}
</style>

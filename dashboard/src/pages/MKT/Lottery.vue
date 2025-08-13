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
        <template #table-column-operation="{ row }">
            <el-button size="small" type="info" @click="showParticipants(row)"
                >查看参与人数</el-button
            >
        </template>
        <template #[`detail-last`]>
            <!-- 新增助力增加类型单选按钮 -->
            <!-- <el-form-item label="助力增加类型" prop="helpAddType">
                <el-radio-group v-model="detailFormModel.helpAddType">
                    <el-radio :value="'times'">按次数</el-radio>
                    <el-radio :value="'config'">按配置</el-radio>
                </el-radio-group>
            </el-form-item> -->

            <!-- 按次数显示输入框 -->
            <!-- <template v-if="detailFormModel.helpAddType === 'times'">
                <el-form-item label="每次助力增加次数" prop="helpAddByTimes">
                    <el-input-number
                        v-model="detailFormModel.helpAddByTimes"
                        :min="1" />
                </el-form-item>
            </template> -->

            <!-- 按配置显示输入框 -->
            <template v-if="detailFormModel.helpAddType === 'config'">
                <el-form-item label="助力增加配置">
                    <div
                        v-for="(config, index) in detailFormModel.helpConfigs"
                        :key="index"
                        class="help-config-item">
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
                        <el-button
                            v-if="
                                index === detailFormModel.helpConfigs.length - 1
                            "
                            type="primary"
                            circle
                            size="small"
                            @click="addHelpConfig">
                            <el-icon><Plus /></el-icon>
                        </el-button>
                        <el-button
                            v-if="detailFormModel.helpConfigs.length > 1"
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
            <template v-if="detailFormModel.lotteryType === 10">
                <el-form-item prop="participate.id" hidden>
                    <el-input
                        v-model="detailFormModel.participate.id"
                        type="hidden" />
                </el-form-item>
                <el-form-item>
                    <div
                        v-for="(prize, index) in detailFormModel.participate
                            .prizes"
                        :key="index"
                        class="prize-config-item">
                        <el-form-item prop="participate.prizes.id" hidden>
                            <el-input v-model="prize.id" type="hidden" />
                        </el-form-item>
                        <span class="prize-label">{{
                            getPrizeLabel(index)
                        }}</span>
                        <el-input
                            v-model="prize.name"
                            placeholder="请输入奖品名称"
                            class="prize-name-input" />
                        <span>x</span>
                        <el-input-number
                            v-model="prize.lotteryQuota"
                            :min="1"
                            :controls="false"
                            class="prize-lotteryQuota-input" />
                        <span>个</span>
                        <div class="prize-button-container">
                            <el-button
                                v-if="
                                    index ===
                                    detailFormModel.participate.prizes.length -
                                        1
                                "
                                type="primary"
                                circle
                                size="small"
                                @click="addPrizeConfig">
                                <el-icon><Plus /></el-icon>
                            </el-button>
                            <el-button
                                v-if="
                                    detailFormModel.participate.prizes.length >
                                    1
                                "
                                type="danger"
                                circle
                                size="small"
                                @click="removePrizeConfig(index)">
                                <el-icon><Minus /></el-icon>
                            </el-button>
                        </div>
                    </div>
                </el-form-item>
            </template>
        </template>

        <template #detail-float>
            <!-- <div class="dy-mockup-phone">
                <div class="dy-camera"></div>
                <div class="dy-display">
                    <div class="dy-artboard dy-artboard-demo dy-phone-1">
                        Hi.
                    </div>
                </div>
            </div> -->
        </template>
    </PDataManager>

    <!-- 参与人数弹窗 -->
    <el-dialog
        v-model="participantsVisible"
        title="参与人数"
        :before-close="closeParticipantsDialog">
        <el-table :data="participantsData" border>
            <el-table-column prop="id" label="ID" />
            <el-table-column prop="user.phone" label="手机号" />
            <el-table-column prop="createdAt" label="参与时间">
                <template #default="{ row }">
                    {{ formatDateTime(row.createdAt) }}
                </template>
            </el-table-column>
            <el-table-column prop="isWinner" label="奖品">
                <template #default="{ row }">
                    <template v-if="row.drawStatus === 10">
                        <el-tag type="warning">未开奖</el-tag>
                    </template>
                    <template v-else>
                        <el-tag :type="row.prize ? 'success' : 'info'">
                            {{ row.prize ? row.prize.name : '未中奖' }}
                        </el-tag>
                    </template>
                </template>
            </el-table-column>
        </el-table>

        <!-- 分页 -->
        <div
            class="pagination-container"
            style="margin-top: 20px; text-align: center">
            <el-pagination
                v-model:current-page="currentPage"
                v-model:page-size="pageSize"
                :page-sizes="[10, 20, 50, 100]"
                :total="total"
                layout="total, sizes, prev, pager, next, jumper"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange" />
        </div>
    </el-dialog>
</template>

<script lang="ts" setup>
import {
    Plus,
    Minus,
    Lollipop,
    ShareOne,
    LoveAndHelp
} from '@icon-park/vue-next';
import { lottery } from '@/api/index.ts';
import provideForm from '@/common/provideForm';
import { ref, h } from 'vue';
import { ElMessage, ElTag } from 'element-plus';

// 开奖状态枚举
const drawStatusMap = {
    10: '未开奖',
    20: '开奖中',
    30: '开奖完成'
};

// 参与人数弹窗相关
const participantsVisible = ref(false);
const participantsData = ref([]);
const currentLotteryId = ref(null);
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);

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
            label: '抽奖类型',
            prop: 'lotteryType',
            domType: 'radio',
            domData: [
                {
                    label: '参与类型',
                    value: 10
                }
            ],
            isSearch: true,
            isTable: true,
            isDetail: true,
            detailDefault: 10
        },
        {
            label: '标题',
            prop: 'title',
            isTable: true,
            isDetail: true,
            rules: [
                {
                    max: 32,
                    message: '最大长度32个字符',
                    trigger: 'blur'
                },
                {
                    required: true,
                    message: '标题必填',
                    trigger: 'blur'
                }
            ]
        },
        {
            label: '封面图',
            prop: 'coverUrl',
            isDetail: true,
            domType: 'uploadImage',
            uploadPath: 'lottery/cover_'
        },
        // {
        //     label: '背景图片',
        //     prop: 'backgroundUrls',
        //     isDetail: true,
        //     domType: 'uploadImageList',
        //     uploadPath: 'lottery/bg_'
        // },
        {
            label: '开始时间',
            prop: 'beginTime',
            isTable: true,
            isDetail: true,
            domType: 'dateTimePicker',
            rules: [
                {
                    required: true,
                    message: '开始时间必填',
                    trigger: 'blur'
                }
            ]
        },
        {
            label: '结束时间',
            prop: 'endTime',
            isTable: true,
            isDetail: true,
            domType: 'dateTimePicker',
            rules: [
                {
                    required: true,
                    message: '结束时间必填',
                    trigger: 'blur'
                }
            ]
        },
        {
            label: '开奖时间',
            prop: 'drawTime',
            isTable: true,
            isDetail: true,
            domType: 'dateTimePicker',
            help: '未设置需要手动开奖'
        },
        {
            label: '开奖状态',
            prop: 'drawStatus',
            isTable: true,
            isSearch: true,
            domType: 'select',
            domData: Object.entries(drawStatusMap).map(([value, label]) => ({
                label,
                value: parseInt(value)
            })),
            tableFormat: (value) => {
                const status =
                    drawStatusMap[value as keyof typeof drawStatusMap];
                return h(
                    'span',
                    { style: 'display: flex; align-items: center; gap: 4px;' },
                    [status || '未知状态']
                );
            }
        },
        {
            label: '描述',
            prop: 'description',
            isDetail: true,
            domType: 'input',
            inputType: 'textarea',
            rules: [
                {
                    max: 500,
                    message: '描述最大长度为500',
                    trigger: 'blur'
                }
            ]
        }

        // {
        //     label: '更多按钮图片',
        //     prop: 'moreButtonUrl',
        //     isDetail: true,
        //     domType: 'uploadImage',
        //     uploadPath: 'lottery/button'
        // },
        // {
        //     label: '更多信息外部链接',
        //     prop: 'moreExternalUrl',
        //     isTable: true,
        //     isDetail: true
        // }
        // {
        //     label: '分享图片',
        //     prop: 'shareImageUrl',
        //     isDetail: true,
        //     domType: 'uploadImage',
        //     uploadPath: 'lottery/share'
        // divider: {
        //     content: '分享配置',
        //     icon: ShareOne
        // }
        // },
        // {
        //     label: '助力海报图',
        //     prop: 'helpPosterUrl',
        //     isDetail: true,
        //     domType: 'uploadImage',
        //     uploadPath: 'lottery/poster'
        // },
        // {
        //     label: '是否已通知',
        //     prop: 'showResult',
        //     isTable: true
        // },
        // {
        //     label: '结果通知链接',
        //     prop: 'resultNotifyUrl',
        //     isTable: true
        // }
        // {
        //     label: '最多被助力次数',
        //     prop: 'maxHelpMe',
        //     isTable: true,
        //     isDetail: true,
        //     domType: 'number',
        //     divider: {
        //         content: '助力配置',
        //         icon: LoveAndHelp
        //     }
        // },
        // {
        //     label: '最多助力他人次数',
        //     prop: 'maxHelpOther',
        //     isTable: true,
        //     isDetail: true,
        //     domType: 'number'
        // }
    ],
    {
        detailDefaultValue: {
            helpAddType: 'times',
            helpAddByTimes: 1,
            helpConfigs: [
                {
                    reachPeople: 1,
                    addTimes: 1
                }
            ],
            participate: {
                prizes: [
                    {
                        name: '',
                        lotteryQuota: 1
                    }
                ]
            }
        }
    }
);

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

// 奖品配置方法
const addPrizeConfig = () => {
    detailFormModel.participate.prizes.push({
        name: '',
        lotteryQuota: 1
    });
};

const removePrizeConfig = (index: number) => {
    detailFormModel.participate.prizes.splice(index, 1);
};

// 获取奖品等级标签
const getPrizeLabel = (index: number) => {
    const prizeLabels = [
        '一等奖',
        '二等奖',
        '三等奖',
        '四等奖',
        '五等奖',
        '六等奖',
        '七等奖',
        '八等奖',
        '九等奖',
        '十等奖'
    ];
    return prizeLabels[index] || `第${index + 1}等奖`;
};

// 显示参与人数
const showParticipants = async (row) => {
    currentLotteryId.value = row.id;
    currentPage.value = 1;
    await loadParticipants();
    participantsVisible.value = true;
};

// 加载参与人数数据
const loadParticipants = async () => {
    if (!currentLotteryId.value) return;

    try {
        const response = await lottery.getParticipants(
            currentLotteryId.value,
            pageSize.value,
            currentPage.value
        );

        // 根据实际返回的数据结构处理分页信息
        participantsData.value = response.records || response.data || [];

        // 处理分页信息，优先使用page对象中的数据
        if (response.page) {
            total.value = response.page.totalRow || 0;
            // 同步当前页码和页面大小
            currentPage.value = response.page.pageNumber || 1;
            pageSize.value = response.page.pageSize || 10;
        } else {
            // 兼容其他可能的返回格式
            total.value = response.total || response.count || 0;
        }
    } catch (error) {
        console.error('获取参与人数失败:', error);
        ElMessage.error('获取参与人数失败');
    }
};

// 关闭参与人数弹窗
const closeParticipantsDialog = () => {
    participantsVisible.value = false;
    participantsData.value = [];
    currentLotteryId.value = null;
    currentPage.value = 1;
    total.value = 0;
};

// 分页大小改变
const handleSizeChange = (newSize: number) => {
    pageSize.value = newSize;
    currentPage.value = 1;
    loadParticipants();
};

// 当前页改变
const handleCurrentChange = (newPage: number) => {
    currentPage.value = newPage;
    loadParticipants();
};

// 格式化日期时间
const formatDateTime = (dateTime: string) => {
    if (!dateTime) return '-';
    const date = new Date(dateTime);
    return date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
    });
};
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

.prize-config-item {
    display: flex;
    align-items: center;
    margin-bottom: 10px;
    width: 100%;
}
.prize-config-item .prize-name-input {
    width: 200px;
    margin: 0 8px;
    flex-shrink: 0;
}
.prize-config-item .prize-lotteryQuota-input {
    width: 80px;
    margin: 0 8px;
    flex-shrink: 0;
}
.prize-config-item .prize-button-container {
    width: 80px;
    display: flex;
    gap: 8px;
    margin-left: 8px;
}
.prize-config-item span {
    white-space: nowrap;
    margin: 0 4px;
}
.prize-config-item .prize-label {
    font-weight: bold;
    color: #409eff;
    min-width: 60px;
    text-align: center;
    margin-right: 8px;
}
</style>

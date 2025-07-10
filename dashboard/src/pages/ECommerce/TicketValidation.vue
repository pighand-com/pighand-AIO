<template>
    <div class="ticket-validation-page">
        <!-- 搜索区域 -->
        <div class="search-section">
            <el-card class="search-card">
                <div class="search-form">
                    <el-input
                        v-model="phoneNumber"
                        placeholder="请输入完整用户手机号"
                        class="phone-input"
                        clearable
                        @keyup.enter="searchUser" />
                    <el-button
                        type="primary"
                        @click="searchUser"
                        :loading="searchLoading">
                        搜索
                    </el-button>
                </div>
            </el-card>
        </div>

        <!-- 票务列表区域 -->
        <div v-if="ticketList.length > 0" class="ticket-list-section">
            <h3 class="section-title">用户票务列表</h3>
            <div class="ticket-grid">
                <el-card
                    v-for="ticket in ticketList"
                    :key="ticket.id"
                    class="ticket-card"
                    shadow="hover">
                    <div class="ticket-content">
                        <div class="ticket-header">
                            <h4 class="ticket-name">
                                {{ ticket.ticket.name }}
                            </h4>
                            <div class="remaining-count">
                                <span class="count-label">剩余核销次数：</span>
                                <span class="count-value">{{
                                    ticket.remainingValidationCount
                                }}</span>
                            </div>
                        </div>
                        <div class="ticket-actions">
                            <el-button
                                type="primary"
                                @click="openValidationDialog(ticket)"
                                :disabled="
                                    ticket.remainingValidationCount <= 0
                                ">
                                核销
                            </el-button>
                        </div>
                    </div>
                </el-card>
            </div>
        </div>

        <!-- 无票务提示 -->
        <div
            v-else-if="selectedUser && !ticketLoading"
            class="no-ticket-section">
            <el-empty description="该用户暂无可用票务" />
        </div>

        <!-- 已核销票务分割线 -->
        <div v-if="selectedUser" class="validated-section">
            <el-divider
                border-style="dashed"
                @click="toggleValidatedTickets"
                class="clickable-divider"
                content-position="center">
                <div class="divider-content">
                    <span class="divider-text">已核销</span>
                    <el-icon
                        class="expand-icon"
                        :class="{ expanded: showValidatedTickets }">
                        <ArrowDown />
                    </el-icon>
                </div>
            </el-divider>

            <!-- 已核销票务列表 -->
            <div v-if="showValidatedTickets" class="validated-tickets-section">
                <div
                    v-if="validatedTicketLoading"
                    class="loading-section"
                    v-loading="validatedTicketLoading"
                    element-loading-text="加载中..."></div>
                <div
                    v-else-if="validatedTicketList.length > 0"
                    class="ticket-grid">
                    <el-card
                        v-for="ticket in validatedTicketList"
                        :key="ticket.id"
                        class="ticket-card validated-card"
                        shadow="hover">
                        <div class="ticket-content">
                            <div class="ticket-header">
                                <h4 class="ticket-name">
                                    {{ ticket.ticket.name }}
                                </h4>
                                <div class="validation-info">
                                    <span class="validation-time">
                                        核销时间：{{
                                            formatValidationTime(
                                                ticket.validationAt
                                            )
                                        }}
                                    </span>
                                </div>
                            </div>
                            <div class="ticket-actions">
                                <el-button
                                    type="warning"
                                    @click="openCancelValidationDialog(ticket)">
                                    取消核销
                                </el-button>
                            </div>
                        </div>
                    </el-card>
                </div>
                <div v-else class="no-validated-ticket">
                    <el-empty description="暂无已核销票务" />
                </div>
            </div>
        </div>

        <!-- 核销确认对话框 -->
        <el-dialog
            v-model="validationDialogVisible"
            title="核销确认"
            width="400px"
            :before-close="closeValidationDialog">
            <div class="validation-dialog-content">
                <p><strong>票务名称：</strong>{{ currentTicket?.name }}</p>
                <p>
                    <strong>剩余核销次数：</strong
                    >{{ currentTicket?.remainingValidationCount }}
                </p>

                <div
                    v-if="currentTicket?.remainingValidationCount === 1"
                    class="validation-tip">
                    <el-alert
                        title="确认核销该票务？核销后将无法撤销。"
                        type="warning"
                        :closable="false"
                        show-icon />
                </div>

                <div v-else class="validation-input">
                    <label class="input-label">核销次数：</label>
                    <el-input-number
                        v-model="validationCount"
                        :min="1"
                        :max="currentTicket?.remainingValidationCount || 1"
                        controls-position="right"
                        class="count-input" />
                </div>
            </div>

            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="closeValidationDialog">取消</el-button>
                    <el-button
                        type="primary"
                        @click="confirmValidation"
                        :loading="validationLoading">
                        确定核销
                    </el-button>
                </div>
            </template>
        </el-dialog>

        <!-- 取消核销确认对话框 -->
        <el-dialog
            v-model="cancelValidationDialogVisible"
            title="取消核销确认"
            width="400px"
            :before-close="closeCancelValidationDialog">
            <div class="validation-dialog-content">
                <p>
                    <strong>票务名称：</strong
                    >{{ currentValidatedTicket?.ticket?.name }}
                </p>
                <p>
                    <strong>核销时间：</strong
                    >{{
                        formatValidationTime(
                            currentValidatedTicket?.validationAt
                        )
                    }}
                </p>

                <div class="validation-tip">
                    <el-alert
                        title="确认取消核销该票务？取消后票务将重新可用。"
                        type="warning"
                        :closable="false"
                        show-icon />
                </div>
            </div>

            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="closeCancelValidationDialog"
                        >取消</el-button
                    >
                    <el-button
                        type="primary"
                        @click="confirmCancelValidation"
                        :loading="cancelValidationLoading">
                        确定取消核销
                    </el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>

<script lang="ts" setup>
import { ref, reactive } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { ArrowDown } from '@icon-park/vue-next';
import { ticket, user } from '@/api';

// 响应式数据
const phoneNumber = ref('');
const searchLoading = ref(false);
const ticketLoading = ref(false);
const validationLoading = ref(false);
const selectedUser = ref(null);
const ticketList = ref([]);
const validationDialogVisible = ref(false);
const currentTicket = ref(null);
const validationCount = ref(1);

// 已核销票务相关
const showValidatedTickets = ref(false);
const validatedTicketLoading = ref(false);
const validatedTicketList = ref([]);
const cancelValidationDialogVisible = ref(false);
const currentValidatedTicket = ref(null);
const cancelValidationLoading = ref(false);

// 搜索用户
const searchUser = async () => {
    if (!phoneNumber.value.trim()) {
        ElMessage.warning('请输入手机号');
        return;
    }

    searchLoading.value = true;
    try {
        const result = await user.query({ phone: phoneNumber.value.trim() });

        if (!result.records || result.records.length === 0) {
            ElMessage.warning('未找到该手机号对应的用户');
            selectedUser.value = null;
            ticketList.value = [];
            return;
        }

        if (result.records.length > 1) {
            ElMessage.warning('找到多个用户，请确认手机号是否正确');
            selectedUser.value = null;
            ticketList.value = [];
            return;
        }

        // 找到唯一用户，查询其票务
        selectedUser.value = result.records[0];
        await queryUserTickets(selectedUser.value.id, true);
    } catch (error) {
        console.error('搜索用户失败:', error);
        ElMessage.error('搜索用户失败，请重试');
    } finally {
        searchLoading.value = false;
    }
};

// 查询用户票务
const queryUserTickets = async (userId, usable) => {
    ticketLoading.value = true;
    try {
        const result = await ticket.queryUserTicket(userId, usable);
        ticketList.value = result.records || [];

        // 如果查询未核销票务且结果为空，自动查询已核销票务并展开
        if (usable && (!result.records || result.records.length === 0)) {
            showValidatedTickets.value = true;
            await queryValidatedTickets(userId);
        }
    } catch (error) {
        console.error('查询用户票务失败:', error);
        ElMessage.error('查询用户票务失败，请重试');
        ticketList.value = [];
    } finally {
        ticketLoading.value = false;
    }
};

// 打开核销对话框
const openValidationDialog = (ticketItem) => {
    currentTicket.value = ticketItem;
    validationCount.value = 1;
    validationDialogVisible.value = true;
};

// 关闭核销对话框
const closeValidationDialog = () => {
    validationDialogVisible.value = false;
    currentTicket.value = null;
    validationCount.value = 1;
};

// 确认核销
const confirmValidation = async () => {
    if (!currentTicket.value) return;

    const countToValidate =
        currentTicket.value.remainingValidationCount === 1
            ? 1
            : validationCount.value;

    validationLoading.value = true;
    try {
        await ticket.validation(currentTicket.value.id, countToValidate);

        ElMessage.success(`核销成功！已核销 ${countToValidate} 次`);

        // 关闭对话框
        closeValidationDialog();

        // 重新查询用户票务
        if (selectedUser.value) {
            await queryUserTickets(selectedUser.value.id, true);
            // 如果已核销列表已展开，也刷新已核销列表
            if (showValidatedTickets.value) {
                await queryValidatedTickets(selectedUser.value.id);
            }
        }
    } catch (error) {
        console.error('核销失败:', error);
        ElMessage.error('核销失败，请重试');
    } finally {
        validationLoading.value = false;
    }
};

// 切换已核销票务显示
const toggleValidatedTickets = async () => {
    showValidatedTickets.value = !showValidatedTickets.value;

    if (showValidatedTickets.value && selectedUser.value) {
        await queryValidatedTickets(selectedUser.value.id);
    }
};

// 查询已核销票务
const queryValidatedTickets = async (userId) => {
    validatedTicketLoading.value = true;
    try {
        const result = await ticket.queryUserTicket(userId, false);
        validatedTicketList.value = result.records || [];
    } catch (error) {
        console.error('查询已核销票务失败:', error);
        ElMessage.error('查询已核销票务失败，请重试');
        validatedTicketList.value = [];
    } finally {
        validatedTicketLoading.value = false;
    }
};

// 格式化核销时间
const formatValidationTime = (validationAt) => {
    if (!validationAt) return '-';
    const date = new Date(validationAt);
    return date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
    });
};

// 打开取消核销对话框
const openCancelValidationDialog = (ticketItem) => {
    currentValidatedTicket.value = ticketItem;
    cancelValidationDialogVisible.value = true;
};

// 关闭取消核销对话框
const closeCancelValidationDialog = () => {
    cancelValidationDialogVisible.value = false;
    currentValidatedTicket.value = null;
};

// 确认取消核销
const confirmCancelValidation = async () => {
    if (!currentValidatedTicket.value) return;

    cancelValidationLoading.value = true;

    const result = await ticket.cancelValidation(
        currentValidatedTicket.value.id
    );

    if (result === undefined) {
        cancelValidationLoading.value = false;
        closeCancelValidationDialog();
        return;
    }

    ElMessage.success('取消核销成功！');

    // 重新查询两个列表
    if (selectedUser.value) {
        await queryUserTickets(selectedUser.value.id, true);
        await queryValidatedTickets(selectedUser.value.id);
    }
    cancelValidationLoading.value = false;

    closeCancelValidationDialog();
};
</script>

<style lang="scss" scoped>
.ticket-validation-page {
    padding: 20px;
    background-color: #f5f7fa;
    min-height: 100vh;
}

.search-section {
    margin-bottom: 20px;

    .search-card {
        border-radius: 8px;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

        .search-form {
            display: flex;
            gap: 16px;
            align-items: center;

            .phone-input {
                flex: 1;
                max-width: 300px;
            }
        }
    }
}

.ticket-list-section {
    .section-title {
        margin: 0 0 20px 0;
        color: #303133;
        font-size: 20px;
        font-weight: 600;
    }

    .ticket-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
        gap: 20px;

        .ticket-card {
            border-radius: 12px;
            transition: all 0.3s ease;
            border: 1px solid #e4e7ed;

            &:hover {
                transform: translateY(-4px);
                box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
            }

            .ticket-content {
                .ticket-header {
                    margin-bottom: 16px;

                    .ticket-name {
                        margin: 0 0 12px 0;
                        color: #303133;
                        font-size: 18px;
                        font-weight: 600;
                        line-height: 1.4;
                    }

                    .remaining-count {
                        display: flex;
                        align-items: center;
                        gap: 8px;

                        .count-label {
                            color: #909399;
                            font-size: 14px;
                        }

                        .count-value {
                            color: #409eff;
                            font-size: 16px;
                            font-weight: 600;
                            background: linear-gradient(
                                135deg,
                                #409eff,
                                #67c23a
                            );
                            -webkit-background-clip: text;
                            -webkit-text-fill-color: transparent;
                            background-clip: text;
                        }
                    }
                }

                .ticket-actions {
                    display: flex;
                    justify-content: flex-end;

                    .el-button {
                        border-radius: 6px;
                        padding: 8px 20px;
                        font-weight: 500;

                        &:disabled {
                            opacity: 0.5;
                        }
                    }
                }
            }
        }
    }
}

.no-ticket-section {
    margin-top: 40px;
    text-align: center;

    :deep(.el-empty) {
        padding: 60px 0;

        .el-empty__description {
            color: #909399;
            font-size: 16px;
        }
    }
}

.validated-section {
    margin-top: 30px;

    .clickable-divider {
        cursor: pointer;
        transition: all 0.3s ease;
        margin: 30px 0;

        :deep(.el-divider__text) {
            background-color: #f5f7fa;
            padding: 8px 16px;
            border-radius: 8px;
            transition: all 0.3s ease;
        }

        &:hover {
            :deep(.el-divider__text) {
                background-color: #f5f7fa;
            }
        }

        .divider-content {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 12px;

            .divider-text {
                color: #606266;
                font-size: 16px;
                font-weight: 500;
            }

            .expand-icon {
                color: #909399;
                transition: transform 0.3s ease;
                font-size: 16px;

                &.expanded {
                    transform: rotate(180deg);
                }
            }
        }
    }

    .validated-tickets-section {
        margin-top: 20px;
        animation: slideDown 0.3s ease;

        .loading-section {
            height: 100px;
            position: relative;
        }

        .no-validated-ticket {
            text-align: center;
            padding: 40px 0;

            :deep(.el-empty) {
                .el-empty__description {
                    color: #909399;
                    font-size: 14px;
                }
            }
        }

        // 已核销票务使用与未核销相同的多列布局
        .ticket-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
            gap: 20px;
        }

        .validated-card {
            border-radius: 12px;
            transition: all 0.3s ease;
            border: 1px solid #e4e7ed;
            background: #f8f9fa;
            opacity: 0.85;
            filter: grayscale(0.3);

            &:hover {
                transform: translateY(-4px);
                box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
                opacity: 0.95;
            }

            .ticket-content {
                .ticket-header {
                    margin-bottom: 16px;

                    .ticket-name {
                        margin: 0 0 12px 0;
                        color: #909399;
                        font-size: 18px;
                        font-weight: 600;
                        line-height: 1.4;
                    }

                    .validation-info {
                        display: flex;
                        align-items: center;
                        gap: 8px;

                        .validation-time {
                            color: #909399;
                            font-size: 14px;
                            background: #e9ecef;
                            padding: 4px 8px;
                            border-radius: 4px;
                            display: inline-block;
                        }
                    }
                }

                .ticket-actions {
                    display: flex;
                    justify-content: flex-end;

                    .el-button {
                        border-radius: 6px;
                        padding: 8px 20px;
                        font-weight: 500;

                        &:disabled {
                            opacity: 0.5;
                        }
                    }

                    .el-button--warning {
                        background: linear-gradient(135deg, #8b5cf6, #ec4899);
                        border: none;
                        color: white;
                        transition: all 0.3s ease;
                        box-shadow: 0 2px 4px rgba(139, 92, 246, 0.3);

                        &:hover {
                            background: linear-gradient(
                                135deg,
                                #a78bfa,
                                #f472b6
                            );
                            box-shadow: 0 6px 12px rgba(139, 92, 246, 0.4);
                        }

                        &:active {
                            background: linear-gradient(
                                135deg,
                                #7c3aed,
                                #db2777
                            );
                            box-shadow: 0 2px 4px rgba(139, 92, 246, 0.3);
                        }
                    }
                }
            }
        }
    }
}

@keyframes slideDown {
    from {
        opacity: 0;
        transform: translateY(-10px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}

.validation-dialog-content {
    padding: 20px 0;

    p {
        margin: 12px 0;
        color: #606266;
        font-size: 14px;

        strong {
            color: #303133;
            font-weight: 500;
        }
    }

    .validation-tip {
        margin: 20px 0;
    }

    .validation-input {
        margin: 20px 0;
        display: flex;
        align-items: center;
        gap: 12px;

        .input-label {
            color: #303133;
            font-size: 14px;
            font-weight: 500;
            min-width: 80px;
        }

        .count-input {
            width: 120px;
        }
    }
}

.dialog-footer {
    display: flex;
    justify-content: flex-end;
    gap: 12px;
}

// 响应式设计
@media (max-width: 768px) {
    .ticket-validation-page {
        padding: 16px;
    }

    .search-form {
        flex-direction: column;
        align-items: stretch !important;

        .phone-input {
            max-width: none !important;
        }
    }

    .ticket-grid {
        grid-template-columns: 1fr !important;
    }

    .validation-input {
        flex-direction: column !important;
        align-items: flex-start !important;

        .input-label {
            min-width: auto !important;
        }
    }
}

// Element Plus 组件样式覆盖
:deep(.el-card__body) {
    padding: 20px;
}

:deep(.el-button--primary) {
    background: linear-gradient(135deg, #6366f1, #8b5cf6);
    border: none;
    transition: all 0.3s ease;
    box-shadow: 0 2px 4px rgba(99, 102, 241, 0.3);

    &:hover {
        background: linear-gradient(135deg, #818cf8, #a78bfa);
        box-shadow: 0 6px 12px rgba(99, 102, 241, 0.4);
    }

    &:active {
        background: linear-gradient(135deg, #4f46e5, #7c3aed);
        box-shadow: 0 2px 4px rgba(99, 102, 241, 0.3);
    }
}

:deep(.el-input__wrapper) {
    border-radius: 6px;
    transition: all 0.3s ease;

    &:hover {
        box-shadow: 0 0 0 1px #c0c4cc inset;
    }

    &.is-focus {
        box-shadow: 0 0 0 1px #409eff inset;
    }
}

:deep(.el-dialog) {
    border-radius: 12px;
    overflow: hidden;
}

:deep(.el-dialog__header) {
    background: linear-gradient(135deg, #f8f9fa, #e9ecef);
    padding: 20px 24px;
    border-bottom: 1px solid #e4e7ed;

    .el-dialog__title {
        font-weight: 600;
        color: #303133;
    }
}

:deep(.el-dialog__body) {
    padding: 0 24px;
}

:deep(.el-dialog__footer) {
    padding: 20px 24px;
    background: #fafafa;
    border-top: 1px solid #e4e7ed;
}
</style>

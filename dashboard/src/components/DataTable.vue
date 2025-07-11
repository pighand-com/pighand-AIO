<!-- eslint-disable vue/no-use-v-if-with-v-for -->
<template>
    <div class="table-form">
        <el-button type="primary" :icon="Plus" v-if="add" @click="onOpenAdd"
            >添加</el-button
        >

        <!-- 表格头槽 -->
        <slot name="header-operation" />

        <!-- 根据批量操作数组显示对应的按钮或下拉菜单 -->
        <template v-if="batchActions && batchActions.length > 0">
            <!-- 当只有一个批量操作时显示单个按钮 -->
            <el-button
                v-if="batchActions.length === 1"
                type="default"
                class="ml-4"
                :disabled="!multipleSelection.length"
                @click="batchActions[0].handler(multipleSelection)">
                {{ batchActions[0].name }}
            </el-button>

            <!-- 当有多个批量操作时显示下拉菜单 -->
            <el-dropdown
                v-else
                placement="bottom-start"
                class="ml-4"
                :disabled="!multipleSelection.length">
                <el-button type="default" :disabled="!multipleSelection.length"
                    >批量操作</el-button
                >
                <template #dropdown>
                    <el-dropdown-menu>
                        <el-dropdown-item
                            v-for="action in batchActions"
                            :key="action.name"
                            @click="action.handler(multipleSelection)">
                            {{ action.name }}
                        </el-dropdown-item>
                    </el-dropdown-menu>
                </template>
            </el-dropdown>
        </template>

        <el-table
            v-loading="isTableDataLoading"
            v-bind="tableAttrs"
            :data="tableDataModel.data"
            @selection-change="handleSelectionChange"
            class="data-table"
            header-cell-class-name="table-header">
            <!-- 根据是否有批量操作显示复选框 -->
            <el-table-column
                v-if="batchActions && batchActions.length > 0"
                type="selection"
                width="42" />

            <el-table-column
                v-for="(item, index) in formColumns.filter(
                    (item) => item.isTable
                )"
                :key="index"
                :label="item.label"
                :prop="item.prop"
                :width="item.tableWidth"
                :align="item.tableAlign || 'left'">
                <template #default="scope">
                    <el-tooltip
                        content="复制连接"
                        placement="top"
                        v-if="
                            item.isTableCopyValue &&
                            scope.row[item.prop] &&
                            ['image', 'link'].includes(item.tableType)
                        ">
                        <el-button
                            type="primary"
                            link
                            @click="onCopy(scope.row, item)">
                            <CopyLink />
                        </el-button>
                    </el-tooltip>

                    <img
                        v-if="item.tableType === 'image'"
                        v-for="(imageItem, imageIndex) in Array.isArray(
                            scope.row[item.prop]
                        )
                            ? scope.row[item.prop]
                            : [scope.row[item.prop]]"
                        :key="'image_' + imageIndex"
                        :src="imageItem"
                        @click="onPreviewImage(imageItem)"
                        class="table-image" />

                    <a
                        v-else-if="item.tableType === 'link'"
                        :href="scope.row[item.prop]"
                        target="_blank"
                        class="table-link"
                        >{{ scope.row[item.prop] }}</a
                    >

                    <component
                        v-else-if="
                            item.tableFormat &&
                            typeof item.tableFormat === 'function' &&
                            isVNode(dataFormat(scope.row, item))
                        "
                        :is="dataFormat(scope.row, item)" />

                    <span
                        v-dompurify-html="dataFormat(scope.row, item)"
                        v-else></span>

                    <el-tooltip
                        content="复制"
                        placement="top"
                        v-if="
                            item.isTableCopyValue &&
                            scope.row[item.prop] &&
                            !['image', 'link'].includes(item.tableType)
                        ">
                        <el-button
                            type="primary"
                            link
                            @click="onCopy(scope.row, item)">
                            <Copy />
                        </el-button>
                    </el-tooltip>
                </template>
            </el-table-column>

            <slot />

            <el-table-column
                fixed="right"
                v-if="Array.isArray(tableOperation)"
                label="操作"
                align="center"
                :width="operationWidth || 180">
                <template #default="scope">
                    <slot name="column-operation" :row="scope.row" />

                    <el-button
                        v-if="tableOperation.includes('edit')"
                        size="small"
                        :icon="Edit"
                        type="primary"
                        @click="onEdit(scope.row)"
                        >编辑</el-button
                    >

                    <el-popconfirm
                        v-if="tableOperation.includes('delete')"
                        icon-color="rgb(245, 108, 108)"
                        title="是否确认删除?"
                        @confirm="onDel(scope.row)">
                        <template #reference>
                            <el-button size="small" :icon="Delete" type="danger"
                                >删除</el-button
                            >
                        </template>
                    </el-popconfirm>
                </template>
            </el-table-column>
        </el-table>

        <el-pagination
            class="pagination"
            :disabled="isTableDataLoading"
            :current-page="pageNumber"
            :page-size="pageSize"
            :page-sizes="[10, 20, 50, 100]"
            :background="true"
            :total="tableDataModel.totalRow"
            size="small"
            layout="total, sizes, prev, pager, next, jumper"
            @update:page-size="handleChangePageSize"
            @update:current-page="handleChangeCurrentPage" />
    </div>

    <PImageView
        :image-url="dialogImageUrl"
        v-model:image-visible="dialogImageVisible"></PImageView>
</template>

<script lang="ts" setup>
import moment from 'moment';
import {
    ref,
    inject,
    watch,
    onMounted,
    useAttrs,
    computed,
    PropType,
    nextTick
} from 'vue';
import { Plus, Edit, Delete, Copy, CopyLink } from '@icon-park/vue-next';

import { ProvideFormInterface } from '@/common/provideForm';
import { isVNode } from 'vue';

const attrs = useAttrs();

const dialogImageUrl = ref('');
const dialogImageVisible = ref(false);

const tableAttrs = computed(() => {
    const { class: _, style: __, ...rest } = attrs;
    return rest;
});

interface BatchAction {
    name: string;
    handler: (selection: any[]) => void;
}

/**
 * 组件属性定义
 * @property {Boolean} add - 是否显示添加按钮,默认为true
 * @property {Array<'edit'|'delete'>|Boolean} tableOperation - 表格操作列配置,可以是数组指定显示的操作按钮,或者布尔值控制是否显示操作列,默认显示编辑和删除按钮
 * @property {Number|String} operationWidth - 操作列宽度
 * @property {Function} handleQuery - 查询数据处理函数
 * @property {Function} handleFind - 查询单条数据处理函数
 * @property {Function} handleDelete - 删除数据处理函数
 * @property {Array<BatchAction>} batchActions - 批量操作配置
 */
const props = defineProps({
    add: {
        type: Boolean,
        default: true
    },
    tableOperation: {
        type: Array<'edit' | 'delete'> || Boolean,
        default: ['edit', 'delete']
    },
    operationWidth: [Number, String],
    handleQuery: Function,
    handleFind: Function,
    handleDelete: Function,
    handleFormatData: Function,
    batchActions: {
        type: Array as PropType<BatchAction[]>,
        default: () => []
    }
});

const provideForm: ProvideFormInterface = inject('provideForm');
let {
    tableDataModel,
    isTableDataLoading,
    isDetailDataLoading,
    formColumns,
    pageSize,
    pageNumber,
    isOpenDetail,
    queryTableData,
    detailFormModel,
    getDetailOperation
} = provideForm;

watch(
    () => tableDataModel.data,
    (newValue) => {
        if (!newValue) {
            return;
        }

        nextTick(() => {
            if (!props.handleFormatData) {
                return;
            }

            tableDataModel.data.forEach((item) => {
                props.handleFormatData({
                    queryData: item
                });
            });
        });
    }
);

/**
 * 点击添加按钮
 */
const onOpenAdd = () => {
    isOpenDetail.value = true;
};

/**
 * 切换每页大小
 * @param pageSize
 */
const handleChangePageSize = async (newPageSize) => {
    if (!props.handleQuery || pageSize.value === newPageSize) return;

    await queryTableData(async (params: any) => {
        pageSize.value = newPageSize;
        pageNumber.value = 1;

        return await props.handleQuery({
            ...params,
            pageSize: newPageSize,
            pageNumber: 1
        });
    });
};

/**
 * 切换当前页
 * @param pageNumber null:第一页
 */
const handleChangeCurrentPage = async (newCurrentPage?: number) => {
    if (
        !props.handleQuery ||
        (newCurrentPage && pageNumber.value === newCurrentPage)
    )
        return;

    await queryTableData(async (params: any) => {
        pageNumber.value = newCurrentPage || 1;

        return await props.handleQuery({
            ...params,
            pageNumber: newCurrentPage || 1
        });
    });
};

/**
 * 编辑前查询
 */
const onEdit = async (row) => {
    if (!props.handleFind) return;

    const { primaryValue } = getDetailOperation(row);

    isDetailDataLoading.value = true;
    isOpenDetail.value = true;

    const result = await props.handleFind(primaryValue);

    Object.assign(detailFormModel, result);

    isDetailDataLoading.value = false;
};

/**
 * 删除
 * @param row
 */
const onDel = async (row: any) => {
    const { primaryValue } = getDetailOperation(row);
    await props.handleDelete(primaryValue);

    await handleChangeCurrentPage();
};

/**
 * 图片预览
 * @param src
 */
const onPreviewImage = (src) => {
    dialogImageUrl.value = src;
    dialogImageVisible.value = true;
};

/**
 * 复制
 * @param row
 * @param item
 */
const onCopy = (row, item) => {
    navigator.clipboard
        .writeText(row[item.prop])
        .then(() => ElMessage.success('复制成功'))
        .catch(() => ElMessage.error('复制失败'));
};

/**
 * 格式化
 * @param row
 * @param item
 */
const dataFormat = (row, item) => {
    const { domType, domData, tableFormat, isTableCopyValue } = item;

    // 支持获取嵌套属性值
    let value = item.prop.split('.').reduce((obj, key) => obj?.[key], row);

    if (value === null || value === undefined) {
        value = '';
    }

    if (tableFormat && typeof tableFormat === 'function') {
        value = tableFormat(value, row, item);
    } else if (
        ['dateTimePicker', 'dateTimePickerRange'].includes(domType) &&
        value
    ) {
        value = moment(value).format('YYYY-MM-DD HH:mm:ss');
    } else if (['datePicker', 'datePickerRange'].includes(domType) && value) {
        value = moment(value).format('YYYY-MM-DD');
    } else if (domData && Array.isArray(domData)) {
        const data = domData.find((item) => item.value == value);
        value = data ? data.label : value;
    } else if (domType === 'select' && domData && Array.isArray(domData)) {
        const data = domData.find((item) => item.value == value);
        value = data ? data.label : value;
    }

    return value || '-';
};

/**
 * 默认执行查询
 */
onMounted(() => {
    handleChangeCurrentPage();
});

// 选中的行数据
const multipleSelection = ref([]);

// 选择变化处理函数
const handleSelectionChange = (selection: any[]) => {
    multipleSelection.value = selection;
};
</script>

<style scoped>
.table-form {
    border-radius: 8px;
    margin-top: 24px;
    padding: 18px 24px;
    background: linear-gradient(
        135deg,
        rgba(255, 255, 255, 0.9) 0%,
        rgba(255, 255, 255, 0.6) 100%
    );
    backdrop-filter: blur(10px);
    -webkit-backdrop-filter: blur(10px);
    box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.07);
}

.data-table {
    width: 100%;
    margin: 8px 0px;
    color: var(--p-color-dark);
}

.table-header {
    background-color: #f5f7fa !important;
}

.pagination {
    display: flex;
    justify-content: flex-end;
}

.table-image {
    max-width: 100%;
    max-height: 160px;
    cursor: pointer;
}

.table-link {
    text-decoration: none !important;
}

.table-link:hover {
    color: var(--p-color-primary) !important;
    text-decoration: underline !important;
}

.operation-column {
    gap: 4px;
    justify-content: center;
    display: flex;
    flex-wrap: wrap;
}
</style>

<!-- eslint-disable vue/no-use-v-if-with-v-for -->
<template>
    <div class="table-form">
        <el-button type="primary" :icon="Plus" v-if="add" @click="onOpenAdd"
            >添加</el-button
        >

        <slot name="operation" />

        <el-table
            v-loading="isTableDataLoading"
            :data="tableDataModel.data"
            class="data-table"
            header-cell-class-name="table-header"
            border>
            <el-table-column
                v-for="(item, index) in formColumns.filter(
                    (item) => item.isTable
                )"
                :key="index"
                :label="item.label"
                :prop="item.prop"
                :width="item.tableWidth"
                :align="item.tableType === 'image' ? 'center' : 'left'">
                <template #default="scope">
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

                    <div
                        v-dompurify-html="dataFormat(scope.row, item)"
                        v-else></div>
                </template>
            </el-table-column>

            <slot />

            <el-table-column
                fixed="right"
                v-if="Array.isArray(tableOperation)"
                label="操作"
                :width="operationWidth">
                <template #default="scope">
                    <slot name="table-operation" :row="scope.row" />

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
                            <el-button
                                size="small"
                                :icon="DeleteFilled"
                                type="danger"
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
            :current-page="pageCurrent"
            :page-size="pageSize"
            :page-sizes="[10, 20, 50, 100]"
            :background="true"
            layout="total, sizes, prev, pager, next, jumper"
            :total="tableDataModel.total"
            @update:page-size="handleChangePageSize"
            @update:current-page="handleChangeCurrentPage" />
    </div>

    <PImageView
        :image-url="dialogImageUrl"
        v-model:image-visible="dialogImageVisible"></PImageView>
</template>

<script lang="ts" setup>
import moment from 'moment';
import { ref, inject, onMounted } from 'vue';
import { Plus, Edit, DeleteFilled } from '@element-plus/icons-vue';

import { ProvideFormInterface } from '@/common/provideForm';

const dialogImageUrl = ref('');
const dialogImageVisible = ref(false);

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
    handleDelete: Function
});

const provideForm: ProvideFormInterface = inject('provideForm');
const {
    tableDataModel,
    isTableDataLoading,
    isDetailDataLoading,
    formColumns,
    pageSize,
    pageCurrent,
    isOpenDetail,
    queryTableData,
    detailFormModel,
    getDetailOperation
} = provideForm;

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
        pageCurrent.value = 1;

        return await props.handleQuery({
            ...params,
            pageSize: newPageSize,
            pageCurrent: 1
        });
    });
};

/**
 * 切换当前页
 * @param pageCurrent null:第一页
 */
const handleChangeCurrentPage = async (newCurrentPage?: number) => {
    if (
        !props.handleQuery ||
        (newCurrentPage && pageCurrent.value === newCurrentPage)
    )
        return;

    await queryTableData(async (params: any) => {
        pageCurrent.value = newCurrentPage || 1;

        return await props.handleQuery({
            ...params,
            pageCurrent: newCurrentPage || 1
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
 * 格式化
 * @param row
 * @param item
 */
const dataFormat = (row, item) => {
    const { domType, domData, tableFormat } = item;
    let value = row[item.prop];
    if (value === null || value === undefined) {
        value = '';
    }

    if (tableFormat && typeof tableFormat === 'function') {
        return tableFormat(value, row, item);
    }

    if (
        (domType === 'dateTimePicker' || domType === 'datePickerRange') &&
        value
    ) {
        return moment(value).format('YYYY-MM-DD HH:mm:ss');
    } else if (domType === 'datePicker' && value) {
        return moment(value).format('YYYY-MM-DD');
    }
    if (domData && Array.isArray(domData)) {
        const data = domData.find((item) => item.value == value);
        return data ? data.label : value;
    }

    return value;
};

/**
 * 默认执行查询
 */
onMounted(() => {
    handleChangeCurrentPage();
});
</script>

<style scoped>
.table-form {
    border-radius: 8px;
    background-color: #ffffff;
    margin-top: 24px;
    padding: 18px 24px;
}

.data-table {
    width: 100%;
    margin: 8px 0px;
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
</style>

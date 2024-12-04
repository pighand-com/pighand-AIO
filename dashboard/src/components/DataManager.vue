<template>
    <!-- 搜索组件 -->
    <PSearch
        v-if="isShowSearch"
        :handle-query="handleQuery"
        v-bind="searchProps">
        <template v-for="(_, name) in $slots" #[getSearchSlotName(name)]>
            <slot :name="name"></slot>
        </template>
    </PSearch>

    <!-- 数据表格组件 -->
    <PDataTable
        v-if="isShowDataTable"
        :handle-query="handleQuery"
        :handle-find="handleFind"
        :handle-delete="handleDelete"
        v-bind="dataTableProps">
        <template v-for="(_, name) in $slots" #[getTableSlotName(name)]>
            <slot :name="name"></slot>
        </template>
    </PDataTable>

    <!-- 抽屉组件 -->
    <PDrawer
        v-if="isShowDrawer"
        :handle-create="handleCreate"
        :handle-update="handleUpdate"
        :handle-query="handleQuery"
        v-bind="drawerProps">
        <template v-for="(_, name) in $slots" #[getDrawerSlotName(name)]>
            <slot :name="name"></slot>
        </template>

        <template #detail-float>
            <slot name="detail-float"></slot>
        </template>
    </PDrawer>
</template>

<script lang="ts" setup>
defineProps({
    // 控制组件显示的开关
    isShowSearch: {
        type: Boolean,
        required: false,
        default: true
    },
    isShowDataTable: {
        type: Boolean,
        required: false,
        default: true
    },
    isShowDrawer: {
        type: Boolean,
        required: false,
        default: true
    },

    // CRUD 操作处理函数
    handleQuery: {
        type: Function,
        required: true
    },
    handleFind: {
        type: Function,
        required: false,
        default: undefined
    },
    handleDelete: {
        type: Function,
        required: false,
        default: undefined
    },
    handleCreate: {
        type: Function,
        required: false,
        default: undefined
    },
    handleUpdate: {
        type: Function,
        required: false,
        default: undefined
    },

    // 子组件属性配置对象
    drawerProps: {
        type: Object,
        required: false,
        default: () => ({})
    },
    dataTableProps: {
        type: Object,
        required: false,
        default: () => ({})
    },
    searchProps: {
        type: Object,
        required: false,
        default: () => ({})
    }
});

/**
 * 处理搜索组件插槽名称
 * @param name 原始插槽名
 * @returns 处理后的插槽名（移除'search-'前缀）
 */
function getSearchSlotName(name: string | number): string {
    const nameStr = String(name);
    return nameStr.startsWith('search-') ? nameStr.substring(7) : nameStr;
}

/**
 * 处理表格组件插槽名称
 * @param name 原始插槽名
 * @returns 处理后的插槽名（移除'table-'前缀）
 */
function getTableSlotName(name: string | number): string {
    const nameStr = String(name);
    return nameStr.startsWith('table-') ? nameStr.substring(6) : nameStr;
}

/**
 * 处理抽屉组件插槽名称
 * @param name 原始插槽名
 * @returns 处理后的插槽名（移除'drawer-'前缀）
 */
function getDrawerSlotName(name: string | number): string {
    const nameStr = String(name);
    return nameStr.startsWith('drawer-') ? nameStr.substring(7) : nameStr;
}
</script>

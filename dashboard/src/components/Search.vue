<!-- eslint-disable vue/no-mutating-props -->
<template>
    <el-form
        ref="searchForm"
        :inline="true"
        :model="searchFormModel"
        :disabled="isTableDataLoading"
        :class="
            formColumns.filter(
                (item) => item.isSearch && !item.isSearchMore && !item.hidden
            ).length
                ? 'search-form'
                : 'search-form search-form-bottom'
        "
        label-width="auto">
        <div class="search-default">
            <div>
                <FormItem
                    :form-model="searchFormModel"
                    :form-columns="
                        formColumns.filter(
                            (item) => item.isSearch && !item.isSearchMore
                        )
                    "
                    :on-where="'search'">
                    <template
                        v-for="(item, index) in formColumns.filter(
                            (item) => item.isSearch && !item.isSearchMore
                        )"
                        :key="index"
                        #[`search-${index}-before`]>
                        <slot :name="`${index}-before`" />
                    </template>

                    <template
                        v-for="(item, index) in formColumns.filter(
                            (item) => item.isSearch && !item.isSearchMore
                        )"
                        :key="index"
                        #[`search-${index}-after`]>
                        <slot :name="`${index}-after`" />
                    </template>
                </FormItem>
                <slot />
            </div>
            <div>
                <el-button-group class="button-group">
                    <el-button
                        color="#409eff"
                        :loading="isTableDataLoading"
                        @click="handleSearch"
                        plain
                        ><el-tooltip
                            class="button-icon"
                            :show-after="1000"
                            content="搜索"
                            placement="bottom"
                            effect="light">
                            <el-icon><Search /></el-icon></el-tooltip
                    ></el-button>

                    <el-button color="#409eff" @click="handleReset" plain>
                        <el-tooltip
                            :show-after="1000"
                            content="重置"
                            placement="bottom"
                            effect="light">
                            <el-icon><Refresh /></el-icon>
                        </el-tooltip>
                    </el-button>

                    <el-button
                        v-if="
                            formColumns
                                .map((item) => item.isSearchMore)
                                .includes(true)
                        "
                        color="#409eff"
                        @click="handleShowHiddenMore"
                        plain>
                        <el-tooltip
                            :show-after="1000"
                            content="更多"
                            placement="bottom"
                            effect="light">
                            <el-icon>
                                <ArrowUp v-if="showMore" />
                                <ArrowDown v-else />
                            </el-icon>
                        </el-tooltip>
                    </el-button>
                </el-button-group>
            </div>
        </div>
        <div class="more-hidden" :class="{ 'more-show': showMore }">
            <el-divider border-style="dashed" />

            <FormItem
                :form-model="searchFormModel"
                :form-columns="formColumns.filter((item) => item.isSearchMore)"
                :on-where="'search-more'">
                <template
                    v-for="(item, index) in formColumns.filter(
                        (item) => item.isSearchMore
                    )"
                    :key="index"
                    #[`search-more-${index}-before`]>
                    <slot :name="`more-${index}-before`" />
                </template>

                <template
                    v-for="(item, index) in formColumns.filter(
                        (item) => item.isSearch && !item.isSearchMore
                    )"
                    :key="index"
                    #[`search-more-${index}-after`]>
                    <slot :name="`more-${index}-after`" />
                </template>
            </FormItem>

            <slot name="more" />
        </div>
    </el-form>
</template>

<script lang="ts" setup>
import { ref, inject, onMounted } from 'vue';
import { Search, Refresh, ArrowUp, ArrowDown } from '@element-plus/icons-vue';
import { ProvideFormInterface } from '@/common/provideForm';
import FormItem from '@/components/FormItem.vue';

const props = defineProps({
    handleQuery: Function
});

const searchForm = ref();
const showMore = ref(false);

const provideForm: ProvideFormInterface = inject('provideForm');
const {
    isTableDataLoading,
    searchFormModel,
    formColumns,
    pageCurrent,
    queryTableData,
    searchDefaultValue,
    getDomData
} = provideForm;

// 设置默认值
Object.keys(searchDefaultValue).forEach((key) => {
    searchFormModel[key] = searchDefaultValue[key];
});

// 初始化加载
onMounted(() => {
    formColumns.forEach((item) => {
        if (
            (item.isSearch || item.isSearchMore) &&
            !item.hidden &&
            getDomData[item.label + item.prop]
        ) {
            getDomData[item.label + item.prop]();
        }
    });
});

/**
 * 搜索
 */
const handleSearch = async () => {
    if (props.handleQuery && !isTableDataLoading.value) {
        await queryTableData(async (params: any) => {
            pageCurrent.value = 1;

            return await props.handleQuery({
                ...params,
                pageCurrent: 1
            });
        });
    }
};

/**
 * 重置
 */
const handleReset = async () => {
    const notEmptyValues = Object.values(searchFormModel).filter(
        (item) => item !== '' && item !== undefined && item !== null
    );

    if (notEmptyValues.length) {
        Object.keys(searchFormModel).forEach((key) => {
            searchFormModel[key] = null;
        });

        await handleSearch();
    }
};

/**
 * 显示隐藏更多搜索条件
 */
const handleShowHiddenMore = () => {
    showMore.value = !showMore.value;
};
</script>

<style lang="scss" scoped>
.search-form {
    border-radius: 8px;
    background-color: #ffffff;
    padding: 18px 24px 0px 24px;
}

.search-form-bottom {
    padding-bottom: 18px;
}

.search-default {
    display: flex;
    justify-content: space-between;
}

.button-group {
    display: flex;
    justify-content: flex-end;
    min-width: 136px;

    :deep(.el-button) {
        padding: 0 !important;
    }

    :deep(.el-button > span) {
        width: 100%;
        height: 100%;
    }

    :deep(.el-icon) {
        width: 100% !important;
        height: 100% !important;
        padding: 0 15px !important;
    }
}

.more-hidden {
    max-height: 0;
    overflow: hidden;
    transition: max-height 0.5s linear;
}

.more-show {
    max-height: 800px;
}

.el-divider {
    margin: 0px 0px 18px 0px;
}
</style>

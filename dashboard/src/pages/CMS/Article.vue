<template>
    <PDataManager
        :handle-query="article.query"
        :handle-find="article.find"
        :handle-delete="article.del"
        :handle-create="article.create"
        :handle-update="article.update"
        :drawer-props="{
            size: '60%'
        }">
        <template #[`drawer-2-after`]>
            <el-form-item label="分类" prop="categories">
                <el-cascader
                    :options="allCategories"
                    :props="props"
                    v-model="detailFormModel['categories']"
                    clearable />
            </el-form-item>
        </template>
    </PDataManager>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { article, category } from '@/api/index.ts';
import provideForm from '@/common/provideForm';

const props = { multiple: true };
const allCategories = ref([]);

// 将数据转换为树形结构
function convertToFormat(data) {
    const idMap = new Map();
    const result = [];

    for (const item of data) {
        idMap.set(item.id, { ...item, children: [] });
    }

    for (const item of idMap.values()) {
        const parentId = item.parentId;
        if (parentId === null) {
            result.push({
                ...item,
                value: item.id,
                label: item.name
            });
        } else {
            const parent = idMap.get(parentId.toString());
            if (parent) {
                parent.children.push({
                    ...item,
                    value: item.id,
                    label: item.name
                });
            }
        }
    }

    return result;
}

onMounted(async () => {
    const categories = await category.query();
    const formattedData = convertToFormat(categories);
    allCategories.value = formattedData;
});

const { detailFormModel } = provideForm([
    {
        label: 'id',
        prop: 'id',
        isDetail: true,
        isPrimaryKey: true,
        hidden: true
    },
    {
        label: 'banner图',
        prop: 'bannerUrl',
        isTable: false,
        isDetail: true,
        domType: 'uploadImage',
        tableType: 'image',
        rules: [
            {
                required: true,
                message: 'banner图必填',
                trigger: 'blur'
            }
        ],
        uploadPath: 'banner'
    },
    {
        label: '标题',
        prop: 'title',
        isSearch: true,
        isSearchMore: false,
        searchPlaceholder: '模糊搜索',
        isTable: true,
        tableAlign: 'left',
        isDetail: true,
        rules: [
            {
                required: true,
                message: '标题必填',
                trigger: 'blur'
            }
        ]
    },
    {
        label: '分类',
        prop: 'classify',
        isSearch: false,
        isSearchMore: false,
        searchPlaceholder: '模糊搜索',
        isTable: true,
        tableAlign: 'left',
        isDetail: false,
        tableFormat: (_value, _row, _item) => {
            const names = (_row.categoryNames || []).map((item) => {
                return `
                <div>
                    ${item}
                </div>`;
            });
            return `
                <div>
                    ${names.join('')}
                </div>
            `;
        }
    },
    {
        label: '内容类型',
        prop: 'type',
        isSearch: true,
        isSearchMore: false,
        searchPlaceholder: '模糊搜索',
        isTable: false,
        isDetail: true,
        domType: 'select',
        domData: [
            {
                label: '图片',
                value: 10
            },
            {
                label: '视频',
                value: 20
            },
            {
                label: '文件',
                value: 30
            }
        ],
        rules: [
            {
                required: true,
                message: '状态必填',
                trigger: 'blur'
            }
        ]
    },
    {
        label: '上传文件',
        prop: 'fileUrl',
        isTable: false,
        isDetail: true,
        domType: 'uploadFile',
        rules: [
            {
                required: true,
                message: '上传文件必填',
                trigger: 'blur'
            }
        ],
        uploadAcceptMap: {
            key: 'type',
            map: {
                10: 'image/*',
                20: 'video/*',
                30: '*'
            }
        }
    },
    {
        label: '内容简介',
        prop: 'description',
        isDetail: true,
        domType: 'editor'
    },
    {
        label: '状态',
        prop: 'status',
        isSearch: true,
        isSearchMore: false,
        searchPlaceholder: '模糊搜索',
        isTable: true,
        isDetail: true,
        domType: 'select',
        domData: [
            {
                label: '上架',
                value: 10
            },
            {
                label: '下架',
                value: 20
            }
        ],
        detailDefault: 20,
        rules: [
            {
                required: true,
                message: '状态必填',
                trigger: 'blur'
            }
        ]
    },
    {
        label: '浏览量',
        prop: 'viewCount',
        isTable: true,
        isDetail: false
    },
    {
        label: '下载量',
        prop: 'downloadCount',
        isTable: true,
        isDetail: false
    },
    {
        label: '创建时间',
        prop: 'createdAt',
        isTable: true,
        domType: 'dateTimePicker'
    },
    {
        label: '创建时间',
        prop: 'createdAtRange',
        isSearch: true,
        isSearchMore: false,
        domType: 'datePickerRange'
    }
]);
</script>

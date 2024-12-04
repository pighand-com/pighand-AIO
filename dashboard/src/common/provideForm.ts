import { ref, Ref, reactive, provide, watch } from 'vue';

/**
 * 表单列配置接口
 * @interface FormColumnsInterface
 */
export interface FormColumnsInterface {
    /** 表单项标签 */
    label: string;
    /** 表单项属性名 */
    prop: string;

    /** 判断唯一键，全部是false则默认为id */
    isPrimaryKey?: boolean;

    /** 默认值 */
    default?: any;
    /** 搜索表单默认值 */
    searchDefault?: any;
    /** 详情表单默认值 */
    detailDefault?: any;

    /** 组件类型 */
    domType?:
        | 'editor'
        | 'input'
        | 'number'
        | 'select'
        | 'checkbox'
        | 'datePicker'
        | 'datePickerRange'
        | 'dateTimePicker'
        | 'radio'
        | 'switch'
        | 'uploadImage'
        | 'uploadImageList'
        | 'uploadFile'
        | 'cascader';

    /**
     * 组件数据源
     * @param key 搜索关键字
     * @param domDataSet 组件数据集合
     * @param formData 表单数据
     * @returns 返回数据源
     */
    domData?:
        | number
        | string
        | Array<any>
        | object
        | ((
              key?: string,
              domDataSet?: any,
              formData?: any
          ) => Promise<number | string | Array<any> | object>);

    /**
     * 组件数据初始化方法
     * @param domData 组件数据
     * @param formData 表单数据
     */
    domDataInit?: (
        domData: number | string | Array<any> | object,
        formData: object
    ) => void;

    /** 占位符 */
    placeholder?: string;
    /** 搜索表单占位符 */
    searchPlaceholder?: string;
    /** 详情表单占位符 */
    detailPlaceholder?: string;

    /** 是否用于搜索 */
    isSearch?: boolean;
    /** 是否在更多搜索中显示 */
    isSearchMore?: boolean;

    /** 是否在表格中显示 */
    isTable?: boolean;
    /** 表格列类型 */
    tableType?: 'input' | 'image' | 'link';
    /** 表格格式化方法 */
    tableFormat?: Function;
    /** 表格列宽度 */
    tableWidth?: string | number;

    /** 是否在详情中显示 */
    isDetail?: boolean | Function;
    /** 是否隐藏 */
    hidden?: boolean;
    /** 是否在新建时显示 */
    isCreate?: boolean;
    /** 是否在更新时显示 */
    isUpdate?: boolean;
    /** 校验规则 */
    rules?: Array<any>;

    /** input组件类型 */
    inputType?: 'text' | 'password';

    /** 上传路径 */
    uploadPath?: string;
    /** 上传文件类型映射 */
    uploadAcceptMap?: {
        /** 映射键名 */
        key: string;
        /** 映射关系 */
        map: {
            [key: string | number]: string;
        };
    };

    /** 组件扩展属性 */
    componentProps?: {
        [key: string]: any;
    };
}

/**
 * 表格数据接口
 * @interface TableDataInterface
 */
export interface TableDataInterface {
    /** 表格数据列表 */
    data: Array<any>;
    /** 数据总条数 */
    totalRow: number;
}

/**
 * 表单提供者接口
 * @interface ProvideFormInterface
 */
export interface ProvideFormInterface {
    /** 表单列配置数组 */
    formColumns: Array<FormColumnsInterface>;
    /** 表单验证规则 */
    formRules: {
        [key: string]: Array<any>;
    };
    /** 表格数据模型 */
    tableDataModel: TableDataInterface;
    /** 搜索表单数据模型 */
    searchFormModel: {
        [key: string]: any;
    };
    /** 详情表单数据模型 */
    detailFormModel: {
        [key: string]: any;
    };
    /** 获取详情操作类型 */
    getDetailOperation: (formModel: any) => {
        op: 'create' | 'update';
        primaryKey: string;
        primaryValue: any;
    };
    /** 查询表格数据 */
    queryTableData: (fun: Function) => Promise<void>;
    /** 表格数据加载状态 */
    isTableDataLoading: Ref<boolean>;
    /** 详情数据加载状态 */
    isDetailDataLoading: Ref<boolean>;
    /** 是否打开详情 */
    isOpenDetail: Ref<boolean>;
    /** 分页大小 */
    pageSize: Ref<number>;
    /** 当前页码 */
    pageNumber: Ref<number>;
    /** 搜索表单默认值 */
    searchDefaultValue: {
        [key: string]: any;
    };
    /** 详情表单默认值 */
    detailDefaultValue: {
        [key: string]: any;
    };
    /** 组件数据集合 */
    domDataSet: {
        [key: string]: any;
    };
    /** 获取组件数据的方法集合 */
    getDomData: {
        [key: string]: () => Promise<void>;
    };
    /** 组件数据加载状态 */
    domDataSetLoading: {
        [key: string]: boolean;
    };
    /** 监听详情表单数据变化 */
    watchDetailForm: (callback: (newVal: any, oldVal: any) => void) => void;
}

/**
 * 提供表单上下文
 * @param formColumns 表单列配置数组
 * @param config 配置对象
 * @param config.defaultValue 表单默认值
 * 
 * @returns {ProvideFormInterface} 返回表单上下文对象,包含表单配置、数据模型、操作方法等
 */
export default function provideForm(
    formColumns: Array<FormColumnsInterface>,
    config?: {
        searchDefaultValue?: Object;
        detailDefaultValue?: Object;
    }
): ProvideFormInterface {
    // 表格数据模型
    const tableDataModel: TableDataInterface = reactive({
        data: [],
        totalRow: 0
    });

    // 搜索表单数据模型
    const searchFormModel: {
        [key: string]: any;
    } = reactive({});

    // 详情表单数据模型
    const detailFormModel: {
        [key: string]: any;
    } = reactive({});

    // 表单验证规则
    const formRules = {};
    // 主键字段名，默认为id
    let primaryKey = 'id';

    // 搜索表单默认值
    const searchDefaultValue = config.searchDefaultValue || {};
    // 详情表单默认值
    const detailDefaultValue = config.detailDefaultValue || {};

    // 组件数据集合
    const domDataSet = reactive({});
    // 获取组件数据的方法集合
    const getDomData = reactive({});
    // 组件数据加载状态
    const domDataSetLoading = reactive({});

    // 处理每个表单列的配置
    formColumns.forEach((item) => {
        const {
            isSearch,
            isDetail,
            label,
            prop,
            rules,
            isPrimaryKey,
            isCreate,
            isUpdate,
            domData
        } = item;

        // 设置主键
        if (isPrimaryKey) {
            primaryKey = prop;
        }

        // 处理下拉选项的默认值和数据源
        if (domData) {
            if (Array.isArray(domData)) {
                domData.forEach((domDataItem) => {
                    if (domDataItem.isDefault) {
                        searchDefaultValue[prop] = domDataItem.value;
                        detailDefaultValue[prop] = domDataItem.value;
                    } else if (domDataItem.isSearchDefault) {
                        searchDefaultValue[prop] = domDataItem.value;
                    } else if (domDataItem.isDetailDefault) {
                        detailDefaultValue[prop] = domDataItem.value;
                    }
                });
            }

            // 处理异步数据源
            if (typeof domData === 'function') {
                domDataSetLoading[label + prop] = false;

                getDomData[label + prop] = async (key: string) => {
                    domDataSetLoading[label + prop] = true;

                    const result = await domData(
                        key,
                        domDataSet,
                        detailFormModel
                    );
                    if (result !== undefined && result !== null) {
                        domDataSet[prop] = result;
                    }

                    domDataSetLoading[label + prop] = false;
                };
            } else {
                // 静态数据源直接赋值
                domDataSet[prop] = domData;
            }
        }

        // 处理搜索表单默认值
        if (isSearch) {
            const value =
                searchDefaultValue[prop] ??
                item.default ??
                item.searchDefault ??
                null;

            if (value !== null) {
                searchDefaultValue[prop] = value;
            }
        }

        // 处理详情表单默认值和验证规则
        if (isDetail) {
            const value =
                detailDefaultValue[prop] ??
                item.default ??
                item.detailDefault ??
                null;

            if (value !== null) {
                detailDefaultValue[prop] = value;
            }

            // 设置表单验证规则
            if (rules && rules.length) {
                formRules[prop] = rules;
            }
        }

        // 设置创建和更新权限
        item.isCreate = isDetail && isCreate === undefined ? true : isCreate;
        item.isUpdate = isDetail && isUpdate === undefined ? true : isUpdate;
    });

    // 获取详情操作类型（创建/更新）
    const getDetailOperation = (
        formModel: any
    ): {
        op: 'create' | 'update';
        primaryKey: string;
        primaryValue: any;
    } => {
        return {
            op: formModel[primaryKey] ? 'update' : 'create',
            primaryKey,
            primaryValue: formModel[primaryKey]
        };
    };

    // 查询表格数据
    const queryTableData = async (fun: Function) => {
        isTableDataLoading.value = true;

        const result = await fun({
            ...searchFormModel,
            pageSize: pageSize.value,
            pageNumber: pageNumber.value
        });

        if (result) {
            tableDataModel.data = result.records;
            tableDataModel.totalRow = result.page.totalRow;
        }

        isTableDataLoading.value = false;
    };

    // 表格数据加载状态
    const isTableDataLoading = ref(false);
    // 详情数据加载状态
    const isDetailDataLoading = ref(false);
    // 是否打开详情弹窗
    const isOpenDetail = ref(false);
    // 分页大小
    const pageSize = ref(10);
    // 当前页码
    const pageNumber = ref(1);

    /** 监听详情表单数据变化 */
    const watchDetailForm = (callback: (newVal: any, oldVal: any) => void) => {
        watch(
            () => detailFormModel,
            (newVal, oldVal) => {
                callback(newVal, oldVal);
            },
            { deep: true }
        );
    };

    // 提供表单上下文给子组件
    provide('provideForm', {
        formColumns,
        formRules,
        tableDataModel,
        searchFormModel,
        detailFormModel,
        getDetailOperation,
        queryTableData,
        isTableDataLoading,
        isDetailDataLoading,
        isOpenDetail,
        pageSize,
        pageNumber,
        searchDefaultValue,
        detailDefaultValue,
        domDataSet,
        getDomData,
        domDataSetLoading,
        watchDetailForm
    });

    // 返回表单上下文
    return {
        formColumns,
        formRules,
        tableDataModel,
        searchFormModel,
        detailFormModel,
        getDetailOperation,
        queryTableData,
        isTableDataLoading,
        isDetailDataLoading,
        isOpenDetail,
        pageSize,
        pageNumber,
        searchDefaultValue,
        detailDefaultValue,
        domDataSet,
        getDomData,
        domDataSetLoading,
        watchDetailForm
    };
}

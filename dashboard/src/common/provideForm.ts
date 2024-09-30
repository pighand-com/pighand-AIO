import { ref, Ref, reactive, provide } from 'vue';

export interface FormColumnsInterface {
    label: string;
    prop: string;

    // 判断唯一键，全部是false则默认为id
    isPrimaryKey?: boolean;

    // 默认值
    default?: any;
    searchDefault?: any;
    detailDefault?: any;

    // 组件类型
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
    domDataInit?: (
        domData: number | string | Array<any> | object,
        formData: object
    ) => void;
    // input组件类型
    inputType?: string;

    placeholder?: string;
    searchPlaceholder?: string;
    detailPlaceholder?: string;

    // search
    isSearch?: boolean;
    isSearchMore?: boolean;

    // table
    isTable?: boolean;
    tableType?: 'input' | 'image' | 'link';
    tableFormat?: Function;
    tableWidth?: string | number;

    // detail
    isDetail?: boolean | Function;
    hidden?: boolean;
    isCreate?: boolean;
    isUpdate?: boolean;
    rules?: Array<any>;

    uploadPath?: string;
    uploadAcceptMap?: {
        key: string;
        map: {
            [key: string | number]: string;
        };
    };
}

export interface TableDataInterface {
    data: Array<any>;
    total: number;
}

export interface ProvideFormInterface {
    formColumns: Array<FormColumnsInterface>;
    formRules: {
        [key: string]: Array<any>;
    };
    tableDataModel: TableDataInterface;
    searchFormModel: {
        [key: string]: any;
    };
    detailFormModel: {
        [key: string]: any;
    };
    getDetailOperation: (formModel: any) => {
        op: 'create' | 'update';
        primaryKey: string;
        primaryValue: any;
    };
    queryTableData: (fun: Function) => Promise<void>;
    isTableDataLoading: Ref<boolean>;
    isDetailDataLoading: Ref<boolean>;
    isOpenDetail: Ref<boolean>;
    pageSize: Ref<number>;
    pageCurrent: Ref<number>;
    searchDefaultValue: {
        [key: string]: any;
    };
    detailDefaultValue: {
        [key: string]: any;
    };
    domDataSet: {
        [key: string]: any;
    };
    getDomData: {
        [key: string]: () => Promise<void>;
    };
    domDataSetLoading: {
        [key: string]: boolean;
    };
}

export default function provideForm(formColumns: Array<FormColumnsInterface>) {
    const tableDataModel: TableDataInterface = reactive({
        data: [],
        total: 0
    });

    const searchFormModel: {
        [key: string]: any;
    } = reactive({});

    const detailFormModel: {
        [key: string]: any;
    } = reactive({});

    const formRules = {};
    let primaryKey = 'id';

    const searchDefaultValue = {};
    const detailDefaultValue = {};

    const domDataSet = reactive({});
    const getDomData = reactive({});
    const domDataSetLoading = reactive({});

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

        if (isPrimaryKey) {
            primaryKey = prop;
        }

        // 下拉默认值。优先default
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
                domDataSet[prop] = domData;
            }
        }

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

        if (isDetail) {
            const value =
                detailDefaultValue[prop] ??
                item.default ??
                item.detailDefault ??
                null;

            if (value !== null) {
                detailDefaultValue[prop] = value;
            }

            if (rules && rules.length) {
                formRules[prop] = rules;
            }
        }

        item.isCreate = isDetail && isCreate === undefined ? true : isCreate;
        item.isUpdate = isDetail && isUpdate === undefined ? true : isUpdate;
    });

    const getDetailOperation = (formModel: any) => {
        return {
            op: formModel[primaryKey] ? 'update' : 'create',
            primaryKey,
            primaryValue: formModel[primaryKey]
        };
    };

    const queryTableData = async (fun: Function) => {
        isTableDataLoading.value = true;

        const result = await fun({
            ...searchFormModel,
            pageSize: pageSize.value,
            pageCurrent: pageCurrent.value
        });

        if (result) {
            tableDataModel.data = result.records;
            tableDataModel.total = result.page.total;
        }

        isTableDataLoading.value = false;
    };

    const isTableDataLoading = ref(false);
    const isDetailDataLoading = ref(false);
    const isOpenDetail = ref(false);
    const pageSize = ref(10);
    const pageCurrent = ref(1);

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
        pageCurrent,
        searchDefaultValue,
        detailDefaultValue,
        domDataSet,
        getDomData,
        domDataSetLoading
    });

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
        pageCurrent,
        searchDefaultValue,
        detailDefaultValue,
        domDataSet,
        getDomData,
        domDataSetLoading
    };
}

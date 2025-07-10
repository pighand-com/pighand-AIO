<template>
    <PDataManager
        :handle-query="distributionGoodsRule.query"
        :handle-find="distributionGoodsRule.find"
        :handle-delete="distributionGoodsRule.del"
        :handle-create="distributionGoodsRule.create"
        :handle-update="distributionGoodsRule.update"
        :handle-format-data="formatData">
        <template #table-column-operation="{ row }"> </template>
    </PDataManager>
</template>

<script lang="ts" setup>
import Decimal from 'decimal.js';
import provideForm from '@/common/provideForm';
import { distributionGoodsRule, ticket, theme } from '@/api';

provideForm([
    {
        label: 'id',
        prop: 'id',
        isDetail: true,
        isPrimaryKey: true,
        hidden: true
    },
    {
        label: '分销商品类型',
        prop: 'objectType',
        isTable: true,
        isDetail: true,
        isSearch: true,
        domType: 'select',
        domData: [
            {
                label: '票务',
                value: 20
            },
            {
                label: '主题',
                value: 10
            }
        ],
        rules: [
            {
                required: true,
                message: '分销商品类型',
                trigger: 'blur'
            }
        ],
        onDetailChange: (_value, _formModel) => {
            _formModel.objectId = '';
        }
    },
    {
        label: '分销商品',
        prop: 'objectId',
        isTable: true,
        isDetail: true,
        domType: 'select',
        domData: async (_key, _domDataSet, _detailFormModel) => {
            const { objectType } = _detailFormModel;

            let result;
            switch (objectType) {
                case 20:
                    const tickets = await ticket.query({ name: _key });
                    result = tickets.records.map((item) => ({
                        label: item.name,
                        value: item.id
                    }));
                    break;
                case 10:
                    const themes = await theme.query({ themeName: _key });
                    result = themes.records.map((item) => ({
                        label: item.themeName,
                        value: item.id
                    }));
                    break;
            }

            return result;
        },
        tableFormat: (_value, _row, _item) => {
            return _row?.objectName || '';
        },
        rules: [
            {
                required: true,
                message: '分销商品ID',
                trigger: 'blur'
            }
        ]
    },
    {
        label: '分成类型',
        prop: 'sharingType',
        isTable: true,
        isDetail: true,
        isSearch: true,
        domType: 'select',
        domData: [
            {
                label: '按比例',
                value: 10
            },
            {
                label: '按固定金额',
                value: 20
            }
        ],
        rules: [
            {
                required: true,
                message: '分成类型',
                trigger: 'blur'
            }
        ]
    },
    {
        label: '分成比例',
        suffix: '%',
        prop: 'sharingRatio',
        isTable: true,
        domType: 'number',
        isDetail: (_detailFormModel) => {
            const { sharingType } = _detailFormModel;

            if (sharingType === 10) {
                return true;
            }

            return false;
        },
        tableFormat: (_value, _row, _item) => {
            return _row?.sharingRatio ? `${_row.sharingRatio} %` : '';
        },
        rules: [
            {
                required: true,
                message: '分成比例',
                trigger: 'blur'
            }
        ]
    },
    {
        label: '分成固定金额',
        prop: 'sharingPrice',
        isTable: true,
        domType: 'number',
        isDetail: (_detailFormModel) => {
            const { sharingType } = _detailFormModel;

            if (sharingType === 20) {
                return true;
            }

            return false;
        },
        tableFormat: (_value, _row, _item) => {
            return _row?.sharingPrice ? `¥ ${_row.sharingPrice}` : '';
        },
        rules: [
            {
                required: true,
                message: '分成固定金额',
                trigger: 'blur'
            }
        ]
    }
]);

const formatData = ({
    queryData,
    saveData
}: {
    queryData: any;
    saveData: any;
}) => {
    if (queryData?.sharingPrice) {
        queryData.sharingPrice = new Decimal(queryData.sharingPrice).div(100);
    }

    if (saveData?.sharingPrice) {
        saveData.sharingPrice = new Decimal(saveData.sharingPrice).mul(100);
    }
};
</script>

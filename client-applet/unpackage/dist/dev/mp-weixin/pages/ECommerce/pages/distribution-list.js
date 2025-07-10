"use strict";
const common_vendor = require("../../../common/vendor.js");
const api_distribution = require("../../../api/distribution.js");
if (!Array) {
  const _easycom_custom_navigation_bar2 = common_vendor.resolveComponent("custom-navigation-bar");
  _easycom_custom_navigation_bar2();
}
const _easycom_custom_navigation_bar = () => "../../../components/custom-navigation-bar/custom-navigation-bar.js";
if (!Math) {
  _easycom_custom_navigation_bar();
}
const _sfc_main = {
  __name: "distribution-list",
  setup(__props) {
    const statistics = common_vendor.ref({});
    const recordsList = common_vendor.ref([]);
    const loading = common_vendor.ref(false);
    const expandedRecords = common_vendor.ref({});
    const recordDetails = common_vendor.ref({});
    const loadingDetails = common_vendor.ref({});
    const statusLabels = {
      frozenAmount: "冻结中",
      incomingAmount: "入账中",
      settledAmount: "待结算",
      withdrawAmount: "已结算",
      refundAmount: "退款"
    };
    const typeLabels = {
      10: "分销",
      20: "结算"
    };
    const getTypeLabel = (type) => {
      return typeLabels[type] || "未知类型";
    };
    const getTypeClass = (type) => {
      const classMap = {
        10: "type-sales",
        20: "type-settlement"
      };
      return classMap[type] || "";
    };
    const getAmountClass = (type) => {
      if (type === 10)
        return "amount-sales";
      if (type === 20)
        return "amount-settlement";
      return "";
    };
    const getRecordAmountClass = (record) => {
      if (record.frozenAmount && record.frozenAmount > 0) {
        return "amount-frozenAmount";
      }
      if (record.incomingAmount && record.incomingAmount > 0) {
        return "amount-incomingAmount";
      }
      if (record.settledAmount && record.settledAmount > 0) {
        return "amount-settledAmount";
      }
      if (record.withdrawAmount && record.withdrawAmount > 0) {
        return "amount-withdrawAmount";
      }
      if (record.refundAmount && record.refundAmount > 0) {
        return "amount-refundAmount";
      }
      return "";
    };
    const formatTime = (timeStr) => {
      if (!timeStr)
        return "-";
      const date = new Date(timeStr);
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, "0")}-${String(date.getDate()).padStart(2, "0")} ${String(date.getHours()).padStart(2, "0")}:${String(date.getMinutes()).padStart(2, "0")}`;
    };
    const formatAmount = (amount) => {
      if (!amount && amount !== 0)
        return "0";
      const result = new common_vendor.Decimal(amount).dividedBy(100);
      return result.modulo(1).equals(0) ? result.toFixed(0) : result.toFixed(2);
    };
    const formatRecordAmount = (record) => {
      const totalAmount = new common_vendor.Decimal(record.frozenAmount || 0).plus(record.settledAmount || 0).plus(record.refundAmount || 0);
      const result = totalAmount.dividedBy(100);
      return result.modulo(1).equals(0) ? result.toFixed(0) : result.toFixed(2);
    };
    const getDetailStatusLabel = (status) => {
      const statusMap = {
        0: "冻结中",
        10: "待结算",
        20: "已结算",
        90: "订单退款"
      };
      return statusMap[status] || "未知状态";
    };
    const getDetailStatusClass = (status) => {
      const classMap = {
        0: "detail-status-frozen",
        10: "detail-status-pending",
        20: "detail-status-settled",
        90: "detail-status-refund"
      };
      return classMap[status] || "";
    };
    const toggleDetail = async (recordId) => {
      const record = recordsList.value.find((r) => r.id === recordId);
      if (record && record.type === 20) {
        return;
      }
      const isExpanded = expandedRecords.value[recordId];
      if (isExpanded) {
        expandedRecords.value[recordId] = false;
      } else {
        expandedRecords.value[recordId] = true;
        if (!recordDetails.value[recordId]) {
          await loadRecordDetail(recordId);
        }
      }
    };
    const loadRecordDetail = async (recordId) => {
      try {
        loadingDetails.value[recordId] = true;
        const res = await api_distribution.distributionAPI.querySalesDetail(recordId);
        recordDetails.value[recordId] = res || [];
      } catch (error) {
        common_vendor.index.__f__("error", "at pages/ECommerce/pages/distribution-list.vue:245", "加载明细失败:", error);
        recordDetails.value[recordId] = [];
      } finally {
        loadingDetails.value[recordId] = false;
      }
    };
    const loadStatistics = async () => {
      const res = await api_distribution.distributionAPI.statistics();
      const defaultStatistics = Object.keys(statusLabels).reduce((acc, key) => {
        acc[key] = 0;
        return acc;
      }, {});
      const processedRes = res ? { ...res } : {};
      if (processedRes.withdrawAmount && processedRes.withdrawAmount < 0) {
        processedRes.withdrawAmount = Math.abs(processedRes.withdrawAmount);
      }
      statistics.value = {
        ...defaultStatistics,
        frozenAmount: processedRes.frozenAmount,
        incomingAmount: processedRes.incomingAmount,
        settledAmount: processedRes.settledAmount,
        withdrawAmount: processedRes.withdrawAmount,
        refundAmount: processedRes.refundAmount
      };
    };
    const loadRecords = async () => {
      loading.value = true;
      const res = await api_distribution.distributionAPI.querySales();
      recordsList.value = (res == null ? void 0 : res.records) || [];
      loading.value = false;
    };
    const initData = async () => {
      await Promise.all([
        loadStatistics(),
        loadRecords()
      ]);
    };
    common_vendor.onLoad(() => {
      initData();
    });
    common_vendor.onShow(() => {
      initData();
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.p({
          back: true,
          position: "flex",
          title: "分销记录"
        }),
        b: common_vendor.f(statistics.value, (value, key, i0) => {
          return {
            a: common_vendor.t(formatAmount(value)),
            b: common_vendor.n(`value-${key}`),
            c: common_vendor.t(statusLabels[key]),
            d: key
          };
        }),
        c: common_vendor.f(recordsList.value, (record, k0, i0) => {
          var _a;
          return common_vendor.e({
            a: common_vendor.t(((_a = record.order) == null ? void 0 : _a.sn) || "-"),
            b: common_vendor.t(formatTime(record.createdAt)),
            c: common_vendor.t(getTypeLabel(record.type)),
            d: common_vendor.n(getTypeClass(record.type)),
            e: common_vendor.t(formatRecordAmount(record)),
            f: common_vendor.n(getAmountClass(record.type)),
            g: common_vendor.n(getRecordAmountClass(record)),
            h: record.type !== 20
          }, record.type !== 20 ? {
            i: common_vendor.t(expandedRecords.value[record.id] ? "收起明细" : "查看明细"),
            j: expandedRecords.value[record.id] ? 1 : "",
            k: common_vendor.o(($event) => toggleDetail(record.id), record.id)
          } : {}, {
            l: expandedRecords.value[record.id] && record.type !== 20
          }, expandedRecords.value[record.id] && record.type !== 20 ? common_vendor.e({
            m: loadingDetails.value[record.id]
          }, loadingDetails.value[record.id] ? {} : recordDetails.value[record.id] && recordDetails.value[record.id].length > 0 ? {
            o: common_vendor.f(recordDetails.value[record.id], (detail, k1, i1) => {
              return common_vendor.e({
                a: common_vendor.t(formatAmount(detail.amount)),
                b: common_vendor.t(getDetailStatusLabel(detail.status)),
                c: common_vendor.n(getDetailStatusClass(detail.status)),
                d: detail.status === 10 && detail.settlementTime
              }, detail.status === 10 && detail.settlementTime ? {
                e: common_vendor.t(formatTime(detail.settlementTime))
              } : {}, {
                f: detail.id
              });
            })
          } : {}, {
            n: recordDetails.value[record.id] && recordDetails.value[record.id].length > 0
          }) : {}, {
            p: record.type === 20 ? 1 : "",
            q: record.id
          });
        }),
        d: recordsList.value.length === 0 && !loading.value
      }, recordsList.value.length === 0 && !loading.value ? {} : {}, {
        e: loading.value
      }, loading.value ? {} : {});
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-61575436"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/ECommerce/pages/distribution-list.js.map

"use strict";
const common_vendor = require("../../../common/vendor.js");
const api_order = require("../../../api/order.js");
if (!Array) {
  const _easycom_custom_navigation_bar2 = common_vendor.resolveComponent("custom-navigation-bar");
  _easycom_custom_navigation_bar2();
}
const _easycom_custom_navigation_bar = () => "../../../components/custom-navigation-bar/custom-navigation-bar.js";
if (!Math) {
  _easycom_custom_navigation_bar();
}
const _sfc_main = {
  __name: "order-list",
  setup(__props) {
    const tabs = [
      { label: "待付款", tradeStatus: 10 },
      { label: "已付款", tradeStatus: 40 },
      { label: "退款/售后", tradeStatus: 51 }
    ];
    const activeTab = common_vendor.ref(0);
    const orderList = common_vendor.ref([]);
    function formatDate(timestamp) {
      if (!timestamp)
        return "";
      const date = new Date(timestamp);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, "0");
      const day = String(date.getDate()).padStart(2, "0");
      const hours = String(date.getHours()).padStart(2, "0");
      const minutes = String(date.getMinutes()).padStart(2, "0");
      return `${year}.${month}.${day} ${hours}:${minutes}`;
    }
    function getTicketQuantityFromMap(order, ticketId) {
      var _a;
      if (!order._ticketQuantityMap) {
        order._ticketQuantityMap = /* @__PURE__ */ new Map();
        (_a = order.orderSku) == null ? void 0 : _a.forEach((sku) => {
          if (sku.ticketId) {
            order._ticketQuantityMap.set(sku.ticketId, sku.quantity || 0);
          }
        });
      }
      return order._ticketQuantityMap.get(ticketId) || 0;
    }
    function fetchOrders() {
      const tradeStatus = tabs[activeTab.value].tradeStatus;
      api_order.orderAPI.query({ tradeStatus }).then((res) => {
        orderList.value = (res == null ? void 0 : res.records) || [];
      });
    }
    function changeTab(idx) {
      activeTab.value = idx;
      fetchOrders();
    }
    function copyOrderSn(orderSn) {
      common_vendor.index.setClipboardData({
        data: orderSn,
        success: () => {
          common_vendor.index.showToast({
            title: "订单编号已复制",
            icon: "success",
            duration: 2e3
          });
        },
        fail: () => {
          common_vendor.index.showToast({
            title: "复制失败",
            icon: "none",
            duration: 2e3
          });
        }
      });
    }
    function goToOrderDetail(id) {
      common_vendor.index.navigateTo({
        url: `/pages/ECommerce/pages/order-detail?id=${id}`
      });
    }
    function formatAmount(amountInCents) {
      if (!amountInCents && amountInCents !== 0)
        return "0.00";
      return new common_vendor.Decimal(amountInCents).div(100).toFixed(2);
    }
    common_vendor.onLoad((options) => {
      if (options && options.tradeStatus) {
        const idx = tabs.findIndex((tab) => String(tab.tradeStatus) === String(options.tradeStatus));
        if (idx !== -1) {
          activeTab.value = idx;
        }
      }
      fetchOrders();
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.p({
          back: true,
          position: "flex",
          title: "我的订单"
        }),
        b: common_vendor.f(tabs, (tab, idx, i0) => {
          return common_vendor.e({
            a: common_vendor.t(tab.label),
            b: idx === activeTab.value
          }, idx === activeTab.value ? {} : {}, {
            c: tab.tradeStatus,
            d: common_vendor.n({
              active: idx === activeTab.value
            }),
            e: common_vendor.o(($event) => changeTab(idx), tab.tradeStatus)
          });
        }),
        c: common_vendor.f(orderList.value, (order, k0, i0) => {
          return common_vendor.e({
            a: common_vendor.t(order.sn),
            b: common_vendor.o(($event) => copyOrderSn(order.sn), order.sn),
            c: common_vendor.t(formatDate(order.createdAt)),
            d: common_vendor.f(order.ticket, (ticket, index, i1) => {
              var _a, _b, _c;
              return common_vendor.e({
                a: (_a = ticket == null ? void 0 : ticket.theme) == null ? void 0 : _a.posterUrl
              }, ((_b = ticket == null ? void 0 : ticket.theme) == null ? void 0 : _b.posterUrl) ? {
                b: (_c = ticket == null ? void 0 : ticket.theme) == null ? void 0 : _c.posterUrl
              } : {}, {
                c: common_vendor.t((ticket == null ? void 0 : ticket.name) || "未知商品"),
                d: common_vendor.t(getTicketQuantityFromMap(order, ticket.id)),
                e: ticket.id + "_" + index
              });
            })
          }, tabs[activeTab.value].tradeStatus === 51 ? {
            e: common_vendor.t(formatAmount(order.refundAmount))
          } : {
            f: common_vendor.t(formatAmount(order.amountPaid))
          }, {
            g: order.sn,
            h: common_vendor.o(($event) => goToOrderDetail(order.id), order.sn)
          });
        }),
        d: tabs[activeTab.value].tradeStatus === 51,
        e: orderList.value.length === 0
      }, orderList.value.length === 0 ? {} : {});
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-ddfe69f5"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/ECommerce/pages/order-list.js.map

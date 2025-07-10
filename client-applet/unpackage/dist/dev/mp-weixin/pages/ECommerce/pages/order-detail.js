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
  __name: "order-detail",
  setup(__props) {
    const orderDetail = common_vendor.ref({});
    const loading = common_vendor.ref(false);
    const isPaying = common_vendor.ref(false);
    const countdownText = common_vendor.ref("");
    let countdownTimer = null;
    const showActionButtons = common_vendor.computed(() => {
      if (orderDetail.value.tradeStatus === 10) {
        if (orderDetail.value.expiredAt) {
          const now = (/* @__PURE__ */ new Date()).getTime();
          const expiredTime = new Date(orderDetail.value.expiredAt).getTime();
          return now < expiredTime;
        }
        return true;
      }
      return false;
    });
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
    function getStatusText(tradeStatus) {
      const statusMap = {
        10: "待付款",
        40: "已付款",
        51: "退款/售后"
      };
      return statusMap[tradeStatus] || "未知状态";
    }
    function getStatusDesc(tradeStatus) {
      const statusMap = {
        10: "请尽快完成支付",
        40: "订单已完成支付",
        51: "订单已退款"
      };
      return statusMap[tradeStatus] || "";
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
    function fetchOrderDetail(id) {
      if (!id)
        return;
      loading.value = true;
      return api_order.orderAPI.find(id).then((res) => {
        orderDetail.value = res || {};
        if (orderDetail.value.tradeStatus === 10) {
          startCountdown();
        }
        return res;
      }).catch((err) => {
        common_vendor.index.__f__("error", "at pages/ECommerce/pages/order-detail.vue:217", "获取订单详情失败:", err);
        common_vendor.index.showToast({
          title: "获取订单详情失败",
          icon: "none",
          duration: 2e3
        });
      }).finally(() => {
        loading.value = false;
      });
    }
    const payOrder = async () => {
      if (isPaying.value || loading.value)
        return;
      isPaying.value = true;
      loading.value = true;
      try {
        const result = await api_order.orderAPI.pay(orderDetail.value.id, "wechat_applet");
        const { nonceStr, prepayId, signType, paySign, timeStamp } = result;
        common_vendor.index.requestPayment({
          provider: "wxpay",
          timeStamp,
          nonceStr,
          package: `prepay_id=${prepayId}`,
          signType,
          paySign,
          success(responseData) {
            isPaying.value = false;
            loading.value = false;
            if (responseData.errMsg === "requestPayment:ok") {
              common_vendor.index.showToast({
                title: "支付成功！",
                icon: "success",
                duration: 2e3
              });
              setTimeout(() => {
                fetchOrderDetail(orderDetail.value.id);
              }, 2e3);
            } else {
              common_vendor.index.showToast({
                title: "支付取消",
                icon: "none",
                duration: 2e3
              });
            }
          },
          fail(error) {
            isPaying.value = false;
            loading.value = false;
            common_vendor.index.showToast({
              title: "支付失败",
              icon: "error",
              duration: 2e3
            });
          }
        });
      } catch (error) {
        isPaying.value = false;
        loading.value = false;
        common_vendor.index.__f__("error", "at pages/ECommerce/pages/order-detail.vue:283", "支付失败:", error);
        common_vendor.index.showToast({
          title: "支付失败，请重试",
          icon: "error",
          duration: 2e3
        });
      }
    };
    function cancelOrder() {
      common_vendor.index.showModal({
        title: "确认取消",
        content: "确定要取消这个订单吗？",
        success: async (res) => {
          if (res.confirm) {
            try {
              loading.value = true;
              await api_order.orderAPI.cancel(orderDetail.value.id);
              common_vendor.index.showToast({
                title: "订单已取消",
                icon: "success",
                duration: 2e3
              });
              setTimeout(() => {
                common_vendor.index.navigateBack();
              }, 2e3);
            } catch (error) {
              common_vendor.index.__f__("error", "at pages/ECommerce/pages/order-detail.vue:314", "取消订单失败:", error);
              common_vendor.index.showToast({
                title: "取消订单失败，请重试",
                icon: "error",
                duration: 2e3
              });
            } finally {
              loading.value = false;
            }
          }
        }
      });
    }
    function startCountdown() {
      if (countdownTimer) {
        clearInterval(countdownTimer);
        countdownTimer = null;
      }
      if (orderDetail.value.tradeStatus !== 10 || !orderDetail.value.expiredAt) {
        countdownText.value = "";
        return;
      }
      const updateCountdown = () => {
        const now = (/* @__PURE__ */ new Date()).getTime();
        const expiredTime = new Date(orderDetail.value.expiredAt).getTime();
        const diff = expiredTime - now;
        if (diff <= 0) {
          countdownText.value = "已过期";
          clearInterval(countdownTimer);
          countdownTimer = null;
          fetchOrderDetail(orderDetail.value.id);
          return;
        }
        const hours = Math.floor(diff / (1e3 * 60 * 60));
        const minutes = Math.floor(diff % (1e3 * 60 * 60) / (1e3 * 60));
        const seconds = Math.floor(diff % (1e3 * 60) / 1e3);
        countdownText.value = `${hours.toString().padStart(2, "0")}:${minutes.toString().padStart(2, "0")}:${seconds.toString().padStart(2, "0")}`;
      };
      updateCountdown();
      countdownTimer = setInterval(updateCountdown, 1e3);
    }
    common_vendor.onLoad((options) => {
      if (options && options.id) {
        fetchOrderDetail(options.id).then(() => {
          startCountdown();
        });
      } else {
        common_vendor.index.showToast({
          title: "订单编号不能为空",
          icon: "none",
          duration: 2e3
        });
        setTimeout(() => {
          common_vendor.index.navigateBack();
        }, 2e3);
      }
    });
    function formatAmount(amountInCents) {
      if (!amountInCents && amountInCents !== 0)
        return "0.00";
      return new common_vendor.Decimal(amountInCents).div(100).toFixed(2);
    }
    common_vendor.onUnmounted(() => {
      if (countdownTimer) {
        clearInterval(countdownTimer);
        countdownTimer = null;
      }
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.p({
          back: true,
          position: "flex",
          title: "订单详情"
        }),
        b: common_vendor.t(getStatusText(orderDetail.value.tradeStatus)),
        c: common_vendor.t(getStatusDesc(orderDetail.value.tradeStatus)),
        d: orderDetail.value.tradeStatus === 10 && orderDetail.value.expiredAt
      }, orderDetail.value.tradeStatus === 10 && orderDetail.value.expiredAt ? {
        e: common_vendor.t(countdownText.value)
      } : {}, {
        f: common_vendor.t(orderDetail.value.sn),
        g: common_vendor.o(($event) => copyOrderSn(orderDetail.value.sn)),
        h: common_vendor.t(formatDate(orderDetail.value.createdAt)),
        i: orderDetail.value.paymentTime
      }, orderDetail.value.paymentTime ? {
        j: common_vendor.t(formatDate(orderDetail.value.paymentTime))
      } : {}, {
        k: common_vendor.f(orderDetail.value.ticket, (ticket, index, i0) => {
          var _a, _b, _c;
          return common_vendor.e({
            a: (_a = ticket == null ? void 0 : ticket.theme) == null ? void 0 : _a.posterUrl
          }, ((_b = ticket == null ? void 0 : ticket.theme) == null ? void 0 : _b.posterUrl) ? {
            b: (_c = ticket == null ? void 0 : ticket.theme) == null ? void 0 : _c.posterUrl
          } : {}, {
            c: common_vendor.t((ticket == null ? void 0 : ticket.name) || "未知商品"),
            d: common_vendor.t(getTicketQuantityFromMap(orderDetail.value, ticket.id)),
            e: ticket.id + "_" + index
          });
        }),
        l: orderDetail.value.tradeStatus === 51
      }, orderDetail.value.tradeStatus === 51 ? {
        m: common_vendor.t(formatAmount(orderDetail.value.amountPaid)),
        n: common_vendor.t(formatAmount(orderDetail.value.refundAmount))
      } : {
        o: common_vendor.t(orderDetail.value.tradeStatus === 10 ? "应付金额" : "实付金额"),
        p: common_vendor.t(formatAmount(orderDetail.value.amountPaid))
      }, {
        q: showActionButtons.value
      }, showActionButtons.value ? common_vendor.e({
        r: orderDetail.value.tradeStatus === 10
      }, orderDetail.value.tradeStatus === 10 ? {
        s: common_vendor.o(payOrder)
      } : {}, {
        t: orderDetail.value.tradeStatus === 10
      }, orderDetail.value.tradeStatus === 10 ? {
        v: common_vendor.o(cancelOrder)
      } : {}) : {});
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-6fc0550b"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/ECommerce/pages/order-detail.js.map

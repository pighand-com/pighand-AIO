"use strict";
const common_vendor = require("../../../common/vendor.js");
const common_storage = require("../../../common/storage.js");
const api_ticket = require("../../../api/ticket.js");
const api_order = require("../../../api/order.js");
if (!Array) {
  const _easycom_custom_navigation_bar2 = common_vendor.resolveComponent("custom-navigation-bar");
  const _easycom_user_check2 = common_vendor.resolveComponent("user-check");
  (_easycom_custom_navigation_bar2 + _easycom_user_check2)();
}
const _easycom_custom_navigation_bar = () => "../../../components/custom-navigation-bar/custom-navigation-bar.js";
const _easycom_user_check = () => "../../../components/user/check.js";
if (!Math) {
  (_easycom_custom_navigation_bar + _easycom_user_check)();
}
const _sfc_main = {
  __name: "ticket-detail",
  setup(__props) {
    const ticketDetail = common_vendor.ref({});
    const quantity = common_vendor.ref(1);
    const totalPrice = common_vendor.ref(0);
    const isPurchasing = common_vendor.ref(false);
    const isLoading = common_vendor.ref(false);
    common_vendor.watch([quantity], () => {
      calculatePrice();
    });
    common_vendor.onLoad((options) => {
      const { id } = options;
      if (id) {
        getTicketDetail(id);
      }
    });
    const getTicketDetail = async (id) => {
      try {
        const res = await api_ticket.ticketAPI.find(id);
        ticketDetail.value = res;
        calculatePrice();
      } catch (error) {
        common_vendor.index.__f__("error", "at pages/ECommerce/pages/ticket-detail.vue:109", "获取票务详情失败:", error);
        common_vendor.index.showToast({
          title: "获取票务详情失败",
          icon: "none"
        });
        setTimeout(() => {
          common_vendor.index.navigateBack();
        }, 1500);
      }
    };
    const formatPrice = (price) => {
      if (!price)
        return "0";
      return new common_vendor.Decimal(price).div(100).toNumber();
    };
    const increaseQuantity = () => {
      quantity.value++;
    };
    const decreaseQuantity = () => {
      if (quantity.value > 1) {
        quantity.value--;
      }
    };
    const calculatePrice = () => {
      if (!ticketDetail.value.currentPrice) {
        totalPrice.value = 0;
        return;
      }
      const price = new common_vendor.Decimal(ticketDetail.value.currentPrice).div(100);
      const calculatedPrice = price.mul(quantity.value).toNumber();
      totalPrice.value = calculatedPrice;
    };
    const purchaseTicket = async () => {
      if (isPurchasing.value || isLoading.value)
        return;
      isPurchasing.value = true;
      isLoading.value = true;
      try {
        const fromSalesId = common_storage.getFromSalesId();
        const orderData = {
          outTradePlatform: "wechat_applet",
          amountPaid: new common_vendor.Decimal(totalPrice.value).mul(100).toNumber(),
          orderSku: [{
            ticketId: ticketDetail.value.id,
            quantity: quantity.value
          }]
        };
        if (fromSalesId) {
          orderData.salespersonId = fromSalesId;
        }
        const result = await api_order.orderAPI.create(orderData);
        const { nonceStr, prepayId, signType, paySign, timeStamp } = result;
        common_vendor.index.requestPayment({
          provider: "wxpay",
          timeStamp,
          nonceStr,
          package: `prepay_id=${prepayId}`,
          signType,
          paySign,
          success(responseData) {
            isPurchasing.value = false;
            isLoading.value = false;
            if (responseData.errMsg === "requestPayment:ok") {
              common_vendor.index.showToast({
                title: "支付成功！",
                icon: "success",
                duration: 2e3
              });
              setTimeout(() => {
                common_vendor.index.navigateTo({
                  url: `/pages/ECommerce/pages/order-list?tradeStatus=40`
                });
              }, 2e3);
            } else {
              common_vendor.index.showToast({
                title: "支付取消",
                icon: "none",
                duration: 2e3
              });
              setTimeout(() => {
                common_vendor.index.navigateTo({
                  url: `/pages/ECommerce/pages/order-list?tradeStatus=10`
                });
              }, 2e3);
            }
          },
          fail(error) {
            isPurchasing.value = false;
            isLoading.value = false;
            common_vendor.index.showToast({
              title: "支付失败",
              icon: "error",
              duration: 2e3
            });
            setTimeout(() => {
              common_vendor.index.navigateTo({
                url: `/pages/ECommerce/pages/order-list?tradeStatus=10`
              });
            }, 2e3);
          }
        });
      } catch (error) {
        isPurchasing.value = false;
        isLoading.value = false;
        common_vendor.index.__f__("error", "at pages/ECommerce/pages/ticket-detail.vue:233", "购票失败:", error);
        common_vendor.index.showToast({
          title: "购票失败，请重试",
          icon: "error",
          duration: 2e3
        });
      }
    };
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.p({
          back: true
        }),
        b: ticketDetail.value.posterUrl
      }, ticketDetail.value.posterUrl ? {
        c: ticketDetail.value.posterUrl
      } : {}, {
        d: ticketDetail.value.name
      }, ticketDetail.value.name ? {
        e: common_vendor.t(ticketDetail.value.name)
      } : {}, {
        f: ticketDetail.value.originalPrice && ticketDetail.value.originalPrice !== ticketDetail.value.currentPrice
      }, ticketDetail.value.originalPrice && ticketDetail.value.originalPrice !== ticketDetail.value.currentPrice ? {
        g: common_vendor.t(formatPrice(ticketDetail.value.originalPrice))
      } : {}, {
        h: common_vendor.t(formatPrice(ticketDetail.value.currentPrice)),
        i: ticketDetail.value.details
      }, ticketDetail.value.details ? {
        j: common_vendor.t(ticketDetail.value.details)
      } : {}, {
        k: common_vendor.o(decreaseQuantity),
        l: common_vendor.t(quantity.value),
        m: common_vendor.o(increaseQuantity),
        n: common_vendor.t(totalPrice.value),
        o: !isPurchasing.value
      }, !isPurchasing.value ? {} : {}, {
        p: isPurchasing.value ? 1 : "",
        q: common_vendor.o(purchaseTicket),
        r: common_vendor.p({
          item: ["login", "phone"]
        }),
        s: isLoading.value
      }, isLoading.value ? {} : {});
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-22bb1c78"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/ECommerce/pages/ticket-detail.js.map

"use strict";
const common_vendor = require("../../../common/vendor.js");
const common_assets = require("../../../common/assets.js");
const common_storage = require("../../../common/storage.js");
const api_theme = require("../../../api/theme.js");
const api_ticket = require("../../../api/ticket.js");
const api_order = require("../../../api/order.js");
if (!Array) {
  const _easycom_custom_navigation_bar2 = common_vendor.resolveComponent("custom-navigation-bar");
  const _easycom_status_bar_gradient2 = common_vendor.resolveComponent("status-bar-gradient");
  const _easycom_user_check2 = common_vendor.resolveComponent("user-check");
  (_easycom_custom_navigation_bar2 + _easycom_status_bar_gradient2 + _easycom_user_check2)();
}
const _easycom_custom_navigation_bar = () => "../../../components/custom-navigation-bar/custom-navigation-bar.js";
const _easycom_status_bar_gradient = () => "../../../components/status-bar-gradient/status-bar-gradient.js";
const _easycom_user_check = () => "../../../components/user/check.js";
if (!Math) {
  (_easycom_custom_navigation_bar + _easycom_status_bar_gradient + _easycom_user_check)();
}
const MAX_DESC_LENGTH = 50;
const _sfc_main = {
  __name: "theme",
  setup(__props) {
    const themeDetail = common_vendor.ref({});
    const isModalVisible = common_vendor.ref(false);
    const isQrModalVisible = common_vendor.ref(false);
    const ticketList = common_vendor.ref([]);
    const selectedTicketIndex = common_vendor.ref(0);
    const quantity = common_vendor.ref(1);
    const totalPrice = common_vendor.ref(0);
    const isPurchasing = common_vendor.ref(false);
    const isLoading = common_vendor.ref(false);
    const showScrollIndicator = common_vendor.ref(false);
    const canScrollDown = common_vendor.ref(false);
    const expandedTickets = common_vendor.ref({});
    common_vendor.watch([quantity, selectedTicketIndex], () => {
      calculatePrice();
    });
    common_vendor.onLoad((options) => {
      const { id } = options;
      if (id) {
        getThemeDetail(id);
      }
    });
    const getThemeDetail = async (id) => {
      try {
        const res = await api_theme.themeAPI.find(id);
        themeDetail.value = res;
      } catch (error) {
        common_vendor.index.__f__("error", "at pages/ECommerce/pages/theme.vue:161", "获取主题详情失败:", error);
      }
    };
    const getTicketList = async () => {
      const res = await api_ticket.ticketAPI.query({ themeId: themeDetail.value.id });
      ticketList.value = res.records || [];
      selectedTicketIndex.value = 0;
      if (ticketList.value.length > 0) {
        calculatePrice();
        checkScrollIndicator();
      }
    };
    const checkScrollIndicator = () => {
      if (ticketList.value.length > 3) {
        showScrollIndicator.value = true;
        canScrollDown.value = true;
        setTimeout(() => {
          showScrollIndicator.value = false;
        }, 3e3);
      }
    };
    const onTicketScroll = (e) => {
      const { scrollTop, scrollHeight, clientHeight } = e.detail;
      if (showScrollIndicator.value) {
        showScrollIndicator.value = false;
      }
      canScrollDown.value = scrollTop + clientHeight < scrollHeight - 10;
    };
    const isTextTruncated = (text) => {
      return text && text.length > MAX_DESC_LENGTH;
    };
    const getDisplayText = (text, index) => {
      if (!text)
        return "";
      if (!isTextTruncated(text))
        return text;
      if (expandedTickets.value[index])
        return text;
      return text.substring(0, MAX_DESC_LENGTH) + "...";
    };
    const toggleExpand = (index) => {
      expandedTickets.value[index] = !expandedTickets.value[index];
    };
    const contactService = () => {
      if (themeDetail.value.serviceQrUrl) {
        isQrModalVisible.value = true;
      } else {
        common_vendor.index.showToast({
          title: "客服暂不可用",
          icon: "none"
        });
      }
    };
    const hideQrModal = () => {
      isQrModalVisible.value = false;
    };
    const showTicketModal = () => {
      isModalVisible.value = true;
      getTicketList();
    };
    const hideModal = () => {
      isModalVisible.value = false;
      expandedTickets.value = {};
    };
    const selectTicket = (index) => {
      selectedTicketIndex.value = index;
      expandedTickets.value = {};
      const ticket = ticketList.value[index];
      if (ticket && isTextTruncated(ticket.details)) {
        expandedTickets.value[index] = true;
      }
    };
    const increaseQuantity = () => {
      quantity.value++;
    };
    const decreaseQuantity = () => {
      if (quantity.value > 1) {
        quantity.value--;
      }
    };
    const calculatePrice = async () => {
      if (ticketList.value.length === 0) {
        totalPrice.value = 0;
        return;
      }
      const selectedTicket = ticketList.value[selectedTicketIndex.value];
      if (!selectedTicket) {
        totalPrice.value = 0;
        return;
      }
      const params = {
        ticketId: selectedTicket.id,
        quantity: quantity.value
      };
      const price = (selectedTicket == null ? void 0 : selectedTicket.currentPrice) ? new common_vendor.Decimal(selectedTicket.currentPrice).div(100) : new common_vendor.Decimal(0);
      const calculatedPrice = price.mul(quantity.value).toNumber();
      totalPrice.value = calculatedPrice;
      common_vendor.index.__f__("log", "at pages/ECommerce/pages/theme.vue:312", "计算价格:", {
        ...params,
        totalPrice: calculatedPrice
      });
    };
    const purchaseTicket = async () => {
      if (isPurchasing.value || isLoading.value)
        return;
      isPurchasing.value = true;
      isLoading.value = true;
      try {
        const selectedTicket = ticketList.value[selectedTicketIndex.value];
        const fromSalesId = common_storage.getFromSalesId();
        const orderData = {
          outTradePlatform: "wechat_applet",
          amountPaid: new common_vendor.Decimal(totalPrice.value).mul(100).toNumber(),
          orderSku: [{
            ticketId: selectedTicket.id,
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
        common_vendor.index.__f__("error", "at pages/ECommerce/pages/theme.vue:403", "购票失败:", error);
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
        b: themeDetail.value.posterUrl
      }, themeDetail.value.posterUrl ? {
        c: themeDetail.value.posterUrl
      } : {}, {
        d: themeDetail.value.title
      }, themeDetail.value.title ? {
        e: common_vendor.t(themeDetail.value.title)
      } : {}, {
        f: themeDetail.value.pictureDescription
      }, themeDetail.value.pictureDescription ? {
        g: themeDetail.value.pictureDescription
      } : {}, {
        h: themeDetail.value.serviceQrUrl
      }, themeDetail.value.serviceQrUrl ? {
        i: common_assets._imports_0$1,
        j: common_vendor.o(contactService)
      } : {}, {
        k: common_vendor.o(showTicketModal),
        l: common_vendor.p({
          item: ["login", "phone"]
        }),
        m: isModalVisible.value
      }, isModalVisible.value ? common_vendor.e({
        n: common_vendor.f(ticketList.value, (ticket, index, i0) => {
          return common_vendor.e({
            a: common_vendor.t(ticket.name),
            b: common_vendor.t(ticket.currentPrice ? new common_vendor.unref(common_vendor.Decimal)(ticket.currentPrice).div(100).toNumber() : 0),
            c: common_vendor.t(getDisplayText(ticket.details, index)),
            d: expandedTickets.value[index] ? 1 : "",
            e: isTextTruncated(ticket.details)
          }, isTextTruncated(ticket.details) ? {
            f: common_vendor.t(expandedTickets.value[index] ? "收起" : "展开"),
            g: common_vendor.o(($event) => toggleExpand(index), ticket.id)
          } : {}, {
            h: ticket.id,
            i: selectedTicketIndex.value === index ? 1 : "",
            j: common_vendor.o(($event) => selectTicket(index), ticket.id)
          });
        }),
        o: common_vendor.o(onTicketScroll),
        p: showScrollIndicator.value
      }, showScrollIndicator.value ? {} : {}, {
        q: canScrollDown.value
      }, canScrollDown.value ? {} : {}, {
        r: common_vendor.o(decreaseQuantity),
        s: common_vendor.t(quantity.value),
        t: common_vendor.o(increaseQuantity),
        v: common_vendor.t(totalPrice.value),
        w: !isPurchasing.value
      }, !isPurchasing.value ? {} : {}, {
        x: isPurchasing.value ? 1 : "",
        y: common_vendor.o(purchaseTicket),
        z: common_vendor.o(() => {
        }),
        A: common_vendor.o(hideModal)
      }) : {}, {
        B: isQrModalVisible.value
      }, isQrModalVisible.value ? {
        C: themeDetail.value.serviceQrUrl,
        D: common_vendor.o(() => {
        }),
        E: common_vendor.o(hideQrModal)
      } : {}, {
        F: isLoading.value
      }, isLoading.value ? {} : {});
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-8c51e834"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/ECommerce/pages/theme.js.map

"use strict";
const common_vendor = require("../../../common/vendor.js");
const api_ticket = require("../../../api/ticket.js");
if (!Array) {
  const _easycom_custom_navigation_bar2 = common_vendor.resolveComponent("custom-navigation-bar");
  const _easycom_tab_bar2 = common_vendor.resolveComponent("tab-bar");
  (_easycom_custom_navigation_bar2 + _easycom_tab_bar2)();
}
const _easycom_custom_navigation_bar = () => "../../../components/custom-navigation-bar/custom-navigation-bar.js";
const _easycom_tab_bar = () => "../../../components/tab-bar/tab-bar.js";
if (!Math) {
  (_easycom_custom_navigation_bar + _easycom_tab_bar)();
}
const _sfc_main = {
  __name: "ticket-list",
  setup(__props) {
    const ticketList = common_vendor.ref([]);
    const isLoading = common_vendor.ref(false);
    common_vendor.onLoad((options) => {
      getTicketList();
    });
    const getTicketList = async () => {
      isLoading.value = true;
      try {
        const res = await api_ticket.ticketAPI.query({});
        ticketList.value = res.records || [];
      } catch (error) {
        common_vendor.index.__f__("error", "at pages/ECommerce/pages/ticket-list.vue:77", "获取票务列表失败:", error);
        common_vendor.index.showToast({
          title: "获取票务列表失败",
          icon: "none"
        });
      } finally {
        isLoading.value = false;
      }
    };
    const formatPrice = (price) => {
      if (!price)
        return "0";
      return new common_vendor.Decimal(price).div(100).toNumber();
    };
    const goToDetail = (ticketId) => {
      common_vendor.index.navigateTo({
        url: `/pages/ECommerce/pages/ticket-detail?id=${ticketId}`
      });
    };
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.p({
          position: "flex",
          title: "票务列表"
        }),
        b: common_vendor.f(ticketList.value, (ticket, k0, i0) => {
          return common_vendor.e({
            a: ticket.posterUrl
          }, ticket.posterUrl ? {
            b: ticket.posterUrl
          } : {}, {
            c: common_vendor.t(ticket.name),
            d: ticket.originalPrice && ticket.originalPrice !== ticket.currentPrice
          }, ticket.originalPrice && ticket.originalPrice !== ticket.currentPrice ? {
            e: common_vendor.t(formatPrice(ticket.originalPrice))
          } : {}, {
            f: common_vendor.t(formatPrice(ticket.currentPrice)),
            g: common_vendor.o(($event) => goToDetail(ticket.id), ticket.id),
            h: ticket.id
          });
        }),
        c: ticketList.value.length === 0 && !isLoading.value
      }, ticketList.value.length === 0 && !isLoading.value ? {} : {}, {
        d: isLoading.value
      }, isLoading.value ? {} : {});
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-db976eb8"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/ECommerce/pages/ticket-list.js.map

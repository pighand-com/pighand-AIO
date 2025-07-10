"use strict";
const common_vendor = require("../../../common/vendor.js");
const api_ticket = require("../../../api/ticket.js");
if (!Array) {
  const _easycom_custom_navigation_bar2 = common_vendor.resolveComponent("custom-navigation-bar");
  _easycom_custom_navigation_bar2();
}
const _easycom_custom_navigation_bar = () => "../../../components/custom-navigation-bar/custom-navigation-bar.js";
if (!Math) {
  _easycom_custom_navigation_bar();
}
const _sfc_main = {
  __name: "mine-ticket",
  setup(__props) {
    const ticketList = common_vendor.ref([]);
    const queryMineTickets = async () => {
      const params = {
        usable: true
      };
      const response = await api_ticket.ticketAPI.queryMine(params);
      if (response && response.records) {
        ticketList.value = response.records;
      }
    };
    common_vendor.onLoad(() => {
      queryMineTickets();
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.p({
          back: true,
          position: "flex",
          title: "我的票务"
        }),
        b: common_vendor.f(ticketList.value, (ticket, k0, i0) => {
          var _a, _b;
          return {
            a: (_a = ticket == null ? void 0 : ticket.theme) == null ? void 0 : _a.posterUrl,
            b: common_vendor.t((_b = ticket == null ? void 0 : ticket.ticket) == null ? void 0 : _b.name),
            c: common_vendor.t(ticket.remainingValidationCount),
            d: ticket.id
          };
        }),
        c: ticketList.value.length === 0
      }, ticketList.value.length === 0 ? {} : {});
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-ed614dfe"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/ECommerce/pages/mine-ticket.js.map

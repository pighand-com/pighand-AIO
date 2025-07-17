"use strict";
const common_vendor = require("../../../common/vendor.js");
const common_assets = require("../../../common/assets.js");
const _sfc_main = {
  name: "CustomerService",
  data() {
    return {};
  },
  methods: {}
};
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
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: common_vendor.p({
      title: "客服"
    }),
    b: common_assets._imports_0$2
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-05bf6414"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/base/pages/customer-service.js.map

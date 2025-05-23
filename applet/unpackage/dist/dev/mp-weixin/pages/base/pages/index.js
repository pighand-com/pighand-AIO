"use strict";
const common_vendor = require("../../../common/vendor.js");
const common_assets = require("../../../common/assets.js");
if (!Array) {
  const _easycom_user_check2 = common_vendor.resolveComponent("user-check");
  const _easycom_tab_bar2 = common_vendor.resolveComponent("tab-bar");
  (_easycom_user_check2 + _easycom_tab_bar2)();
}
const _easycom_user_check = () => "../../../components/user/check.js";
const _easycom_tab_bar = () => "../../../components/tab-bar/tab-bar.js";
if (!Math) {
  (_easycom_user_check + _easycom_tab_bar)();
}
const _sfc_main = {
  __name: "index",
  setup(__props) {
    function test() {
      common_vendor.index.__f__("log", "at pages/base/pages/index.vue:19", 11111);
    }
    return (_ctx, _cache) => {
      return {
        a: common_assets._imports_0,
        b: common_vendor.o(test)
      };
    };
  }
};
wx.createPage(_sfc_main);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/base/pages/index.js.map

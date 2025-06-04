"use strict";
const common_vendor = require("../../../common/vendor.js");
const common_assets = require("../../../common/assets.js");
const common_storage = require("../../../common/storage.js");
if (!Array) {
  const _easycom_custom_navigation_bar2 = common_vendor.resolveComponent("custom-navigation-bar");
  const _easycom_user_check2 = common_vendor.resolveComponent("user-check");
  const _easycom_tab_bar2 = common_vendor.resolveComponent("tab-bar");
  (_easycom_custom_navigation_bar2 + _easycom_user_check2 + _easycom_tab_bar2)();
}
const _easycom_custom_navigation_bar = () => "../../../components/custom-navigation-bar/custom-navigation-bar.js";
const _easycom_user_check = () => "../../../components/user/check.js";
const _easycom_tab_bar = () => "../../../components/tab-bar/tab-bar.js";
if (!Math) {
  (_easycom_custom_navigation_bar + _easycom_user_check + _easycom_tab_bar)();
}
const _sfc_main = {
  __name: "mine",
  setup(__props) {
    const userInfo = common_vendor.ref(common_storage.getUserInfo());
    const bgImageExists = common_vendor.ref(true);
    common_vendor.index.$on("storage-changed", () => {
      userInfo.value = common_storage.getUserInfo();
    });
    function onBgImageError() {
      bgImageExists.value = false;
    }
    function goToOrder() {
      common_vendor.index.navigateTo({
        url: "/pages/order/index"
      });
    }
    return (_ctx, _cache) => {
      var _a, _b;
      return common_vendor.e({
        a: bgImageExists.value
      }, bgImageExists.value ? {
        b: common_assets._imports_0$1,
        c: common_vendor.o(onBgImageError)
      } : {}, {
        d: ((_a = userInfo.value) == null ? void 0 : _a.avatar) || common_vendor.unref(common_assets.defaultAvatar),
        e: common_vendor.p({
          item: ["login", "avatar"]
        }),
        f: common_vendor.t(((_b = userInfo.value) == null ? void 0 : _b.phone) || "获取手机号"),
        g: common_vendor.p({
          item: ["login", "phone"]
        }),
        h: common_vendor.o(goToOrder),
        i: common_vendor.p({
          item: ["login"]
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-0a65c2d0"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/base/pages/mine.js.map

"use strict";
const common_vendor = require("../../common/vendor.js");
const common_login = require("../../common/login.js");
const common_storage = require("../../common/storage.js");
const _sfc_main = {
  __name: "login",
  setup(__props) {
    const isLogin = common_storage.getToken() && props.item.includes("login");
    function beginLogin() {
      common_login.login();
    }
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: !common_vendor.unref(isLogin)
      }, !common_vendor.unref(isLogin) ? {
        b: common_vendor.o(beginLogin)
      } : {});
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-ac254fa3"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/components/user/login.js.map

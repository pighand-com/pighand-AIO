"use strict";
const common_vendor = require("../../common/vendor.js");
const common_login = require("../../common/login.js");
const common_storage = require("../../common/storage.js");
const _sfc_main = {
  __name: "login",
  props: {
    item: {
      type: Array,
      default: () => ["login"]
    }
  },
  setup(__props) {
    const props = __props;
    const token = common_vendor.ref(common_storage.getToken());
    const handleStorageChange = () => {
      token.value = common_storage.getToken();
    };
    common_vendor.index.$on("storage-changed", handleStorageChange);
    common_vendor.onUnmounted(() => {
      common_vendor.index.$off("storage-changed", handleStorageChange);
    });
    const isLogin = common_vendor.computed(() => token.value && props.item.includes("login"));
    function beginLogin() {
      common_login.login();
    }
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: !isLogin.value
      }, !isLogin.value ? {
        b: common_vendor.o(beginLogin)
      } : {});
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-ac254fa3"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/components/user/login.js.map

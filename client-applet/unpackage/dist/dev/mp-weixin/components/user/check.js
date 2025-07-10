"use strict";
const common_vendor = require("../../common/vendor.js");
const common_storage = require("../../common/storage.js");
if (!Array) {
  const _easycom_user_login2 = common_vendor.resolveComponent("user-login");
  const _easycom_user_phone2 = common_vendor.resolveComponent("user-phone");
  const _easycom_user_avatar2 = common_vendor.resolveComponent("user-avatar");
  (_easycom_user_login2 + _easycom_user_phone2 + _easycom_user_avatar2)();
}
const _easycom_user_login = () => "./login.js";
const _easycom_user_phone = () => "./phone.js";
const _easycom_user_avatar = () => "./avatar.js";
if (!Math) {
  (_easycom_user_login + _easycom_user_phone + _easycom_user_avatar)();
}
const _sfc_main = {
  __name: "check",
  props: {
    item: {
      type: Array,
      default: () => ["login", "phone", "avatar"]
    }
  },
  setup(__props) {
    const token = common_vendor.ref(common_storage.getToken());
    const userInfo = common_vendor.ref(common_storage.getUserInfo());
    const props = __props;
    const handleStorageChange = () => {
      token.value = common_storage.getToken();
      userInfo.value = common_storage.getUserInfo();
    };
    common_vendor.index.$on("storage-changed", handleStorageChange);
    common_vendor.onUnmounted(() => {
      common_vendor.index.$off("storage-changed", handleStorageChange);
    });
    const needLogin = common_vendor.computed(() => (!token.value || !(userInfo == null ? void 0 : userInfo.value)) && props.item.includes("login"));
    const needPhone = common_vendor.computed(() => {
      var _a;
      return !((_a = userInfo.value) == null ? void 0 : _a.phone) && props.item.includes("phone");
    });
    const needAvatar = common_vendor.computed(() => {
      var _a;
      return !((_a = userInfo.value) == null ? void 0 : _a.avatar) && props.item.includes("avatar");
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: needLogin.value
      }, needLogin.value ? {} : needPhone.value ? {
        c: common_vendor.t(userInfo.value.phone)
      } : needAvatar.value ? {
        e: common_vendor.t(userInfo.value.avatar)
      } : {}, {
        b: needPhone.value,
        d: needAvatar.value
      });
    };
  }
};
wx.createComponent(_sfc_main);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/components/user/check.js.map

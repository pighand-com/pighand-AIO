"use strict";
const common_vendor = require("../../common/vendor.js");
const api_user = require("../../api/user.js");
const common_storage = require("../../common/storage.js");
const _sfc_main = {
  __name: "phone",
  setup(__props) {
    const phoneRef = common_vendor.ref(common_storage.getUserInfo().phone);
    const handleStorageChange = () => {
      phoneRef.value = common_storage.getUserInfo().phone;
    };
    common_vendor.index.$on("storage-changed", handleStorageChange);
    common_vendor.onUnmounted(() => {
      common_vendor.index.$off("storage-changed", handleStorageChange);
    });
    const finishGetPhoneNumber = async (params) => {
      common_vendor.index.showLoading({
        title: "绑定手机号中..."
      });
      const { detail: { errMsg, code } } = params;
      if (errMsg === "getPhoneNumber:fail user deny") {
        return;
      }
      const userInfo = await api_user.user.bindPhoneWechat(code);
      common_storage.setUserInfo({
        ...common_storage.getUserInfo(),
        phone: userInfo.phone
      });
      common_vendor.index.hideLoading();
    };
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: !phoneRef.value
      }, !phoneRef.value ? {
        b: common_vendor.o(finishGetPhoneNumber)
      } : {});
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-9e489cb3"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/components/user/phone.js.map

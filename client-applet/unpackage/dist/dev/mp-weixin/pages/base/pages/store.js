"use strict";
const common_vendor = require("../../../common/vendor.js");
const api_store = require("../../../api/store.js");
if (!Array) {
  const _easycom_custom_navigation_bar2 = common_vendor.resolveComponent("custom-navigation-bar");
  _easycom_custom_navigation_bar2();
}
const _easycom_custom_navigation_bar = () => "../../../components/custom-navigation-bar/custom-navigation-bar.js";
if (!Math) {
  _easycom_custom_navigation_bar();
}
const _sfc_main = {
  __name: "store",
  setup(__props) {
    const storeDetail = common_vendor.ref({});
    common_vendor.onLoad((options) => {
      const { id } = options;
      if (id) {
        getStoreDetail(id);
      }
    });
    const getStoreDetail = async (id) => {
      try {
        const res = await api_store.storeAPI.find(id);
        storeDetail.value = res;
      } catch (error) {
        common_vendor.index.__f__("error", "at pages/base/pages/store.vue:32", "获取详情失败:", error);
      }
    };
    return (_ctx, _cache) => {
      return {
        a: common_vendor.p({
          back: true
        }),
        b: storeDetail.value.introduce
      };
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-30addfe8"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/base/pages/store.js.map

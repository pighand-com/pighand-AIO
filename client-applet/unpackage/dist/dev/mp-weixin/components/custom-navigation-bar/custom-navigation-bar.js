"use strict";
const common_vendor = require("../../common/vendor.js");
const common_assets = require("../../common/assets.js");
const _sfc_main = {
  __name: "custom-navigation-bar",
  props: {
    title: {
      type: String,
      default: ""
    },
    back: {
      type: Boolean,
      default: false
    }
  },
  setup(__props) {
    const navigationBarHeight = common_vendor.ref(0);
    const statusBarHeight = common_vendor.ref(0);
    common_vendor.onMounted(() => {
      const systemInfo = common_vendor.index.getSystemInfoSync();
      statusBarHeight.value = systemInfo.statusBarHeight;
      const MenuButton = common_vendor.wx$1.getMenuButtonBoundingClientRect();
      navigationBarHeight.value = MenuButton.height + (MenuButton.top - statusBarHeight.value) * 2;
    });
    const onBack = () => {
      let pages = getCurrentPages();
      if (pages.length > 1) {
        common_vendor.index.navigateBack({
          delta: -1
        });
      } else {
        common_vendor.index.switchTab({
          url: "/pages/base/pages/index"
        });
      }
    };
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.s("height: " + statusBarHeight.value + "px"),
        b: __props.title
      }, __props.title ? {
        c: common_vendor.t(__props.title),
        d: common_vendor.s("height: " + navigationBarHeight.value + "px; line-height: " + navigationBarHeight.value + "px;")
      } : {}, {
        e: __props.back
      }, __props.back ? {
        f: common_assets._imports_0$2,
        g: common_vendor.o(onBack)
      } : {}, {
        h: common_vendor.s("position: relative; height: " + (navigationBarHeight.value + statusBarHeight.value) + "px")
      });
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-78fe8cb5"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/components/custom-navigation-bar/custom-navigation-bar.js.map

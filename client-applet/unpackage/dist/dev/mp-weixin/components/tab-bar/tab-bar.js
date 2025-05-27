"use strict";
const common_vendor = require("../../common/vendor.js");
if (!Array) {
  const _easycom_tui_tabbar2 = common_vendor.resolveComponent("tui-tabbar");
  _easycom_tui_tabbar2();
}
const _easycom_tui_tabbar = () => "../thorui/tui-tabbar/tui-tabbar.js";
if (!Math) {
  _easycom_tui_tabbar();
}
const _sfc_main = {
  __name: "tab-bar",
  props: {
    current: {
      type: Number,
      default: 0
    },
    tabBar: {
      type: Array,
      default: () => [
        {
          pagePath: "/pages/tabbar/index/index",
          text: "code",
          iconPath: "/static/images/tabbar/code_gray.png",
          selectedIconPath: "/static/images/tabbar/code_active.png"
        }
      ]
    }
  },
  setup(__props) {
    const props = __props;
    const tabbarSwitch = (e) => {
      props.current = e.index;
    };
    return (_ctx, _cache) => {
      return {
        a: common_vendor.o(tabbarSwitch),
        b: common_vendor.p({
          current: __props.current,
          backdropFilter: true,
          backgroundColor: "#f8f8f8",
          tabBar: __props.tabBar,
          color: "#777",
          selectedColor: "#AC9157"
        })
      };
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-ffb3232c"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/components/tab-bar/tab-bar.js.map

"use strict";
const common_vendor = require("../../common/vendor.js");
const pages = [
  {
    path: "pages/base/pages/index"
  },
  {
    path: "pages/base/pages/mine"
  }
];
const globalStyle = {
  navigationBarTextStyle: "black",
  navigationBarTitleText: "uni-app",
  navigationStyle: "custom",
  navigationBarBackgroundColor: "#F8F8F8",
  backgroundColor: "#F8F8F8"
};
const tabBar = {
  color: "#777",
  selectedColor: "#AC9157",
  backgroundColor: "#f8f8f8",
  list: [
    {
      pagePath: "pages/base/pages/index",
      text: "index",
      iconPath: "static/logo.png",
      selectedIconPath: "static/logo.png"
    },
    {
      pagePath: "pages/base/pages/mine",
      text: "mine",
      iconPath: "static/logo.png",
      selectedIconPath: "static/logo.png"
    }
  ]
};
const uniIdRouter = {};
const easycom = {
  autoscan: true,
  custom: {
    "tui-(.*)": "@/components/thorui/tui-$1/tui-$1.vue",
    "user-(.*)": "@/components/user/$1.vue",
    "tab-bar": "@/components/tab-bar/tab-bar",
    "custom-navigation-bar": "@/components/custom-navigation-bar/custom-navigation-bar"
  }
};
const pageJson = {
  pages,
  globalStyle,
  tabBar,
  uniIdRouter,
  easycom
};
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
  setup(__props) {
    var _a;
    const tabBarConfig = common_vendor.ref((((_a = pageJson == null ? void 0 : pageJson.tabBar) == null ? void 0 : _a.list) || []).map((item) => {
      return {
        pagePath: "/" + item.pagePath,
        // 页面路径
        text: item.text,
        // 显示文本
        iconPath: "/" + item.iconPath,
        // 未选中图标
        selectedIconPath: "/" + item.selectedIconPath
        // 选中图标
      };
    }));
    const tabBarCurrent = common_vendor.ref(0);
    const updateTabBarCurrent = () => {
      const currentPages = getCurrentPages();
      const currentPage = currentPages[currentPages.length - 1];
      const currentPath = "/" + currentPage.route;
      const index = tabBarConfig.value.findIndex((item) => item.pagePath === currentPath);
      if (index > -1) {
        tabBarCurrent.value = index;
      }
    };
    common_vendor.onMounted(() => {
      updateTabBarCurrent();
      common_vendor.index.addInterceptor("switchTab", {
        invoke: (args) => {
          const targetIndex = tabBarConfig.value.findIndex((item) => item.pagePath === args.url);
          if (targetIndex > -1) {
            tabBarCurrent.value = targetIndex;
          }
        },
        success: () => updateTabBarCurrent()
      });
    });
    const tabbarSwitch = (e) => {
      tabBarCurrent.value = e.index;
      common_vendor.index.switchTab({
        url: tabBarConfig.value[e.index].pagePath
      });
    };
    return (_ctx, _cache) => {
      return {
        a: common_vendor.o(tabbarSwitch),
        b: common_vendor.p({
          current: tabBarCurrent.value,
          tabBar: tabBarConfig.value,
          selectedColor: "#AC9157"
        })
      };
    };
  }
};
wx.createComponent(_sfc_main);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/components/tab-bar/tab-bar.js.map

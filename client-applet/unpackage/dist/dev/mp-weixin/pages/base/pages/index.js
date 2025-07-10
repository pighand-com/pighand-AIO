"use strict";
const common_vendor = require("../../../common/vendor.js");
const common_storage = require("../../../common/storage.js");
const api_theme = require("../../../api/theme.js");
const api_banner = require("../../../api/banner.js");
if (!Array) {
  const _easycom_custom_button2 = common_vendor.resolveComponent("custom-button");
  const _easycom_uni_popup2 = common_vendor.resolveComponent("uni-popup");
  const _easycom_tab_bar2 = common_vendor.resolveComponent("tab-bar");
  (_easycom_custom_button2 + _easycom_uni_popup2 + _easycom_tab_bar2)();
}
const _easycom_custom_button = () => "../../../components/custom-button/custom-button.js";
const _easycom_uni_popup = () => "../../../uni_modules/uni-popup/components/uni-popup/uni-popup.js";
const _easycom_tab_bar = () => "../../../components/tab-bar/tab-bar.js";
if (!Math) {
  (_easycom_custom_button + _easycom_uni_popup + _easycom_tab_bar)();
}
const _sfc_main = {
  __name: "index",
  setup(__props) {
    const bannerList = common_vendor.ref([]);
    const themeList = common_vendor.ref([]);
    const queuePopup = common_vendor.ref(null);
    const formatQueueDuration = (minutes) => {
      if (minutes >= 60) {
        const hours = Math.floor(minutes / 60);
        const remainingMinutes = minutes % 60;
        if (remainingMinutes === 0) {
          return `${hours}小时`;
        } else {
          return `${hours}小时${remainingMinutes}分钟`;
        }
      } else {
        return `${minutes}分钟`;
      }
    };
    const getBannerList = async () => {
      try {
        const res = await api_banner.bannerAPI.query();
        if (res && res.records) {
          bannerList.value = res.records;
        }
      } catch (error) {
        common_vendor.index.__f__("error", "at pages/base/pages/index.vue:91", "获取banner列表失败:", error);
      }
    };
    const getThemeList = async () => {
      try {
        const res = await api_theme.themeAPI.query();
        if (res && res.records) {
          themeList.value = res.records;
        }
      } catch (error) {
        common_vendor.index.__f__("error", "at pages/base/pages/index.vue:103", "获取主题列表失败:", error);
      }
    };
    const handleBuyNow = () => {
      const themeId = themeList.value[0].id;
      common_vendor.index.navigateTo({
        url: `/pages/ECommerce/pages/theme?id=${themeId}`
      });
    };
    const showQueuePopup = async () => {
      await getThemeList();
      queuePopup.value.open();
    };
    common_vendor.onLoad((options) => {
      const { scene = "" } = options;
      const sales = scene && decodeURIComponent(scene).split("=")[1];
      if (sales) {
        common_storage.setFromSalesId(sales);
      }
      getBannerList();
      getThemeList();
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: bannerList.value.length > 0
      }, bannerList.value.length > 0 ? {
        b: common_vendor.f(bannerList.value, (banner, index, i0) => {
          return {
            a: banner.imageUrl || banner.image,
            b: index
          };
        })
      } : {}, {
        c: common_vendor.o(handleBuyNow),
        d: common_vendor.p({
          text: "立即购买",
          width: "360rpx",
          fontSize: "48rpx"
        }),
        e: common_vendor.o(showQueuePopup),
        f: common_vendor.p({
          text: "查看排队时间",
          width: "280rpx",
          fontSize: "32rpx"
        }),
        g: themeList.value.length === 0
      }, themeList.value.length === 0 ? {} : {
        h: common_vendor.f(themeList.value, (theme, index, i0) => {
          return {
            a: common_vendor.t(theme.themeName || theme.name || "未知主题"),
            b: common_vendor.t(formatQueueDuration(theme.queueDuration || 0)),
            c: index
          };
        })
      }, {
        i: common_vendor.sr(queuePopup, "3a6cd336-2", {
          "k": "queuePopup"
        }),
        j: common_vendor.p({
          type: "center",
          ["mask-click"]: true
        })
      });
    };
  }
};
wx.createPage(_sfc_main);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/base/pages/index.js.map

"use strict";
const common_vendor = require("../../../common/vendor.js");
const api_theme = require("../../../api/theme.js");
if (!Array) {
  const _easycom_custom_navigation_bar2 = common_vendor.resolveComponent("custom-navigation-bar");
  const _easycom_uni_popup2 = common_vendor.resolveComponent("uni-popup");
  (_easycom_custom_navigation_bar2 + _easycom_uni_popup2)();
}
const _easycom_custom_navigation_bar = () => "../../../components/custom-navigation-bar/custom-navigation-bar.js";
const _easycom_uni_popup = () => "../../../uni_modules/uni-popup/components/uni-popup/uni-popup.js";
if (!Math) {
  (_easycom_custom_navigation_bar + _easycom_uni_popup)();
}
const _sfc_main = {
  __name: "queue-setting",
  setup(__props) {
    const themeList = common_vendor.ref([]);
    const currentTheme = common_vendor.ref(null);
    const inputQueueDuration = common_vendor.ref("");
    const queuePopup = common_vendor.ref(null);
    const queryThemes = async () => {
      try {
        const response = await api_theme.themeAPI.query();
        if (response && response.records) {
          themeList.value = response.records;
        } else if (response && Array.isArray(response)) {
          themeList.value = response;
        }
      } catch (error) {
        common_vendor.index.__f__("error", "at pages/ECommerce/pages/queue-setting.vue:78", "查询主题列表失败:", error);
        common_vendor.index.showToast({
          title: "查询失败",
          icon: "none"
        });
      }
    };
    const openQueueSetting = (theme) => {
      currentTheme.value = theme;
      inputQueueDuration.value = theme.queueDuration || "";
      queuePopup.value.open();
    };
    const closePopup = () => {
      queuePopup.value.close();
      currentTheme.value = null;
      inputQueueDuration.value = "";
    };
    const submitQueueDuration = async () => {
      if (!inputQueueDuration.value || inputQueueDuration.value < 0) {
        common_vendor.index.showToast({
          title: "请输入有效的排队时间",
          icon: "none"
        });
        return;
      }
      try {
        common_vendor.index.showLoading({
          title: "设置中..."
        });
        await api_theme.themeAPI.updateQueueDuration(currentTheme.value.id, Number(inputQueueDuration.value));
        const themeIndex = themeList.value.findIndex((item) => item.id === currentTheme.value.id);
        if (themeIndex !== -1) {
          themeList.value[themeIndex].queueDuration = Number(inputQueueDuration.value);
        }
        common_vendor.index.hideLoading();
        common_vendor.index.showToast({
          title: "设置成功",
          icon: "success"
        });
        closePopup();
      } catch (error) {
        common_vendor.index.hideLoading();
        common_vendor.index.__f__("error", "at pages/ECommerce/pages/queue-setting.vue:132", "设置排队时间失败:", error);
        common_vendor.index.showToast({
          title: "设置失败",
          icon: "none"
        });
      }
    };
    common_vendor.onLoad(() => {
      queryThemes();
    });
    return (_ctx, _cache) => {
      var _a;
      return common_vendor.e({
        a: common_vendor.p({
          back: true,
          position: "flex",
          title: "排队设置"
        }),
        b: common_vendor.f(themeList.value, (theme, k0, i0) => {
          return {
            a: theme.posterUrl,
            b: common_vendor.t(theme.themeName),
            c: common_vendor.t(theme.queueDuration || 0),
            d: theme.id,
            e: common_vendor.o(($event) => openQueueSetting(theme), theme.id)
          };
        }),
        c: themeList.value.length === 0
      }, themeList.value.length === 0 ? {} : {}, {
        d: common_vendor.t((_a = currentTheme.value) == null ? void 0 : _a.themeName),
        e: inputQueueDuration.value,
        f: common_vendor.o(($event) => inputQueueDuration.value = $event.detail.value),
        g: common_vendor.o(closePopup),
        h: common_vendor.o(submitQueueDuration),
        i: common_vendor.sr(queuePopup, "0f25ed10-1", {
          "k": "queuePopup"
        }),
        j: common_vendor.p({
          type: "center"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-0f25ed10"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/ECommerce/pages/queue-setting.js.map

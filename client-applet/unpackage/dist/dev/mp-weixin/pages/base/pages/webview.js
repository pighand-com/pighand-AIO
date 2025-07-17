"use strict";
const common_vendor = require("../../../common/vendor.js");
if (!Array) {
  const _easycom_custom_navigation_bar2 = common_vendor.resolveComponent("custom-navigation-bar");
  const _easycom_uni_load_more2 = common_vendor.resolveComponent("uni-load-more");
  const _easycom_custom_button2 = common_vendor.resolveComponent("custom-button");
  (_easycom_custom_navigation_bar2 + _easycom_uni_load_more2 + _easycom_custom_button2)();
}
const _easycom_custom_navigation_bar = () => "../../../components/custom-navigation-bar/custom-navigation-bar.js";
const _easycom_uni_load_more = () => "../../../uni_modules/uni-load-more/components/uni-load-more/uni-load-more.js";
const _easycom_custom_button = () => "../../../components/custom-button/custom-button.js";
if (!Math) {
  (_easycom_custom_navigation_bar + _easycom_uni_load_more + _easycom_custom_button)();
}
const _sfc_main = {
  __name: "webview",
  setup(__props) {
    const webviewUrl = common_vendor.ref("");
    const pageTitle = common_vendor.ref("网页浏览");
    const isLoading = common_vendor.ref(false);
    const hasError = common_vendor.ref(false);
    const errorMessage = common_vendor.ref("");
    const loadingText = common_vendor.ref({
      contentdown: "上拉显示更多",
      contentrefresh: "正在加载...",
      contentnomore: "没有更多数据了"
    });
    const handleMessage = (event) => {
      const data = event.detail.data;
      if (data && data.length > 0) {
        const message = data[0];
        switch (message.type) {
          case "title":
            pageTitle.value = message.title || "网页浏览";
            break;
          case "close":
            common_vendor.index.navigateBack();
            break;
          default:
            common_vendor.index.__f__("log", "at pages/base/pages/webview.vue:72", "未知消息类型:", message);
        }
      }
    };
    const handleError = (event) => {
      common_vendor.index.__f__("error", "at pages/base/pages/webview.vue:78", "WebView加载错误:", event);
      isLoading.value = false;
      hasError.value = true;
      errorMessage.value = "网页加载失败，请检查网络连接或稍后重试";
    };
    const handleLoad = (event) => {
      isLoading.value = false;
      hasError.value = false;
    };
    const handleLoading = (event) => {
      isLoading.value = true;
      hasError.value = false;
    };
    const reloadWebview = () => {
      hasError.value = false;
      isLoading.value = true;
      const currentUrl = webviewUrl.value;
      webviewUrl.value = "";
      setTimeout(() => {
        webviewUrl.value = currentUrl;
      }, 100);
    };
    common_vendor.onLoad((options) => {
      if (options.url) {
        webviewUrl.value = decodeURIComponent(options.url);
      }
      if (options.title) {
        pageTitle.value = decodeURIComponent(options.title);
      }
      if (!webviewUrl.value) {
        hasError.value = true;
        errorMessage.value = "缺少必要的URL参数";
        return;
      }
      isLoading.value = true;
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.p({
          position: "flex",
          title: pageTitle.value
        }),
        b: webviewUrl.value,
        c: common_vendor.o(handleMessage),
        d: common_vendor.o(handleError),
        e: common_vendor.o(handleLoad),
        f: common_vendor.o(handleLoading),
        g: isLoading.value
      }, isLoading.value ? {
        h: common_vendor.p({
          status: "loading",
          ["content-text"]: loadingText.value
        })
      } : {}, {
        i: hasError.value
      }, hasError.value ? {
        j: common_vendor.t(errorMessage.value),
        k: common_vendor.o(reloadWebview),
        l: common_vendor.p({
          text: "重新加载",
          width: "200rpx",
          fontSize: "28rpx"
        })
      } : {});
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-c9d3bf4f"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/base/pages/webview.js.map

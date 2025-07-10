"use strict";
Object.defineProperty(exports, Symbol.toStringTag, { value: "Module" });
const common_vendor = require("./common/vendor.js");
if (!Math) {
  "./pages/base/pages/index.js";
  "./pages/base/pages/mine.js";
  "./pages/ECommerce/pages/theme.js";
  "./pages/ECommerce/pages/order-list.js";
  "./pages/ECommerce/pages/order-detail.js";
  "./pages/ECommerce/pages/mine-ticket.js";
  "./pages/ECommerce/pages/distribution-list.js";
  "./pages/ECommerce/pages/queue-setting.js";
}
const _sfc_main = {
  onLaunch: function() {
    common_vendor.index.hideTabBar();
  },
  onShow: function() {
    common_vendor.index.__f__("log", "at App.vue:11", "App Show");
  },
  onHide: function() {
    common_vendor.index.__f__("log", "at App.vue:14", "App Hide");
  }
};
function createApp() {
  const app = common_vendor.createSSRApp(_sfc_main);
  return {
    app
  };
}
createApp().app.mount("#app");
exports.createApp = createApp;
//# sourceMappingURL=../.sourcemap/mp-weixin/app.js.map

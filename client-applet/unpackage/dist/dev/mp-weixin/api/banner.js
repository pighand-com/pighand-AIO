"use strict";
const common_request = require("../common/request.js");
const bannerAPI = {
  /**
   * 列表
   */
  query: async () => await common_request.request.get(`banner`)
};
exports.bannerAPI = bannerAPI;
//# sourceMappingURL=../../.sourcemap/mp-weixin/api/banner.js.map

"use strict";
const common_request = require("../common/request.js");
const storeAPI = {
  /**
   * 获取详情
   */
  find: async (id) => await common_request.request.get(`store/${id}`)
};
exports.storeAPI = storeAPI;
//# sourceMappingURL=../../.sourcemap/mp-weixin/api/store.js.map

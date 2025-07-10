"use strict";
const common_request = require("../common/request.js");
const themeAPI = {
  /**
   * 列表
   */
  query: async () => await common_request.request.get(`theme`),
  /**
   * 详情
   * @param id 
   * @returns 
   */
  find: async (id) => await common_request.request.get(`theme/${id}`),
  /**
   * 修改排队时间
   * @param id 
   * @returns 
   */
  updateQueueDuration: async (id, queueDuration) => await common_request.request.put(`theme/${id}/queue_duration`, {
    queueDuration
  })
};
exports.themeAPI = themeAPI;
//# sourceMappingURL=../../.sourcemap/mp-weixin/api/theme.js.map

"use strict";
const common_request = require("../common/request.js");
const ticketAPI = {
  /**
   * 列表
   */
  query: async (params) => await common_request.request.get(`ticket`, params),
  /**
   * 我的列表
   */
  queryMine: async (params) => await common_request.request.get(`ticket/mine`, params),
  /**
   * 获取详情
   */
  find: async (id) => await common_request.request.get(`ticket/${id}`)
};
exports.ticketAPI = ticketAPI;
//# sourceMappingURL=../../.sourcemap/mp-weixin/api/ticket.js.map

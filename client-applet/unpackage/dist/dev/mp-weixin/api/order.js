"use strict";
const common_request = require("../common/request.js");
const orderAPI = {
  /**
   * 下单
   */
  create: async (params) => await common_request.request.post(`order`, params),
  /**
   * 订单支付
   */
  pay: async (id, outTradePlatform) => await common_request.request.post(`order/${id}/pay`, { outTradePlatform }),
  /**
   * 列表
   */
  query: async (params) => await common_request.request.get(`order`, params),
  /**
   * 详情
   */
  find: async (id) => await common_request.request.get(`order/${id}`),
  /**
   * 取消订单
   */
  cancel: async (id) => await common_request.request.post(`order/${id}/cancel`)
};
exports.orderAPI = orderAPI;
//# sourceMappingURL=../../.sourcemap/mp-weixin/api/order.js.map

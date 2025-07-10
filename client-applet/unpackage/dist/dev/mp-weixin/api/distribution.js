"use strict";
const common_request = require("../common/request.js");
const distributionAPI = {
  /**
   * 查询分销资格
   */
  salesperson: async () => await common_request.request.get(`distribution/salesperson`),
  /**
   * 分销二维码
   */
  QRCode: async () => await common_request.request.get(`distribution/salesperson/qr_code`),
  /**
   * 分销统计
   */
  statistics: async () => await common_request.request.get(`distribution/sales/statistics`),
  /**
   * 分销记录
   */
  querySales: async () => await common_request.request.get(`distribution/sales`),
  /**
   * 分销记录明细
   */
  querySalesDetail: async (id) => await common_request.request.get(`distribution/sales/${id}/detail`)
};
exports.distributionAPI = distributionAPI;
//# sourceMappingURL=../../.sourcemap/mp-weixin/api/distribution.js.map

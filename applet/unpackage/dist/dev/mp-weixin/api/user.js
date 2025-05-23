"use strict";
const common_request = require("../common/request.js");
const user = {
  /**
   * 绑定微信手机号
   * @param code 微信授权码
   */
  bindPhoneWechat: async (code) => await common_request.request.post(
    `user/bind/phone/wechat`,
    { code }
  ),
  /**
   * 修改自己的信息
   */
  updateSelf: async (data) => {
    await common_request.request.put(
      `user/self`,
      data
    );
  }
};
exports.user = user;
//# sourceMappingURL=../../.sourcemap/mp-weixin/api/user.js.map

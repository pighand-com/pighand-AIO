"use strict";
const common_request = require("../common/request.js");
const api = {
  login: async (code) => await common_request.request.post(
    `login/wechat/applet`,
    { code }
  )
};
exports.api = api;
//# sourceMappingURL=../../.sourcemap/mp-weixin/api/login.js.map

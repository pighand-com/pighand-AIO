"use strict";
const common_vendor = require("./vendor.js");
const common_storage = require("./storage.js");
const api_login = require("../api/login.js");
const login = async (options = {}) => {
  const token = common_storage.getToken();
  if (token) {
    return;
  }
  common_vendor.index.showLoading({
    title: "登录中..."
  });
  try {
    const loginRes = await new Promise((resolve, reject) => {
      common_vendor.index.login({
        success: (res2) => resolve(res2),
        fail: (err) => reject(err)
      });
    });
    const { code } = loginRes;
    const res = await api_login.api.login(code);
    if (!res) {
      return;
    }
    const { token: token2, application, ...userInfo } = res;
    common_storage.setToken(token2);
    common_storage.setUserInfo(userInfo);
    common_storage.setApplicationInfo(application);
    if (!options.silent) {
      common_vendor.index.showToast({
        title: "登录成功",
        icon: "success"
      });
    }
  } catch (error) {
    if (!options.silent) {
      common_vendor.index.showToast({
        title: "登录失败",
        icon: "error"
      });
    }
  }
};
exports.login = login;
//# sourceMappingURL=../../.sourcemap/mp-weixin/common/login.js.map

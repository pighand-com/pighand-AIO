"use strict";
const common_vendor = require("./vendor.js");
const common_constant = require("./constant.js");
const common_storage = require("./storage.js");
const getUrl = (url) => {
  return common_constant.constant.api[__wxConfig.envVersion] + url;
};
const disposeResponse = (response, isDialog = true, blob = false) => {
  const { statusCode, data } = response;
  let errorMsg = "";
  let isSuccess = true;
  if (statusCode === 200 && data.code && data.code !== 200) {
    isSuccess = false;
    errorMsg = data.msg || data.error;
  } else if (statusCode === 401) {
    common_storage.clearAll();
    common_vendor.index.showToast({
      title: "请先登录",
      icon: "error"
    });
    return;
  } else if (statusCode !== 200) {
    isSuccess = false;
    errorMsg = data;
  }
  let result = {
    data: data.data,
    error: errorMsg
  };
  if (isDialog) {
    if (!isSuccess) {
      common_vendor.index.showToast({
        title: errorMsg,
        icon: "error"
      });
      throw new Error(errorMsg);
    }
    result = data.data;
  }
  return result;
};
const request = async (method, url, data, config) => {
  url = getUrl(url);
  const requestConfig = {
    url,
    method,
    data,
    header: {
      "X-Application-Id": common_constant.constant.APPLICATION_ID,
      ...(config == null ? void 0 : config.headers) || {},
      "Authorization": common_storage.getToken()
    },
    timeout: 6e4
  };
  try {
    const response = await common_vendor.index.request(requestConfig);
    return disposeResponse(response, config == null ? void 0 : config.isDialog, (config == null ? void 0 : config.responseType) === "blob");
  } catch (error) {
    common_vendor.index.showToast({
      title: "网络请求失败：" + error,
      icon: "none"
    });
    throw error;
  }
};
const get = (url, params, config) => {
  return request("GET", url, params, config);
};
const post = (url, data, config) => {
  return request("POST", url, data, config);
};
const put = (url, data, config) => {
  return request("PUT", url, data, config);
};
const del = (url, params, config) => {
  return request("DELETE", url, params, config);
};
const request$1 = {
  get,
  post,
  put,
  del
};
exports.request = request$1;
//# sourceMappingURL=../../.sourcemap/mp-weixin/common/request.js.map

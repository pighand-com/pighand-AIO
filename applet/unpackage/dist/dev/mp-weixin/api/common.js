"use strict";
const common_vendor = require("../common/vendor.js");
const common_request = require("../common/request.js");
const common = {
  // 获取验证码
  getCAPTCHACode: async (key) => await common_request.request.get(`CAPTCHA/code`, { key }),
  // 获取上传url
  getUploadUrls: async (params) => await common_request.request.post(`upload/url`, params),
  // 上传到server
  uploadToServer: async (file, onUploadProgress) => {
    const formData = new FormData();
    formData.append("files", file);
    return await common_request.request.post(`upload/server`, formData, {
      headers: {
        "Content-Type": "multipart/form-data"
      },
      onUploadProgress: (progressEvent) => {
        if (onUploadProgress) {
          const percentCompleted = Math.round(
            progressEvent.loaded * 100 / progressEvent.total
          );
          onUploadProgress(percentCompleted);
        }
      }
    });
  },
  // 上传到COS
  uploadCOS: async (file, contentType, uploadUrl, headers = {}, onUploadProgress) => {
    try {
      await common_vendor.index.request({
        url: uploadUrl,
        method: "PUT",
        data: file,
        header: {
          "Content-Type": contentType,
          ...headers
        }
      });
    } catch (error) {
      common_vendor.index.__f__("log", "at api/common.ts:64", error);
      common_vendor.index.showToast({
        title: `文件上传失败：${error}`,
        icon: "none"
      });
    }
  }
};
exports.common = common;
//# sourceMappingURL=../../.sourcemap/mp-weixin/api/common.js.map

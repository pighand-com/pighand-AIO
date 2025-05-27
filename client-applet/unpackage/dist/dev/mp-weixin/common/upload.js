"use strict";
const common_vendor = require("./vendor.js");
const common_storage = require("./storage.js");
const api_common = require("../api/common.js");
const upload = async (file, fileExt, contentType, uploadPath, uploadProgress) => {
  let applicationInfo = common_storage.getApplicationInfo();
  applicationInfo = {
    id: "1",
    name: "",
    uploadType: "tencent_cloud_cos"
  };
  if (applicationInfo.uploadType === "system") {
    const fileUrls = await api_common.common.uploadToServer(file, uploadProgress);
    return fileUrls[0];
  } else {
    const uploadUrlInfo = await api_common.common.getUploadUrls([{
      extension: fileExt,
      path: uploadPath
    }]);
    if (!uploadUrlInfo || uploadUrlInfo.urls.length === 0) {
      return;
    }
    for (const uploadUrl of uploadUrlInfo.urls) {
      switch (applicationInfo.uploadType) {
        case "tencent_cloud_cos":
          await api_common.common.uploadCOS(file, contentType, uploadUrl.uploadUrl, uploadUrlInfo.headers, uploadProgress);
          return uploadUrl.url;
        default:
          common_vendor.index.showToast({
            title: "暂不支持当前上传类型",
            icon: "none"
          });
          break;
      }
    }
  }
};
exports.upload = upload;
//# sourceMappingURL=../../.sourcemap/mp-weixin/common/upload.js.map

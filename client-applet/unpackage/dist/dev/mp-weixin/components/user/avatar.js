"use strict";
const common_vendor = require("../../common/vendor.js");
const common_storage = require("../../common/storage.js");
const common_upload = require("../../common/upload.js");
const api_user = require("../../api/user.js");
const _sfc_main = {
  __name: "avatar",
  setup(__props) {
    const { avatar } = common_storage.getUserInfo();
    const finishChooseAvatar = async (params) => {
      common_vendor.index.showLoading({
        title: "头像上传中"
      });
      const filePath = params.detail.avatarUrl;
      const fileExt = filePath.split(".")[1];
      const contentType = "image/jpeg";
      const fileBuffer = common_vendor.index.getFileSystemManager().readFileSync(filePath);
      const url = await common_upload.upload(fileBuffer, fileExt, contentType, "avatar");
      await api_user.user.updateSelf({
        extension: {
          profile: url
        }
      });
      common_storage.setUserInfo({
        ...common_storage.getUserInfo(),
        profile: url
      });
      common_vendor.index.hideLoading();
    };
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: !common_vendor.unref(avatar)
      }, !common_vendor.unref(avatar) ? {
        b: common_vendor.o(finishChooseAvatar)
      } : {});
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-f245d64b"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/components/user/avatar.js.map

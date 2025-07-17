"use strict";
const common_vendor = require("../../../common/vendor.js");
const common_assets = require("../../../common/assets.js");
const common_storage = require("../../../common/storage.js");
const api_distribution = require("../../../api/distribution.js");
if (!Array) {
  const _easycom_custom_navigation_bar2 = common_vendor.resolveComponent("custom-navigation-bar");
  const _easycom_user_check2 = common_vendor.resolveComponent("user-check");
  const _easycom_tab_bar2 = common_vendor.resolveComponent("tab-bar");
  (_easycom_custom_navigation_bar2 + _easycom_user_check2 + _easycom_tab_bar2)();
}
const _easycom_custom_navigation_bar = () => "../../../components/custom-navigation-bar/custom-navigation-bar.js";
const _easycom_user_check = () => "../../../components/user/check.js";
const _easycom_tab_bar = () => "../../../components/tab-bar/tab-bar.js";
if (!Math) {
  (_easycom_custom_navigation_bar + _easycom_user_check + _easycom_tab_bar)();
}
const _sfc_main = {
  __name: "mine",
  setup(__props) {
    const isLogin = common_vendor.ref(!!common_storage.getToken());
    const userInfo = common_vendor.ref(common_storage.getUserInfo());
    const salespersonId = common_vendor.ref(common_storage.getSalespersonId());
    const isQrModalVisible = common_vendor.ref(false);
    const distributionQrCode = common_vendor.ref("");
    const isSettingsModalVisible = common_vendor.ref(false);
    const isContactModalVisible = common_vendor.ref(false);
    const isStaff = common_vendor.ref(false);
    const checkIsStaff = async () => {
      const user = userInfo.value;
      isStaff.value = (user == null ? void 0 : user.roles) && Array.isArray(user.roles) && user.roles.some((role) => role.roleId == 9e3);
    };
    const checkAndSetSalespersonId = async () => {
      const token = common_storage.getToken();
      if (!token) {
        salespersonId.value = null;
        return;
      }
      const currentSalespersonId = common_storage.getSalespersonId();
      if (currentSalespersonId) {
        salespersonId.value = currentSalespersonId;
        return;
      }
      try {
        const response = await api_distribution.distributionAPI.salesperson();
        if (response) {
          common_storage.setSalespersonId(response);
          salespersonId.value = response;
        }
      } catch (error) {
        common_vendor.index.__f__("log", "at pages/base/pages/mine.vue:188", "获取销售员ID失败:", error);
      }
    };
    const handleStorageChange = () => {
      userInfo.value = common_storage.getUserInfo();
      isLogin.value = !!common_storage.getToken();
      checkIsStaff();
      checkAndSetSalespersonId();
    };
    common_vendor.onShow(() => {
      handleStorageChange();
    });
    common_vendor.index.$on("storage-changed", handleStorageChange);
    common_vendor.onUnmounted(() => {
      common_vendor.index.$off("storage-changed", handleStorageChange);
    });
    function goToOrder() {
      common_vendor.index.navigateTo({
        url: "/pages/ECommerce/pages/order-list"
      });
    }
    function goToTicket() {
      common_vendor.index.navigateTo({
        url: "/pages/ECommerce/pages/mine-ticket"
      });
    }
    function goToDistribution() {
      common_vendor.index.navigateTo({
        url: "/pages/ECommerce/pages/distribution-list"
      });
    }
    function goToQueueSettings() {
      common_vendor.index.navigateTo({
        url: "/pages/ECommerce/pages/queue-setting"
      });
    }
    const showDistributionQR = async () => {
      try {
        const response = await api_distribution.distributionAPI.QRCode();
        if (response) {
          distributionQrCode.value = `data:image/png;base64,${response}`;
          isQrModalVisible.value = true;
        } else {
          common_vendor.index.showToast({
            title: "获取二维码失败",
            icon: "none"
          });
        }
      } catch (error) {
        common_vendor.index.__f__("log", "at pages/base/pages/mine.vue:256", "获取分销二维码失败:", error);
        common_vendor.index.showToast({
          title: "获取二维码失败",
          icon: "none"
        });
      }
    };
    const hideQrModal = () => {
      isQrModalVisible.value = false;
    };
    const showSettings = () => {
      isSettingsModalVisible.value = true;
    };
    const hideSettings = () => {
      isSettingsModalVisible.value = false;
    };
    const logout = () => {
      common_vendor.index.showModal({
        title: "确认退出",
        content: "确定要退出登录吗？",
        success: (res) => {
          if (res.confirm) {
            common_storage.clearAll();
            hideSettings();
            common_vendor.index.showToast({
              title: "已退出登录",
              icon: "success"
            });
          }
        }
      });
    };
    const showContactModal = () => {
      isContactModalVisible.value = true;
    };
    const hideContactModal = () => {
      isContactModalVisible.value = false;
    };
    const makePhoneCall = (type) => {
      hideContactModal();
      common_vendor.index.makePhoneCall({
        phoneNumber: "18182400663",
        success: () => {
          common_vendor.index.__f__("log", "at pages/base/pages/mine.vue:313", `${type}电话拨打成功`);
        },
        fail: (err) => {
          common_vendor.index.__f__("log", "at pages/base/pages/mine.vue:316", `${type}电话拨打失败:`, err);
          common_vendor.index.showToast({
            title: "拨打失败",
            icon: "none"
          });
        }
      });
    };
    return (_ctx, _cache) => {
      var _a, _b;
      return common_vendor.e({
        a: common_vendor.p({
          back: false,
          position: "flex"
        }),
        b: ((_a = userInfo.value) == null ? void 0 : _a.avatar) || common_vendor.unref(common_assets.defaultAvatar),
        c: common_vendor.p({
          item: ["login", "avatar"]
        }),
        d: common_vendor.t(((_b = userInfo.value) == null ? void 0 : _b.phone) || "获取手机号"),
        e: common_vendor.p({
          item: ["login", "phone"]
        }),
        f: salespersonId.value
      }, salespersonId.value ? {
        g: common_assets._imports_0$1,
        h: common_vendor.o(showDistributionQR)
      } : {}, {
        i: isLogin.value
      }, isLogin.value ? {
        j: common_assets._imports_1$1,
        k: common_vendor.o(showSettings)
      } : {}, {
        l: common_vendor.o(goToOrder),
        m: common_vendor.p({
          item: ["login"]
        }),
        n: common_vendor.o(goToTicket),
        o: common_vendor.p({
          item: ["login"]
        }),
        p: salespersonId.value
      }, salespersonId.value ? {
        q: common_vendor.o(goToDistribution),
        r: common_vendor.p({
          item: ["login"]
        })
      } : {}, {
        s: isStaff.value
      }, isStaff.value ? {
        t: common_vendor.o(goToQueueSettings),
        v: common_vendor.p({
          item: ["login"]
        })
      } : {}, {
        w: common_assets._imports_2$1,
        x: common_vendor.o(showContactModal),
        y: isQrModalVisible.value
      }, isQrModalVisible.value ? {
        z: distributionQrCode.value,
        A: common_vendor.o(() => {
        }),
        B: common_vendor.o(hideQrModal)
      } : {}, {
        C: isSettingsModalVisible.value
      }, isSettingsModalVisible.value ? {
        D: common_vendor.o(logout),
        E: common_vendor.o(() => {
        }),
        F: common_vendor.o(hideSettings)
      } : {}, {
        G: isContactModalVisible.value
      }, isContactModalVisible.value ? {
        H: common_assets._imports_2$1,
        I: common_vendor.o(($event) => makePhoneCall("投诉反馈")),
        J: common_assets._imports_2$1,
        K: common_vendor.o(($event) => makePhoneCall("品牌合作")),
        L: common_vendor.o(() => {
        }),
        M: common_vendor.o(hideContactModal)
      } : {});
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-0a65c2d0"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/base/pages/mine.js.map

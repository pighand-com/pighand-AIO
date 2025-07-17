"use strict";
const common_vendor = require("../../../common/vendor.js");
const common_assets = require("../../../common/assets.js");
const common_storage = require("../../../common/storage.js");
const api_theme = require("../../../api/theme.js");
const api_banner = require("../../../api/banner.js");
const api_ticket = require("../../../api/ticket.js");
if (!Array) {
  const _easycom_custom_navigation_bar2 = common_vendor.resolveComponent("custom-navigation-bar");
  const _easycom_uni_popup2 = common_vendor.resolveComponent("uni-popup");
  const _easycom_tab_bar2 = common_vendor.resolveComponent("tab-bar");
  (_easycom_custom_navigation_bar2 + _easycom_uni_popup2 + _easycom_tab_bar2)();
}
const _easycom_custom_navigation_bar = () => "../../../components/custom-navigation-bar/custom-navigation-bar.js";
const _easycom_uni_popup = () => "../../../uni_modules/uni-popup/components/uni-popup/uni-popup.js";
const _easycom_tab_bar = () => "../../../components/tab-bar/tab-bar.js";
if (!Math) {
  (_easycom_custom_navigation_bar + _easycom_uni_popup + _easycom_tab_bar)();
}
const _sfc_main = {
  __name: "index",
  setup(__props) {
    const bannerList = common_vendor.ref([]);
    const themeList = common_vendor.ref([]);
    const ticketList = common_vendor.ref([]);
    const activityList = common_vendor.ref([]);
    const queuePopup = common_vendor.ref(null);
    const displayTickets = common_vendor.computed(() => {
      return ticketList.value.slice(2, 4);
    });
    const formatQueueDuration = (minutes) => {
      if (minutes >= 60) {
        const hours = Math.floor(minutes / 60);
        const remainingMinutes = minutes % 60;
        if (remainingMinutes === 0) {
          return `${hours}小时`;
        } else {
          return `${hours}小时${remainingMinutes}分钟`;
        }
      } else {
        return `${minutes}分钟`;
      }
    };
    const formatPrice = (price) => {
      if (!price)
        return "0";
      return new common_vendor.Decimal(price).div(100).toNumber();
    };
    const getBannerList = async () => {
      try {
        const res = await api_banner.bannerAPI.query({ group: "banner" });
        if (res && res.records) {
          bannerList.value = res.records.filter((item) => item.group === "banner");
        }
      } catch (error) {
        common_vendor.index.__f__("error", "at pages/base/pages/index.vue:205", "获取banner列表失败:", error);
      }
    };
    const getActivityList = async () => {
      try {
        const res = await api_banner.bannerAPI.query({ group: "activity" });
        if (res && res.records) {
          activityList.value = res.records.filter((item) => item.group === "activity");
        }
      } catch (error) {
        common_vendor.index.__f__("error", "at pages/base/pages/index.vue:217", "获取活动列表失败:", error);
      }
    };
    const getTicketList = async () => {
      try {
        const res = await api_ticket.ticketAPI.query({});
        if (res && res.records) {
          ticketList.value = res.records;
        }
      } catch (error) {
        common_vendor.index.__f__("error", "at pages/base/pages/index.vue:229", "获取门票列表失败:", error);
      }
    };
    const getThemeList = async () => {
      try {
        const res = await api_theme.themeAPI.query();
        if (res && res.records) {
          themeList.value = res.records;
        }
      } catch (error) {
        common_vendor.index.__f__("error", "at pages/base/pages/index.vue:241", "获取主题列表失败:", error);
      }
    };
    const goToStore = () => {
      common_vendor.index.navigateTo({
        url: "/pages/base/pages/store?id=1"
      });
    };
    const showGameTip = () => {
      common_vendor.index.showToast({
        title: "即将开放，敬请期待",
        icon: "none"
      });
    };
    const goToTicketDetail = (ticketId) => {
      common_vendor.index.navigateTo({
        url: `/pages/ECommerce/pages/ticket-detail?id=${ticketId}`
      });
    };
    const goToTicketList = () => {
      common_vendor.index.switchTab({
        url: "/pages/ECommerce/pages/ticket-list"
      });
    };
    const showQueuePopup = async () => {
      await getThemeList();
      queuePopup.value.open();
    };
    const handleBannerClick = (banner) => {
      if (!banner.redirectionPath) {
        return;
      }
      const redirectionPath = banner.redirectionPath;
      if (redirectionPath.startsWith("http")) {
        const title = banner.title || banner.name || "网页浏览";
        common_vendor.index.navigateTo({
          url: `/pages/base/pages/webview?url=${encodeURIComponent(redirectionPath)}&title=${encodeURIComponent(title)}`
        });
      } else if (redirectionPath.startsWith("/pages")) {
        common_vendor.index.navigateTo({
          url: redirectionPath
        });
      } else {
        try {
          common_vendor.index.navigateTo({
            url: redirectionPath
          });
        } catch (error) {
          common_vendor.index.__f__("warn", "at pages/base/pages/index.vue:314", "无效的跳转路径:", redirectionPath, error);
        }
      }
    };
    common_vendor.onLoad((options) => {
      const { scene = "" } = options;
      const sales = scene && decodeURIComponent(scene).split("=")[1];
      if (sales) {
        common_storage.setFromSalesId(sales);
      }
      getBannerList();
      getActivityList();
      getThemeList();
      getTicketList();
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.p({
          position: "flex"
        }),
        b: bannerList.value.length > 0
      }, bannerList.value.length > 0 ? {
        c: common_vendor.f(bannerList.value, (banner, index, i0) => {
          return {
            a: banner.imageUrl || banner.image,
            b: common_vendor.o(($event) => handleBannerClick(banner), index),
            c: index
          };
        })
      } : {}, {
        d: common_assets._imports_0,
        e: common_vendor.o(showQueuePopup),
        f: common_assets._imports_1,
        g: common_vendor.o(goToStore),
        h: common_assets._imports_2,
        i: common_vendor.o(showGameTip),
        j: common_vendor.f(displayTickets.value, (ticket, k0, i0) => {
          return common_vendor.e({
            a: ticket.posterUrl
          }, ticket.posterUrl ? {
            b: ticket.posterUrl
          } : {}, {
            c: common_vendor.t(ticket.name),
            d: ticket.originalPrice && ticket.originalPrice !== ticket.currentPrice
          }, ticket.originalPrice && ticket.originalPrice !== ticket.currentPrice ? {
            e: common_vendor.t(formatPrice(ticket.originalPrice))
          } : {}, {
            f: common_vendor.t(formatPrice(ticket.currentPrice)),
            g: ticket.id,
            h: common_vendor.o(($event) => goToTicketDetail(ticket.id), ticket.id)
          });
        }),
        k: common_vendor.o(goToTicketList),
        l: common_vendor.f(themeList.value, (theme, k0, i0) => {
          return {
            a: theme.posterUrl,
            b: common_vendor.t(theme.themeName || theme.name),
            c: theme.id
          };
        }),
        m: activityList.value.length > 0
      }, activityList.value.length > 0 ? {
        n: common_vendor.f(activityList.value, (activity, index, i0) => {
          return {
            a: activity.imageUrl || activity.image,
            b: common_vendor.o(($event) => handleBannerClick(activity), index),
            c: index
          };
        })
      } : {}, {
        o: themeList.value.length === 0
      }, themeList.value.length === 0 ? {} : {
        p: common_vendor.f(themeList.value, (theme, index, i0) => {
          return {
            a: common_vendor.t(theme.themeName || theme.name || "未知主题"),
            b: common_vendor.t(formatQueueDuration(theme.queueDuration || 0)),
            c: index
          };
        })
      }, {
        q: common_vendor.sr(queuePopup, "946b03a8-1", {
          "k": "queuePopup"
        }),
        r: common_vendor.p({
          type: "center",
          ["mask-click"]: true
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-946b03a8"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/base/pages/index.js.map

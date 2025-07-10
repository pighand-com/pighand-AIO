"use strict";
const common_vendor = require("../../common/vendor.js");
const _sfc_main = {
  __name: "status-bar-gradient",
  props: {
    // 渐变颜色，支持rgb、rgba、hex等格式
    color: {
      type: String,
      default: "white"
    },
    // 顶部不透明度
    topOpacity: {
      type: Number,
      default: 0.8
    },
    // 中间不透明度
    middleOpacity: {
      type: Number,
      default: 0.4
    },
    // 遮罩高度，默认为安全区高度
    height: {
      type: String,
      default: ""
    }
  },
  setup(__props) {
    const systemInfo = common_vendor.ref({});
    const safeAreaHeight = common_vendor.ref("44px");
    common_vendor.onMounted(() => {
      try {
        systemInfo.value = common_vendor.index.getSystemInfoSync();
        const statusBarHeight = systemInfo.value.statusBarHeight || 20;
        const navHeight = 44;
        safeAreaHeight.value = `${statusBarHeight + navHeight}px`;
      } catch (e) {
        common_vendor.index.__f__("warn", "at components/status-bar-gradient/status-bar-gradient.vue:20", "获取系统信息失败，使用默认安全区高度", e);
      }
    });
    const props = __props;
    const gradientStyle = common_vendor.computed(() => {
      const { color, topOpacity, middleOpacity, height } = props;
      const finalHeight = height || safeAreaHeight.value;
      let rgbaColor;
      if (color === "white") {
        rgbaColor = "255, 255, 255";
      } else if (color === "black") {
        rgbaColor = "0, 0, 0";
      } else if (color.startsWith("#")) {
        const hex = color.slice(1);
        const r = parseInt(hex.slice(0, 2), 16);
        const g = parseInt(hex.slice(2, 4), 16);
        const b = parseInt(hex.slice(4, 6), 16);
        rgbaColor = `${r}, ${g}, ${b}`;
      } else if (color.startsWith("rgb")) {
        const match = color.match(/\d+/g);
        if (match && match.length >= 3) {
          rgbaColor = `${match[0]}, ${match[1]}, ${match[2]}`;
        } else {
          rgbaColor = "255, 255, 255";
        }
      } else {
        rgbaColor = "255, 255, 255";
      }
      return {
        height: finalHeight,
        background: `linear-gradient(to bottom, rgba(${rgbaColor}, ${topOpacity}) 0%, rgba(${rgbaColor}, ${middleOpacity}) 50%, transparent 100%)`
      };
    });
    return (_ctx, _cache) => {
      return {
        a: common_vendor.s(gradientStyle.value)
      };
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-06701e00"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/components/status-bar-gradient/status-bar-gradient.js.map

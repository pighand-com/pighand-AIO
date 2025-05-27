"use strict";
const common_vendor = require("../../../common/vendor.js");
const _sfc_main = {
  name: "tuiTabbar",
  emits: ["click"],
  // components:{
  // 	tuiIcon
  // },
  props: {
    //当前索引
    current: {
      type: Number,
      default: 0
    },
    //字体颜色
    color: {
      type: String,
      default: "#666"
    },
    //字体选中颜色
    selectedColor: {
      type: String,
      default: ""
    },
    //背景颜色
    backgroundColor: {
      type: String,
      default: "#FFFFFF"
    },
    //是否需要中间凸起按钮
    hump: {
      type: Boolean,
      default: false
    },
    iconSize: {
      type: [Number, String],
      default: 52
    },
    //固定在底部
    isFixed: {
      type: Boolean,
      default: true
    },
    tabBar: {
      type: Array,
      default() {
        return [];
      }
    },
    //角标字体颜色
    badgeColor: {
      type: String,
      default: "#fff"
    },
    //角标背景颜色
    badgeBgColor: {
      type: String,
      default: ""
    },
    unlined: {
      type: Boolean,
      default: false
    },
    //是否开启高斯模糊效果[仅在支持的浏览器有效果]
    backdropFilter: {
      type: Boolean,
      default: false
    },
    //z-index
    zIndex: {
      type: [Number, String],
      default: 9999
    }
  },
  data() {
    return {};
  },
  computed: {
    getActiveColor() {
      return this.selectedColor || common_vendor.index && common_vendor.index.$tui && common_vendor.index.$tui.color.primary || "#5677fc";
    },
    getBadgeBgColor() {
      return this.badgeBgColor || common_vendor.index && common_vendor.index.$tui && common_vendor.index.$tui.color.pink || "#f74d54";
    }
  },
  methods: {
    tabbarSwitch(index, hump, pagePath, verify) {
      this.$emit("click", {
        index,
        hump,
        pagePath,
        verify
      });
    }
  }
};
if (!Array) {
  const _easycom_tui_icon2 = common_vendor.resolveComponent("tui-icon");
  _easycom_tui_icon2();
}
const _easycom_tui_icon = () => "../tui-icon/tui-icon.js";
if (!Math) {
  _easycom_tui_icon();
}
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: common_vendor.f($props.tabBar, (item, index, i0) => {
      return common_vendor.e({
        a: !item.name
      }, !item.name ? {
        b: $props.current == index ? item.selectedIconPath : item.iconPath,
        c: common_vendor.n(item.hump ? "" : "tui-tabbar-icon")
      } : {
        d: "a5dcc4b5-0-" + i0,
        e: common_vendor.p({
          name: $props.current === index ? item.activeName : item.name,
          customPrefix: item.customPrefix || "",
          size: item.iconSize || $props.iconSize,
          unit: "rpx",
          color: $props.current == index ? $options.getActiveColor : $props.color
        })
      }, {
        f: item.num
      }, item.num ? {
        g: common_vendor.t(item.isDot ? "" : item.num),
        h: common_vendor.n(item.isDot ? "tui-badge-dot" : "tui-badge"),
        i: $props.badgeColor,
        j: $options.getBadgeBgColor
      } : {}, {
        k: item.hump ? 1 : "",
        l: common_vendor.t(item.text),
        m: item.hump ? 1 : "",
        n: $props.current == index ? $options.getActiveColor : $props.color,
        o: item.hump ? 1 : "",
        p: item.hump && !$props.backdropFilter ? $props.backgroundColor : "none",
        q: common_vendor.o(($event) => $options.tabbarSwitch(index, item.hump, item.pagePath, item.verify), index),
        r: index
      });
    }),
    b: $props.hump && !$props.unlined && !$props.backdropFilter
  }, $props.hump && !$props.unlined && !$props.backdropFilter ? {
    c: $props.backgroundColor,
    d: $props.hump ? 1 : ""
  } : {}, {
    e: $props.isFixed ? 1 : "",
    f: $props.unlined ? 1 : "",
    g: $props.backdropFilter ? 1 : "",
    h: $props.backgroundColor,
    i: $props.isFixed ? $props.zIndex : "auto"
  });
}
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-a5dcc4b5"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/components/thorui/tui-tabbar/tui-tabbar.js.map

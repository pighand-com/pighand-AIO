"use strict";
const common_vendor = require("../../common/vendor.js");
const common_assets = require("../../common/assets.js");
const _sfc_main = {
  __name: "custom-button",
  props: {
    text: {
      type: String,
      default: "按钮"
    },
    width: {
      type: String,
      default: "300rpx"
    },
    height: {
      type: String,
      default: ""
    },
    borderRadius: {
      type: String,
      default: "0"
    },
    fontSize: {
      type: String,
      default: "32rpx"
    },
    textColor: {
      type: String,
      default: "#ffffff"
    }
  },
  emits: ["click"],
  setup(__props, { emit: __emit }) {
    const emit = __emit;
    const handleClick = () => {
      emit("click");
    };
    return (_ctx, _cache) => {
      return {
        a: common_assets._imports_0$4,
        b: __props.height ? "aspectFill" : "widthFix",
        c: common_vendor.t(__props.text),
        d: __props.fontSize,
        e: __props.fontSize,
        f: __props.textColor,
        g: common_vendor.n({
          "auto-height": !__props.height
        }),
        h: common_vendor.o(handleClick),
        i: __props.width,
        j: __props.height,
        k: __props.borderRadius
      };
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-34596d2b"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/components/custom-button/custom-button.js.map

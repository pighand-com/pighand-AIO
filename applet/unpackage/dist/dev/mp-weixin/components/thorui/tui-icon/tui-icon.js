"use strict";
const common_vendor = require("../../../common/vendor.js");
const icons = {
  "about": "",
  "about-fill": "",
  "add": "",
  "add-fill": "",
  "addmessage": "",
  "addressbook": "",
  "agree": "",
  "agree-fill": "",
  "alarm": "",
  "alarm-fill": "",
  "alipay": "",
  "android": "",
  "applets": "",
  "arrowdown": "",
  "arrowleft": "",
  "arrowright": "",
  "arrowup": "",
  "attestation": "",
  "back": "",
  "bag": "",
  "bag-fill": "",
  "balloon": "",
  "bankcard": "",
  "bankcard-fill": "",
  "bottom": "",
  "calendar": "",
  "camera": "",
  "camera-fill": "",
  "camera-add": "",
  "card": "",
  "card-fill": "",
  "cart": "",
  "cart-fill": "",
  "category": "",
  "category-fill": "",
  "check": "",
  "circle": "",
  "circle-fill": "",
  "circle-selected": "",
  "clock": "",
  "clock-fill": "",
  "close": "",
  "close-fill": "",
  "community": "",
  "community-fill": "",
  "computer": "",
  "computer-fill": "",
  "coupon": "",
  "delete": "",
  "deletekey": "",
  "dingtalk": "",
  "dissatisfied": "",
  "down": "",
  "download": "",
  "edit": "",
  "ellipsis": "",
  "enlarge": "",
  "evaluate": "",
  "exchange": "",
  "explain": "",
  "explain-fill": "",
  "explore": "",
  "explore-fill": "",
  "eye": "",
  "feedback": "",
  "fingerprint": "",
  "friendadd": "",
  "friendadd-fill": "",
  "gps": "",
  "histogram": "",
  "home": "",
  "home-fill": "",
  "house": "",
  "imface": "",
  "imkeyboard": "",
  "immore": "",
  "imvoice": "",
  "ios": "",
  "kefu": "",
  "label": "",
  "label-fill": "",
  "like": "",
  "like-fill": "",
  "link": "",
  "listview": "",
  "loading": "",
  "location": "",
  "mail": "",
  "mail-fill": "",
  "manage": "",
  "manage-fill": "",
  "member": "",
  "member-fill": "",
  "message": "",
  "message-fill": "",
  "mobile": "",
  "moments": "",
  "more": "",
  "more-fill": "",
  "narrow": "",
  "news": "",
  "news-fill": "",
  "nodata": "",
  "notice": "",
  "notice-fill": "",
  "offline": "",
  "offline-fill": "",
  "oppose": "",
  "oppose-fill": "",
  "order": "",
  "partake": "",
  "people": "",
  "people-fill": "",
  "pic": "",
  "pic-fill": "",
  "picture": "",
  "pie": "",
  "plus": "",
  "polygonal": "",
  "position": "",
  "pwd": "",
  "qq": "",
  "qrcode": "",
  "redpacket": "",
  "redpacket-fill": "",
  "reduce": "",
  "refresh": "",
  "revoke": "",
  "satisfied": "",
  "screen": "",
  "search": "",
  "search-2": "",
  "send": "",
  "service": "",
  "service-fill": "",
  "setup": "",
  "setup-fill": "",
  "share": "",
  "share-fill": "",
  "shield": "",
  "shop": "",
  "shop-fill": "",
  "shut": "",
  "signin": "",
  "sina": "",
  "skin": "",
  "soso": "",
  "square": "",
  "square-fill": "",
  "square-selected": "",
  "star": "",
  "star-fill": "",
  "strategy": "",
  "sweep": "",
  "time": "",
  "time-fill": "",
  "todown": "",
  "toleft": "",
  "tool": "",
  "top": "",
  "toright": "",
  "towardsleft": "",
  "towardsright": "",
  "towardsright-fill": "",
  "transport": "",
  "transport-fill": "",
  "turningdown": "",
  "turningleft": "",
  "turningright": "",
  "turningup": "",
  "unreceive": "",
  "seen": "",
  "unseen": "",
  "up": "",
  "upload": "",
  "video": "",
  "voice": "",
  "voice-fill": "",
  "voipphone": "",
  "wallet": "",
  "warning": "",
  "wealth": "",
  "wealth-fill": "",
  "weather": "",
  "wechat": "",
  "wifi": "",
  "play": "",
  "suspend": ""
};
const _sfc_main = {
  name: "tuiIcon",
  emits: ["click"],
  props: {
    name: {
      type: String,
      default: ""
    },
    customPrefix: {
      type: String,
      default: ""
    },
    size: {
      type: [Number, String],
      default: 0
    },
    //px或者rpx
    unit: {
      type: String,
      default: ""
    },
    color: {
      type: String,
      default: ""
    },
    bold: {
      type: Boolean,
      default: false
    },
    margin: {
      type: String,
      default: "0"
    },
    index: {
      type: Number,
      default: 0
    }
  },
  computed: {
    getColor() {
      return this.color || common_vendor.index && common_vendor.index.$tui && common_vendor.index.$tui.tuiIcon.color || "#999";
    },
    getSize() {
      const size = this.size || common_vendor.index && common_vendor.index.$tui && common_vendor.index.$tui.tuiIcon.size || 32;
      const unit = this.unit || common_vendor.index && common_vendor.index.$tui && common_vendor.index.$tui.tuiIcon.unit || "px";
      return size + unit;
    }
  },
  data() {
    return {
      icons
    };
  },
  methods: {
    handleClick() {
      this.$emit("click", {
        index: this.index
      });
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: common_vendor.t($data.icons[$props.name] || ""),
    b: common_vendor.n($props.customPrefix),
    c: common_vendor.n($props.customPrefix ? $props.name : ""),
    d: $options.getColor,
    e: $options.getSize,
    f: $props.bold ? "bold" : "normal",
    g: $props.margin,
    h: common_vendor.o((...args) => $options.handleClick && $options.handleClick(...args))
  };
}
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-096cf6db"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/components/thorui/tui-icon/tui-icon.js.map

"use strict";
const common_vendor = require("./vendor.js");
const common_constant = require("./constant.js");
const getToken = () => {
  return common_vendor.index.getStorageSync(common_constant.constant.local_storage_token);
};
const setToken = (token) => {
  common_vendor.index.setStorageSync(common_constant.constant.local_storage_token, token);
  common_vendor.index.$emit("storage-changed");
};
const clearToken = () => {
  common_vendor.index.removeStorageSync(common_constant.constant.local_storage_token);
  common_vendor.index.$emit("storage-changed");
};
const clearUserInfo = () => {
  common_vendor.index.removeStorageSync(common_constant.constant.local_storage_user_info);
  common_vendor.index.$emit("storage-changed");
};
const clearApplicationInfo = () => {
  common_vendor.index.removeStorageSync(common_constant.constant.local_storage_application_info);
  common_vendor.index.$emit("storage-changed");
};
const getSalespersonId = () => {
  return common_vendor.index.getStorageSync(common_constant.constant.local_storage_salesperson_id);
};
const setSalespersonId = (salespersonId) => {
  common_vendor.index.setStorageSync(common_constant.constant.local_storage_salesperson_id, salespersonId);
  common_vendor.index.$emit("storage-changed");
};
const clearSalespersonId = () => {
  common_vendor.index.removeStorageSync(common_constant.constant.local_storage_salesperson_id);
  common_vendor.index.$emit("storage-changed");
};
const getFromSalesId = () => {
  return common_vendor.index.getStorageSync(common_constant.constant.local_storage_from_sales_id);
};
const setFromSalesId = (fromSalesId) => {
  common_vendor.index.setStorageSync(common_constant.constant.local_storage_from_sales_id, fromSalesId);
  common_vendor.index.$emit("storage-changed");
};
const clearFromSalesId = () => {
  common_vendor.index.removeStorageSync(common_constant.constant.local_storage_from_sales_id);
  common_vendor.index.$emit("storage-changed");
};
const clearAll = () => {
  clearToken();
  clearUserInfo();
  clearApplicationInfo();
  clearSalespersonId();
  clearFromSalesId();
  common_vendor.index.$emit("storage-changed");
};
const getUserInfo = () => {
  return JSON.parse(common_vendor.index.getStorageSync(common_constant.constant.local_storage_user_info) || "{}");
};
const setUserInfo = (userInfo) => {
  common_vendor.index.setStorageSync(
    common_constant.constant.local_storage_user_info,
    JSON.stringify(userInfo)
  );
  common_vendor.index.$emit("storage-changed");
};
const getApplicationInfo = () => {
  return JSON.parse(
    common_vendor.index.getStorageSync(common_constant.constant.local_storage_application_info) || "{}"
  );
};
const setApplicationInfo = (applicationInfo) => {
  common_vendor.index.setStorageSync(
    common_constant.constant.local_storage_application_info,
    JSON.stringify(applicationInfo)
  );
  common_vendor.index.$emit("storage-changed");
};
exports.clearAll = clearAll;
exports.getApplicationInfo = getApplicationInfo;
exports.getFromSalesId = getFromSalesId;
exports.getSalespersonId = getSalespersonId;
exports.getToken = getToken;
exports.getUserInfo = getUserInfo;
exports.setApplicationInfo = setApplicationInfo;
exports.setFromSalesId = setFromSalesId;
exports.setSalespersonId = setSalespersonId;
exports.setToken = setToken;
exports.setUserInfo = setUserInfo;
//# sourceMappingURL=../../.sourcemap/mp-weixin/common/storage.js.map

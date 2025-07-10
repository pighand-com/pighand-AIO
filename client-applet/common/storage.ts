import constant from './constant';

const getToken = () => {
    return uni.getStorageSync(constant.local_storage_token);
};

const setToken = (token: string) => {
    uni.setStorageSync(constant.local_storage_token, token);
    uni.$emit('storage-changed');
};

const clearToken = () => {
    uni.removeStorageSync(constant.local_storage_token);
    uni.$emit('storage-changed');
};

const clearUserInfo = () => {
    uni.removeStorageSync(constant.local_storage_user_info);
    uni.$emit('storage-changed');
};

const clearApplicationInfo = () => {
    uni.removeStorageSync(constant.local_storage_application_info);
    uni.$emit('storage-changed');
};

const getSalespersonId = () => {
    return uni.getStorageSync(constant.local_storage_salesperson_id);
};

const setSalespersonId = (salespersonId: string) => {
    uni.setStorageSync(constant.local_storage_salesperson_id, salespersonId);
    uni.$emit('storage-changed');
};

const clearSalespersonId = () => {
    uni.removeStorageSync(constant.local_storage_salesperson_id);
    uni.$emit('storage-changed');
};

const getFromSalesId = () => {
    return uni.getStorageSync(constant.local_storage_from_sales_id);
};

const setFromSalesId = (fromSalesId: string) => {
    uni.setStorageSync(constant.local_storage_from_sales_id, fromSalesId);
    uni.$emit('storage-changed');
};

const clearFromSalesId = () => {
    uni.removeStorageSync(constant.local_storage_from_sales_id);
    uni.$emit('storage-changed');
};

const clearAll = () => {
    clearToken();
    clearUserInfo();
    clearApplicationInfo();
    clearSalespersonId();
    clearFromSalesId();
    uni.$emit('storage-changed');
};

const getUserInfo = () => {
    return JSON.parse(uni.getStorageSync(constant.local_storage_user_info) || '{}');
};

const setUserInfo = (userInfo: Object) => {
    uni.setStorageSync(
        constant.local_storage_user_info,
        JSON.stringify(userInfo)
    );
    uni.$emit('storage-changed');
};

interface ApplicationInfo {
    id: string;
    name: string;
    uploadType: string;
}

const getApplicationInfo = (): ApplicationInfo | null => {
    return JSON.parse(
        uni.getStorageSync(constant.local_storage_application_info) || '{}'
    );
};

const setApplicationInfo = (applicationInfo: {
    id: string;
    name: string;
    uploadType: string;
}) => {
    uni.setStorageSync(
        constant.local_storage_application_info,
        JSON.stringify(applicationInfo)
    );
    uni.$emit('storage-changed');
};

export {
    getToken,
    setToken,
    clearToken,
    getUserInfo,
    setUserInfo,
    getApplicationInfo,
    setApplicationInfo,
    getSalespersonId,
    setSalespersonId,
    clearSalespersonId,
    getFromSalesId,
    setFromSalesId,
    clearFromSalesId,
    clearAll
};

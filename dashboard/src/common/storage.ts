import constant from './constant';

const getToken = () => {
    return localStorage.getItem(constant.local_storage_token);
};

const setToken = (token: string) => {
    localStorage.setItem(constant.local_storage_token, token);
};

const clearToken = () => {
    localStorage.removeItem(constant.local_storage_token);
};

const clearUserInfo = () => {
    localStorage.removeItem(constant.local_storage_user_info);
};

const clearApplicationInfo = () => {
    localStorage.removeItem(constant.local_storage_application_info);
};

const clearAll = () => {
    clearToken();
    clearUserInfo();
    clearApplicationInfo();
};

const getUserInfo = () => {
    return JSON.parse(localStorage.getItem(constant.local_storage_user_info));
};

const setUserInfo = (userInfo: Object) => {
    localStorage.setItem(
        constant.local_storage_user_info,
        JSON.stringify(userInfo)
    );
};

interface ApplicationInfo {
    id: string;
    name: string;
}

const getApplicationInfo = (): ApplicationInfo | null => {
    return JSON.parse(localStorage.getItem(constant.local_storage_application_info));
};

const setApplicationInfo = (applicationInfo: { id: string; name: string }) => {
    localStorage.setItem(
        constant.local_storage_application_info,
        JSON.stringify(applicationInfo)
    );
};

export { getToken, setToken, clearToken, getUserInfo, setUserInfo, getApplicationInfo, setApplicationInfo, clearAll };

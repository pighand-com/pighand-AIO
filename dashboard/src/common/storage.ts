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

const getUserInfo = () => {
    return JSON.parse(localStorage.getItem(constant.local_storage_user_info));
};

const setUserInfo = (userInfo: Object) => {
    localStorage.setItem(
        constant.local_storage_user_info,
        JSON.stringify(userInfo)
    );
};

export { getToken, setToken, clearToken, getUserInfo, setUserInfo };

import constant from './constant';
import { getToken, clearAll } from './storage';

interface RequestConfig {
    isDialog?: boolean;
    headers?: any;
    params?: any;
    responseType?: string;
	onUploadProgress?: any;
}

const getUrl = (url: string) => {
    return constant.api[__wxConfig.envVersion] + url;
};

const disposeResponse = (response: any, isDialog = true, blob = false) => {
    const { statusCode, data } = response;

    let errorMsg = '';
    let isSuccess = true;

    if (statusCode === 200 && data.code && data.code !== 200) {
        isSuccess = false;
        errorMsg = data.msg || data.error;
    } else if (statusCode === 401) {
        clearAll();
        uni.showToast({
            title: "请先登录",
            icon: 'error'
        });
        return;
    } else if (statusCode !== 200) {
        isSuccess = false;
        errorMsg = data;
    }

    let result: { data: object; error: string } | any = {
        data: data.data,
        error: errorMsg
    };

    if (isDialog) {
        if (!isSuccess) {
            uni.showToast({
                title: errorMsg,
                icon: 'error'
            });
            throw new Error(errorMsg);
        }
        result = data.data;
    }

    // blob 下载逻辑在小程序环境可能需要使用其他方式实现
    return result;
};

const request = async (method: 'GET' | 'POST' | 'PUT' | 'DELETE', url: string, data?: any, config?: RequestConfig) => {
    url = getUrl(url);
    
    const requestConfig: any = {
        url,
        method,
        data,
        header: {
            'X-Application-Id': constant.APPLICATION_ID,
            ...(config?.headers || {}),
            'Authorization': getToken()
        },
        timeout: 60000
    };

    try {
        const response = await uni.request(requestConfig);
		
        return disposeResponse(response, config?.isDialog, config?.responseType === 'blob');
    } catch (error) {
        uni.showToast({
            title: '网络请求失败：' + error,
            icon: 'none'
        });
        throw error;
    }
};

const get = (url: string, params?: any, config?: RequestConfig) => {
    return request('GET', url, params, config);
};

const post = (url: string, data?: any, config?: RequestConfig) => {
    return request('POST', url, data, config);
};

const put = (url: string, data?: any, config?: RequestConfig) => {
    return request('PUT', url, data, config);
};

const del = (url: string, params?: any, config?: RequestConfig) => {
    return request('DELETE', url, params, config);
};

export default {
    get,
    post,
    put,
    del
};

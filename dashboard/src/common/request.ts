// import { ElMessage } from 'element-plus';
import axios, { AxiosResponse, HttpStatusCode } from 'axios';
import Qs from 'qs';

import router from '@/routers/index';
import constant from './constant';
import { getToken, clearAll } from './storage';

const default_application_id = 1;

/**
 * 将所有状态码视为成功
 * @param status
 * @returns
 */
axios.defaults.validateStatus = function (_status) {
    return true;
};

const getUrl = (url: string) => {
    return constant.api[process.env.NODE_ENV] + url;
};

const disposeResponse = (
    axiosResponse: AxiosResponse<any, any>,
    isDialog = true,
    blob = false
) => {
    const { status, data, headers } = axiosResponse;

    let errorMsg = '';
    let isSuccess = true;

    if (status === HttpStatusCode.Ok && data.code && data.code !== 200) {
        isSuccess = false;
        errorMsg = data.msg || data.error;
    } else if (status === HttpStatusCode.Unauthorized) {
        clearAll();
        router.push('/login');
        return;
    } else if (status !== HttpStatusCode.Ok) {
        isSuccess = false;
        errorMsg = data;
    }

    let result: { data: object; error: string } | any = {
        data: data.data,
        error: errorMsg
    };

    if (isDialog) {
        if (!isSuccess) {
            ElMessage({
                showClose: true,
                message: errorMsg,
                type: 'error'
            });
            return;
        }

        result = data.data;
    }

    if (blob) {
        const contentDisposition = headers['content-disposition'];

        let fileName = Date.now().toString();
        if (contentDisposition) {
            fileName = decodeURI(contentDisposition.split('filename=')[1]);
        }

        let url = window.URL.createObjectURL(new Blob([data]));
        let a = document.createElement('a');
        a.style.display = 'none';
        a.href = url;
        a.setAttribute('download', fileName);
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(a.href);
        document.body.removeChild(a);
    }

    return result;
};

const get = async (
    url: string,
    params?: any,
    config?: { isDialog?: boolean; headers?: any; responseType?: string }
) => {
    url = getUrl(url);

    const axiosConfig: any = {
        params: params || {},
        paramsSerializer: {
            serialize: (params) =>
                Qs.stringify(params, { arrayFormat: 'repeat' })
        },
        headers: {
            'X-Application-Id': default_application_id,
            ...(config?.headers || {}),
            Authorization: getToken()
        }
    };

    if (config?.responseType) {
        axiosConfig.responseType = config.responseType;
    }

    const result = await axios.get(url, axiosConfig);

    return disposeResponse(
        result,
        config?.isDialog,
        config?.responseType === 'blob'
    );
};

const post = async (
    url: string,
    data?: any,
    config?: {
        params?: any;
        isDialog?: boolean;
        headers?: any;
        responseType?: string;
    }
) => {
    url = getUrl(url);

    const axiosConfig: any = {
        params: config?.params || {},
        paramsSerializer: {
            serialize: (params) =>
                Qs.stringify(params, { arrayFormat: 'repeat' })
        },
        headers: {
            'X-Application-Id': default_application_id,
            ...(config?.headers || {}),
            Authorization: getToken()
        }
    };

    if (config?.responseType) {
        axiosConfig.responseType = config.responseType;
    }

    const result = await axios.post(url, data, axiosConfig);

    return disposeResponse(
        result,
        config?.isDialog,
        config?.responseType === 'blob'
    );
};

const put = async (
    url: string,
    data?: any,
    config?: {
        params?: any;
        isDialog?: boolean;
        headers?: any;
    }
) => {
    url = getUrl(url);
    const result = await axios.put(url, data, {
        params: config?.params || {},
        paramsSerializer: {
            serialize: (params) =>
                Qs.stringify(params, { arrayFormat: 'repeat' })
        },
        headers: {
            'X-Application-Id': default_application_id,
            ...(config?.headers || {}),
            Authorization: getToken()
        }
    });

    return disposeResponse(result, config?.isDialog);
};

const del = async (
    url: string,
    params?: any,
    config?: { isDialog?: boolean; headers?: any }
) => {
    url = getUrl(url);
    const result = await axios.delete(url, {
        params: params || {},
        paramsSerializer: {
            serialize: (params) =>
                Qs.stringify(params, { arrayFormat: 'repeat' })
        },
        headers: {
            'X-Application-Id': default_application_id,
            ...(config?.headers || {}),
            Authorization: getToken()
        }
    });

    return disposeResponse(result, config?.isDialog);
};

export default {
    get,
    post,
    put,
    del
};

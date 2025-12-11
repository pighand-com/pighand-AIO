import request from '@/common/request';

/**
 * 获取验证码
 * @param key 
 * @returns 
 */
const getCAPTCHACode = async (key: string) => {
    return await request.get(`CAPTCHA/code`, { key })
}

/**
 * 获取上传URL
 * @param files 文件信息数组，包含path和extension
 * @returns 上传URL信息
 */
const getUploadUrls = async (files: Array<{path: string, extension: string}>) => {
    try {
        const response = await request.post('upload/url', files);
        return response;
    } catch (error) {
        console.error('获取上传URL失败:', error);
        throw error;
    }
};

/**
 * 上传文件到服务器
 * @param files 文件数组
 * @returns 上传结果
 */
const uploadToServer = async (files: File[]) => {
    try {
        const formData = new FormData();
        files.forEach((file, index) => {
            formData.append(`file${index}`, file);
        });

        const response = await request.post('upload', formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });
        return response.data;
    } catch (error) {
        console.error('上传文件失败:', error);
        throw error;
    }
};

export default {
    getCAPTCHACode,
    getUploadUrls,
    uploadToServer
};
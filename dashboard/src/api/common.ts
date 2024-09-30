import request from '@/common/request';

export default {
    // 获取验证码
    getCAPTCHACode: async (key: string) =>
        await request.get(`CAPTCHA/code`, { key }),

    getCOSUploadUrl: async (extension: string, path: string) =>
        await request.get(`upload/url`, { extension, path }),

    uploadToServer: async (file: File) => {
        const formData = new FormData();
        formData.append('files', file);
        return await request.post(`upload/server`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });
    }
};

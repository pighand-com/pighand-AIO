import request from '@/common/request';

export default {
    // 获取验证码
    getCAPTCHACode: async (key: string) =>
        await request.get(`CAPTCHA/code`, { key }),

    // 获取上传url
    getUploadUrls: async (params: { extension: string; path: string }[]) =>
        await request.post(`upload/url`, params),

    // 上传到server
    uploadToServer: async (
        file: File,
        onUploadProgress: (progress: number) => void
    ) => {
        const formData = new FormData();
        formData.append('files', file);
        return await request.post(`upload/server`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            },
            onUploadProgress: (progressEvent) => {
                if (onUploadProgress) {
                    const percentCompleted = Math.round(
                        (progressEvent.loaded * 100) / progressEvent.total
                    );
                    onUploadProgress(percentCompleted);
                }
            }
        });
    }
};

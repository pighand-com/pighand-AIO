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
        file: any,
        onUploadProgress?: (progress: number) => void
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
    },

	// 上传到COS
	uploadCOS: async (
		file: ArrayBuffer,
		contentType: string,
		uploadUrl: string,
		headers = {},
		onUploadProgress?: (progress: number) => void
	) => {
		try {
			// await uni.uploadFile({
			// 	url: uploadUrl,
			// 	filePath: filePath,
			// 	name: 'file',
			// 	headers: {
			// 		...headers
			// 	},
			// 	formData: {
			// 		"wsl": "wsl"
			// 	}
			// });
			await uni.request({
				url: uploadUrl,
				method: 'PUT',
				data: file,
				header: {
					'Content-Type': contentType,
					...headers
				},
			});
		} catch (error) {
			console.log(error)
			uni.showToast({
			    title: `文件上传失败：${error}`,
			    icon: 'none'
			});
		}
	}
};

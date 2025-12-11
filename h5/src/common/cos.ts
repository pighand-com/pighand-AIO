import axios from 'axios';

/**
 * 上传文件
 * @param file 文件
 * @param uploadUrl 上传地址
 * @param onUploadProgress 上传进度回调
 * @returns
 */
const upload = async (
    file,
    uploadUrl,
    headers = {},
    onUploadProgress: (progress: number) => void
) => {
    try {
        const contentType = file.type;
        await axios.put(uploadUrl, file, {
            headers: {
                'Content-Type': contentType,
                ...headers
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

        return uploadUrl.split('?')[0];
    } catch (error) {
        ElMessage({
            showClose: true,
            message: `文件上传失败：${error}`,
            type: 'error'
        });
    }
};

export default { upload };

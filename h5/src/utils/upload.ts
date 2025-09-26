import { common } from '@/api/index';

/**
 * 上传文件到COS
 * @param file 文件对象
 * @param uploadPath 上传路径
 * @param uploadProgress 上传进度回调
 * @returns 上传后的文件URL
 */
export const uploadFile = async (
    file: File, 
    uploadPath: string = 'avatar', 
    uploadProgress?: (progress: number) => void
): Promise<string> => {
    try {
        // 获取文件扩展名
        const fileExt = file.name.split('.').pop() || 'jpg';
        
        // 获取上传URL
        const uploadUrlInfo = await common.getUploadUrls([{
            path: uploadPath,
            extension: fileExt
        }]);

        // 上传失败
        if (!uploadUrlInfo || !uploadUrlInfo.urls || uploadUrlInfo.urls.length === 0) {
            throw new Error('获取上传URL失败');
        }

        const uploadUrl = uploadUrlInfo.urls[0];
        
        // 上传文件到COS
        await uploadToCOS(file, uploadUrl.uploadUrl, uploadUrlInfo.headers, uploadProgress);
        
        return uploadUrl.url;
    } catch (error) {
        console.error('上传文件失败:', error);
        throw error;
    }
};

/**
 * 上传文件到腾讯云COS
 * @param file 文件对象
 * @param uploadUrl 上传URL
 * @param headers 请求头
 * @param uploadProgress 上传进度回调
 */
const uploadToCOS = async (
    file: File,
    uploadUrl: string,
    headers: any,
    uploadProgress?: (progress: number) => void
): Promise<void> => {
    return new Promise((resolve, reject) => {
        const xhr = new XMLHttpRequest();
        
        // 监听上传进度
        if (uploadProgress) {
            xhr.upload.addEventListener('progress', (event) => {
                if (event.lengthComputable) {
                    const progress = Math.round((event.loaded / event.total) * 100);
                    uploadProgress(progress);
                }
            });
        }
        
        // 监听上传完成
        xhr.addEventListener('load', () => {
            if (xhr.status >= 200 && xhr.status < 300) {
                resolve();
            } else {
                reject(new Error(`上传失败: ${xhr.status} ${xhr.statusText}`));
            }
        });
        
        // 监听上传错误
        xhr.addEventListener('error', () => {
            reject(new Error('上传过程中发生错误'));
        });
        
        // 设置请求
        xhr.open('PUT', uploadUrl);
        
        // 设置请求头
        if (headers) {
            Object.keys(headers).forEach(key => {
                xhr.setRequestHeader(key, headers[key]);
            });
        }
        
        // 设置内容类型
        xhr.setRequestHeader('Content-Type', file.type);
        
        // 发送文件
        xhr.send(file);
    });
};

/**
 * 选择图片文件
 * @param accept 接受的文件类型
 * @returns Promise<File>
 */
export const selectImageFile = (accept: string = 'image/*'): Promise<File> => {
    return new Promise((resolve, reject) => {
        const input = document.createElement('input');
        input.type = 'file';
        input.accept = accept;
        
        input.addEventListener('change', (event) => {
            const target = event.target as HTMLInputElement;
            const file = target.files?.[0];
            
            if (file) {
                resolve(file);
            } else {
                reject(new Error('未选择文件'));
            }
        });
        
        input.click();
    });
};
/**
 * 文件工具类
 * 提供文件类型转换、格式化等功能
 */

/**
 * MIME类型到文件扩展名的映射
 */
const MIME_TO_EXTENSION_MAP: Record<string, string> = {
    // Office文档完整MIME类型映射
    'application/vnd.openxmlformats-officedocument.wordprocessingml.document': 'docx',
    'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet': 'xlsx',
    'application/vnd.openxmlformats-officedocument.presentationml.presentation': 'ppt'
};

/**
 * 获取文件扩展名
 * @param file 文件对象
 * @returns 文件扩展名
 */
export function getFileExtension(file: File): string {
    // 优先使用映射表
    if (MIME_TO_EXTENSION_MAP[file.type]) {
        return MIME_TO_EXTENSION_MAP[file.type];
    }
    
    // 从MIME类型中提取扩展名
    const mimeExtension = file.type.split('/')[1];
    if (mimeExtension) {
        return mimeExtension;
    }
    
    // 从文件名中提取扩展名
    const nameExtension = file.name.split('.').pop();
    return nameExtension || '';
}

/**
 * 格式化文件大小
 * @param bytes 文件大小（字节）
 * @returns 格式化后的文件大小字符串
 */
export function formatFileSize(bytes: number): string {
    if (bytes === 0) return '0 B';
    
    const k = 1024;
    const sizes = ['B', 'KB', 'MB', 'GB', 'TB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
}

/**
 * 检查文件类型是否为图片
 * @param file 文件对象
 * @returns 是否为图片
 */
export function isImageFile(file: File): boolean {
    return file.type.startsWith('image/');
}

/**
 * 检查文件类型是否为视频
 * @param file 文件对象
 * @returns 是否为视频
 */
export function isVideoFile(file: File): boolean {
    return file.type.startsWith('video/');
}

/**
 * 检查文件类型是否为Office文档
 * @param file 文件对象
 * @returns 是否为Office文档
 */
export function isOfficeFile(file: File): boolean {
    return Object.keys(MIME_TO_EXTENSION_MAP).includes(file.type) || 
           file.type.includes('msword') || 
           file.type.includes('ms-excel') || 
           file.type.includes('ms-powerpoint');
}
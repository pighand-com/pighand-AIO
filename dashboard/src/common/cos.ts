import axios from 'axios';
import { ElMessage } from 'element-plus';
import { common } from '@/api/index';

/**
 * 上传文件
 * @param file
 * @param path
 * @returns
 */
const upload = async (file, path) => {
    try {
        const contentType = file.type;
        const uploadUrl = await common.getCOSUploadUrl(
            contentType.split('/')[1],
            path
        );

        await axios.put(uploadUrl, file, {
            headers: {
                'Content-Type': contentType
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

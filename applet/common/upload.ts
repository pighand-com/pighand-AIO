import { getApplicationInfo } from '@/common/storage';
import {common} from '@/api/index'

/**
 * 上传文件
 * @param options
 */
export default async (file: ArrayBuffer, fileExt: string, contentType: string, uploadPath: string, uploadProgress?: (progress: number) => void) => {
    let applicationInfo = getApplicationInfo();
	applicationInfo = {
		id: '1',
		name: "",
		uploadType: 'tencent_cloud_cos'
	}

	if (applicationInfo.uploadType === 'system') {
		// 上传到server
		const fileUrls = await common.uploadToServer(file, uploadProgress);
		return fileUrls[0];
	} else {
		// 上传到云服务
		// 获取上传url
		const uploadUrlInfo = await common.getUploadUrls([{
			extension: fileExt,
			path: uploadPath
		}]);

		// 上传失败
		if (!uploadUrlInfo || uploadUrlInfo.urls.length === 0) {
			return;
		}

		for (const uploadUrl of uploadUrlInfo.urls) {
			switch (applicationInfo.uploadType) {
				case 'tencent_cloud_cos':
					await common.uploadCOS(file, contentType, uploadUrl.uploadUrl, uploadUrlInfo.headers, uploadProgress);

					return uploadUrl.url;
				default:
					uni.showToast({
					    title: '暂不支持当前上传类型',
					    icon: 'none'
					});
					break;
			}
		}
	}
}

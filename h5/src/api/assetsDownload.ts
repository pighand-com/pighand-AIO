import request from '@/common/request';

const baseUrl = 'assets/download';

/**
 * 素材下载记录管理API
 * 适配新的后端接口设计
 */
export default {
    /**
     * 创建下载记录
     * 多次下载同一素材时，更新最新的下载时间
     * @param assetsId 素材ID
     * @param assetsType 素材类型 (10图片 20视频 30文档)
     */
    createDownloadRecord: async (assetsId: any, assetsType: number) => 
        await request.post(`${baseUrl}/record/${assetsId}/${assetsType}`),

    /**
     * 获取下载列表
     * @param param 查询条件
     * @param param.assetsType 素材类型（可选）10图片 20视频 30文档
     * @param param.pageNumber 页码（可选）
     * @param param.pageSize 每页大小（可选）
     */
    getDownloadList: async (param?: any) => {
        return await request.get(`${baseUrl}/list`, param);
    }
};
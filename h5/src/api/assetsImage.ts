import request from '@/common/request';

const baseUrl = 'assets/image';

/**
 * 图片素材管理API
 */
export default {
    /**
     * 创建图片素材（管理端功能）
     * @param param 图片素材参数
     */
    create: async (param: any) => await request.post(`${baseUrl}`, param),

    /**
     * 获取图片详情
     * @param id 图片ID
     */
    find: async (id: any) => await request.get(`${baseUrl}/${id}`),

    /**
     * 获取图片列表
     * @param param 查询条件（包含分页信息）
     */
    query: async (param?: any) => {
        return await request.get(`${baseUrl}`, param);
    },

    /**
     * 更新图片素材（管理端功能）
     * @param param 更新参数，包含id
     */
    update: async (param: any) =>
        await request.put(`${baseUrl}/${param.id}`, param),

    /**
     * 删除图片素材（管理端功能）
     * @param id 图片ID
     */
    del: async (id: any) => await request.del(`${baseUrl}/${id}`),

    /**
     * 上架图片（管理端功能）
     * @param id 图片ID
     */
    onShelf: async (id: any) => await request.put(`${baseUrl}/${id}/on-shelf`),

    /**
     * 下架图片（管理端功能）
     * @param id 图片ID
     */
    offShelf: async (id: any) => await request.put(`${baseUrl}/${id}/off-shelf`),

    /**
     * 设为精选（管理端功能）
     * @param id 图片ID
     */
    setHandpick: async (id: any) => await request.put(`${baseUrl}/${id}/set-handpick`),

    /**
     * 取消精选（管理端功能）
     * @param id 图片ID
     */
    cancelHandpick: async (id: any) => await request.put(`${baseUrl}/${id}/cancel-handpick`),

    /**
     * 下载图片
     * @param id 图片ID
     */
    download: async (id: any) => await request.get(`${baseUrl}/${id}/download`),

    /**
     * 获取精选图片
     * @param limit 限制数量
     */
    getFeatured: async (limit: number = 10) => 
        await request.get(`${baseUrl}/featured`, { limit })
};
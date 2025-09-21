import request from '@/common/request';

const baseUrl = 'assets/doc';

/**
 * 文档素材管理API
 */
export default {
    /**
     * 创建文档素材（管理端功能）
     * @param param 文档素材参数
     */
    create: async (param: any) => await request.post(`${baseUrl}`, param),

    /**
     * 获取文档详情
     * @param id 文档ID
     */
    find: async (id: any) => await request.get(`${baseUrl}/${id}`),

    /**
     * 获取文档列表
     * @param param 查询条件（包含分页信息）
     */
    query: async (param?: any) => {
        return await request.get(`${baseUrl}`, param);
    },

    /**
     * 更新文档素材（管理端功能）
     * @param param 更新参数，包含id
     */
    update: async (param: any) =>
        await request.put(`${baseUrl}/${param.id}`, param),

    /**
     * 删除文档素材（管理端功能）
     * @param id 文档ID
     */
    del: async (id: any) => await request.del(`${baseUrl}/${id}`),

    /**
     * 上架文档（管理端功能）
     * @param id 文档ID
     */
    onShelf: async (id: any) => await request.put(`${baseUrl}/${id}/on-shelf`),

    /**
     * 下架文档（管理端功能）
     * @param id 文档ID
     */
    offShelf: async (id: any) => await request.put(`${baseUrl}/${id}/off-shelf`),

    /**
     * 设为精选（管理端功能）
     * @param id 文档ID
     */
    setHandpick: async (id: any) => await request.put(`${baseUrl}/${id}/set-handpick`),

    /**
     * 取消精选（管理端功能）
     * @param id 文档ID
     */
    cancelHandpick: async (id: any) => await request.put(`${baseUrl}/${id}/cancel-handpick`),

    /**
     * 下载文档
     * @param id 文档ID
     */
    download: async (id: any) => await request.get(`${baseUrl}/${id}/download`),

    /**
     * 获取精选文档
     * @param limit 限制数量
     */
    getFeatured: async (limit: number = 10) => 
        await request.get(`${baseUrl}/featured`, { limit })
};
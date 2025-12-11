import request from '@/common/request';

const baseUrl = 'assets/collection';

/**
 * 素材专辑管理API
 */
export default {
    /**
     * 创建专辑（管理端功能）
     * @param param 专辑参数
     */
    create: async (param: any) => await request.post(`${baseUrl}`, param),

    /**
     * 获取专辑详情
     * @param id 专辑ID
     */
    find: async (id: any) => await request.get(`${baseUrl}/${id}`),

    /**
     * 获取专辑列表（不分页）
     * @param param 查询条件
     */
    query: async (param?: any) => {
        return await request.get(`${baseUrl}`, param);
    },

    /**
     * 更新专辑（管理端功能）
     * @param param 更新参数，包含id
     */
    update: async (param: any) =>
        await request.put(`${baseUrl}/${param.id}`, param),

    /**
     * 删除专辑（管理端功能）
     * @param id 专辑ID
     */
    del: async (id: any) => await request.del(`${baseUrl}/${id}`),

    /**
     * 获取精选专辑
     * @param limit 限制数量
     */
    getFeatured: async (limit: number = 10) => 
        await request.get(`${baseUrl}/featured`, { limit }),

    /**
     * 获取专辑中的素材
     * @param id 专辑ID
     * @param param 查询参数（包含分页信息）
     */
    getAssets: async (id: any, param?: any) => 
        await request.get(`${baseUrl}/${id}/assets`, param)
};
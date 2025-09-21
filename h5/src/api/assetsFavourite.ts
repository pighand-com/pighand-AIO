import request from '@/common/request';

const baseUrl = 'assets/favourite';

/**
 * 素材收藏管理API
 */
export default {
    /**
     * 创建收藏
     * @param param 收藏参数 { assetsId: number, assetsType: string, userId: number }
     */
    create: async (param: any) => await request.post(`${baseUrl}`, param),

    /**
     * 获取收藏详情
     * @param id 收藏记录ID
     */
    find: async (id: any) => await request.get(`${baseUrl}/${id}`),

    /**
     * 获取收藏列表
     * @param param 查询条件（包含分页信息）
     */
    query: async (param?: any) => {
        return await request.get(`${baseUrl}`, param);
    },

    /**
     * 更新收藏
     * @param param 更新参数，包含id
     */
    update: async (param: any) =>
        await request.put(`${baseUrl}/${param.id}`, param),

    /**
     * 删除收藏
     * @param id 收藏记录ID
     */
    del: async (id: any) => await request.del(`${baseUrl}/${id}`),

    /**
     * 批量删除收藏
     * @param ids 收藏记录ID数组
     */
    batchDelete: async (ids: any[]) => await request.del(`${baseUrl}/batch`, { ids }),

    /**
     * 检查是否已收藏
     * @param assetsId 素材ID
     * @param assetsType 素材类型
     */
    checkFavorite: async (assetsId: any, assetsType: string) => 
        await request.get(`${baseUrl}/check`, { assetsId, assetsType }),

    /**
     * 切换收藏状态（收藏/取消收藏）
     * @param param 参数 { assetsId: number, assetsType: string }
     */
    toggle: async (param: any) => await request.post(`${baseUrl}/toggle`, param)
};
import request from '@/common/request';

const baseUrl = 'assets/favourite';

/**
 * 素材收藏管理API
 * 适配新的后端接口设计
 */
export default {
    /**
     * 检查是否已收藏
     * @param assetsId 素材ID
     * @param assetsType 素材类型 (10图片 20视频 30文档)
     */
    checkFavorite: async (assetsId: any, assetsType: number) => 
        await request.get(`${baseUrl}/check/${assetsId}/${assetsType}`),

    /**
     * 收藏素材
     * @param assetsId 素材ID
     * @param assetsType 素材类型 (10图片 20视频 30文档)
     */
    addFavorite: async (assetsId: any, assetsType: number) => 
        await request.post(`${baseUrl}/add/${assetsId}/${assetsType}`),

    /**
     * 取消收藏
     * @param assetsId 素材ID
     * @param assetsType 素材类型 (10图片 20视频 30文档)
     */
    removeFavorite: async (assetsId: any, assetsType: number) => 
        await request.del(`${baseUrl}/remove/${assetsId}/${assetsType}`),

    /**
     * 获取收藏列表
     * @param param 查询条件
     * @param param.assetsType 素材类型（可选）10图片 20视频 30文档
     * @param param.pageNumber 页码（可选）
     * @param param.pageSize 每页大小（可选）
     */
    getFavoriteList: async (param?: any) => {
        return await request.get(`${baseUrl}/list`, param);
    },

    // 保留旧接口以兼容现有代码
    /**
     * @deprecated 使用 addFavorite 替代
     * 创建收藏
     * @param param 收藏参数 { assetsId: number, assetsType: string }
     */
    create: async (param: any) => {
        const assetsType = getAssetsTypeNumber(param.assetsType);
        return await request.post(`${baseUrl}/add/${param.assetsId}/${assetsType}`);
    },

    /**
     * @deprecated 使用 removeFavorite 替代
     * 删除收藏
     * @param assetsId 素材ID，如果是旧格式则为收藏记录ID
     */
    del: async (assetsId: any) => {
        // 这里需要根据实际情况处理，暂时保留原逻辑
        return await request.del(`assets/favourite/${assetsId}`);
    },

    /**
     * @deprecated 使用 getFavoriteList 替代
     * 获取收藏列表
     * @param param 查询条件（包含分页信息）
     */
    query: async (param?: any) => {
        return await request.get(`${baseUrl}/list`, param);
    }
};

/**
 * 将字符串类型转换为数字类型
 * @param assetsType 素材类型字符串
 * @returns 素材类型数字
 */
function getAssetsTypeNumber(assetsType: string): number {
    const typeMap: { [key: string]: number } = {
        'image': 10,
        'video': 20,
        'doc': 30
    };
    return typeMap[assetsType] || 10;
}
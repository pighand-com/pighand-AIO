import request from '@/common/request';

const baseUrl = 'assets/classification';

/**
 * 素材分类管理API
 */
export default {
    /**
     * 获取分类列表（不分页）
     * @param param 查询条件
     */
    query: async (param?: any) => {
        return await request.get(`${baseUrl}`, param);
    },

};
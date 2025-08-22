import request from '@/common/request';

export default {
    /**
     * 获取门店列表
     */
    query: async (params?: any) => await request.get('store', { params }),
    
    /**
     * 获取详情
     */
    find: async (id: string) => await request.get(`store/${id}`),
};

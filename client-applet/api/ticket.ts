import request from '@/common/request';

export default {
    /**
     * 列表
     */
    query: async (params: any) => await request.get(`ticket`, params),

    /**
     * 我的列表
     */
    queryMine: async (params: any) => await request.get(`ticket/mine`, params),
};

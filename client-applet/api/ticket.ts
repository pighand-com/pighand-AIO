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

    /**
     * 获取详情
     */
    find: async (id: string) => await request.get(`ticket/${id}`),

    /**
     * 核销票务
     */
    validation: async (id: string) => await request.post(`ticket/user/${id}/validation`, {}),
};

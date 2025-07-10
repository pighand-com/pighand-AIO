import request from '@/common/request';

export default {
    /**
     * 下单
     */
    create: async (params: any) => await request.post(`order`, params),

    /**
     * 订单支付
     */
    pay: async (id: any, outTradePlatform: string) => await request.post(`order/${id}/pay`, { outTradePlatform }),

    /**
     * 列表
     */
    query: async (params: any) => await request.get(`order`, params),

    /**
     * 详情
     */
    find: async (id: any) => await request.get(`order/${id}`),

    /**
     * 取消订单
     */
    cancel: async (id: any) => await request.post(`order/${id}/cancel`),
};

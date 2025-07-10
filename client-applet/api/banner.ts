import request from '@/common/request';

export default {
    /**
     * 列表
     */
    query: async () =>
        await request.get(`banner`),
};

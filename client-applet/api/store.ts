import request from '@/common/request';

export default {
    /**
     * 获取详情
     */
    find: async (id: string) => await request.get(`store/${id}`),
};

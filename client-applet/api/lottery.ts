import request from '@/common/request';

export default {
    /**
     * 获取抽奖列表
     */
    query: async () =>
        await request.get(`lottery?pageSize=10&pageNumber=1`),

    /**
     * 获取抽奖详情
     * @param id 抽奖ID
     */
    getDetail: async (id: string) =>
        await request.get(`lottery/${id}`),

    /**
     * 获取参与人员列表
     * @param id 抽奖ID
     * @param pageSize 页面大小
     * @param pageNumber 页码
     */
    getParticipants: async (id: string, pageSize: number = 10, pageNumber: number = 1) =>
        await request.get(`lottery/${id}/participate`, {
            pageSize,
            pageNumber
        }),

    /**
     * 参与抽奖
     * @param id 抽奖ID
     */
    participate: async (id: string) =>
        await request.post(`lottery/${id}`)
};
import request from '@/common/request';

const baseUrl = 'lottery';

export default {
    create: async (param: any) => await request.post(`${baseUrl}`, param),
    find: async (id: any) => await request.get(`${baseUrl}/${id}`),
    query: async (param?: any) => {
        if (param.createdAtRange) {
            param.createdAtRange = param.createdAtRange.join(',');
        }
        return await request.get(`${baseUrl}`, param);
    },
    update: async (param: any) =>
        await request.put(`${baseUrl}/${param.id}`, param),
    del: async (id: any) => await request.del(`${baseUrl}/${id}`),

    /**
     * 获取参与人员列表
     * @param id 抽奖ID
     * @param pageSize 页面大小
     * @param pageNumber 页码
     */
    getParticipants: async (
        id: string,
        pageSize: number = 10,
        pageNumber: number = 1
    ) =>
        await request.get(`lottery/${id}/participate`, {
            pageSize,
            pageNumber,
            isOrderPrize: true
        })
};

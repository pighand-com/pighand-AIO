import request from '@/common/request';

const baseUrl = 'check-in/location';

export default {
    create: async (param: any) => await request.post(`${baseUrl}`, param),
    find: async (id: any) => await request.get(`${baseUrl}/${id}`),
    query: async (param?: any) => {
        if (param?.createdAtRange) {
            param.createdAtRange = param.createdAtRange.join(',');
        }
        return await request.get(`${baseUrl}`, param);
    },
    update: async (param: any) =>
        await request.put(`${baseUrl}/${param.id}`, param),
    del: async (id: any) => await request.del(`${baseUrl}/${id}`),

    /**
     * 获取打卡参与人员列表
     * @param locationId 打卡地点ID
     * @param pageSize 页面大小
     * @param pageNumber 页码
     */
    getParticipants: async (
        locationId: string,
        pageSize: number = 10,
        pageNumber: number = 1
    ) =>
        await request.get(`checkInUser`, {
            locationId,
            pageSize,
            pageNumber
        }),

    /**
     * 获取打卡记录列表
     * @param locationId 打卡地点ID
     * @param pageSize 页面大小
     * @param pageNumber 页码
     */
    getCheckInRecords: async (
        locationId: string,
        pageSize: number = 10,
        pageNumber: number = 1
    ) =>
        await request.get(`checkInRecord`, {
            locationId,
            pageSize,
            pageNumber
        })
};
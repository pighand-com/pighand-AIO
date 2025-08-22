import request from '@/common/request';

const baseUrl = 'ticket';

export default {
    create: async (param: any) => await request.post(`${baseUrl}`, param),
    find: async (id: any) => await request.get(`${baseUrl}/${id}`),
    query: async (param?: any) => {
        return await request.get(`${baseUrl}`, param);
    },
    update: async (param: any) =>
        await request.put(`${baseUrl}/${param.id}`, param),
    del: async (id: any) => await request.del(`${baseUrl}/${id}`),

    // 用户票列表
    queryUserTicket: async (userId: any, usable: boolean) => {
        return await request.get(`${baseUrl}/user`, {
            creatorId: userId,
            usable
        });
    },

    // 核销
    validation: async (id?: any, validationCount?: number) => {
        return await request.post(`${baseUrl}/user/validation`, [
            {
                id,
                validationCount
            }
        ]);
    },

    // 取消核销
    cancelValidation: async (id?: any, validationCount?: number) => {
        const params = validationCount ? { validationCount } : {};
        return await request.put(`${baseUrl}/user/${id}/validation`, params);
    }
};

import request from '@/common/request';

const baseUrl = 'theme';

export default {
    create: async (param: any) => await request.post(`${baseUrl}`, param),
    find: async (id: any) => await request.get(`${baseUrl}/${id}`),
    query: async (param?: any) => {
        return await request.get(`${baseUrl}`, param);
    },
    update: async (param: any) =>
        await request.put(`${baseUrl}/${param.id}`, param),
    del: async (id: any) => await request.del(`${baseUrl}/${id}`)
};
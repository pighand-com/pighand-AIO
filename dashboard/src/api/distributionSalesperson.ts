import request from '@/common/request';

const baseUrl = 'distribution/salesperson';

export default {
    create: async (param: any) => await request.post(`${baseUrl}`, param),
    find: async (id: any) => await request.get(`${baseUrl}/${id}`),
    query: async (param?: any) => {
        return await request.get(`${baseUrl}`, param);
    },
    update: async (param: any) =>
        await request.put(`${baseUrl}/${param.id}`, param),
    del: async (id: any) => await request.del(`${baseUrl}/${id}`),
    enable: async (id: any) => await request.post(`${baseUrl}/${id}/enable`),
    disable: async (id: any) => await request.post(`${baseUrl}/${id}/disable`),
    qrCode: async (id: any) => await request.get(`${baseUrl}/${id}/qr_code`)
};

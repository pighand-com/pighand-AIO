import request from '@/common/request';

const baseUrl = 'distribution/sales';

export default {
    query: async (salespersonId: any) =>
        await request.get(`${baseUrl}`, { salespersonId }),
    statistics: async (salespersonId: any) =>
        await request.get(`${baseUrl}/statistics/status`, { salespersonId }),
    settlement: async (salespersonId: any, settledAmount: number, settledDetailIds: any[]) =>
        await request.post(`${baseUrl}`, { salespersonId, settledAmount, settledDetailIds })
};

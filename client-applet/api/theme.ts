import request from '@/common/request';

export default {
    /**
     * 列表
     */
    query: async () => await request.get(`theme`),
	
    /**
     * 详情
     * @param id 
     * @returns 
     */
    find: async (id: string) => await  request.get(`theme/${id}`),
	
    /**
     * 修改排队时间
     * @param id 
     * @returns 
     */
    updateQueueDuration: async (id: string, queueDuration: number) => await  request.put(`theme/${id}/queue_duration`, {
        queueDuration,
    })
};

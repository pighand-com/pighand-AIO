import request from '@/common/request';

export default {
    /**
     * 获取打卡地点列表
     */
    getLocations: async () =>
        await request.get(`check-in/locations`),

    /**
     * 获取打卡地点详情（已废弃）
     * @param id 打卡地点ID
     */
    getDetail: async (id: string) =>
        await request.get(`check-in/locations/${id}`),

    /**
     * 参加打卡活动
     * @param fromQrCode 是否来自扫码
     */
    join: async (id?: string) => {
        const params = { id };
        return await request.post(`check-in/join`, params);
    },

    /**
     * 参与打卡活动（已废弃，使用join代替）
     * @param locationId 打卡地点ID
     */
    participate: async (locationId: string) =>
        await request.post(`check-in/join`),

    /**
     * 执行打卡
     * @param locationId 打卡地点ID
     */
    checkIn: async (locationId: string, userId: string) =>
        await request.post(`check-in/checkIn/${locationId}/${userId}`),

    /**
     * 获取用户活动状态
     */
    getActivityStatus: async () =>
        await request.get(`check-in/status`),

    /**
     * 获取今日打卡记录
     */
    getTodayRecords: async () =>
        await request.get(`check-in/records/today`),

    /**
     * 延长打卡时间（已废弃）
     * @param locationId 打卡地点ID
     * @param days 延长天数
     */
    extendTime: async (locationId: string, days: number) =>
        await request.post(`check-in/extend`, {
            days
        }),

    /**
     * 获取打卡记录
     * @param locationId 打卡地点ID
     * @param pageSize 页面大小
     * @param pageNumber 页码
     */
    getCheckInRecords: async (locationId: string, pageSize: number = 10, pageNumber: number = 1) =>
        await request.get(`checkInRecord`, {
            locationId,
            pageSize,
            pageNumber
        }),

    /**
     * 获取打卡记录（别名）
     * @param locationId 打卡地点ID
     * @param options 查询选项
     */
    getRecords: async (locationId: string, options: any = {}) =>
        await request.get(`checkInRecord`, {
            locationId,
            pageSize: options.pageSize || 10,
            pageNumber: options.pageNumber || 1,
            ...options
        })
};
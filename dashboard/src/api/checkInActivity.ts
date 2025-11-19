import request from '@/common/request';

const baseUrl = 'check-in/activity';

/**
 * 打卡活动数据接口
 */
export interface CheckInActivityData {
    id?: number;
    name: string;
    durationMinutes: number; // 活动时长（分钟）
}

export default {
    /**
     * 获取打卡活动列表
     * @param params 查询参数
     */
    getList: (params?: any) => {
        return request.get(`${baseUrl}`, params);
    },

    /**
     * 获取打卡活动详情
     * @param id 活动ID
     */
    getDetail: (id: number) => {
        return request.get(`${baseUrl}/${id}`);
    },

    /**
     * 创建打卡活动
     * @param data 活动数据
     */
    create: (data: CheckInActivityData) => {
        return request.post(baseUrl, data);
    },

    /**
     * 更新打卡活动
     * @param id 活动ID
     * @param data 活动数据
     */
    update: (id: number, data: CheckInActivityData) => {
        return request.put(`${baseUrl}/${id}`, data);
    },

    /**
     * 删除打卡活动
     * @param id 活动ID
     */
    del: (id: number) => {
        return request.del(`${baseUrl}/${id}`);
    },

    /**
     * 批量删除打卡活动
     * @param ids 活动ID数组
     */
    batchDelete: (ids: number[]) => {
        return request.del(`${baseUrl}/batch`, { data: { ids } });
    },

    /**
     * 生成活动二维码
     * @param id 活动ID
     */
    generateQRCode: (id: number) => {
        return request.get(`${baseUrl}/${id}/qrcode`);
    },

    getStats: (id: number, date: string) => {
        return request.get(`${baseUrl}/${id}/stats`, { date });
    }
};
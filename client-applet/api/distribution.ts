import request from '@/common/request';

export default {
    /**
     * 查询分销资格
     */
    salesperson: async () => await request.get(`distribution/salesperson`),

    /**
     * 分销二维码
     */
    QRCode: async () => await request.get(`distribution/salesperson/qr_code`),

    /**
     * 分销统计
     */
    statistics: async () => await request.get(`distribution/sales/statistics`),

    /**
     * 分销记录
     */
    querySales: async () => await request.get(`distribution/sales`),

    /**
     * 分销记录明细
     */
    querySalesDetail: async (id: string) => await request.get(`distribution/sales/${id}/detail`),
};

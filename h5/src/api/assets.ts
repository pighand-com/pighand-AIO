import request from '@/common/request';

const baseUrl = 'assets';

/**
 * 素材聚合管理API
 * 支持统一查询图片、视频、文档三种类型的素材
 */
export default {
    /**
     * 聚合查询素材（图片、视频、文档）
     * 支持关键词搜索和分类筛选
     * @param param 查询条件
     * @param param.keyword 关键词搜索（可选）
     * @param param.classificationId 分类ID（可选，1-图片 2-视频 3-课件）
     * @param param.assetTypes 素材类型数组（可选，['image', 'video', 'doc']）
     * @param param.pageNumber 页码（可选，默认1）
     * @param param.pageSize 每页数量（可选，默认20）
     * @param param.onlyHandpick 是否只查询精选（可选）
     * @param param.collectionIds 专辑ID列表（可选）
     * @param param.statusList 状态列表（可选）
     * @param param.startTime 开始时间（可选）
     * @param param.endTime 结束时间（可选）
     */
    aggregate: async (param?: any) => {
        return await request.get(`${baseUrl}/aggregate`, param);
    },

    /**
     * 获取文档详情
     * @param id 文档ID
     */
    findDoc: async (id: any) => await request.get(`${baseUrl}/doc/${id}`),

    /**
     * 获取图片详情
     * @param id 图片ID
     */
    findImage: async (id: any) => await request.get(`${baseUrl}/image/${id}`),

    /**
     * 获取视频详情
     * @param id 视频ID
     */
    findVideo: async (id: any) => await request.get(`${baseUrl}/video/${id}`),

    /**
     * 根据素材类型和ID获取详情
     * @param type 素材类型 ('image' | 'video' | 'doc')
     * @param id 素材ID
     */
    findByType: async (type: string, id: any) => {
        switch (type) {
            case 'image':
                return await request.get(`${baseUrl}/image/${id}`);
            case 'video':
                return await request.get(`${baseUrl}/video/${id}`);
            case 'doc':
                return await request.get(`${baseUrl}/doc/${id}`);
            default:
                throw new Error(`不支持的素材类型: ${type}`);
        }
    },

    /**
     * 下载素材
     * @param type 素材类型
     * @param id 素材ID
     */
    download: async (type: string, id: any) => {
        return await request.get(`${baseUrl}/${type}/${id}/download`);
    }
};
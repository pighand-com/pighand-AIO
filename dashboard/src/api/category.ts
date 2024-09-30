import request from '@/common/request';

const baseUrl = 'article/category';

export default {
    query: async (param?: any) => await request.get(`${baseUrl}/all`, param)
};

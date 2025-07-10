import request from '@/common/request';

const baseUrl = 'role';

export default {
    query: async (param?: any) => {
        return await request.get(`${baseUrl}`, param);
    }
};

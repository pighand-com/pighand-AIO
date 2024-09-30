import request from '@/common/request';

export default {
    login: async (param: any) => await request.post(`login`, param)
};

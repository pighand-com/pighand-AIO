import request from '@/common/request';

const baseUrl = 'user';

export default {
    applications: async () => await request.get('applications'),

    login: async (param: any) => await request.post('login', param)
};

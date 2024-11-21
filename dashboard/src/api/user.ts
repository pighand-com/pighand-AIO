import request from '@/common/request';

const baseUrl = 'user';

export default {
    login: async ({ captcha, captchaId, ...param }: any) =>
        await request.post(
            `login?captcha=${captcha}&captchaId=${captchaId}`,
            param
        ),

    create: async (param: any) => await request.post(`${baseUrl}`, param),
    find: async (id: any) => await request.get(`${baseUrl}/${id}`),
    query: async (param?: any) => {
        if (param.createdAtRange) {
            param.createdAtRange = param.createdAtRange.join(',');
        }
        return await request.get(`${baseUrl}`, param);
    },
    update: async (param: any) =>
        await request.put(`${baseUrl}/${param.id}`, param),
    del: async (id: any) => await request.del(`${baseUrl}/${id}`)
};

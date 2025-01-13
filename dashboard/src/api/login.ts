import request from '@/common/request';

const baseUrl = 'user';

export default {
    applications: async () => await request.get('applications'),

    login: async ({ captcha, captchaId, ...param }: any) =>
        await request.post(
            `login?captcha=${captcha}&captchaId=${captchaId}`,
            param
        )
};

import request from '@/common/request';

export default {
    login: async ({ captcha, captchaId, ...param }: any) =>
        await request.post(
            `login?captcha=${captcha}&captchaId=${captchaId}`,
            param
        )
};

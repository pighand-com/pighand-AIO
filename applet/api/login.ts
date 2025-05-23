import request from '@/common/request';

export default {
    login: async (code: string) =>
        await request.post(
            `login/wechat/applet`,
            {code}
        )
};

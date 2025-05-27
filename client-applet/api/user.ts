import request from '@/common/request';

export default {
    /**
     * 绑定微信手机号
     * @param code 微信授权码
     */
    bindPhoneWechat: async (code: string) =>
        await request.post(
            `user/bind/phone/wechat`,
            {code}
    ),
	
	/**
	 * 修改自己的信息
	 */
	updateSelf: async(data:any) => {
		await request.put(
		    `user/self`,
		    data)
	}
};

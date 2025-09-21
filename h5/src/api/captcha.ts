import request from '@/common/request';

/**
 * 人机验证码管理API
 */
export default {
    /**
     * 获取客户端验证码
     * @param key 验证码标识键
     */
    getClientCode: async (key: string) => 
        await request.get(`CAPTCHA/code`, { key }),

    /**
     * 验证验证码
     * @param param 验证参数 { codeId: string, code: string }
     */
    verify: async (param: any) => 
        await request.post(`CAPTCHA/verify`, param),

    /**
     * 刷新验证码
     * @param codeId 验证码ID
     */
    refresh: async (codeId: string) => 
        await request.post(`CAPTCHA/refresh`, { codeId })
};
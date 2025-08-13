import { getToken, setBatchStorage } from '@/common/storage';
import api from '@/api/login';

/**
 * 登录
 * @param options 登录选项
 * @param options.silent 是否静默登录
 */
export default async (options: { silent?: false } = {}) => {
    const token = getToken();

    if (token) {
        return;
    }

    uni.showLoading({
        title: '登录中...'
    });

    try {
        const loginRes: any = await new Promise((resolve, reject) => {
            uni.login({
                success: (res) => resolve(res),
                fail: (err) => reject(err)
            });
        });
        
        const { code } = loginRes;
        const res = await api.login(code);
        
        if (!res) {
            return;
        }
        
        const { token, application, ...userInfo } = res;
        // 使用批量设置方法，只触发一次storage-changed事件
        setBatchStorage({
            token,
            userInfo,
            applicationInfo: application
        });

        if (!options.silent) {
            uni.showToast({
                title: '登录成功',
                icon: 'success'
            });
        }
    } catch (error) {
        if (!options.silent) {
            uni.showToast({
                title: '登录失败',
                icon: 'error'
            });
        }
    }
}

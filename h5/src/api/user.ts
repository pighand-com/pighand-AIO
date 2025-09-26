import request from '@/common/request';

/**
 * 用户相关API
 */

/**
 * 更新用户信息
 * @param userInfo 用户信息对象
 * @returns 更新结果
 */
const updateUserInfo = async (userInfo: any) => {
    try {
        const response = await request.put('user/self', userInfo);
        return response;
    } catch (error) {
        console.error('更新用户信息失败:', error);
        throw error;
    }
};

/**
 * 获取当前用户信息
 * @returns 用户信息
 */
const getCurrentUserInfo = async () => {
    try {
        const response = await request.get('/api/user/current');
        return response.data;
    } catch (error) {
        console.error('获取用户信息失败:', error);
        throw error;
    }
};

export default {
    updateUserInfo,
    getCurrentUserInfo
};
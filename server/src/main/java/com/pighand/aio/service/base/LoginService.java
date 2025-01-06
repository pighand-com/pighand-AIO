package com.pighand.aio.service.base;

import com.pighand.aio.vo.base.UserVO;

/**
 * 登录相关接口
 */
public interface LoginService {
    /**
     * 用户名密码登录
     *
     * @param username
     * @param password
     * @return token
     */
    UserVO loginByPassword(String username, String password);
}

package com.pighand.aio.service.user;

import com.pighand.aio.vo.user.UserVO;

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
    UserVO byPassword(String username, String password);
}

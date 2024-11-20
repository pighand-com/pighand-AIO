package com.pighand.aio.service.base;

import com.pighand.aio.domain.base.ApplicationAuthorizationDomain;
import com.pighand.aio.vo.base.LoginUser;
import com.pighand.aio.vo.base.UserVO;

import java.security.NoSuchAlgorithmException;

/**
 * 授权服务
 *
 * @author wangshuli
 */
public interface AuthorizationService {

    /**
     * 生成hash token
     *
     * @param authorization
     * @param userId
     * @throws NoSuchAlgorithmException
     * @returns hashPrefix + hash(base64)
     */
    String generateHashToken(ApplicationAuthorizationDomain authorization, Long userId);

    /**
     * 生成授权
     *
     * @param user UserDomain
     * @throws NoSuchAlgorithmException
     * @returns token
     */
    UserVO generateToken(UserVO user);

    /**
     * 校验token
     *
     * <p>jwt根据_c判断是否校验redis，否则只校验jwt hash校验redis
     *
     * @param authorization
     * @returns userId
     */
    LoginUser checkToken(String authorization);

    /**
     * 登出
     *
     * <p>删除redis中的授权信息
     *
     * @param authorization
     */
    void logout(String authorization);
}

package com.pighand.aio.service.base;

import com.pighand.aio.domain.base.UserBindDomain;
import com.pighand.framework.spring.base.BaseService;

/**
 * 用户 - 绑定信息
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
public interface UserBindService extends BaseService<UserBindDomain> {

    /**
     * 判断是否绑定
     *
     * @return true: 已绑定；false: 未绑定
     */
    boolean isBind();

    /**
     * 绑定用户
     *
     * @param bindUserId
     */
    void bind(Long bindUserId);

    /**
     * 解绑用户
     *
     * @param bindUserId
     */
    void unbind(Long bindUserId);
}

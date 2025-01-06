package com.pighand.aio.service.base.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.domain.base.UserBindDomain;
import com.pighand.aio.mapper.base.UserBindMapper;
import com.pighand.aio.service.base.UserBindService;
import com.pighand.aio.vo.base.LoginUser;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.exception.ThrowPrompt;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.pighand.aio.domain.base.table.UserBindTableDef.USER_BIND;

/**
 * 用户 - 绑定信息
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@AllArgsConstructor
@Service
public class UserBindServiceImpl extends BaseServiceImpl<UserBindMapper, UserBindDomain> implements UserBindService {

    /**
     * 判断是否绑定
     *
     * @return true: 已绑定；false: 未绑定
     */
    @Override
    public boolean isBind() {
        Long applicationId = Context.applicationId();
        LoginUser loginUser = Context.loginUser();

        UserBindDomain userBindDomain = this.queryChain().select(UserBindDomain::getId)
            .where(USER_BIND.APPLICATION_ID.eq(applicationId).and(USER_BIND.USER_ID.eq(loginUser.getId()))).one();

        return userBindDomain == null;
    }

    /**
     * 绑定用户
     *
     * @param bindUserId
     */
    @Override
    public void bind(Long bindUserId) {
        Long applicationId = Context.applicationId();
        LoginUser loginUser = Context.loginUser();

        if (loginUser.getId().equals(bindUserId)) {
            throw new ThrowPrompt("不能绑定自己");
        }

        boolean isBind = this.isBind();
        if (isBind) {
            throw new ThrowPrompt("已绑定");
        }

        UserBindDomain userBindDomain = new UserBindDomain();
        userBindDomain.setApplicationId(applicationId);
        userBindDomain.setUserId(loginUser.getId());
        userBindDomain.setUpperId(bindUserId);
        userBindDomain.setCreatedAt(new Date());

        this.save(userBindDomain);
    }

    /**
     * 解绑用户
     *
     * @param bindUserId
     */
    @Override
    public void unbind(Long bindUserId) {
        Long applicationId = Context.applicationId();
        LoginUser loginUser = Context.loginUser();

        QueryWrapper queryWrapper = QueryWrapper.create().and(
            USER_BIND.APPLICATION_ID.eq(applicationId).and(USER_BIND.USER_ID.eq(loginUser.getId()))
                .and(USER_BIND.UPPER_ID.eq(bindUserId)));
        super.mapper.deleteByQuery(queryWrapper);
    }

}

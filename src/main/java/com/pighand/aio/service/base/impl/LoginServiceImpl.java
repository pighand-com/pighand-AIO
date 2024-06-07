package com.pighand.aio.service.base.impl;

import com.pighand.aio.common.enums.UserStatusEnum;
import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.entityMapper.base.UserEntityMapper;
import com.pighand.aio.service.base.AuthorizationService;
import com.pighand.aio.service.base.LoginService;
import com.pighand.aio.service.base.UserService;
import com.pighand.aio.vo.base.UserVO;
import com.pighand.framework.spring.exception.ThrowPrompt;
import com.pighand.framework.spring.util.VerifyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.pighand.aio.domain.base.table.UserTableDef.USER;

/**
 * 登录相关接口
 */
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserEntityMapper userEntityMapper;

    private final UserService userService;
    private final AuthorizationService authorizationService;

    @Override
    public UserVO byPassword(String username, String password) {
        Long projectId = Context.getProjectId();
        UserVO user = userService.queryChain().select(USER.ID, USER.PASSWORD, USER.USERNAME).select(USER.STATUS)
            .where(USER.PROJECT_ID.eq(projectId)).and(USER.USERNAME.eq(username)).or(USER.EMAIL.eq(username))
            .or(USER.PHONE.eq(username)).oneAs(UserVO.class);

        if (user == null) {
            throw new ThrowPrompt("用户或密码错误");
        }

        if (UserStatusEnum.STOP.equals(user.getPassword())) {
            throw new ThrowPrompt("您的账号已停用");
        }

        if (VerifyUtils.isEmpty(user.getPassword())) {
            throw new ThrowPrompt("未设置密码，请使用其他方式登录");
        }

        boolean isMatch = new BCryptPasswordEncoder().matches(password, user.getPassword());
        if (!isMatch) {
            throw new ThrowPrompt("用户或密码错误");
        }

        return authorizationService.generateToken(user);
    }
}

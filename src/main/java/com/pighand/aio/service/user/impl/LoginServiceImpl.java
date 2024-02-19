package com.pighand.aio.service.user.impl;

import com.pighand.aio.common.enums.UserStatusEnum;
import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.domain.user.table.UserTableDef;
import com.pighand.aio.entityMapper.user.UserEntityMapper;
import com.pighand.aio.service.user.AuthorizationService;
import com.pighand.aio.service.user.LoginService;
import com.pighand.aio.service.user.UserService;
import com.pighand.aio.vo.user.UserVO;
import com.pighand.framework.spring.exception.ThrowPrompt;
import com.pighand.framework.spring.util.VerifyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
        UserTableDef userTableDef = UserTableDef.USER;
        Long projectId = Context.getProjectId();
        UserVO user =
            userService.queryChain().select(userTableDef.ID, userTableDef.PASSWORD).select(userTableDef.STATUS)
                .where(userTableDef.PROJECT_ID.eq(projectId)).and(userTableDef.USERNAME.eq(username))
                .or(userTableDef.EMAIL.eq(username)).or(userTableDef.PHONE.eq(username)).oneAs(UserVO.class);

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

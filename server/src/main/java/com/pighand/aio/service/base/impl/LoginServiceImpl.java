package com.pighand.aio.service.base.impl;

import com.pighand.aio.common.enums.UserStatusEnum;
import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.domain.base.ApplicationDomain;
import com.pighand.aio.entityMapper.base.UserEntityMapper;
import com.pighand.aio.service.base.ApplicationService;
import com.pighand.aio.service.base.AuthorizationService;
import com.pighand.aio.service.base.LoginService;
import com.pighand.aio.service.base.UserService;
import com.pighand.aio.vo.base.UserVO;
import com.pighand.framework.spring.exception.ThrowPrompt;
import com.pighand.framework.spring.extension.mybatis.flex.PHQueryChain;
import com.pighand.framework.spring.util.VerifyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.pighand.aio.domain.base.table.ApplicationTableDef.APPLICATION;
import static com.pighand.aio.domain.base.table.UserTableDef.USER;

/**
 * 登录相关接口
 */
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserEntityMapper userEntityMapper;
    private final ApplicationService applicationService;

    private final UserService userService;
    private final AuthorizationService authorizationService;

    /**
     * 获取关联应用
     *
     * @param users
     * @param relevanceUsers
     * @param firstMatchUser
     * @return
     */
    private List<ApplicationDomain> getRelevanceApplications(List<UserVO> users, List<UserVO> relevanceUsers,
        UserVO firstMatchUser) {
        List<ApplicationDomain> relevanceApplications = new ArrayList<>(users.size() - 1 + relevanceUsers.size());

        Set ApplicationIds = new HashSet(users.size() - 1 + relevanceUsers.size());
        ApplicationIds.add(firstMatchUser.getApplicationId());

        for (int i = 1; i < users.size(); i++) {
            if (!ApplicationIds.add(users.get(i).getApplicationId())) {
                ApplicationDomain application = new ApplicationDomain();
                application.setId(users.get(i).getApplicationId());
                application.setName(users.get(i).getApplication().getName());
                relevanceApplications.add(application);
            }
        }

        for (UserVO relevanceUser : relevanceUsers) {
            if (!ApplicationIds.add(relevanceUser.getApplicationId())) {
                ApplicationDomain application = new ApplicationDomain();
                application.setId(relevanceUser.getApplicationId());
                application.setName(relevanceUser.getApplication().getName());
                relevanceApplications.add(application);
            }
        }
        return relevanceApplications;
    }

    @Override
    public UserVO loginByPassword(String username, String password) {
        Long applicationId = Context.applicationId();

        PHQueryChain queryChain = userService.queryChain();
        queryChain.fullSelect(USER.ID, USER.PASSWORD, USER.USERNAME, USER.STATUS, APPLICATION.ID, APPLICATION.NAME,
            APPLICATION.UPLOAD_TYPE).from(USER).innerJoin(APPLICATION).on(APPLICATION.ID.eq(USER.APPLICATION_ID));

        if (applicationId != null) {
            queryChain.where(USER.APPLICATION_ID.eq(applicationId)).and(USER.USERNAME.eq(username))
                .or(USER.EMAIL.eq(username)).or(USER.PHONE.eq(username)).limit(1);
        } else {
            queryChain.where(USER.PHONE.eq(username)).or(USER.EMAIL.eq(username));
        }

        List<UserVO> users = queryChain.listAs(UserVO.class);

        if (users.size() == 0) {
            throw new ThrowPrompt("用户或密码错误");
        }

        boolean isNoPwd = false;
        int stopCount = 0;
        List<UserVO> matchUsers = new ArrayList<>(users.size());
        for (UserVO user : users) {
            if (VerifyUtils.isEmpty(user.getPassword())) {
                isNoPwd = true;
                continue;
            }

            if (new BCryptPasswordEncoder().matches(password, user.getPassword())) {
                if (UserStatusEnum.STOP.equals(user.getStatus())) {
                    stopCount++;
                }

                matchUsers.add(user);
            }
        }

        if (matchUsers.size() == 0) {
            throw new ThrowPrompt(isNoPwd ? "未设置密码，请使用其他方式登录" : "用户或密码错误");
        }

        if (stopCount >= matchUsers.size()) {
            throw new ThrowPrompt("您的账号已停用");
        }

        UserVO firstMatchUser = matchUsers.get(0);

        List<UserVO> relevanceUsers = new ArrayList<>(0);
        if (applicationId == null) {
            String relevanceKey = "";
            String relevanceValue = "";

            if (username.equals(firstMatchUser.getPhone())) {
                relevanceKey = "email";
                relevanceValue = firstMatchUser.getEmail();
            } else if (username.equals(firstMatchUser.getEmail())) {
                relevanceKey = "phone";
                relevanceValue = firstMatchUser.getPhone();
            }

            if (VerifyUtils.isNotEmpty(relevanceValue)) {
                PHQueryChain relevanceUserQueryChain = userService.queryChain();
                relevanceUserQueryChain.select(USER.APPLICATION_ID, APPLICATION.NAME).innerJoin(APPLICATION)
                    .on(APPLICATION.ID.eq(USER.APPLICATION_ID));

                if (relevanceKey.equals("email")) {
                    relevanceUserQueryChain.where(USER.EMAIL.eq(relevanceValue));
                } else {
                    relevanceUserQueryChain.where(USER.PHONE.eq(relevanceValue));
                }

                relevanceUsers = relevanceUserQueryChain.listAs(UserVO.class);
            }

        }

        if (users.size() > 1 || relevanceUsers.size() > 0) {
            List<ApplicationDomain> relevanceApplications =
                getRelevanceApplications(users, relevanceUsers, firstMatchUser);

            firstMatchUser.setRelevanceApplications(relevanceApplications);
        }

        return authorizationService.generateToken(firstMatchUser);
    }
}

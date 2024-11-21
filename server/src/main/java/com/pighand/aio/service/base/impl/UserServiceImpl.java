package com.pighand.aio.service.base.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.common.enums.RoleEnum;
import com.pighand.aio.common.enums.UserStatusEnum;
import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.domain.base.UserDomain;
import com.pighand.aio.mapper.base.UserMapper;
import com.pighand.aio.service.base.UserExtensionService;
import com.pighand.aio.service.base.UserService;
import com.pighand.aio.vo.base.CheckUserExist;
import com.pighand.aio.vo.base.UserVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.exception.ThrowPrompt;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.page.PageType;
import com.pighand.framework.spring.util.VerifyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.pighand.aio.domain.base.table.UserExtensionTableDef.USER_EXTENSION;
import static com.pighand.aio.domain.base.table.UserTableDef.USER;

/**
 * 用户关键信息表，主要保存登录所用信息
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends BaseServiceImpl<UserMapper, UserDomain> implements UserService {

    private final UserExtensionService userExtensionService;
    ObjectMapper om = new ObjectMapper();

    /**
     * 判断是否是手机号
     *
     * @param phone
     * @return
     */
    private boolean isPhone(String phone) {
        return phone.matches("^1[3-9]\\d{9}$");
    }

    /**
     * 判断是否是邮箱
     *
     * @param email
     * @return
     */
    private boolean isEmail(String email) {
        return email.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
    }

    /**
     * 查询用户存在信息
     *
     * @param username
     * @param phone
     * @param email
     * @param userId   库中用户id，用来排除当前用户
     * @return
     */
    private CheckUserExist getExistInfo(String username, String phone, String email, Long userId) {
        boolean hasUsername = VerifyUtils.isNotEmpty(username);
        boolean hasPhone = VerifyUtils.isNotEmpty(phone);
        boolean hasEmail = VerifyUtils.isNotEmpty(email);

        QueryChain query = this.queryChain().select(USER.USERNAME, USER.PHONE, USER.EMAIL)
            .where(USER.APPLICATION_ID.eq(Context.getApplicationId()));

        if (userId != null) {
            query.and(USER.ID.ne(userId));
        }

        if (hasUsername) {
            query.or(USER.USERNAME.eq(username));
        }
        if (hasPhone) {
            query.or(USER.PHONE.eq(phone));
        }
        if (hasEmail) {
            query.or(USER.EMAIL.eq(email));
        }
        List<UserDomain> users = query.list();

        boolean isUsernameExist = false;
        boolean isPhoneExist = false;
        boolean isEmailExist = false;

        for (UserDomain user : users) {
            if (hasUsername && user.getUsername().equals(username)) {
                isUsernameExist = true;
            } else if (hasPhone && user.getPhone().equals(phone)) {
                isPhoneExist = true;
            } else if (hasEmail && user.getEmail().equals(email)) {
                isEmailExist = true;
            }
        }

        return new CheckUserExist(isUsernameExist, isPhoneExist, isEmailExist);
    }

    /**
     * 检测用户是否存在
     * <p>如果用户名是一个手机号，且填写了手机号，手机号必须和用户名一致</p>
     * <p>如果用户名是一个邮箱，且填写了邮箱，邮箱必须和用户名一致</p>
     * <p>如果用户名是一个手机号，未填写手机号，会将用户名设置为手机号</p>
     * <p>如果用户名是一个邮箱，未填写邮箱，会将用户名设置为邮箱</p>
     *
     * @param userVO
     */
    public void checkUserExist(UserVO userVO) {
        String username = userVO.getUsername();
        String phone = userVO.getPhone();
        String email = userVO.getEmail();

        if (VerifyUtils.isEmpty(username) && VerifyUtils.isEmpty(phone) && VerifyUtils.isEmpty(email)) {
            return;
        }

        if (VerifyUtils.isEmpty(username)) {
            boolean isPhone = isPhone(username);
            boolean isEmail = isEmail(username);

            if (isPhone && VerifyUtils.isNotEmpty(phone) && !phone.equals(username)) {
                throw new RuntimeException("用户名如果是手机号，请和手机号一致");
            } else if (isEmail && VerifyUtils.isNotEmpty(email) && !email.equals(username)) {
                throw new RuntimeException("用户名如果是邮箱，请和邮箱一致");
            }

            userVO.setPhone(isPhone ? username : phone);
            userVO.setEmail(isEmail ? username : email);
        }

        CheckUserExist checkUserExist = this.getExistInfo(username, phone, email, userVO.getId());

        if (checkUserExist.isUserNameExist()) {
            throw new ThrowPrompt("用户名已存在");
        } else if (checkUserExist.isPhoneExist()) {
            throw new ThrowPrompt("手机号已存在");
        } else if (checkUserExist.isEmailExist()) {
            throw new ThrowPrompt("邮箱已存在");
        }
    }

    /**
     * 创建
     *
     * @param userVO
     * @return
     */
    @Override
    public UserVO create(UserVO userVO) {
        this.checkUserExist(userVO);

        if (VerifyUtils.isNotEmpty(userVO.getPassword())) {
            userVO.setPassword(new BCryptPasswordEncoder().encode(userVO.getPassword()));
        }

        userVO.setRoleId(RoleEnum.USER.value);
        userVO.setStatus(UserStatusEnum.NORMAL);
        super.mapper.insert(userVO);

        return userVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public UserVO find(Long id) {
        return this.queryChain()
            .select(USER.ID, USER.ROLE_ID, USER.USERNAME, USER.PHONE, USER.EMAIL, USER.STATUS, USER_EXTENSION.PROFILE)
            .leftJoin(USER_EXTENSION).on(USER_EXTENSION.ID.eq(USER.ID))
            .where(USER.APPLICATION_ID.eq(Context.getApplicationId())).and(USER.ID.eq(id)).oneAs(UserVO.class);
    }

    /**
     * 分页或列表
     *
     * @param userVO
     */
    @Override
    public PageOrList<UserVO> query(UserVO userVO) {
        userVO.setPageType(PageType.PAGE);
        userVO.setJoinTables(USER_EXTENSION.getTableName(), "bind_count");
        userVO.setApplicationId(Context.getApplicationId());

        // like
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.where(USER.APPLICATION_ID.eq(userVO.getApplicationId()));
        if (VerifyUtils.isNotEmpty(userVO.getUsername())) {
            queryWrapper.and(USER.USERNAME.like(userVO.getUsername()));
        }
        if (VerifyUtils.isNotEmpty(userVO.getPhone())) {
            queryWrapper.and(USER.PHONE.like(userVO.getPhone()));
        }
        if (VerifyUtils.isNotEmpty(userVO.getEmail())) {
            queryWrapper.and(USER.EMAIL.like(userVO.getEmail()));
        }

        // equal
        if (VerifyUtils.isNotEmpty(userVO.getStatus())) {
            queryWrapper.and(USER.STATUS.eq(userVO.getStatus()));
        }
        if (VerifyUtils.isNotEmpty(userVO.getRoleId())) {
            queryWrapper.and(USER.ROLE_ID.eq(userVO.getRoleId()));
        }

        return this.mapper.query(userVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param userVO
     */
    @Override
    public void update(UserVO userVO) {
        this.checkUserExist(userVO);

        om.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        om.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        Map map = om.convertValue(userVO, Map.class);
        boolean isUpdateExtension = map.containsKey("extension") && ((Map)map.get("extension")).size() > 0;

        map.remove("extension");
        map.remove("id");
        map.remove("pageType");
        boolean isUpdateUser = map.size() > 0;

        if (isUpdateUser) {
            this.mapper.update(userVO);
        }

        if (isUpdateExtension) {
            userVO.getExtension().setId(userVO.getId());
            userExtensionService.updateById(userVO.getExtension());
        }
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        super.mapper.deleteById(id);
    }

}

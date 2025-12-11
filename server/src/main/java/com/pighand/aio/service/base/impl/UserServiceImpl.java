package com.pighand.aio.service.base.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.common.enums.UserStatusEnum;
import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.domain.base.UserDomain;
import com.pighand.aio.domain.base.UserRoleDomain;
import com.pighand.aio.mapper.base.UserMapper;
import com.pighand.aio.service.base.UserExtensionService;
import com.pighand.aio.service.base.UserRoleService;
import com.pighand.aio.service.base.UserService;
import com.pighand.aio.service.base.UserWechatService;
import com.pighand.aio.service.common.UploadService;
import com.pighand.aio.vo.base.CheckUserExist;
import com.pighand.aio.vo.base.UserRoleVO;
import com.pighand.aio.vo.base.UserVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.exception.ThrowPrompt;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.page.PageType;
import com.pighand.framework.spring.util.VerifyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    private final UserWechatService userWechatService;

    private final UploadService uploadService;

    private final UserRoleService userRoleService;

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
            .where(USER.APPLICATION_ID.eq(Context.applicationId()));

        if (userId != null) {
            query.and(USER.ID.ne(userId));
        }

        // 构建username、phone、email之间的or条件
        if (hasUsername || hasPhone || hasEmail) {
            boolean first = true;

            if (hasUsername) {
                query.and(USER.USERNAME.eq(username));
                first = false;
            }
            if (hasPhone) {
                if (first) {
                    query.and(USER.PHONE.eq(phone));
                } else {
                    query.or(USER.PHONE.eq(phone));
                }
                first = false;
            }
            if (hasEmail) {
                if (first) {
                    query.and(USER.EMAIL.eq(email));
                } else {
                    query.or(USER.EMAIL.eq(email));
                }
            }
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

        if (VerifyUtils.isNotEmpty(username)) {
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

        userVO.setStatus(UserStatusEnum.NORMAL);
        super.mapper.insert(userVO);

        if (userVO.getRoleIds() != null && !userVO.getRoleIds().isEmpty()) {
            List<UserRoleDomain> userRoles = new ArrayList<>(userVO.getRoleIds().size());
            userVO.getRoleIds().forEach(roleId -> {
                UserRoleDomain userRole = new UserRoleDomain();
                userRole.setRoleId(roleId);
                userRole.setUserId(userVO.getId());
                userRoles.add(userRole);
            });
            userRoleService.saveBatch(userRoles);

        }

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
        UserVO user =
            this.queryChain().select(USER.ID, USER.ROLE_ID, USER.USERNAME, USER.PHONE, USER.EMAIL, USER.STATUS)
                .where(USER.APPLICATION_ID.eq(Context.applicationId())).and(USER.ID.eq(id)).oneAs(UserVO.class);

        // 关联角色
        if (user != null && user.getId() != null) {
            List<UserRoleVO> roles = userRoleService.findRolesByUserId(List.of(user.getId()));
            if (VerifyUtils.isNotEmpty(roles)) {
                user.setRoles(roles);
            }
        }

        // 关联扩展
        user.setExtension(userExtensionService.find(user.getId()));

        return user;
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

        QueryWrapper queryWrapper = QueryWrapper.create()
            // like
            .and(USER.ID.like(userVO.getId())).and(USER.ID.like(userVO.getId()))
            .and(USER.USERNAME.like(userVO.getUsername()))
            .and(USER.PHONE.like(userVO.getPhone(), VerifyUtils::isNotEmpty))
            .and(USER.EMAIL.like(userVO.getEmail(), VerifyUtils::isNotEmpty))

            // equal
            .and(USER.APPLICATION_ID.eq(userVO.getApplicationId())).and(USER.STATUS.eq(userVO.getStatus()))
            .and(USER.ROLE_ID.eq(userVO.getRoleId()));

        PageOrList<UserVO> result = this.mapper.query(userVO, queryWrapper);

        // 关联角色
        List<Long> ids = result.getRecords().stream().map(UserVO::getId).collect(Collectors.toList());
        if (VerifyUtils.isNotEmpty(ids)) {
            List<UserRoleVO> roles = userRoleService.findRolesByUserId(ids);

            // 按userId组装
            Map<Long, List<UserRoleVO>> roleMap = roles.stream().collect(Collectors.groupingBy(UserRoleVO::getUserId));

            result.getRecords().forEach(user -> {
                List<UserRoleVO> userRoles = roleMap.get(user.getId());
                if (VerifyUtils.isNotEmpty(userRoles)) {
                    user.setRoles(userRoles);
                }
            });
        }

        return result;
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
        boolean isUpdateRoles = map.containsKey("roleIds");

        map.remove("extension");
        map.remove("id");
        map.remove("pageType");
        map.remove("roleIds");
        boolean isUpdateUser = map.size() > 0;

        if (isUpdateUser) {
            this.mapper.update(userVO);
        }

        if (isUpdateExtension) {
            userVO.getExtension().setId(userVO.getId());
            userExtensionService.updateById(userVO.getExtension());

            // 取消临时头像
            String profile = userVO.getExtension().getProfile();
            if (VerifyUtils.isNotEmpty(profile)) {
                uploadService.updateFileOfficial(profile);
            }
        }

        // 处理角色关联
        if (isUpdateRoles) {
            userRoleService.updateUserRoles(userVO.getId(), userVO.getRoleIds());
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
        userExtensionService.delete(id);
        userWechatService.deleteByUserId(id);
        userRoleService.delUserRoles(id, null);
    }

}

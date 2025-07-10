package com.pighand.aio.service.base.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.base.UserRoleDomain;
import com.pighand.aio.mapper.base.UserRoleMapper;
import com.pighand.aio.service.base.UserRoleService;
import com.pighand.aio.vo.base.UserRoleVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.pighand.aio.domain.base.table.RoleTableDef.ROLE;
import static com.pighand.aio.domain.base.table.UserRoleTableDef.USER_ROLE;

/**
 * 用户角色
 *
 * @author wangshuli
 * @createDate 2025-01-27 10:08:01
 */
@Service
public class UserRoleServiceImpl extends BaseServiceImpl<UserRoleMapper, UserRoleDomain> implements UserRoleService {

    /**
     * 根据用户ID查询关联的角色信息
     *
     * @param userIds 用户ID
     * @return 角色信息列表，包含id和name
     */
    @Override
    public List<UserRoleVO> findRolesByUserId(List<Long> userIds) {
        return super.queryChain().select(USER_ROLE.USER_ID, USER_ROLE.ROLE_ID, ROLE.NAME).from(USER_ROLE)
            .innerJoin(ROLE).on(USER_ROLE.ROLE_ID.eq(ROLE.ID)).where(USER_ROLE.USER_ID.in(userIds))
            .listAs(UserRoleVO.class);
    }

    /**
     * 删除用户角色关联
     *
     * @param userId
     * @param roleIds
     */
    @Override
    public void delUserRoles(Long userId, List<Long> roleIds) {
        QueryWrapper deleteWrapper = new QueryWrapper().where(USER_ROLE.USER_ID.eq(userId));

        if (roleIds != null && roleIds.size() > 0) {
            deleteWrapper.and(USER_ROLE.ROLE_ID.in(roleIds));
        }

        super.mapper.deleteByQuery(deleteWrapper);
    }

    /**
     * 更新用户角色关联
     *
     * @param userId     用户ID
     * @param newRoleIds 前端传入的角色ID列表
     */
    @Override
    public void updateUserRoles(Long userId, List<Long> newRoleIds) {
        // 查询数据库中当前用户的角色
        List<UserRoleVO> currentRoles = this.findRolesByUserId(List.of(userId));
        List<Long> currentRoleIds = currentRoles.stream().map(UserRoleVO::getRoleId).collect(Collectors.toList());

        // 如果前端没传roleIds，设为空列表
        if (newRoleIds == null) {
            newRoleIds = new ArrayList<>();
        }

        // 找出需要删除的角色（数据库有，前端没传的）
        List<Long> roleIdsToDelete = new ArrayList<>();
        for (Long currentRoleId : currentRoleIds) {
            if (!newRoleIds.contains(currentRoleId)) {
                roleIdsToDelete.add(currentRoleId);
            }
        }

        // 找出需要新增的角色（前端传了，数据库没有的）
        List<Long> roleIdsToAdd =
            newRoleIds.stream().filter(roleId -> !currentRoleIds.contains(roleId)).collect(Collectors.toList());

        // 删除不需要的角色关联
        if (!roleIdsToDelete.isEmpty()) {
            this.delUserRoles(userId, roleIdsToDelete);
        }

        // 新增需要的角色关联
        if (!roleIdsToAdd.isEmpty()) {
            List<UserRoleDomain> userRolesToAdd = roleIdsToAdd.stream().map(roleId -> {
                UserRoleDomain userRole = new UserRoleDomain();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                return userRole;
            }).collect(Collectors.toList());
            super.mapper.insertBatch(userRolesToAdd);
        }
    }
}
package com.pighand.aio.service.base;

import com.pighand.aio.domain.base.UserRoleDomain;
import com.pighand.aio.vo.base.UserRoleVO;
import com.pighand.framework.spring.base.BaseService;

import java.util.List;

/**
 * 用户角色关联表
 *
 * @author wangshuli
 * @createDate 2024-01-01 00:00:00
 */
public interface UserRoleService extends BaseService<UserRoleDomain> {
    List<UserRoleVO> findRolesByUserId(List<Long> userIds);

    void delUserRoles(Long userId, List<Long> roleIds);

    void updateUserRoles(Long userId, List<Long> newRoleIds);
}
package com.pighand.aio.vo.base;

import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.base.UserRoleDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户角色关联表
 *
 * @author wangshuli
 * @createDate 2024-01-01 00:00:00
 */
@Data
@TableRef(UserRoleDomain.class)
@EqualsAndHashCode(callSuper = false)
public class UserRoleVO extends UserRoleDomain {

    // relation table: begin
    // relation table: end

    private String name;
}
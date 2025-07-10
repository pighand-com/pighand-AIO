package com.pighand.aio.vo.base;

import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.base.RoleDomain;
import lombok.Data;

/**
 * 角色
 *
 * @author wangshuli
 * @createDate 2025-06-04 10:08:01
 */
@Data
@TableRef(RoleDomain.class)
public class RoleVO extends RoleDomain {

    // relation table: begin
    // relation table: end
}

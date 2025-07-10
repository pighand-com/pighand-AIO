package com.pighand.aio.mapper.base;

import com.pighand.aio.domain.base.UserRoleDomain;
import com.pighand.framework.spring.base.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户角色
 *
 * @author wangshuli
 * @createDate 2025-01-27 10:08:01
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRoleDomain> {

}
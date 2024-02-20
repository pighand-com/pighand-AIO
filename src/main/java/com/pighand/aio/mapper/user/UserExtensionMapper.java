package com.pighand.aio.mapper.user;

import com.pighand.aio.domain.user.UserExtensionDomain;
import com.pighand.aio.vo.user.UserExtensionVO;
import com.pighand.framework.spring.base.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户扩展信息。id与user表相同
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@Mapper
public interface UserExtensionMapper extends BaseMapper<UserExtensionDomain> {

    /**
     * 分页或列表
     *
     * @param userExtensionVO
     * @return
     */

    UserExtensionVO join(@Param("userExtensionVO") UserExtensionVO userExtensionVO);
}

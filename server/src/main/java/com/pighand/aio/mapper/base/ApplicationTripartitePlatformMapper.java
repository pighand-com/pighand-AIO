package com.pighand.aio.mapper.base;

import com.pighand.aio.domain.base.ApplicationTripartitePlatformDomain;
import com.pighand.framework.spring.base.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 项目三方平台配置
 * <p>
 * TODO: 删除，已改别的表
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@Mapper
public interface ApplicationTripartitePlatformMapper extends BaseMapper<ApplicationTripartitePlatformDomain> {

    /**
     * 分页或列表
     *
     * @param pageInfo
     * @param projectTripartitePlatformVO
     * @return
     */
}

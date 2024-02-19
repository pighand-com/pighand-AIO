package com.pighand.aio.mapper.project;

import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.aio.domain.project.ProjectTripartitePlatformDomain;
import org.apache.ibatis.annotations.Mapper;

/**
 * 项目三方平台配置
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@Mapper
public interface ProjectTripartitePlatformMapper extends BaseMapper<ProjectTripartitePlatformDomain> {

    /**
     * 分页或列表
     *
     * @param pageInfo
     * @param projectTripartitePlatformVO
     * @return
     */
}

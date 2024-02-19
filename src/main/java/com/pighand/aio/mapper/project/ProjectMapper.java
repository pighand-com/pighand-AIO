package com.pighand.aio.mapper.project;

import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.aio.domain.project.ProjectDomain;
import org.apache.ibatis.annotations.Mapper;

/**
 * 项目
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@Mapper
public interface ProjectMapper extends BaseMapper<ProjectDomain> {

    /**
     * 分页或列表
     *
     * @param pageInfo
     * @param projectVO
     * @return
     */
}

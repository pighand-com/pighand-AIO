package com.pighand.aio.mapper.project;

import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.aio.domain.project.ProjectDefaultDomain;
import org.apache.ibatis.annotations.Mapper;

/**
 * 项目默认设置
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@Mapper
public interface ProjectDefaultMapper extends BaseMapper<ProjectDefaultDomain> {

    /**
     * 分页或列表
     *
     * @param pageInfo
     * @param projectDefaultVO
     * @return
     */
}

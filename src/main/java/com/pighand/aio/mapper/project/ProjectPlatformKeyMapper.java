package com.pighand.aio.mapper.project;

import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.aio.domain.project.ProjectPlatformKeyDomain;
import org.apache.ibatis.annotations.Mapper;

/**
 * 三方平台key
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@Mapper
public interface ProjectPlatformKeyMapper extends BaseMapper<ProjectPlatformKeyDomain> {

    /**
     * 分页或列表
     *
     * @param pageInfo
     * @param platformKeyVO
     * @return
     */
    //    PageOrList<PlatformKeyVO> query(Page pageInfo, PlatformKeyVO platformKeyVO);
}

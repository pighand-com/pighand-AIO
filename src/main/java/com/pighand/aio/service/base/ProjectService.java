package com.pighand.aio.service.base;

import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.aio.domain.base.ProjectDomain;
import com.pighand.aio.vo.base.ProjectVO;

/**
 * 项目
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
public interface ProjectService extends BaseService<ProjectDomain> {

    /**
     * 创建
     *
     * @param projectVO
     * @return
     */
    ProjectVO create(ProjectVO projectVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    ProjectDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param projectVO
     * @return PageOrList<ProjectVO>
     */
    PageOrList<ProjectVO> query(ProjectVO projectVO);

    /**
     * 修改
     *
     * @param projectVO
     */
    void update(ProjectVO projectVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}

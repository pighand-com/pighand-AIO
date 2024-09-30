package com.pighand.aio.service.base;

import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.aio.domain.base.ProjectTripartitePlatformDomain;
import com.pighand.aio.vo.base.ProjectTripartitePlatformVO;

/**
 * 项目三方平台配置
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
public interface ProjectTripartitePlatformService extends BaseService<ProjectTripartitePlatformDomain> {

    /**
     * 创建
     *
     * @param projectTripartitePlatformVO
     * @return
     */
    ProjectTripartitePlatformVO create(ProjectTripartitePlatformVO projectTripartitePlatformVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    ProjectTripartitePlatformDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param projectTripartitePlatformVO
     * @return PageOrList<ProjectTripartitePlatformVO>
     */
    PageOrList<ProjectTripartitePlatformVO> query(ProjectTripartitePlatformVO projectTripartitePlatformVO);

    /**
     * 修改
     *
     * @param projectTripartitePlatformVO
     */
    void update(ProjectTripartitePlatformVO projectTripartitePlatformVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}

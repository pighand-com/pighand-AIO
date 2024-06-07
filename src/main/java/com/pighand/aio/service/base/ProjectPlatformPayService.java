package com.pighand.aio.service.base;

import com.pighand.aio.domain.base.ProjectPlatformPayDomain;
import com.pighand.aio.vo.base.ProjectPlatformPayVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;

/**
 * 项目 - 支付信息
 *
 * @author wangshuli
 * @createDate 2024-04-22 15:11:06
 */
public interface ProjectPlatformPayService extends BaseService<ProjectPlatformPayDomain> {

    /**
     * 创建
     *
     * @param projectPlatformPayVO
     * @return
     */
    ProjectPlatformPayVO create(ProjectPlatformPayVO projectPlatformPayVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    ProjectPlatformPayDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param projectPlatformPayVO
     * @return PageOrList<ProjectPlatformPayVO>
     */
    PageOrList<ProjectPlatformPayVO> query(ProjectPlatformPayVO projectPlatformPayVO);

    /**
     * 修改
     *
     * @param projectPlatformPayVO
     */
    void update(ProjectPlatformPayVO projectPlatformPayVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}

package com.pighand.aio.service.base;

import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.aio.domain.base.ApplicationDomain;
import com.pighand.aio.vo.base.ApplicationVO;

/**
 * 项目
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
public interface ApplicationService extends BaseService<ApplicationDomain> {

    /**
     * 创建
     *
     * @param projectVO
     * @return
     */
    ApplicationVO create(ApplicationVO projectVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    ApplicationDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param projectVO
     * @return PageOrList<ProjectVO>
     */
    PageOrList<ApplicationVO> query(ApplicationVO projectVO);

    /**
     * 修改
     *
     * @param projectVO
     */
    void update(ApplicationVO projectVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}

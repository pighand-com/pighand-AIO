package com.pighand.aio.service.base;

import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.aio.domain.base.ApplicationAuthorizationDomain;
import com.pighand.aio.vo.base.ApplicationAuthorizationVO;

/**
 * Authorization配置
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
public interface ApplicationAuthorizationService extends BaseService<ApplicationAuthorizationDomain> {

    /**
     * 创建
     *
     * @param projectAuthorizationVO
     * @return
     */
    ApplicationAuthorizationVO create(ApplicationAuthorizationVO projectAuthorizationVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    ApplicationAuthorizationDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param projectAuthorizationVO
     * @return PageOrList<ProjectAuthorizationVO>
     */
    PageOrList<ApplicationAuthorizationVO> query(ApplicationAuthorizationVO projectAuthorizationVO);

    /**
     * 修改
     *
     * @param projectAuthorizationVO
     */
    void update(ApplicationAuthorizationVO projectAuthorizationVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);

}

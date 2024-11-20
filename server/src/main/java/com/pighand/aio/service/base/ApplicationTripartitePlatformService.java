package com.pighand.aio.service.base;

import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.aio.domain.base.ApplicationTripartitePlatformDomain;
import com.pighand.aio.vo.base.ApplicationTripartitePlatformVO;

/**
 * 项目三方平台配置
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
public interface ApplicationTripartitePlatformService extends BaseService<ApplicationTripartitePlatformDomain> {

    /**
     * 创建
     *
     * @param projectTripartitePlatformVO
     * @return
     */
    ApplicationTripartitePlatformVO create(ApplicationTripartitePlatformVO projectTripartitePlatformVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    ApplicationTripartitePlatformDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param projectTripartitePlatformVO
     * @return PageOrList<ProjectTripartitePlatformVO>
     */
    PageOrList<ApplicationTripartitePlatformVO> query(ApplicationTripartitePlatformVO projectTripartitePlatformVO);

    /**
     * 修改
     *
     * @param projectTripartitePlatformVO
     */
    void update(ApplicationTripartitePlatformVO projectTripartitePlatformVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}

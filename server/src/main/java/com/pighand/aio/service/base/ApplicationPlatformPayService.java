package com.pighand.aio.service.base;

import com.pighand.aio.domain.base.ApplicationPlatformPayDomain;
import com.pighand.aio.vo.base.ApplicationPlatformPayVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;

/**
 * 项目 - 支付信息
 *
 * @author wangshuli
 * @createDate 2024-04-22 15:11:06
 */
public interface ApplicationPlatformPayService extends BaseService<ApplicationPlatformPayDomain> {

    /**
     * 创建
     *
     * @param projectPlatformPayVO
     * @return
     */
    ApplicationPlatformPayVO create(ApplicationPlatformPayVO projectPlatformPayVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    ApplicationPlatformPayDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param projectPlatformPayVO
     * @return PageOrList<ProjectPlatformPayVO>
     */
    PageOrList<ApplicationPlatformPayVO> query(ApplicationPlatformPayVO projectPlatformPayVO);

    /**
     * 修改
     *
     * @param projectPlatformPayVO
     */
    void update(ApplicationPlatformPayVO projectPlatformPayVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}

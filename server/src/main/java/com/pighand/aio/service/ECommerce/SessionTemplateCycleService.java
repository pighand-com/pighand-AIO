package com.pighand.aio.service.ECommerce;

import com.pighand.aio.domain.ECommerce.SessionTemplateCycleDomain;
import com.pighand.aio.vo.ECommerce.SessionTemplateCycleVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;

/**
 * 电商 - 场次模板 - 按周期
 *
 * @author wangshuli
 * @createDate 2024-05-23 15:01:58
 */
public interface SessionTemplateCycleService extends BaseService<SessionTemplateCycleDomain> {

    /**
     * 创建
     *
     * @param sessionTemplateCycleVO
     * @return
     */
    SessionTemplateCycleVO create(SessionTemplateCycleVO sessionTemplateCycleVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    SessionTemplateCycleDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param sessionTemplateCycleVO
     * @return PageOrList<SessionTemplateCycleVO>
     */
    PageOrList<SessionTemplateCycleVO> query(SessionTemplateCycleVO sessionTemplateCycleVO);

    /**
     * 修改
     *
     * @param sessionTemplateCycleVO
     */
    void update(SessionTemplateCycleVO sessionTemplateCycleVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}

package com.pighand.aio.service.ECommerce;

import com.pighand.aio.domain.ECommerce.SessionUserCycleDomain;
import com.pighand.aio.vo.ECommerce.SessionUserCycleVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;

/**
 * 电商 - 场次 - 用户周期
 *
 * @author wangshuli
 * @createDate 2024-05-23 15:01:58
 */
public interface SessionUserCycleService extends BaseService<SessionUserCycleDomain> {

    /**
     * 创建
     *
     * @param sessionUserCycleVO
     * @return
     */
    SessionUserCycleVO create(SessionUserCycleVO sessionUserCycleVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    SessionUserCycleDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param sessionUserCycleVO
     * @return PageOrList<SessionUserCycleVO>
     */
    PageOrList<SessionUserCycleVO> query(SessionUserCycleVO sessionUserCycleVO);

    /**
     * 修改
     *
     * @param sessionUserCycleVO
     */
    void update(SessionUserCycleVO sessionUserCycleVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}

package com.pighand.aio.service.ECommerce;

import com.pighand.aio.domain.ECommerce.TicketValidityDomain;
import com.pighand.aio.vo.ECommerce.TicketValidityVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;

/**
 * 电商 - 票务 - 使用范围
 *
 * @author wangshuli
 * @createDate 2024-04-28 11:36:03
 */
public interface TicketValidityService extends BaseService<TicketValidityDomain> {

    /**
     * 创建
     *
     * @param ticketValidityVO
     * @return
     */
    TicketValidityVO create(TicketValidityVO ticketValidityVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    TicketValidityDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param ticketValidityVO
     * @return PageOrList<TicketValidityVO>
     */
    PageOrList<TicketValidityVO> query(TicketValidityVO ticketValidityVO);

    /**
     * 修改
     *
     * @param ticketValidityVO
     */
    void update(TicketValidityVO ticketValidityVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}

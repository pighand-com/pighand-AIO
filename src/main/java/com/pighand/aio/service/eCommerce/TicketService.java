package com.pighand.aio.service.ECommerce;

import com.pighand.aio.domain.ECommerce.TicketDomain;
import com.pighand.aio.vo.ECommerce.TicketVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;

/**
 * 电商 - 票务
 *
 * @author wangshuli
 * @createDate 2024-04-26 14:52:18
 */
public interface TicketService extends BaseService<TicketDomain> {

    /**
     * 创建
     *
     * @param ticketVO
     * @return
     */
    TicketVO create(TicketVO ticketVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    TicketDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param ticketVO
     * @return PageOrList<TicketVO>
     */
    PageOrList<TicketVO> query(TicketVO ticketVO);

    /**
     * 修改
     *
     * @param ticketVO
     */
    void update(TicketVO ticketVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}

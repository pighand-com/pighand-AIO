package com.pighand.aio.service.ECommerce;

import com.pighand.aio.domain.ECommerce.TicketUserValidityDomain;
import com.pighand.aio.vo.ECommerce.TicketUserValidityVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;

/**
 * 电商 - 票务 - 已购票使用范围
 *
 * @author wangshuli
 * @createDate 2024-04-28 11:36:03
 */
public interface TicketUserValidityService extends BaseService<TicketUserValidityDomain> {

    /**
     * 创建
     *
     * @param ticketUserValidityVO
     * @return
     */
    TicketUserValidityVO create(TicketUserValidityVO ticketUserValidityVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    TicketUserValidityDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param ticketUserValidityVO
     * @return PageOrList<TicketUserValidityVO>
     */
    PageOrList<TicketUserValidityVO> query(TicketUserValidityVO ticketUserValidityVO);

    /**
     * 修改
     *
     * @param ticketUserValidityVO
     */
    void update(TicketUserValidityVO ticketUserValidityVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}

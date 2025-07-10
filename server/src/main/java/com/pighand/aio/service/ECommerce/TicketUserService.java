package com.pighand.aio.service.ECommerce;

import com.pighand.aio.domain.ECommerce.TicketUserDomain;
import com.pighand.aio.vo.ECommerce.TicketUserVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;
import org.springframework.transaction.annotation.Transactional;

/**
 * 电商 - 已购票务
 *
 * @author wangshuli
 * @createDate 2024-04-26 14:52:18
 */
public interface TicketUserService extends BaseService<TicketUserDomain> {

    /**
     * 创建
     *
     * @param ticketUserVO
     * @return
     */
    TicketUserVO create(TicketUserVO ticketUserVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    TicketUserDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param ticketUserVO
     * @return PageOrList<TicketUserVO>
     */
    PageOrList<TicketUserVO> query(TicketUserVO ticketUserVO);

    /**
     * 修改
     *
     * @param ticketUserVO
     */
    void update(TicketUserVO ticketUserVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);

    void validation(TicketUserVO ticketUserVO);

    @Transactional(rollbackFor = Exception.class)
    void cancelValidation(TicketUserVO ticketUserVO);
}

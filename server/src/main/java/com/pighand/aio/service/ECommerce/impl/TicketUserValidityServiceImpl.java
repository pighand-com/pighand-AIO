package com.pighand.aio.service.ECommerce.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.TicketUserValidityDomain;
import com.pighand.aio.mapper.ECommerce.TicketUserValidityMapper;
import com.pighand.aio.service.ECommerce.TicketUserValidityService;
import com.pighand.aio.vo.ECommerce.TicketUserValidityVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import org.springframework.stereotype.Service;

import static com.pighand.aio.domain.ECommerce.table.TicketUserValidityTableDef.TICKET_USER_VALIDITY;

/**
 * 电商 - 票务 - 已购票使用范围
 *
 * @author wangshuli
 * @createDate 2024-04-28 11:36:03
 */
@Service
public class TicketUserValidityServiceImpl extends BaseServiceImpl<TicketUserValidityMapper, TicketUserValidityDomain>
    implements TicketUserValidityService {

    /**
     * 创建
     *
     * @param ticketUserValidityVO
     * @return
     */
    @Override
    public TicketUserValidityVO create(TicketUserValidityVO ticketUserValidityVO) {
        super.mapper.insert(ticketUserValidityVO);

        return ticketUserValidityVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public TicketUserValidityDomain find(Long id) {
        return super.mapper.find(id, null);
    }

    /**
     * 分页或列表
     *
     * @param ticketUserValidityVO
     * @return PageOrList<TicketUserValidityVO>
     */
    @Override
    public PageOrList<TicketUserValidityVO> query(TicketUserValidityVO ticketUserValidityVO) {

        QueryWrapper queryWrapper = QueryWrapper.create()
            // equal
            .and(TICKET_USER_VALIDITY.TICKET_ID.eq(ticketUserValidityVO.getTicketId()))
            .and(TICKET_USER_VALIDITY.TICKET_USER_ID.eq(ticketUserValidityVO.getTicketUserId()))
            .and(TICKET_USER_VALIDITY.TICKET_VALIDITY_ID.eq(ticketUserValidityVO.getTicketValidityId()))
            .and(TICKET_USER_VALIDITY.VALIDATION_COUNT.eq(ticketUserValidityVO.getValidationCount()));

        return super.mapper.query(ticketUserValidityVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param ticketUserValidityVO
     */
    @Override
    public void update(TicketUserValidityVO ticketUserValidityVO) {
        super.mapper.update(ticketUserValidityVO);
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        super.mapper.deleteById(id);
    }
}

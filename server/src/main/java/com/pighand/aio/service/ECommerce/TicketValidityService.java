package com.pighand.aio.service.ECommerce;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.TicketValidityDomain;
import com.pighand.aio.mapper.ECommerce.TicketValidityMapper;
import com.pighand.aio.vo.ECommerce.TicketValidityVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import org.springframework.stereotype.Service;

import static com.pighand.aio.domain.ECommerce.table.TicketValidityTableDef.TICKET_VALIDITY;

/**
 * 电商 - 票务 - 使用范围
 *
 * @author wangshuli
 * @createDate 2024-04-28 11:36:03
 */
@Service
public class TicketValidityService extends BaseServiceImpl<TicketValidityMapper, TicketValidityDomain>
     {

    /**
     * 创建
     *
     * @param ticketValidityVO
     * @return
     */
    public TicketValidityVO create(TicketValidityVO ticketValidityVO) {
        super.mapper.insert(ticketValidityVO);

        return ticketValidityVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public TicketValidityDomain find(Long id) {
        return super.mapper.find(id);
    }

    /**
     * 分页或列表
     *
     * @param ticketValidityVO
     * @return PageOrList<TicketValidityVO>
     */
    public PageOrList<TicketValidityVO> query(TicketValidityVO ticketValidityVO) {

        QueryWrapper queryWrapper = QueryWrapper.create()
            // equal
            .and(TICKET_VALIDITY.TICKET_ID.eq(ticketValidityVO.getTicketId()))
            .and(TICKET_VALIDITY.VALIDATION_COUNT.eq(ticketValidityVO.getValidationCount()));

        return super.mapper.query(ticketValidityVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param ticketValidityVO
     */
    public void update(TicketValidityVO ticketValidityVO) {
        super.mapper.update(ticketValidityVO);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(Long id) {
        super.mapper.deleteById(id);
    }
}

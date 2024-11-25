package com.pighand.aio.service.ECommerce.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.OrderTradeDomain;
import com.pighand.aio.mapper.ECommerce.OrderTradeMapper;
import com.pighand.aio.service.ECommerce.OrderTradeService;
import com.pighand.aio.vo.ECommerce.OrderTradeVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.pighand.aio.domain.ECommerce.table.BillTableDef.BILL;
import static com.pighand.aio.domain.ECommerce.table.OrderSkuTableDef.ORDER_SKU;
import static com.pighand.aio.domain.ECommerce.table.OrderTableDef.ORDER;
import static com.pighand.aio.domain.ECommerce.table.OrderTradeTableDef.ORDER_TRADE;

/**
 * 电商 - 订单 - 交易单
 *
 * @author wangshuli
 * @createDate 2024-04-18 14:35:34
 */
@Service
public class OrderTradeServiceImpl extends BaseServiceImpl<OrderTradeMapper, OrderTradeDomain>
    implements OrderTradeService {

    /**
     * 创建
     *
     * @param orderTradeVO
     * @return
     */
    @Override
    public OrderTradeVO create(OrderTradeVO orderTradeVO) {
        super.mapper.insert(orderTradeVO);

        return orderTradeVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public OrderTradeDomain find(Long id) {
        Set<String> joinTables = Set.of();

        return super.mapper.find(id, ORDER.getTableName(), ORDER_SKU.getTableName(), BILL.getTableName());
    }

    /**
     * 分页或列表
     *
     * @param orderTradeVO
     * @return PageOrList<OrderTradeVO>
     */
    @Override
    public PageOrList<OrderTradeVO> query(OrderTradeVO orderTradeVO) {
        orderTradeVO.setJoinTables(ORDER.getTableName(), ORDER_SKU.getTableName(), BILL.getTableName());

        QueryWrapper queryWrapper = QueryWrapper.create()

            // like
            .and(ORDER_TRADE.SN.like(orderTradeVO.getSn()))
            .and(ORDER_TRADE.CREATOR_ID.like(orderTradeVO.getCreatorId()))

            // equal
            .and(ORDER_TRADE.AMOUNT_PAYABLE.eq(orderTradeVO.getAmountPayable()))
            .and(ORDER_TRADE.AMOUNT_PAID.eq(orderTradeVO.getAmountPaid()))

            .orderBy(ORDER_TRADE.ID.desc());

        return super.mapper.query(orderTradeVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param orderTradeVO
     */
    @Override
    public void update(OrderTradeVO orderTradeVO) {
        super.mapper.update(orderTradeVO);
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

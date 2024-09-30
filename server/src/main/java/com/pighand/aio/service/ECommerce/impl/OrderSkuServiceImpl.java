package com.pighand.aio.service.ECommerce.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.OrderSkuDomain;
import com.pighand.aio.mapper.ECommerce.OrderSkuMapper;
import com.pighand.aio.service.ECommerce.OrderSkuService;
import com.pighand.aio.vo.ECommerce.OrderSkuVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import org.springframework.stereotype.Service;

import static com.pighand.aio.domain.ECommerce.table.BillTableDef.BILL;
import static com.pighand.aio.domain.ECommerce.table.OrderSkuTableDef.ORDER_SKU;
import static com.pighand.aio.domain.ECommerce.table.OrderTableDef.ORDER;
import static com.pighand.aio.domain.ECommerce.table.OrderTradeTableDef.ORDER_TRADE;

/**
 * 电商 - 订单 - SKU
 *
 * @author wangshuli
 * @createDate 2024-04-18 14:35:34
 */
@Service
public class OrderSkuServiceImpl extends BaseServiceImpl<OrderSkuMapper, OrderSkuDomain> implements OrderSkuService {

    /**
     * 创建
     *
     * @param orderSkuVO
     * @return
     */
    @Override
    public OrderSkuVO create(OrderSkuVO orderSkuVO) {
        super.mapper.insert(orderSkuVO);

        return orderSkuVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public OrderSkuDomain find(Long id) {
        return super.mapper.find(id, ORDER.getTableName(), ORDER_TRADE.getTableName(), BILL.getTableName());
    }

    /**
     * 分页或列表
     *
     * @param orderSkuVO
     * @return PageOrList<OrderSkuVO>
     */
    @Override
    public PageOrList<OrderSkuVO> query(OrderSkuVO orderSkuVO) {
        orderSkuVO.setJoinTables(ORDER.getTableName(), ORDER_TRADE.getTableName(), BILL.getTableName());

        QueryWrapper queryWrapper = new QueryWrapper();

        // like
        queryWrapper.and(ORDER_SKU.ORDER_TRADE_ID.like(orderSkuVO.getOrderTradeId(), VerifyUtils::isNotEmpty));

        // equal
        queryWrapper.and(ORDER_SKU.ORDER_ID.eq(orderSkuVO.getOrderId(), VerifyUtils::isNotEmpty));
        queryWrapper.and(ORDER_SKU.SPU_ID.eq(orderSkuVO.getSpuId(), VerifyUtils::isNotEmpty));
        queryWrapper.and(ORDER_SKU.SKU_ID.eq(orderSkuVO.getSkuId(), VerifyUtils::isNotEmpty));
        queryWrapper.and(ORDER_SKU.TICKET_ID.eq(orderSkuVO.getTicketId(), VerifyUtils::isNotEmpty));
        queryWrapper.and(ORDER_SKU.SESSION_ID.eq(orderSkuVO.getSessionId(), VerifyUtils::isNotEmpty));
        queryWrapper.and(ORDER_SKU.TYPE.eq(orderSkuVO.getType(), VerifyUtils::isNotEmpty));
        queryWrapper.and(ORDER_SKU.AMOUNT_PAYABLE.eq(orderSkuVO.getAmountPayable(), VerifyUtils::isNotEmpty));
        queryWrapper.and(ORDER_SKU.AMOUNT_PAID.eq(orderSkuVO.getAmountPaid(), VerifyUtils::isNotEmpty));

        return super.mapper.query(orderSkuVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param orderSkuVO
     */
    @Override
    public void update(OrderSkuVO orderSkuVO) {
        super.mapper.update(orderSkuVO);
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

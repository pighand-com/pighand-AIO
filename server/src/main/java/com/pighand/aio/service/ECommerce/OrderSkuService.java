package com.pighand.aio.service.ECommerce;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.OrderSkuDomain;
import com.pighand.aio.mapper.ECommerce.OrderSkuMapper;
import com.pighand.aio.vo.ECommerce.OrderSkuVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
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
public class OrderSkuService extends BaseServiceImpl<OrderSkuMapper, OrderSkuDomain>  {

    /**
     * 创建
     *
     * @param orderSkuVO
     * @return
     */
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
    public OrderSkuDomain find(Long id) {
        return super.mapper.find(id, ORDER.getTableName(), ORDER_TRADE.getTableName(), BILL.getTableName());
    }

    /**
     * 分页或列表
     *
     * @param orderSkuVO
     * @return PageOrList<OrderSkuVO>
     */
    public PageOrList<OrderSkuVO> query(OrderSkuVO orderSkuVO) {
        orderSkuVO.setJoinTables(ORDER.getTableName(), ORDER_TRADE.getTableName(), BILL.getTableName());

        QueryWrapper queryWrapper = QueryWrapper.create()
            // like
            .and(ORDER_SKU.ORDER_TRADE_ID.like(orderSkuVO.getOrderTradeId()))

            // equal
            .and(ORDER_SKU.ORDER_ID.eq(orderSkuVO.getOrderId())).and(ORDER_SKU.SPU_ID.eq(orderSkuVO.getSpuId()))
            .and(ORDER_SKU.SKU_ID.eq(orderSkuVO.getSkuId())).and(ORDER_SKU.TICKET_ID.eq(orderSkuVO.getTicketId()))
            .and(ORDER_SKU.SESSION_ID.eq(orderSkuVO.getSessionId())).and(ORDER_SKU.TYPE.eq(orderSkuVO.getType()))
            .and(ORDER_SKU.AMOUNT_PAYABLE.eq(orderSkuVO.getAmountPayable()))
            .and(ORDER_SKU.AMOUNT_PAID.eq(orderSkuVO.getAmountPaid()));

        return super.mapper.query(orderSkuVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param orderSkuVO
     */
    public void update(OrderSkuVO orderSkuVO) {
        super.mapper.update(orderSkuVO);
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

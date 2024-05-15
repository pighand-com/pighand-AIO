package com.pighand.aio.mapper.ECommerce;

import com.mybatisflex.core.field.FieldQueryBuilder;
import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.OrderTradeDomain;
import com.pighand.aio.vo.ECommerce.OrderTradeVO;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.framework.spring.page.PageOrList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.function.Consumer;

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
@Mapper
public interface OrderTradeMapper extends BaseMapper<OrderTradeDomain> {

    /**
     * 基础查询条件
     *
     * @return
     */
    default QueryWrapper relationOne(List<String> joinTables, QueryWrapper queryWrapper) {
        if (queryWrapper == null) {
            queryWrapper = QueryWrapper.create();
        }

        if (joinTables == null) {
            return queryWrapper;
        }

        return queryWrapper;
    }

    /**
     * 一对多关联查询结果
     *
     * @return
     */
    default Consumer<FieldQueryBuilder<OrderTradeVO>>[] relationMany(List<String> joinTables) {
        if (joinTables == null) {
            return null;
        }

        boolean hasOrder = joinTables.contains(ORDER.getTableName());
        boolean hasOrderSku = joinTables.contains(ORDER_SKU.getTableName());
        boolean hasBill = joinTables.contains(BILL.getTableName());

        int length = 0;

        if (hasOrder) {
            length++;
        }
        if (hasOrderSku) {
            length++;
        }
        if (hasBill) {
            length++;
        }

        Consumer<FieldQueryBuilder<OrderTradeVO>>[] fieldQueryBuilders = new Consumer[length];

        int nowIndex = 0;
        if (hasOrder) {
            Consumer<FieldQueryBuilder<OrderTradeVO>> consumer = (builder) -> {
                builder.field(OrderTradeVO::getOrder).queryWrapper(
                    orderTrade -> QueryWrapper.create().from(ORDER).where(ORDER.ORDER_TRADE_ID.eq(orderTrade.getId())));
            };
            fieldQueryBuilders[nowIndex] = consumer;
            nowIndex++;
        }
        if (hasOrderSku) {
            Consumer<FieldQueryBuilder<OrderTradeVO>> consumer = (builder) -> {
                builder.field(OrderTradeVO::getOrderSku).queryWrapper(
                    orderTrade -> QueryWrapper.create().from(ORDER_SKU)
                        .where(ORDER_SKU.ORDER_TRADE_ID.eq(orderTrade.getId())));
            };
            fieldQueryBuilders[nowIndex] = consumer;
            nowIndex++;
        }
        if (hasBill) {
            Consumer<FieldQueryBuilder<OrderTradeVO>> consumer = (builder) -> {
                builder.field(OrderTradeVO::getBill).queryWrapper(
                    orderTrade -> QueryWrapper.create().from(BILL).where(BILL.ORDER_TRADE_ID.eq(orderTrade.getId())));
            };
            fieldQueryBuilders[nowIndex] = consumer;
            nowIndex++;
        }

        return fieldQueryBuilders;
    }

    /**
     * 查询详情
     *
     * @param id         主键
     * @param joinTables 关联表
     * @return
     */
    default OrderTradeVO find(Long id, List<String> joinTables) {
        QueryWrapper queryWrapper = this.relationOne(joinTables, null).where(ORDER_TRADE.ID.eq(id));
        Consumer<FieldQueryBuilder<OrderTradeVO>>[] relationManyBuilders = this.relationMany(joinTables);

        return this.selectOneByQueryAs(queryWrapper, OrderTradeVO.class, relationManyBuilders);
    }

    /**
     * 查询详情
     *
     * @param queryWrapper
     * @param joinTables   关联表
     * @return
     */
    default OrderTradeVO find(QueryWrapper queryWrapper, List<String> joinTables) {
        QueryWrapper finalQueryWrapper = this.relationOne(joinTables, queryWrapper);
        Consumer<FieldQueryBuilder<OrderTradeVO>>[] relationManyBuilders = this.relationMany(joinTables);

        return this.selectOneByQueryAs(finalQueryWrapper, OrderTradeVO.class, relationManyBuilders);
    }

    /**
     * 分页或列表
     *
     * @param orderTradeDomain
     * @return
     */
    default PageOrList<OrderTradeVO> query(OrderTradeDomain orderTradeDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(orderTradeDomain.getJoinTables(), queryWrapper);
        Consumer<FieldQueryBuilder<OrderTradeVO>>[] relationManyBuilders =
            this.relationMany(orderTradeDomain.getJoinTables());

        return this.page(orderTradeDomain, finalQueryWrapper, OrderTradeVO.class, relationManyBuilders);
    }
}

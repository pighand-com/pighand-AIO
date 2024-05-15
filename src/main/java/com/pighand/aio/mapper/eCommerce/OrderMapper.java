package com.pighand.aio.mapper.ECommerce;

import com.mybatisflex.core.field.FieldQueryBuilder;
import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.OrderDomain;
import com.pighand.aio.vo.ECommerce.OrderVO;
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
 * 电商 - 订单
 *
 * @author wangshuli
 * @createDate 2024-04-18 14:35:34
 */
@Mapper
public interface OrderMapper extends BaseMapper<OrderDomain> {

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

        if (joinTables.contains(ORDER_TRADE.getTableName())) {
            queryWrapper.leftJoin(ORDER_TRADE).on(ORDER_TRADE.ID.eq(ORDER.ORDER_TRADE_ID));
        }

        return queryWrapper;
    }

    /**
     * 一对多关联查询结果
     *
     * @return
     */
    default Consumer<FieldQueryBuilder<OrderVO>>[] relationMany(List<String> joinTables) {
        if (joinTables == null) {
            return null;
        }

        boolean hasOrderSku = joinTables.contains(ORDER_SKU.getTableName());
        boolean hasBill = joinTables.contains(BILL.getTableName());
        //        boolean hasGoodsSpu = joinTables.contains(GOODS_SPU.getTableName());
        //        boolean hasTicket = joinTables.contains(TICKET.getTableName());

        int length = 0;

        if (hasOrderSku) {
            length++;
        }
        if (hasBill) {
            length++;
        }
        //        if (hasGoodsSpu) {
        //            length++;
        //        }
        //        if (hasTicket) {
        //            length++;
        //        }

        Consumer<FieldQueryBuilder<OrderVO>>[] fieldQueryBuilders = new Consumer[length];

        int nowIndex = 0;
        if (hasOrderSku) {
            Consumer<FieldQueryBuilder<OrderVO>> consumer = (builder) -> {
                builder.field(OrderVO::getOrderSku).queryWrapper(
                    order -> QueryWrapper.create().from(ORDER_SKU).where(ORDER_SKU.ORDER_ID.eq(order.getId())));
            };
            fieldQueryBuilders[nowIndex] = consumer;
            nowIndex++;
        }
        if (hasBill) {
            Consumer<FieldQueryBuilder<OrderVO>> consumer = (builder) -> {
                builder.field(OrderVO::getBill)
                    .queryWrapper(order -> QueryWrapper.create().from(BILL).where(BILL.ORDER_ID.eq(order.getId())));
            };
            fieldQueryBuilders[nowIndex] = consumer;
            nowIndex++;
        }
        //        if (hasGoodsSpu) {
        //            Consumer<FieldQueryBuilder<OrderVO>> consumer = (builder) -> {
        //                builder.field(OrderVO::getGoodsSpu).queryWrapper(order -> {
        //                    if (order.getOrderSku() == null || order.getOrderSku().size() == 0) {
        //                        return null;
        //                    }
        //
        //                    List<Long> spuIds =
        //                        order.getOrderSku().stream().map(OrderSkuVO::getSpuId).filter(item -> item != null).toList();
        //
        //                    if (spuIds.size() == 0) {
        //                        return null;
        //                    }
        //
        //                    return QueryWrapper.create().from(GOODS_SPU).where(GOODS_SPU.ID.in(spuIds));
        //                });
        //
        //                builder.field(OrderVO::getGoodsSpu).queryWrapper(order -> {
        //                    if (order.getOrderSku() == null || order.getOrderSku().size() == 0) {
        //                        return null;
        //                    }
        //
        //                    List<Long> spuIds =
        //                        order.getOrderSku().stream().map(OrderSkuVO::getSpuId).filter(item -> item != null).toList();
        //
        //                    if (spuIds.size() == 0) {
        //                        return null;
        //                    }
        //
        //                    return QueryWrapper.create().from(GOODS_SPU).where(GOODS_SPU.ID.in(spuIds));
        //                });
        //            };
        //            fieldQueryBuilders[nowIndex] = consumer;
        //            nowIndex++;
        //        }
        //        if (hasTicket) {
        //            Consumer<FieldQueryBuilder<OrderVO>> consumer = (builder) -> {
        //                builder.field(OrderVO::getTicket).queryWrapper(order -> {
        //                    if (order.getOrderSku() == null || order.getOrderSku().size() == 0) {
        //                        return null;
        //                    }
        //
        //                    List<Long> ticketIds =
        //                        order.getOrderSku().stream().map(OrderSkuVO::getTicketId).filter(item -> item != null).toList();
        //
        //                    if (ticketIds.size() == 0) {
        //                        return null;
        //                    }
        //
        //                    return QueryWrapper.create().from(TICKET).where(TICKET.ID.in(ticketIds));
        //                });
        //            };
        //            fieldQueryBuilders[nowIndex] = consumer;
        //            nowIndex++;
        //        }

        return fieldQueryBuilders;
    }

    /**
     * 查询详情
     *
     * @param id         主键
     * @param joinTables 关联表
     * @return
     */
    default OrderVO find(Long id, List<String> joinTables) {
        QueryWrapper queryWrapper = this.relationOne(joinTables, null).where(ORDER.ID.eq(id));
        Consumer<FieldQueryBuilder<OrderVO>>[] relationManyBuilders = this.relationMany(joinTables);

        return this.selectOneByQueryAs(queryWrapper, OrderVO.class, relationManyBuilders);
    }

    /**
     * 查询详情
     *
     * @param queryWrapper
     * @param joinTables   关联表
     * @return
     */
    default OrderVO find(QueryWrapper queryWrapper, List<String> joinTables) {
        QueryWrapper finalQueryWrapper = this.relationOne(joinTables, queryWrapper);
        Consumer<FieldQueryBuilder<OrderVO>>[] relationManyBuilders = this.relationMany(joinTables);

        return this.selectOneByQueryAs(finalQueryWrapper, OrderVO.class, relationManyBuilders);
    }

    /**
     * 分页或列表
     *
     * @param orderDomain
     * @return
     */
    // TODO: 第一个参数改为Page对象。this.page的第三个参数，替换泛型
    default PageOrList<OrderVO> query(OrderDomain orderDomain, QueryWrapper queryWrapper) {
        // 冒泡排序
        
        QueryWrapper finalQueryWrapper = this.relationOne(orderDomain.getJoinTables(), queryWrapper);
        Consumer<FieldQueryBuilder<OrderVO>>[] relationManyBuilders = this.relationMany(orderDomain.getJoinTables());

        return this.page(orderDomain, finalQueryWrapper, OrderVO.class, relationManyBuilders);
    }
}

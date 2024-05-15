package com.pighand.aio.mapper.ECommerce;

import com.mybatisflex.core.field.FieldQueryBuilder;
import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.OrderSkuDomain;
import com.pighand.aio.vo.ECommerce.OrderSkuVO;
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
 * 电商 - 订单 - SKU
 *
 * @author wangshuli
 * @createDate 2024-04-18 14:35:34
 */
@Mapper
public interface OrderSkuMapper extends BaseMapper<OrderSkuDomain> {

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

        if (joinTables.contains(ORDER.getTableName())) {
            queryWrapper.leftJoin(ORDER).on(ORDER.ID.eq(ORDER_SKU.ORDER_ID));
        }

        if (joinTables.contains(ORDER_TRADE.getTableName())) {
            queryWrapper.leftJoin(ORDER_TRADE).on(ORDER_TRADE.ID.eq(ORDER_SKU.ORDER_TRADE_ID));
        }

        return queryWrapper;
    }

    /**
     * 一对多关联查询结果
     *
     * @return
     */
    default Consumer<FieldQueryBuilder<OrderSkuVO>>[] relationMany(List<String> joinTables) {
        if (joinTables == null) {
            return null;
        }

        boolean hasBill = joinTables.contains(BILL.getTableName());

        int length = 0;

        if (hasBill) {
            length++;
        }

        Consumer<FieldQueryBuilder<OrderSkuVO>>[] fieldQueryBuilders = new Consumer[length];

        int nowIndex = 0;
        if (hasBill) {
            Consumer<FieldQueryBuilder<OrderSkuVO>> consumer = (builder) -> {
                builder.field(OrderSkuVO::getBill).queryWrapper(
                    orderSku -> QueryWrapper.create().from(BILL).where(BILL.ORDER_SKU_ID.eq(orderSku.getId())));
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
    default OrderSkuVO find(Long id, List<String> joinTables) {
        QueryWrapper queryWrapper = this.relationOne(joinTables, null).where(ORDER_SKU.ID.eq(id));
        Consumer<FieldQueryBuilder<OrderSkuVO>>[] relationManyBuilders = this.relationMany(joinTables);

        return this.selectOneByQueryAs(queryWrapper, OrderSkuVO.class, relationManyBuilders);
    }

    /**
     * 查询详情
     *
     * @param queryWrapper
     * @param joinTables   关联表
     * @return
     */
    default OrderSkuVO find(QueryWrapper queryWrapper, List<String> joinTables) {
        QueryWrapper finalQueryWrapper = this.relationOne(joinTables, queryWrapper);
        Consumer<FieldQueryBuilder<OrderSkuVO>>[] relationManyBuilders = this.relationMany(joinTables);

        return this.selectOneByQueryAs(finalQueryWrapper, OrderSkuVO.class, relationManyBuilders);
    }

    /**
     * 分页或列表
     *
     * @param orderSkuDomain
     * @return
     */
    default PageOrList<OrderSkuVO> query(OrderSkuDomain orderSkuDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(orderSkuDomain.getJoinTables(), queryWrapper);
        Consumer<FieldQueryBuilder<OrderSkuVO>>[] relationManyBuilders =
            this.relationMany(orderSkuDomain.getJoinTables());

        return this.page(orderSkuDomain, finalQueryWrapper, OrderSkuVO.class, relationManyBuilders);
    }
}

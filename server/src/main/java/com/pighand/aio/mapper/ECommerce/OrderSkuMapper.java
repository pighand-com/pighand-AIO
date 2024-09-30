package com.pighand.aio.mapper.ECommerce;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.OrderSkuDomain;
import com.pighand.aio.vo.ECommerce.OrderSkuVO;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.BeanUtil;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.pighand.aio.domain.ECommerce.table.GoodsSkuTableDef.GOODS_SKU;
import static com.pighand.aio.domain.ECommerce.table.GoodsSpuTableDef.GOODS_SPU;
import static com.pighand.aio.domain.ECommerce.table.OrderSkuTableDef.ORDER_SKU;
import static com.pighand.aio.domain.ECommerce.table.OrderTableDef.ORDER;
import static com.pighand.aio.domain.ECommerce.table.OrderTradeTableDef.ORDER_TRADE;
import static com.pighand.aio.domain.ECommerce.table.SessionTableDef.SESSION;
import static com.pighand.aio.domain.ECommerce.table.TicketTableDef.TICKET;

/**
 * 电商 - 订单 - SKU
 *
 * @author wangshuli
 * @createDate 2024-06-05 17:35:51
 */
@Mapper
public interface OrderSkuMapper extends BaseMapper<OrderSkuDomain> {

    /**
     * 基础查询条件
     *
     * @return
     */
    default QueryWrapper relationOne(Set<String> joinTables, QueryWrapper queryWrapper) {
        if (queryWrapper == null) {
            queryWrapper = QueryWrapper.create();
        }

        if (joinTables == null || joinTables.isEmpty()) {
            return queryWrapper;
        }

        // ORDER
        if (joinTables.contains(ORDER.getTableName())) {
            queryWrapper.leftJoin(ORDER).on(ORDER.ID.eq(ORDER_SKU.ORDER_ID));

            joinTables.remove(ORDER.getTableName());
        }

        // ORDER_TRADE
        if (joinTables.contains(ORDER_TRADE.getTableName())) {
            queryWrapper.leftJoin(ORDER_TRADE).on(ORDER_TRADE.ID.eq(ORDER_SKU.ORDER_TRADE_ID));

            joinTables.remove(ORDER_TRADE.getTableName());
        }

        // GOODS_SPU
        if (joinTables.contains(GOODS_SPU.getTableName())) {
            queryWrapper.leftJoin(GOODS_SPU).on(GOODS_SPU.ID.eq(ORDER_SKU.SPU_ID));

            joinTables.remove(GOODS_SPU.getTableName());
        }

        // GOODS_SKU
        if (joinTables.contains(GOODS_SKU.getTableName())) {
            queryWrapper.leftJoin(GOODS_SKU).on(GOODS_SKU.ID.eq(ORDER_SKU.SKU_ID));

            joinTables.remove(GOODS_SKU.getTableName());
        }

        // TICKET
        if (joinTables.contains(TICKET.getTableName())) {
            queryWrapper.leftJoin(TICKET).on(TICKET.ID.eq(ORDER_SKU.TICKET_ID));

            joinTables.remove(TICKET.getTableName());
        }

        // SESSION
        if (joinTables.contains(SESSION.getTableName())) {
            queryWrapper.leftJoin(SESSION).on(SESSION.ID.eq(ORDER_SKU.SESSION_ID));

            joinTables.remove(SESSION.getTableName());
        }

        return queryWrapper;
    }

    /**
     * 一对多关联查询结果
     *
     * @return
     */
    default void relationMany(Set<String> joinTables, Object result) {
        if (joinTables == null || joinTables.isEmpty()) {
            return;
        }

        boolean isList = result instanceof List;

        List<Function<OrderSkuVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<OrderSkuVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

        List<Function<Set<Long>, List>> subTableQueriesList = null;
        List<Function<Long, List>> subTableQueriesSingle = null;
        if (isList) {
            subTableQueriesList = new ArrayList<>(joinTables.size());
        } else {
            subTableQueriesSingle = new ArrayList<>(joinTables.size());
        }

        if (result instanceof List) {
            BeanUtil.queryWithRelatedData((List)result, mainIdGetters, subTableQueriesList, subTableIdGetter,
                subResultSetter);
        } else {
            BeanUtil.queryWithRelatedData((OrderSkuVO)result, mainIdGetters, subTableQueriesSingle, subResultSetter);
        }
    }

    /**
     * 查询详情
     *
     * @param id         主键
     * @param joinTables 关联表
     * @return
     */
    default OrderSkuVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(ORDER_SKU.ID.eq(id));

        OrderSkuVO result = this.selectOneByQueryAs(queryWrapper, OrderSkuVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 查询详情
     *
     * @param queryWrapper
     * @param joinTables   关联表
     * @return
     */
    default OrderSkuVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        OrderSkuVO result = this.selectOneByQueryAs(finalQueryWrapper, OrderSkuVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param orderSkuDomain
     * @return
     */
    default PageOrList<OrderSkuVO> query(OrderSkuDomain orderSkuDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(orderSkuDomain.getJoinTables(), queryWrapper);

        PageOrList<OrderSkuVO> result = this.page(orderSkuDomain, finalQueryWrapper, OrderSkuVO.class);
        this.relationMany(orderSkuDomain.getJoinTables(), result.getRecords());

        return result;
    }
}

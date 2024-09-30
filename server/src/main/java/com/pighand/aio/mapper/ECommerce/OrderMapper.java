package com.pighand.aio.mapper.ECommerce;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.OrderDomain;
import com.pighand.aio.domain.ECommerce.OrderSkuDomain;
import com.pighand.aio.vo.ECommerce.OrderSkuVO;
import com.pighand.aio.vo.ECommerce.OrderVO;
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

import static com.pighand.aio.domain.ECommerce.table.CouponUserTableDef.COUPON_USER;
import static com.pighand.aio.domain.ECommerce.table.OrderSkuTableDef.ORDER_SKU;
import static com.pighand.aio.domain.ECommerce.table.OrderTableDef.ORDER;
import static com.pighand.aio.domain.ECommerce.table.OrderTradeTableDef.ORDER_TRADE;
import static com.pighand.aio.domain.ECommerce.table.SessionUserCycleTableDef.SESSION_USER_CYCLE;
import static com.pighand.aio.domain.ECommerce.table.StoreTableDef.STORE;
import static com.pighand.aio.domain.ECommerce.table.TicketUserTableDef.TICKET_USER;
import static com.pighand.aio.domain.base.table.UserTableDef.USER;

/**
 * 电商 - 订单
 *
 * @author wangshuli
 * @createDate 2024-06-05 17:35:51
 */
@Mapper
public interface OrderMapper extends BaseMapper<OrderDomain> {

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

        // SESSION_USER_CYCLE
        if (joinTables.contains(SESSION_USER_CYCLE.getTableName())) {
            queryWrapper.leftJoin(SESSION_USER_CYCLE).on(SESSION_USER_CYCLE.ORDER_ID.eq(ORDER.ID));

            joinTables.remove(SESSION_USER_CYCLE.getTableName());
        }

        // TICKET_USER
        if (joinTables.contains(TICKET_USER.getTableName())) {
            queryWrapper.leftJoin(TICKET_USER).on(TICKET_USER.ORDER_ID.eq(ORDER.ID));

            joinTables.remove(TICKET_USER.getTableName());
        }

        // COUPON_USER
        if (joinTables.contains(COUPON_USER.getTableName())) {
            queryWrapper.leftJoin(COUPON_USER).on(COUPON_USER.USED_ORDER_ID.eq(ORDER.ID));

            joinTables.remove(COUPON_USER.getTableName());
        }

        // STORE
        if (joinTables.contains(STORE.getTableName())) {
            queryWrapper.leftJoin(STORE).on(STORE.ID.eq(ORDER.STORE_ID));

            joinTables.remove(STORE.getTableName());
        }

        // ORDER_TRADE
        if (joinTables.contains(ORDER_TRADE.getTableName())) {
            queryWrapper.leftJoin(ORDER_TRADE).on(ORDER_TRADE.ID.eq(ORDER.ORDER_TRADE_ID));

            joinTables.remove(ORDER_TRADE.getTableName());
        }

        // USER
        if (joinTables.contains(USER.getTableName())) {
            queryWrapper.leftJoin(USER).on(USER.ID.eq(ORDER.CREATOR_ID));

            joinTables.remove(USER.getTableName());
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

        List<Function<OrderVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<OrderVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

        List<Function<Set<Long>, List>> subTableQueriesList = null;
        List<Function<Long, List>> subTableQueriesSingle = null;
        if (isList) {
            subTableQueriesList = new ArrayList<>(joinTables.size());
        } else {
            subTableQueriesSingle = new ArrayList<>(joinTables.size());
        }

        // ORDER_SKU
        if (joinTables.contains(ORDER_SKU.getTableName())) {
            mainIdGetters.add(OrderVO::getId);

            if (subTableQueriesList != null) {
                subTableQueriesList.add(
                    ids -> new OrderSkuDomain().select(ORDER_SKU.DEFAULT_COLUMNS).where(ORDER_SKU.ORDER_ID.in(ids))
                        .listAs(OrderSkuVO.class));
            } else {
                subTableQueriesSingle.add(
                    id -> new OrderSkuDomain().select(ORDER_SKU.DEFAULT_COLUMNS).where(ORDER_SKU.ORDER_ID.eq(id))
                        .listAs(OrderSkuVO.class));
            }

            subTableIdGetter.add(obj -> ((OrderSkuVO)obj).getOrderId());
            subResultSetter.add((vo, list) -> vo.setOrderSku((List<OrderSkuVO>)list));
        }

        if (result instanceof List) {
            BeanUtil.queryWithRelatedData((List)result, mainIdGetters, subTableQueriesList, subTableIdGetter,
                subResultSetter);
        } else {
            BeanUtil.queryWithRelatedData((OrderVO)result, mainIdGetters, subTableQueriesSingle, subResultSetter);
        }
    }

    /**
     * 查询详情
     *
     * @param id         主键
     * @param joinTables 关联表
     * @return
     */
    default OrderVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(ORDER.ID.eq(id));

        OrderVO result = this.selectOneByQueryAs(queryWrapper, OrderVO.class);
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
    default OrderVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        OrderVO result = this.selectOneByQueryAs(finalQueryWrapper, OrderVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param orderDomain
     * @return
     */
    default PageOrList<OrderVO> query(OrderDomain orderDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(orderDomain.getJoinTables(), queryWrapper);

        PageOrList<OrderVO> result = this.page(orderDomain, finalQueryWrapper, OrderVO.class);
        this.relationMany(orderDomain.getJoinTables(), result.getRecords());

        return result;
    }
}

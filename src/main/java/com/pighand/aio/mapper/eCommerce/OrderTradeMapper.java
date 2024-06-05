package com.pighand.aio.mapper.ECommerce;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.OrderSkuDomain;
import com.pighand.aio.domain.ECommerce.OrderTradeDomain;
import com.pighand.aio.vo.ECommerce.OrderSkuVO;
import com.pighand.aio.vo.ECommerce.OrderTradeVO;
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

import static com.pighand.aio.domain.ECommerce.table.OrderSkuTableDef.ORDER_SKU;
import static com.pighand.aio.domain.ECommerce.table.OrderTableDef.ORDER;
import static com.pighand.aio.domain.ECommerce.table.OrderTradeTableDef.ORDER_TRADE;

/**
 * 电商 - 订单 - 交易单
 *
 * @author wangshuli
 * @createDate 2024-06-05 17:35:51
 */
@Mapper
public interface OrderTradeMapper extends BaseMapper<OrderTradeDomain> {

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
            queryWrapper.leftJoin(ORDER).on(ORDER.ORDER_TRADE_ID.eq(ORDER_TRADE.ID));

            joinTables.remove(ORDER.getTableName());
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

        List<Function<OrderTradeVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<OrderTradeVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

        List<Function<Set<Long>, List>> subTableQueriesList = null;
        List<Function<Long, List>> subTableQueriesSingle = null;
        if (isList) {
            subTableQueriesList = new ArrayList<>(joinTables.size());
        } else {
            subTableQueriesSingle = new ArrayList<>(joinTables.size());
        }

        // ORDER_SKU
        if (joinTables.contains(ORDER_SKU.getTableName())) {
            mainIdGetters.add(OrderTradeVO::getId);

            if (subTableQueriesList != null) {
                subTableQueriesList.add(ids -> new OrderSkuDomain().select(ORDER_SKU.DEFAULT_COLUMNS)
                    .where(ORDER_SKU.ORDER_TRADE_ID.in(ids)).listAs(OrderSkuVO.class));
            } else {
                subTableQueriesSingle.add(
                    id -> new OrderSkuDomain().select(ORDER_SKU.DEFAULT_COLUMNS).where(ORDER_SKU.ORDER_TRADE_ID.eq(id))
                        .listAs(OrderSkuVO.class));
            }

            subTableIdGetter.add(obj -> ((OrderSkuVO)obj).getOrderTradeId());
            subResultSetter.add((vo, list) -> vo.setOrderSku((List<OrderSkuVO>)list));
        }

        if (result instanceof List) {
            BeanUtil.queryWithRelatedData((List)result, mainIdGetters, subTableQueriesList, subTableIdGetter,
                subResultSetter);
        } else {
            BeanUtil.queryWithRelatedData((OrderTradeVO)result, mainIdGetters, subTableQueriesSingle, subResultSetter);
        }
    }

    /**
     * 查询详情
     *
     * @param id         主键
     * @param joinTables 关联表
     * @return
     */
    default OrderTradeVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(ORDER_TRADE.ID.eq(id));

        OrderTradeVO result = this.selectOneByQueryAs(queryWrapper, OrderTradeVO.class);
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
    default OrderTradeVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        OrderTradeVO result = this.selectOneByQueryAs(finalQueryWrapper, OrderTradeVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param orderTradeDomain
     * @return
     */
    default PageOrList<OrderTradeVO> query(OrderTradeDomain orderTradeDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(orderTradeDomain.getJoinTables(), queryWrapper);

        PageOrList<OrderTradeVO> result = this.page(orderTradeDomain, finalQueryWrapper, OrderTradeVO.class);
        this.relationMany(orderTradeDomain.getJoinTables(), result.getRecords());

        return result;
    }
}

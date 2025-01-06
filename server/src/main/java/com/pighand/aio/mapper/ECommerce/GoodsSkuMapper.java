package com.pighand.aio.mapper.ECommerce;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.GoodsSkuDomain;
import com.pighand.aio.domain.ECommerce.OrderSkuDomain;
import com.pighand.aio.vo.ECommerce.GoodsSkuVO;
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
import static com.pighand.aio.domain.base.table.StoreTableDef.STORE;

/**
 * 电商 - SKU
 *
 * @author wangshuli
 * @createDate 2024-06-05 17:35:51
 */
@Mapper
public interface GoodsSkuMapper extends BaseMapper<GoodsSkuDomain> {

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

        // STORE
        if (joinTables.contains(STORE.getTableName())) {
            queryWrapper.leftJoin(STORE).on(STORE.ID.eq(GOODS_SKU.STORE_ID));

            joinTables.remove(STORE.getTableName());
        }

        // GOODS_SPU
        if (joinTables.contains(GOODS_SPU.getTableName())) {
            queryWrapper.leftJoin(GOODS_SPU).on(GOODS_SPU.ID.eq(GOODS_SKU.SPU_ID));

            joinTables.remove(GOODS_SPU.getTableName());
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

        List<Function<GoodsSkuVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<GoodsSkuVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

        List<Function<Set<Long>, List>> subTableQueriesList = null;
        List<Function<Long, List>> subTableQueriesSingle = null;
        if (isList) {
            subTableQueriesList = new ArrayList<>(joinTables.size());
        } else {
            subTableQueriesSingle = new ArrayList<>(joinTables.size());
        }

        // ORDER_SKU
        if (joinTables.contains(ORDER_SKU.getTableName())) {
            mainIdGetters.add(GoodsSkuVO::getId);

            if (subTableQueriesList != null) {
                subTableQueriesList.add(
                    ids -> new OrderSkuDomain().select(ORDER_SKU.DEFAULT_COLUMNS).where(ORDER_SKU.SKU_ID.in(ids))
                        .listAs(OrderSkuVO.class));
            } else {
                subTableQueriesSingle.add(
                    id -> new OrderSkuDomain().select(ORDER_SKU.DEFAULT_COLUMNS).where(ORDER_SKU.SKU_ID.eq(id))
                        .listAs(OrderSkuVO.class));
            }

            subTableIdGetter.add(obj -> ((OrderSkuVO)obj).getSkuId());
            subResultSetter.add((vo, list) -> vo.setOrderSku((List<OrderSkuVO>)list));
        }

        if (result instanceof List) {
            BeanUtil.queryWithRelatedData((List)result, mainIdGetters, subTableQueriesList, subTableIdGetter,
                subResultSetter);
        } else {
            BeanUtil.queryWithRelatedData((GoodsSkuVO)result, mainIdGetters, subTableQueriesSingle, subResultSetter);
        }
    }

    /**
     * 查询详情
     *
     * @param id         主键
     * @param joinTables 关联表
     * @return
     */
    default GoodsSkuVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(GOODS_SKU.ID.eq(id));

        GoodsSkuVO result = this.selectOneByQueryAs(queryWrapper, GoodsSkuVO.class);
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
    default GoodsSkuVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        GoodsSkuVO result = this.selectOneByQueryAs(finalQueryWrapper, GoodsSkuVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param goodsSkuDomain
     * @return
     */
    default PageOrList<GoodsSkuVO> query(GoodsSkuDomain goodsSkuDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(goodsSkuDomain.getJoinTables(), queryWrapper);

        PageOrList<GoodsSkuVO> result = this.page(goodsSkuDomain, finalQueryWrapper, GoodsSkuVO.class);
        this.relationMany(goodsSkuDomain.getJoinTables(), result.getRecords());

        return result;
    }
}

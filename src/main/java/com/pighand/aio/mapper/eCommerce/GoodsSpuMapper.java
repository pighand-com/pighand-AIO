package com.pighand.aio.mapper.ECommerce;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.GoodsSpuDomain;
import com.pighand.aio.domain.ECommerce.OrderSkuDomain;
import com.pighand.aio.vo.ECommerce.GoodsSpuVO;
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

import static com.pighand.aio.domain.ECommerce.table.GoodsCategoryTableDef.GOODS_CATEGORY;
import static com.pighand.aio.domain.ECommerce.table.GoodsSpuTableDef.GOODS_SPU;
import static com.pighand.aio.domain.ECommerce.table.OrderSkuTableDef.ORDER_SKU;
import static com.pighand.aio.domain.ECommerce.table.StoreTableDef.STORE;

/**
 * 电商 - SPU
 *
 * @author wangshuli
 * @createDate 2024-06-05 17:35:51
 */
@Mapper
public interface GoodsSpuMapper extends BaseMapper<GoodsSpuDomain> {

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
            queryWrapper.leftJoin(STORE).on(STORE.ID.eq(GOODS_SPU.STORE_ID));

            joinTables.remove(STORE.getTableName());
        }

        // GOODS_CATEGORY
        if (joinTables.contains(GOODS_CATEGORY.getTableName())) {
            queryWrapper.leftJoin(GOODS_CATEGORY).on(GOODS_CATEGORY.ID.eq(GOODS_SPU.GOODS_CATEGORY_ID));

            joinTables.remove(GOODS_CATEGORY.getTableName());
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

        List<Function<GoodsSpuVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<GoodsSpuVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

        List<Function<Set<Long>, List>> subTableQueriesList = null;
        List<Function<Long, List>> subTableQueriesSingle = null;
        if (isList) {
            subTableQueriesList = new ArrayList<>(joinTables.size());
        } else {
            subTableQueriesSingle = new ArrayList<>(joinTables.size());
        }

        // ORDER_SKU
        if (joinTables.contains(ORDER_SKU.getTableName())) {
            mainIdGetters.add(GoodsSpuVO::getId);

            if (subTableQueriesList != null) {
                subTableQueriesList.add(
                    ids -> new OrderSkuDomain().select(ORDER_SKU.DEFAULT_COLUMNS).where(ORDER_SKU.SPU_ID.in(ids))
                        .listAs(OrderSkuVO.class));
            } else {
                subTableQueriesSingle.add(
                    id -> new OrderSkuDomain().select(ORDER_SKU.DEFAULT_COLUMNS).where(ORDER_SKU.SPU_ID.eq(id))
                        .listAs(OrderSkuVO.class));
            }

            subTableIdGetter.add(obj -> ((OrderSkuVO)obj).getSpuId());
            subResultSetter.add((vo, list) -> vo.setOrderSku((List<OrderSkuVO>)list));
        }

        if (result instanceof List) {
            BeanUtil.queryWithRelatedData((List)result, mainIdGetters, subTableQueriesList, subTableIdGetter,
                subResultSetter);
        } else {
            BeanUtil.queryWithRelatedData((GoodsSpuVO)result, mainIdGetters, subTableQueriesSingle, subResultSetter);
        }
    }

    /**
     * 查询详情
     *
     * @param id         主键
     * @param joinTables 关联表
     * @return
     */
    default GoodsSpuVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(GOODS_SPU.ID.eq(id));

        GoodsSpuVO result = this.selectOneByQueryAs(queryWrapper, GoodsSpuVO.class);
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
    default GoodsSpuVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        GoodsSpuVO result = this.selectOneByQueryAs(finalQueryWrapper, GoodsSpuVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param goodsSpuDomain
     * @return
     */
    default PageOrList<GoodsSpuVO> query(GoodsSpuDomain goodsSpuDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(goodsSpuDomain.getJoinTables(), queryWrapper);

        PageOrList<GoodsSpuVO> result = this.page(goodsSpuDomain, finalQueryWrapper, GoodsSpuVO.class);
        this.relationMany(goodsSpuDomain.getJoinTables(), result.getRecords());

        return result;
    }
}

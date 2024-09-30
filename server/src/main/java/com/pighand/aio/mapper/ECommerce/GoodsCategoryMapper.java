package com.pighand.aio.mapper.ECommerce;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.GoodsCategoryDomain;
import com.pighand.aio.domain.ECommerce.GoodsSpuDomain;
import com.pighand.aio.vo.ECommerce.GoodsCategoryVO;
import com.pighand.aio.vo.ECommerce.GoodsSpuVO;
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

/**
 * 电商 - 商品类目
 *
 * @author wangshuli
 * @createDate 2024-06-05 17:35:51
 */
@Mapper
public interface GoodsCategoryMapper extends BaseMapper<GoodsCategoryDomain> {

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

        List<Function<GoodsCategoryVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<GoodsCategoryVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

        List<Function<Set<Long>, List>> subTableQueriesList = null;
        List<Function<Long, List>> subTableQueriesSingle = null;
        if (isList) {
            subTableQueriesList = new ArrayList<>(joinTables.size());
        } else {
            subTableQueriesSingle = new ArrayList<>(joinTables.size());
        }

        // GOODS_SPU
        if (joinTables.contains(GOODS_SPU.getTableName())) {
            mainIdGetters.add(GoodsCategoryVO::getId);

            if (subTableQueriesList != null) {
                subTableQueriesList.add(ids -> new GoodsSpuDomain().select(GOODS_SPU.DEFAULT_COLUMNS)
                    .where(GOODS_SPU.GOODS_CATEGORY_ID.in(ids)).listAs(GoodsSpuVO.class));
            } else {
                subTableQueriesSingle.add(id -> new GoodsSpuDomain().select(GOODS_SPU.DEFAULT_COLUMNS)
                    .where(GOODS_SPU.GOODS_CATEGORY_ID.eq(id)).listAs(GoodsSpuVO.class));
            }

            subTableIdGetter.add(obj -> ((GoodsSpuVO)obj).getGoodsCategoryId());
            subResultSetter.add((vo, list) -> vo.setGoodsSpu((List<GoodsSpuVO>)list));
        }

        if (result instanceof List) {
            BeanUtil.queryWithRelatedData((List)result, mainIdGetters, subTableQueriesList, subTableIdGetter,
                subResultSetter);
        } else {
            BeanUtil.queryWithRelatedData((GoodsCategoryVO)result, mainIdGetters, subTableQueriesSingle,
                subResultSetter);
        }
    }

    /**
     * 查询详情
     *
     * @param id         主键
     * @param joinTables 关联表
     * @return
     */
    default GoodsCategoryVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(GOODS_CATEGORY.ID.eq(id));

        GoodsCategoryVO result = this.selectOneByQueryAs(queryWrapper, GoodsCategoryVO.class);
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
    default GoodsCategoryVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        GoodsCategoryVO result = this.selectOneByQueryAs(finalQueryWrapper, GoodsCategoryVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param goodsCategoryDomain
     * @return
     */
    default PageOrList<GoodsCategoryVO> query(GoodsCategoryDomain goodsCategoryDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(goodsCategoryDomain.getJoinTables(), queryWrapper);

        PageOrList<GoodsCategoryVO> result = this.page(goodsCategoryDomain, finalQueryWrapper, GoodsCategoryVO.class);
        this.relationMany(goodsCategoryDomain.getJoinTables(), result.getRecords());

        return result;
    }
}

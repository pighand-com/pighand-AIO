package com.pighand.aio.mapper.eCommerce;

import com.mybatisflex.core.field.FieldQueryBuilder;
import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.eCommerce.GoodsSpuDomain;
import com.pighand.aio.vo.eCommerce.GoodsSpuVO;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.framework.spring.page.PageOrList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.function.Consumer;

import static com.pighand.aio.domain.eCommerce.table.GoodsCategoryTableDef.GOODS_CATEGORY;
import static com.pighand.aio.domain.eCommerce.table.GoodsSkuTableDef.GOODS_SKU;
import static com.pighand.aio.domain.eCommerce.table.GoodsSpuTableDef.GOODS_SPU;

/**
 * 电商 - SPU
 *
 * @author wangshuli
 * @createDate 2024-01-08 11:05:18
 */
@Mapper
public interface GoodsSpuMapper extends BaseMapper<GoodsSpuDomain> {

    /**
     * 一对一关联查询条件
     *
     * @return QueryWrapper
     */
    default QueryWrapper relationOne(List<String> joinTables, QueryWrapper queryWrapper) {
        if (queryWrapper == null) {
            queryWrapper = QueryWrapper.create();
        }

        if (joinTables == null) {
            return queryWrapper;
        }

        if (joinTables.contains(GOODS_CATEGORY.getTableName())) {
            queryWrapper.leftJoin(GOODS_CATEGORY).on(GOODS_CATEGORY.ID.eq(GOODS_SPU.GOODS_CATEGORY_ID));
        }

        return queryWrapper;
    }

    /**
     * 一对多关联查询结果
     *
     * @return
     */
    default Consumer<FieldQueryBuilder<GoodsSpuVO>>[] relationMany(List<String> joinTables) {
        if (joinTables == null) {
            return null;
        }

        boolean hasGoodsSku = joinTables.contains(GOODS_SKU.getTableName());

        int length = 0;
        if (hasGoodsSku) {
            length++;
        }

        Consumer<FieldQueryBuilder<GoodsSpuVO>>[] fieldQueryBuilders = new Consumer[length];

        int nowIndex = 0;
        if (hasGoodsSku) {
            Consumer<FieldQueryBuilder<GoodsSpuVO>> consumer = (builder) -> {
                builder.field(GoodsSpuVO::getGoodsSku).queryWrapper(goodsSpuVO -> QueryWrapper.create().from(GOODS_SKU)
                    .where(GOODS_SKU.GOODS_SPU_ID.eq(goodsSpuVO.getId())));
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
    default GoodsSpuVO find(Long id, List<String> joinTables) {
        QueryWrapper queryWrapper = this.relationOne(joinTables, null).where(GOODS_SPU.ID.eq(id));
        Consumer<FieldQueryBuilder<GoodsSpuVO>>[] relationManyBuilders = this.relationMany(joinTables);

        return this.selectOneByQueryAs(queryWrapper, GoodsSpuVO.class, relationManyBuilders);
    }

    /**
     * 查询详情
     *
     * @param queryWrapper
     * @param joinTables   关联表
     * @return
     */
    default GoodsSpuVO find(QueryWrapper queryWrapper, List<String> joinTables) {
        QueryWrapper finalQueryWrapper = this.relationOne(joinTables, queryWrapper);
        Consumer<FieldQueryBuilder<GoodsSpuVO>>[] relationManyBuilders = this.relationMany(joinTables);

        return this.selectOneByQueryAs(finalQueryWrapper, GoodsSpuVO.class, relationManyBuilders);
    }

    /**
     * 分页或列表
     *
     * @param goodsSpuDomain
     * @return
     */
    default PageOrList<GoodsSpuVO> query(GoodsSpuDomain goodsSpuDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(goodsSpuDomain.getJoinTables(), queryWrapper);
        Consumer<FieldQueryBuilder<GoodsSpuVO>>[] relationManyBuilders =
            this.relationMany(goodsSpuDomain.getJoinTables());

        return this.page(goodsSpuDomain, finalQueryWrapper, GoodsSpuVO.class, relationManyBuilders);
    }
}

package com.pighand.aio.mapper.ECommerce;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.GoodsSkuDomain;
import com.pighand.aio.vo.ECommerce.GoodsSkuVO;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.framework.spring.page.PageOrList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

import static com.pighand.aio.domain.ECommerce.table.GoodsSkuTableDef.GOODS_SKU;
import static com.pighand.aio.domain.ECommerce.table.GoodsSpuTableDef.GOODS_SPU;

/**
 * 电商 - SKU
 *
 * @author wangshuli
 * @createDate 2024-01-08 11:05:18
 */
@Mapper
public interface GoodsSkuMapper extends BaseMapper<GoodsSkuDomain> {

    /**
     * 基础查询条件
     *
     * @return
     */
    default QueryWrapper baseQuery(List<String> joinTables, QueryWrapper queryWrapper) {
        if (queryWrapper == null) {
            queryWrapper = QueryWrapper.create();
        }

        if (joinTables == null) {
            return queryWrapper;
        }

        if (joinTables.contains(GOODS_SPU.getTableName())) {
            queryWrapper.leftJoin(GOODS_SPU).on(GOODS_SPU.ID.eq(GOODS_SKU.SPU_ID));
        }

        return queryWrapper;
    }

    /**
     * 查询详情
     *
     * @param id         主键
     * @param joinTables 关联表
     * @return
     */
    default GoodsSkuVO find(Long id, List<String> joinTables) {
        QueryWrapper queryWrapper = this.baseQuery(joinTables, null).where(GOODS_SKU.ID.eq(id));

        return this.selectOneByQueryAs(queryWrapper, GoodsSkuVO.class);
    }

    /**
     * 查询详情
     *
     * @param queryWrapper
     * @param joinTables   关联表
     * @return
     */
    default GoodsSkuVO find(QueryWrapper queryWrapper, List<String> joinTables) {
        QueryWrapper finalQueryWrapper = this.baseQuery(joinTables, queryWrapper);

        return this.selectOneByQueryAs(finalQueryWrapper, GoodsSkuVO.class);
    }

    /**
     * 分页或列表
     *
     * @param goodsSkuDomain
     * @return
     */
    default PageOrList<GoodsSkuVO> query(GoodsSkuDomain goodsSkuDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.baseQuery(goodsSkuDomain.getJoinTables(), queryWrapper);

        return this.page(goodsSkuDomain, finalQueryWrapper, GoodsSkuVO.class);
    }
}

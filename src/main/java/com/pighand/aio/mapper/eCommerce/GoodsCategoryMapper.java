package com.pighand.aio.mapper.eCommerce;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.eCommerce.GoodsCategoryDomain;
import com.pighand.aio.vo.eCommerce.GoodsCategoryVO;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.framework.spring.page.PageOrList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

import static com.pighand.aio.domain.eCommerce.table.GoodsCategoryTableDef.GOODS_CATEGORY;
import static com.pighand.aio.domain.eCommerce.table.GoodsSpuTableDef.GOODS_SPU;

/**
 * 电商 - 商品类目
 *
 * @author wangshuli
 * @createDate 2024-01-08 11:05:18
 */
@Mapper
public interface GoodsCategoryMapper extends BaseMapper<GoodsCategoryDomain> {

    /**
     * 基础查询条件
     *
     * @return
     */
    default QueryWrapper baseQuery(List<String> joinTables, QueryWrapper queryWrapper) {
        if (queryWrapper == null) {
            queryWrapper = QueryWrapper.create();
        }

        if (joinTables != null && joinTables.contains(GOODS_SPU.getTableName())) {
            queryWrapper.leftJoin(GOODS_SPU).on(GOODS_SPU.GOODS_CATEGORY_ID.eq(GOODS_SPU.ID));
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
    default GoodsCategoryVO find(Long id, List<String> joinTables) {
        QueryWrapper queryWrapper = this.baseQuery(joinTables, null).where(GOODS_CATEGORY.ID.eq(id));

        return this.selectOneByQueryAs(queryWrapper, GoodsCategoryVO.class);
    }

    /**
     * 查询详情
     *
     * @param queryWrapper
     * @param joinTables   关联表
     * @return
     */
    default GoodsCategoryVO find(QueryWrapper queryWrapper, List<String> joinTables) {
        QueryWrapper finalQueryWrapper = this.baseQuery(joinTables, queryWrapper);

        return this.selectOneByQueryAs(finalQueryWrapper, GoodsCategoryVO.class);
    }

    /**
     * 分页或列表
     *
     * @param goodsCategoryDomain
     * @return
     */
    default PageOrList<GoodsCategoryVO> query(GoodsCategoryDomain goodsCategoryDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.baseQuery(goodsCategoryDomain.getJoinTables(), queryWrapper);

        return this.page(goodsCategoryDomain, finalQueryWrapper, GoodsCategoryVO.class);
    }
}

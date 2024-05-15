package com.pighand.aio.mapper.ECommerce;

import com.mybatisflex.core.field.FieldQueryBuilder;
import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.BillDomain;
import com.pighand.aio.vo.ECommerce.BillVO;
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
 * 商城 - 账单
 *
 * @author wangshuli
 * @createDate 2024-04-18 14:35:34
 */
@Mapper
public interface BillMapper extends BaseMapper<BillDomain> {

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
            queryWrapper.leftJoin(ORDER).on(ORDER.ID.eq(BILL.ORDER_ID));
        }

        if (joinTables.contains(ORDER_TRADE.getTableName())) {
            queryWrapper.leftJoin(ORDER_TRADE).on(ORDER_TRADE.ID.eq(BILL.ORDER_TRADE_ID));
        }

        if (joinTables.contains(ORDER_SKU.getTableName())) {
            queryWrapper.leftJoin(ORDER_SKU).on(ORDER_SKU.ID.eq(BILL.ORDER_SKU_ID));
        }

        return queryWrapper;
    }

    /**
     * 一对多关联查询结果
     *
     * @return
     */
    default Consumer<FieldQueryBuilder<BillVO>>[] relationMany(List<String> joinTables) {
        if (joinTables == null) {
            return null;
        }

        int length = 0;

        Consumer<FieldQueryBuilder<BillVO>>[] fieldQueryBuilders = new Consumer[length];

        int nowIndex = 0;

        return fieldQueryBuilders;
    }

    /**
     * 查询详情
     *
     * @param id         主键
     * @param joinTables 关联表
     * @return
     */
    default BillVO find(Long id, List<String> joinTables) {
        QueryWrapper queryWrapper = this.relationOne(joinTables, null).where(BILL.ID.eq(id));
        Consumer<FieldQueryBuilder<BillVO>>[] relationManyBuilders = this.relationMany(joinTables);

        return this.selectOneByQueryAs(queryWrapper, BillVO.class, relationManyBuilders);
    }

    /**
     * 查询详情
     *
     * @param queryWrapper
     * @param joinTables   关联表
     * @return
     */
    default BillVO find(QueryWrapper queryWrapper, List<String> joinTables) {
        QueryWrapper finalQueryWrapper = this.relationOne(joinTables, queryWrapper);
        Consumer<FieldQueryBuilder<BillVO>>[] relationManyBuilders = this.relationMany(joinTables);

        return this.selectOneByQueryAs(finalQueryWrapper, BillVO.class, relationManyBuilders);
    }

    /**
     * 分页或列表
     *
     * @param billDomain
     * @return
     */
    default PageOrList<BillVO> query(BillDomain billDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(billDomain.getJoinTables(), queryWrapper);
        Consumer<FieldQueryBuilder<BillVO>>[] relationManyBuilders = this.relationMany(billDomain.getJoinTables());

        return this.page(billDomain, finalQueryWrapper, BillVO.class, relationManyBuilders);
    }
}

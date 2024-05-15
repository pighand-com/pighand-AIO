package com.pighand.aio.mapper.ECommerce;

import com.mybatisflex.core.field.FieldQueryBuilder;
import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.TicketDomain;
import com.pighand.aio.vo.ECommerce.TicketVO;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.framework.spring.page.PageOrList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.function.Consumer;

import static com.pighand.aio.domain.ECommerce.table.TicketTableDef.TICKET;

/**
 * 电商 - 票务
 *
 * @author wangshuli
 * @createDate 2024-04-26 14:52:18
 */
@Mapper
public interface TicketMapper extends BaseMapper<TicketDomain> {

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

        return queryWrapper;
    }

    /**
     * 一对多关联查询结果
     *
     * @return
     */
    default Consumer<FieldQueryBuilder<TicketVO>>[] relationMany(List<String> joinTables) {
        if (joinTables == null) {
            return null;
        }

        int length = 0;

        Consumer<FieldQueryBuilder<TicketVO>>[] fieldQueryBuilders = new Consumer[length];

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
    default TicketVO find(Long id, List<String> joinTables) {
        QueryWrapper queryWrapper = this.relationOne(joinTables, null).where(TICKET.ID.eq(id));
        Consumer<FieldQueryBuilder<TicketVO>>[] relationManyBuilders = this.relationMany(joinTables);

        // TODO: 将商品表的base实体标记为非表字段，字表重写本表字段
        queryWrapper.select(TICKET.ID, TICKET.NAME, TICKET.DETAILS, TICKET.ORIGINAL_PRICE, TICKET.CURRENT_PRICE,
            TICKET.VALIDATION_COUNT);

        return this.selectOneByQueryAs(queryWrapper, TicketVO.class, relationManyBuilders);
    }

    /**
     * 查询详情
     *
     * @param queryWrapper
     * @param joinTables   关联表
     * @return
     */
    default TicketVO find(QueryWrapper queryWrapper, List<String> joinTables) {
        QueryWrapper finalQueryWrapper = this.relationOne(joinTables, queryWrapper);
        Consumer<FieldQueryBuilder<TicketVO>>[] relationManyBuilders = this.relationMany(joinTables);

        return this.selectOneByQueryAs(finalQueryWrapper, TicketVO.class, relationManyBuilders);
    }

    /**
     * 分页或列表
     *
     * @param ticketDomain
     * @return
     */
    default PageOrList<TicketVO> query(TicketDomain ticketDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(ticketDomain.getJoinTables(), queryWrapper);
        Consumer<FieldQueryBuilder<TicketVO>>[] relationManyBuilders = this.relationMany(ticketDomain.getJoinTables());

        return this.page(ticketDomain, finalQueryWrapper, TicketVO.class, relationManyBuilders);
    }
}

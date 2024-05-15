package com.pighand.aio.mapper.ECommerce;

import com.mybatisflex.core.field.FieldQueryBuilder;
import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.TicketUserDomain;
import com.pighand.aio.vo.ECommerce.TicketUserVO;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.framework.spring.page.PageOrList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.function.Consumer;

import static com.pighand.aio.domain.ECommerce.table.TicketUserTableDef.TICKET_USER;

/**
 * 电商 - 已购票务
 *
 * @author wangshuli
 * @createDate 2024-04-26 14:52:18
 */
@Mapper
public interface TicketUserMapper extends BaseMapper<TicketUserDomain> {

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
    default Consumer<FieldQueryBuilder<TicketUserVO>>[] relationMany(List<String> joinTables) {
        if (joinTables == null) {
            return null;
        }

        int length = 0;

        Consumer<FieldQueryBuilder<TicketUserVO>>[] fieldQueryBuilders = new Consumer[length];

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
    default TicketUserVO find(Long id, List<String> joinTables) {
        QueryWrapper queryWrapper = this.relationOne(joinTables, null).where(TICKET_USER.ID.eq(id));
        Consumer<FieldQueryBuilder<TicketUserVO>>[] relationManyBuilders = this.relationMany(joinTables);

        return this.selectOneByQueryAs(queryWrapper, TicketUserVO.class, relationManyBuilders);
    }

    /**
     * 查询详情
     *
     * @param queryWrapper
     * @param joinTables   关联表
     * @return
     */
    default TicketUserVO find(QueryWrapper queryWrapper, List<String> joinTables) {
        QueryWrapper finalQueryWrapper = this.relationOne(joinTables, queryWrapper);
        Consumer<FieldQueryBuilder<TicketUserVO>>[] relationManyBuilders = this.relationMany(joinTables);

        return this.selectOneByQueryAs(finalQueryWrapper, TicketUserVO.class, relationManyBuilders);
    }

    /**
     * 分页或列表
     *
     * @param ticketUserDomain
     * @return
     */
    default PageOrList<TicketUserVO> query(TicketUserDomain ticketUserDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(ticketUserDomain.getJoinTables(), queryWrapper);
        Consumer<FieldQueryBuilder<TicketUserVO>>[] relationManyBuilders =
            this.relationMany(ticketUserDomain.getJoinTables());

        return this.page(ticketUserDomain, finalQueryWrapper, TicketUserVO.class, relationManyBuilders);
    }
}

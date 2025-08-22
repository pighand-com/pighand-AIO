package com.pighand.aio.mapper.ECommerce;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.TicketUserDomain;
import com.pighand.aio.vo.ECommerce.TicketUserVO;
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

import static com.pighand.aio.domain.ECommerce.table.OrderTableDef.ORDER;
import static com.pighand.aio.domain.ECommerce.table.ThemeTableDef.THEME;
import static com.pighand.aio.domain.ECommerce.table.TicketTableDef.TICKET;
import static com.pighand.aio.domain.ECommerce.table.TicketUserTableDef.TICKET_USER;
import static com.pighand.aio.domain.ECommerce.table.TicketUserValidityTableDef.TICKET_USER_VALIDITY;

/**
 * 电商 - 已购票务
 *
 * @author wangshuli
 * @createDate 2024-06-05 17:35:51
 */
@Mapper
public interface TicketUserMapper extends BaseMapper<TicketUserDomain> {

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

        // TICKET
        if (joinTables.contains(TICKET.getTableName())) {
            queryWrapper.select(TICKET.ID, TICKET.NAME, TICKET.POSTER_URL).leftJoin(TICKET)
                .on(TICKET.ID.eq(TICKET_USER.TICKET_ID));

            joinTables.remove(TICKET.getTableName());
        }

        // THEME
        if (joinTables.contains(THEME.getTableName())) {
            queryWrapper.select(THEME.POSTER_URL).leftJoin(THEME).on(THEME.ID.eq(TICKET.THEME_ID));

            joinTables.remove(TICKET.getTableName());
        }

        // ORDER
        if (joinTables.contains(ORDER.getTableName())) {
            queryWrapper.leftJoin(ORDER).on(ORDER.ID.eq(TICKET_USER.ORDER_ID));

            joinTables.remove(ORDER.getTableName());
        }

        // TICKET_USER_VALIDITY
        if (joinTables.contains(TICKET_USER_VALIDITY.getTableName())) {
            queryWrapper.leftJoin(TICKET_USER_VALIDITY).on(TICKET_USER_VALIDITY.TICKET_USER_ID.eq(TICKET_USER.ID));

            joinTables.remove(TICKET_USER_VALIDITY.getTableName());
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

        List<Function<TicketUserVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<TicketUserVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

        List<Function<Set<Long>, List>> subTableQueriesList = null;
        List<Function<Long, List>> subTableQueriesSingle = null;
        if (isList) {
            subTableQueriesList = new ArrayList<>(joinTables.size());
        } else {
            subTableQueriesSingle = new ArrayList<>(joinTables.size());
        }

        if (result instanceof List) {
            BeanUtil.queryWithRelatedData((List)result, mainIdGetters, subTableQueriesList, subTableIdGetter,
                subResultSetter);
        } else {
            BeanUtil.queryWithRelatedData((TicketUserVO)result, mainIdGetters, subTableQueriesSingle, subResultSetter);
        }
    }

    /**
     * 查询详情
     *
     * @param id         主键
     * @param joinTables 关联表
     * @return
     */
    default TicketUserVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(TICKET_USER.ID.eq(id));

        TicketUserVO result = this.selectOneByQueryAs(queryWrapper, TicketUserVO.class);
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
    default TicketUserVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        TicketUserVO result = this.selectOneByQueryAs(finalQueryWrapper, TicketUserVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param ticketUserDomain
     * @return
     */
    default PageOrList<TicketUserVO> query(TicketUserDomain ticketUserDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(ticketUserDomain.getJoinTables(), queryWrapper);

        PageOrList<TicketUserVO> result = this.page(ticketUserDomain, finalQueryWrapper, TicketUserVO.class);
        this.relationMany(ticketUserDomain.getJoinTables(), result.getRecords());

        return result;
    }
}

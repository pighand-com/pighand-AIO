package com.pighand.aio.mapper.ECommerce;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.TicketValidityDomain;
import com.pighand.aio.vo.ECommerce.TicketValidityVO;
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

import static com.pighand.aio.domain.ECommerce.table.TicketTableDef.TICKET;
import static com.pighand.aio.domain.ECommerce.table.TicketUserValidityTableDef.TICKET_USER_VALIDITY;
import static com.pighand.aio.domain.ECommerce.table.TicketValidityTableDef.TICKET_VALIDITY;

/**
 * 电商 - 票务 - 使用范围
 *
 * @author wangshuli
 * @createDate 2024-06-05 17:35:51
 */
@Mapper
public interface TicketValidityMapper extends BaseMapper<TicketValidityDomain> {

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
            queryWrapper.leftJoin(TICKET).on(TICKET.ID.eq(TICKET_VALIDITY.TICKET_ID));

            joinTables.remove(TICKET.getTableName());
        }

        // TICKET_USER_VALIDITY
        if (joinTables.contains(TICKET_USER_VALIDITY.getTableName())) {
            queryWrapper.leftJoin(TICKET_USER_VALIDITY)
                .on(TICKET_USER_VALIDITY.TICKET_VALIDITY_ID.eq(TICKET_VALIDITY.ID));

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

        List<Function<TicketValidityVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<TicketValidityVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

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
            BeanUtil.queryWithRelatedData((TicketValidityVO)result, mainIdGetters, subTableQueriesSingle,
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
    default TicketValidityVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(TICKET_VALIDITY.ID.eq(id));

        TicketValidityVO result = this.selectOneByQueryAs(queryWrapper, TicketValidityVO.class);
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
    default TicketValidityVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        TicketValidityVO result = this.selectOneByQueryAs(finalQueryWrapper, TicketValidityVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param ticketValidityDomain
     * @return
     */
    default PageOrList<TicketValidityVO> query(TicketValidityDomain ticketValidityDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(ticketValidityDomain.getJoinTables(), queryWrapper);

        PageOrList<TicketValidityVO> result =
            this.page(ticketValidityDomain, finalQueryWrapper, TicketValidityVO.class);
        this.relationMany(ticketValidityDomain.getJoinTables(), result.getRecords());

        return result;
    }
}

package com.pighand.aio.mapper.ECommerce;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.TicketDomain;
import com.pighand.aio.domain.ECommerce.TicketValidityDomain;
import com.pighand.aio.vo.ECommerce.TicketVO;
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

import static com.pighand.aio.domain.ECommerce.table.StoreTableDef.STORE;
import static com.pighand.aio.domain.ECommerce.table.TicketTableDef.TICKET;
import static com.pighand.aio.domain.ECommerce.table.TicketValidityTableDef.TICKET_VALIDITY;

/**
 * 电商 - 票务
 *
 * @author wangshuli
 * @createDate 2024-06-05 17:35:51
 */
@Mapper
public interface TicketMapper extends BaseMapper<TicketDomain> {

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
            queryWrapper.leftJoin(STORE).on(STORE.ID.eq(TICKET.STORE_ID));

            joinTables.remove(STORE.getTableName());
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

        List<Function<TicketVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<TicketVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

        List<Function<Set<Long>, List>> subTableQueriesList = null;
        List<Function<Long, List>> subTableQueriesSingle = null;
        if (isList) {
            subTableQueriesList = new ArrayList<>(joinTables.size());
        } else {
            subTableQueriesSingle = new ArrayList<>(joinTables.size());
        }

        // TICKET_VALIDITY
        if (joinTables.contains(TICKET_VALIDITY.getTableName())) {
            mainIdGetters.add(TicketVO::getId);

            if (subTableQueriesList != null) {
                subTableQueriesList.add(ids -> new TicketValidityDomain().select(TICKET_VALIDITY.DEFAULT_COLUMNS)
                    .where(TICKET_VALIDITY.TICKET_ID.in(ids)).listAs(TicketValidityVO.class));
            } else {
                subTableQueriesSingle.add(id -> new TicketValidityDomain().select(TICKET_VALIDITY.DEFAULT_COLUMNS)
                    .where(TICKET_VALIDITY.TICKET_ID.eq(id)).listAs(TicketValidityVO.class));
            }

            subTableIdGetter.add(obj -> ((TicketValidityVO)obj).getTicketId());
            subResultSetter.add((vo, list) -> vo.setTicketValidity((List<TicketValidityVO>)list));
        }

        if (result instanceof List) {
            BeanUtil.queryWithRelatedData((List)result, mainIdGetters, subTableQueriesList, subTableIdGetter,
                subResultSetter);
        } else {
            BeanUtil.queryWithRelatedData((TicketVO)result, mainIdGetters, subTableQueriesSingle, subResultSetter);
        }
    }

    /**
     * 查询详情
     *
     * @param id         主键
     * @param joinTables 关联表
     * @return
     */
    default TicketVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(TICKET.ID.eq(id));

        TicketVO result = this.selectOneByQueryAs(queryWrapper, TicketVO.class);
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
    default TicketVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        TicketVO result = this.selectOneByQueryAs(finalQueryWrapper, TicketVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param ticketDomain
     * @return
     */
    default PageOrList<TicketVO> query(TicketDomain ticketDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(ticketDomain.getJoinTables(), queryWrapper);

        PageOrList<TicketVO> result = this.page(ticketDomain, finalQueryWrapper, TicketVO.class);
        this.relationMany(ticketDomain.getJoinTables(), result.getRecords());

        return result;
    }
}

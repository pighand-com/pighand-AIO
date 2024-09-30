package com.pighand.aio.mapper.ECommerce;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.SessionUserCycleDomain;
import com.pighand.aio.vo.ECommerce.SessionUserCycleVO;
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
import static com.pighand.aio.domain.ECommerce.table.SessionTableDef.SESSION;
import static com.pighand.aio.domain.ECommerce.table.SessionTemplateCycleTableDef.SESSION_TEMPLATE_CYCLE;
import static com.pighand.aio.domain.ECommerce.table.SessionUserCycleTableDef.SESSION_USER_CYCLE;
import static com.pighand.aio.domain.base.table.UserTableDef.USER;

/**
 * 电商 - 场次 - 用户周期
 *
 * @author wangshuli
 * @createDate 2024-06-05 17:35:51
 */
@Mapper
public interface SessionUserCycleMapper extends BaseMapper<SessionUserCycleDomain> {

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

        // SESSION
        if (joinTables.contains(SESSION.getTableName())) {
            queryWrapper.leftJoin(SESSION).on(SESSION.ID.eq(SESSION_USER_CYCLE.SESSION_ID));

            joinTables.remove(SESSION.getTableName());
        }

        // SESSION_TEMPLATE_CYCLE
        if (joinTables.contains(SESSION_TEMPLATE_CYCLE.getTableName())) {
            queryWrapper.leftJoin(SESSION_TEMPLATE_CYCLE)
                .on(SESSION_TEMPLATE_CYCLE.ID.eq(SESSION_USER_CYCLE.SESSION_TEMPLATE_CYCLE_ID));

            joinTables.remove(SESSION_TEMPLATE_CYCLE.getTableName());
        }

        // ORDER
        if (joinTables.contains(ORDER.getTableName())) {
            queryWrapper.leftJoin(ORDER).on(ORDER.ID.eq(SESSION_USER_CYCLE.ORDER_ID));

            joinTables.remove(ORDER.getTableName());
        }

        // USER
        if (joinTables.contains(USER.getTableName())) {
            queryWrapper.leftJoin(USER).on(USER.ID.eq(SESSION_USER_CYCLE.USER_ID));

            joinTables.remove(USER.getTableName());
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

        List<Function<SessionUserCycleVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<SessionUserCycleVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

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
            BeanUtil.queryWithRelatedData((SessionUserCycleVO)result, mainIdGetters, subTableQueriesSingle,
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
    default SessionUserCycleVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(SESSION_USER_CYCLE.ID.eq(id));

        SessionUserCycleVO result = this.selectOneByQueryAs(queryWrapper, SessionUserCycleVO.class);
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
    default SessionUserCycleVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        SessionUserCycleVO result = this.selectOneByQueryAs(finalQueryWrapper, SessionUserCycleVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param sessionUserCycleDomain
     * @return
     */
    default PageOrList<SessionUserCycleVO> query(SessionUserCycleDomain sessionUserCycleDomain,
        QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(sessionUserCycleDomain.getJoinTables(), queryWrapper);

        PageOrList<SessionUserCycleVO> result =
            this.page(sessionUserCycleDomain, finalQueryWrapper, SessionUserCycleVO.class);
        this.relationMany(sessionUserCycleDomain.getJoinTables(), result.getRecords());

        return result;
    }
}

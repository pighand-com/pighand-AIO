package com.pighand.aio.mapper.ECommerce;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.OrderSkuDomain;
import com.pighand.aio.domain.ECommerce.SessionDomain;
import com.pighand.aio.domain.ECommerce.SessionUserCycleDomain;
import com.pighand.aio.domain.ECommerce.SessionUserGroupDomain;
import com.pighand.aio.vo.ECommerce.OrderSkuVO;
import com.pighand.aio.vo.ECommerce.SessionUserCycleVO;
import com.pighand.aio.vo.ECommerce.SessionUserGroupVO;
import com.pighand.aio.vo.ECommerce.SessionVO;
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

import static com.pighand.aio.domain.ECommerce.table.OrderSkuTableDef.ORDER_SKU;
import static com.pighand.aio.domain.ECommerce.table.SessionTableDef.SESSION;
import static com.pighand.aio.domain.ECommerce.table.SessionTemplateTableDef.SESSION_TEMPLATE;
import static com.pighand.aio.domain.ECommerce.table.SessionUserCycleTableDef.SESSION_USER_CYCLE;
import static com.pighand.aio.domain.ECommerce.table.SessionUserGroupTableDef.SESSION_USER_GROUP;

/**
 * 电商 - 场次
 *
 * @author wangshuli
 * @createDate 2024-06-05 17:35:51
 */
@Mapper
public interface SessionMapper extends BaseMapper<SessionDomain> {

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

        // SESSION_TEMPLATE
        if (joinTables.contains(SESSION_TEMPLATE.getTableName())) {
            queryWrapper.leftJoin(SESSION_TEMPLATE).on(SESSION_TEMPLATE.ID.eq(SESSION.SESSION_TEMPLATE_ID));

            joinTables.remove(SESSION_TEMPLATE.getTableName());
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

        List<Function<SessionVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<SessionVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

        List<Function<Set<Long>, List>> subTableQueriesList = null;
        List<Function<Long, List>> subTableQueriesSingle = null;
        if (isList) {
            subTableQueriesList = new ArrayList<>(joinTables.size());
        } else {
            subTableQueriesSingle = new ArrayList<>(joinTables.size());
        }

        if (joinTables.contains(SESSION_USER_GROUP.getTableName())) {
            mainIdGetters.add(SessionVO::getId);

            if (subTableQueriesList != null) {
                subTableQueriesList.add(ids -> new SessionUserGroupDomain().select(SESSION_USER_GROUP.DEFAULT_COLUMNS)
                    .where(SESSION_USER_GROUP.SESSION_ID.in(ids)).listAs(SessionUserGroupVO.class));
            } else {
                subTableQueriesSingle.add(id -> new SessionUserGroupDomain().select(SESSION_USER_GROUP.DEFAULT_COLUMNS)
                    .where(SESSION_USER_GROUP.SESSION_ID.eq(id)).listAs(SessionUserGroupVO.class));
            }

            subTableIdGetter.add(obj -> ((SessionUserGroupVO)obj).getSessionId());
            subResultSetter.add((vo, list) -> vo.setSessionUserGroup((List<SessionUserGroupVO>)list));
        }
        if (joinTables.contains(SESSION_USER_CYCLE.getTableName())) {
            mainIdGetters.add(SessionVO::getId);

            if (subTableQueriesList != null) {
                subTableQueriesList.add(ids -> new SessionUserCycleDomain().select(SESSION_USER_CYCLE.DEFAULT_COLUMNS)
                    .where(SESSION_USER_CYCLE.SESSION_ID.in(ids)).listAs(SessionUserCycleVO.class));
            } else {
                subTableQueriesSingle.add(id -> new SessionUserCycleDomain().select(SESSION_USER_CYCLE.DEFAULT_COLUMNS)
                    .where(SESSION_USER_CYCLE.SESSION_ID.eq(id)).listAs(SessionUserCycleVO.class));
            }

            subTableIdGetter.add(obj -> ((SessionUserCycleVO)obj).getSessionId());
            subResultSetter.add((vo, list) -> vo.setSessionUserCycle((List<SessionUserCycleVO>)list));
        }
        if (joinTables.contains(ORDER_SKU.getTableName())) {
            mainIdGetters.add(SessionVO::getId);

            if (subTableQueriesList != null) {
                subTableQueriesList.add(
                    ids -> new OrderSkuDomain().select(ORDER_SKU.DEFAULT_COLUMNS).where(ORDER_SKU.SESSION_ID.in(ids))
                        .listAs(OrderSkuVO.class));
            } else {
                subTableQueriesSingle.add(
                    id -> new OrderSkuDomain().select(ORDER_SKU.DEFAULT_COLUMNS).where(ORDER_SKU.SESSION_ID.eq(id))
                        .listAs(OrderSkuVO.class));
            }

            subTableIdGetter.add(obj -> ((OrderSkuVO)obj).getSessionId());
            subResultSetter.add((vo, list) -> vo.setOrderSku((List<OrderSkuVO>)list));
        }

        if (result instanceof List) {
            BeanUtil.queryWithRelatedData((List)result, mainIdGetters, subTableQueriesList, subTableIdGetter,
                subResultSetter);
        } else {
            BeanUtil.queryWithRelatedData((SessionVO)result, mainIdGetters, subTableQueriesSingle, subResultSetter);
        }
    }

    /**
     * 查询详情
     *
     * @param id         主键
     * @param joinTables 关联表
     * @return
     */
    default SessionVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(SESSION.ID.eq(id));

        SessionVO result = this.selectOneByQueryAs(queryWrapper, SessionVO.class);
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
    default SessionVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        SessionVO result = this.selectOneByQueryAs(finalQueryWrapper, SessionVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param sessionDomain
     * @return
     */
    default PageOrList<SessionVO> query(SessionDomain sessionDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(sessionDomain.getJoinTables(), queryWrapper);

        PageOrList<SessionVO> result = this.page(sessionDomain, finalQueryWrapper, SessionVO.class);
        this.relationMany(sessionDomain.getJoinTables(), result.getRecords());

        return result;
    }
}

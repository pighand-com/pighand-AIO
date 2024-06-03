package com.pighand.aio.mapper.ECommerce;

import com.mybatisflex.core.field.FieldQueryBuilder;
import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.SessionUserCycleDomain;
import com.pighand.aio.vo.ECommerce.SessionUserCycleVO;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.framework.spring.page.PageOrList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.function.Consumer;

import static com.pighand.aio.domain.ECommerce.table.SessionTableDef.SESSION;
import static com.pighand.aio.domain.ECommerce.table.SessionUserCycleTableDef.SESSION_USER_CYCLE;
import static com.pighand.aio.domain.user.table.UserTableDef.USER;

/**
 * 电商 - 场次 - 用户周期
 *
 * @author wangshuli
 * @createDate 2024-05-23 15:01:58
 */
@Mapper
public interface SessionUserCycleMapper extends BaseMapper<SessionUserCycleDomain> {

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

        if (joinTables.contains(SESSION.getTableName())) {
            queryWrapper.leftJoin(SESSION).on(SESSION.ID.eq(SESSION_USER_CYCLE.SESSION_ID));
        }

        if (joinTables.contains(USER.getTableName())) {
            queryWrapper.leftJoin(USER).on(USER.ID.eq(SESSION_USER_CYCLE.USER_ID));
        }

        return queryWrapper;
    }

    /**
     * 一对多关联查询结果
     *
     * @return
     */
    default Consumer<FieldQueryBuilder<SessionUserCycleVO>>[] relationMany(List<String> joinTables) {
        if (joinTables == null) {
            return null;
        }

        int length = 0;

        Consumer<FieldQueryBuilder<SessionUserCycleVO>>[] fieldQueryBuilders = new Consumer[length];

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
    default SessionUserCycleVO find(Long id, List<String> joinTables) {
        QueryWrapper queryWrapper = this.relationOne(joinTables, null).where(SESSION_USER_CYCLE.ID.eq(id));
        Consumer<FieldQueryBuilder<SessionUserCycleVO>>[] relationManyBuilders = this.relationMany(joinTables);

        return this.selectOneByQueryAs(queryWrapper, SessionUserCycleVO.class, relationManyBuilders);
    }

    /**
     * 查询详情
     *
     * @param queryWrapper
     * @param joinTables   关联表
     * @return
     */
    default SessionUserCycleVO find(QueryWrapper queryWrapper, List<String> joinTables) {
        QueryWrapper finalQueryWrapper = this.relationOne(joinTables, queryWrapper);
        Consumer<FieldQueryBuilder<SessionUserCycleVO>>[] relationManyBuilders = this.relationMany(joinTables);

        return this.selectOneByQueryAs(finalQueryWrapper, SessionUserCycleVO.class, relationManyBuilders);
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
        Consumer<FieldQueryBuilder<SessionUserCycleVO>>[] relationManyBuilders =
            this.relationMany(sessionUserCycleDomain.getJoinTables());

        return this.page(sessionUserCycleDomain, finalQueryWrapper, SessionUserCycleVO.class, relationManyBuilders);
    }
}

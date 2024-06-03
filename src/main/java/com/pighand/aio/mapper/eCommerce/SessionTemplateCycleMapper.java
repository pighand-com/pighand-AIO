package com.pighand.aio.mapper.ECommerce;

import com.mybatisflex.core.field.FieldQueryBuilder;
import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.SessionTemplateCycleDomain;
import com.pighand.aio.vo.ECommerce.SessionTemplateCycleVO;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.framework.spring.page.PageOrList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.function.Consumer;

import static com.pighand.aio.domain.ECommerce.table.SessionTemplateCycleTableDef.SESSION_TEMPLATE_CYCLE;

/**
 * 电商 - 场次模板 - 按周期
 *
 * @author wangshuli
 * @createDate 2024-05-23 15:01:58
 */
@Mapper
public interface SessionTemplateCycleMapper extends BaseMapper<SessionTemplateCycleDomain> {

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
    default Consumer<FieldQueryBuilder<SessionTemplateCycleVO>>[] relationMany(List<String> joinTables) {
        if (joinTables == null) {
            return null;
        }

        int length = 0;

        Consumer<FieldQueryBuilder<SessionTemplateCycleVO>>[] fieldQueryBuilders = new Consumer[length];

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
    default SessionTemplateCycleVO find(Long id, List<String> joinTables) {
        QueryWrapper queryWrapper = this.relationOne(joinTables, null).where(SESSION_TEMPLATE_CYCLE.ID.eq(id));
        Consumer<FieldQueryBuilder<SessionTemplateCycleVO>>[] relationManyBuilders = this.relationMany(joinTables);

        return this.selectOneByQueryAs(queryWrapper, SessionTemplateCycleVO.class, relationManyBuilders);
    }

    /**
     * 查询详情
     *
     * @param queryWrapper
     * @param joinTables   关联表
     * @return
     */
    default SessionTemplateCycleVO find(QueryWrapper queryWrapper, List<String> joinTables) {
        QueryWrapper finalQueryWrapper = this.relationOne(joinTables, queryWrapper);
        Consumer<FieldQueryBuilder<SessionTemplateCycleVO>>[] relationManyBuilders = this.relationMany(joinTables);

        return this.selectOneByQueryAs(finalQueryWrapper, SessionTemplateCycleVO.class, relationManyBuilders);
    }

    /**
     * 分页或列表
     *
     * @param sessionTemplateCycleDomain
     * @return
     */
    default PageOrList<SessionTemplateCycleVO> query(SessionTemplateCycleDomain sessionTemplateCycleDomain,
        QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(sessionTemplateCycleDomain.getJoinTables(), queryWrapper);
        Consumer<FieldQueryBuilder<SessionTemplateCycleVO>>[] relationManyBuilders =
            this.relationMany(sessionTemplateCycleDomain.getJoinTables());

        return this.page(sessionTemplateCycleDomain, finalQueryWrapper, SessionTemplateCycleVO.class,
            relationManyBuilders);
    }
}

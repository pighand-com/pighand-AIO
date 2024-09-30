package com.pighand.aio.mapper.ECommerce;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.SessionTemplateCycleDomain;
import com.pighand.aio.vo.ECommerce.SessionTemplateCycleVO;
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

import static com.pighand.aio.domain.ECommerce.table.SessionTemplateCycleTableDef.SESSION_TEMPLATE_CYCLE;
import static com.pighand.aio.domain.ECommerce.table.SessionTemplateTableDef.SESSION_TEMPLATE;

/**
 * 电商 - 场次模板 - 按周期
 *
 * @author wangshuli
 * @createDate 2024-06-05 17:35:51
 */
@Mapper
public interface SessionTemplateCycleMapper extends BaseMapper<SessionTemplateCycleDomain> {

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
            queryWrapper.leftJoin(SESSION_TEMPLATE)
                .on(SESSION_TEMPLATE.ID.eq(SESSION_TEMPLATE_CYCLE.SESSION_TEMPLATE_ID));

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

        List<Function<SessionTemplateCycleVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<SessionTemplateCycleVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

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
            BeanUtil.queryWithRelatedData((SessionTemplateCycleVO)result, mainIdGetters, subTableQueriesSingle,
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
    default SessionTemplateCycleVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(SESSION_TEMPLATE_CYCLE.ID.eq(id));

        SessionTemplateCycleVO result = this.selectOneByQueryAs(queryWrapper, SessionTemplateCycleVO.class);
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
    default SessionTemplateCycleVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        SessionTemplateCycleVO result = this.selectOneByQueryAs(finalQueryWrapper, SessionTemplateCycleVO.class);
        this.relationMany(joinTableSet, result);

        return result;
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

        PageOrList<SessionTemplateCycleVO> result =
            this.page(sessionTemplateCycleDomain, finalQueryWrapper, SessionTemplateCycleVO.class);
        this.relationMany(sessionTemplateCycleDomain.getJoinTables(), result.getRecords());

        return result;
    }
}

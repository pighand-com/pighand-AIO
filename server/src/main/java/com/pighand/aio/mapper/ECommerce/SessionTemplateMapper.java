package com.pighand.aio.mapper.ECommerce;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.SessionTemplateDomain;
import com.pighand.aio.vo.ECommerce.SessionTemplateVO;
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

import static com.pighand.aio.domain.ECommerce.table.SessionTableDef.SESSION;
import static com.pighand.aio.domain.ECommerce.table.SessionTemplateGourpTableDef.SESSION_TEMPLATE_GOURP;
import static com.pighand.aio.domain.ECommerce.table.SessionTemplateTableDef.SESSION_TEMPLATE;

/**
 * 电商 - 场次模板。根据模板生成场次
 *
 * @author wangshuli
 * @createDate 2024-06-05 17:35:51
 */
@Mapper
public interface SessionTemplateMapper extends BaseMapper<SessionTemplateDomain> {

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
            queryWrapper.leftJoin(SESSION).on(SESSION.SESSION_TEMPLATE_ID.eq(SESSION_TEMPLATE.ID));

            joinTables.remove(SESSION.getTableName());
        }

        // SESSION_TEMPLATE_GOURP
        if (joinTables.contains(SESSION_TEMPLATE_GOURP.getTableName())) {
            queryWrapper.leftJoin(SESSION_TEMPLATE_GOURP)
                .on(SESSION_TEMPLATE_GOURP.SESSION_TEMPLATE_ID.eq(SESSION_TEMPLATE.ID));

            joinTables.remove(SESSION_TEMPLATE_GOURP.getTableName());
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

        List<Function<SessionTemplateVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<SessionTemplateVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

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
            BeanUtil.queryWithRelatedData((SessionTemplateVO)result, mainIdGetters, subTableQueriesSingle,
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
    default SessionTemplateVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(SESSION_TEMPLATE.ID.eq(id));

        SessionTemplateVO result = this.selectOneByQueryAs(queryWrapper, SessionTemplateVO.class);
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
    default SessionTemplateVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        SessionTemplateVO result = this.selectOneByQueryAs(finalQueryWrapper, SessionTemplateVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param sessionTemplateDomain
     * @return
     */
    default PageOrList<SessionTemplateVO> query(SessionTemplateDomain sessionTemplateDomain,
        QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(sessionTemplateDomain.getJoinTables(), queryWrapper);

        PageOrList<SessionTemplateVO> result =
            this.page(sessionTemplateDomain, finalQueryWrapper, SessionTemplateVO.class);
        this.relationMany(sessionTemplateDomain.getJoinTables(), result.getRecords());

        return result;
    }
}

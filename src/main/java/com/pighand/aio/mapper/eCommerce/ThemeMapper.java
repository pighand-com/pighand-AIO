package com.pighand.aio.mapper.ECommerce;

import com.mybatisflex.core.field.FieldQueryBuilder;
import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.SessionDomain;
import com.pighand.aio.domain.ECommerce.SessionTemplateDomain;
import com.pighand.aio.domain.ECommerce.ThemeDomain;
import com.pighand.aio.vo.ECommerce.SessionTemplateVO;
import com.pighand.aio.vo.ECommerce.SessionVO;
import com.pighand.aio.vo.ECommerce.ThemeVO;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.framework.spring.page.PageOrList;
import org.apache.ibatis.annotations.Mapper;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.pighand.aio.domain.ECommerce.table.SessionTableDef.SESSION;
import static com.pighand.aio.domain.ECommerce.table.SessionTemplateTableDef.SESSION_TEMPLATE;
import static com.pighand.aio.domain.ECommerce.table.ThemeTableDef.THEME;

/**
 * 电商 - 主题
 *
 * @author wangshuli
 * @createDate 2024-05-23 15:01:58
 */
@Mapper
public interface ThemeMapper extends BaseMapper<ThemeDomain> {

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

        //        if (joinTables.contains(SESSION.getTableName())) {
        //            queryWrapper.leftJoin(SESSION).on(SESSION.THEME_ID.eq(THEME.ID));
        //        }
        //
        //        if (joinTables.contains(SESSION_TEMPLATE.getTableName())) {
        //            queryWrapper.leftJoin(SESSION_TEMPLATE).on(SESSION_TEMPLATE.THEME_ID.eq(THEME.ID));
        //        }

        return queryWrapper;
    }

    /**
     * 一对多关联查询结果
     *
     * @return
     */
    default Consumer<FieldQueryBuilder<ThemeVO>>[] relationMany(List<String> joinTables) {
        if (joinTables == null) {
            return null;
        }

        int length = 0;

        Consumer<FieldQueryBuilder<ThemeVO>>[] fieldQueryBuilders = new Consumer[length];

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
    default ThemeVO find(Long id, List<String> joinTables) {
        QueryWrapper queryWrapper = this.relationOne(joinTables, null).where(THEME.ID.eq(id));
        Consumer<FieldQueryBuilder<ThemeVO>>[] relationManyBuilders = this.relationMany(joinTables);

        return this.selectOneByQueryAs(queryWrapper, ThemeVO.class, relationManyBuilders);
    }

    /**
     * 查询详情
     *
     * @param queryWrapper
     * @param joinTables   关联表
     * @return
     */
    default ThemeVO find(QueryWrapper queryWrapper, List<String> joinTables) {
        QueryWrapper finalQueryWrapper = this.relationOne(joinTables, queryWrapper);
        Consumer<FieldQueryBuilder<ThemeVO>>[] relationManyBuilders = this.relationMany(joinTables);

        return this.selectOneByQueryAs(finalQueryWrapper, ThemeVO.class, relationManyBuilders);
    }

    /**
     * 分页或列表
     *
     * @param themeDomain
     * @return
     */
    default PageOrList<ThemeVO> query(ThemeDomain themeDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(themeDomain.getJoinTables(), queryWrapper);
        Consumer<FieldQueryBuilder<ThemeVO>>[] relationManyBuilders = this.relationMany(themeDomain.getJoinTables());

        PageOrList<ThemeVO> result = this.page(themeDomain, finalQueryWrapper, ThemeVO.class, relationManyBuilders);

        Function<Set<Long>, List<SessionVO>> tableSession = ids -> {
            // 模拟数据库查询
            return new SessionDomain().select(SESSION.DEFAULT_COLUMNS).where(SESSION.THEME_ID.in(ids))
                .listAs(SessionVO.class);
        };

        Function<Set<Long>, List<SessionTemplateVO>> tableSessionTemplate = ids -> {
            // 模拟数据库查询
            return new SessionTemplateDomain().select(SESSION_TEMPLATE.DEFAULT_COLUMNS)
                .where(SESSION_TEMPLATE.THEME_ID.in(ids)).listAs(SessionTemplateVO.class);
        };

        this.queryWithRelatedData(result.getRecords(), Arrays.asList(ThemeVO::getId, ThemeVO::getId),
            Arrays.asList(tableSession, tableSessionTemplate),
            Arrays.asList(obj -> ((SessionVO)obj).getThemeId(), obj -> ((SessionTemplateVO)obj).getThemeId()),
            Arrays.asList((vo, list) -> {
                vo.setSession(list);
            }, (vo, list) -> {
                vo.setSessionTemplate(list);
            }));
        return result;
    }
}

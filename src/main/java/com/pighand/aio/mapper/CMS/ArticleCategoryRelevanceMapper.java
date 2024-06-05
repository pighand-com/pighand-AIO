package com.pighand.aio.mapper.CMS;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.CMS.ArticleCategoryRelevanceDomain;
import com.pighand.aio.vo.CMS.ArticleCategoryRelevanceVO;
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

import static com.pighand.aio.domain.CMS.table.ArticleCategoryRelevanceTableDef.ARTICLE_CATEGORY_RELEVANCE;
import static com.pighand.aio.domain.CMS.table.ArticleTableDef.ARTICLE;

/**
 * CMS - 文章、分类关联表
 *
 * @author wangshuli
 * @createDate 2024-06-05 17:35:51
 */
@Mapper
public interface ArticleCategoryRelevanceMapper extends BaseMapper<ArticleCategoryRelevanceDomain> {

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

        // ARTICLE
        if (joinTables.contains(ARTICLE.getTableName())) {
            queryWrapper.leftJoin(ARTICLE).on(ARTICLE.ID.eq(ARTICLE_CATEGORY_RELEVANCE.ARTICLE_ID));

            joinTables.remove(ARTICLE.getTableName());
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

        List<Function<ArticleCategoryRelevanceVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<ArticleCategoryRelevanceVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

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
            BeanUtil.queryWithRelatedData((ArticleCategoryRelevanceVO)result, mainIdGetters, subTableQueriesSingle,
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
    default ArticleCategoryRelevanceVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(ARTICLE_CATEGORY_RELEVANCE.ID.eq(id));

        ArticleCategoryRelevanceVO result = this.selectOneByQueryAs(queryWrapper, ArticleCategoryRelevanceVO.class);
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
    default ArticleCategoryRelevanceVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        ArticleCategoryRelevanceVO result =
            this.selectOneByQueryAs(finalQueryWrapper, ArticleCategoryRelevanceVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param articleCategoryRelevanceDomain
     * @return
     */
    default PageOrList<ArticleCategoryRelevanceVO> query(ArticleCategoryRelevanceDomain articleCategoryRelevanceDomain,
        QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(articleCategoryRelevanceDomain.getJoinTables(), queryWrapper);

        PageOrList<ArticleCategoryRelevanceVO> result =
            this.page(articleCategoryRelevanceDomain, finalQueryWrapper, ArticleCategoryRelevanceVO.class);
        this.relationMany(articleCategoryRelevanceDomain.getJoinTables(), result.getRecords());

        return result;
    }
}

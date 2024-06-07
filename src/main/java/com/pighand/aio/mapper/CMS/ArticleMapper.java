package com.pighand.aio.mapper.CMS;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.CMS.ArticleCategoryRelevanceDomain;
import com.pighand.aio.domain.CMS.ArticleDomain;
import com.pighand.aio.vo.CMS.ArticleCategoryRelevanceVO;
import com.pighand.aio.vo.CMS.ArticleVO;
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
import static com.pighand.aio.domain.CMS.table.ArticleCategoryTableDef.ARTICLE_CATEGORY;
import static com.pighand.aio.domain.CMS.table.ArticleTableDef.ARTICLE;
import static com.pighand.aio.domain.base.table.UserTableDef.USER;

/**
 * CMS - 文章
 *
 * @author wangshuli
 * @createDate 2024-06-05 17:35:51
 */
@Mapper
public interface ArticleMapper extends BaseMapper<ArticleDomain> {

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

        // USER
        if (joinTables.contains(USER.getTableName())) {
            queryWrapper.leftJoin(USER).on(USER.ID.eq(ARTICLE.CREATOR_ID));

            joinTables.remove(USER.getTableName());
        }

        // ARTICLE_CATEGORY
        if (joinTables.contains(ARTICLE_CATEGORY.getTableName())) {
            queryWrapper.leftJoin(ARTICLE_CATEGORY).on(ARTICLE_CATEGORY.ID.eq(ARTICLE.CATEGORY_ID));

            joinTables.remove(ARTICLE_CATEGORY.getTableName());
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

        List<Function<ArticleVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<ArticleVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

        List<Function<Set<Long>, List>> subTableQueriesList = null;
        List<Function<Long, List>> subTableQueriesSingle = null;
        if (isList) {
            subTableQueriesList = new ArrayList<>(joinTables.size());
        } else {
            subTableQueriesSingle = new ArrayList<>(joinTables.size());
        }

        // ARTICLE_CATEGORY_RELEVANCE
        if (joinTables.contains(ARTICLE_CATEGORY_RELEVANCE.getTableName())) {
            mainIdGetters.add(ArticleVO::getId);

            if (subTableQueriesList != null) {
                subTableQueriesList.add(
                    ids -> new ArticleCategoryRelevanceDomain().select(ARTICLE_CATEGORY_RELEVANCE.DEFAULT_COLUMNS)
                        .where(ARTICLE_CATEGORY_RELEVANCE.ARTICLE_ID.in(ids)).listAs(ArticleCategoryRelevanceVO.class));
            } else {
                subTableQueriesSingle.add(
                    id -> new ArticleCategoryRelevanceDomain().select(ARTICLE_CATEGORY_RELEVANCE.DEFAULT_COLUMNS)
                        .where(ARTICLE_CATEGORY_RELEVANCE.ARTICLE_ID.eq(id)).listAs(ArticleCategoryRelevanceVO.class));
            }

            subTableIdGetter.add(obj -> ((ArticleCategoryRelevanceVO)obj).getArticleId());
            subResultSetter.add((vo, list) -> vo.setArticleCategoryRelevance((List<ArticleCategoryRelevanceVO>)list));
        }

        if (result instanceof List) {
            BeanUtil.queryWithRelatedData((List)result, mainIdGetters, subTableQueriesList, subTableIdGetter,
                subResultSetter);
        } else {
            BeanUtil.queryWithRelatedData((ArticleVO)result, mainIdGetters, subTableQueriesSingle, subResultSetter);
        }
    }

    /**
     * 查询详情
     *
     * @param id         主键
     * @param joinTables 关联表
     * @return
     */
    default ArticleVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(ARTICLE.ID.eq(id));

        ArticleVO result = this.selectOneByQueryAs(queryWrapper, ArticleVO.class);
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
    default ArticleVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        ArticleVO result = this.selectOneByQueryAs(finalQueryWrapper, ArticleVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param articleDomain
     * @return
     */
    default PageOrList<ArticleVO> query(ArticleDomain articleDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(articleDomain.getJoinTables(), queryWrapper);

        PageOrList<ArticleVO> result = this.page(articleDomain, finalQueryWrapper, ArticleVO.class);
        this.relationMany(articleDomain.getJoinTables(), result.getRecords());

        return result;
    }
}

package com.pighand.aio.mapper.CMS;

import com.mybatisflex.core.field.FieldQueryBuilder;
import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.CMS.ArticleDomain;
import com.pighand.aio.vo.CMS.ArticleVO;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.framework.spring.page.PageOrList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.function.Consumer;

import static com.pighand.aio.domain.CMS.table.ArticleCategoryRelevanceTableDef.ARTICLE_CATEGORY_RELEVANCE;
import static com.pighand.aio.domain.CMS.table.ArticleTableDef.ARTICLE;

/**
 * CMS - 文章
 *
 * @author wangshuli
 * @createDate 2024-04-22 15:11:06
 */
@Mapper
public interface ArticleMapper extends BaseMapper<ArticleDomain> {

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

        if (joinTables.contains(ARTICLE_CATEGORY_RELEVANCE.getTableName())) {
            queryWrapper.leftJoin(ARTICLE_CATEGORY_RELEVANCE).on(ARTICLE_CATEGORY_RELEVANCE.ARTICLE_ID.eq(ARTICLE.ID));
        }

        return queryWrapper;
    }

    /**
     * 一对多关联查询结果
     *
     * @return
     */
    default Consumer<FieldQueryBuilder<ArticleVO>>[] relationMany(List<String> joinTables) {
        if (joinTables == null) {
            return null;
        }

        boolean hasRelevance = joinTables.contains(ARTICLE_CATEGORY_RELEVANCE.getTableName());

        int length = 0;

        if (hasRelevance) {
            length++;
        }

        Consumer<FieldQueryBuilder<ArticleVO>>[] fieldQueryBuilders = new Consumer[length];

        int nowIndex = 0;
        if (hasRelevance) {
            Consumer<FieldQueryBuilder<ArticleVO>> consumer = (builder) -> {
                builder.field(ArticleVO::getArticleCategoryRelevance).queryWrapper(
                    ArticleVO -> QueryWrapper.create().from(ARTICLE_CATEGORY_RELEVANCE)
                        .where(ARTICLE_CATEGORY_RELEVANCE.ARTICLE_ID.eq(ArticleVO.getId())));
            };
            fieldQueryBuilders[nowIndex] = consumer;
            nowIndex++;
        }

        return fieldQueryBuilders;
    }

    /**
     * 查询详情
     *
     * @param id         主键
     * @param joinTables 关联表
     * @return
     */
    default ArticleVO find(Long id, List<String> joinTables) {
        QueryWrapper queryWrapper = this.relationOne(joinTables, null).where(ARTICLE.ID.eq(id));
        Consumer<FieldQueryBuilder<ArticleVO>>[] relationManyBuilders = this.relationMany(joinTables);

        return this.selectOneByQueryAs(queryWrapper, ArticleVO.class, relationManyBuilders);
    }

    /**
     * 查询详情
     *
     * @param queryWrapper
     * @param joinTables   关联表
     * @return
     */
    default ArticleVO find(QueryWrapper queryWrapper, List<String> joinTables) {
        QueryWrapper finalQueryWrapper = this.relationOne(joinTables, queryWrapper);
        Consumer<FieldQueryBuilder<ArticleVO>>[] relationManyBuilders = this.relationMany(joinTables);

        return this.selectOneByQueryAs(finalQueryWrapper, ArticleVO.class, relationManyBuilders);
    }

    /**
     * 分页或列表
     *
     * @param articleDomain
     * @return
     */
    default PageOrList<ArticleVO> query(ArticleDomain articleDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(articleDomain.getJoinTables(), queryWrapper);
        Consumer<FieldQueryBuilder<ArticleVO>>[] relationManyBuilders =
            this.relationMany(articleDomain.getJoinTables());

        return this.page(articleDomain, finalQueryWrapper, ArticleVO.class, relationManyBuilders);
    }
}

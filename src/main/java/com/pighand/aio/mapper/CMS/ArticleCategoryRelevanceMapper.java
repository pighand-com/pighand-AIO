package com.pighand.aio.mapper.CMS;

import com.mybatisflex.core.field.FieldQueryBuilder;
import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.CMS.ArticleCategoryRelevanceDomain;
import com.pighand.aio.vo.CMS.ArticleCategoryRelevanceVO;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.framework.spring.page.PageOrList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.function.Consumer;

import static com.pighand.aio.domain.CMS.table.ArticleCategoryRelevanceTableDef.ARTICLE_CATEGORY_RELEVANCE;

/**
 * CMS - 文章、分类关联表
 *
 * @author wangshuli
 * @createDate 2024-04-22 16:17:05
 */
@Mapper
public interface ArticleCategoryRelevanceMapper extends BaseMapper<ArticleCategoryRelevanceDomain> {

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
    default Consumer<FieldQueryBuilder<ArticleCategoryRelevanceVO>>[] relationMany(List<String> joinTables) {
        if (joinTables == null) {
            return null;
        }

        int length = 0;

        Consumer<FieldQueryBuilder<ArticleCategoryRelevanceVO>>[] fieldQueryBuilders = new Consumer[length];

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
    default ArticleCategoryRelevanceVO find(Long id, List<String> joinTables) {
        QueryWrapper queryWrapper = this.relationOne(joinTables, null).where(ARTICLE_CATEGORY_RELEVANCE.ID.eq(id));
        Consumer<FieldQueryBuilder<ArticleCategoryRelevanceVO>>[] relationManyBuilders = this.relationMany(joinTables);

        return this.selectOneByQueryAs(queryWrapper, ArticleCategoryRelevanceVO.class, relationManyBuilders);
    }

    /**
     * 查询详情
     *
     * @param queryWrapper
     * @param joinTables   关联表
     * @return
     */
    default ArticleCategoryRelevanceVO find(QueryWrapper queryWrapper, List<String> joinTables) {
        QueryWrapper finalQueryWrapper = this.relationOne(joinTables, queryWrapper);
        Consumer<FieldQueryBuilder<ArticleCategoryRelevanceVO>>[] relationManyBuilders = this.relationMany(joinTables);

        return this.selectOneByQueryAs(finalQueryWrapper, ArticleCategoryRelevanceVO.class, relationManyBuilders);
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
        Consumer<FieldQueryBuilder<ArticleCategoryRelevanceVO>>[] relationManyBuilders =
            this.relationMany(articleCategoryRelevanceDomain.getJoinTables());

        return this.page(articleCategoryRelevanceDomain, finalQueryWrapper, ArticleCategoryRelevanceVO.class,
            relationManyBuilders);
    }
}

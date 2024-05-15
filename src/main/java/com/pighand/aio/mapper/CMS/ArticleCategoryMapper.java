package com.pighand.aio.mapper.CMS;

import com.mybatisflex.core.field.FieldQueryBuilder;
import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.CMS.ArticleCategoryDomain;
import com.pighand.aio.vo.CMS.ArticleCategoryVO;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.framework.spring.page.PageOrList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.function.Consumer;

import static com.pighand.aio.domain.CMS.table.ArticleCategoryTableDef.ARTICLE_CATEGORY;
import static com.pighand.aio.domain.CMS.table.ArticleTableDef.ARTICLE;

/**
 * CMS - 文章分类
 *
 * @author wangshuli
 * @createDate 2024-04-22 15:11:06
 */
@Mapper
public interface ArticleCategoryMapper extends BaseMapper<ArticleCategoryDomain> {

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
    default Consumer<FieldQueryBuilder<ArticleCategoryVO>>[] relationMany(List<String> joinTables) {
        if (joinTables == null) {
            return null;
        }

        boolean hasArticle = joinTables.contains(ARTICLE.getTableName());

        int length = 0;

        if (hasArticle) {
            length++;
        }

        Consumer<FieldQueryBuilder<ArticleCategoryVO>>[] fieldQueryBuilders = new Consumer[length];

        int nowIndex = 0;
        if (hasArticle) {
            //            Consumer<FieldQueryBuilder<ArticleCategoryVO>> consumer = (builder) -> {
            //                builder.field(ArticleCategoryVO::getArticle).queryWrapper(
            //                    articleCategory -> QueryWrapper.create().from(ARTICLE)
            //                        .where(ARTICLE.CATEGORY_ID.eq(articleCategory.getId())));
            //            };
            //            fieldQueryBuilders[nowIndex] = consumer;
            //            nowIndex++;
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
    default ArticleCategoryVO find(Long id, List<String> joinTables) {
        QueryWrapper queryWrapper = this.relationOne(joinTables, null).where(ARTICLE_CATEGORY.ID.eq(id));
        Consumer<FieldQueryBuilder<ArticleCategoryVO>>[] relationManyBuilders = this.relationMany(joinTables);

        return this.selectOneByQueryAs(queryWrapper, ArticleCategoryVO.class, relationManyBuilders);
    }

    /**
     * 查询详情
     *
     * @param queryWrapper
     * @param joinTables   关联表
     * @return
     */
    default ArticleCategoryVO find(QueryWrapper queryWrapper, List<String> joinTables) {
        QueryWrapper finalQueryWrapper = this.relationOne(joinTables, queryWrapper);
        Consumer<FieldQueryBuilder<ArticleCategoryVO>>[] relationManyBuilders = this.relationMany(joinTables);

        return this.selectOneByQueryAs(finalQueryWrapper, ArticleCategoryVO.class, relationManyBuilders);
    }

    /**
     * 分页或列表
     *
     * @param articleCategoryDomain
     * @return
     */
    default PageOrList<ArticleCategoryVO> query(ArticleCategoryDomain articleCategoryDomain,
        QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(articleCategoryDomain.getJoinTables(), queryWrapper);
        Consumer<FieldQueryBuilder<ArticleCategoryVO>>[] relationManyBuilders =
            this.relationMany(articleCategoryDomain.getJoinTables());

        return this.page(articleCategoryDomain, finalQueryWrapper, ArticleCategoryVO.class, relationManyBuilders);
    }
}

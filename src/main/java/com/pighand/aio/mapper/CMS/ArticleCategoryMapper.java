package com.pighand.aio.mapper.CMS;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.CMS.ArticleCategoryDomain;
import com.pighand.aio.domain.CMS.ArticleDomain;
import com.pighand.aio.vo.CMS.ArticleCategoryVO;
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

import static com.pighand.aio.domain.CMS.table.ArticleCategoryTableDef.ARTICLE_CATEGORY;
import static com.pighand.aio.domain.CMS.table.ArticleTableDef.ARTICLE;

/**
 * CMS - 文章分类
 *
 * @author wangshuli
 * @createDate 2024-06-05 17:35:51
 */
@Mapper
public interface ArticleCategoryMapper extends BaseMapper<ArticleCategoryDomain> {

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

        List<Function<ArticleCategoryVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<ArticleCategoryVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

        List<Function<Set<Long>, List>> subTableQueriesList = null;
        List<Function<Long, List>> subTableQueriesSingle = null;
        if (isList) {
            subTableQueriesList = new ArrayList<>(joinTables.size());
        } else {
            subTableQueriesSingle = new ArrayList<>(joinTables.size());
        }

        // ARTICLE
        if (joinTables.contains(ARTICLE.getTableName())) {
            mainIdGetters.add(ArticleCategoryVO::getId);

            if (subTableQueriesList != null) {
                subTableQueriesList.add(
                    ids -> new ArticleDomain().select(ARTICLE.DEFAULT_COLUMNS).where(ARTICLE.CATEGORY_ID.in(ids))
                        .listAs(ArticleVO.class));
            } else {
                subTableQueriesSingle.add(
                    id -> new ArticleDomain().select(ARTICLE.DEFAULT_COLUMNS).where(ARTICLE.CATEGORY_ID.eq(id))
                        .listAs(ArticleVO.class));
            }

            subTableIdGetter.add(obj -> ((ArticleVO)obj).getCategoryId());
            subResultSetter.add((vo, list) -> vo.setArticle((List<ArticleVO>)list));
        }

        if (result instanceof List) {
            BeanUtil.queryWithRelatedData((List)result, mainIdGetters, subTableQueriesList, subTableIdGetter,
                subResultSetter);
        } else {
            BeanUtil.queryWithRelatedData((ArticleCategoryVO)result, mainIdGetters, subTableQueriesSingle,
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
    default ArticleCategoryVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(ARTICLE_CATEGORY.ID.eq(id));

        ArticleCategoryVO result = this.selectOneByQueryAs(queryWrapper, ArticleCategoryVO.class);
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
    default ArticleCategoryVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        ArticleCategoryVO result = this.selectOneByQueryAs(finalQueryWrapper, ArticleCategoryVO.class);
        this.relationMany(joinTableSet, result);

        return result;
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

        PageOrList<ArticleCategoryVO> result =
            this.page(articleCategoryDomain, finalQueryWrapper, ArticleCategoryVO.class);
        this.relationMany(articleCategoryDomain.getJoinTables(), result.getRecords());

        return result;
    }
}

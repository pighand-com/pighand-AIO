package com.pighand.aio.service.CMS.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.CMS.ArticleCategoryRelevanceDomain;
import com.pighand.aio.mapper.CMS.ArticleCategoryRelevanceMapper;
import com.pighand.aio.service.CMS.ArticleCategoryRelevanceService;
import com.pighand.aio.vo.CMS.ArticleCategoryRelevanceVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import org.springframework.stereotype.Service;

import static com.pighand.aio.domain.CMS.table.ArticleCategoryRelevanceTableDef.ARTICLE_CATEGORY_RELEVANCE;

/**
 * CMS - 文章、分类关联表
 *
 * @author wangshuli
 * @createDate 2024-04-22 16:17:05
 */
@Service
public class ArticleCategoryRelevanceServiceImpl
    extends BaseServiceImpl<ArticleCategoryRelevanceMapper, ArticleCategoryRelevanceDomain>
    implements ArticleCategoryRelevanceService {

    /**
     * 创建
     *
     * @param articleCategoryRelevanceVO
     * @return
     */
    @Override
    public ArticleCategoryRelevanceVO create(ArticleCategoryRelevanceVO articleCategoryRelevanceVO) {
        super.mapper.insert(articleCategoryRelevanceVO);

        return articleCategoryRelevanceVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public ArticleCategoryRelevanceDomain find(Long id) {
        return super.mapper.find(id, null);
    }

    /**
     * 分页或列表
     *
     * @param articleCategoryRelevanceVO
     * @return PageOrList<ArticleCategoryRelevanceVO>
     */
    @Override
    public PageOrList<ArticleCategoryRelevanceVO> query(ArticleCategoryRelevanceVO articleCategoryRelevanceVO) {

        QueryWrapper queryWrapper = QueryWrapper.create()
            // like
            .and(ARTICLE_CATEGORY_RELEVANCE.ARTICLE_CATEGORY_PATH.like(
                articleCategoryRelevanceVO.getArticleCategoryPath()))

            // equal
            .and(ARTICLE_CATEGORY_RELEVANCE.ARTICLE_ID.eq(articleCategoryRelevanceVO.getArticleId()));

        return super.mapper.query(articleCategoryRelevanceVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param articleCategoryRelevanceVO
     */
    @Override
    public void update(ArticleCategoryRelevanceVO articleCategoryRelevanceVO) {
        super.mapper.update(articleCategoryRelevanceVO);
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        super.mapper.deleteById(id);
    }
}

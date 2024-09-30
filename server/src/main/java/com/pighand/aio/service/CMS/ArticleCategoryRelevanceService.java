package com.pighand.aio.service.CMS;

import com.pighand.aio.domain.CMS.ArticleCategoryRelevanceDomain;
import com.pighand.aio.vo.CMS.ArticleCategoryRelevanceVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;

/**
 * CMS - 文章、分类关联表
 *
 * @author wangshuli
 * @createDate 2024-04-22 16:17:05
 */
public interface ArticleCategoryRelevanceService extends BaseService<ArticleCategoryRelevanceDomain> {

    /**
     * 创建
     *
     * @param articleCategoryRelevanceVO
     * @return
     */
    ArticleCategoryRelevanceVO create(ArticleCategoryRelevanceVO articleCategoryRelevanceVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    ArticleCategoryRelevanceDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param articleCategoryRelevanceVO
     * @return PageOrList<ArticleCategoryRelevanceVO>
     */
    PageOrList<ArticleCategoryRelevanceVO> query(ArticleCategoryRelevanceVO articleCategoryRelevanceVO);

    /**
     * 修改
     *
     * @param articleCategoryRelevanceVO
     */
    void update(ArticleCategoryRelevanceVO articleCategoryRelevanceVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}

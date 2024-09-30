package com.pighand.aio.service.CMS;

import com.pighand.aio.domain.CMS.ArticleCategoryDomain;
import com.pighand.aio.vo.CMS.ArticleCategoryVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;

import java.util.List;

/**
 * CMS - 文章分类
 *
 * @author wangshuli
 * @createDate 2024-04-22 15:11:06
 */
public interface ArticleCategoryService extends BaseService<ArticleCategoryDomain> {

    /**
     * 创建
     *
     * @param articleCategoryVO
     * @return
     */
    ArticleCategoryVO create(ArticleCategoryVO articleCategoryVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    ArticleCategoryDomain find(Long id);

    List<ArticleCategoryDomain> queryAll();

    /**
     * 分页或列表
     *
     * @param articleCategoryVO
     * @return PageOrList<ArticleCategoryVO>
     */
    PageOrList<ArticleCategoryVO> query(ArticleCategoryVO articleCategoryVO);

    /**
     * 修改
     *
     * @param articleCategoryVO
     */
    void update(ArticleCategoryVO articleCategoryVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}

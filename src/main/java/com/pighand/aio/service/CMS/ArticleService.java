package com.pighand.aio.service.CMS;

import com.pighand.aio.domain.CMS.ArticleDomain;
import com.pighand.aio.vo.CMS.ArticleVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;

/**
 * CMS - 文章
 *
 * @author wangshuli
 * @createDate 2024-04-22 15:11:06
 */
public interface ArticleService extends BaseService<ArticleDomain> {

    /**
     * 创建
     *
     * @param articleVO
     * @return
     */
    ArticleVO create(ArticleVO articleVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    ArticleVO find(Long id);

    void statistics(Long id, String type);

    /**
     * 分页或列表
     *
     * @param articleVO
     * @return PageOrList<ArticleVO>
     */
    PageOrList<ArticleVO> query(ArticleVO articleVO);

    /**
     * 修改
     *
     * @param articleVO
     */
    void update(ArticleVO articleVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}

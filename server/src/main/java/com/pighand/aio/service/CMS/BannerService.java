package com.pighand.aio.service.CMS;

import com.pighand.aio.domain.CMS.BannerDomain;
import com.pighand.aio.vo.CMS.BannerVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;

/**
 * CMS - banner
 *
 * @author wangshuli
 * @createDate 2025-06-17 13:59:52
 */
public interface BannerService extends BaseService<BannerDomain> {

    /**
     * 创建
     *
     * @param cmsBannerVO
     * @return
     */
    BannerVO create(BannerVO cmsBannerVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    BannerDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param cmsBannerVO
     * @return PageOrList<CmsBannerVO>
     */
    PageOrList<BannerVO> query(BannerVO cmsBannerVO);

    /**
     * 修改
     *
     * @param cmsBannerVO
     */
    void update(BannerVO cmsBannerVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);

    /**
     * 上架
     *
     * @param id
     */
    void online(Long id);

    /**
     * 下架
     *
     * @param id
     */
    void offline(Long id);
}

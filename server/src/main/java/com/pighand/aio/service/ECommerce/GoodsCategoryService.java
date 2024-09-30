package com.pighand.aio.service.ECommerce;

import com.pighand.aio.domain.ECommerce.GoodsCategoryDomain;
import com.pighand.aio.vo.ECommerce.GoodsCategoryVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;

/**
 * 电商 - 商品类目
 *
 * @author wangshuli
 * @createDate 2024-01-08 11:05:18
 */
public interface GoodsCategoryService extends BaseService<GoodsCategoryDomain> {

    /**
     * 创建
     *
     * @param goodsCategoryVO
     * @return
     */
    GoodsCategoryVO create(GoodsCategoryVO goodsCategoryVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    GoodsCategoryDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param goodsCategoryVO
     * @return PageOrList<GoodsCategoryVO>
     */
    PageOrList<GoodsCategoryVO> query(GoodsCategoryVO goodsCategoryVO);

    /**
     * 修改
     *
     * @param goodsCategoryVO
     */
    void update(GoodsCategoryVO goodsCategoryVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}

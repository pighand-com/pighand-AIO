package com.pighand.aio.service.ECommerce;

import com.pighand.aio.domain.ECommerce.GoodsSkuDomain;
import com.pighand.aio.vo.ECommerce.GoodsSkuVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;

/**
 * 电商 - SKU
 *
 * @author wangshuli
 * @createDate 2024-01-07 19:55:48
 */
public interface GoodsSkuService extends BaseService<GoodsSkuDomain> {

    /**
     * 创建
     *
     * @param goodsSkuVO
     * @return
     */
    GoodsSkuVO create(GoodsSkuVO goodsSkuVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    GoodsSkuDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param goodsSkuVO
     * @return PageOrList<GoodsSkuVO>
     */
    PageOrList<GoodsSkuVO> query(GoodsSkuVO goodsSkuVO);

    /**
     * 修改
     *
     * @param goodsSkuVO
     */
    void update(GoodsSkuVO goodsSkuVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}

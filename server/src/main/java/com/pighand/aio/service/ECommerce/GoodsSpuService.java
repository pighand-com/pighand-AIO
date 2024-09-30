package com.pighand.aio.service.ECommerce;

import com.pighand.aio.domain.ECommerce.GoodsSpuDomain;
import com.pighand.aio.vo.ECommerce.GoodsSpuVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;

/**
 * 电商 - SPU
 *
 * @author wangshuli
 * @createDate 2024-01-07 19:55:48
 */
public interface GoodsSpuService extends BaseService<GoodsSpuDomain> {

    /**
     * 创建
     *
     * @param goodsSpuVO
     * @return
     */
    GoodsSpuVO create(GoodsSpuVO goodsSpuVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    GoodsSpuVO find(Long id);

    /**
     * 分页或列表
     *
     * @param goodsSpuVO
     * @return PageOrList<GoodsSpuVO>
     */
    PageOrList<GoodsSpuVO> query(GoodsSpuVO goodsSpuVO);

    /**
     * 修改
     *
     * @param goodsSpuVO
     */
    void update(GoodsSpuVO goodsSpuVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}

package com.pighand.aio.service.ECommerce;

import com.pighand.aio.domain.ECommerce.GoodsSkuDomain;
import com.pighand.aio.mapper.ECommerce.GoodsSkuMapper;
import com.pighand.aio.vo.ECommerce.GoodsSkuVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import org.springframework.stereotype.Service;

/**
 * 电商 - SKU
 *
 * @author wangshuli
 * @createDate 2024-01-07 19:55:48
 */
@Service
public class GoodsSkuService extends BaseServiceImpl<GoodsSkuMapper, GoodsSkuDomain>  {

    /**
     * 创建
     *
     * @param goodsSkuVO
     * @return
     */
    public GoodsSkuVO create(GoodsSkuVO goodsSkuVO) {
        super.mapper.insert(goodsSkuVO);

        return goodsSkuVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public GoodsSkuDomain find(Long id) {
        return super.mapper.find(id);
    }

    /**
     * 分页或列表
     *
     * @param goodsSkuVO
     * @return PageOrList<GoodsSkuVO>
     */
    public PageOrList<GoodsSkuVO> query(GoodsSkuVO goodsSkuVO) {
        return super.mapper.query(goodsSkuVO, null);
    }

    /**
     * 修改
     *
     * @param goodsSkuVO
     */
    public void update(GoodsSkuVO goodsSkuVO) {
        super.mapper.update(goodsSkuVO);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(Long id) {
        super.mapper.deleteById(id);
    }
}

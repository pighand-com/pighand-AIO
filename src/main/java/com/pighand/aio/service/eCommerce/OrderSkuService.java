package com.pighand.aio.service.ECommerce;

import com.pighand.aio.domain.ECommerce.OrderSkuDomain;
import com.pighand.aio.vo.ECommerce.OrderSkuVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;

/**
 * 电商 - 订单 - SKU
 *
 * @author wangshuli
 * @createDate 2024-04-18 14:35:34
 */
public interface OrderSkuService extends BaseService<OrderSkuDomain> {

    /**
     * 创建
     *
     * @param orderSkuVO
     * @return
     */
    OrderSkuVO create(OrderSkuVO orderSkuVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    OrderSkuDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param orderSkuVO
     * @return PageOrList<OrderSkuVO>
     */
    PageOrList<OrderSkuVO> query(OrderSkuVO orderSkuVO);

    /**
     * 修改
     *
     * @param orderSkuVO
     */
    void update(OrderSkuVO orderSkuVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}

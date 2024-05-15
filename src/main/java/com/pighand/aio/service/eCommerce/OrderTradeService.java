package com.pighand.aio.service.ECommerce;

import com.pighand.aio.domain.ECommerce.OrderTradeDomain;
import com.pighand.aio.vo.ECommerce.OrderTradeVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;

/**
 * 电商 - 订单 - 交易单
 *
 * @author wangshuli
 * @createDate 2024-04-18 14:35:34
 */
public interface OrderTradeService extends BaseService<OrderTradeDomain> {

    /**
     * 创建
     *
     * @param orderTradeVO
     * @return
     */
    OrderTradeVO create(OrderTradeVO orderTradeVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    OrderTradeDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param orderTradeVO
     * @return PageOrList<OrderTradeVO>
     */
    PageOrList<OrderTradeVO> query(OrderTradeVO orderTradeVO);

    /**
     * 修改
     *
     * @param orderTradeVO
     */
    void update(OrderTradeVO orderTradeVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}

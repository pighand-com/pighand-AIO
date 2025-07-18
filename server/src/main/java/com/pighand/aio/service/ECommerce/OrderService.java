package com.pighand.aio.service.ECommerce;

import com.pighand.aio.domain.ECommerce.OrderDomain;
import com.pighand.aio.vo.ECommerce.OrderVO;
import com.pighand.aio.vo.ECommerce.PayVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 电商 - 订单
 *
 * @author wangshuli
 * @createDate 2024-04-18 14:35:34
 */
public interface OrderService extends BaseService<OrderDomain> {

    /**
     * 下单并支付
     *
     * @param orderVO
     * @return
     */
    PayVO placeOrderAndPay(OrderVO orderVO);

    void payNotify(HttpServletRequest request);

    PageOrList<OrderVO> query(OrderVO orderVO);

    OrderVO find(Long id, Long loginUserId);

    void confirmReceipt(Long id);

    void refund(OrderVO orderVO);

    /**
     * 取消订单
     *
     * @param id
     */
    void cancelOrder(Long id);

    /**
     * 根据订单ID再次支付
     *
     * @param orderId 订单ID
     * @param outTradePlatform 支付平台
     * @return PayVO
     */
    PayVO payByOrderId(Long orderId, String outTradePlatform);
}

package com.pighand.aio.controller.ECommerce;

import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.common.interfaces.Authorization;
import com.pighand.aio.service.ECommerce.OrderService;
import com.pighand.aio.service.ECommerce.OrderTradeService;
import com.pighand.aio.vo.ECommerce.OrderVO;
import com.pighand.aio.vo.ECommerce.PayVO;
import com.pighand.framework.spring.api.annotation.Get;
import com.pighand.framework.spring.api.annotation.Post;
import com.pighand.framework.spring.api.annotation.RestController;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 电商 - 订单
 *
 * @author wangshuli
 * @createDate 2024-04-16 11:44:49
 */
@Authorization
@RestController(path = "order", docName = "电商 - 订单")
@AllArgsConstructor
public class OrderController extends BaseController<OrderService> {

    private final OrderService orderService;

    private final OrderTradeService orderTradeService;

    // @Post(path = "price", docSummary = "价格试算", fieldGroup = "orderPrice")
    // public Result<OrderVO> price(@Validated({ValidationGroup.Create.class}) @RequestBody OrderVO orderVO) {
    //     orderVO = super.service.price(orderVO);
    //
    //     return new Result(orderVO);
    // }

    /**
     * @param orderVO
     * @return
     */
    @Post(docSummary = "下单并支付")
    public Result<PayVO> placeOrderAndPay(@Validated({ValidationGroup.Create.class}) @RequestBody OrderVO orderVO) {
        PayVO payId = super.service.placeOrderAndPay(orderVO);

        return new Result(payId);
    }

    @Get(path = "mine", docSummary = "我的订单")
    public Result<OrderVO> queryMine(OrderVO orderVO) {
        orderVO.setCreatorId(Context.loginUser().getId());
        PageOrList<OrderVO> result = orderService.query(orderVO);

        return new Result(result);
    }

    @Authorization(false)
    @Post("notify/{platform}")
    public Result notifyPay() {
        super.service.payNotify(super.request);

        return new Result();
    }

    @Post(path = "confirm_receipt/{id}", docSummary = "确认收货")
    public Result confirmReceipt(@PathVariable(name = "id") Long id) {
        super.service.confirmReceipt(id);

        return new Result();
    }

    @Post(path = "refund", docSummary = "退款")
    public Result refund(@RequestBody OrderVO orderVO) {
        super.service.refund(orderVO);
        return new Result();
    }
}

package com.pighand.aio.controller.client.ECommerce;

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

import java.util.Map;

/**
 * 电商 - 订单
 *
 * @author wangshuli
 * @createDate 2024-04-16 11:44:49
 */
@Authorization
@RestController(path = "client/order", docName = "电商 - 订单")
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

    @Get(docSummary = "订单列表")
    public Result<OrderVO> queryMine(OrderVO orderVO) {
        orderVO.setCreatorId(Context.loginUser().getId());
        PageOrList<OrderVO> result = orderService.query(orderVO);

        return new Result(result);
    }

    @Get(path = "{id}", docSummary = "订单详情")
    public Result<OrderVO> find(@PathVariable(name = "id") Long id) {
        Long loginUserId = Context.loginUser().getId();
        OrderVO result = orderService.find(id, loginUserId);

        return new Result(result);
    }

    @Post(path = "{id}/confirm_receipt", docSummary = "确认收货")
    public Result confirmReceipt(@PathVariable(name = "id") Long id) {
        super.service.confirmReceipt(id);

        return new Result();
    }

    @Post(path = "refund", docSummary = "退款")
    public Result refund(@RequestBody OrderVO orderVO) {
        super.service.refund(orderVO);
        return new Result();
    }

    @Post(path = "{id}/cancel", docSummary = "取消订单")
    public Result cancelOrder(@PathVariable(name = "id") Long id) {
        super.service.cancelOrder(id);
        return new Result();
    }

    @Post(path = "{id}/pay", docSummary = "根据订单ID再次支付")
    public Result<PayVO> payByOrderId(@PathVariable(name = "id") Long id, @RequestBody Map<String, String> requestBody) {
        String outTradePlatform = requestBody.get("outTradePlatform");
        PayVO payVO = super.service.payByOrderId(id, outTradePlatform);
        return new Result(payVO);
    }
}

package com.pighand.aio.service.ECommerce.payments;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.payments.jsapi.JsapiService;
import com.wechat.pay.java.service.payments.jsapi.model.Amount;
import com.wechat.pay.java.service.payments.jsapi.model.Payer;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayResponse;
import com.wechat.pay.java.service.refund.RefundService;
import com.wechat.pay.java.service.refund.model.AmountReq;
import com.wechat.pay.java.service.refund.model.CreateRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * 支付 - 微信
 */
@Component
public class Wechat {

    @Value("${pay.notify-url}")
    private String notifyUrl;

    public void refund(String merchantId, String privateKeyPath, String merchantSerialNumber, String apiV3Key,
        Long total, Long refund, String orderTradeSn, String refundSn) {
        Config config = getConfig(merchantId, privateKeyPath, merchantSerialNumber, apiV3Key);

        //构造申请退款对象
        RefundService service = new RefundService.Builder().config(config).build();
        //请求参数
        CreateRequest request = new CreateRequest();
        //设置退款金额 根据自己的实际业务自行填写
        AmountReq amountReq = new AmountReq();
        amountReq.setRefund(refund);
        amountReq.setTotal(total);
        amountReq.setCurrency("CNY");
        request.setAmount(amountReq);
        //支付成功后回调回来的transactionId 按照实际情况填写
        request.setOutTradeNo(orderTradeSn);
        //支付成功后回调回来的transactionId 按照实际情况填写
        request.setOutRefundNo(refundSn);
        //退款成功的回调地址
        //        request.setNotifyUrl(MtWeChatConstant.REFUND_NOTIFY_URL);
        //发起请求,申请退款
        service.create(request);
    }

    public Config getConfig(String merchantId, String privateKeyPath, String merchantSerialNumber, String apiV3Key) {
        return new RSAAutoCertificateConfig.Builder().merchantId(merchantId).privateKeyFromPath(privateKeyPath)
            .merchantSerialNumber(merchantSerialNumber).apiV3Key(apiV3Key).build();
    }

    /**
     * 支付
     */
    public String pay(String appid, String merchantId, String privateKeyPath, String merchantSerialNumber,
        String apiV3Key, String openid, String goodsName, String orderSn, Integer total) {
        Config config = getConfig(merchantId, privateKeyPath, merchantSerialNumber, apiV3Key);

        JsapiService service = new JsapiService.Builder().config(config).build();

        PrepayRequest request = new PrepayRequest();
        Amount amount = new Amount();
        amount.setTotal(total);
        request.setAmount(amount);
        request.setAppid(appid);
        request.setMchid(merchantId);
        request.setDescription(goodsName);
        try {
            String url = new URL(new URL(notifyUrl), "wechat").toString();
            request.setNotifyUrl(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        request.setOutTradeNo(orderSn);
        Payer payer = new Payer();
        payer.setOpenid(openid);
        request.setPayer(payer);

        PrepayResponse response = service.prepay(request);
        return response.getPrepayId();
    }
}

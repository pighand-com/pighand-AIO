package com.pighand.aio.service.ECommerce.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.common.enums.*;
import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.common.utils.IDGenerator;
import com.pighand.aio.domain.ECommerce.*;
import com.pighand.aio.domain.base.ApplicationPlatformKeyDomain;
import com.pighand.aio.domain.base.ApplicationPlatformPayDomain;
import com.pighand.aio.domain.base.UserWechatDomain;
import com.pighand.aio.mapper.ECommerce.OrderMapper;
import com.pighand.aio.service.ECommerce.*;
import com.pighand.aio.service.ECommerce.payments.Wechat;
import com.pighand.aio.service.base.ApplicationPlatformKeyService;
import com.pighand.aio.service.base.ApplicationPlatformPayService;
import com.pighand.aio.service.base.UserWechatService;
import com.pighand.aio.vo.ECommerce.*;
import com.pighand.aio.vo.base.LoginUser;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.exception.ThrowPrompt;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.notification.NotificationConfig;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.partnerpayments.jsapi.model.Transaction;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.*;

import static com.pighand.aio.domain.ECommerce.table.BillTableDef.BILL;
import static com.pighand.aio.domain.ECommerce.table.GoodsSkuTableDef.GOODS_SKU;
import static com.pighand.aio.domain.ECommerce.table.GoodsSpuTableDef.GOODS_SPU;
import static com.pighand.aio.domain.ECommerce.table.OrderSkuTableDef.ORDER_SKU;
import static com.pighand.aio.domain.ECommerce.table.OrderTableDef.ORDER;
import static com.pighand.aio.domain.ECommerce.table.OrderTradeTableDef.ORDER_TRADE;
import static com.pighand.aio.domain.ECommerce.table.TicketTableDef.TICKET;
import static com.pighand.aio.domain.ECommerce.table.TicketValidityTableDef.TICKET_VALIDITY;
import static com.pighand.aio.domain.base.table.UserWechatTableDef.USER_WECHAT;

/**
 * 电商 - 订单
 *
 * @author wangshuli
 * @createDate 2024-04-18 14:35:34
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends BaseServiceImpl<OrderMapper, OrderDomain> implements OrderService {

    private final GoodsSkuService goodsSkuService;
    private final GoodsSpuService goodsSpuService;
    private final OrderTradeService orderTradeService;
    private final OrderSkuService orderSkuService;
    private final Wechat wechat;
    private final ApplicationPlatformPayService projectPlatformPayService;
    private final ApplicationPlatformKeyService projectPlatformKeyService;
    private final UserWechatService wechatService;
    private final TicketService ticketService;
    private final TicketValidityService ticketValidityService;
    private final TicketUserService ticketUserService;
    private final TicketUserValidityService ticketUserValidityService;

    @Value("${upload.path}")
    private String uploadPath;

    /**
     * 价格计算
     *
     * @param goods
     * @param orderSkus
     * @return
     */
    public CalculatePriceEstimateVO calculatePriceEstimate(List<? extends GoodsBaseInfo> goods,
        Map<Long, OrderSkuVO> orderSkus) {
        Map<Long, OrderVO> storeOrder = new HashMap<>(goods.size());

        BigDecimal totalAmountPayable = BigDecimal.ZERO;
        BigDecimal totalAmountPaid = BigDecimal.ZERO;

        for (GoodsBaseInfo goodsBaseInfo : goods) {
            OrderSkuVO orderSkuVO = orderSkus.get(goodsBaseInfo.getId());

            orderSkuVO.setSpuId(goodsBaseInfo.getSpuId());

            // 应付价格 = 数量 * 现价
            BigDecimal amountPayable = new BigDecimal(orderSkuVO.getQuantity().toString()).multiply(
                new BigDecimal(goodsBaseInfo.getCurrentPrice().toString()));
            orderSkuVO.setAmountPayable(amountPayable.longValue());
            totalAmountPayable = totalAmountPayable.add(amountPayable);

            // 实付金额 = 应付金额（TODO：扣减优惠）
            BigDecimal amountPaid = amountPayable;
            orderSkuVO.setAmountPaid(amountPaid.longValue());
            totalAmountPaid = totalAmountPaid.add(amountPaid);

            // 是否库存不足
            boolean stockOut = goodsBaseInfo.getStock() != null && orderSkuVO.getQuantity() > goodsBaseInfo.getStock();
            orderSkuVO.setStockOut(stockOut);

            // TODO: 库存不足处理，价格试算不抛异常
            if (stockOut) {
                throw new ThrowPrompt("库存不足");
            }

            Long mapStoreId = Optional.ofNullable(goodsBaseInfo.getStoreId()).orElse(0L);
            OrderVO orderVO = storeOrder.get(mapStoreId);

            if (orderVO == null) {
                orderVO = new OrderVO();
                orderVO.setStoreId(goodsBaseInfo.getStoreId());
                orderVO.setSn(IDGenerator.generate(TableIdEnum.ORDER, BillTypeEnum.PAYMENT));
                orderVO.setTradeStatus(10);
                orderVO.setRefundStatus(10);
                orderVO.setAmountPaid(0L);
                orderVO.setAmountPayable(0L);
                orderVO.setOrderSku(new ArrayList<>(goods.size()));
            }

            orderVO.setAmountPaid(new BigDecimal(orderVO.getAmountPaid().toString()).add(amountPayable).longValue());

            orderVO.setAmountPayable(new BigDecimal(orderVO.getAmountPayable().toString()).add(amountPaid).longValue());

            orderVO.getOrderSku().add(orderSkuVO);
            storeOrder.put(mapStoreId, orderVO);
        }

        CalculatePriceEstimateVO calculatePriceEstimateVO = new CalculatePriceEstimateVO();
        calculatePriceEstimateVO.setOrder(storeOrder.values().stream().toList());
        calculatePriceEstimateVO.setAmountPaid(totalAmountPaid);
        calculatePriceEstimateVO.setAmountPayable(totalAmountPayable);
        return calculatePriceEstimateVO;
    }

    /**
     * 获取库存信息
     *
     * @param orderSKUType
     * @param ids
     * @return
     */
    private List<? extends GoodsBaseInfo> getStock(OrderSKUTypeEnum orderSKUType, Set<Long> ids) {
        if (ids == null || ids.size() == 0) {
            return Collections.emptyList();
        }

        List<? extends GoodsBaseInfo> goodsInfos = null;

        if (orderSKUType.equals(OrderSKUTypeEnum.SKU)) {
            goodsInfos = goodsSkuService.queryChain()
                .select(GOODS_SKU.ID, GOODS_SKU.SPU_ID, GOODS_SKU.STOCK, GOODS_SKU.CURRENT_PRICE, GOODS_SPU.STORE_ID)
                .innerJoin(GOODS_SPU).on(GOODS_SKU.SPU_ID.eq(GOODS_SPU.ID)).where(GOODS_SKU.ID.in(ids))
                .and(GOODS_SPU.STATUS.eq(GoodsSpuStatusEnum.LISTED)).list();
        } else if (orderSKUType.equals(OrderSKUTypeEnum.TICKET)) {
            // TODO: ticket
            goodsInfos =
                ticketService.queryChain().select(TICKET.ID, TICKET.STOCK, TICKET.CURRENT_PRICE, TICKET.STORE_ID)
                    .where(TICKET.ID.in(ids)).list();
        } else if (orderSKUType.equals(OrderSKUTypeEnum.SESSION)) {
            // TODO: session
        }

        return goodsInfos;
    }

    /**
     * 获取下单商品信息
     *
     * @param orderSku
     * @return
     */
    private OrderTradeDomain getPlaceGoods(List<OrderSkuVO> orderSku) {
        Date now = new Date();
        LoginUser loginUser = Context.getLoginUser();

        // 购买数量map
        Map<Long, OrderSkuVO> skuQuantities = new HashMap<>(orderSku.size());
        Map<Long, OrderSkuVO> ticketQuantities = new HashMap<>(orderSku.size());
        Map<Long, OrderSkuVO> sessionQuantities = new HashMap<>(orderSku.size());

        orderSku.forEach(orderSkuVO -> {
            if (VerifyUtils.isNotEmpty(orderSkuVO.getSkuId())) {
                orderSkuVO.setType(OrderSKUTypeEnum.SKU);
                skuQuantities.put(orderSkuVO.getSkuId(), orderSkuVO);
            } else if (VerifyUtils.isNotEmpty(orderSkuVO.getTicketId())) {
                orderSkuVO.setType(OrderSKUTypeEnum.TICKET);
                ticketQuantities.put(orderSkuVO.getTicketId(), orderSkuVO);
            } else if (VerifyUtils.isNotEmpty(orderSkuVO.getTicketId())) {
                orderSkuVO.setType(OrderSKUTypeEnum.SESSION);
                sessionQuantities.put(orderSkuVO.getSessionId(), orderSkuVO);
            }
        });

        Set<Long> skuIds = skuQuantities.keySet();
        Set<Long> ticketIds = ticketQuantities.keySet();
        Set<Long> sessionIds = sessionQuantities.keySet();

        List<? extends GoodsBaseInfo> skuInfo = this.getStock(OrderSKUTypeEnum.SKU, skuIds);
        List<? extends GoodsBaseInfo> ticketInfo = this.getStock(OrderSKUTypeEnum.TICKET, ticketIds);
        List<? extends GoodsBaseInfo> sessionInfo = this.getStock(OrderSKUTypeEnum.SESSION, sessionIds);

        CalculatePriceEstimateVO skuOrder = this.calculatePriceEstimate(skuInfo, skuQuantities);
        CalculatePriceEstimateVO ticketOrder = this.calculatePriceEstimate(ticketInfo, ticketQuantities);
        CalculatePriceEstimateVO sessionOrder = this.calculatePriceEstimate(sessionInfo, sessionQuantities);

        // 保存交易单
        OrderTradeDomain orderTradeDomain = new OrderTradeDomain();
        orderTradeDomain.setSn(IDGenerator.generate(TableIdEnum.ORDER_TRADE, BillTypeEnum.PAYMENT));
        orderTradeDomain.setAmountPaid(
            skuOrder.getAmountPaid().add(ticketOrder.getAmountPaid()).add(sessionOrder.getAmountPaid()).longValue());
        orderTradeDomain.setAmountPayable(
            skuOrder.getAmountPayable().add(ticketOrder.getAmountPayable()).add(sessionOrder.getAmountPayable())
                .longValue());
        orderTradeDomain.setCreatorId(loginUser.getId());
        orderTradeDomain.setCreatedAt(now);
        orderTradeService.save(orderTradeDomain);

        // 保存订单
        List<OrderSkuDomain> saveOrderSku = new ArrayList<>(orderSku.size());
        for (OrderVO orderVO : skuOrder.getOrder()) {
            orderVO.setOrderTradeId(orderTradeDomain.getId());
            orderVO.setCreatedAt(now);
            orderVO.setCreatorId(loginUser.getId());
            this.save(orderVO);

            for (OrderSkuVO orderSkuVO : orderVO.getOrderSku()) {
                orderSkuVO.setOrderId(orderVO.getId());
                orderSkuVO.setOrderTradeId(orderTradeDomain.getId());
                saveOrderSku.add(orderSkuVO);
            }
        }

        for (OrderVO orderVO : ticketOrder.getOrder()) {
            orderVO.setOrderTradeId(orderTradeDomain.getId());
            orderVO.setCreatedAt(now);
            orderVO.setCreatorId(loginUser.getId());
            this.save(orderVO);

            for (OrderSkuVO orderSkuVO : orderVO.getOrderSku()) {
                orderSkuVO.setOrderId(orderVO.getId());
                orderSkuVO.setOrderTradeId(orderTradeDomain.getId());
                saveOrderSku.add(orderSkuVO);
            }
        }

        for (OrderVO orderVO : sessionOrder.getOrder()) {
            orderVO.setCreatedAt(now);
            orderVO.setCreatorId(loginUser.getId());
            this.save(orderVO);

            for (OrderSkuVO orderSkuVO : orderVO.getOrderSku()) {
                orderSkuVO.setOrderId(orderVO.getId());
                orderSkuVO.setOrderTradeId(orderTradeDomain.getId());
                saveOrderSku.add(orderSkuVO);
            }
        }

        orderSkuService.saveBatch(saveOrderSku);

        return orderTradeDomain;
    }

    /**
     * 下单并支付
     *
     * @param orderVO
     * @return
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PayVO placeOrderAndPay(OrderVO orderVO) {
        OrderTradeDomain order = this.getPlaceGoods(orderVO.getOrderSku());

        if (!orderVO.getAmountPaid().equals(order.getAmountPaid())) {
            throw new ThrowPrompt("价格发生改变，请重新下单");
        }

        if (orderVO.getOutTradePlatform() != null) {
            Long applicationId = Context.getApplicationId();
            if (orderVO.getOutTradePlatform().equals(PlatformEnum.WECHAT_APPLET.value)) {
                ApplicationPlatformPayDomain projectPlatformPayDomain = projectPlatformPayService.find(applicationId);
                ApplicationPlatformKeyDomain projectPlatformKeyDomain =
                    projectPlatformKeyService.findByPlatform(applicationId, PlatformEnum.WECHAT_APPLET);

                UserWechatDomain userWechatDomain = wechatService.queryChain().select(USER_WECHAT.OPENID)
                    .where(USER_WECHAT.USER_ID.eq(Context.getLoginUser().getId()))
                    .and(USER_WECHAT.APPLICATION_ID.eq(applicationId)).one();

                PayVO payVO = new PayVO();
                String prepay_id =
                    wechat.pay(projectPlatformKeyDomain.getAppid(), projectPlatformPayDomain.getWechatMerchantId(),
                        uploadPath + "/apiclient_key.pem",
                        projectPlatformPayDomain.getWechatMerchantCertificateSerial(),
                        projectPlatformPayDomain.getWechatMerchantV3(), userWechatDomain.getOpenid(), "商品",
                        order.getSn(), Integer.valueOf(order.getAmountPaid().toString()));
                payVO.setPrepayId(prepay_id);

                Long nonceStr = System.currentTimeMillis();
                StringBuilder sb = new StringBuilder();
                // 应用id
                sb.append(projectPlatformKeyDomain.getAppid()).append("\n");
                // 支付签名时间戳
                sb.append(nonceStr).append("\n");
                // 随机字符串
                sb.append(nonceStr).append("\n");
                // 预支付交易会话ID
                sb.append("prepay_id=").append(prepay_id).append("\n");
                // 签名
                Signature sign = null;
                try {
                    sign = Signature.getInstance("SHA256withRSA");
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
                // 获取商户私钥并进行签名
                PrivateKey privateKey = this.getPrivateKey(uploadPath + "/apiclient_key.pem");
                try {
                    sign.initSign(privateKey);
                } catch (InvalidKeyException e) {
                    throw new RuntimeException(e);
                }
                try {
                    sign.update(sb.toString().getBytes(StandardCharsets.UTF_8));
                } catch (SignatureException e) {
                    throw new RuntimeException(e);
                }
                try {
                    String paySign = Base64.getEncoder().encodeToString(sign.sign());
                    payVO.setPaySign(paySign);
                } catch (SignatureException e) {
                    throw new RuntimeException(e);
                }
                payVO.setSignType("RSA");
                payVO.setTimeStamp(nonceStr.toString());
                payVO.setNonceStr(nonceStr.toString());
                return payVO;
            }
        }

        return null;
    }

    public PrivateKey getPrivateKey(String filename) {
        try {
            File file = ResourceUtils.getFile(filename);
            return PemUtil.loadPrivateKey(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("私钥文件不存在", e);
        }
    }

    // 获取请求头里的数据
    private String getRequestBody(HttpServletRequest request) {

        StringBuffer sb = new StringBuffer();

        try (ServletInputStream inputStream = request.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            log.error("读取数据流异常:{}", e);
        }

        return sb.toString();

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void payNotify(HttpServletRequest request) {
        Date now = new Date();
        log.info("=============微信支付回调========");
        // 初始化map，给微信响应用
        Map<String, String> map = new HashMap<>(2);
        try {

            ApplicationPlatformPayDomain projectPlatformPayDomain =
                projectPlatformPayService.find(Context.getApplicationId());
            Config config =
                wechat.getConfig(projectPlatformPayDomain.getWechatMerchantId(), uploadPath + "/apiclient_key.pem",
                    projectPlatformPayDomain.getWechatMerchantCertificateSerial(),
                    projectPlatformPayDomain.getWechatMerchantV3());

            // 从请求头中获取信息
            // 这是微信回调固定在请求头里的参数，验签要用
            String timestamp = request.getHeader("Wechatpay-Timestamp");
            String nonce = request.getHeader("Wechatpay-Nonce");
            String signature = request.getHeader("Wechatpay-Signature");
            String singType = request.getHeader("Wechatpay-Signature-Type");
            String number = request.getHeader("Wechatpay-Serial");
            String body = getRequestBody(request);
            // 初始化解析器 NotificationParser
            // 这个解析器是微信支付SDK里的，他会帮我们做验签等一系列的事，不需要我们去做
            NotificationParser parser = new NotificationParser((NotificationConfig)config);
            RequestParam requestParam =
                new RequestParam.Builder().nonce(nonce).signature(signature).signType(singType).timestamp(timestamp)
                    .serialNumber(number).body(body).build();
            // 不同的API的回调有不同的参数，这里的Transaction只是微信支付接口的回调参数对象
            //.parse()方法中会做验签及resource对象的解密，Transaction对象中就是解密后的参数
            Transaction decryptObject = parser.parse(requestParam, Transaction.class);
            if (decryptObject != null) {
                // 支付成功
                log.info("=============微信支付回调========trade_status:" + decryptObject.getTradeState());
                if (Transaction.TradeStateEnum.SUCCESS.equals(decryptObject.getTradeState())) {
                    // 处理支付成功逻辑
                    log.info("进入try catch");
                    try {
                        // 下面就是做支付成功的逻辑
                        String sn = decryptObject.getOutTradeNo();

                        OrderTradeDomain orderTradeDomain =
                            orderTradeService.queryChain().select(ORDER_TRADE.ID, ORDER_TRADE.CREATOR_ID)
                                .where(ORDER_TRADE.SN.eq(sn)).limit(1).one();

                        this.updateChain().set(ORDER.TRADE_STATUS, 20)
                            .where(ORDER.ORDER_TRADE_ID.eq(orderTradeDomain.getId())).update();

                        // 查询票务或场次订单
                        List<OrderSkuDomain> orderSkus = orderSkuService.queryChain()
                            .select(ORDER_SKU.SESSION_ID, ORDER_SKU.TICKET_ID, ORDER_SKU.ORDER_ID)
                            .where(ORDER_SKU.ORDER_TRADE_ID.eq(orderTradeDomain.getId())).list();

                        orderSkus.forEach(orderSkuDomain -> {
                            if (orderSkuDomain.getSessionId() != null) {
                            } else if (orderSkuDomain.getTicketId() != null) {
                                TicketDomain ticketDomain = ticketService.find(orderSkuDomain.getTicketId());

                                List<TicketValidityDomain> ticketValidity = ticketValidityService.queryChain()
                                    .select(TICKET_VALIDITY.ID, TICKET_VALIDITY.VALIDATION_COUNT,
                                        TICKET_VALIDITY.VALIDITY_IDS)
                                    .where(TICKET_VALIDITY.TICKET_ID.eq(orderSkuDomain.getTicketId())).list();

                                // 下发票务
                                TicketUserVO ticketUserVO = new TicketUserVO();
                                ticketUserVO.setTicketId(orderSkuDomain.getTicketId());
                                ticketUserVO.setOrderId(orderSkuDomain.getOrderId());
                                // TODO: 创建票务时，如果配置可用范围，核销次数根据可用范围计算总和
                                ticketUserVO.setRemainingValidationCount(ticketDomain.getValidationCount());
                                ticketUserVO.setCreatedAt(now);
                                ticketUserVO.setCreatorId(orderTradeDomain.getCreatorId());
                                ticketUserService.create(ticketUserVO);

                                List<TicketUserValidityDomain> userValidities = new ArrayList<>(ticketValidity.size());
                                ticketValidity.forEach(item -> {
                                    TicketUserValidityVO ticketUserValidityVO = new TicketUserValidityVO();
                                    ticketUserValidityVO.setTicketUserId(ticketUserVO.getId());
                                    ticketUserValidityVO.setTicketId(orderSkuDomain.getTicketId());
                                    ticketUserValidityVO.setTicketValidityId(item.getId());
                                    ticketUserValidityVO.setValidationCount(item.getValidationCount());
                                    userValidities.add(ticketUserValidityVO);
                                });
                                ticketUserValidityService.saveBatch(userValidities);
                            }
                        });

                        //响应微信
                        map.put("code", "SUCCESS");
                        map.put("message", "成功");
                    } catch (Exception e) {
                        log.error("微信回调处理失败", e);
                        e.printStackTrace();
                    }
                } else {
                    log.info("莫名其妙");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 分页或列表
     *
     * @return PageOrList<OrderTradeVO>
     */
    @Override
    public PageOrList<OrderVO> query(OrderVO orderVO) {
        orderVO.setJoinTables(ORDER_TRADE.getTableName(), ORDER_SKU.getTableName(), BILL.getTableName(),
            GOODS_SPU.getTableName(), TICKET.getTableName());

        QueryWrapper queryWrapper = new QueryWrapper();

        // like
        queryWrapper.and(ORDER.SN.like(orderVO.getSn(), VerifyUtils::isNotEmpty));

        // equal
        queryWrapper.and(ORDER.CREATOR_ID.eq(orderVO.getCreatorId(), VerifyUtils::isNotEmpty));

        queryWrapper.and(ORDER.TRADE_STATUS.ne(10));

        queryWrapper.orderBy(ORDER.ID.desc());

        PageOrList<OrderVO> result = super.mapper.query(orderVO, queryWrapper);

        List<Long> goodsSpuIds = new ArrayList<>();
        List<Long> ticketIds = new ArrayList<>();
        List<Long> tradeIds = new ArrayList<>();

        result.getRecords().forEach(order -> {
            order.getOrderSku().forEach(orderSku -> {
                if (orderSku.getSpuId() != null) {
                    goodsSpuIds.add(orderSku.getSpuId());
                } else if (orderSku.getTicketId() != null) {
                    ticketIds.add(orderSku.getTicketId());
                }
            });

            tradeIds.add(order.getOrderTradeId());
        });

        Map<Long, GoodsSpuVO> goodsSpuMap = new HashMap<>();
        if (goodsSpuIds.size() > 0) {
            List<GoodsSpuVO> goodsSpu =
                goodsSpuService.queryChain().select(GOODS_SPU.ID, GOODS_SPU.NAME).where(GOODS_SPU.ID.in(goodsSpuIds))
                    .listAs(GoodsSpuVO.class);

            goodsSpu.forEach(goodsSpuDomain -> {
                goodsSpuMap.put(goodsSpuDomain.getId(), goodsSpuDomain);
            });
        }

        Map<Long, TicketVO> ticketMap = new HashMap<>();
        if (ticketIds.size() > 0) {
            List<TicketVO> tickets =
                ticketService.queryChain().select(TICKET.ID, TICKET.NAME).where(TICKET.ID.in(ticketIds))
                    .listAs(TicketVO.class);

            tickets.forEach(ticketDomain -> {
                ticketMap.put(ticketDomain.getId(), ticketDomain);
            });
        }

        // TODO: mapper中关联无效，排查原因
        Map<Long, OrderTradeVO> tradeMap = new HashMap<>();
        if (tradeIds.size() > 0) {
            List<OrderTradeVO> trades =
                orderTradeService.queryChain().select(ORDER_TRADE.ID, ORDER_TRADE.SN).where(ORDER_TRADE.ID.in(tradeIds))
                    .listAs(OrderTradeVO.class);

            trades.forEach(trade -> {
                tradeMap.put(trade.getId(), trade);
            });
        }

        result.getRecords().forEach(order -> {
            List<GoodsSpuVO> goodsSpu = new ArrayList<>();
            List<TicketVO> ticket = new ArrayList<>();

            order.getOrderSku().forEach(orderSku -> {
                if (orderSku.getSpuId() != null) {
                    goodsSpu.add(goodsSpuMap.get(orderSku.getSpuId()));
                } else if (orderSku.getTicketId() != null) {
                    ticket.add(ticketMap.get(orderSku.getTicketId()));
                }
            });

            order.setGoodsSpu(goodsSpu);
            order.setTicket(ticket);

            order.setOrderTrade(tradeMap.get(order.getOrderTradeId()));
        });

        return result;
    }

    /**
     * 确认收货
     *
     * @param id order - id
     */
    @Override
    public void confirmReceipt(Long id) {
        OrderDomain orderDomain = this.getById(id);

        if (orderDomain == null) {
            throw new ThrowPrompt("订单不存在");
        }

        switch (orderDomain.getTradeStatus()) {
            case 10:
                throw new ThrowPrompt("订单未支付");
            case 40:
                throw new ThrowPrompt("设备已领取");
            case 50:
                throw new ThrowPrompt("订单已取消");
            case 51:
                throw new ThrowPrompt("订单已退款");
        }

        this.updateChain().set(ORDER.TRADE_STATUS, 40).set(ORDER.REFUND_STATUS, 11).set(ORDER.EVALUATION_STATUS, 20)
            .where(ORDER.ID.eq(id)).update();
    }

    /**
     * 退款
     *
     * @param orderVO
     */
    @Override
    public void refund(OrderVO orderVO) {
        if (orderVO.getOrderTradeId() != null) {
            OrderTradeDomain orderTradeDomain = orderTradeService.getById(orderVO.getOrderTradeId());

            Long applicationId = Context.getApplicationId();
            ApplicationPlatformPayDomain projectPlatformPayDomain = projectPlatformPayService.find(applicationId);
            wechat.refund(projectPlatformPayDomain.getWechatMerchantId(), uploadPath + "/apiclient_key.pem",
                projectPlatformPayDomain.getWechatMerchantCertificateSerial(),
                projectPlatformPayDomain.getWechatMerchantV3(), orderTradeDomain.getAmountPaid(),
                orderTradeDomain.getSn(), orderTradeDomain.getSn());
        }
    }
}

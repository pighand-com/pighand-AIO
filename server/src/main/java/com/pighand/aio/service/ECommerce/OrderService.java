package com.pighand.aio.service.ECommerce;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.common.enums.*;
import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.common.utils.IDGenerator;
import com.pighand.aio.domain.ECommerce.*;
import com.pighand.aio.domain.base.*;
import com.pighand.aio.mapper.ECommerce.OrderMapper;
import com.pighand.aio.service.ECommerce.*;
import com.pighand.aio.service.ECommerce.payments.Wechat;
import com.pighand.aio.service.base.*;
import com.pighand.aio.service.distribution.DistributionSalesService;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.*;
import java.util.stream.Collectors;

import static com.pighand.aio.domain.ECommerce.table.BillTableDef.BILL;
import static com.pighand.aio.domain.ECommerce.table.GoodsSkuTableDef.GOODS_SKU;
import static com.pighand.aio.domain.ECommerce.table.GoodsSpuTableDef.GOODS_SPU;
import static com.pighand.aio.domain.ECommerce.table.OrderSkuTableDef.ORDER_SKU;
import static com.pighand.aio.domain.ECommerce.table.OrderTableDef.ORDER;
import static com.pighand.aio.domain.ECommerce.table.OrderTradeTableDef.ORDER_TRADE;
import static com.pighand.aio.domain.ECommerce.table.TicketTableDef.TICKET;
import static com.pighand.aio.domain.ECommerce.table.TicketUserTableDef.TICKET_USER;
import static com.pighand.aio.domain.ECommerce.table.TicketValidityTableDef.TICKET_VALIDITY;
import static com.pighand.aio.domain.base.table.UserTableDef.USER;
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
public class OrderService extends BaseServiceImpl<OrderMapper, OrderDomain>  {

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

    private final DistributionSalesService distributionSalesService;

    private final UserService userService;

    private final StoreService storeService;

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

            //            orderSkuVO.setSpuId(goodsBaseInfo.getSpuId());

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
    private OrderTradeVO getPlaceGoods(List<OrderSkuVO> orderSku, Long salespersonId) {
        Date now = new Date();
        LoginUser loginUser = Context.loginUser();

        UserDomain userDomain = userService.queryChain().select(USER.PHONE).where(USER.ID.eq(loginUser.getId())).one();

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
        OrderTradeVO orderTrade = new OrderTradeVO();
        orderTrade.setSn(IDGenerator.generate(TableIdEnum.ORDER_TRADE, BillTypeEnum.PAYMENT));
        orderTrade.setAmountPaid(
            skuOrder.getAmountPaid().add(ticketOrder.getAmountPaid()).add(sessionOrder.getAmountPaid()).longValue());
        orderTrade.setAmountPayable(
            skuOrder.getAmountPayable().add(ticketOrder.getAmountPayable()).add(sessionOrder.getAmountPayable())
                .longValue());
        orderTrade.setCreatorId(loginUser.getId());
        orderTrade.setCreatedAt(now);
        orderTrade.setSalespersonId(salespersonId);
        orderTradeService.save(orderTrade);

        // 保存订单
        List<Long> orderIds = new ArrayList<>(orderSku.size());
        List<OrderSkuDomain> saveOrderSku = new ArrayList<>(orderSku.size());
        for (OrderVO orderVO : skuOrder.getOrder()) {
            orderVO.setOrderTradeId(orderTrade.getId());
            orderVO.setCreatedAt(now);
            orderVO.setCreatorId(loginUser.getId());
            orderVO.setUserPhone(userDomain.getPhone());
            // 设置订单超时时间（10分钟后）
            orderVO.setExpiredAt(new Date(now.getTime() + 10 * 60 * 1000));
            this.save(orderVO);

            orderIds.add(orderVO.getId());

            for (OrderSkuVO orderSkuVO : orderVO.getOrderSku()) {
                orderSkuVO.setOrderId(orderVO.getId());
                orderSkuVO.setOrderTradeId(orderTrade.getId());
                saveOrderSku.add(orderSkuVO);
            }
        }

        for (OrderVO orderVO : ticketOrder.getOrder()) {
            orderVO.setOrderTradeId(orderTrade.getId());
            orderVO.setCreatedAt(now);
            orderVO.setCreatorId(loginUser.getId());
            orderVO.setUserPhone(userDomain.getPhone());
            // 设置订单超时时间（10分钟后）
            orderVO.setExpiredAt(new Date(now.getTime() + 10 * 60 * 1000));
            this.save(orderVO);

            orderIds.add(orderVO.getId());

            for (OrderSkuVO orderSkuVO : orderVO.getOrderSku()) {
                orderSkuVO.setOrderId(orderVO.getId());
                orderSkuVO.setOrderTradeId(orderTrade.getId());
                saveOrderSku.add(orderSkuVO);
            }
        }

        for (OrderVO orderVO : sessionOrder.getOrder()) {
            orderVO.setOrderTradeId(orderTrade.getId());
            orderVO.setCreatedAt(now);
            orderVO.setCreatorId(loginUser.getId());
            orderVO.setUserPhone(userDomain.getPhone());
            // 设置订单超时时间（10分钟后）
            orderVO.setExpiredAt(new Date(now.getTime() + 10 * 60 * 1000));
            this.save(orderVO);

            orderIds.add(orderVO.getId());

            for (OrderSkuVO orderSkuVO : orderVO.getOrderSku()) {
                orderSkuVO.setOrderId(orderVO.getId());
                orderSkuVO.setOrderTradeId(orderTrade.getId());
                saveOrderSku.add(orderSkuVO);
            }
        }

        orderSkuService.saveBatch(saveOrderSku);

        orderTrade.setOrderIds(orderIds);
        return orderTrade;
    }

    /**
     * 下单并支付
     * <p>
     * TODO 解决订单sn、交易单sn不同问题，前台显示订单sn，但在微信商户的是交易单sn，查询起来费劲
     *
     * @param orderVO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public PayVO placeOrderAndPay(OrderVO orderVO) {
        OrderTradeVO orderTrade = this.getPlaceGoods(orderVO.getOrderSku(), orderVO.getSalespersonId());

        if (!orderVO.getAmountPaid().equals(orderTrade.getAmountPaid())) {
            throw new ThrowPrompt("价格发生改变，请重新下单");
        }

        if (VerifyUtils.isEmpty(orderVO.getOutTradePlatform())) {
            return null;
        }

        Long applicationId = Context.applicationId();
        if (orderVO.getOutTradePlatform().equals(PlatformEnum.WECHAT_APPLET.value)) {
            ApplicationPlatformPayDomain projectPlatformPayDomain = projectPlatformPayService.find(applicationId);
            ApplicationPlatformKeyDomain projectPlatformKeyDomain =
                projectPlatformKeyService.findByPlatform(PlatformEnum.WECHAT_APPLET);

            UserWechatDomain userWechatDomain = wechatService.queryChain().select(USER_WECHAT.OPENID)
                .where(USER_WECHAT.USER_ID.eq(Context.loginUser().getId()))
                .and(USER_WECHAT.APPLICATION_ID.eq(applicationId)).one();

            PayVO payVO = new PayVO();
            payVO.setOrderIds(orderTrade.getOrderIds().stream().map(String::valueOf).collect(Collectors.toList()));

            OrderDomain order = this.getById(orderTrade.getOrderIds().get(0));
            StoreDomain store = storeService.getById(order.getStoreId());

            String prepay_id =
                wechat.pay(projectPlatformKeyDomain.getAppid(), projectPlatformPayDomain.getWechatMerchantId(),
                    projectPlatformPayDomain.getWechatMerchantCertificate(),
                    projectPlatformPayDomain.getWechatMerchantCertificateSerial(),
                    projectPlatformPayDomain.getWechatMerchantPublicKey(),
                    projectPlatformPayDomain.getWechatMerchantPublicKeyId(),
                    projectPlatformPayDomain.getWechatMerchantV3(), userWechatDomain.getOpenid(), store.getName(),
                    orderTrade.getSn(), Integer.valueOf(orderTrade.getAmountPaid().toString()));
            payVO.setPrepayId(prepay_id);

            Long nonceStr = System.currentTimeMillis();

            // 获取商户私钥并进行签名
            try {
                Signature sign = Signature.getInstance("SHA256withRSA");

                File file = ResourceUtils.getFile(projectPlatformPayDomain.getWechatMerchantCertificate());
                PrivateKey privateKey = PemUtil.loadPrivateKey(new FileInputStream(file));
                sign.initSign(privateKey);

                // 应用id
                String sb = projectPlatformKeyDomain.getAppid() + "\n"
                    // 支付签名时间戳
                    + nonceStr + "\n"
                    // 随机字符串
                    + nonceStr + "\n"
                    // 预支付交易会话ID
                    + "prepay_id=" + prepay_id + "\n";
                sign.update(sb.getBytes(StandardCharsets.UTF_8));

                String paySign = Base64.getEncoder().encodeToString(sign.sign());
                payVO.setPaySign(paySign);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            payVO.setSignType("RSA");
            payVO.setTimeStamp(nonceStr.toString());
            payVO.setNonceStr(nonceStr.toString());
            return payVO;
        }

        return null;
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
    public void payNotify(HttpServletRequest request) {
        Date now = new Date();
        log.info("=============微信支付回调========");
        // 初始化map，给微信响应用
        Map<String, String> map = new HashMap<>(2);
        try {

            ApplicationPlatformPayDomain projectPlatformPayDomain =
                projectPlatformPayService.find(Context.applicationId());
            Config config = wechat.getConfig(projectPlatformPayDomain.getWechatMerchantId(),
                projectPlatformPayDomain.getWechatMerchantCertificate(),
                projectPlatformPayDomain.getWechatMerchantCertificateSerial(),
                projectPlatformPayDomain.getWechatMerchantPublicKey(),
                projectPlatformPayDomain.getWechatMerchantPublicKeyId(),
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

                        OrderTradeDomain orderTradeDomain = orderTradeService.queryChain()
                            .select(ORDER_TRADE.ID, ORDER_TRADE.CREATOR_ID, ORDER_TRADE.SALESPERSON_ID)
                            .where(ORDER_TRADE.SN.eq(sn)).limit(1).one();

                        // TODO 根据类型判断，票务直接40，商品20
                        this.updateChain().set(ORDER.TRADE_STATUS, 40).set(ORDER.REFUND_STATUS, 11)
                            .where(ORDER.ORDER_TRADE_ID.eq(orderTradeDomain.getId())).update();

                        // 查询票务或场次订单
                        List<OrderSkuDomain> orderSkus = orderSkuService.queryChain()
                            .select(ORDER_SKU.ID, ORDER_SKU.SESSION_ID, ORDER_SKU.TICKET_ID, ORDER_SKU.ORDER_ID,
                                ORDER_SKU.QUANTITY).where(ORDER_SKU.ORDER_TRADE_ID.eq(orderTradeDomain.getId())).list();

                        List<Long> ticketUserIds = new ArrayList<>(orderSkus.size());
                        orderSkus.forEach(orderSkuDomain -> {
                            if (orderSkuDomain.getSessionId() != null) {
                            } else if (orderSkuDomain.getTicketId() != null) {
                                TicketDomain ticketDomain = ticketService.find(orderSkuDomain.getTicketId());

                                List<TicketValidityDomain> ticketValidity = ticketValidityService.queryChain()
                                    .select(TICKET_VALIDITY.ID, TICKET_VALIDITY.VALIDATION_COUNT,
                                        TICKET_VALIDITY.VALIDITY_IDS)
                                    .where(TICKET_VALIDITY.TICKET_ID.eq(orderSkuDomain.getTicketId())).list();

                                List<TicketUserValidityDomain> userValidities = new ArrayList<>(ticketValidity.size());

                                // 下发票务
                                Integer quantity = orderSkuDomain.getQuantity();
                                for (int i = 0; i < quantity; i++) {
                                    TicketUserVO ticketUserVO = new TicketUserVO();
                                    ticketUserVO.setTicketId(orderSkuDomain.getTicketId());
                                    ticketUserVO.setOrderId(orderSkuDomain.getOrderId());
                                    ticketUserVO.setOrderSkuId(orderSkuDomain.getId());
                                    // TODO: 创建票务时，如果配置可用范围，核销次数根据可用范围计算总和
                                    ticketUserVO.setRemainingValidationCount(ticketDomain.getValidationCount());
                                    ticketUserVO.setCreatedAt(now);
                                    ticketUserVO.setCreatorId(orderTradeDomain.getCreatorId());

                                    ticketUserService.create(ticketUserVO);
                                    ticketUserIds.add(ticketUserVO.getId());

                                    ticketValidity.forEach(item -> {
                                        TicketUserValidityVO ticketUserValidityVO = new TicketUserValidityVO();
                                        ticketUserValidityVO.setTicketUserId(ticketUserVO.getId());
                                        ticketUserValidityVO.setTicketId(orderSkuDomain.getTicketId());
                                        ticketUserValidityVO.setTicketValidityId(item.getId());
                                        ticketUserValidityVO.setValidationCount(item.getValidationCount());
                                        userValidities.add(ticketUserValidityVO);
                                    });
                                }

                                ticketUserValidityService.saveBatch(userValidities);
                            }
                        });

                        // 创建分销
                        // TODO 支持交易单，多种票
                        distributionSalesService.createTicket(orderTradeDomain.getSalespersonId(),
                            orderSkus.get(0).getOrderId(), orderSkus.get(0).getTicketId(), ticketUserIds);

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
    public PageOrList<OrderVO> query(OrderVO orderVO) {
        orderVO.setJoinTables(ORDER_TRADE.getTableName(), ORDER_SKU.getTableName(), BILL.getTableName(),
            GOODS_SPU.getTableName(), TICKET.getTableName());

        QueryWrapper queryWrapper = new QueryWrapper();

        // like
        queryWrapper.and(ORDER.SN.like(orderVO.getSn(), VerifyUtils::isNotEmpty));
        queryWrapper.and(ORDER.USER_PHONE.eq(orderVO.getUserPhone(), VerifyUtils::isNotEmpty));

        // equal
        queryWrapper.and(ORDER.CREATOR_ID.eq(orderVO.getCreatorId(), VerifyUtils::isNotEmpty));
        queryWrapper.and(ORDER.TRADE_STATUS.eq(orderVO.getTradeStatus(), VerifyUtils::isNotEmpty));

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
            List<TicketVO> tickets = ticketService.queryChain().select(TICKET.ID, TICKET.NAME, TICKET.POSTER_URL)
                .where(TICKET.ID.in(ticketIds)).listAs(TicketVO.class);

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
     * 订单详情
     *
     * @param id          order - id
     * @param loginUserId 登录用户ID，如果不为空则校验订单归属
     * @return OrderVO
     */
    public OrderVO find(Long id, Long loginUserId) {
        // 查询订单基本信息，关联相关表
        OrderVO orderVO = new OrderVO();
        orderVO.setJoinTables(ORDER_TRADE.getTableName(), ORDER_SKU.getTableName(), BILL.getTableName(),
            GOODS_SPU.getTableName(), TICKET.getTableName());

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.and(ORDER.ID.eq(id));

        // 如果传入了登录用户ID，则校验订单归属
        if (loginUserId != null) {
            queryWrapper.and(ORDER.CREATOR_ID.eq(loginUserId));
        }

        PageOrList<OrderVO> result = super.mapper.query(orderVO, queryWrapper);

        if (result.getRecords().isEmpty()) {
            if (loginUserId != null) {
                throw new ThrowPrompt("订单不存在或无权限访问");
            } else {
                throw new ThrowPrompt("订单不存在");
            }
        }

        OrderVO order = result.getRecords().get(0);

        List<Long> goodsSpuIds = new ArrayList<>();
        List<Long> ticketIds = new ArrayList<>();

        // 收集商品和票务ID
        order.getOrderSku().forEach(orderSku -> {
            if (orderSku.getSpuId() != null) {
                goodsSpuIds.add(orderSku.getSpuId());
            } else if (orderSku.getTicketId() != null) {
                ticketIds.add(orderSku.getTicketId());
            }
        });

        // 查询商品信息
        Map<Long, GoodsSpuVO> goodsSpuMap = new HashMap<>();
        if (goodsSpuIds.size() > 0) {
            List<GoodsSpuVO> goodsSpu =
                goodsSpuService.queryChain().select(GOODS_SPU.ID, GOODS_SPU.NAME).where(GOODS_SPU.ID.in(goodsSpuIds))
                    .listAs(GoodsSpuVO.class);

            goodsSpu.forEach(goodsSpuDomain -> {
                goodsSpuMap.put(goodsSpuDomain.getId(), goodsSpuDomain);
            });
        }

        // 查询票务信息
        Map<Long, TicketVO> ticketMap = new HashMap<>();
        if (ticketIds.size() > 0) {
            List<TicketVO> tickets = ticketService.queryChain().select(TICKET.ID, TICKET.NAME, TICKET.POSTER_URL)
                .where(TICKET.ID.in(ticketIds)).listAs(TicketVO.class);

            tickets.forEach(ticketDomain -> {
                ticketMap.put(ticketDomain.getId(), ticketDomain);
            });

            // 查询用户票务
            List<TicketUserVO> ticketUsers =
                ticketUserService.queryChain().select(TICKET_USER.ID, TICKET_USER.STATUS, TICKET_USER.TICKET_ID)
                    .where(TICKET_USER.ORDER_ID.eq(order.getId())).listAs(TicketUserVO.class);

            Map<Long, TicketUserVO> ticketUserMap = new HashMap<>();
            ticketUsers.forEach(ticketUserDomain -> {
                ticketUserMap.put(ticketUserDomain.getId(), ticketUserDomain);
            });

            tickets.forEach(ticket -> {
                ticket.setTicketUsers(ticketUserMap.values().stream()
                    .filter(ticketUser -> ticketUser.getTicketId().equals(ticket.getId()))
                    .collect(Collectors.toList()));
            });
        }

        // 查询交易信息
        OrderTradeVO orderTrade = null;
        if (order.getOrderTradeId() != null) {
            orderTrade = orderTradeService.queryChain().select(ORDER_TRADE.ID, ORDER_TRADE.SN)
                .where(ORDER_TRADE.ID.eq(order.getOrderTradeId())).oneAs(OrderTradeVO.class);
        }

        // 组装商品和票务信息
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
        order.setOrderTrade(orderTrade);

        return order;
    }

    /**
     * 确认收货
     *
     * @param id order - id
     */
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
     * <p>
     * TODO 重新处理逻辑，有的地方用orderId，有的地方用OrderTradeId，统一下
     *
     * @param orderVO
     */
    @Transactional(rollbackFor = Exception.class)
    public void refund(OrderVO orderVO) {
        if (orderVO.getOrderTradeId() == null) {
            return;
        }

        OrderVO order =
            this.queryChain().select(ORDER_TRADE.SN, ORDER_TRADE.AMOUNT_PAID, ORDER.ID, ORDER.ORDER_TRADE_ID)
                .innerJoin(ORDER_TRADE).on(ORDER_TRADE.ID.eq(ORDER.ORDER_TRADE_ID))
                .where(ORDER.ORDER_TRADE_ID.eq(orderVO.getOrderTradeId())).oneAs(OrderVO.class);

        // 如果有票务，根据票务状态计算退款金额
        List<TicketUserDomain> ticketUsers =
            ticketUserService.queryChain().select(TICKET_USER.ID, TICKET_USER.ORDER_SKU_ID, TICKET_USER.STATUS)
                .where(TICKET_USER.ORDER_ID.eq(order.getId())).list();

        // TODO: 支持根据核销次数判断退款
        Long refundTotal = order.getAmountPaid();
        Long refundAmount = ticketUsers.size() > 0 ? 0 : order.getAmountPaid();
        if (ticketUsers.size() > 0) {
            List<OrderSkuDomain> orderSkus =
                orderSkuService.queryChain().select(ORDER_SKU.ID, ORDER_SKU.QUANTITY, ORDER_SKU.AMOUNT_PAID)
                    .where(ORDER_SKU.ORDER_ID.eq(order.getId())).list();

            // 未核销的可退款
            Map<Long, Integer> skuRefundCount = new HashMap<>(orderSkus.size());
            List<Long> refundTicketIds = new ArrayList<>(orderSkus.size());
            ticketUsers.forEach(ticketUserDomain -> {
                Integer refundCount = skuRefundCount.getOrDefault(ticketUserDomain.getOrderSkuId(), 0);

                if (ticketUserDomain.getStatus().equals(10)) {
                    skuRefundCount.put(ticketUserDomain.getOrderSkuId(), ++refundCount);

                    // 分销扣除
                    distributionSalesService.refundTicket(ticketUserDomain.getId());

                    refundTicketIds.add(ticketUserDomain.getId());
                }
            });

            if (refundTicketIds.size() > 0) {
                ticketUserService.updateChain().set(TICKET_USER.STATUS, 99).where(TICKET_USER.ID.in(refundTicketIds))
                    .update();
            }

            for (OrderSkuDomain orderSku : orderSkus) {
                Integer refundCount = skuRefundCount.get(orderSku.getId());

                if (refundCount <= 0) {
                    continue;
                }

                // 计算退款金额
                refundAmount += orderSku.getAmountPaid() / orderSku.getQuantity() * refundCount;
            }

        }

        // 微信退款
        Long applicationId = Context.applicationId();
        ApplicationPlatformPayDomain projectPlatformPayDomain = projectPlatformPayService.find(applicationId);
        wechat.refund(projectPlatformPayDomain.getWechatMerchantId(),
            projectPlatformPayDomain.getWechatMerchantCertificate(),
            projectPlatformPayDomain.getWechatMerchantCertificateSerial(),
            projectPlatformPayDomain.getWechatMerchantPublicKey(),
            projectPlatformPayDomain.getWechatMerchantPublicKeyId(), projectPlatformPayDomain.getWechatMerchantV3(),
            refundTotal, refundAmount, order.getSn(), order.getSn());

        // TODO: 支持一个交易单，多个订单情况
        super.updateChain().set(ORDER.TRADE_STATUS, 51).set(ORDER.REFUND_STATUS, 10)
            .set(ORDER.REFUND_AMOUNT, refundAmount).where(ORDER.ORDER_TRADE_ID.eq(orderVO.getOrderTradeId())).update();

        orderTradeService.updateChain().set(ORDER_TRADE.REFUND_AMOUNT, refundAmount)
            .where(ORDER_TRADE.ID.eq(orderVO.getOrderTradeId())).update();

    }

    /**
     * 取消订单
     *
     * @param id 订单ID
     */
    public void cancelOrder(Long id) {
        LoginUser loginUser = Context.loginUser();
        if (loginUser == null) {
            throw new ThrowPrompt("用户未登录");
        }

        OrderDomain orderDomain = this.getById(id);
        if (orderDomain == null) {
            throw new ThrowPrompt("订单不存在");
        }

        // 检查是否为订单创建人
        if (!loginUser.getId().equals(orderDomain.getCreatorId())) {
            throw new ThrowPrompt("无权限取消此订单");
        }

        // 检查订单状态，只有待支付状态(10)的订单才能取消
        if (!Integer.valueOf(10).equals(orderDomain.getTradeStatus())) {
            throw new ThrowPrompt("当前订单状态不允许取消");
        }

        // 更新订单状态为已取消(50)
        this.updateChain().set(ORDER.TRADE_STATUS, 50).where(ORDER.ID.eq(id)).update();
    }

    public PayVO payByOrderId(Long orderId, String outTradePlatform) {
        if (orderId == null) {
            throw new ThrowPrompt("订单ID不能为空");
        }

        if (VerifyUtils.isEmpty(outTradePlatform)) {
            throw new ThrowPrompt("支付平台不能为空");
        }

        Long loginUserId = Context.loginUser().getId();

        // 根据订单ID查询订单信息
        OrderDomain order = super.queryChain()
            .select(ORDER.ID, ORDER.ORDER_TRADE_ID, ORDER.TRADE_STATUS, ORDER.EXPIRED_AT, ORDER.CREATOR_ID)
            .where(ORDER.ID.eq(orderId)).and(ORDER.CREATOR_ID.eq(loginUserId)).one();

        if (order == null) {
            throw new ThrowPrompt("订单不存在或无权限访问");
        }

        // 检查订单状态
        if (!Integer.valueOf(10).equals(order.getTradeStatus())) {
            throw new ThrowPrompt("订单已支付或状态不允许支付");
        }

        // 检查订单是否过期
        if (order.getExpiredAt() != null && order.getExpiredAt().before(new Date())) {
            throw new ThrowPrompt("订单已过期，无法支付");
        }

        // 查询订单交易信息
        OrderTradeDomain orderTrade =
            orderTradeService.queryChain().select(ORDER_TRADE.ID, ORDER_TRADE.SN, ORDER_TRADE.AMOUNT_PAID)
                .where(ORDER_TRADE.ID.eq(order.getOrderTradeId())).one();

        if (orderTrade == null) {
            throw new ThrowPrompt("订单交易信息不存在");
        }

        // 执行支付逻辑
        Long applicationId = Context.applicationId();
        if (outTradePlatform.equals(PlatformEnum.WECHAT_APPLET.value)) {
            ApplicationPlatformPayDomain projectPlatformPayDomain = projectPlatformPayService.find(applicationId);
            ApplicationPlatformKeyDomain projectPlatformKeyDomain =
                projectPlatformKeyService.findByPlatform(PlatformEnum.WECHAT_APPLET);

            UserWechatDomain userWechatDomain =
                wechatService.queryChain().select(USER_WECHAT.OPENID).where(USER_WECHAT.USER_ID.eq(loginUserId))
                    .and(USER_WECHAT.APPLICATION_ID.eq(applicationId)).one();

            if (userWechatDomain == null) {
                throw new ThrowPrompt("未找到微信用户信息");
            }

            PayVO payVO = new PayVO();
            String prepay_id =
                wechat.pay(projectPlatformKeyDomain.getAppid(), projectPlatformPayDomain.getWechatMerchantId(),
                    projectPlatformPayDomain.getWechatMerchantCertificate(),
                    projectPlatformPayDomain.getWechatMerchantCertificateSerial(),
                    projectPlatformPayDomain.getWechatMerchantPublicKey(),
                    projectPlatformPayDomain.getWechatMerchantPublicKeyId(),
                    projectPlatformPayDomain.getWechatMerchantV3(), userWechatDomain.getOpenid(), "商品",
                    orderTrade.getSn(), Integer.valueOf(orderTrade.getAmountPaid().toString()));
            payVO.setPrepayId(prepay_id);

            Long nonceStr = System.currentTimeMillis();

            // 获取商户私钥并进行签名
            try {
                Signature sign = Signature.getInstance("SHA256withRSA");

                File file = ResourceUtils.getFile(projectPlatformPayDomain.getWechatMerchantCertificate());
                PrivateKey privateKey = PemUtil.loadPrivateKey(new FileInputStream(file));
                sign.initSign(privateKey);

                // 应用id
                String sb = projectPlatformKeyDomain.getAppid() + "\n"
                    // 支付签名时间戳
                    + nonceStr + "\n"
                    // 随机字符串
                    + nonceStr + "\n"
                    // 预支付交易会话ID
                    + "prepay_id=" + prepay_id + "\n";
                sign.update(sb.getBytes(StandardCharsets.UTF_8));

                String paySign = Base64.getEncoder().encodeToString(sign.sign());
                payVO.setPaySign(paySign);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            payVO.setSignType("RSA");
            payVO.setTimeStamp(nonceStr.toString());
            payVO.setNonceStr(nonceStr.toString());
            return payVO;
        }

        return null;
    }
}

package com.pighand.aio.service.distribution;

import com.mybatisflex.core.query.QueryMethods;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.pighand.aio.domain.ECommerce.TicketDomain;
import com.pighand.aio.domain.distribution.DistributionGoodsRuleDomain;
import com.pighand.aio.domain.distribution.DistributionSalesDetailDomain;
import com.pighand.aio.domain.distribution.DistributionSalesDomain;
import com.pighand.aio.domain.distribution.DistributionSalespersonDomain;
import com.pighand.aio.mapper.distribution.DistributionSalesDetailMapper;
import com.pighand.aio.mapper.distribution.DistributionSalesMapper;
import com.pighand.aio.service.ECommerce.TicketService;
import com.pighand.aio.service.distribution.DistributionGoodsRuleService;
import com.pighand.aio.service.distribution.DistributionSalespersonService;
import com.pighand.aio.vo.ECommerce.TicketVO;
import com.pighand.aio.vo.distribution.DistributionSalesVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.exception.ThrowPrompt;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.pighand.aio.domain.ECommerce.table.OrderSkuTableDef.ORDER_SKU;
import static com.pighand.aio.domain.ECommerce.table.OrderTableDef.ORDER;
import static com.pighand.aio.domain.ECommerce.table.ThemeTableDef.THEME;
import static com.pighand.aio.domain.ECommerce.table.TicketTableDef.TICKET;
import static com.pighand.aio.domain.distribution.table.DistributionGoodsRuleTableDef.DISTRIBUTION_GOODS_RULE;
import static com.pighand.aio.domain.distribution.table.DistributionSalesDetailTableDef.DISTRIBUTION_SALES_DETAIL;
import static com.pighand.aio.domain.distribution.table.DistributionSalesTableDef.DISTRIBUTION_SALES;

/**
 * 分销 - 销售记录
 *
 * @author wangshuli
 * @createDate 2025-05-24 14:35:05
 */
@Service
@RequiredArgsConstructor
public class DistributionSalesService extends BaseServiceImpl<DistributionSalesMapper, DistributionSalesDomain>
     {

    private final DistributionSalespersonService distributionSalespersonService;

    private final DistributionGoodsRuleService distributionGoodsRuleService;

    private final TicketService ticketService;

    private final DistributionSalesDetailMapper distributionSalesDetailMapper;

    /**
     * 创建
     *
     * @param distDistributionSalesVO
     * @return
     */
    public DistributionSalesVO create(DistributionSalesVO distDistributionSalesVO) {
        // 如果类型=20（已结算），需要校验金额是否够
        if (distDistributionSalesVO.getType() != null && distDistributionSalesVO.getType() == 20) {
            validateSettlementAmount(distDistributionSalesVO);
        }

        distDistributionSalesVO.setCreatedAt(new Date());
        super.mapper.insert(distDistributionSalesVO);

        DistributionSalesDetailDomain detailDomain = new DistributionSalesDetailDomain();
        detailDomain.setWithdrawDistributionSalesId(distDistributionSalesVO.getId());
        detailDomain.setStatus(20);
        distributionSalesDetailMapper.updateByQuery(detailDomain, QueryWrapper.create()
            .where(DISTRIBUTION_SALES_DETAIL.ID.in(distDistributionSalesVO.getSettledDetailIds())));

        return distDistributionSalesVO;
    }

    /**
     * 校验结算金额是否足够
     * 金额计算方式：状态=20和10的amount的和
     *
     * @param distDistributionSalesVO
     */
    private void validateSettlementAmount(DistributionSalesVO distDistributionSalesVO) {
        if (distDistributionSalesVO.getSalespersonId() == null) {
            throw new RuntimeException("销售ID不能为空");
        }

        // 查询该销售记录
        QueryWrapper queryWrapper =
            QueryWrapper.create().select(QueryMethods.sum(DISTRIBUTION_SALES_DETAIL.AMOUNT).as("amount"))
                .from(DISTRIBUTION_SALES_DETAIL)
                .where(DISTRIBUTION_SALES_DETAIL.ID.in(distDistributionSalesVO.getSettledDetailIds()))
                .and(DISTRIBUTION_SALES_DETAIL.STATUS.eq(10))
                .and(DISTRIBUTION_SALES_DETAIL.SETTLEMENT_TIME.le(new Date()));

        DistributionSalesDetailDomain distributionSalesDetails =
            distributionSalesDetailMapper.selectOneByQuery(queryWrapper);

        // 校验：可用余额 + 当前结算金额 >= 0
        if (distributionSalesDetails == null || distributionSalesDetails.getAmount() == null ||
            distributionSalesDetails.getAmount().multiply(new BigDecimal(-1))
                .compareTo(distDistributionSalesVO.getSettledAmount()) != 0) {
            throw new ThrowPrompt("结算金额错误");
        }
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public DistributionSalesDomain find(Long id) {
        return super.mapper.find(id);
    }

    /**
     * 分页或列表
     *
     * @param distDistributionSalesVO
     * @return PageOrList<DistDistributionSalesVO>
     */
    public PageOrList<DistributionSalesVO> query(DistributionSalesVO distDistributionSalesVO) {
        distDistributionSalesVO.setJoinTables(ORDER.getName(), ORDER_SKU.getName());

        QueryWrapper queryWrapper = QueryWrapper.create()
            .select(DISTRIBUTION_SALES.ID, DISTRIBUTION_SALES.ORDER_ID, DISTRIBUTION_SALES.FROZEN_AMOUNT,
                DISTRIBUTION_SALES.REFUND_AMOUNT, DISTRIBUTION_SALES.SETTLED_AMOUNT, DISTRIBUTION_SALES.TYPE,
                DISTRIBUTION_SALES.CREATED_AT)

            .orderBy(DISTRIBUTION_SALES.ID.desc())

            // equal
            .and(DISTRIBUTION_SALES.SALESPERSON_ID.eq(distDistributionSalesVO.getSalespersonId()))
            .and(DISTRIBUTION_SALES.ORDER_ID.eq(distDistributionSalesVO.getOrderId()))
            .and(DISTRIBUTION_SALES.SETTLEMENT_ID.eq(distDistributionSalesVO.getSettlementId()))
            .and(DISTRIBUTION_SALES.TYPE.eq(distDistributionSalesVO.getType()));

        PageOrList<DistributionSalesVO> result = super.mapper.query(distDistributionSalesVO, queryWrapper);

        List<Long> ticketIds = new ArrayList<>();
        result.getRecords().forEach(distributionSalesVO -> {
            if (distributionSalesVO.getOrderSku() == null) {
                return;
            }

            distributionSalesVO.getOrderSku().forEach(orderSku -> {
                if (orderSku.getTicketId() != null) {
                    ticketIds.add(orderSku.getTicketId());
                }
            });
        });

        Map<Long, TicketVO> ticketMap = new HashMap<>();
        if (ticketIds.size() > 0) {
            List<TicketVO> tickets =
                ticketService.queryChain().select(TICKET.ID, TICKET.NAME, THEME.POSTER_URL).leftJoin(THEME)
                    .on(TICKET.THEME_ID.eq(THEME.ID)).where(TICKET.ID.in(ticketIds)).listAs(TicketVO.class);

            tickets.forEach(ticketDomain -> {
                ticketMap.put(ticketDomain.getId(), ticketDomain);
            });
        }

        result.getRecords().forEach(distributionSalesVO -> {
            List<TicketVO> tickets = new ArrayList<>();

            if (distributionSalesVO.getOrderSku() != null) {
                distributionSalesVO.getOrderSku().forEach(orderSku -> {
                    if (orderSku.getTicketId() != null) {
                        tickets.add(ticketMap.get(orderSku.getTicketId()));
                    }
                });
            }

            distributionSalesVO.setTickets(tickets);
        });

        return result;
    }

    /**
     * 查询明细
     * TODO: 校验用户id
     *
     * @param distributionSalesId
     * @return
     */
    public List<DistributionSalesDetailDomain> queryDetail(Long distributionSalesId) {
        QueryWrapper queryWrapper =
            QueryWrapper.create().where(DISTRIBUTION_SALES_DETAIL.DISTRIBUTION_SALES_ID.eq(distributionSalesId));
        return distributionSalesDetailMapper.selectListByQuery(queryWrapper);
    }

    /**
     * 修改
     *
     * @param distDistributionSalesVO
     */
    public void update(DistributionSalesVO distDistributionSalesVO) {
        UpdateChain updateChain = this.updateChain().where(DISTRIBUTION_SALES.ID.eq(distDistributionSalesVO.getId()));

        updateChain.set(DISTRIBUTION_SALES.ID, distDistributionSalesVO.getId(), VerifyUtils::isNotEmpty);

        updateChain.update();
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(Long id) {
        super.mapper.deleteById(id);
    }

    /**
     * 根据结算ID统计各个状态的总数
     *
     * @return Map<String, Object> 状态统计结果
     */
    public Map<String, Object> statistics(DistributionSalesVO vo) {
        Long salespersonId = vo.getSalespersonId();

        if (salespersonId == null && vo.getUserId() != null) {
            DistributionSalespersonDomain person = distributionSalespersonService.findByUserId(vo.getUserId());
            salespersonId = person != null ? person.getId() : null;
        }

        if (salespersonId == null) {
            throw new ThrowPrompt("用户没有分销资格");
        }

        final Long finalSalespersonId = salespersonId;

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("frozenAmount", 0L);
        resultMap.put("incomingAmount", 0L);
        resultMap.put("settledAmount", 0L);
        resultMap.put("refundAmount", 0L);
        resultMap.put("withdrawAmount", 0L);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            Future<DistributionSalesVO> frozenFuture = executor.submit(() -> {
                QueryWrapper queryWrapper = QueryWrapper.create()
                    .select(QueryMethods.sum(DISTRIBUTION_SALES.FROZEN_AMOUNT).as("frozenAmount"),
                        QueryMethods.sum(DISTRIBUTION_SALES.REFUND_AMOUNT).as("refundAmount")).from(DISTRIBUTION_SALES)
                    .where(DISTRIBUTION_SALES.SALESPERSON_ID.eq(finalSalespersonId))
                    .and(DISTRIBUTION_SALES.TYPE.eq(10));
                return super.mapper.selectOneByQueryAs(queryWrapper, DistributionSalesVO.class);
            });

            Future<DistributionSalesVO> withdrawFuture = executor.submit(() -> {
                QueryWrapper queryWithdrawWrapper = QueryWrapper.create()
                    .select(QueryMethods.sum(DISTRIBUTION_SALES.SETTLED_AMOUNT).as("settledAmount"))
                    .from(DISTRIBUTION_SALES).where(DISTRIBUTION_SALES.SALESPERSON_ID.eq(finalSalespersonId))
                    .and(DISTRIBUTION_SALES.TYPE.eq(20));
                return super.mapper.selectOneByQueryAs(queryWithdrawWrapper, DistributionSalesVO.class);
            });

            Future<List<DistributionSalesDetailDomain>> detailFuture = executor.submit(() -> {
                QueryWrapper queryDetailWrapper = QueryWrapper.create()
                    .select(DISTRIBUTION_SALES_DETAIL.ID, DISTRIBUTION_SALES_DETAIL.AMOUNT,
                        DISTRIBUTION_SALES_DETAIL.SETTLEMENT_TIME).from(DISTRIBUTION_SALES_DETAIL)
                    .where(DISTRIBUTION_SALES_DETAIL.SALESPERSON_ID.eq(finalSalespersonId))
                    .and(DISTRIBUTION_SALES_DETAIL.STATUS.eq(10));
                return distributionSalesDetailMapper.selectListByQuery(queryDetailWrapper);
            });

            // 等待结果
            DistributionSalesVO frozen = frozenFuture.get();
            DistributionSalesVO withdraw = withdrawFuture.get();
            List<DistributionSalesDetailDomain> details = detailFuture.get();

            // 处理统计逻辑
            resultMap.put("frozenAmount",
                Optional.ofNullable(frozen).map(DistributionSalesVO::getFrozenAmount).orElse(BigDecimal.ZERO)
                    .longValue());
            resultMap.put("refundAmount",
                Optional.ofNullable(frozen).map(DistributionSalesVO::getRefundAmount).orElse(BigDecimal.ZERO)
                    .longValue());
            resultMap.put("withdrawAmount",
                Optional.ofNullable(withdraw).map(DistributionSalesVO::getSettledAmount).orElse(BigDecimal.ZERO)
                    .longValue());

            BigDecimal incomingAmount = BigDecimal.ZERO;
            BigDecimal settledAmount = BigDecimal.ZERO;
            List<String> settledDetailIds = new ArrayList<>();
            Date now = new Date();

            for (DistributionSalesDetailDomain detail : details) {
                if (detail.getSettlementTime() != null && detail.getSettlementTime().after(now)) {
                    incomingAmount = incomingAmount.add(detail.getAmount());
                } else {
                    settledAmount = settledAmount.add(detail.getAmount());
                    settledDetailIds.add(String.valueOf(detail.getId()));
                }
            }

            resultMap.put("incomingAmount", incomingAmount.longValue());
            resultMap.put("settledAmount", settledAmount.longValue());
            resultMap.put("settledDetailIds", settledDetailIds);

        } catch (Exception e) {
            throw new RuntimeException("统计失败", e);
        }

        return resultMap;
    }

    /**
     * 计算价格预估
     * <p>
     * TODO 票同时只会买1中票，没必要用map
     *
     * @param objectType
     * @param objectIds
     * @return
     */
    public Map<Long, BigDecimal> calculatePriceEstimate(Integer objectType, Set<Long> objectIds) {
        List<DistributionGoodsRuleDomain> distributionSales = distributionGoodsRuleService.queryChain()
            .select(DISTRIBUTION_GOODS_RULE.OBJECT_ID, DISTRIBUTION_GOODS_RULE.SHARING_TYPE,
                DISTRIBUTION_GOODS_RULE.SHARING_RATIO, DISTRIBUTION_GOODS_RULE.SHARING_PRICE)
            .where(DISTRIBUTION_GOODS_RULE.OBJECT_TYPE.eq(objectType))
            .and(DISTRIBUTION_GOODS_RULE.OBJECT_ID.in(objectIds)).list();

        Map<Long, BigDecimal> amountMap = new HashMap(objectIds.size());
        if (VerifyUtils.isEmpty(distributionSales)) {
            return amountMap;
        }

        Map<Long, DistributionGoodsRuleDomain> distributionSalesMap = distributionSales.stream()
            .collect(Collectors.toMap(DistributionGoodsRuleDomain::getObjectId, Function.identity()));

        if (objectType.equals(20)) {
            List<TicketDomain> tickets = ticketService.queryChain().select(TICKET.ID, TICKET.CURRENT_PRICE)
                .where(TICKET.ID.in(distributionSalesMap.keySet())).list();

            for (TicketDomain ticketDomain : tickets) {
                DistributionGoodsRuleDomain distributionGoodsRule = distributionSalesMap.get(ticketDomain.getId());

                if (distributionGoodsRule == null) {
                    continue;
                }

                BigDecimal amount = BigDecimal.ZERO;
                if (distributionGoodsRule.getSharingType().equals(10)) {
                    amount = BigDecimal.valueOf(ticketDomain.getCurrentPrice()).divide(BigDecimal.valueOf(100))
                        .multiply(distributionGoodsRule.getSharingRatio());
                } else {
                    amount = distributionGoodsRule.getSharingPrice();
                }

                amountMap.put(ticketDomain.getId(), amount);
            }
        }

        return amountMap;
    }

    /**
     * 创建票务分销记录
     *
     * @param salespersonId
     * @param orderId
     * @param ticketId
     * @param ticketUserIds
     */
    public void createTicket(Long salespersonId, Long orderId, Long ticketId, List<Long> ticketUserIds) {
        if (salespersonId == null) {
            return;
        }

        Set<Long> objectIds = new HashSet<>(1);
        objectIds.add(ticketId);
        Map<Long, BigDecimal> amountMap = calculatePriceEstimate(20, objectIds);

        // 无分销规则，不创建分销记录
        BigDecimal amount = amountMap.get(ticketId);
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            return;
        }

        DistributionSalesDomain distributionSalesDomain = new DistributionSalesDomain();
        distributionSalesDomain.setSalespersonId(salespersonId);
        distributionSalesDomain.setOrderId(orderId);
        distributionSalesDomain.setFrozenAmount(amount.multiply(BigDecimal.valueOf(ticketUserIds.size())));
        distributionSalesDomain.setType(10);
        distributionSalesDomain.setCreatedAt(new Date());
        super.mapper.insert(distributionSalesDomain);

        for (Long ticketUserId : ticketUserIds) {
            DistributionSalesDetailDomain distributionSalesDetail = new DistributionSalesDetailDomain();
            distributionSalesDetail.setSalespersonId(salespersonId);
            distributionSalesDetail.setDistributionSalesId(distributionSalesDomain.getId());
            distributionSalesDetail.setObjectType(20);
            distributionSalesDetail.setObjectId(ticketUserId);
            distributionSalesDetail.setAmount(amount);
            distributionSalesDetail.setStatus(0);
            distributionSalesDetailMapper.insert(distributionSalesDetail);
        }
    }

    /**
     * 解冻
     *
     * @param ticketUserId
     */
    public void thawTicket(Long ticketUserId) {
        QueryWrapper queryWrapper = QueryWrapper.create()
            .select(DISTRIBUTION_SALES_DETAIL.ID, DISTRIBUTION_SALES_DETAIL.DISTRIBUTION_SALES_ID,
                DISTRIBUTION_SALES_DETAIL.STATUS, DISTRIBUTION_SALES_DETAIL.AMOUNT).from(DISTRIBUTION_SALES_DETAIL)
            .where(DISTRIBUTION_SALES_DETAIL.OBJECT_TYPE.eq(20))
            .and(DISTRIBUTION_SALES_DETAIL.OBJECT_ID.eq(ticketUserId));
        DistributionSalesDetailDomain distributionSalesDetail =
            distributionSalesDetailMapper.selectOneByQuery(queryWrapper);

        if (distributionSalesDetail == null || distributionSalesDetail.getAmount() == null) {
            return;
        }

        distributionSalesDetail.setStatus(10);

        // 可结算为当前时间+24小时
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, 24);
        distributionSalesDetail.setSettlementTime(calendar.getTime());

        distributionSalesDetailMapper.update(distributionSalesDetail);

        DistributionSalesDomain distributionSalesDomain =
            super.mapper.find(distributionSalesDetail.getDistributionSalesId());
        if (distributionSalesDomain == null) {
            return;
        }

        // 处理冻结金额，如果为null则设为0
        BigDecimal currentFrozenAmount =
            distributionSalesDomain.getFrozenAmount() != null ? distributionSalesDomain.getFrozenAmount() :
                BigDecimal.ZERO;
        distributionSalesDomain.setFrozenAmount(currentFrozenAmount.subtract(distributionSalesDetail.getAmount()));

        // 处理已结算金额，如果为null则设为0
        BigDecimal currentSettledAmount =
            distributionSalesDomain.getSettledAmount() != null ? distributionSalesDomain.getSettledAmount() :
                BigDecimal.ZERO;
        distributionSalesDomain.setSettledAmount(currentSettledAmount.add(distributionSalesDetail.getAmount()));

        super.mapper.update(distributionSalesDomain);
    }

    /**
     * 退票
     *
     * @param ticketUserId
     */
    public void refundTicket(Long ticketUserId) {
        QueryWrapper queryWrapper = QueryWrapper.create()
            .select(DISTRIBUTION_SALES_DETAIL.ID, DISTRIBUTION_SALES_DETAIL.STATUS, DISTRIBUTION_SALES_DETAIL.AMOUNT,
                DISTRIBUTION_SALES_DETAIL.DISTRIBUTION_SALES_ID).from(DISTRIBUTION_SALES_DETAIL)
            .where(DISTRIBUTION_SALES_DETAIL.OBJECT_TYPE.eq(20))
            .and(DISTRIBUTION_SALES_DETAIL.OBJECT_ID.eq(ticketUserId));
        DistributionSalesDetailDomain distributionSalesDetail =
            distributionSalesDetailMapper.selectOneByQuery(queryWrapper);

        if (distributionSalesDetail == null || distributionSalesDetail.getAmount() == null) {
            return;
        }

        Integer status = distributionSalesDetail.getStatus();

        // 将明细状态设置为退款状态
        distributionSalesDetail.setStatus(90);
        distributionSalesDetailMapper.update(distributionSalesDetail);

        DistributionSalesDomain distributionSalesDomain =
            super.mapper.find(distributionSalesDetail.getDistributionSalesId());
        if (distributionSalesDomain == null) {
            return;
        }

        // 根据当前明细的状态来处理退款逻辑
        if (status.equals(0)) {
            // 如果是冻结状态，从冻结金额中减去
            BigDecimal currentFrozenAmount =
                distributionSalesDomain.getFrozenAmount() != null ? distributionSalesDomain.getFrozenAmount() :
                    BigDecimal.ZERO;
            distributionSalesDomain.setFrozenAmount(currentFrozenAmount.subtract(distributionSalesDetail.getAmount()));
        } else if (status.equals(10)) {
            // 如果是已结算状态，从已结算金额中减去
            BigDecimal currentSettledAmount =
                distributionSalesDomain.getSettledAmount() != null ? distributionSalesDomain.getSettledAmount() :
                    BigDecimal.ZERO;
            distributionSalesDomain.setSettledAmount(
                currentSettledAmount.subtract(distributionSalesDetail.getAmount()));
        }

        // 增加退款金额
        BigDecimal currentRefundAmount =
            distributionSalesDomain.getRefundAmount() != null ? distributionSalesDomain.getRefundAmount() :
                BigDecimal.ZERO;
        distributionSalesDomain.setRefundAmount(currentRefundAmount.add(distributionSalesDetail.getAmount()));

        super.mapper.update(distributionSalesDomain);
    }

    /**
     * 冻结票务
     *
     * @param ticketUserId
     */
    public void freezeTicket(Long ticketUserId) {
        QueryWrapper queryWrapper = QueryWrapper.create()
            .select(DISTRIBUTION_SALES_DETAIL.ID, DISTRIBUTION_SALES_DETAIL.DISTRIBUTION_SALES_ID,
                DISTRIBUTION_SALES_DETAIL.STATUS, DISTRIBUTION_SALES_DETAIL.AMOUNT).from(DISTRIBUTION_SALES_DETAIL)
            .where(DISTRIBUTION_SALES_DETAIL.OBJECT_TYPE.eq(20))
            .and(DISTRIBUTION_SALES_DETAIL.OBJECT_ID.eq(ticketUserId));
        DistributionSalesDetailDomain distributionSalesDetail =
            distributionSalesDetailMapper.selectOneByQuery(queryWrapper);

        if (distributionSalesDetail == null || distributionSalesDetail.getAmount() == null) {
            return;
        }

        if (distributionSalesDetail.getStatus().equals(90)) {
            throw new ThrowPrompt("已退款");
        } else if (distributionSalesDetail.getStatus().equals(20)) {
            throw new ThrowPrompt("分销已提现");
        } else if (!distributionSalesDetail.getStatus().equals(10)) {
            throw new ThrowPrompt("分销非结算状态");
        }

        // 将明细状态设置为冻结状态
        // TODO: 移到service中，或者使用updateChain，方式设置其他字段为null
        distributionSalesDetail.setStatus(0);
        distributionSalesDetail.setSettlementTime(null); // 清除结算时间
        distributionSalesDetailMapper.update(distributionSalesDetail, true);

        DistributionSalesDomain distributionSalesDomain =
            super.mapper.find(distributionSalesDetail.getDistributionSalesId());
        if (distributionSalesDomain == null) {
            return;
        }

        // 处理已结算金额，从已结算金额中减去
        BigDecimal currentSettledAmount =
            distributionSalesDomain.getSettledAmount() != null ? distributionSalesDomain.getSettledAmount() :
                BigDecimal.ZERO;
        distributionSalesDomain.setSettledAmount(currentSettledAmount.subtract(distributionSalesDetail.getAmount()));

        // 处理冻结金额，增加到冻结金额中
        BigDecimal currentFrozenAmount =
            distributionSalesDomain.getFrozenAmount() != null ? distributionSalesDomain.getFrozenAmount() :
                BigDecimal.ZERO;
        distributionSalesDomain.setFrozenAmount(currentFrozenAmount.add(distributionSalesDetail.getAmount()));

        super.mapper.update(distributionSalesDomain);
    }
}

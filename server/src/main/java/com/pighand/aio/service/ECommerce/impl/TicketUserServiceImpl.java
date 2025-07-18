package com.pighand.aio.service.ECommerce.impl;

import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.domain.ECommerce.OrderDomain;
import com.pighand.aio.domain.ECommerce.TicketUserDomain;
import com.pighand.aio.domain.IoT.DeviceDomain;
import com.pighand.aio.mapper.ECommerce.OrderMapper;
import com.pighand.aio.mapper.ECommerce.TicketUserMapper;
import com.pighand.aio.service.ECommerce.TicketUserService;
import com.pighand.aio.service.ECommerce.TicketUserValidityService;
import com.pighand.aio.service.IoT.DeviceService;
import com.pighand.aio.service.IoT.DeviceTaskService;
import com.pighand.aio.service.distribution.DistributionSalesService;
import com.pighand.aio.vo.ECommerce.TicketUserVO;
import com.pighand.aio.vo.ECommerce.TicketUserValidityVO;
import com.pighand.aio.vo.ECommerce.TicketValidityDetailEntity;
import com.pighand.aio.vo.IoT.DeviceTaskVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.exception.ThrowPrompt;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.pighand.aio.domain.ECommerce.table.ThemeTableDef.THEME;
import static com.pighand.aio.domain.ECommerce.table.TicketTableDef.TICKET;
import static com.pighand.aio.domain.ECommerce.table.TicketUserTableDef.TICKET_USER;
import static com.pighand.aio.domain.ECommerce.table.TicketUserValidityTableDef.TICKET_USER_VALIDITY;
import static com.pighand.aio.domain.ECommerce.table.TicketValidityTableDef.TICKET_VALIDITY;
import static com.pighand.aio.domain.IoT.table.DeviceTableDef.DEVICE;

/**
 * 电商 - 已购票务
 *
 * @author wangshuli
 * @createDate 2024-04-26 14:52:18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TicketUserServiceImpl extends BaseServiceImpl<TicketUserMapper, TicketUserDomain>
    implements TicketUserService {

    private final DeviceService deviceService;

    private final DeviceTaskService deviceTaskService;

    private final TicketUserValidityService ticketUserValidityService;

    private final DistributionSalesService distributionSalesService;

    private final OrderMapper orderMapper;

    /**
     * 创建
     *
     * @param ticketUserVO
     * @return
     */
    @Override
    public TicketUserVO create(TicketUserVO ticketUserVO) {
        ticketUserVO.setStatus(10);
        if (ticketUserVO.getValidationCount() == null) {
            ticketUserVO.setValidationCount(1);
        }

        super.mapper.insert(ticketUserVO);

        return ticketUserVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public TicketUserDomain find(Long id) {
        return super.mapper.find(id);
    }

    /**
     * 分页或列表
     *
     * @param ticketUserVO
     * @return PageOrList<TicketUserVO>
     */
    @Override
    public PageOrList<TicketUserVO> query(TicketUserVO ticketUserVO) {
        ticketUserVO.setJoinTables(TICKET.getTableName(), THEME.getTableName());

        // TODO: TICKET.NAME 在mapper中查询无效
        QueryWrapper queryWrapper = QueryWrapper.create()
            .select(TICKET_USER.ID, TICKET_USER.STATUS, TICKET_USER.VALIDATION_AT,
                TICKET_USER.REMAINING_VALIDATION_COUNT, TICKET.NAME)

            // equal
            .and(TICKET_USER.TICKET_ID.eq(ticketUserVO.getTicketId()))
            .and(TICKET_USER.ORDER_ID.eq(ticketUserVO.getOrderId()))
            .and(TICKET_USER.REMAINING_VALIDATION_COUNT.eq(ticketUserVO.getRemainingValidationCount()))
            .and(TICKET_USER.CREATOR_ID.eq(ticketUserVO.getCreatorId()));

        // 查询可用票务
        if (ticketUserVO.getUsable() != null) {
            if (ticketUserVO.getUsable()) {
                queryWrapper.and(TICKET_USER.STATUS.eq(10));
                queryWrapper.orderBy(TICKET_USER.ID.desc());
            } else {
                // TODO: 支持查询多次核销。取消核销时候也支持多次的情况
                queryWrapper.and(TICKET_USER.STATUS.eq(20));
                queryWrapper.orderBy(TICKET_USER.VALIDATION_AT.desc());
            }
        } else if (ticketUserVO.getUsed() != null && ticketUserVO.getUsed()) {
            queryWrapper.and(TICKET_USER.STATUS.eq(20));
            queryWrapper.orderBy(TICKET_USER.ID.desc());
        } else {
            queryWrapper.orderBy(TICKET_USER.STATUS.asc(), TICKET_USER.ID.desc());
        }

        PageOrList<TicketUserVO> result = super.mapper.query(ticketUserVO, queryWrapper);

        List<Long> ids = result.getRecords().stream().map(TicketUserVO::getId).collect(Collectors.toList());

        if (ids.size() > 0) {
            // 查询适用信息
            List<TicketUserValidityVO> ticketValidities = ticketUserValidityService.queryChain()
                .select(TICKET_USER_VALIDITY.ID, TICKET_USER_VALIDITY.TICKET_USER_ID, TICKET_USER_VALIDITY.TICKET_ID,
                    TICKET_USER_VALIDITY.VALIDATION_COUNT, TICKET_VALIDITY.VALIDITY_IDS,
                    TICKET_VALIDITY.VALIDITY_CONFIG).leftJoin(TICKET_VALIDITY)
                .on(TICKET_VALIDITY.ID.eq(TICKET_USER_VALIDITY.TICKET_VALIDITY_ID))
                .where(TICKET_USER_VALIDITY.TICKET_USER_ID.in(ids)).and(TICKET_USER_VALIDITY.VALIDATION_COUNT.gt(0))
                .listAs(TicketUserValidityVO.class);

            List<Long> validyIds = new ArrayList<>();
            ticketValidities.forEach(ticketValidity -> {
                validyIds.addAll(ticketValidity.getValidityIds());
            });

            // 查询适用信息
            if (validyIds.size() > 0) {

                List<DeviceDomain> devices =
                    deviceService.queryChain().select(DEVICE.ID, DEVICE.DESCRIPTION, DEVICE.CONFIG)
                        .where(DEVICE.ID.in(validyIds)).list();
                Map<Long, TicketValidityDetailEntity> deviceMap =
                    devices.stream().collect(Collectors.toMap(DeviceDomain::getId, device -> {
                        TicketValidityDetailEntity ticketValidityDetailEntity = new TicketValidityDetailEntity();
                        ticketValidityDetailEntity.setId(device.getId());
                        ticketValidityDetailEntity.setName(device.getDescription());
                        ticketValidityDetailEntity.setConfig(device.getConfig());

                        return ticketValidityDetailEntity;
                    }));

                Map<Long, List<TicketUserValidityVO>> ticketValidityMap = new HashMap<>();
                ticketValidities.forEach(ticketValidity -> {
                    // 回填适用信息
                    List<TicketValidityDetailEntity> ticketValidityDetailEntities = new ArrayList<>();
                    ticketValidity.getValidityIds().forEach(validityId -> {
                        TicketValidityDetailEntity ticketValidityDetailEntity = deviceMap.get(validityId);
                        if (ticketValidityDetailEntity != null) {
                            ticketValidityDetailEntities.add(ticketValidityDetailEntity);
                        }
                    });
                    ticketValidity.setValidityDetail(ticketValidityDetailEntities);

                    List<TicketUserValidityVO> ticketValidityVOS =
                        ticketValidityMap.get(ticketValidity.getTicketUserId());
                    if (ticketValidityVOS == null) {
                        ticketValidityVOS = new ArrayList<>();
                    }
                    ticketValidityVOS.add(ticketValidity);

                    ticketValidityMap.put(ticketValidity.getTicketUserId(), ticketValidityVOS);
                });

                result.getRecords().forEach(resultItem -> {
                    resultItem.setValidity(ticketValidityMap.get(resultItem.getId()));
                });
            }
        }

        return result;
    }

    /**
     * 修改
     *
     * @param ticketUserVO
     */
    @Override
    public void update(TicketUserVO ticketUserVO) {
        super.mapper.update(ticketUserVO);
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        super.mapper.deleteById(id);
    }

    /**
     * 核销
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void validation(TicketUserVO ticketUserVO) {
        TicketUserDomain ticketUserDomain = this.getById(ticketUserVO.getId());
        if (ticketUserDomain == null) {
            throw new ThrowPrompt("票务不存在");
        }

        if (ticketUserDomain.getRemainingValidationCount() <= 0) {
            throw new ThrowPrompt("票务已核销");
        }

        // 校验适配信息
        List<TicketUserValidityVO> validity = ticketUserValidityService.queryChain()
            .select(TICKET_USER_VALIDITY.ID, TICKET_USER_VALIDITY.VALIDATION_COUNT, TICKET_VALIDITY.VALIDITY_IDS,
                TICKET_VALIDITY.VALIDITY_CONFIG).innerJoin(TICKET_VALIDITY)
            .on(TICKET_VALIDITY.ID.eq(TICKET_USER_VALIDITY.TICKET_VALIDITY_ID))
            .where(TICKET_USER_VALIDITY.TICKET_USER_ID.eq(ticketUserDomain.getId()))
            .and(TICKET_USER_VALIDITY.VALIDATION_COUNT.gt(0)).listAs(TicketUserValidityVO.class);
        if (validity.size() > 0) {
            TicketUserValidityVO ticketUserValidityVO = validity.stream().filter(item -> {
                if (item.getValidityIds() == null) {
                    return true;
                }

                boolean isContains = item.getValidityIds().contains(ticketUserVO.getDeviceId());

                if (!isContains) {
                    return false;
                }

                if (item.getValidityConfig() == null) {
                    return true;
                } else if (VerifyUtils.isNotEmpty(ticketUserVO.getMessage())) {
                    return
                        item.getValidityConfig().stream().filter(config -> ticketUserVO.getMessage().startsWith(config))
                            .count() > 0;
                }

                return false;
            }).findFirst().orElse(null);

            if (ticketUserValidityVO != null) {
                // TODO: 高并发，或开多端，需要加锁
                boolean isUpdate = ticketUserValidityService.updateChain()
                    .set(TICKET_USER_VALIDITY.VALIDATION_COUNT, TICKET_USER_VALIDITY.VALIDATION_COUNT.subtract(1))
                    .where(TICKET_USER_VALIDITY.ID.eq(ticketUserValidityVO.getId()))
                    .and(TICKET_USER_VALIDITY.VALIDATION_COUNT.gt(0)).update();

                if (!isUpdate) {
                    throw new ThrowPrompt("核销失败");
                }
            }
        }

        Integer validationCount = Optional.ofNullable(ticketUserVO.getValidationCount()).orElse(1);
        // 数量减一
        UpdateChain updateChain = this.updateChain().set(TICKET_USER.REMAINING_VALIDATION_COUNT,
                TICKET_USER.REMAINING_VALIDATION_COUNT.subtract(validationCount)).set(TICKET_USER.VALIDATION_AT, new Date())
            .where(TICKET_USER.ID.eq(ticketUserVO.getId())).and(TICKET_USER.REMAINING_VALIDATION_COUNT.gt(0));

        if (ticketUserDomain.getRemainingValidationCount().equals(1)) {
            updateChain.set(TICKET_USER.STATUS, 20);
        }

        boolean isUpdate = updateChain.update();
        if (!isUpdate) {
            throw new ThrowPrompt("核销失败");
        }

        // 处理分销
        distributionSalesService.thawTicket(ticketUserDomain.getId());

        // 创建任务
        if (ticketUserVO.getDeviceId() != null) {
            DeviceDomain deviceDomain = deviceService.getById(ticketUserVO.getDeviceId());

            String finalMessage = "";
            if (deviceDomain.getConfig() == null) {
                // 无配置，不传编号
                finalMessage = "AABBCC" + deviceDomain.getSn() + "030200FF";
            } else if (deviceDomain.getConfig().toString().indexOf("input") != -1) {
                // 输入文字

                // 违禁词过滤
                finalMessage = SensitiveWordHelper.replace(ticketUserVO.getMessage());

                //                // 转为16进制
                //                try {
                //                    byte[] bytes = finalMessage.getBytes(StandardCharsets.UTF_8);
                //                    StringBuilder hexStringBuilder = new StringBuilder();
                //                    for (byte b : bytes) {
                //                        hexStringBuilder.append(String.format("%02X", b));
                //                    }
                //                    finalMessage = hexStringBuilder.toString();
                //                } catch (Exception e) {
                //                    throw new ThrowException("文字转换失败");
                //                }
            } else {
                // 传编号
                finalMessage = "AABBCC" + deviceDomain.getSn() + "0302" + ticketUserVO.getMessage() + "FF";
            }

            DeviceTaskVO deviceTaskVO = new DeviceTaskVO();
            deviceTaskVO.setDeviceId(ticketUserVO.getDeviceId());
            deviceTaskVO.setCreatedAt(new Date());
            deviceTaskVO.setCreatorId(Context.loginUser().getId());
            deviceTaskVO.setMessage(finalMessage);
            deviceTaskVO.setRunningStatus(10);
            deviceTaskVO.setRunningStatus(10);

            deviceTaskService.create(deviceTaskVO);
        }

        // 全部核销，更新订单退款状态
        long notValidationCount =
            this.queryChain().select(TICKET_USER.ID).where(TICKET_USER.ORDER_ID.eq(ticketUserDomain.getOrderId()))
                .and(TICKET_USER.STATUS.eq(10)).count();

        if (notValidationCount <= 0) {
            OrderDomain orderDomain = new OrderDomain();
            orderDomain.setId(ticketUserDomain.getOrderId());
            orderDomain.setRefundStatus(10);
            orderMapper.update(orderDomain);
        }
    }

    /**
     * 取消核销
     *
     * @param ticketUserVO
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void cancelValidation(TicketUserVO ticketUserVO) {
        TicketUserDomain ticketUserDomain = this.getById(ticketUserVO.getId());
        if (ticketUserDomain == null) {
            throw new ThrowPrompt("票务不存在");
        }

        if (!ticketUserDomain.getStatus().equals(20)) {
            throw new ThrowPrompt("票务未核销");
        }

        // 处理分销
        distributionSalesService.freezeTicket(ticketUserDomain.getId());

        // 还原票务
        this.updateChain().set(TICKET_USER.REMAINING_VALIDATION_COUNT, TICKET_USER.REMAINING_VALIDATION_COUNT.add(1))
            .set(TICKET_USER.VALIDATION_AT, null).set(TICKET_USER.STATUS, 10)
            .where(TICKET_USER.ID.eq(ticketUserVO.getId())).update();

        // 更新订单退款状态
        OrderDomain order = orderMapper.find(ticketUserDomain.getOrderId());

        if (order != null && order.getRefundStatus() == 10) {
            order.setRefundStatus(11);
            orderMapper.update(order);
        }
    }
}

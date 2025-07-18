package com.pighand.aio.service.ECommerce.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.TicketDomain;
import com.pighand.aio.domain.IoT.DeviceDomain;
import com.pighand.aio.mapper.ECommerce.TicketMapper;
import com.pighand.aio.service.ECommerce.TicketService;
import com.pighand.aio.service.ECommerce.TicketValidityService;
import com.pighand.aio.service.IoT.DeviceService;
import com.pighand.aio.service.base.ApplicationDefaultService;
import com.pighand.aio.vo.ECommerce.TicketVO;
import com.pighand.aio.vo.ECommerce.TicketValidityDetailEntity;
import com.pighand.aio.vo.ECommerce.TicketValidityVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.pighand.aio.domain.ECommerce.table.ThemeTableDef.THEME;
import static com.pighand.aio.domain.ECommerce.table.TicketTableDef.TICKET;
import static com.pighand.aio.domain.ECommerce.table.TicketValidityTableDef.TICKET_VALIDITY;
import static com.pighand.aio.domain.IoT.table.DeviceTableDef.DEVICE;

/**
 * 电商 - 票务
 *
 * @author wangshuli
 * @createDate 2024-04-26 14:52:18
 */
@Service
@RequiredArgsConstructor
public class TicketServiceImpl extends BaseServiceImpl<TicketMapper, TicketDomain> implements TicketService {

    private final TicketValidityService ticketValidityService;

    private final DeviceService deviceService;

    private final ApplicationDefaultService projectDefaultService;

    /**
     * 创建
     *
     * @param ticketVO
     * @return
     */
    @Override
    public TicketVO create(TicketVO ticketVO) {
        if (ticketVO.getValidationCount() == null) {
            ticketVO.setValidationCount(1);
        }

        super.mapper.insert(ticketVO);

        return ticketVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public TicketDomain find(Long id) {
        return super.mapper.find(id);
    }

    /**
     * 分页或列表
     *
     * @param ticketVO
     * @return PageOrList<TicketVO>
     */
    @Override
    public PageOrList<TicketVO> query(TicketVO ticketVO) {
        ticketVO.setJoinTables(THEME.getName());

        QueryWrapper queryWrapper = QueryWrapper.create()
            .select(TICKET.ID, TICKET.NAME, TICKET.DETAILS, TICKET.POSTER_URL, TICKET.ORIGINAL_PRICE,
                TICKET.CURRENT_PRICE, TICKET.VALIDATION_COUNT)

            // like
            .and(TICKET.NAME.like(ticketVO.getName())).and(TICKET.DETAILS.like(ticketVO.getDetails()))

            // equal
            .and(TICKET.ORIGINAL_PRICE.eq(ticketVO.getOriginalPrice()))
            .and(TICKET.CURRENT_PRICE.eq(ticketVO.getCurrentPrice()))
            .and(TICKET.VALIDATION_COUNT.eq(ticketVO.getValidationCount()));

        PageOrList<TicketVO> result = super.mapper.query(ticketVO, queryWrapper);

        List<Long> ids = result.getRecords().stream().map(TicketVO::getId).collect(Collectors.toList());

        // 查询适用信息
        List<TicketValidityVO> ticketValidities = new ArrayList<>(0);
        if (!ids.isEmpty()) {
            ticketValidities = ticketValidityService.queryChain().where(TICKET_VALIDITY.TICKET_ID.in(ids))
                .listAs(TicketValidityVO.class);
        }

        List<Long> validyIds = new ArrayList<>();
        ticketValidities.forEach(ticketValidity -> {
            validyIds.addAll(ticketValidity.getValidityIds());
        });

        // 查询适用信息
        List<DeviceDomain> devices = new ArrayList<>(0);
        if (!validyIds.isEmpty()) {
            devices = deviceService.queryChain().select(DEVICE.ID, DEVICE.DESCRIPTION, DEVICE.CONFIG)
                .where(DEVICE.ID.in(validyIds)).list();
        }

        Map<Long, TicketValidityDetailEntity> deviceMap =
            devices.stream().collect(Collectors.toMap(DeviceDomain::getId, device -> {
                TicketValidityDetailEntity ticketValidityDetailEntity = new TicketValidityDetailEntity();
                ticketValidityDetailEntity.setId(device.getId());
                ticketValidityDetailEntity.setName(device.getDescription());
                ticketValidityDetailEntity.setConfig(device.getConfig());

                return ticketValidityDetailEntity;
            }));

        Map<Long, List<TicketValidityVO>> ticketValidityMap = new HashMap<>();
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

            List<TicketValidityVO> ticketValidityVOS = ticketValidityMap.get(ticketValidity.getTicketId());
            if (ticketValidityVOS == null) {
                ticketValidityVOS = new ArrayList<>();
            }
            ticketValidityVOS.add(ticketValidity);

            ticketValidityMap.put(ticketValidity.getTicketId(), ticketValidityVOS);
        });

        result.getRecords().forEach(resultItem -> {
            resultItem.setTicketValidity(ticketValidityMap.get(resultItem.getId()));
        });

        return result;
    }

    /**
     * 修改
     *
     * @param ticketVO
     */
    @Override
    public void update(TicketVO ticketVO) {
        super.mapper.update(ticketVO);
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
}

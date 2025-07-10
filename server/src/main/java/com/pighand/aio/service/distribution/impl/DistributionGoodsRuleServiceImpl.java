package com.pighand.aio.service.distribution.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.pighand.aio.domain.ECommerce.ThemeDomain;
import com.pighand.aio.domain.ECommerce.TicketDomain;
import com.pighand.aio.domain.distribution.DistributionGoodsRuleDomain;
import com.pighand.aio.mapper.distribution.DistributionGoodsRuleMapper;
import com.pighand.aio.service.ECommerce.ThemeService;
import com.pighand.aio.service.ECommerce.TicketService;
import com.pighand.aio.service.distribution.DistributionGoodsRuleService;
import com.pighand.aio.vo.distribution.DistributionGoodsRuleVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pighand.aio.domain.ECommerce.table.ThemeTableDef.THEME;
import static com.pighand.aio.domain.ECommerce.table.TicketTableDef.TICKET;
import static com.pighand.aio.domain.distribution.table.DistributionGoodsRuleTableDef.DISTRIBUTION_GOODS_RULE;

/**
 * 分销 - 分销商品规则
 *
 * @author wangshuli
 * @createDate 2025-05-24 14:35:05
 */
@Service
@RequiredArgsConstructor
public class DistributionGoodsRuleServiceImpl
    extends BaseServiceImpl<DistributionGoodsRuleMapper, DistributionGoodsRuleDomain>
    implements DistributionGoodsRuleService {

    private final TicketService ticketService;

    private final ThemeService themeService;

    /**
     * 创建
     *
     * @param distDistributionGoodsRuleVO
     * @return
     */
    @Override
    public DistributionGoodsRuleVO create(DistributionGoodsRuleVO distDistributionGoodsRuleVO) {
        super.mapper.insert(distDistributionGoodsRuleVO);

        return distDistributionGoodsRuleVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public DistributionGoodsRuleDomain find(Long id) {
        DistributionGoodsRuleVO distributionGoodsRule = super.mapper.find(id);

        return distributionGoodsRule;
    }

    /**
     * 分页或列表
     *
     * @param distDistributionGoodsRuleVO
     * @return PageOrList<DistDistributionGoodsRuleVO>
     */
    @Override
    public PageOrList<DistributionGoodsRuleVO> query(DistributionGoodsRuleVO distDistributionGoodsRuleVO) {

        QueryWrapper queryWrapper = QueryWrapper.create()

            // like
            .and(DISTRIBUTION_GOODS_RULE.OBJECT_TYPE.like(distDistributionGoodsRuleVO.getObjectType()))

            // equal
            .and(DISTRIBUTION_GOODS_RULE.OBJECT_ID.eq(distDistributionGoodsRuleVO.getObjectId()))
            .and(DISTRIBUTION_GOODS_RULE.SHARING_TYPE.eq(distDistributionGoodsRuleVO.getSharingType()))
            .and(DISTRIBUTION_GOODS_RULE.SHARING_RATIO.eq(distDistributionGoodsRuleVO.getSharingRatio()))
            .and(DISTRIBUTION_GOODS_RULE.SHARING_PRICE.eq(distDistributionGoodsRuleVO.getSharingPrice()));

        PageOrList<DistributionGoodsRuleVO> result = super.mapper.query(distDistributionGoodsRuleVO, queryWrapper);

        // 批量回填objectName
        if (result != null && result.getRecords() != null && !result.getRecords().isEmpty()) {
            // 收集所有需要查询的ID
            List<Long> ticketIds = new ArrayList<>();
            List<Long> themeIds = new ArrayList<>();

            for (DistributionGoodsRuleVO item : result.getRecords()) {
                if (item.getObjectId() != null) {
                    if (Integer.valueOf(20).equals(item.getObjectType())) {
                        ticketIds.add(item.getObjectId());
                    } else if (Integer.valueOf(10).equals(item.getObjectType())) {
                        themeIds.add(item.getObjectId());
                    }
                }
            }

            // 批量查询ticket名称
            Map<Long, String> ticketNameMap = new HashMap<>();
            if (!ticketIds.isEmpty()) {
                List<TicketDomain> tickets =
                    ticketService.queryChain().select(TICKET.ID, TICKET.NAME).where(TICKET.ID.in(ticketIds)).list();

                for (TicketDomain ticket : tickets) {
                    ticketNameMap.put(ticket.getId(), ticket.getName());
                }
            }

            // 批量查询theme名称
            Map<Long, String> themeNameMap = new HashMap<>();
            if (!themeIds.isEmpty()) {
                List<ThemeDomain> themes =
                    themeService.queryChain().select(THEME.ID, THEME.THEME_NAME).where(THEME.ID.in(themeIds)).list();

                for (ThemeDomain theme : themes) {
                    themeNameMap.put(theme.getId(), theme.getThemeName());
                }
            }

            // 设置objectName
            for (DistributionGoodsRuleVO item : result.getRecords()) {
                if (item.getObjectId() != null) {
                    String objectName = "";
                    if (Integer.valueOf(20).equals(item.getObjectType())) {
                        objectName = ticketNameMap.getOrDefault(item.getObjectId(), "");
                    } else if (Integer.valueOf(10).equals(item.getObjectType())) {
                        objectName = themeNameMap.getOrDefault(item.getObjectId(), "");
                    }
                    item.setObjectName(objectName);
                }
            }
        }

        return result;
    }

    /**
     * 修改
     *
     * @param distDistributionGoodsRuleVO
     */
    @Override
    public void update(DistributionGoodsRuleVO distDistributionGoodsRuleVO) {
        UpdateChain updateChain =
            this.updateChain().where(DISTRIBUTION_GOODS_RULE.ID.eq(distDistributionGoodsRuleVO.getId()));

        updateChain.set(DISTRIBUTION_GOODS_RULE.OBJECT_TYPE, distDistributionGoodsRuleVO.getObjectType());
        updateChain.set(DISTRIBUTION_GOODS_RULE.OBJECT_ID, distDistributionGoodsRuleVO.getObjectId());
        updateChain.set(DISTRIBUTION_GOODS_RULE.SHARING_TYPE, distDistributionGoodsRuleVO.getSharingType());
        updateChain.set(DISTRIBUTION_GOODS_RULE.SHARING_RATIO, distDistributionGoodsRuleVO.getSharingRatio());
        updateChain.set(DISTRIBUTION_GOODS_RULE.SHARING_PRICE, distDistributionGoodsRuleVO.getSharingPrice());

        updateChain.update();
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

package com.pighand.aio.service.ECommerce.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.BillDomain;
import com.pighand.aio.mapper.ECommerce.BillMapper;
import com.pighand.aio.service.ECommerce.BillService;
import com.pighand.aio.vo.ECommerce.BillVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import org.springframework.stereotype.Service;

import static com.pighand.aio.domain.ECommerce.table.BillTableDef.BILL;
import static com.pighand.aio.domain.ECommerce.table.OrderSkuTableDef.ORDER_SKU;
import static com.pighand.aio.domain.ECommerce.table.OrderTableDef.ORDER;
import static com.pighand.aio.domain.ECommerce.table.OrderTradeTableDef.ORDER_TRADE;

/**
 * 商城 - 账单
 *
 * @author wangshuli
 * @createDate 2024-04-18 14:35:34
 */
@Service
public class BillServiceImpl extends BaseServiceImpl<BillMapper, BillDomain> implements BillService {

    /**
     * 创建
     *
     * @param billVO
     * @return
     */
    @Override
    public BillVO create(BillVO billVO) {
        super.mapper.insert(billVO);

        return billVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public BillDomain find(Long id) {
        return super.mapper.find(id, ORDER.getTableName(), ORDER_TRADE.getTableName(), ORDER_SKU.getTableName());
    }

    /**
     * 分页或列表
     *
     * @param billVO
     * @return PageOrList<BillVO>
     */
    @Override
    public PageOrList<BillVO> query(BillVO billVO) {
        billVO.setJoinTables(ORDER.getTableName(), ORDER_TRADE.getTableName(), ORDER_SKU.getTableName());

        QueryWrapper queryWrapper = new QueryWrapper();

        // like
        queryWrapper.and(BILL.SOURCE_ID.like(billVO.getSourceId(), VerifyUtils::isNotEmpty));
        queryWrapper.and(BILL.SN.like(billVO.getSn(), VerifyUtils::isNotEmpty));

        // equal
        queryWrapper.and(BILL.ORDER_TRADE_ID.eq(billVO.getOrderTradeId(), VerifyUtils::isNotEmpty));
        queryWrapper.and(BILL.ORDER_ID.eq(billVO.getOrderId(), VerifyUtils::isNotEmpty));
        queryWrapper.and(BILL.ORDER_SKU_ID.eq(billVO.getOrderSkuId(), VerifyUtils::isNotEmpty));
        queryWrapper.and(BILL.AMOUNT.eq(billVO.getAmount(), VerifyUtils::isNotEmpty));
        queryWrapper.and(BILL.CREATOR_ID.eq(billVO.getCreatorId(), VerifyUtils::isNotEmpty));

        return super.mapper.query(billVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param billVO
     */
    @Override
    public void update(BillVO billVO) {
        super.mapper.update(billVO);
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

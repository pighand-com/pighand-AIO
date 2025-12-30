package com.pighand.aio.service.ECommerce;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.BillDomain;
import com.pighand.aio.mapper.ECommerce.BillMapper;
import com.pighand.aio.vo.ECommerce.BillVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
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
public class BillService extends BaseServiceImpl<BillMapper, BillDomain>  {

    /**
     * 创建
     *
     * @param billVO
     * @return
     */
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
    public BillDomain find(Long id) {
        return super.mapper.find(id, ORDER.getTableName(), ORDER_TRADE.getTableName(), ORDER_SKU.getTableName());
    }

    /**
     * 分页或列表
     *
     * @param billVO
     * @return PageOrList<BillVO>
     */
    public PageOrList<BillVO> query(BillVO billVO) {
        billVO.setJoinTables(ORDER.getTableName(), ORDER_TRADE.getTableName(), ORDER_SKU.getTableName());

        QueryWrapper queryWrapper = QueryWrapper.create()
            // like
            .and(BILL.SOURCE_ID.like(billVO.getSourceId())).and(BILL.SN.like(billVO.getSn()))

            // equal
            .and(BILL.ORDER_TRADE_ID.eq(billVO.getOrderTradeId())).and(BILL.ORDER_ID.eq(billVO.getOrderId()))
            .and(BILL.ORDER_SKU_ID.eq(billVO.getOrderSkuId())).and(BILL.AMOUNT.eq(billVO.getAmount()))
            .and(BILL.CREATOR_ID.eq(billVO.getCreatorId()));

        return super.mapper.query(billVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param billVO
     */
    public void update(BillVO billVO) {
        super.mapper.update(billVO);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(Long id) {
        super.mapper.deleteById(id);
    }
}

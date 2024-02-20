package com.pighand.aio.mapper.eCommerce;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.domain.eCommerce.WalletBillDomain;
import com.pighand.aio.vo.eCommerce.WalletBillVO;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.framework.spring.page.PageOrList;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

import static com.pighand.aio.domain.eCommerce.table.WalletBillTableDef.WALLET_BILL;
import static com.pighand.aio.domain.eCommerce.table.WalletTransferTableDef.WALLET_TRANSFER;

/**
 * 电商 - 钱包账单
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Mapper
public interface WalletBillMapper extends BaseMapper<WalletBillDomain> {

    /**
     * 基础查询条件
     *
     * @return
     */
    default QueryWrapper baseQuery(List<String> joinTables) {
        if (joinTables == null) {
            joinTables = new ArrayList<>();
        }

        QueryWrapper queryWrapper = QueryWrapper.create();
        queryWrapper.where(WALLET_BILL.USER_ID.eq(Context.getLoginUser().getId()));

        if (joinTables.contains(WALLET_TRANSFER.getTableName())) {
            queryWrapper.leftJoin(WALLET_TRANSFER).on(WALLET_TRANSFER.ID.eq(WALLET_BILL.WALLET_TRANSFER_ID));
        }

        return queryWrapper;
    }

    /**
     * 查询详情
     *
     * @param id         主键
     * @param joinTables 关联表
     * @return
     */
    default WalletBillVO find(Long id, List<String> joinTables) {
        QueryWrapper queryWrapper = this.baseQuery(joinTables).where(WALLET_BILL.ID.eq(id));

        return this.selectOneByQueryAs(queryWrapper, WalletBillVO.class);
    }

    /**
     * 分页或列表
     *
     * @param walletBillDomain
     * @return
     */
    default PageOrList<WalletBillVO> query(WalletBillDomain walletBillDomain) {
        QueryWrapper queryWrapper = this.baseQuery(walletBillDomain.getJoinTables());
        queryWrapper.orderBy(WALLET_BILL.ID.desc());

        return this.page(walletBillDomain, queryWrapper, WalletBillVO.class);
    }
}

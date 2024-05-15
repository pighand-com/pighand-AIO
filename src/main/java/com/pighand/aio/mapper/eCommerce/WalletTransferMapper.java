package com.pighand.aio.mapper.ECommerce;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.WalletTransferDomain;
import com.pighand.aio.vo.ECommerce.WalletTransferVO;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.framework.spring.page.PageOrList;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

import static com.pighand.aio.domain.ECommerce.table.WalletBillTableDef.WALLET_BILL;
import static com.pighand.aio.domain.ECommerce.table.WalletTransferTableDef.WALLET_TRANSFER;

/**
 * 电商 - 转账记录
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Mapper
public interface WalletTransferMapper extends BaseMapper<WalletTransferDomain> {

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

        if (joinTables.contains(WALLET_BILL.getTableName())) {
            queryWrapper.leftJoin(WALLET_BILL).on(WALLET_BILL.WALLET_TRANSFER_ID.eq(WALLET_TRANSFER.ID));
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
    default WalletTransferVO find(Long id, List<String> joinTables) {
        QueryWrapper queryWrapper = this.baseQuery(joinTables).where(WALLET_TRANSFER.ID.eq(id));

        return this.selectOneByQueryAs(queryWrapper, WalletTransferVO.class);
    }

    /**
     * 分页或列表
     *
     * @param walletTransferDomain
     * @return
     */
    default PageOrList<WalletTransferVO> query(WalletTransferDomain walletTransferDomain) {
        QueryWrapper queryWrapper = this.baseQuery(walletTransferDomain.getJoinTables());

        return this.page(walletTransferDomain, queryWrapper, WalletTransferVO.class);
    }
}

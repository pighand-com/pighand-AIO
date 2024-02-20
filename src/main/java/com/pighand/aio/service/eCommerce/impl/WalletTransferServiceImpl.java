package com.pighand.aio.service.eCommerce.impl;

import com.pighand.aio.domain.eCommerce.WalletTransferDomain;
import com.pighand.aio.mapper.eCommerce.WalletTransferMapper;
import com.pighand.aio.service.eCommerce.WalletTransferService;
import com.pighand.aio.vo.eCommerce.WalletTransferVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.pighand.aio.domain.eCommerce.table.WalletBillTableDef.WALLET_BILL;

/**
 * 电商 - 转账记录
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Service
public class WalletTransferServiceImpl extends BaseServiceImpl<WalletTransferMapper, WalletTransferDomain>
    implements WalletTransferService {

    /**
     * 创建
     *
     * @param walletTransferVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public WalletTransferVO create(WalletTransferVO walletTransferVO) {
        super.mapper.insert(walletTransferVO);

        return walletTransferVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public WalletTransferDomain find(Long id) {
        List<String> joinTables = List.of("wallet_bill");

        return super.mapper.find(id, joinTables);
    }

    /**
     * 分页或列表
     *
     * @param walletTransferVO
     * @return PageOrList<WalletTransferVO>
     */
    @Override
    public PageOrList<WalletTransferVO> query(WalletTransferVO walletTransferVO) {
        walletTransferVO.setJoinTables(List.of(WALLET_BILL.getTableName()));

        return super.mapper.query(walletTransferVO);
    }

    /**
     * 修改
     *
     * @param walletTransferVO
     */
    @Override
    public void update(WalletTransferVO walletTransferVO) {
        super.mapper.update(walletTransferVO);
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

package com.pighand.aio.service.ECommerce;

import com.pighand.aio.domain.ECommerce.WalletTransferDomain;
import com.pighand.aio.mapper.ECommerce.WalletTransferMapper;
import com.pighand.aio.vo.ECommerce.WalletTransferVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static com.pighand.aio.domain.ECommerce.table.WalletBillTableDef.WALLET_BILL;

/**
 * 电商 - 转账记录
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Service
public class WalletTransferService extends BaseServiceImpl<WalletTransferMapper, WalletTransferDomain>
     {

    /**
     * 创建
     *
     * @param walletTransferVO
     * @return
     */
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
    public WalletTransferDomain find(Long id) {
        return super.mapper.find(id, WALLET_BILL.getTableName());
    }

    /**
     * 分页或列表
     *
     * @param walletTransferVO
     * @return PageOrList<WalletTransferVO>
     */
    public PageOrList<WalletTransferVO> query(WalletTransferVO walletTransferVO) {
        walletTransferVO.setJoinTables(Set.of(WALLET_BILL.getTableName()));

        return super.mapper.query(walletTransferVO, null);
    }

    /**
     * 修改
     *
     * @param walletTransferVO
     */
    public void update(WalletTransferVO walletTransferVO) {
        super.mapper.update(walletTransferVO);
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

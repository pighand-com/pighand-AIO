package com.pighand.aio.service.ECommerce;

import com.pighand.aio.domain.ECommerce.WalletDomain;
import com.pighand.aio.vo.ECommerce.WalletTopVO;
import com.pighand.aio.vo.ECommerce.WalletTransferVO;
import com.pighand.aio.vo.ECommerce.WalletVO;
import com.pighand.framework.spring.base.BaseService;

import java.util.List;

/**
 * 电商 - 钱包
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
public interface WalletService extends BaseService<WalletDomain> {

    /**
     * 转账
     *
     * @param walletTransferVO
     */
    void transfer(WalletTransferVO walletTransferVO);

    /**
     * 钱包详情
     *
     * @return
     */
    WalletVO find();

    /**
     * 排行榜
     *
     * @param walletTopVO
     * @return
     */
    List<WalletTopVO> top(WalletTopVO walletTopVO);
}

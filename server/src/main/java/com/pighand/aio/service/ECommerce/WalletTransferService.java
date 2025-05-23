package com.pighand.aio.service.ECommerce;

import com.pighand.aio.domain.ECommerce.WalletTransferDomain;
import com.pighand.aio.vo.ECommerce.WalletTransferVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;

/**
 * 电商 - 转账记录
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
public interface WalletTransferService extends BaseService<WalletTransferDomain> {

    /**
     * 创建
     *
     * @param walletTransferVO
     * @return
     */
    WalletTransferVO create(WalletTransferVO walletTransferVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    WalletTransferDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param walletTransferVO
     * @return PageOrList<WalletTransferVO>
     */
    PageOrList<WalletTransferVO> query(WalletTransferVO walletTransferVO);

    /**
     * 修改
     *
     * @param walletTransferVO
     */
    void update(WalletTransferVO walletTransferVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}

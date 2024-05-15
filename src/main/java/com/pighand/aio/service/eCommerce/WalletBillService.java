package com.pighand.aio.service.ECommerce;

import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.aio.domain.ECommerce.WalletBillDomain;
import com.pighand.aio.vo.ECommerce.WalletBillTop;
import com.pighand.aio.vo.ECommerce.WalletBillVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * 电商 - 钱包账单
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
public interface WalletBillService extends BaseService<WalletBillDomain> {

    /**
     * 分页或列表
     *
     * @param walletBillVO
     * @return PageOrList<WalletBillVO>
     */
    PageOrList<WalletBillVO> query(WalletBillVO walletBillVO);

    /**
     * 按场次分组查询top
     *
     * @param walletBillVO
     * @return
     */
    List<WalletBillTop> queryBillTopBySessionGroup(WalletBillVO walletBillVO);

    /**
     * 统计入账总金额
     *
     * @param totalType
     * @return
     */
    BigDecimal queryTotalAmount(Integer totalType);
}

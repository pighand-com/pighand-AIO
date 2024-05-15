package com.pighand.aio.vo.ECommerce;

import com.mybatisflex.annotation.Column;
import com.pighand.aio.domain.ECommerce.WalletTransferDomain;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 电商 - 转账记录
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Data
public class WalletTransferVO extends WalletTransferDomain {
    @Column(ignore = true)
    private WalletBillVO walletBill;

    // 转账金额
    private BigDecimal account;
}

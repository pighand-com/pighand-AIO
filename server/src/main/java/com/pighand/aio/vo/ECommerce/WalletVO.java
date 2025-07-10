package com.pighand.aio.vo.ECommerce;

import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.ECommerce.WalletDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 电商 - 钱包
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Data
@TableRef(WalletDomain.class)
@EqualsAndHashCode(callSuper = false)
public class WalletVO extends WalletDomain {
}

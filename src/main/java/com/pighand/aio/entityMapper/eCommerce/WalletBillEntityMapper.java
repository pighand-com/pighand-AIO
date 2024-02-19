package com.pighand.aio.entityMapper.eCommerce;

import com.pighand.aio.domain.eCommerce.WalletBillDomain;
import com.pighand.aio.vo.eCommerce.WalletBillVO;
import org.mapstruct.Mapper;

/**
 * 电商 - 钱包账单
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Mapper(componentModel = "spring")
public interface WalletBillEntityMapper {

    WalletBillVO toVo(WalletBillDomain entity);

    WalletBillDomain toDomain(WalletBillVO vo);
}

package com.pighand.aio.entityMapper.eCommerce;

import com.pighand.aio.domain.eCommerce.WalletTransferDomain;
import com.pighand.aio.vo.eCommerce.WalletTransferVO;
import org.mapstruct.Mapper;

/**
 * 电商 - 转账记录
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Mapper(componentModel = "spring")
public interface WalletTransferEntityMapper {

    WalletTransferVO toVo(WalletTransferDomain entity);

    WalletTransferDomain toDomain(WalletTransferVO vo);
}

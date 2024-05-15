package com.pighand.aio.entityMapper.ECommerce;

import com.pighand.aio.domain.ECommerce.WalletTransferDomain;
import com.pighand.aio.vo.ECommerce.WalletTransferVO;
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

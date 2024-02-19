package com.pighand.aio.entityMapper.eCommerce;

import com.pighand.aio.domain.eCommerce.WalletDomain;
import com.pighand.aio.vo.eCommerce.WalletVO;
import org.mapstruct.Mapper;

/**
 * 电商 - 钱包
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Mapper(componentModel = "spring")
public interface WalletEntityMapper {

    WalletVO toVo(WalletDomain entity);

    WalletDomain toDomain(WalletVO vo);
}

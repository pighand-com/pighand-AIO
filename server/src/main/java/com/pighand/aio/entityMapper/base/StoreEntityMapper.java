package com.pighand.aio.entityMapper.base;

import com.pighand.aio.domain.base.StoreDomain;
import com.pighand.aio.vo.base.StoreVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 门店
 *
 * @author wangshuli
 * @createDate 2024-12-31 19:04:50
 */
@Mapper(componentModel = "spring")
public interface StoreEntityMapper {

    StoreVO toVo(StoreDomain entity);

    StoreDomain toDomain(StoreVO vo);

    List<StoreVO> toVoList(List<StoreDomain> entity);

    List<StoreDomain> toDomainList(List<StoreVO> vo);
}

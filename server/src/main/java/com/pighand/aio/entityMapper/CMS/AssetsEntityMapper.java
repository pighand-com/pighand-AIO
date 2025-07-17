package com.pighand.aio.entityMapper.CMS;

import com.pighand.aio.domain.CMS.AssetsDomain;
import com.pighand.aio.vo.CMS.AssetsVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 公共 - 素材
 *
 * @author wangshuli
 * @createDate 2024-12-31 19:04:50
 */
@Mapper(componentModel = "spring")
public interface AssetsEntityMapper {

    AssetsVO toVo(AssetsDomain entity);

    AssetsDomain toDomain(AssetsVO vo);

    List<AssetsVO> toVoList(List<AssetsDomain> entity);

    List<AssetsDomain> toDomainList(List<AssetsVO> vo);
}

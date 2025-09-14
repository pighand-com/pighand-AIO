package com.pighand.aio.entityMapper.CMS;

import com.pighand.aio.domain.CMS.AssetsFavouriteDomain;
import com.pighand.aio.vo.CMS.AssetsFavouriteVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * CMS - 素材 - 收藏
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@Mapper(componentModel = "spring")
public interface AssetsFavouriteEntityMapper {

    AssetsFavouriteVO toVo(AssetsFavouriteDomain entity);

    AssetsFavouriteDomain toDomain(AssetsFavouriteVO vo);

    List<AssetsFavouriteVO> toVoList(List<AssetsFavouriteDomain> entity);

    List<AssetsFavouriteDomain> toDomainList(List<AssetsFavouriteVO> vo);
}

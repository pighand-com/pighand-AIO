package com.pighand.aio.entityMapper.CMS;

import com.pighand.user.domain.CmsAssetsFavouriteDomain;
import com.pighand.user.domain.CmsAssetsFavouriteVO;
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

    CmsAssetsFavouriteVO toVo(CmsAssetsFavouriteDomain entity);

    CmsAssetsFavouriteDomain toDomain(CmsAssetsFavouriteVO vo);

    List<CmsAssetsFavouriteVO> toVoList(List<CmsAssetsFavouriteDomain> entity);

    List<CmsAssetsFavouriteDomain> toDomainList(List<CmsAssetsFavouriteVO> vo);
}

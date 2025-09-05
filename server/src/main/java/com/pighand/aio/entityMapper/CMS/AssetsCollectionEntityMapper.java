package com.pighand.aio.entityMapper.CMS;

import com.pighand.user.domain.CmsAssetsCollectionDomain;
import com.pighand.user.domain.CmsAssetsCollectionVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * CMS - 素材 - 专辑
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@Mapper(componentModel = "spring")
public interface AssetsCollectionEntityMapper {

    CmsAssetsCollectionVO toVo(CmsAssetsCollectionDomain entity);

    CmsAssetsCollectionDomain toDomain(CmsAssetsCollectionVO vo);

    List<CmsAssetsCollectionVO> toVoList(List<CmsAssetsCollectionDomain> entity);

    List<CmsAssetsCollectionDomain> toDomainList(List<CmsAssetsCollectionVO> vo);
}

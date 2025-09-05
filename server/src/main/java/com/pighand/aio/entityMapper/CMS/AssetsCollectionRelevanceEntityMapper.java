package com.pighand.aio.entityMapper.CMS;

import com.pighand.user.domain.CmsAssetsCollectionRelevanceDomain;
import com.pighand.user.domain.CmsAssetsCollectionRelevanceVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * CMS - 素材 - 专辑关系表
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@Mapper(componentModel = "spring")
public interface AssetsCollectionRelevanceEntityMapper {

    CmsAssetsCollectionRelevanceVO toVo(CmsAssetsCollectionRelevanceDomain entity);

    CmsAssetsCollectionRelevanceDomain toDomain(CmsAssetsCollectionRelevanceVO vo);

    List<CmsAssetsCollectionRelevanceVO> toVoList(List<CmsAssetsCollectionRelevanceDomain> entity);

    List<CmsAssetsCollectionRelevanceDomain> toDomainList(List<CmsAssetsCollectionRelevanceVO> vo);
}

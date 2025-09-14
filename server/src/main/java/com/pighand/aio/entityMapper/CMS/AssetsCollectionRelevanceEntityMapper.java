package com.pighand.aio.entityMapper.CMS;

import com.pighand.aio.domain.CMS.AssetsCollectionRelevanceDomain;
import com.pighand.aio.vo.CMS.AssetsCollectionRelevanceVO;
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

    AssetsCollectionRelevanceVO toVo(AssetsCollectionRelevanceDomain entity);

    AssetsCollectionRelevanceDomain toDomain(AssetsCollectionRelevanceVO vo);

    List<AssetsCollectionRelevanceVO> toVoList(List<AssetsCollectionRelevanceDomain> entity);

    List<AssetsCollectionRelevanceDomain> toDomainList(List<AssetsCollectionRelevanceVO> vo);
}

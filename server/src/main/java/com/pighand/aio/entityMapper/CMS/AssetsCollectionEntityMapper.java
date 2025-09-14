package com.pighand.aio.entityMapper.CMS;

import com.pighand.aio.domain.CMS.AssetsCollectionDomain;
import com.pighand.aio.vo.CMS.AssetsCollectionVO;
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

    AssetsCollectionVO toVo(AssetsCollectionDomain entity);

    AssetsCollectionDomain toDomain(AssetsCollectionVO vo);

    List<AssetsCollectionVO> toVoList(List<AssetsCollectionDomain> entity);

    List<AssetsCollectionDomain> toDomainList(List<AssetsCollectionVO> vo);
}

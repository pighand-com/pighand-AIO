package com.pighand.aio.entityMapper.CMS;

import com.pighand.user.domain.CmsAssetsDocDomain;
import com.pighand.user.domain.CmsAssetsDocVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * CMS - 素材 - 文档
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@Mapper(componentModel = "spring")
public interface AssetsDocEntityMapper {

    CmsAssetsDocVO toVo(CmsAssetsDocDomain entity);

    CmsAssetsDocDomain toDomain(CmsAssetsDocVO vo);

    List<CmsAssetsDocVO> toVoList(List<CmsAssetsDocDomain> entity);

    List<CmsAssetsDocDomain> toDomainList(List<CmsAssetsDocVO> vo);
}

package com.pighand.aio.entityMapper.CMS;

import com.pighand.user.domain.CmsAssetsImageDomain;
import com.pighand.user.domain.CmsAssetsImageVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * CMS - 素材 - 图片
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@Mapper(componentModel = "spring")
public interface AssetsImageEntityMapper {

    CmsAssetsImageVO toVo(CmsAssetsImageDomain entity);

    CmsAssetsImageDomain toDomain(CmsAssetsImageVO vo);

    List<CmsAssetsImageVO> toVoList(List<CmsAssetsImageDomain> entity);

    List<CmsAssetsImageDomain> toDomainList(List<CmsAssetsImageVO> vo);
}

package com.pighand.aio.entityMapper.CMS;

import com.pighand.user.domain.CmsAssetsClassificationDomain;
import com.pighand.user.domain.CmsAssetsClassificationVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * CMS - 素材 - 分类
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@Mapper(componentModel = "spring")
public interface AssetsClassificationEntityMapper {

    CmsAssetsClassificationVO toVo(CmsAssetsClassificationDomain entity);

    CmsAssetsClassificationDomain toDomain(CmsAssetsClassificationVO vo);

    List<CmsAssetsClassificationVO> toVoList(List<CmsAssetsClassificationDomain> entity);

    List<CmsAssetsClassificationDomain> toDomainList(List<CmsAssetsClassificationVO> vo);
}

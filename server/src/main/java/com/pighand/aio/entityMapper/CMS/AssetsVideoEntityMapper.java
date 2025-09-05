package com.pighand.aio.entityMapper.CMS;

import com.pighand.user.domain.CmsAssetsVideoDomain;
import com.pighand.user.domain.CmsAssetsVideoVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * CMS - 素材 - 视频
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@Mapper(componentModel = "spring")
public interface AssetsVideoEntityMapper {

    CmsAssetsVideoVO toVo(CmsAssetsVideoDomain entity);

    CmsAssetsVideoDomain toDomain(CmsAssetsVideoVO vo);

    List<CmsAssetsVideoVO> toVoList(List<CmsAssetsVideoDomain> entity);

    List<CmsAssetsVideoDomain> toDomainList(List<CmsAssetsVideoVO> vo);
}

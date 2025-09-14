package com.pighand.aio.entityMapper.CMS;

import com.pighand.aio.domain.CMS.AssetsVideoDomain;
import com.pighand.aio.vo.CMS.AssetsVideoVO;
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

    AssetsVideoVO toVo(AssetsVideoDomain entity);

    AssetsVideoDomain toDomain(AssetsVideoVO vo);

    List<AssetsVideoVO> toVoList(List<AssetsVideoDomain> entity);

    List<AssetsVideoDomain> toDomainList(List<AssetsVideoVO> vo);
}

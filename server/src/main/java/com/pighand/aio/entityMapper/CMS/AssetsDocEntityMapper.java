package com.pighand.aio.entityMapper.CMS;

import com.pighand.aio.domain.CMS.AssetsDocDomain;
import com.pighand.aio.vo.CMS.AssetsDocVO;
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

    AssetsDocVO toVo(AssetsDocDomain entity);

    AssetsDocDomain toDomain(AssetsDocVO vo);

    List<AssetsDocVO> toVoList(List<AssetsDocDomain> entity);

    List<AssetsDocDomain> toDomainList(List<AssetsDocVO> vo);
}

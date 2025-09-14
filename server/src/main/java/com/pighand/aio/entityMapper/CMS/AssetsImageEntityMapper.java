package com.pighand.aio.entityMapper.CMS;

import com.pighand.aio.domain.CMS.AssetsImageDomain;
import com.pighand.aio.vo.CMS.AssetsImageVO;
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

    AssetsImageVO toVo(AssetsImageDomain entity);

    AssetsImageDomain toDomain(AssetsImageVO vo);

    List<AssetsImageVO> toVoList(List<AssetsImageDomain> entity);

    List<AssetsImageDomain> toDomainList(List<AssetsImageVO> vo);
}

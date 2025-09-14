package com.pighand.aio.entityMapper.CMS;

import com.pighand.aio.domain.CMS.AssetsClassificationDomain;
import com.pighand.aio.vo.CMS.AssetsClassificationVO;
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

    AssetsClassificationVO toVo(AssetsClassificationDomain entity);

    AssetsClassificationDomain toDomain(AssetsClassificationVO vo);

    List<AssetsClassificationVO> toVoList(List<AssetsClassificationDomain> entity);

    List<AssetsClassificationDomain> toDomainList(List<AssetsClassificationVO> vo);
}

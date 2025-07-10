package com.pighand.aio.entityMapper.CMS;

import com.pighand.aio.domain.CMS.BannerDomain;
import com.pighand.aio.vo.CMS.BannerVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * CMS - banner
 *
 * @author wangshuli
 * @createDate 2025-06-17 13:59:52
 */
@Mapper(componentModel = "spring")
public interface BannerEntityMapper {

    BannerVO toVo(BannerDomain entity);

    BannerDomain toDomain(BannerVO vo);

    List<BannerVO> toVoList(List<BannerDomain> entity);

    List<BannerDomain> toDomainList(List<BannerVO> vo);
}

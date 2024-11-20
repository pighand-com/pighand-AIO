package com.pighand.aio.entityMapper.base;

import com.pighand.aio.domain.base.ApplicationPlatformPayDomain;
import com.pighand.aio.vo.base.ApplicationPlatformPayVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 项目 - 支付信息
 *
 * @author wangshuli
 * @createDate 2024-04-22 15:11:06
 */
@Mapper(componentModel = "spring")
public interface ApplicationPlatformPayEntityMapper {

    ApplicationPlatformPayVO toVo(ApplicationPlatformPayDomain entity);

    ApplicationPlatformPayDomain toDomain(ApplicationPlatformPayVO vo);

    List<ApplicationPlatformPayVO> toVoList(List<ApplicationPlatformPayDomain> entity);

    List<ApplicationPlatformPayDomain> toDomainList(List<ApplicationPlatformPayVO> vo);
}

package com.pighand.aio.entityMapper.base;

import com.pighand.aio.domain.base.ProjectPlatformPayDomain;
import com.pighand.aio.vo.base.ProjectPlatformPayVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 项目 - 支付信息
 *
 * @author wangshuli
 * @createDate 2024-04-22 15:11:06
 */
@Mapper(componentModel = "spring")
public interface ProjectPlatformPayEntityMapper {

    ProjectPlatformPayVO toVo(ProjectPlatformPayDomain entity);

    ProjectPlatformPayDomain toDomain(ProjectPlatformPayVO vo);

    List<ProjectPlatformPayVO> toVoList(List<ProjectPlatformPayDomain> entity);

    List<ProjectPlatformPayDomain> toDomainList(List<ProjectPlatformPayVO> vo);
}

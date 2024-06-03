package com.pighand.aio.entityMapper.ECommerce;

import com.pighand.aio.domain.ECommerce.SessionTemplateCycleDomain;
import com.pighand.aio.vo.ECommerce.SessionTemplateCycleVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 电商 - 场次模板 - 按周期
 *
 * @author wangshuli
 * @createDate 2024-05-23 15:01:58
 */
@Mapper(componentModel = "spring")
public interface SessionTemplateCycleEntityMapper {

    SessionTemplateCycleVO toVo(SessionTemplateCycleDomain entity);

    SessionTemplateCycleDomain toDomain(SessionTemplateCycleVO vo);

    List<SessionTemplateCycleVO> toVoList(List<SessionTemplateCycleDomain> entity);

    List<SessionTemplateCycleDomain> toDomainList(List<SessionTemplateCycleVO> vo);
}

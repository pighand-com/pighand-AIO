package com.pighand.aio.entityMapper.ECommerce;

import com.pighand.aio.domain.ECommerce.SessionTemplateGourpDomain;
import com.pighand.aio.vo.ECommerce.SessionTemplateGourpVO;
import org.mapstruct.Mapper;

/**
 * 电商 - 场次模板 - 分组
 *
 * @author wangshuli
 * @createDate 2023-12-05 16:13:27
 */
@Mapper(componentModel = "spring")
public interface SessionTemplateGourpEntityMapper {

    SessionTemplateGourpVO toVo(SessionTemplateGourpDomain entity);

    SessionTemplateGourpDomain toDomain(SessionTemplateGourpVO vo);
}

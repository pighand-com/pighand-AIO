package com.pighand.aio.entityMapper.eCommerce;

import com.pighand.aio.domain.eCommerce.SessionTemplateGourpDomain;
import com.pighand.aio.vo.eCommerce.SessionTemplateGourpVO;
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

package com.pighand.aio.entityMapper.ECommerce;

import com.pighand.aio.domain.ECommerce.SessionTemplateDomain;
import com.pighand.aio.vo.ECommerce.SessionTemplateVO;
import org.mapstruct.Mapper;

/**
 * 电商 - 场次模板。根据模板生成场次
 *
 * @author wangshuli
 * @createDate 2023-12-05 16:13:27
 */
@Mapper(componentModel = "spring")
public interface SessionTemplateEntityMapper {

    SessionTemplateVO toVo(SessionTemplateDomain entity);

    SessionTemplateDomain toDomain(SessionTemplateVO vo);
}

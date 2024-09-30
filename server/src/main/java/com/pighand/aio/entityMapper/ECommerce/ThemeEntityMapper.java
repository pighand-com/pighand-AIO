package com.pighand.aio.entityMapper.ECommerce;

import com.pighand.aio.domain.ECommerce.ThemeDomain;
import com.pighand.aio.vo.ECommerce.ThemeVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 电商 - 主题
 *
 * @author wangshuli
 * @createDate 2024-05-23 15:01:58
 */
@Mapper(componentModel = "spring")
public interface ThemeEntityMapper {

    ThemeVO toVo(ThemeDomain entity);

    ThemeDomain toDomain(ThemeVO vo);

    List<ThemeVO> toVoList(List<ThemeDomain> entity);

    List<ThemeDomain> toDomainList(List<ThemeVO> vo);
}

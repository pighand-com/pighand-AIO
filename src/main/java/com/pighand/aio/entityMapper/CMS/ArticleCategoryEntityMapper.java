package com.pighand.aio.entityMapper.CMS;

import com.pighand.aio.domain.CMS.ArticleCategoryDomain;
import com.pighand.aio.vo.CMS.ArticleCategoryVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * CMS - 文章分类
 *
 * @author wangshuli
 * @createDate 2024-04-22 15:11:06
 */
@Mapper(componentModel = "spring")
public interface ArticleCategoryEntityMapper {

    ArticleCategoryVO toVo(ArticleCategoryDomain entity);

    ArticleCategoryDomain toDomain(ArticleCategoryVO vo);

    List<ArticleCategoryVO> toVoList(List<ArticleCategoryDomain> entity);

    List<ArticleCategoryDomain> toDomainList(List<ArticleCategoryVO> vo);
}

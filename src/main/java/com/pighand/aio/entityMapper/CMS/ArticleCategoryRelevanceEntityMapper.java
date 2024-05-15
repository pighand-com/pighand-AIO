package com.pighand.aio.entityMapper.CMS;

import com.pighand.aio.domain.CMS.ArticleCategoryRelevanceDomain;
import com.pighand.aio.vo.CMS.ArticleCategoryRelevanceVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * CMS - 文章、分类关联表
 *
 * @author wangshuli
 * @createDate 2024-04-22 16:17:05
 */
@Mapper(componentModel = "spring")
public interface ArticleCategoryRelevanceEntityMapper {

    ArticleCategoryRelevanceVO toVo(ArticleCategoryRelevanceDomain entity);

    ArticleCategoryRelevanceDomain toDomain(ArticleCategoryRelevanceVO vo);

    List<ArticleCategoryRelevanceVO> toVoList(List<ArticleCategoryRelevanceDomain> entity);

    List<ArticleCategoryRelevanceDomain> toDomainList(List<ArticleCategoryRelevanceVO> vo);
}

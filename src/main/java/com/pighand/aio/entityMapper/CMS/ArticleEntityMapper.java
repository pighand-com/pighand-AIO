package com.pighand.aio.entityMapper.CMS;

import com.pighand.aio.domain.CMS.ArticleDomain;
import com.pighand.aio.vo.CMS.ArticleVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * CMS - 文章
 *
 * @author wangshuli
 * @createDate 2024-04-22 15:11:06
 */
@Mapper(componentModel = "spring")
public interface ArticleEntityMapper {

    ArticleVO toVo(ArticleDomain entity);

    ArticleDomain toDomain(ArticleVO vo);

    List<ArticleVO> toVoList(List<ArticleDomain> entity);

    List<ArticleDomain> toDomainList(List<ArticleVO> vo);
}

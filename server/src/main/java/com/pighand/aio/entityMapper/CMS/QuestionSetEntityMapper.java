package com.pighand.aio.entityMapper.CMS;

import com.pighand.aio.domain.CMS.QuestionSetDomain;
import com.pighand.aio.vo.CMS.QuestionSetVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * CMS - 题目
 *
 * @author wangshuli
 * @createDate 2024-04-10 23:45:23
 */
@Mapper(componentModel = "spring")
public interface QuestionSetEntityMapper {

    QuestionSetVO toVo(QuestionSetDomain entity);

    QuestionSetDomain toDomain(QuestionSetVO vo);

    List<QuestionSetVO> toVoList(List<QuestionSetDomain> entity);

    List<QuestionSetDomain> toDomainList(List<QuestionSetVO> vo);
}

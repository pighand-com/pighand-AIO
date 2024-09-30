package com.pighand.aio.entityMapper.CMS;

import com.pighand.aio.domain.CMS.QuestionAnswerDomain;
import com.pighand.aio.vo.CMS.QuestionAnswerVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * CMS - 题目交卷
 *
 * @author wangshuli
 * @createDate 2024-04-10 23:45:23
 */
@Mapper(componentModel = "spring")
public interface QuestionAnswerEntityMapper {

    QuestionAnswerVO toVo(QuestionAnswerDomain entity);

    QuestionAnswerDomain toDomain(QuestionAnswerVO vo);

    List<QuestionAnswerVO> toVoList(List<QuestionAnswerDomain> entity);

    List<QuestionAnswerDomain> toDomainList(List<QuestionAnswerVO> vo);
}

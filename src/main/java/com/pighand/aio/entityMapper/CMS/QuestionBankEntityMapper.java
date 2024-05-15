package com.pighand.aio.entityMapper.CMS;

import com.pighand.aio.domain.CMS.QuestionBankDomain;
import com.pighand.aio.vo.CMS.QuestionBankVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * CMS - 题库
 *
 * @author wangshuli
 * @createDate 2024-04-10 23:45:23
 */
@Mapper(componentModel = "spring")
public interface QuestionBankEntityMapper {

    QuestionBankVO toVo(QuestionBankDomain entity);

    QuestionBankDomain toDomain(QuestionBankVO vo);

    List<QuestionBankVO> toVoList(List<QuestionBankDomain> entity);

    List<QuestionBankDomain> toDomainList(List<QuestionBankVO> vo);
}

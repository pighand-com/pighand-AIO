package com.pighand.aio.vo.CMS;

import com.pighand.aio.domain.CMS.QuestionSetDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * CMS - 题目
 *
 * @author wangshuli
 * @createDate 2024-04-10 23:45:23
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class QuestionSetVO extends QuestionSetDomain {

    // relation table: begin
    private QuestionBankVO questionBank;

    private QuestionAnswerVO questionAnswer;
    // relation table: end
}

package com.pighand.aio.vo.CMS;

import com.pighand.aio.domain.CMS.QuestionBankDomain;
import lombok.Data;

import java.util.List;

/**
 * CMS - 题库
 *
 * @author wangshuli
 * @createDate 2024-04-10 23:45:23
 */
@Data
public class QuestionBankVO extends QuestionBankDomain {

    // relation table: begin
    private List<QuestionSetVO> questionSet;
    // relation table: end
}

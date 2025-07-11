package com.pighand.aio.vo.CMS;

import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.CMS.QuestionAnswerDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * CMS - 题目交卷
 *
 * @author wangshuli
 * @createDate 2024-04-10 23:45:23
 */
@Data
@TableRef(QuestionAnswerDomain.class)
@EqualsAndHashCode(callSuper = false)
public class QuestionAnswerVO extends QuestionAnswerDomain {

    // relation table: begin
    private QuestionSetVO questionSet;
    // relation table: end

    private Long deviceId;

    private List<QuestionAnswerDomain> answers;
}

package com.pighand.aio.service.CMS.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.CMS.QuestionAnswerDomain;
import com.pighand.aio.mapper.CMS.QuestionAnswerMapper;
import com.pighand.aio.service.CMS.QuestionAnswerService;
import com.pighand.aio.vo.CMS.QuestionAnswerVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import org.springframework.stereotype.Service;

import static com.pighand.aio.domain.CMS.table.QuestionAnswerTableDef.QUESTION_ANSWER;
import static com.pighand.aio.domain.CMS.table.QuestionSetTableDef.QUESTION_SET;

/**
 * CMS - 题目交卷
 *
 * @author wangshuli
 * @createDate 2024-04-10 23:45:23
 */
@Service
public class QuestionAnswerServiceImpl extends BaseServiceImpl<QuestionAnswerMapper, QuestionAnswerDomain>
    implements QuestionAnswerService {

    /**
     * 创建
     *
     * @param questionAnswerVO
     * @return
     */
    @Override
    public QuestionAnswerVO create(QuestionAnswerVO questionAnswerVO) {
        super.mapper.insert(questionAnswerVO);

        return questionAnswerVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public QuestionAnswerDomain find(Long id) {
        return super.mapper.find(id, QUESTION_SET.getTableName());
    }

    /**
     * 分页或列表
     *
     * @param questionAnswerVO
     * @return PageOrList<QuestionAnswerVO>
     */
    @Override
    public PageOrList<QuestionAnswerVO> query(QuestionAnswerVO questionAnswerVO) {
        questionAnswerVO.setJoinTables(QUESTION_SET.getTableName());

        QueryWrapper queryWrapper = new QueryWrapper();

        // like
        queryWrapper.and(QUESTION_ANSWER.ANSWER_TEXT.like(questionAnswerVO.getAnswerText(), VerifyUtils::isNotEmpty));

        // equal
        queryWrapper.and(
            QUESTION_ANSWER.QUESTION_BANK_ID.eq(questionAnswerVO.getQuestionBankId(), VerifyUtils::isNotEmpty));
        queryWrapper.and(
            QUESTION_ANSWER.QUESTION_SET_ID.eq(questionAnswerVO.getQuestionSetId(), VerifyUtils::isNotEmpty));
        queryWrapper.and(QUESTION_ANSWER.CREATOR_ID.eq(questionAnswerVO.getCreatorId(), VerifyUtils::isNotEmpty));

        return super.mapper.query(questionAnswerVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param questionAnswerVO
     */
    @Override
    public void update(QuestionAnswerVO questionAnswerVO) {
        super.mapper.update(questionAnswerVO);
    }
}

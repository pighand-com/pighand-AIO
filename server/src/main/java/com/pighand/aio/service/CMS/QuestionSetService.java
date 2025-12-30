package com.pighand.aio.service.CMS;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.CMS.QuestionSetDomain;
import com.pighand.aio.mapper.CMS.QuestionSetMapper;
import com.pighand.aio.vo.CMS.QuestionSetVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import org.springframework.stereotype.Service;

import static com.pighand.aio.domain.CMS.table.QuestionAnswerTableDef.QUESTION_ANSWER;
import static com.pighand.aio.domain.CMS.table.QuestionBankTableDef.QUESTION_BANK;
import static com.pighand.aio.domain.CMS.table.QuestionSetTableDef.QUESTION_SET;

/**
 * CMS - 题目
 *
 * @author wangshuli
 * @createDate 2024-04-10 23:45:23
 */
@Service
public class QuestionSetService extends BaseServiceImpl<QuestionSetMapper, QuestionSetDomain>
     {

    /**
     * 创建
     *
     * @param questionSetVO
     * @return
     */
    public QuestionSetVO create(QuestionSetVO questionSetVO) {
        super.mapper.insert(questionSetVO);

        return questionSetVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public QuestionSetDomain find(Long id) {
        return super.mapper.find(id, QUESTION_BANK.getTableName(), QUESTION_ANSWER.getTableName(),
            QUESTION_BANK.getTableName());
    }

    /**
     * 分页或列表
     *
     * @param questionSetVO
     * @return PageOrList<QuestionSetVO>
     */
    public PageOrList<QuestionSetVO> query(QuestionSetVO questionSetVO) {
        questionSetVO.setJoinTables(QUESTION_BANK.getTableName(), QUESTION_ANSWER.getTableName(),
            QUESTION_BANK.getTableName());

        QueryWrapper queryWrapper = QueryWrapper.create()
            // like
            .and(QUESTION_SET.QUESTION.like(questionSetVO.getQuestion()))
            .and(QUESTION_SET.PROMPT.like(questionSetVO.getPrompt()))

            // equal
            .and(QUESTION_SET.QUESTION_BANK_ID.eq(questionSetVO.getQuestionBankId()))
            .and(QUESTION_SET.TYPE.eq(questionSetVO.getType())).and(QUESTION_SET.PAGE.eq(questionSetVO.getPage()));

        return super.mapper.query(questionSetVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param questionSetVO
     */
    public void update(QuestionSetVO questionSetVO) {
        super.mapper.update(questionSetVO);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(Long id) {
        super.mapper.deleteById(id);
    }
}

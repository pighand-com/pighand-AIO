package com.pighand.aio.service.CMS;

import com.pighand.aio.domain.CMS.QuestionAnswerDomain;
import com.pighand.aio.vo.CMS.QuestionAnswerVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;

/**
 * CMS - 题目交卷
 *
 * @author wangshuli
 * @createDate 2024-04-10 23:45:23
 */
public interface QuestionAnswerService extends BaseService<QuestionAnswerDomain> {

    /**
     * 创建
     *
     * @param questionAnswerVO
     * @return
     */
    QuestionAnswerVO create(QuestionAnswerVO questionAnswerVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    QuestionAnswerDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param questionAnswerVO
     * @return PageOrList<QuestionAnswerVO>
     */
    PageOrList<QuestionAnswerVO> query(QuestionAnswerVO questionAnswerVO);

    /**
     * 修改
     *
     * @param questionAnswerVO
     */
    void update(QuestionAnswerVO questionAnswerVO);
}

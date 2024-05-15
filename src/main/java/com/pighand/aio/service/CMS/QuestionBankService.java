package com.pighand.aio.service.CMS;

import com.pighand.aio.domain.CMS.QuestionAnswerDomain;
import com.pighand.aio.domain.CMS.QuestionBankDomain;
import com.pighand.aio.domain.CMS.QuestionSetDomain;
import com.pighand.aio.vo.CMS.QuestionAnswerResultVO;
import com.pighand.aio.vo.CMS.QuestionBankVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;

import java.util.List;

/**
 * CMS - 题库
 *
 * @author wangshuli
 * @createDate 2024-04-10 23:45:23
 */
public interface QuestionBankService extends BaseService<QuestionBankDomain> {

    /**
     * 创建
     *
     * @param questionBankVO
     * @return
     */
    QuestionBankVO create(QuestionBankVO questionBankVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    QuestionBankDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param questionBankVO
     * @return PageOrList<QuestionBankVO>
     */
    PageOrList<QuestionBankVO> query(QuestionBankVO questionBankVO);

    /**
     * 修改
     *
     * @param questionBankVO
     */
    void update(QuestionBankVO questionBankVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);

    /**
     * 获取题
     *
     * @param id
     * @return
     */
    List<QuestionSetDomain> getSet(Long id);

    /**
     * 答题
     *
     * @param questionBandId
     * @param deviceId
     * @param answer
     */
    QuestionAnswerResultVO answerSet(Long questionBandId, Long deviceId, List<QuestionAnswerDomain> answer);
}

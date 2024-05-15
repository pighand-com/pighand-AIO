package com.pighand.aio.service.CMS;

import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.aio.domain.CMS.QuestionSetDomain;
import com.pighand.aio.vo.CMS.QuestionSetVO;

/**
 * CMS - 题目
 *
 * @author wangshuli
 * @createDate 2024-04-10 23:45:23
 */
public interface QuestionSetService extends BaseService<QuestionSetDomain> {

    /**
     * 创建
     *
     * @param questionSetVO
     * @return
     */
    QuestionSetVO create(QuestionSetVO questionSetVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    QuestionSetDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param questionSetVO
     * @return PageOrList<QuestionSetVO>
     */
    PageOrList<QuestionSetVO> query(QuestionSetVO questionSetVO);

    /**
     * 修改
     *
     * @param questionSetVO
     */
    void update(QuestionSetVO questionSetVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}

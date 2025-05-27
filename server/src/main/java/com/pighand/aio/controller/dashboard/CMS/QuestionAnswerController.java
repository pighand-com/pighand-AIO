package com.pighand.aio.controller.dashboard.CMS;

import com.pighand.aio.domain.CMS.QuestionAnswerDomain;
import com.pighand.aio.service.CMS.QuestionAnswerService;
import com.pighand.aio.vo.CMS.QuestionAnswerVO;
import com.pighand.framework.spring.api.annotation.Get;
import com.pighand.framework.spring.api.annotation.Post;
import com.pighand.framework.spring.api.annotation.Put;
import com.pighand.framework.spring.api.annotation.RestController;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * CMS - 题目交卷
 *
 * @author wangshuli
 * @createDate 2024-04-10 23:45:23
 */
@RestController(path = "question/answer", docName = "CMS - 题目交卷")
public class QuestionAnswerController extends BaseController<QuestionAnswerService> {

    /**
     * @param questionAnswerVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "questionAnswerCreate")
    public Result<QuestionAnswerVO> create(
        @Validated({ValidationGroup.Create.class}) @RequestBody QuestionAnswerVO questionAnswerVO) {
        questionAnswerVO = super.service.create(questionAnswerVO);

        return new Result(questionAnswerVO);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<QuestionAnswerDomain> find(@PathVariable(name = "id") Long id) {
        QuestionAnswerDomain questionAnswerDomain = super.service.find(id);

        return new Result(questionAnswerDomain);
    }

    /**
     * @param questionAnswerVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "questionAnswerQuery")
    public Result<PageOrList<QuestionAnswerVO>> query(QuestionAnswerVO questionAnswerVO) {
        PageOrList<QuestionAnswerVO> result = super.service.query(questionAnswerVO);

        return new Result(result);
    }

    /**
     * @param questionAnswerVO
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "questionAnswerUpdate")
    public Result update(@PathVariable(name = "id") Long id, @RequestBody QuestionAnswerVO questionAnswerVO) {
        questionAnswerVO.setId(id);

        // Prohibit modifying fields
        questionAnswerVO.setCreatorId(null);
        questionAnswerVO.setCreatedAt(null);

        super.service.update(questionAnswerVO);

        return new Result();
    }
}

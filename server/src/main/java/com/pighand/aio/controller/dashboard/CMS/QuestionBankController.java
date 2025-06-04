package com.pighand.aio.controller.dashboard.CMS;

import com.pighand.aio.domain.CMS.QuestionBankDomain;
import com.pighand.aio.domain.CMS.QuestionSetDomain;
import com.pighand.aio.service.CMS.QuestionBankService;
import com.pighand.aio.vo.CMS.QuestionAnswerResultVO;
import com.pighand.aio.vo.CMS.QuestionAnswerVO;
import com.pighand.aio.vo.CMS.QuestionBankVO;
import com.pighand.aio.vo.CMS.QuestionSetVO;
import com.pighand.framework.spring.api.annotation.*;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * CMS - 题库
 *
 * @author wangshuli
 * @createDate 2024-04-10 23:45:23
 */
@RestController(path = "dashboard/question/bank", docName = "CMS - 题库")
public class QuestionBankController extends BaseController<QuestionBankService> {

    /**
     * @param questionBankVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "questionBankCreate")
    public Result<QuestionBankVO> create(
        @Validated({ValidationGroup.Create.class}) @RequestBody QuestionBankVO questionBankVO) {
        questionBankVO = super.service.create(questionBankVO);

        return new Result(questionBankVO);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<QuestionBankDomain> find(@PathVariable(name = "id") Long id) {
        QuestionBankDomain questionBankDomain = super.service.find(id);

        return new Result(questionBankDomain);
    }

    /**
     * @param questionBankVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "questionBankQuery")
    public Result<PageOrList<QuestionBankVO>> query(QuestionBankVO questionBankVO) {
        PageOrList<QuestionBankVO> result = super.service.query(questionBankVO);

        return new Result(result);
    }

    /**
     * @param questionBankVO
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "questionBankUpdate")
    public Result update(@PathVariable(name = "id") Long id, @RequestBody QuestionBankVO questionBankVO) {
        questionBankVO.setId(id);

        super.service.update(questionBankVO);

        return new Result();
    }

    /**
     * @param id
     */
    @Delete(path = "{id}", docSummary = "删除")
    public Result delete(@PathVariable(name = "id") Long id) {
        super.service.delete(id);
        return new Result();
    }

    /**
     * @param id
     */
    @Get(path = "{id}/set", docSummary = "获取题")
    public Result<PageOrList<QuestionSetVO>> querySet(@PathVariable(name = "id") Long id) {
        List<QuestionSetDomain> result = super.service.getSet(id);

        return new Result(result);
    }

    /**
     * @param id
     */
    @Post(path = "{id}/answer", docSummary = "答题")
    public Result<QuestionAnswerResultVO> answerSet(@PathVariable(name = "id") Long id,
        @RequestBody QuestionAnswerVO questionAnswerVO) {
        QuestionAnswerResultVO result =
            super.service.answerSet(id, questionAnswerVO.getDeviceId(), questionAnswerVO.getAnswers());

        return new Result(result);
    }
}

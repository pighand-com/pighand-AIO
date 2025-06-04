package com.pighand.aio.controller.dashboard.CMS;

import com.pighand.aio.domain.CMS.QuestionSetDomain;
import com.pighand.aio.service.CMS.QuestionSetService;
import com.pighand.aio.vo.CMS.QuestionSetVO;
import com.pighand.framework.spring.api.annotation.*;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * CMS - 题目
 *
 * @author wangshuli
 * @createDate 2024-04-10 23:45:23
 */
@RestController(path = "dashboard/question/set", docName = "CMS - 题目")
public class QuestionSetController extends BaseController<QuestionSetService> {

    /**
     * @param questionSetVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "questionSetCreate")
    public Result<QuestionSetVO> create(
        @Validated({ValidationGroup.Create.class}) @RequestBody QuestionSetVO questionSetVO) {
        questionSetVO = super.service.create(questionSetVO);

        return new Result(questionSetVO);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<QuestionSetDomain> find(@PathVariable(name = "id") Long id) {
        QuestionSetDomain questionSetDomain = super.service.find(id);

        return new Result(questionSetDomain);
    }

    /**
     * @param questionSetVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "questionSetQuery")
    public Result<PageOrList<QuestionSetVO>> query(QuestionSetVO questionSetVO) {
        PageOrList<QuestionSetVO> result = super.service.query(questionSetVO);

        return new Result(result);
    }

    /**
     * @param questionSetVO
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "questionSetUpdate")
    public Result update(@PathVariable(name = "id") Long id, @RequestBody QuestionSetVO questionSetVO) {
        questionSetVO.setId(id);

        super.service.update(questionSetVO);

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
}

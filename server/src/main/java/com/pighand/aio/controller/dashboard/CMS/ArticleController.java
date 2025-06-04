package com.pighand.aio.controller.dashboard.CMS;

import com.pighand.aio.service.CMS.ArticleService;
import com.pighand.aio.vo.CMS.ArticleVO;
import com.pighand.framework.spring.api.annotation.*;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * CMS - 文章
 *
 * @author wangshuli
 * @createDate 2024-04-22 15:11:06
 */
@RestController(path = "dashboard/article", docName = "CMS - 文章")
public class ArticleController extends BaseController<ArticleService> {

    /**
     * @param articleVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "articleCreate")
    public Result<ArticleVO> create(@Validated({ValidationGroup.Create.class}) @RequestBody ArticleVO articleVO) {
        articleVO = super.service.create(articleVO);

        return new Result(articleVO);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<ArticleVO> find(@PathVariable(name = "id") Long id) {
        ArticleVO articleDomain = super.service.find(id);

        return new Result(articleDomain);
    }

    /**
     * @param articleVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "articleQuery")
    public Result<PageOrList<ArticleVO>> query(ArticleVO articleVO) {
        PageOrList<ArticleVO> result = super.service.query(articleVO);

        return new Result(result);
    }

    /**
     * @param articleVO
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "articleUpdate")
    public Result update(@PathVariable(name = "id") Long id, @RequestBody ArticleVO articleVO) {
        articleVO.setId(id);

        // Prohibit modifying fields
        articleVO.setCreatorId(null);
        articleVO.setCreatedAt(null);

        super.service.update(articleVO);

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
     * @param articleVO
     * @return
     */
    @Post(path = "{id}/statistics", docSummary = "统计")
    public Result statistics(@PathVariable(name = "id") Long id, @RequestBody() ArticleVO articleVO) {
        super.service.statistics(id, articleVO.getStatisticsType());

        return new Result();
    }
}

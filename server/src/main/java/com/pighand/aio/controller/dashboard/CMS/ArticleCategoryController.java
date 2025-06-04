package com.pighand.aio.controller.dashboard.CMS;

import com.pighand.aio.domain.CMS.ArticleCategoryDomain;
import com.pighand.aio.service.CMS.ArticleCategoryService;
import com.pighand.aio.vo.CMS.ArticleCategoryVO;
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
 * CMS - 文章分类
 *
 * @author wangshuli
 * @createDate 2024-04-22 15:11:06
 */
@RestController(path = "dashboard/article/category", docName = "CMS - 文章分类")
public class ArticleCategoryController extends BaseController<ArticleCategoryService> {

    /**
     * @param articleCategoryVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "articleCategoryCreate")
    public Result<ArticleCategoryVO> create(
        @Validated({ValidationGroup.Create.class}) @RequestBody ArticleCategoryVO articleCategoryVO) {
        articleCategoryVO = super.service.create(articleCategoryVO);

        return new Result(articleCategoryVO);
    }

    @Get(path = "all", docSummary = "查询所有")
    public Result<List<ArticleCategoryDomain>> queryAll() {
        List<ArticleCategoryDomain> result = super.service.queryAll();

        return new Result(result);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<ArticleCategoryDomain> find(@PathVariable(name = "id") Long id) {
        ArticleCategoryDomain articleCategoryDomain = super.service.find(id);

        return new Result(articleCategoryDomain);
    }

    /**
     * @param articleCategoryVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "articleCategoryQuery")
    public Result<PageOrList<ArticleCategoryVO>> query(ArticleCategoryVO articleCategoryVO) {
        PageOrList<ArticleCategoryVO> result = super.service.query(articleCategoryVO);

        return new Result(result);
    }

    /**
     * @param articleCategoryVO
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "articleCategoryUpdate")
    public Result update(@PathVariable(name = "id") Long id, @RequestBody ArticleCategoryVO articleCategoryVO) {
        articleCategoryVO.setId(id);

        super.service.update(articleCategoryVO);

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

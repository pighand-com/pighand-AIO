package com.pighand.aio.controller.dashboard.CMS;

import com.pighand.aio.domain.CMS.AssetsDocDomain;
import com.pighand.aio.service.CmsAssetsDocService;
import com.pighand.aio.vo.CMS.AssetsDocVO;
import com.pighand.framework.spring.api.annotation.*;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * CMS - 素材 - 文档
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@RestController(path = "cms/assets/doc", docName = "CMS - 素材 - 文档")
public class AssetsDocController extends BaseController<CmsAssetsDocService> {

    /**
     * @param cmsAssetsDocVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "cmsAssetsDocCreate")
    public Result<AssetsDocVO> create(
        @Validated({ValidationGroup.Create.class}) @RequestBody AssetsDocVO cmsAssetsDocVO) {
        cmsAssetsDocVO = super.service.create(cmsAssetsDocVO);

        return new Result(cmsAssetsDocVO);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<AssetsDocDomain> find(@PathVariable(name = "id") Long id) {
        AssetsDocDomain cmsAssetsDocDomain = super.service.find(id);

        return new Result(cmsAssetsDocDomain);
    }

    /**
     * @param cmsAssetsDocVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "cmsAssetsDocQuery")
    public Result<PageOrList<AssetsDocVO>> query(AssetsDocVO cmsAssetsDocVO) {
        PageOrList<AssetsDocVO> result = super.service.query(cmsAssetsDocVO);

        return new Result(result);
    }

    /**
     * @param cmsAssetsDocVO
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "cmsAssetsDocUpdate")
    public Result update(@PathVariable(name = "id") Long id, @RequestBody AssetsDocVO cmsAssetsDocVO) {
        cmsAssetsDocVO.setId(id);

        super.service.update(cmsAssetsDocVO);

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

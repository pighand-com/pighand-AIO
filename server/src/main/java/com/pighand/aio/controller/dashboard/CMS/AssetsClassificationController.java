package com.pighand.aio.controller.dashboard.CMS;

import com.pighand.aio.domain.CMS.AssetsClassificationDomain;
import com.pighand.aio.service.CMS.AssetsClassificationService;
import com.pighand.aio.vo.CMS.AssetsClassificationVO;
import com.pighand.framework.spring.api.annotation.*;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * CMS - 素材 - 分类
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@RestController(path = "dashboard/assets/classification", docName = "CMS - 素材 - 分类")
public class AssetsClassificationController extends BaseController<AssetsClassificationService> {

    /**
     * @param cmsAssetsClassificationVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "cmsAssetsClassificationCreate")
    public Result<AssetsClassificationVO> create(
        @Validated({ValidationGroup.Create.class}) @RequestBody AssetsClassificationVO cmsAssetsClassificationVO) {
        cmsAssetsClassificationVO = super.service.create(cmsAssetsClassificationVO);

        return new Result(cmsAssetsClassificationVO);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<AssetsClassificationDomain> find(@PathVariable(name = "id") Long id) {
        AssetsClassificationDomain cmsAssetsClassificationDomain = super.service.find(id);

        return new Result(cmsAssetsClassificationDomain);
    }

    /**
     * @param cmsAssetsClassificationVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "cmsAssetsClassificationQuery")
    public Result<PageOrList<AssetsClassificationVO>> query(AssetsClassificationVO cmsAssetsClassificationVO) {
        PageOrList<AssetsClassificationVO> result = super.service.query(cmsAssetsClassificationVO);

        return new Result(result);
    }

    /**
     * @param cmsAssetsClassificationVO
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "cmsAssetsClassificationUpdate")
    public Result update(@PathVariable(name = "id") Long id,
        @RequestBody AssetsClassificationVO cmsAssetsClassificationVO) {
        cmsAssetsClassificationVO.setId(id);

        super.service.update(cmsAssetsClassificationVO);

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

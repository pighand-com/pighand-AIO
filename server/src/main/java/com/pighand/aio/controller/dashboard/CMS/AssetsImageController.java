package com.pighand.aio.controller.dashboard.CMS;

import com.pighand.aio.domain.CMS.AssetsImageDomain;
import com.pighand.aio.service.CmsAssetsImageService;
import com.pighand.aio.vo.CMS.AssetsImageVO;
import com.pighand.framework.spring.api.annotation.*;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * CMS - 素材 - 图片
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@RestController(path = "cms/assets/image", docName = "CMS - 素材 - 图片")
public class AssetsImageController extends BaseController<CmsAssetsImageService> {

    /**
     * @param cmsAssetsImageVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "cmsAssetsImageCreate")
    public Result<AssetsImageVO> create(
        @Validated({ValidationGroup.Create.class}) @RequestBody AssetsImageVO cmsAssetsImageVO) {
        cmsAssetsImageVO = super.service.create(cmsAssetsImageVO);

        return new Result(cmsAssetsImageVO);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<AssetsImageDomain> find(@PathVariable(name = "id") Long id) {
        AssetsImageDomain cmsAssetsImageDomain = super.service.find(id);

        return new Result(cmsAssetsImageDomain);
    }

    /**
     * @param cmsAssetsImageVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "cmsAssetsImageQuery")
    public Result<PageOrList<AssetsImageVO>> query(AssetsImageVO cmsAssetsImageVO) {
        PageOrList<AssetsImageVO> result = super.service.query(cmsAssetsImageVO);

        return new Result(result);
    }

    /**
     * @param cmsAssetsImageVO
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "cmsAssetsImageUpdate")
    public Result update(@PathVariable(name = "id") Long id, @RequestBody AssetsImageVO cmsAssetsImageVO) {
        cmsAssetsImageVO.setId(id);

        super.service.update(cmsAssetsImageVO);

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

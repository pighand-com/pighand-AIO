package com.pighand.aio.controller.client.CMS;

import com.pighand.aio.domain.CMS.AssetsFavouriteDomain;
import com.pighand.aio.service.CMS.AssetsFavouriteService;
import com.pighand.aio.vo.CMS.AssetsFavouriteVO;
import com.pighand.framework.spring.api.annotation.*;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * CMS - 素材 - 收藏
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@RestController(path = "cms/assets/favourite", docName = "CMS - 素材 - 收藏")
public class AssetsFavouriteController extends BaseController<AssetsFavouriteService> {

    /**
     * @param cmsAssetsFavouriteVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "cmsAssetsFavouriteCreate")
    public Result<AssetsFavouriteVO> create(
        @Validated({ValidationGroup.Create.class}) @RequestBody AssetsFavouriteVO cmsAssetsFavouriteVO) {
        cmsAssetsFavouriteVO = super.service.create(cmsAssetsFavouriteVO);

        return new Result(cmsAssetsFavouriteVO);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<AssetsFavouriteDomain> find(@PathVariable(name = "id") Long id) {
        AssetsFavouriteDomain cmsAssetsFavouriteDomain = super.service.find(id);

        return new Result(cmsAssetsFavouriteDomain);
    }

    /**
     * @param cmsAssetsFavouriteVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "cmsAssetsFavouriteQuery")
    public Result<PageOrList<AssetsFavouriteVO>> query(AssetsFavouriteVO cmsAssetsFavouriteVO) {
        PageOrList<AssetsFavouriteVO> result = super.service.query(cmsAssetsFavouriteVO);

        return new Result(result);
    }

    /**
     * @param cmsAssetsFavouriteVO
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "cmsAssetsFavouriteUpdate")
    public Result update(@PathVariable(name = "id") Long id, @RequestBody AssetsFavouriteVO cmsAssetsFavouriteVO) {
        cmsAssetsFavouriteVO.setId(id);

        super.service.update(cmsAssetsFavouriteVO);

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

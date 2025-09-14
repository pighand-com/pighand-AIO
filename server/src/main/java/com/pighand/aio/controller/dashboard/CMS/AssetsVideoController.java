package com.pighand.aio.controller.dashboard.CMS;

import com.pighand.aio.domain.CMS.AssetsVideoDomain;
import com.pighand.aio.service.CMS.AssetsVideoService;
import com.pighand.aio.vo.CMS.AssetsVideoVO;
import com.pighand.framework.spring.api.annotation.*;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import static com.pighand.aio.domain.CMS.table.AssetsClassificationTableDef.ASSETS_CLASSIFICATION;

/**
 * CMS - 素材 - 视频
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@RestController(path = "dashboard/assets/video", docName = "CMS - 素材 - 视频")
public class AssetsVideoController extends BaseController<AssetsVideoService> {

    /**
     * @param cmsAssetsVideoVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "cmsAssetsVideoCreate")
    public Result<AssetsVideoVO> create(
        @Validated({ValidationGroup.Create.class}) @RequestBody AssetsVideoVO cmsAssetsVideoVO) {
        cmsAssetsVideoVO = super.service.create(cmsAssetsVideoVO);

        return new Result(cmsAssetsVideoVO);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<AssetsVideoDomain> find(@PathVariable(name = "id") Long id) {
        AssetsVideoDomain cmsAssetsVideoDomain = super.service.find(id);

        return new Result(cmsAssetsVideoDomain);
    }

    /**
     * @param cmsAssetsVideoVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "cmsAssetsVideoQuery")
    public Result<PageOrList<AssetsVideoVO>> query(AssetsVideoVO cmsAssetsVideoVO) {
        cmsAssetsVideoVO.setJoinTables(ASSETS_CLASSIFICATION.getTableName());

        PageOrList<AssetsVideoVO> result = super.service.query(cmsAssetsVideoVO);

        return new Result(result);
    }

    /**
     * @param cmsAssetsVideoVO
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "cmsAssetsVideoUpdate")
    public Result update(@PathVariable(name = "id") Long id, @RequestBody AssetsVideoVO cmsAssetsVideoVO) {
        cmsAssetsVideoVO.setId(id);

        super.service.update(cmsAssetsVideoVO);

        return new Result();
    }

    /**
     * @param id
     */
    @Put(path = "{id}/on-shelf", docSummary = "上架")
    public Result onShelf(@PathVariable(name = "id") Long id) {
        super.service.onShelf(id);
        return new Result();
    }

    /**
     * @param id
     */
    @Put(path = "{id}/off-shelf", docSummary = "下架")
    public Result offShelf(@PathVariable(name = "id") Long id) {
        super.service.offShelf(id);
        return new Result();
    }

    /**
     * @param id
     */
    @Put(path = "{id}/set-handpick", docSummary = "设置精选")
    public Result setHandpick(@PathVariable(name = "id") Long id) {
        super.service.setHandpick(id);
        return new Result();
    }

    /**
     * @param id
     */
    @Put(path = "{id}/cancel-handpick", docSummary = "取消精选")
    public Result cancelHandpick(@PathVariable(name = "id") Long id) {
        super.service.cancelHandpick(id);
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

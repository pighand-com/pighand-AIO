package com.pighand.aio.controller.dashboard.CMS;

import com.pighand.aio.common.interfaces.ApplicationId;
import com.pighand.aio.common.interfaces.Authorization;
import com.pighand.aio.domain.CMS.AssetsDomain;
import com.pighand.aio.service.CMS.AssetsService;
import com.pighand.aio.vo.CMS.AssetsVO;
import com.pighand.framework.spring.api.annotation.*;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 公共 - 素材
 *
 * @author wangshuli
 * @createDate 2024-12-31 19:04:50
 */
@Authorization
@ApplicationId
@RestController(path = "dashboard/assets", docName = "公共 - 素材")
public class AssetsController extends BaseController<AssetsService> {

    /**
     * @param comAssetsVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "comAssetsCreate")
    public Result<AssetsVO> create(@Validated({ValidationGroup.Create.class}) @RequestBody AssetsVO comAssetsVO) {
        comAssetsVO = super.service.create(comAssetsVO);

        return new Result(comAssetsVO);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<AssetsDomain> find(@PathVariable(name = "id") Long id) {
        AssetsDomain comAssetsDomain = super.service.find(id);

        return new Result(comAssetsDomain);
    }

    /**
     * @param comAssetsVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "comAssetsQuery")
    public Result<PageOrList<AssetsVO>> query(AssetsVO comAssetsVO) {
        PageOrList<AssetsVO> result = super.service.query(comAssetsVO);

        return new Result(result);
    }

    /**
     * @param comAssetsVO
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "comAssetsUpdate")
    public Result update(@PathVariable(name = "id") Long id, @RequestBody AssetsVO comAssetsVO) {
        comAssetsVO.setId(id);

        super.service.update(comAssetsVO);

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

    @Delete(docSummary = "批量删除")
    public Result batchDelete(@RequestBody AssetsVO comAssetsVO) {
        super.service.batchDelete(comAssetsVO.getIds());
        return new Result();
    }
}

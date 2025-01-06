package com.pighand.aio.controller.common;

import com.pighand.framework.spring.api.annotation.*;
import com.pighand.framework.spring.api.annotation.RestController;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import com.pighand.aio.domain.common.AssetsDomain;
import com.pighand.aio.service.common.AssetsService;
import com.pighand.aio.vo.common.AssetsVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 公共 - 素材
 *
 * @author wangshuli
 * @createDate 2024-12-31 19:04:50
 */
@RestController(path = "com/assets", docName = "公共 - 素材")
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
}
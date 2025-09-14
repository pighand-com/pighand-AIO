package com.pighand.aio.controller.dashboard.CMS;

import com.pighand.aio.domain.CMS.AssetsCollectionDomain;
import com.pighand.aio.service.CMS.AssetsCollectionService;
import com.pighand.aio.vo.CMS.AssetsCollectionVO;
import com.pighand.framework.spring.api.annotation.*;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import static com.pighand.aio.domain.base.table.UserExtensionTableDef.USER_EXTENSION;

/**
 * CMS - 素材 - 专辑
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@RestController(path = "dashboard/assets/collection", docName = "CMS - 素材 - 专辑")
public class AssetsCollectionController extends BaseController<AssetsCollectionService> {

    /**
     * @param cmsAssetsCollectionVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "cmsAssetsCollectionCreate")
    public Result<AssetsCollectionVO> create(
        @Validated({ValidationGroup.Create.class}) @RequestBody AssetsCollectionVO cmsAssetsCollectionVO) {
        cmsAssetsCollectionVO = super.service.create(cmsAssetsCollectionVO);

        return new Result(cmsAssetsCollectionVO);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<AssetsCollectionDomain> find(@PathVariable(name = "id") Long id) {
        AssetsCollectionDomain cmsAssetsCollectionDomain = super.service.find(id);

        return new Result(cmsAssetsCollectionDomain);
    }

    /**
     * @param cmsAssetsCollectionVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "cmsAssetsCollectionQuery")
    public Result<PageOrList<AssetsCollectionVO>> query(AssetsCollectionVO cmsAssetsCollectionVO) {
        cmsAssetsCollectionVO.setJoinTables(USER_EXTENSION.getName());

        PageOrList<AssetsCollectionVO> result = super.service.query(cmsAssetsCollectionVO);

        return new Result(result);
    }

    /**
     * @param cmsAssetsCollectionVO
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "cmsAssetsCollectionUpdate")
    public Result update(@PathVariable(name = "id") Long id, @RequestBody AssetsCollectionVO cmsAssetsCollectionVO) {
        cmsAssetsCollectionVO.setId(id);

        super.service.update(cmsAssetsCollectionVO);

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

package com.pighand.aio.controller.client.CMS;

import com.pighand.aio.service.CMS.AssetsCollectionService;
import com.pighand.aio.vo.CMS.AssetsCollectionVO;
import com.pighand.framework.spring.api.annotation.Get;
import com.pighand.framework.spring.api.annotation.RestController;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.page.PageType;
import com.pighand.framework.spring.response.Result;

import java.util.List;

import static com.pighand.aio.domain.base.table.UserExtensionTableDef.USER_EXTENSION;

/**
 * 客户端 - CMS - 素材 - 专辑
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@RestController(path = "client/assets/collection", docName = "客户端 - CMS - 素材 - 专辑")
public class AssetsCollectionController extends BaseController<AssetsCollectionService> {

    /**
     * 获取专辑列表（不分页）
     * 客户端用于展示所有可用的专辑选项
     *
     * @param cmsAssetsCollectionVO 查询条件
     * @return 专辑列表
     */
    @Get(docSummary = "专辑列表")
    public Result<List<AssetsCollectionVO>> list(AssetsCollectionVO cmsAssetsCollectionVO) {
        // 设置为列表模式（不分页）
        cmsAssetsCollectionVO.setPageType(PageType.LIST);
        
        PageOrList<AssetsCollectionVO> result = super.service.query(cmsAssetsCollectionVO);
        
        return new Result(result.getRecords());
    }
}
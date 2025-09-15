package com.pighand.aio.controller.client.CMS;

import com.pighand.aio.domain.CMS.AssetsDocDomain;
import com.pighand.aio.service.CMS.AssetsDocService;
import com.pighand.aio.vo.CMS.AssetsDocVO;
import com.pighand.framework.spring.api.annotation.Get;
import com.pighand.framework.spring.api.annotation.RestController;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import org.springframework.web.bind.annotation.PathVariable;

import static com.pighand.aio.domain.CMS.table.AssetsClassificationTableDef.ASSETS_CLASSIFICATION;

/**
 * 客户端 - CMS - 素材 - 文档
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@RestController(path = "client/assets/doc", docName = "客户端 - CMS - 素材 - 文档")
public class AssetsDocController extends BaseController<AssetsDocService> {

    /**
     * 获取文档详情
     * 客户端用于查看文档的详细信息
     *
     * @param id 文档ID
     * @return 文档详情
     */
    @Get(path = "{id}", docSummary = "文档详情")
    public Result<AssetsDocDomain> find(@PathVariable(name = "id") Long id) {
        AssetsDocDomain cmsAssetsDocDomain = super.service.find(id);
        
        return new Result(cmsAssetsDocDomain);
    }

    /**
     * 获取文档列表（分页）
     * 客户端用于浏览文档列表，支持分页和筛选
     *
     * @param cmsAssetsDocVO 查询条件
     * @return 文档分页列表
     */
    @Get(docSummary = "文档列表")
    public Result<PageOrList<AssetsDocVO>> query(AssetsDocVO cmsAssetsDocVO) {
        // 关联分类信息
        cmsAssetsDocVO.setJoinTables(ASSETS_CLASSIFICATION.getTableName());
        
        PageOrList<AssetsDocVO> result = super.service.query(cmsAssetsDocVO);
        
        return new Result(result);
    }
}
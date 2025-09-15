package com.pighand.aio.controller.client.CMS;

import com.pighand.aio.service.CMS.AssetsClassificationService;
import com.pighand.aio.vo.CMS.AssetsClassificationVO;
import com.pighand.framework.spring.api.annotation.Get;
import com.pighand.framework.spring.api.annotation.RestController;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.page.PageType;
import com.pighand.framework.spring.response.Result;

import java.util.List;

/**
 * 客户端 - CMS - 素材 - 分类
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@RestController(path = "client/assets/classification", docName = "客户端 - CMS - 素材 - 分类")
public class AssetsClassificationController extends BaseController<AssetsClassificationService> {

    /**
     * 获取分类列表（不分页）
     * 客户端用于展示所有可用的分类选项
     *
     * @param cmsAssetsClassificationVO 查询条件
     * @return 分类列表
     */
    @Get(docSummary = "分类列表")
    public Result<List<AssetsClassificationVO>> list(AssetsClassificationVO cmsAssetsClassificationVO) {
        // 设置为列表模式（不分页）
        cmsAssetsClassificationVO.setPageType(PageType.LIST);
        
        PageOrList<AssetsClassificationVO> result = super.service.query(cmsAssetsClassificationVO);
        
        return new Result(result.getRecords());
    }
}
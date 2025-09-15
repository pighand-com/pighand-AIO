package com.pighand.aio.controller.client.CMS;

import com.pighand.aio.domain.CMS.AssetsImageDomain;
import com.pighand.aio.service.CMS.AssetsImageService;
import com.pighand.aio.vo.CMS.AssetsImageVO;
import com.pighand.framework.spring.api.annotation.Get;
import com.pighand.framework.spring.api.annotation.RestController;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import org.springframework.web.bind.annotation.PathVariable;

import static com.pighand.aio.domain.CMS.table.AssetsClassificationTableDef.ASSETS_CLASSIFICATION;

/**
 * 客户端 - CMS - 素材 - 图片
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@RestController(path = "client/assets/image", docName = "客户端 - CMS - 素材 - 图片")
public class AssetsImageController extends BaseController<AssetsImageService> {

    /**
     * 获取图片详情
     * 客户端用于查看图片的详细信息
     *
     * @param id 图片ID
     * @return 图片详情
     */
    @Get(path = "{id}", docSummary = "图片详情")
    public Result<AssetsImageDomain> find(@PathVariable(name = "id") Long id) {
        AssetsImageDomain cmsAssetsImageDomain = super.service.find(id);
        
        return new Result(cmsAssetsImageDomain);
    }

    /**
     * 获取图片列表（分页）
     * 客户端用于浏览图片列表，支持分页和筛选
     *
     * @param cmsAssetsImageVO 查询条件
     * @return 图片分页列表
     */
    @Get(docSummary = "图片列表")
    public Result<PageOrList<AssetsImageVO>> query(AssetsImageVO cmsAssetsImageVO) {
        // 关联分类信息
        cmsAssetsImageVO.setJoinTables(ASSETS_CLASSIFICATION.getTableName());
        
        PageOrList<AssetsImageVO> result = super.service.query(cmsAssetsImageVO);
        
        return new Result(result);
    }
}
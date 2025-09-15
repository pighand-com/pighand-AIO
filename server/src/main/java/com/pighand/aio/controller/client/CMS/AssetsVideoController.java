package com.pighand.aio.controller.client.CMS;

import com.pighand.aio.domain.CMS.AssetsVideoDomain;
import com.pighand.aio.service.CMS.AssetsVideoService;
import com.pighand.aio.vo.CMS.AssetsVideoVO;
import com.pighand.framework.spring.api.annotation.Get;
import com.pighand.framework.spring.api.annotation.RestController;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import org.springframework.web.bind.annotation.PathVariable;

import static com.pighand.aio.domain.CMS.table.AssetsClassificationTableDef.ASSETS_CLASSIFICATION;

/**
 * 客户端 - CMS - 素材 - 视频
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@RestController(path = "client/assets/video", docName = "客户端 - CMS - 素材 - 视频")
public class AssetsVideoController extends BaseController<AssetsVideoService> {

    /**
     * 获取视频详情
     * 客户端用于查看视频的详细信息
     *
     * @param id 视频ID
     * @return 视频详情
     */
    @Get(path = "{id}", docSummary = "视频详情")
    public Result<AssetsVideoDomain> find(@PathVariable(name = "id") Long id) {
        AssetsVideoDomain cmsAssetsVideoDomain = super.service.find(id);
        
        return new Result(cmsAssetsVideoDomain);
    }

    /**
     * 获取视频列表（分页）
     * 客户端用于浏览视频列表，支持分页和筛选
     *
     * @param cmsAssetsVideoVO 查询条件
     * @return 视频分页列表
     */
    @Get(docSummary = "视频列表")
    public Result<PageOrList<AssetsVideoVO>> query(AssetsVideoVO cmsAssetsVideoVO) {
        // 关联分类信息
        cmsAssetsVideoVO.setJoinTables(ASSETS_CLASSIFICATION.getTableName());
        
        PageOrList<AssetsVideoVO> result = super.service.query(cmsAssetsVideoVO);
        
        return new Result(result);
    }
}
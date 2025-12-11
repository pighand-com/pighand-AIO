package com.pighand.aio.controller.client.CMS;

import com.pighand.aio.service.CMS.AssetsAggregateService;
import com.pighand.aio.service.CMS.AssetsDocService;
import com.pighand.aio.service.CMS.AssetsImageService;
import com.pighand.aio.service.CMS.AssetsVideoService;
import com.pighand.aio.vo.CMS.AssetsAggregateVO;
import com.pighand.aio.vo.CMS.AssetsDocVO;
import com.pighand.aio.vo.CMS.AssetsImageVO;
import com.pighand.aio.vo.CMS.AssetsVideoVO;
import com.pighand.framework.spring.api.annotation.Get;
import com.pighand.framework.spring.api.annotation.RestController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;

import static com.pighand.aio.domain.CMS.table.AssetsClassificationTableDef.ASSETS_CLASSIFICATION;

/**
 * 客户端 - CMS - 素材统一管理
 * 提供文档、图片、视频三种素材类型的统一查询接口
 *
 * @author wangshuli
 * @createDate 2025-01-21
 */
@RestController(path = "client/assets", docName = "客户端 - CMS - 素材统一管理")
@RequiredArgsConstructor
public class AssetsController {

    private final AssetsAggregateService assetsAggregateService;

    private final AssetsDocService assetsDocService;

    private final AssetsImageService assetsImageService;

    private final AssetsVideoService assetsVideoService;

    // ==================== 聚合查询接口 ====================

    /**
     * 聚合查询素材（文档、图片、视频）
     * 客户端用于统一查询多种类型的素材，支持分页和筛选
     * 支持按素材类型、分类、状态等条件查询
     *
     * @param assetsAggregateVO 查询条件
     * @return 聚合查询结果
     */
    @Get(path = "aggregate", docSummary = "素材聚合查询")
    public Result<PageOrList<AssetsAggregateVO>> queryAggregate(AssetsAggregateVO assetsAggregateVO) {
        // 关联分类信息
        assetsAggregateVO.setJoinTables(ASSETS_CLASSIFICATION.getTableName());

        PageOrList<AssetsAggregateVO> result = assetsAggregateService.queryAggregate(assetsAggregateVO);

        return new Result(result);
    }

    // ==================== 文档素材接口 ====================

    /**
     * 获取文档详情
     * 客户端用于查看文档的详细信息
     *
     * @param id 文档ID
     * @return 文档详情
     */
    @Get(path = "doc/{id}", docSummary = "文档详情")
    public Result<AssetsDocVO> findDoc(@PathVariable(name = "id") Long id) {
        AssetsDocVO assetsDocVO = assetsDocService.find(id);

        return new Result(assetsDocVO);
    }

    /**
     * 获取文档列表（分页）
     * 客户端用于浏览文档列表，支持分页和筛选
     *
     * @param assetsDocVO 查询条件
     * @return 文档分页列表
     */
    @Get(path = "doc", docSummary = "文档列表")
    public Result<PageOrList<AssetsDocVO>> queryDoc(AssetsDocVO assetsDocVO) {
        // 关联分类信息
        assetsDocVO.setJoinTables(ASSETS_CLASSIFICATION.getTableName());

        PageOrList<AssetsDocVO> result = assetsDocService.query(assetsDocVO);

        return new Result(result);
    }

    // ==================== 图片素材接口 ====================

    /**
     * 获取图片详情
     * 客户端用于查看图片的详细信息
     *
     * @param id 图片ID
     * @return 图片详情
     */
    @Get(path = "image/{id}", docSummary = "图片详情")
    public Result<AssetsImageVO> findImage(@PathVariable(name = "id") Long id) {
        AssetsImageVO assetsImageVO = assetsImageService.find(id);

        return new Result(assetsImageVO);
    }

    /**
     * 获取图片列表（分页）
     * 客户端用于浏览图片列表，支持分页和筛选
     *
     * @param assetsImageVO 查询条件
     * @return 图片分页列表
     */
    @Get(path = "image", docSummary = "图片列表")
    public Result<PageOrList<AssetsImageVO>> queryImage(AssetsImageVO assetsImageVO) {
        // 关联分类信息
        assetsImageVO.setJoinTables(ASSETS_CLASSIFICATION.getTableName());

        PageOrList<AssetsImageVO> result = assetsImageService.query(assetsImageVO);

        return new Result(result);
    }

    // ==================== 视频素材接口 ====================

    /**
     * 获取视频详情
     * 客户端用于查看视频的详细信息
     *
     * @param id 视频ID
     * @return 视频详情
     */
    @Get(path = "video/{id}", docSummary = "视频详情")
    public Result<AssetsVideoVO> findVideo(@PathVariable(name = "id") Long id) {
        AssetsVideoVO assetsVideoVO = assetsVideoService.find(id);

        return new Result(assetsVideoVO);
    }

    /**
     * 获取视频列表（分页）
     * 客户端用于浏览视频列表，支持分页和筛选
     *
     * @param assetsVideoVO 查询条件
     * @return 视频分页列表
     */
    @Get(path = "video", docSummary = "视频列表")
    public Result<PageOrList<AssetsVideoVO>> queryVideo(AssetsVideoVO assetsVideoVO) {
        // 关联分类信息
        assetsVideoVO.setJoinTables(ASSETS_CLASSIFICATION.getTableName());

        PageOrList<AssetsVideoVO> result = assetsVideoService.query(assetsVideoVO);

        return new Result(result);
    }
}
package com.pighand.aio.controller.client.CMS;

import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.service.CMS.AssetsDownloadService;
import com.pighand.aio.vo.CMS.AssetsDownloadVO;
import com.pighand.framework.spring.api.annotation.Get;
import com.pighand.framework.spring.api.annotation.Post;
import com.pighand.framework.spring.api.annotation.RestController;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * CMS - 素材 - 下载记录
 *
 * @author wangshuli
 * @createDate 2025-01-25 10:00:00
 */
@RestController(path = "client/assets/download", docName = "CMS - 素材 - 下载记录")
public class AssetsDownloadController extends BaseController<AssetsDownloadService> {

    /**
     * 创建下载记录
     * 多次下载同一素材时，更新最新的下载时间
     *
     * @param assetsId   素材ID
     * @param assetsType 素材类型 10图片 20视频 30文档
     * @return 下载记录
     */
    @Post(path = "record/{assetsId}/{assetsType}", docSummary = "创建下载记录")
    public Result<AssetsDownloadVO> createDownloadRecord(@PathVariable(name = "assetsId") Long assetsId,
        @PathVariable(name = "assetsType") Integer assetsType) {

        Long userId = Context.loginUser().getId();
        AssetsDownloadVO result = super.service.createOrUpdateDownload(assetsId, assetsType, userId);

        return new Result<>(result);
    }

    /**
     * 查询已下载的资源列表
     *
     * @param assetsType 素材类型（可选）10图片 20视频 30文档
     * @param pageNumber 页码
     * @param pageSize   每页大小
     * @return 下载列表
     */
    @Get(path = "list", docSummary = "查询已下载的资源列表")
    public Result<PageOrList<AssetsDownloadVO>> getDownloadList(
        @RequestParam(name = "assetsType", required = false) Integer assetsType,
        @RequestParam(name = "pageNumber", required = false) Long pageNumber,
        @RequestParam(name = "pageSize", required = false) Long pageSize) {

        Long userId = Context.loginUser().getId();
        PageOrList<AssetsDownloadVO> result = super.service.getDownloadList(userId, assetsType, pageNumber, pageSize);

        return new Result<>(result);
    }
}
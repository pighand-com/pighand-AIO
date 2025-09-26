package com.pighand.aio.controller.client.CMS;

import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.service.CMS.AssetsFavouriteService;
import com.pighand.aio.vo.CMS.AssetsFavouriteVO;
import com.pighand.framework.spring.api.annotation.Delete;
import com.pighand.framework.spring.api.annotation.Get;
import com.pighand.framework.spring.api.annotation.Post;
import com.pighand.framework.spring.api.annotation.RestController;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

/**
 * CMS - 素材 - 收藏
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@RestController(path = "client/assets/favourite", docName = "CMS - 素材 - 收藏")
public class AssetsFavouriteController extends BaseController<AssetsFavouriteService> {

    /**
     * 查询当前素材，登录人是否已收藏
     *
     * @param assetsId   素材ID
     * @param assetsType 素材类型 10图片 20视频 30文档
     * @return 是否已收藏
     */
    @Get(path = "check/{assetsId}/{assetsType}", docSummary = "查询是否已收藏")
    public Result<Map<String, Boolean>> checkFavourite(@PathVariable(name = "assetsId") Long assetsId,
        @PathVariable(name = "assetsType") Integer assetsType) {

        Long userId = Context.loginUser().getId();
        boolean isFavourited = super.service.isFavourited(assetsId, assetsType, userId);

        Map<String, Boolean> result = new HashMap<>();
        result.put("isFavourited", isFavourited);

        return new Result<>(result);
    }

    /**
     * 收藏素材
     *
     * @param assetsId   素材ID
     * @param assetsType 素材类型 10图片 20视频 30文档
     * @return 收藏记录
     */
    @Post(path = "add/{assetsId}/{assetsType}", docSummary = "收藏素材")
    public Result<AssetsFavouriteVO> addFavourite(@PathVariable(name = "assetsId") Long assetsId,
        @PathVariable(name = "assetsType") Integer assetsType) {

        Long userId = Context.loginUser().getId();
        AssetsFavouriteVO result = super.service.addFavourite(assetsId, assetsType, userId);

        return new Result<>(result);
    }

    /**
     * 取消收藏
     *
     * @param assetsId   素材ID
     * @param assetsType 素材类型 10图片 20视频 30文档
     * @return 操作结果
     */
    @Delete(path = "remove/{assetsId}/{assetsType}", docSummary = "取消收藏")
    public Result<Void> removeFavourite(@PathVariable(name = "assetsId") Long assetsId,
        @PathVariable(name = "assetsType") Integer assetsType) {

        Long userId = Context.loginUser().getId();
        super.service.removeFavourite(assetsId, assetsType, userId);

        return new Result<>();
    }

    /**
     * 收藏列表
     *
     * @param assetsType 素材类型（可选）10图片 20视频 30文档
     * @param pageNumber 页码
     * @param pageSize   每页大小
     * @return 收藏列表
     */
    @Get(path = "list", docSummary = "收藏列表")
    public Result<PageOrList<AssetsFavouriteVO>> getFavouriteList(
        @RequestParam(name = "assetsType", required = false) Integer assetsType,
        @RequestParam(name = "pageNumber", required = false) Long pageNumber,
        @RequestParam(name = "pageSize", required = false) Long pageSize) {

        Long userId = Context.loginUser().getId();
        PageOrList<AssetsFavouriteVO> result = super.service.getFavouriteList(userId, assetsType, pageNumber, pageSize);

        return new Result<>(result);
    }
}

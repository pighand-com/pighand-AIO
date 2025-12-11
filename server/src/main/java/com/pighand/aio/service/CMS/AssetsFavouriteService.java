package com.pighand.aio.service.CMS;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.pighand.aio.domain.CMS.AssetsFavouriteDomain;
import com.pighand.aio.mapper.CMS.AssetsFavouriteMapper;
import com.pighand.aio.vo.CMS.AssetsFavouriteVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.pighand.aio.domain.CMS.table.AssetsFavouriteTableDef.ASSETS_FAVOURITE;

/**
 * CMS - 素材 - 收藏
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@Service
public class AssetsFavouriteService extends BaseServiceImpl<AssetsFavouriteMapper, AssetsFavouriteDomain> {

    /**
     * 创建
     *
     * @param cmsAssetsFavouriteVO
     * @return
     */
    public AssetsFavouriteVO create(AssetsFavouriteVO cmsAssetsFavouriteVO) {
        super.mapper.insert(cmsAssetsFavouriteVO);

        return cmsAssetsFavouriteVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public AssetsFavouriteDomain find(Long id) {
        return super.mapper.find(id, null);
    }

    /**
     * 分页或列表
     *
     * @param cmsAssetsFavouriteVO
     * @return PageOrList<AssetsFavouriteVO>
     */
    public PageOrList<AssetsFavouriteVO> query(AssetsFavouriteVO cmsAssetsFavouriteVO) {

        QueryWrapper queryWrapper = QueryWrapper.create()

            // equal
            .and(ASSETS_FAVOURITE.ASSETS_TYPE.eq(cmsAssetsFavouriteVO.getAssetsType()))
            .and(ASSETS_FAVOURITE.ASSETS_ID.eq(cmsAssetsFavouriteVO.getAssetsId()))
            .and(ASSETS_FAVOURITE.CREATED_BY.eq(cmsAssetsFavouriteVO.getCreatedBy()));

        return super.mapper.query(cmsAssetsFavouriteVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param cmsAssetsFavouriteVO
     */
    public void update(AssetsFavouriteVO cmsAssetsFavouriteVO) {
        UpdateChain updateChain = this.updateChain().where(ASSETS_FAVOURITE.ID.eq(cmsAssetsFavouriteVO.getId()));

        updateChain.set(ASSETS_FAVOURITE.ID, cmsAssetsFavouriteVO.getId(), VerifyUtils::isNotEmpty);
        updateChain.set(ASSETS_FAVOURITE.CREATED_AT, cmsAssetsFavouriteVO.getCreatedAt(), VerifyUtils::isNotEmpty);

        updateChain.update();
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(Long id) {
        super.mapper.deleteById(id);
    }

    /**
     * 查询当前用户是否已收藏指定素材
     *
     * @param assetsId   素材ID
     * @param assetsType 素材类型 10图片 20视频 30文档
     * @param userId     用户ID
     * @return 是否已收藏
     */
    public boolean isFavourited(Long assetsId, Integer assetsType, Long userId) {
        QueryWrapper queryWrapper = QueryWrapper.create().where(ASSETS_FAVOURITE.ASSETS_ID.eq(assetsId))
            .and(ASSETS_FAVOURITE.ASSETS_TYPE.eq(assetsType)).and(ASSETS_FAVOURITE.CREATED_BY.eq(userId));

        return super.mapper.selectCountByQuery(queryWrapper) > 0;
    }

    /**
     * 收藏素材
     *
     * @param assetsId   素材ID
     * @param assetsType 素材类型 10图片 20视频 30文档
     * @param userId     用户ID
     * @return 收藏记录
     */
    public AssetsFavouriteVO addFavourite(Long assetsId, Integer assetsType, Long userId) {
        // 检查是否已收藏
        if (isFavourited(assetsId, assetsType, userId)) {
            throw new RuntimeException("该素材已收藏");
        }

        AssetsFavouriteVO assetsFavouriteVO = new AssetsFavouriteVO();
        assetsFavouriteVO.setAssetsId(assetsId);
        assetsFavouriteVO.setAssetsType(assetsType);
        assetsFavouriteVO.setCreatedBy(userId);
        assetsFavouriteVO.setCreatedAt(new Date());

        return create(assetsFavouriteVO);
    }

    /**
     * 取消收藏
     *
     * @param assetsId   素材ID
     * @param assetsType 素材类型 10图片 20视频 30文档
     * @param userId     用户ID
     */
    public void removeFavourite(Long assetsId, Integer assetsType, Long userId) {
        QueryWrapper queryWrapper = QueryWrapper.create().where(ASSETS_FAVOURITE.ASSETS_ID.eq(assetsId))
            .and(ASSETS_FAVOURITE.ASSETS_TYPE.eq(assetsType)).and(ASSETS_FAVOURITE.CREATED_BY.eq(userId));

        super.mapper.deleteByQuery(queryWrapper);
    }

    /**
     * 获取用户收藏列表
     *
     * @param userId     用户ID
     * @param assetsType 素材类型，可选
     * @param pageNumber 页码
     * @param pageSize   每页大小
     * @return 收藏列表，包含素材详细信息
     */
    public PageOrList<AssetsFavouriteVO> getFavouriteList(Long userId, Integer assetsType, Long pageNumber,
        Long pageSize) {
        AssetsFavouriteDomain queryDomain = new AssetsFavouriteDomain();
        queryDomain.setCreatedBy(userId);
        queryDomain.setAssetsType(assetsType); // 直接设置assetsType到domain对象中

        if (pageNumber != null && pageSize != null) {
            queryDomain.setPageNumber(pageNumber);
            queryDomain.setPageSize(pageSize);
        }

        // 使用新的关联查询方法，不再通过QueryWrapper传递assetsType
        return super.mapper.queryWithAssetDetails(queryDomain, null);
    }
}

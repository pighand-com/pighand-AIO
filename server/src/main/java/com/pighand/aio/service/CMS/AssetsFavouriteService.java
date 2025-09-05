package com.pighand.aio.service.CMS;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.CMS.AssetsFavouriteDomain;
import com.pighand.aio.mapper.CMS.AssetsFavouriteMapper;
import com.pighand.aio.service.CmsAssetsFavouriteService;
import com.pighand.aio.vo.CMS.AssetsFavouriteVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import org.springframework.stereotype.Service;

/**
 * CMS - 素材 - 收藏
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@Service
public class AssetsFavouriteService extends BaseServiceImpl<AssetsFavouriteMapper, AssetsFavouriteDomain>
     implements CmsAssetsFavouriteService{

    /**
     * 创建
     *
     * @param cmsAssetsFavouriteVO
     * @return
     */
    @Override
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
    @Override
    public AssetsFavouriteDomain find(Long id) {
        return super.mapper.find(id, null);
    }

    /**
     * 分页或列表
     *
     * @param cmsAssetsFavouriteVO
     * @return PageOrList<CmsAssetsFavouriteVO>
     */
    @Override
    public PageOrList<AssetsFavouriteVO> query(AssetsFavouriteVO cmsAssetsFavouriteVO) {

        QueryWrapper queryWrapper = QueryWrapper.create()


        // equal
        .and(CMS_ASSETS_FAVOURITE.ASSETS_TYPE.eq(cmsAssetsFavouriteVO.getAssetsType()))
        .and(CMS_ASSETS_FAVOURITE.ASSETS_ID.eq(cmsAssetsFavouriteVO.getAssetsId()))
        .and(CMS_ASSETS_FAVOURITE.CREATED_BY.eq(cmsAssetsFavouriteVO.getCreatedBy()))
        ;

        return super.mapper.query(cmsAssetsFavouriteVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param cmsAssetsFavouriteVO
     */
    @Override
    public void update(AssetsFavouriteVO cmsAssetsFavouriteVO) {
        UpdateChain updateChain = this.updateChain().where(CMS_ASSETS_FAVOURITE.ID.eq(cmsAssetsFavouriteVO.getId()));

            updateChain.set(CMS_ASSETS_FAVOURITE.ID, cmsAssetsFavouriteVO.getId(), , VerifyUtils::isNotEmpty);
            updateChain.set(CMS_ASSETS_FAVOURITE.CREATED_AT, cmsAssetsFavouriteVO.getCreatedAt(), , VerifyUtils::isNotEmpty);

        if (updateTmpColumns.size() > 0) {
            updateChain.update();
        }
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        super.mapper.deleteById(id);
    }
}

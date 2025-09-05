package com.pighand.aio.service.CMS;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.CMS.AssetsVideoDomain;
import com.pighand.aio.mapper.CMS.AssetsVideoMapper;
import com.pighand.aio.service.CmsAssetsVideoService;
import com.pighand.aio.vo.CMS.AssetsVideoVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import org.springframework.stereotype.Service;

/**
 * CMS - 素材 - 视频
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@Service
public class AssetsVideoService extends BaseServiceImpl<AssetsVideoMapper, AssetsVideoDomain>
     implements CmsAssetsVideoService{

    /**
     * 创建
     *
     * @param cmsAssetsVideoVO
     * @return
     */
    @Override
    public AssetsVideoVO create(AssetsVideoVO cmsAssetsVideoVO) {
        super.mapper.insert(cmsAssetsVideoVO);

        return cmsAssetsVideoVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public AssetsVideoDomain find(Long id) {
        return super.mapper.find(id, null);
    }

    /**
     * 分页或列表
     *
     * @param cmsAssetsVideoVO
     * @return PageOrList<CmsAssetsVideoVO>
     */
    @Override
    public PageOrList<AssetsVideoVO> query(AssetsVideoVO cmsAssetsVideoVO) {

        QueryWrapper queryWrapper = QueryWrapper.create()

        // like
        .and(CMS_ASSETS_VIDEO.TITLE.like(cmsAssetsVideoVO.getTitle()))
        .and(CMS_ASSETS_VIDEO.DESCRIPTION.like(cmsAssetsVideoVO.getDescription()))
        .and(CMS_ASSETS_VIDEO.COVER_URL.like(cmsAssetsVideoVO.getCoverUrl()))
        .and(CMS_ASSETS_VIDEO.URL.like(cmsAssetsVideoVO.getUrl()))
        .and(CMS_ASSETS_VIDEO.FILE_FORMAT.like(cmsAssetsVideoVO.getFileFormat()))

        // equal
        .and(CMS_ASSETS_VIDEO.APPLICATION_ID.eq(cmsAssetsVideoVO.getApplicationId()))
        .and(CMS_ASSETS_VIDEO.CLASSIFICATION_ID.eq(cmsAssetsVideoVO.getClassificationId()))
        .and(CMS_ASSETS_VIDEO.FILE_SIZE.eq(cmsAssetsVideoVO.getFileSize()))
        .and(CMS_ASSETS_VIDEO.VIEW_COUNT.eq(cmsAssetsVideoVO.getViewCount()))
        .and(CMS_ASSETS_VIDEO.DOWNLOAD_COUNT.eq(cmsAssetsVideoVO.getDownloadCount()))
        .and(CMS_ASSETS_VIDEO.HANDPICK.eq(cmsAssetsVideoVO.getHandpick()))
        .and(CMS_ASSETS_VIDEO.CREATED_BY.eq(cmsAssetsVideoVO.getCreatedBy()))
        ;

        return super.mapper.query(cmsAssetsVideoVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param cmsAssetsVideoVO
     */
    @Override
    public void update(AssetsVideoVO cmsAssetsVideoVO) {
        UpdateChain updateChain = this.updateChain().where(CMS_ASSETS_VIDEO.ID.eq(cmsAssetsVideoVO.getId()));

            updateChain.set(CMS_ASSETS_VIDEO.ID, cmsAssetsVideoVO.getId(), , VerifyUtils::isNotEmpty);
            updateChain.set(CMS_ASSETS_VIDEO.CREATED_AT, cmsAssetsVideoVO.getCreatedAt(), , VerifyUtils::isNotEmpty);
            updateChain.set(CMS_ASSETS_VIDEO.UPDATED_AT, cmsAssetsVideoVO.getUpdatedAt(), , VerifyUtils::isNotEmpty);
            updateChain.set(CMS_ASSETS_VIDEO.DELETED, cmsAssetsVideoVO.getDeleted(), , VerifyUtils::isNotEmpty);

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

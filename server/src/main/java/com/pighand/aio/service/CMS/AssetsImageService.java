package com.pighand.aio.service.CMS;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.CMS.AssetsImageDomain;
import com.pighand.aio.mapper.CMS.AssetsImageMapper;
import com.pighand.aio.service.CmsAssetsImageService;
import com.pighand.aio.vo.CMS.AssetsImageVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import org.springframework.stereotype.Service;

/**
 * CMS - 素材 - 图片
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@Service
public class AssetsImageService extends BaseServiceImpl<AssetsImageMapper, AssetsImageDomain>
     implements CmsAssetsImageService{

    /**
     * 创建
     *
     * @param cmsAssetsImageVO
     * @return
     */
    @Override
    public AssetsImageVO create(AssetsImageVO cmsAssetsImageVO) {
        super.mapper.insert(cmsAssetsImageVO);

        return cmsAssetsImageVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public AssetsImageDomain find(Long id) {
        return super.mapper.find(id, null);
    }

    /**
     * 分页或列表
     *
     * @param cmsAssetsImageVO
     * @return PageOrList<CmsAssetsImageVO>
     */
    @Override
    public PageOrList<AssetsImageVO> query(AssetsImageVO cmsAssetsImageVO) {

        QueryWrapper queryWrapper = QueryWrapper.create()

        // like
        .and(CMS_ASSETS_IMAGE.TITLE.like(cmsAssetsImageVO.getTitle()))
        .and(CMS_ASSETS_IMAGE.DESCRIPTION.like(cmsAssetsImageVO.getDescription()))
        .and(CMS_ASSETS_IMAGE.URL.like(cmsAssetsImageVO.getUrl()))
        .and(CMS_ASSETS_IMAGE.FILE_FORMAT.like(cmsAssetsImageVO.getFileFormat()))

        // equal
        .and(CMS_ASSETS_IMAGE.APPLICATION_ID.eq(cmsAssetsImageVO.getApplicationId()))
        .and(CMS_ASSETS_IMAGE.CLASSIFICATION_ID.eq(cmsAssetsImageVO.getClassificationId()))
        .and(CMS_ASSETS_IMAGE.FILE_SIZE.eq(cmsAssetsImageVO.getFileSize()))
        .and(CMS_ASSETS_IMAGE.VIEW_COUNT.eq(cmsAssetsImageVO.getViewCount()))
        .and(CMS_ASSETS_IMAGE.DOWNLOAD_COUNT.eq(cmsAssetsImageVO.getDownloadCount()))
        .and(CMS_ASSETS_IMAGE.HANDPICK.eq(cmsAssetsImageVO.getHandpick()))
        .and(CMS_ASSETS_IMAGE.CREATED_BY.eq(cmsAssetsImageVO.getCreatedBy()))
        ;

        return super.mapper.query(cmsAssetsImageVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param cmsAssetsImageVO
     */
    @Override
    public void update(AssetsImageVO cmsAssetsImageVO) {
        UpdateChain updateChain = this.updateChain().where(CMS_ASSETS_IMAGE.ID.eq(cmsAssetsImageVO.getId()));

            updateChain.set(CMS_ASSETS_IMAGE.ID, cmsAssetsImageVO.getId(), , VerifyUtils::isNotEmpty);
            updateChain.set(CMS_ASSETS_IMAGE.CREATED_AT, cmsAssetsImageVO.getCreatedAt(), , VerifyUtils::isNotEmpty);
            updateChain.set(CMS_ASSETS_IMAGE.UPDATED_AT, cmsAssetsImageVO.getUpdatedAt(), , VerifyUtils::isNotEmpty);
            updateChain.set(CMS_ASSETS_IMAGE.DELETED, cmsAssetsImageVO.getDeleted(), , VerifyUtils::isNotEmpty);

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

package com.pighand.aio.service.CMS;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.CMS.AssetsDocDomain;
import com.pighand.aio.mapper.CMS.AssetsDocMapper;
import com.pighand.aio.service.CmsAssetsDocService;
import com.pighand.aio.vo.CMS.AssetsDocVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import org.springframework.stereotype.Service;

/**
 * CMS - 素材 - 文档
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@Service
public class AssetsDocService extends BaseServiceImpl<AssetsDocMapper, AssetsDocDomain>
     implements CmsAssetsDocService{

    /**
     * 创建
     *
     * @param cmsAssetsDocVO
     * @return
     */
    @Override
    public AssetsDocVO create(AssetsDocVO cmsAssetsDocVO) {
        super.mapper.insert(cmsAssetsDocVO);

        return cmsAssetsDocVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public AssetsDocDomain find(Long id) {
        return super.mapper.find(id, null);
    }

    /**
     * 分页或列表
     *
     * @param cmsAssetsDocVO
     * @return PageOrList<CmsAssetsDocVO>
     */
    @Override
    public PageOrList<AssetsDocVO> query(AssetsDocVO cmsAssetsDocVO) {

        QueryWrapper queryWrapper = QueryWrapper.create()

        // like
        .and(CMS_ASSETS_DOC.TITLE.like(cmsAssetsDocVO.getTitle()))
        .and(CMS_ASSETS_DOC.DESCRIPTION.like(cmsAssetsDocVO.getDescription()))
        .and(CMS_ASSETS_DOC.COVER_URL.like(cmsAssetsDocVO.getCoverUrl()))
        .and(CMS_ASSETS_DOC.URL.like(cmsAssetsDocVO.getUrl()))
        .and(CMS_ASSETS_DOC.FILE_FORMAT.like(cmsAssetsDocVO.getFileFormat()))

        // equal
        .and(CMS_ASSETS_DOC.APPLICATION_ID.eq(cmsAssetsDocVO.getApplicationId()))
        .and(CMS_ASSETS_DOC.CLASSIFICATION_ID.eq(cmsAssetsDocVO.getClassificationId()))
        .and(CMS_ASSETS_DOC.FILE_SIZE.eq(cmsAssetsDocVO.getFileSize()))
        .and(CMS_ASSETS_DOC.VIEW_COUNT.eq(cmsAssetsDocVO.getViewCount()))
        .and(CMS_ASSETS_DOC.DOWNLOAD_COUNT.eq(cmsAssetsDocVO.getDownloadCount()))
        .and(CMS_ASSETS_DOC.HANDPICK.eq(cmsAssetsDocVO.getHandpick()))
        .and(CMS_ASSETS_DOC.CREATED_BY.eq(cmsAssetsDocVO.getCreatedBy()))
        ;

        return super.mapper.query(cmsAssetsDocVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param cmsAssetsDocVO
     */
    @Override
    public void update(AssetsDocVO cmsAssetsDocVO) {
        UpdateChain updateChain = this.updateChain().where(CMS_ASSETS_DOC.ID.eq(cmsAssetsDocVO.getId()));

            updateChain.set(CMS_ASSETS_DOC.ID, cmsAssetsDocVO.getId(), , VerifyUtils::isNotEmpty);
            updateChain.set(CMS_ASSETS_DOC.CREATED_AT, cmsAssetsDocVO.getCreatedAt(), , VerifyUtils::isNotEmpty);
            updateChain.set(CMS_ASSETS_DOC.UPDATED_AT, cmsAssetsDocVO.getUpdatedAt(), , VerifyUtils::isNotEmpty);
            updateChain.set(CMS_ASSETS_DOC.DELETED, cmsAssetsDocVO.getDeleted(), , VerifyUtils::isNotEmpty);

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

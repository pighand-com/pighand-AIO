package com.pighand.aio.service.CMS;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.CMS.AssetsCollectionRelevanceDomain;
import com.pighand.aio.mapper.CMS.AssetsCollectionRelevanceMapper;
import com.pighand.aio.service.CmsAssetsCollectionRelevanceService;
import com.pighand.aio.vo.CMS.AssetsCollectionRelevanceVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import org.springframework.stereotype.Service;

/**
 * CMS - 素材 - 专辑关系表
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@Service
public class AssetsCollectionRelevanceService extends BaseServiceImpl<AssetsCollectionRelevanceMapper, AssetsCollectionRelevanceDomain>
     implements CmsAssetsCollectionRelevanceService{

    /**
     * 创建
     *
     * @param cmsAssetsCollectionRelevanceVO
     * @return
     */
    @Override
    public AssetsCollectionRelevanceVO create(AssetsCollectionRelevanceVO cmsAssetsCollectionRelevanceVO) {
        super.mapper.insert(cmsAssetsCollectionRelevanceVO);

        return cmsAssetsCollectionRelevanceVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public AssetsCollectionRelevanceDomain find(Long id) {
        return super.mapper.find(id, null);
    }

    /**
     * 分页或列表
     *
     * @param cmsAssetsCollectionRelevanceVO
     * @return PageOrList<CmsAssetsCollectionRelevanceVO>
     */
    @Override
    public PageOrList<AssetsCollectionRelevanceVO> query(AssetsCollectionRelevanceVO cmsAssetsCollectionRelevanceVO) {

        QueryWrapper queryWrapper = QueryWrapper.create()


        // equal
        .and(CMS_ASSETS_COLLECTION_RELEVANCE.COLLECTION_ID.eq(cmsAssetsCollectionRelevanceVO.getCollectionId()))
        .and(CMS_ASSETS_COLLECTION_RELEVANCE.ASSETS_TYPE.eq(cmsAssetsCollectionRelevanceVO.getAssetsType()))
        .and(CMS_ASSETS_COLLECTION_RELEVANCE.ASSETS_ID.eq(cmsAssetsCollectionRelevanceVO.getAssetsId()))
        .and(CMS_ASSETS_COLLECTION_RELEVANCE.CREATED_BY.eq(cmsAssetsCollectionRelevanceVO.getCreatedBy()))
        ;

        return super.mapper.query(cmsAssetsCollectionRelevanceVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param cmsAssetsCollectionRelevanceVO
     */
    @Override
    public void update(AssetsCollectionRelevanceVO cmsAssetsCollectionRelevanceVO) {
        UpdateChain updateChain = this.updateChain().where(CMS_ASSETS_COLLECTION_RELEVANCE.ID.eq(cmsAssetsCollectionRelevanceVO.getId()));

            updateChain.set(CMS_ASSETS_COLLECTION_RELEVANCE.ID, cmsAssetsCollectionRelevanceVO.getId(), , VerifyUtils::isNotEmpty);
            updateChain.set(CMS_ASSETS_COLLECTION_RELEVANCE.CREATED_AT, cmsAssetsCollectionRelevanceVO.getCreatedAt(), , VerifyUtils::isNotEmpty);
            updateChain.set(CMS_ASSETS_COLLECTION_RELEVANCE.UPDATED_AT, cmsAssetsCollectionRelevanceVO.getUpdatedAt(), , VerifyUtils::isNotEmpty);
            updateChain.set(CMS_ASSETS_COLLECTION_RELEVANCE.DELETED, cmsAssetsCollectionRelevanceVO.getDeleted(), , VerifyUtils::isNotEmpty);

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

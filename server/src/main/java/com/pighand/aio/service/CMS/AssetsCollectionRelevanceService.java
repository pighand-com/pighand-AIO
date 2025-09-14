package com.pighand.aio.service.CMS;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.pighand.aio.domain.CMS.AssetsCollectionRelevanceDomain;
import com.pighand.aio.mapper.CMS.AssetsCollectionRelevanceMapper;
import com.pighand.aio.vo.CMS.AssetsCollectionRelevanceVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.pighand.aio.domain.CMS.table.AssetsCollectionRelevanceTableDef.ASSETS_COLLECTION_RELEVANCE;
import static com.pighand.aio.domain.CMS.table.AssetsCollectionTableDef.ASSETS_COLLECTION;

/**
 * CMS - 素材 - 专辑关系表
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@Service
public class AssetsCollectionRelevanceService
    extends BaseServiceImpl<AssetsCollectionRelevanceMapper, AssetsCollectionRelevanceDomain> {

    /**
     * 创建
     *
     * @param cmsAssetsCollectionRelevanceVO
     * @return
     */
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
    public AssetsCollectionRelevanceDomain find(Long id) {
        return super.mapper.find(id, null);
    }

    /**
     * 分页或列表
     *
     * @param cmsAssetsCollectionRelevanceVO
     * @return PageOrList<AssetsCollectionRelevanceVO>
     */
    public PageOrList<AssetsCollectionRelevanceVO> query(AssetsCollectionRelevanceVO cmsAssetsCollectionRelevanceVO) {

        QueryWrapper queryWrapper = QueryWrapper.create()

            // equal
            .and(ASSETS_COLLECTION_RELEVANCE.COLLECTION_ID.eq(cmsAssetsCollectionRelevanceVO.getCollectionId()))
            .and(ASSETS_COLLECTION_RELEVANCE.ASSETS_TYPE.eq(cmsAssetsCollectionRelevanceVO.getAssetsType()))
            .and(ASSETS_COLLECTION_RELEVANCE.ASSETS_ID.eq(cmsAssetsCollectionRelevanceVO.getAssetsId()))
            .and(ASSETS_COLLECTION_RELEVANCE.CREATED_BY.eq(cmsAssetsCollectionRelevanceVO.getCreatedBy()));

        return super.mapper.query(cmsAssetsCollectionRelevanceVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param cmsAssetsCollectionRelevanceVO
     */
    public void update(AssetsCollectionRelevanceVO cmsAssetsCollectionRelevanceVO) {
        UpdateChain updateChain =
            this.updateChain().where(ASSETS_COLLECTION_RELEVANCE.ID.eq(cmsAssetsCollectionRelevanceVO.getId()));

        updateChain.set(ASSETS_COLLECTION_RELEVANCE.ID, cmsAssetsCollectionRelevanceVO.getId(),
            VerifyUtils::isNotEmpty);
        updateChain.set(ASSETS_COLLECTION_RELEVANCE.CREATED_AT, cmsAssetsCollectionRelevanceVO.getCreatedAt(),
            VerifyUtils::isNotEmpty);
        updateChain.set(ASSETS_COLLECTION_RELEVANCE.UPDATED_AT, cmsAssetsCollectionRelevanceVO.getUpdatedAt(),
            VerifyUtils::isNotEmpty);
        updateChain.set(ASSETS_COLLECTION_RELEVANCE.DELETED, cmsAssetsCollectionRelevanceVO.getDeleted(),
            VerifyUtils::isNotEmpty);
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
     * 根据素材类型和素材ID列表查询专辑关系，并关联查询专辑名称
     *
     * @param assetsType 素材类型 10图片 20视频 30文档
     * @param assetsIds 素材ID列表
     * @return 查询结果列表，包含专辑名称
     */
    public List<AssetsCollectionRelevanceVO> queryByAssetsTypeAndIds(Integer assetsType, List<Long> assetsIds) {
        QueryWrapper queryWrapper = QueryWrapper.create()
            .select(ASSETS_COLLECTION_RELEVANCE.ALL_COLUMNS, ASSETS_COLLECTION.NAME.as("collectionName"))
            .from(ASSETS_COLLECTION_RELEVANCE)
            .leftJoin(ASSETS_COLLECTION).on(ASSETS_COLLECTION_RELEVANCE.COLLECTION_ID.eq(ASSETS_COLLECTION.ID))
            .where(ASSETS_COLLECTION_RELEVANCE.ASSETS_TYPE.eq(assetsType))
            .and(ASSETS_COLLECTION_RELEVANCE.ASSETS_ID.in(assetsIds));

        return super.mapper.selectListByQueryAs(queryWrapper, AssetsCollectionRelevanceVO.class);
    }

    /**
     * 根据素材类型和素材ID列表查询专辑关系，返回Map格式
     *
     * @param assetsType 素材类型 10图片 20视频 30文档
     * @param assetsIds 素材ID列表
     * @return Map<Long, List<AssetsCollectionRelevanceVO>> key为assetsId，value为专辑关联信息列表
     */
    public Map<Long, List<AssetsCollectionRelevanceVO>> queryByAssetsTypeAndIdsAsMap(Integer assetsType, List<Long> assetsIds) {
        List<AssetsCollectionRelevanceVO> collectionRelevances = queryByAssetsTypeAndIds(assetsType, assetsIds);
        
        // 将结果转成map，key为assetsId，value为专辑关联信息列表
        return collectionRelevances.stream()
            .collect(Collectors.groupingBy(AssetsCollectionRelevanceVO::getAssetsId));
    }

    /**
     * 批量创建专辑关联信息
     *
     * @param assetsId 素材ID
     * @param collectionIds 专辑ID列表
     * @param assetsType 素材类型 10图片 20视频 30文档
     */
    @Transactional(rollbackFor = Exception.class)
    public void createCollectionRelevances(Long assetsId, List<Long> collectionIds, Integer assetsType) {
        if (CollectionUtils.isEmpty(collectionIds)) {
            return;
        }
        
        for (Long collectionId : collectionIds) {
            AssetsCollectionRelevanceVO relevanceVO = new AssetsCollectionRelevanceVO();
            relevanceVO.setCollectionId(collectionId);
            relevanceVO.setAssetsType(assetsType);
            relevanceVO.setAssetsId(assetsId);
            this.create(relevanceVO);
        }
    }

    /**
     * 根据素材ID删除专辑关联信息
     *
     * @param assetsId 素材ID
     * @param assetsType 素材类型 10图片 20视频 30文档
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteCollectionRelevancesByAssetsId(Long assetsId, Integer assetsType) {
        // 查询该素材的所有专辑关联
        AssetsCollectionRelevanceVO queryVO = new AssetsCollectionRelevanceVO();
        queryVO.setAssetsType(assetsType);
        queryVO.setAssetsId(assetsId);
        
        PageOrList<AssetsCollectionRelevanceVO> relevances = this.query(queryVO);
        
        // 删除所有关联记录
        if (relevances != null && !CollectionUtils.isEmpty(relevances.getRecords())) {
            for (AssetsCollectionRelevanceVO relevance : relevances.getRecords()) {
                this.delete(relevance.getId());
            }
        }
    }

    /**
     * 更新素材的专辑关联信息（先删除原有关联，再创建新的关联）
     *
     * @param assetsId 素材ID
     * @param collectionIds 新的专辑ID列表
     * @param assetsType 素材类型 10图片 20视频 30文档
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateCollectionRelevances(Long assetsId, List<Long> collectionIds, Integer assetsType) {
        // 先删除原有专辑关联
        this.deleteCollectionRelevancesByAssetsId(assetsId, assetsType);
        
        // 如果有新的专辑ID列表，则创建新的关联
        if (!CollectionUtils.isEmpty(collectionIds)) {
            this.createCollectionRelevances(assetsId, collectionIds, assetsType);
        }
    }
}

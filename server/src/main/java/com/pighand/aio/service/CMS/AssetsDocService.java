package com.pighand.aio.service.CMS;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.pighand.aio.domain.CMS.AssetsDocDomain;
import com.pighand.aio.mapper.CMS.AssetsDocMapper;
import com.pighand.aio.vo.CMS.AssetsCollectionRelevanceVO;
import com.pighand.aio.vo.CMS.AssetsDocVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.pighand.aio.domain.CMS.table.AssetsDocTableDef.ASSETS_DOC;

/**
 * CMS - 素材 - 文档
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@Service
@RequiredArgsConstructor
public class AssetsDocService extends BaseServiceImpl<AssetsDocMapper, AssetsDocDomain> {

    private final AssetsCollectionRelevanceService assetsCollectionRelevanceService;





    /**
     * 创建
     *
     * @param cmsAssetsDocVO
     * @return
     */
    public AssetsDocVO create(AssetsDocVO cmsAssetsDocVO) {
        // 设置默认值
        if (cmsAssetsDocVO.getHandpick() == null) {
            cmsAssetsDocVO.setHandpick(false);
        }
        if (cmsAssetsDocVO.getStatus() == null) {
            cmsAssetsDocVO.setStatus(20);
        }

        super.mapper.insert(cmsAssetsDocVO);

        // 处理专辑关联
        if (!CollectionUtils.isEmpty(cmsAssetsDocVO.getCollectionIds())) {
            assetsCollectionRelevanceService.createCollectionRelevances(cmsAssetsDocVO.getId(),
                cmsAssetsDocVO.getCollectionIds(), 30);
        }

        return cmsAssetsDocVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public AssetsDocDomain find(Long id) {
        return super.mapper.find(id, null);
    }

    /**
     * 分页或列表
     *
     * @param cmsAssetsDocVO
     * @return PageOrList<AssetsDocVO>
     */
    public PageOrList<AssetsDocVO> query(AssetsDocVO cmsAssetsDocVO) {

        QueryWrapper queryWrapper = QueryWrapper.create().select(ASSETS_DOC.DEFAULT_COLUMNS)

            // like
            .and(ASSETS_DOC.TITLE.like(cmsAssetsDocVO.getTitle()))
            .and(ASSETS_DOC.DESCRIPTION.like(cmsAssetsDocVO.getDescription()))

            // equal
            .and(ASSETS_DOC.FILE_FORMAT.eq(cmsAssetsDocVO.getFileFormat()))
            .and(ASSETS_DOC.APPLICATION_ID.eq(cmsAssetsDocVO.getApplicationId()))
            .and(ASSETS_DOC.CLASSIFICATION_ID.eq(cmsAssetsDocVO.getClassificationId()))
            .and(ASSETS_DOC.HANDPICK.eq(cmsAssetsDocVO.getHandpick()))
            .and(ASSETS_DOC.STATUS.eq(cmsAssetsDocVO.getStatus()))
            .and(ASSETS_DOC.CREATED_BY.eq(cmsAssetsDocVO.getCreatedBy()));

        PageOrList<AssetsDocVO> result = super.mapper.query(cmsAssetsDocVO, queryWrapper);

        // 整理id，调用queryByAssetsTypeAndIds方法，结果转成map，然后回填
        if (result != null && result.getRecords() != null && !result.getRecords().isEmpty()) {
            // 整理所有图片的id
            List<Long> imageIds = result.getRecords().stream().map(AssetsDocVO::getId).collect(Collectors.toList());

            // 调用queryByAssetsTypeAndIdsAsMap方法查询专辑关联信息并返回map格式
            // assetsType = 20 表示图片类型
            Map<Long, List<AssetsCollectionRelevanceVO>> collectionMap =
                assetsCollectionRelevanceService.queryByAssetsTypeAndIdsAsMap(30, imageIds);

            // 回填专辑信息到每个图片VO中
            result.getRecords().forEach(imageVO -> {
                List<AssetsCollectionRelevanceVO> collections = collectionMap.get(imageVO.getId());
                imageVO.setCollections(collections);
            });
        }

        return result;
    }

    /**
     * 修改
     *
     * @param cmsAssetsDocVO
     */
    public void update(AssetsDocVO cmsAssetsDocVO) {
        UpdateChain updateChain = this.updateChain().where(ASSETS_DOC.ID.eq(cmsAssetsDocVO.getId()));

        updateChain.set(ASSETS_DOC.TITLE, cmsAssetsDocVO.getTitle(), VerifyUtils::isNotEmpty);
        updateChain.set(ASSETS_DOC.CLASSIFICATION_ID, cmsAssetsDocVO.getClassificationId(), VerifyUtils::isNotEmpty);
        updateChain.set(ASSETS_DOC.COVER_URL, cmsAssetsDocVO.getCoverUrl(), VerifyUtils::isNotEmpty);
        updateChain.set(ASSETS_DOC.URL, cmsAssetsDocVO.getUrl(), VerifyUtils::isNotEmpty);
        updateChain.set(ASSETS_DOC.DESCRIPTION, cmsAssetsDocVO.getDescription(), VerifyUtils::isNotEmpty);
        updateChain.set(ASSETS_DOC.FILE_FORMAT, cmsAssetsDocVO.getFileFormat(), VerifyUtils::isNotEmpty);
        updateChain.set(ASSETS_DOC.FILE_SIZE, cmsAssetsDocVO.getFileSize(), VerifyUtils::isNotEmpty);

        updateChain.update();

        // 如果传了collectionIds，更新专辑关联信息
        if (cmsAssetsDocVO.getCollectionIds() != null) {
            assetsCollectionRelevanceService.updateCollectionRelevances(cmsAssetsDocVO.getId(),
                cmsAssetsDocVO.getCollectionIds(), 30);
        }
    }

    /**
     * 上架
     *
     * @param id
     */
    public void onShelf(Long id) {
        UpdateChain updateChain = this.updateChain().where(ASSETS_DOC.ID.eq(id));
        updateChain.set(ASSETS_DOC.STATUS, 10);
        updateChain.update();
    }

    /**
     * 下架
     *
     * @param id
     */
    public void offShelf(Long id) {
        UpdateChain updateChain = this.updateChain().where(ASSETS_DOC.ID.eq(id));
        updateChain.set(ASSETS_DOC.STATUS, 20);
        updateChain.update();
    }

    /**
     * 设置精选
     *
     * @param id
     */
    public void setHandpick(Long id) {
        UpdateChain updateChain = this.updateChain().where(ASSETS_DOC.ID.eq(id));
        updateChain.set(ASSETS_DOC.HANDPICK, true);
        updateChain.update();
    }

    /**
     * 取消精选
     *
     * @param id
     */
    public void cancelHandpick(Long id) {
        UpdateChain updateChain = this.updateChain().where(ASSETS_DOC.ID.eq(id));
        updateChain.set(ASSETS_DOC.HANDPICK, false);
        updateChain.update();
    }

    /**
     * 删除
     *
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        // 删除相关专辑关联信息
        assetsCollectionRelevanceService.deleteCollectionRelevancesByAssetsId(id, 30);

        super.mapper.deleteById(id);
    }
}

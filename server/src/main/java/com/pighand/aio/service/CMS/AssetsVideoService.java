package com.pighand.aio.service.CMS;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.pighand.aio.domain.CMS.AssetsVideoDomain;
import com.pighand.aio.mapper.CMS.AssetsVideoMapper;
import com.pighand.aio.vo.CMS.AssetsCollectionRelevanceVO;
import com.pighand.aio.vo.CMS.AssetsVideoVO;
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

import static com.pighand.aio.domain.CMS.table.AssetsVideoTableDef.ASSETS_VIDEO;

/**
 * CMS - 素材 - 视频
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@RequiredArgsConstructor
@Service
public class AssetsVideoService extends BaseServiceImpl<AssetsVideoMapper, AssetsVideoDomain> {

    private final AssetsCollectionRelevanceService assetsCollectionRelevanceService;

    /**
     * 创建
     *
     * @param cmsAssetsVideoVO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public AssetsVideoVO create(AssetsVideoVO cmsAssetsVideoVO) {
        if (cmsAssetsVideoVO.getHandpick() == null) {
            cmsAssetsVideoVO.setHandpick(false);
        }

        if (cmsAssetsVideoVO.getStatus() == null) {
            cmsAssetsVideoVO.setStatus(20);
        }

        super.mapper.insert(cmsAssetsVideoVO);

        // 如果传了collectionIds，创建专辑关联信息
        if (!CollectionUtils.isEmpty(cmsAssetsVideoVO.getCollectionIds())) {
            assetsCollectionRelevanceService.createCollectionRelevances(cmsAssetsVideoVO.getId(),
                cmsAssetsVideoVO.getCollectionIds(), 20);
        }

        return cmsAssetsVideoVO;

    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public AssetsVideoDomain find(Long id) {
        return super.mapper.find(id, null);
    }

    /**
     * 分页或列表
     *
     * @param cmsAssetsVideoVO
     * @return PageOrList<AssetsVideoVO>
     */
    public PageOrList<AssetsVideoVO> query(AssetsVideoVO cmsAssetsVideoVO) {

        QueryWrapper queryWrapper = QueryWrapper.create().select(ASSETS_VIDEO.DEFAULT_COLUMNS)

            // like
            .and(ASSETS_VIDEO.TITLE.like(cmsAssetsVideoVO.getTitle()))
            .and(ASSETS_VIDEO.DESCRIPTION.like(cmsAssetsVideoVO.getDescription()))

            // equal
            .and(ASSETS_VIDEO.FILE_FORMAT.eq(cmsAssetsVideoVO.getFileFormat()))
            .and(ASSETS_VIDEO.APPLICATION_ID.eq(cmsAssetsVideoVO.getApplicationId()))
            .and(ASSETS_VIDEO.CLASSIFICATION_ID.eq(cmsAssetsVideoVO.getClassificationId()))
            .and(ASSETS_VIDEO.VIEW_COUNT.eq(cmsAssetsVideoVO.getViewCount()))
            .and(ASSETS_VIDEO.HANDPICK.eq(cmsAssetsVideoVO.getHandpick()))
            .and(ASSETS_VIDEO.STATUS.eq(cmsAssetsVideoVO.getStatus()))
            .and(ASSETS_VIDEO.CREATED_BY.eq(cmsAssetsVideoVO.getCreatedBy()));

        PageOrList<AssetsVideoVO> result = super.mapper.query(cmsAssetsVideoVO, queryWrapper);

        // 整理id，调用queryByAssetsTypeAndIds方法，结果转成map，然后回填
        if (result != null && result.getRecords() != null && !result.getRecords().isEmpty()) {
            // 整理所有图片的id
            List<Long> imageIds = result.getRecords().stream().map(AssetsVideoVO::getId).collect(Collectors.toList());

            // 调用queryByAssetsTypeAndIdsAsMap方法查询专辑关联信息并返回map格式
            // assetsType = 20 表示图片类型
            Map<Long, List<AssetsCollectionRelevanceVO>> collectionMap =
                assetsCollectionRelevanceService.queryByAssetsTypeAndIdsAsMap(20, imageIds);

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
     * @param cmsAssetsVideoVO
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(AssetsVideoVO cmsAssetsVideoVO) {
        UpdateChain updateChain = this.updateChain().where(ASSETS_VIDEO.ID.eq(cmsAssetsVideoVO.getId()));

        updateChain.set(ASSETS_VIDEO.TITLE, cmsAssetsVideoVO.getTitle(), VerifyUtils::isNotEmpty);
        updateChain.set(ASSETS_VIDEO.DESCRIPTION, cmsAssetsVideoVO.getDescription(), VerifyUtils::isNotEmpty);
        updateChain.set(ASSETS_VIDEO.COVER_URL, cmsAssetsVideoVO.getCoverUrl(), VerifyUtils::isNotEmpty);
        updateChain.set(ASSETS_VIDEO.URL, cmsAssetsVideoVO.getUrl(), VerifyUtils::isNotEmpty);
        updateChain.set(ASSETS_VIDEO.FILE_FORMAT, cmsAssetsVideoVO.getFileFormat(), VerifyUtils::isNotEmpty);
        updateChain.set(ASSETS_VIDEO.FILE_SIZE, cmsAssetsVideoVO.getFileSize(), VerifyUtils::isNotEmpty);
        updateChain.set(ASSETS_VIDEO.RESOLUTION_RATIO, cmsAssetsVideoVO.getResolutionRatio(), VerifyUtils::isNotEmpty);
        updateChain.set(ASSETS_VIDEO.CLASSIFICATION_ID, cmsAssetsVideoVO.getClassificationId());

        updateChain.update();

        // 如果传了collectionIds，更新专辑关联信息
        if (cmsAssetsVideoVO.getCollectionIds() != null) {
            assetsCollectionRelevanceService.updateCollectionRelevances(cmsAssetsVideoVO.getId(),
                cmsAssetsVideoVO.getCollectionIds(), 20);
        }
    }

    /**
     * 上架
     *
     * @param id
     */
    public void onShelf(Long id) {
        UpdateChain updateChain = this.updateChain().where(ASSETS_VIDEO.ID.eq(id));
        updateChain.set(ASSETS_VIDEO.STATUS, 10);
        updateChain.update();
    }

    /**
     * 下架
     *
     * @param id
     */
    public void offShelf(Long id) {
        UpdateChain updateChain = this.updateChain().where(ASSETS_VIDEO.ID.eq(id));
        updateChain.set(ASSETS_VIDEO.STATUS, 20);
        updateChain.update();
    }

    /**
     * 设置精选
     *
     * @param id
     */
    public void setHandpick(Long id) {
        UpdateChain updateChain = this.updateChain().where(ASSETS_VIDEO.ID.eq(id));
        updateChain.set(ASSETS_VIDEO.HANDPICK, true);
        updateChain.update();
    }

    /**
     * 取消精选
     *
     * @param id
     */
    public void cancelHandpick(Long id) {
        UpdateChain updateChain = this.updateChain().where(ASSETS_VIDEO.ID.eq(id));
        updateChain.set(ASSETS_VIDEO.HANDPICK, false);
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
        assetsCollectionRelevanceService.deleteCollectionRelevancesByAssetsId(id, 20);

        super.mapper.deleteById(id);
    }
}

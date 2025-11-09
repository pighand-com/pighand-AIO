package com.pighand.aio.service.CMS;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.pighand.aio.domain.CMS.AssetsImageDomain;
import com.pighand.aio.domain.base.UserExtensionDomain;
import com.pighand.aio.mapper.CMS.AssetsImageMapper;
import com.pighand.aio.service.base.OrgDepartmentService;
import com.pighand.aio.service.base.UserExtensionService;
import com.pighand.aio.vo.CMS.AssetsCollectionRelevanceVO;
import com.pighand.aio.vo.CMS.AssetsImageVO;
import com.pighand.aio.vo.base.OrgDepartmentSimpleVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.pighand.aio.domain.CMS.table.AssetsCollectionTableDef.ASSETS_COLLECTION;
import static com.pighand.aio.domain.CMS.table.AssetsImageTableDef.ASSETS_IMAGE;

/**
 * CMS - 素材 - 图片
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@Service
@RequiredArgsConstructor
public class AssetsImageService extends BaseServiceImpl<AssetsImageMapper, AssetsImageDomain> {

    private final AssetsCollectionRelevanceService assetsCollectionRelevanceService;

    private final UserExtensionService userExtensionService;

    private final OrgDepartmentService orgDepartmentService;

    /**
     * 创建
     *
     * @param cmsAssetsImageVO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public AssetsImageVO create(AssetsImageVO cmsAssetsImageVO) {
        if (cmsAssetsImageVO.getHandpick() == null) {
            cmsAssetsImageVO.setHandpick(false);
        }

        if (cmsAssetsImageVO.getStatus() == null) {
            cmsAssetsImageVO.setStatus(10);
        }

        cmsAssetsImageVO.setViewCount(0);
        cmsAssetsImageVO.setDownloadCount(0);
        super.mapper.insert(cmsAssetsImageVO);

        // 如果传了collectionIds，创建专辑关联信息
        if (!CollectionUtils.isEmpty(cmsAssetsImageVO.getCollectionIds())) {
            assetsCollectionRelevanceService.createCollectionRelevances(cmsAssetsImageVO.getId(),
                cmsAssetsImageVO.getCollectionIds(), 10);
        }

        return cmsAssetsImageVO;
    }

    /**
     * 详情
     * 查看详情时自动增加浏览次数
     *
     * @param id
     * @return
     */
    public AssetsImageVO find(Long id) {
        // 先增加浏览次数
        UpdateChain updateChain = this.updateChain().where(ASSETS_IMAGE.ID.eq(id));
        updateChain.set(ASSETS_IMAGE.VIEW_COUNT, ASSETS_IMAGE.VIEW_COUNT.add(1));
        updateChain.update();

        // 然后返回详情
        AssetsImageDomain domain = super.mapper.find(id);

        // 构建VO对象
        AssetsImageVO vo = new AssetsImageVO();
        BeanUtils.copyProperties(domain, vo);

        // 获取创建人信息
        if (domain.getCreatedBy() != null) {
            UserExtensionDomain creator = userExtensionService.find(domain.getCreatedBy());
            vo.setCreator(creator);

            // 获取创建人的组织架构信息（简化版）
            OrgDepartmentSimpleVO creatorDepartment =
                orgDepartmentService.getUserDepartmentSimple(domain.getCreatedBy());
            vo.setCreatorDepartment(creatorDepartment);
        }

        return vo;
    }

    /**
     * 分页或列表
     *
     * @param cmsAssetsImageVO
     * @return PageOrList<AssetsImageVO>
     */
    public PageOrList<AssetsImageVO> query(AssetsImageVO cmsAssetsImageVO) {

        QueryWrapper queryWrapper = QueryWrapper.create().select(ASSETS_IMAGE.DEFAULT_COLUMNS)

            // like
            .and(ASSETS_IMAGE.TITLE.like(cmsAssetsImageVO.getTitle()))
            .and(ASSETS_IMAGE.DESCRIPTION.like(cmsAssetsImageVO.getDescription()))
            .and(ASSETS_IMAGE.URL.like(cmsAssetsImageVO.getUrl()))
            .and(ASSETS_IMAGE.FILE_FORMAT.like(cmsAssetsImageVO.getFileFormat()))

            // equal
            .and(ASSETS_IMAGE.APPLICATION_ID.eq(cmsAssetsImageVO.getApplicationId()))
            .and(ASSETS_IMAGE.CLASSIFICATION_ID.eq(cmsAssetsImageVO.getClassificationId()))
            .and(ASSETS_IMAGE.FILE_SIZE.eq(cmsAssetsImageVO.getFileSize()))
            .and(ASSETS_IMAGE.VIEW_COUNT.eq(cmsAssetsImageVO.getViewCount()))
            .and(ASSETS_IMAGE.DOWNLOAD_COUNT.eq(cmsAssetsImageVO.getDownloadCount()))
            .and(ASSETS_IMAGE.HANDPICK.eq(cmsAssetsImageVO.getHandpick()))
            .and(ASSETS_IMAGE.STATUS.eq(cmsAssetsImageVO.getStatus()))
            .and(ASSETS_IMAGE.CREATED_BY.eq(cmsAssetsImageVO.getCreatedBy()));

        PageOrList<AssetsImageVO> result = super.mapper.query(cmsAssetsImageVO, queryWrapper);

        // 整理id，调用queryByAssetsTypeAndIds方法，结果转成map，然后回填
        if (result != null && result.getRecords() != null && !result.getRecords().isEmpty()) {
            // 整理所有图片的id
            List<Long> imageIds = result.getRecords().stream().map(AssetsImageVO::getId).collect(Collectors.toList());

            // 调用queryByAssetsTypeAndIdsAsMap方法查询专辑关联信息并返回map格式
            // assetsType = 10 表示图片类型
            Map<Long, List<AssetsCollectionRelevanceVO>> collectionMap =
                assetsCollectionRelevanceService.queryByAssetsTypeAndIdsAsMap(10, imageIds);

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
     * @param cmsAssetsImageVO
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(AssetsImageVO cmsAssetsImageVO) {
        UpdateChain updateChain = this.updateChain().where(ASSETS_IMAGE.ID.eq(cmsAssetsImageVO.getId()));

        updateChain.set(ASSETS_IMAGE.TITLE, cmsAssetsImageVO.getTitle(), VerifyUtils::isNotEmpty);
        updateChain.set(ASSETS_IMAGE.DESCRIPTION, cmsAssetsImageVO.getDescription(), VerifyUtils::isNotEmpty);
        updateChain.set(ASSETS_IMAGE.URL, cmsAssetsImageVO.getUrl(), VerifyUtils::isNotEmpty);
        updateChain.set(ASSETS_IMAGE.FILE_FORMAT, cmsAssetsImageVO.getFileFormat(), VerifyUtils::isNotEmpty);
        updateChain.set(ASSETS_IMAGE.FILE_SIZE, cmsAssetsImageVO.getFileSize(), VerifyUtils::isNotEmpty);
        updateChain.set(ASSETS_IMAGE.HANDPICK, cmsAssetsImageVO.getHandpick(), VerifyUtils::isNotEmpty);
        updateChain.set(ASSETS_IMAGE.STATUS, cmsAssetsImageVO.getStatus(), VerifyUtils::isNotEmpty);

        updateChain.set(ASSETS_COLLECTION.UPDATED_AT, new Date());

        updateChain.set(ASSETS_IMAGE.CLASSIFICATION_ID, cmsAssetsImageVO.getClassificationId());

        updateChain.update();

        // 如果传了collectionIds，更新专辑关联信息
        if (cmsAssetsImageVO.getCollectionIds() != null) {
            assetsCollectionRelevanceService.updateCollectionRelevances(cmsAssetsImageVO.getId(),
                cmsAssetsImageVO.getCollectionIds(), 10);
        }
    }

    /**
     * 上架
     *
     * @param id
     */
    public void onShelf(Long id) {
        UpdateChain updateChain = this.updateChain().where(ASSETS_IMAGE.ID.eq(id));
        updateChain.set(ASSETS_IMAGE.STATUS, 10);
        updateChain.update();
    }

    /**
     * 下架
     *
     * @param id
     */
    public void offShelf(Long id) {
        UpdateChain updateChain = this.updateChain().where(ASSETS_IMAGE.ID.eq(id));
        updateChain.set(ASSETS_IMAGE.STATUS, 20);
        updateChain.update();
    }

    /**
     * 设置精选
     *
     * @param id
     */
    public void setHandpick(Long id) {
        UpdateChain updateChain = this.updateChain().where(ASSETS_IMAGE.ID.eq(id));
        updateChain.set(ASSETS_IMAGE.HANDPICK, 1);
        updateChain.update();
    }

    /**
     * 取消精选
     *
     * @param id
     */
    public void cancelHandpick(Long id) {
        UpdateChain updateChain = this.updateChain().where(ASSETS_IMAGE.ID.eq(id));
        updateChain.set(ASSETS_IMAGE.HANDPICK, 0);
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
        assetsCollectionRelevanceService.deleteCollectionRelevancesByAssetsId(id, 10);

        // 删除图片记录
        super.mapper.deleteById(id);
    }

}

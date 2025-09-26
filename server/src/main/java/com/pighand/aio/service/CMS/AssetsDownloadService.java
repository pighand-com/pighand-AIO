package com.pighand.aio.service.CMS;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.pighand.aio.domain.CMS.AssetsDownloadDomain;
import com.pighand.aio.mapper.CMS.AssetsDownloadMapper;
import com.pighand.aio.vo.CMS.AssetsDownloadVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.pighand.aio.domain.CMS.table.AssetsDocTableDef.ASSETS_DOC;
import static com.pighand.aio.domain.CMS.table.AssetsDownloadTableDef.ASSETS_DOWNLOAD;
import static com.pighand.aio.domain.CMS.table.AssetsImageTableDef.ASSETS_IMAGE;
import static com.pighand.aio.domain.CMS.table.AssetsVideoTableDef.ASSETS_VIDEO;

/**
 * CMS - 素材 - 下载记录
 *
 * @author wangshuli
 * @createDate 2025-01-25 10:00:00
 */
@Service
@RequiredArgsConstructor
public class AssetsDownloadService extends BaseServiceImpl<AssetsDownloadMapper, AssetsDownloadDomain> {

    private final AssetsImageService assetsImageService;

    private final AssetsVideoService assetsVideoService;

    private final AssetsDocService assetsDocService;

    /**
     * 创建下载记录
     *
     * @param assetsDownloadVO
     * @return
     */
    public AssetsDownloadVO create(AssetsDownloadVO assetsDownloadVO) {
        super.mapper.insert(assetsDownloadVO);
        return assetsDownloadVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public AssetsDownloadDomain find(Long id) {
        return super.mapper.find(id, null);
    }

    /**
     * 分页或列表
     *
     * @param assetsDownloadVO
     * @return PageOrList<AssetsDownloadVO>
     */
    public PageOrList<AssetsDownloadVO> query(AssetsDownloadVO assetsDownloadVO) {
        QueryWrapper queryWrapper = QueryWrapper.create()
            // equal
            .and(ASSETS_DOWNLOAD.ASSETS_TYPE.eq(assetsDownloadVO.getAssetsType()))
            .and(ASSETS_DOWNLOAD.ASSETS_ID.eq(assetsDownloadVO.getAssetsId()))
            .and(ASSETS_DOWNLOAD.CREATED_BY.eq(assetsDownloadVO.getCreatedBy()))
            // 按下载时间倒序排列
            .orderBy(ASSETS_DOWNLOAD.UPDATED_AT.desc());

        return super.mapper.query(assetsDownloadVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param assetsDownloadVO
     */
    public void update(AssetsDownloadVO assetsDownloadVO) {
        UpdateChain updateChain = this.updateChain().where(ASSETS_DOWNLOAD.ID.eq(assetsDownloadVO.getId()));

        updateChain.set(ASSETS_DOWNLOAD.UPDATED_AT, new Date(), VerifyUtils::isNotEmpty);

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
     * 查询当前用户是否已下载指定素材
     *
     * @param assetsId   素材ID
     * @param assetsType 素材类型 10图片 20视频 30文档
     * @param userId     用户ID
     * @return 是否已下载
     */
    public boolean isDownloaded(Long assetsId, Integer assetsType, Long userId) {
        QueryWrapper queryWrapper = QueryWrapper.create().where(ASSETS_DOWNLOAD.ASSETS_ID.eq(assetsId))
            .and(ASSETS_DOWNLOAD.ASSETS_TYPE.eq(assetsType)).and(ASSETS_DOWNLOAD.CREATED_BY.eq(userId));

        return super.mapper.selectCountByQuery(queryWrapper) > 0;
    }

    /**
     * 创建或更新下载记录
     * 如果已存在记录，则更新最后下载时间；如果不存在，则创建新记录并更新素材下载次数
     *
     * @param assetsId   素材ID
     * @param assetsType 素材类型 10图片 20视频 30文档
     * @param userId     用户ID
     * @return 下载记录
     */
    public AssetsDownloadVO createOrUpdateDownload(Long assetsId, Integer assetsType, Long userId) {
        // 查询是否已存在下载记录
        QueryWrapper queryWrapper = QueryWrapper.create().where(ASSETS_DOWNLOAD.ASSETS_ID.eq(assetsId))
            .and(ASSETS_DOWNLOAD.ASSETS_TYPE.eq(assetsType)).and(ASSETS_DOWNLOAD.CREATED_BY.eq(userId));

        AssetsDownloadVO existingRecord = super.mapper.selectOneByQueryAs(queryWrapper, AssetsDownloadVO.class);

        if (existingRecord != null) {
            // 更新最后下载时间
            existingRecord.setUpdatedAt(new Date());
            UpdateChain.of(AssetsDownloadDomain.class).set(ASSETS_DOWNLOAD.UPDATED_AT, new Date())
                .where(ASSETS_DOWNLOAD.ID.eq(existingRecord.getId())).update();

            // 更新对应素材的下载次数
            updateAssetDownloadCount(assetsId, assetsType);
            
            return existingRecord;
        } else {
            // 创建新的下载记录
            AssetsDownloadVO assetsDownloadVO = new AssetsDownloadVO();
            assetsDownloadVO.setAssetsId(assetsId);
            assetsDownloadVO.setAssetsType(assetsType);
            assetsDownloadVO.setCreatedBy(userId);
            assetsDownloadVO.setCreatedAt(new Date());
            assetsDownloadVO.setUpdatedAt(new Date());

            AssetsDownloadVO result = create(assetsDownloadVO);

            // 更新对应素材的下载次数
            updateAssetDownloadCount(assetsId, assetsType);

            return result;
        }
    }

    /**
     * 更新对应素材的下载次数
     * 参考AssetsDocService中增加浏览次数的实现方式
     *
     * @param assetsId   素材ID
     * @param assetsType 素材类型 10图片 20视频 30文档
     */
    private void updateAssetDownloadCount(Long assetsId, Integer assetsType) {
        try {
            switch (assetsType) {
                case 10: // 图片
                    UpdateChain.of(com.pighand.aio.domain.CMS.AssetsImageDomain.class)
                        .set(ASSETS_IMAGE.DOWNLOAD_COUNT, ASSETS_IMAGE.DOWNLOAD_COUNT.add(1))
                        .where(ASSETS_IMAGE.ID.eq(assetsId)).update();
                    break;
                case 20: // 视频
                    UpdateChain.of(com.pighand.aio.domain.CMS.AssetsVideoDomain.class)
                        .set(ASSETS_VIDEO.DOWNLOAD_COUNT, ASSETS_VIDEO.DOWNLOAD_COUNT.add(1))
                        .where(ASSETS_VIDEO.ID.eq(assetsId)).update();
                    break;
                case 30: // 文档
                    UpdateChain.of(com.pighand.aio.domain.CMS.AssetsDocDomain.class)
                        .set(ASSETS_DOC.DOWNLOAD_COUNT, ASSETS_DOC.DOWNLOAD_COUNT.add(1))
                        .where(ASSETS_DOC.ID.eq(assetsId)).update();
                    break;
                default:
                    // 未知类型，记录日志但不抛出异常
                    System.out.println("未知的素材类型: " + assetsType);
                    break;
            }
        } catch (Exception e) {
            // 更新下载次数失败不影响下载记录的创建，只记录日志
            System.err.println("更新素材下载次数失败: assetsId=" + assetsId + ", assetsType=" + assetsType + ", error="
                + e.getMessage());
        }
    }

    /**
     * 获取用户下载列表
     *
     * @param userId     用户ID
     * @param assetsType 素材类型，可选
     * @param pageNumber 页码
     * @param pageSize   每页大小
     * @return 下载列表，包含素材详细信息
     */
    public PageOrList<AssetsDownloadVO> getDownloadList(Long userId, Integer assetsType, Long pageNumber,
        Long pageSize) {
        AssetsDownloadDomain queryDomain = new AssetsDownloadDomain();
        queryDomain.setCreatedBy(userId);
        
        // 将assetsType参数设置到domain中，这样mapper可以直接使用
        if (assetsType != null) {
            queryDomain.setAssetsType(assetsType);
        }

        if (pageNumber != null && pageSize != null) {
            queryDomain.setPageNumber(pageNumber);
            queryDomain.setPageSize(pageSize);
        }

        // 使用新的关联查询方法，不再需要额外的queryWrapper
        return super.mapper.queryWithAssetDetails(queryDomain, null);
    }
}
package com.pighand.aio.service.CMS;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.pighand.aio.domain.CMS.AssetsCollectionDomain;
import com.pighand.aio.mapper.CMS.AssetsCollectionMapper;
import com.pighand.aio.vo.CMS.AssetsCollectionVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import org.springframework.stereotype.Service;

import static com.pighand.aio.domain.CMS.table.AssetsCollectionTableDef.ASSETS_COLLECTION;

/**
 * CMS - 素材 - 专辑
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@Service
public class AssetsCollectionService extends BaseServiceImpl<AssetsCollectionMapper, AssetsCollectionDomain> {

    /**
     * 创建
     *
     * @param cmsAssetsCollectionVO
     * @return
     */
    public AssetsCollectionVO create(AssetsCollectionVO cmsAssetsCollectionVO) {
        super.mapper.insert(cmsAssetsCollectionVO);

        return cmsAssetsCollectionVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public AssetsCollectionDomain find(Long id) {
        return super.mapper.find(id);
    }

    /**
     * 分页或列表
     *
     * @param cmsAssetsCollectionVO
     * @return PageOrList<AssetsCollectionVO>
     */
    public PageOrList<AssetsCollectionVO> query(AssetsCollectionVO cmsAssetsCollectionVO) {
        QueryWrapper queryWrapper = QueryWrapper.create()

            .select(ASSETS_COLLECTION.DEFAULT_COLUMNS)

            // like
            .and(ASSETS_COLLECTION.NAME.like(cmsAssetsCollectionVO.getName()))
            .and(ASSETS_COLLECTION.COVER_URL.like(cmsAssetsCollectionVO.getCoverUrl()))

            // equal
            .and(ASSETS_COLLECTION.CREATED_BY.eq(cmsAssetsCollectionVO.getCreatedBy()));

        return super.mapper.query(cmsAssetsCollectionVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param cmsAssetsCollectionVO
     */
    public void update(AssetsCollectionVO cmsAssetsCollectionVO) {
        UpdateChain updateChain = this.updateChain().where(ASSETS_COLLECTION.ID.eq(cmsAssetsCollectionVO.getId()));

        updateChain.set(ASSETS_COLLECTION.ID, cmsAssetsCollectionVO.getId(), VerifyUtils::isNotEmpty);
        updateChain.set(ASSETS_COLLECTION.CREATED_AT, cmsAssetsCollectionVO.getCreatedAt(), VerifyUtils::isNotEmpty);
        updateChain.set(ASSETS_COLLECTION.UPDATED_AT, cmsAssetsCollectionVO.getUpdatedAt(), VerifyUtils::isNotEmpty);
        updateChain.set(ASSETS_COLLECTION.DELETED, cmsAssetsCollectionVO.getDeleted(), VerifyUtils::isNotEmpty);

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
}

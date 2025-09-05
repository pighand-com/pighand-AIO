package com.pighand.aio.service.CMS;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.CMS.AssetsClassificationDomain;
import com.pighand.aio.mapper.CMS.AssetsClassificationMapper;
import com.pighand.aio.service.CmsAssetsClassificationService;
import com.pighand.aio.vo.CMS.AssetsClassificationVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import org.springframework.stereotype.Service;

/**
 * CMS - 素材 - 分类
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@Service
public class AssetsClassificationService extends BaseServiceImpl<AssetsClassificationMapper, AssetsClassificationDomain>
     implements CmsAssetsClassificationService{

    /**
     * 创建
     *
     * @param cmsAssetsClassificationVO
     * @return
     */
    @Override
    public AssetsClassificationVO create(AssetsClassificationVO cmsAssetsClassificationVO) {
        super.mapper.insert(cmsAssetsClassificationVO);

        return cmsAssetsClassificationVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public AssetsClassificationDomain find(Long id) {
        return super.mapper.find(id, null);
    }

    /**
     * 分页或列表
     *
     * @param cmsAssetsClassificationVO
     * @return PageOrList<CmsAssetsClassificationVO>
     */
    @Override
    public PageOrList<AssetsClassificationVO> query(AssetsClassificationVO cmsAssetsClassificationVO) {

        QueryWrapper queryWrapper = QueryWrapper.create()

        // like
        .and(CMS_ASSETS_CLASSIFICATION.NAME.like(cmsAssetsClassificationVO.getName()))

        // equal
        .and(CMS_ASSETS_CLASSIFICATION.PARENT_ID.eq(cmsAssetsClassificationVO.getParentId()))
        .and(CMS_ASSETS_CLASSIFICATION.INLAY.eq(cmsAssetsClassificationVO.getInlay()))
        .and(CMS_ASSETS_CLASSIFICATION.CREATED_BY.eq(cmsAssetsClassificationVO.getCreatedBy()))
        ;

        return super.mapper.query(cmsAssetsClassificationVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param cmsAssetsClassificationVO
     */
    @Override
    public void update(AssetsClassificationVO cmsAssetsClassificationVO) {
        UpdateChain updateChain = this.updateChain().where(CMS_ASSETS_CLASSIFICATION.ID.eq(cmsAssetsClassificationVO.getId()));

            updateChain.set(CMS_ASSETS_CLASSIFICATION.ID, cmsAssetsClassificationVO.getId(), , VerifyUtils::isNotEmpty);
            updateChain.set(CMS_ASSETS_CLASSIFICATION.CREATED_AT, cmsAssetsClassificationVO.getCreatedAt(), , VerifyUtils::isNotEmpty);
            updateChain.set(CMS_ASSETS_CLASSIFICATION.UPDATED_AT, cmsAssetsClassificationVO.getUpdatedAt(), , VerifyUtils::isNotEmpty);
            updateChain.set(CMS_ASSETS_CLASSIFICATION.DELETED, cmsAssetsClassificationVO.getDeleted(), , VerifyUtils::isNotEmpty);

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

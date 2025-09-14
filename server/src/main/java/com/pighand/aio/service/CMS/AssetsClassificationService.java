package com.pighand.aio.service.CMS;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.pighand.aio.domain.CMS.AssetsClassificationDomain;
import com.pighand.aio.mapper.CMS.AssetsClassificationMapper;
import com.pighand.aio.vo.CMS.AssetsClassificationVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import org.springframework.stereotype.Service;

import static com.pighand.aio.domain.CMS.table.AssetsClassificationTableDef.ASSETS_CLASSIFICATION;

/**
 * CMS - 素材 - 分类
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@Service
public class AssetsClassificationService
    extends BaseServiceImpl<AssetsClassificationMapper, AssetsClassificationDomain> {

    /**
     * 创建
     *
     * @param cmsAssetsClassificationVO
     * @return
     */
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
    public AssetsClassificationDomain find(Long id) {
        return super.mapper.find(id);
    }

    /**
     * 分页或列表
     *
     * @param cmsAssetsClassificationVO
     * @return PageOrList<AssetsClassificationVO>
     */
    public PageOrList<AssetsClassificationVO> query(AssetsClassificationVO cmsAssetsClassificationVO) {

        QueryWrapper queryWrapper = QueryWrapper.create()

            // like
            .and(ASSETS_CLASSIFICATION.NAME.like(cmsAssetsClassificationVO.getName()))

            // equal
            .and(ASSETS_CLASSIFICATION.PARENT_ID.eq(cmsAssetsClassificationVO.getParentId()))
            .and(ASSETS_CLASSIFICATION.INLAY.eq(cmsAssetsClassificationVO.getInlay()))
            .and(ASSETS_CLASSIFICATION.CREATED_BY.eq(cmsAssetsClassificationVO.getCreatedBy()));

        return super.mapper.query(cmsAssetsClassificationVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param cmsAssetsClassificationVO
     */
    public void update(AssetsClassificationVO cmsAssetsClassificationVO) {
        UpdateChain updateChain =
            this.updateChain().where(ASSETS_CLASSIFICATION.ID.eq(cmsAssetsClassificationVO.getId()));

        updateChain.set(ASSETS_CLASSIFICATION.NAME, cmsAssetsClassificationVO.getName(), VerifyUtils::isNotEmpty);
        updateChain.set(ASSETS_CLASSIFICATION.PARENT_ID, cmsAssetsClassificationVO.getParentId());
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

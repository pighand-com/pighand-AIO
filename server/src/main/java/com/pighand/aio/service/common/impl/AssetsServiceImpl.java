package com.pighand.aio.service.common.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.pighand.aio.domain.common.AssetsDomain;
import com.pighand.aio.mapper.common.AssetsMapper;
import com.pighand.aio.service.common.AssetsService;
import com.pighand.aio.vo.common.AssetsVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import org.springframework.stereotype.Service;

import static com.pighand.aio.domain.common.table.AssetsTableDef.ASSETS;

/**
 * 公共 - 素材
 *
 * @author wangshuli
 * @createDate 2024-12-31 19:04:50
 */
@Service
public class AssetsServiceImpl extends BaseServiceImpl<AssetsMapper, AssetsDomain> implements AssetsService {

    /**
     * 创建
     *
     * @param comAssetsVO
     * @return
     */
    @Override
    public AssetsVO create(AssetsVO comAssetsVO) {
        super.mapper.insert(comAssetsVO);

        return comAssetsVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public AssetsDomain find(Long id) {
        return super.mapper.find(id, null);
    }

    /**
     * 分页或列表
     *
     * @param comAssetsVO
     * @return PageOrList<ComAssetsVO>
     */
    @Override
    public PageOrList<AssetsVO> query(AssetsVO comAssetsVO) {

        QueryWrapper queryWrapper = QueryWrapper.create()

            // like
            .and(ASSETS.TAG.like(comAssetsVO.getTag())).and(ASSETS.URL.like(comAssetsVO.getUrl()));

        return super.mapper.query(comAssetsVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param comAssetsVO
     */
    @Override
    public void update(AssetsVO comAssetsVO) {
        UpdateChain updateChain = this.updateChain().where(ASSETS.ID.eq(comAssetsVO.getId()));

        updateChain.set(ASSETS.ID, comAssetsVO.getId(), VerifyUtils::isNotEmpty);

        updateChain.update();
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

package com.pighand.aio.service.ECommerce.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.pighand.aio.common.enums.GoodsSpuStatusEnum;
import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.domain.ECommerce.GoodsSkuDomain;
import com.pighand.aio.domain.ECommerce.GoodsSpuDomain;
import com.pighand.aio.domain.base.ProjectDefaultDomain;
import com.pighand.aio.entityMapper.ECommerce.GoodsSkuEntityMapper;
import com.pighand.aio.mapper.ECommerce.GoodsSkuMapper;
import com.pighand.aio.mapper.ECommerce.GoodsSpuMapper;
import com.pighand.aio.service.ECommerce.GoodsSpuService;
import com.pighand.aio.service.base.ProjectDefaultService;
import com.pighand.aio.vo.ECommerce.GoodsSkuVO;
import com.pighand.aio.vo.ECommerce.GoodsSpuVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.pighand.aio.domain.ECommerce.table.GoodsSkuTableDef.GOODS_SKU;
import static com.pighand.aio.domain.ECommerce.table.GoodsSpuTableDef.GOODS_SPU;
import static com.pighand.aio.domain.base.table.ProjectDefaultTableDef.PROJECT_DEFAULT;

/**
 * 电商 - SPU
 *
 * @author wangshuli
 * @createDate 2024-01-07 19:55:48
 */
@RequiredArgsConstructor
@Service
public class GoodsSpuServiceImpl extends BaseServiceImpl<GoodsSpuMapper, GoodsSpuDomain> implements GoodsSpuService {

    private final GoodsSkuMapper goodsSkuMapper;
    private final GoodsSkuEntityMapper goodsSkuEntityMapper;
    private final ProjectDefaultService projectDefaultService;

    /**
     * 创建
     *
     * @param goodsSpuVO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public GoodsSpuVO create(GoodsSpuVO goodsSpuVO) {
        goodsSpuVO.setProjectId(Context.getProjectId());
        goodsSpuVO.setStatus(GoodsSpuStatusEnum.UNLISTED);
        goodsSpuVO.setDeleted(false);
        super.mapper.insert(goodsSpuVO);

        List<GoodsSkuVO> goodsSku = goodsSpuVO.getGoodsSku();
        goodsSku.forEach(goodsSkuVO -> {
            goodsSkuVO.setProjectId(Context.getProjectId());
            goodsSkuVO.setSpuId(goodsSpuVO.getId());
            goodsSkuVO.setDeleted(false);
        });

        List<GoodsSkuDomain> sku = goodsSkuEntityMapper.toDomainList(goodsSku);
        goodsSkuMapper.insertBatch(sku);

        return goodsSpuVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public GoodsSpuVO find(Long id) {
        return super.mapper.find(id, GOODS_SKU.getTableName());
    }

    /**
     * 分页或列表
     *
     * @param goodsSpuVO
     * @return PageOrList<GoodsSpuVO>
     */
    @Override
    public PageOrList<GoodsSpuVO> query(GoodsSpuVO goodsSpuVO) {
        ProjectDefaultDomain projectDefaultDomain =
            projectDefaultService.queryChain().where(PROJECT_DEFAULT.ID.eq(Context.getProjectId())).one();
        if (goodsSpuVO.getSystem().equals("ios") && (projectDefaultDomain == null
            || projectDefaultDomain.getDefaultNickname().equals("1"))) {
            return null;
        }

        goodsSpuVO.setJoinTables(GOODS_SKU.getTableName());

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.where(GoodsSpuDomain::getProjectId).eq(Context.getProjectId());
        // like
        queryWrapper.and(GOODS_SPU.NAME.like(goodsSpuVO.getName(), VerifyUtils::isNotEmpty));

        // equal
        queryWrapper.and(GOODS_SPU.GOODS_CATEGORY_ID.eq(goodsSpuVO.getGoodsCategoryId(), VerifyUtils::isNotEmpty));
        queryWrapper.and(GOODS_SPU.STATUS.eq(goodsSpuVO.getStatus(), VerifyUtils::isNotEmpty));

        return super.mapper.query(goodsSpuVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param goodsSpuVO
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(GoodsSpuVO goodsSpuVO) {
        UpdateChain updateChain = this.updateChain().where(GOODS_SPU.ID.eq(goodsSpuVO.getId()));

        boolean needUpdate = false;
        if (VerifyUtils.isNotEmpty(goodsSpuVO.getName())) {
            needUpdate = true;
            updateChain.set(GOODS_SPU.NAME, goodsSpuVO.getName());
        }
        if (VerifyUtils.isNotEmpty(goodsSpuVO.getGoodsCategoryId())) {
            needUpdate = true;
            updateChain.set(GOODS_SPU.GOODS_CATEGORY_ID, goodsSpuVO.getGoodsCategoryId());
        }
        if (VerifyUtils.isNotEmpty(goodsSpuVO.getImageUrls())) {
            needUpdate = true;
            updateChain.set(GOODS_SPU.IMAGE_URLS, goodsSpuVO.getImageUrls());
        }
        if (VerifyUtils.isNotEmpty(goodsSpuVO.getStatus())) {
            needUpdate = true;
            updateChain.set(GOODS_SPU.STATUS, goodsSpuVO.getStatus());
        }

        if (needUpdate) {
            updateChain.update();
        }

        if (VerifyUtils.isNotEmpty(goodsSpuVO.getGoodsSku())) {
            goodsSpuVO.getGoodsSku().forEach(goodsSkuVO -> {
                goodsSkuMapper.update(goodsSkuVO);
            });
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

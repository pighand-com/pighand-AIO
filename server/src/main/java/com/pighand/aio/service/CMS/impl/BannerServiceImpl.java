package com.pighand.aio.service.CMS.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.pighand.aio.domain.CMS.BannerDomain;
import com.pighand.aio.mapper.CMS.BannerMapper;
import com.pighand.aio.service.CMS.BannerService;
import com.pighand.aio.service.common.UploadService;
import com.pighand.aio.vo.CMS.BannerVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.exception.ThrowException;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.pighand.aio.domain.CMS.table.BannerTableDef.BANNER;

/**
 * CMS - banner
 *
 * @author wangshuli
 * @createDate 2025-06-17 13:59:52
 */
@Service
@RequiredArgsConstructor
public class BannerServiceImpl extends BaseServiceImpl<BannerMapper, BannerDomain> implements BannerService {

    private final UploadService uploadService;

    /**
     * 创建
     *
     * @param cmsBannerVO
     * @return
     */
    @Override
    public BannerVO create(BannerVO cmsBannerVO) {
        // 设置默认状态为下架
        if (cmsBannerVO.getStatus() == null) {
            cmsBannerVO.setStatus(0);
        }
        super.mapper.insert(cmsBannerVO);

        uploadService.updateFileOfficial(cmsBannerVO.getImageUrl());

        return cmsBannerVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public BannerDomain find(Long id) {
        return super.mapper.find(id);
    }

    /**
     * 分页或列表
     *
     * @param cmsBannerVO
     * @return PageOrList<CmsBannerVO>
     */
    @Override
    public PageOrList<BannerVO> query(BannerVO cmsBannerVO) {

        QueryWrapper queryWrapper = QueryWrapper.create()
            // equal
            .and(BANNER.APPLICATION_ID.eq(cmsBannerVO.getApplicationId(), VerifyUtils::isNotEmpty))
            .and(BANNER.STATUS.eq(cmsBannerVO.getStatus(), VerifyUtils::isNotEmpty));

        return super.mapper.query(cmsBannerVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param cmsBannerVO
     */
    @Override
    public void update(BannerVO cmsBannerVO) {
        BannerDomain bannerDomain =
            super.queryChain().select(BANNER.IMAGE_URL).where(BANNER.ID.eq(cmsBannerVO.getId())).one();

        if (bannerDomain == null) {
            throw new ThrowException("未找到banner");
        }

        if (!bannerDomain.getImageUrl().equals(cmsBannerVO.getImageUrl())) {
            uploadService.updateFileOfficial(cmsBannerVO.getImageUrl());
        }

        UpdateChain updateChain = this.updateChain().where(BANNER.ID.eq(cmsBannerVO.getId()));

        updateChain.set(BANNER.IMAGE_URL, cmsBannerVO.getImageUrl());
        updateChain.set(BANNER.REDIRECTION_PATH, cmsBannerVO.getRedirectionPath());

        updateChain.update();
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        BannerDomain bannerDomain = super.queryChain().select(BANNER.IMAGE_URL).where(BANNER.ID.eq(id)).one();

        if (bannerDomain == null) {
            throw new ThrowException("未找到banner");
        }

        super.mapper.deleteById(id);

        if (VerifyUtils.isNotEmpty(bannerDomain.getImageUrl())) {
            uploadService.deleteFileOfficial(bannerDomain.getImageUrl());
        }
    }

    /**
     * 上架
     *
     * @param id
     */
    @Override
    public void online(Long id) {
        UpdateChain.of(BannerDomain.class).set(BANNER.STATUS, 10).where(BANNER.ID.eq(id)).update();
    }

    /**
     * 下架
     *
     * @param id
     */
    @Override
    public void offline(Long id) {
        UpdateChain.of(BannerDomain.class).set(BANNER.STATUS, 0).where(BANNER.ID.eq(id)).update();
    }
}

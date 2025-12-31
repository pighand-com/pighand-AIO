package com.pighand.aio.service.ECommerce;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.ThemeDomain;
import com.pighand.aio.mapper.ECommerce.ThemeMapper;
import com.pighand.aio.service.base.StoreService;
import com.pighand.aio.service.common.UploadService;
import com.pighand.aio.vo.ECommerce.ThemeVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.exception.ThrowException;
import com.pighand.framework.spring.page.PageOrList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.pighand.aio.domain.ECommerce.table.SessionTableDef.SESSION;
import static com.pighand.aio.domain.ECommerce.table.SessionTemplateTableDef.SESSION_TEMPLATE;
import static com.pighand.aio.domain.ECommerce.table.ThemeTableDef.THEME;

/**
 * 电商 - 主题
 *
 * @author wangshuli
 * @createDate 2024-05-23 15:01:58
 */
@Service
@RequiredArgsConstructor
public class ThemeService extends BaseServiceImpl<ThemeMapper, ThemeDomain>  {

    private final UploadService uploadService;

    private final StoreService storeService;

    /**
     * 创建
     *
     * @param themeVO
     * @return
     */
    public ThemeVO create(ThemeVO themeVO) {
        super.mapper.insert(themeVO);

        uploadService.updateFileOfficial(themeVO.getPosterUrl(), themeVO.getServiceQrUrl());

        return themeVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public ThemeVO find(Long id) {
        ThemeVO theme = super.mapper.find(id, SESSION.getTableName(), SESSION_TEMPLATE.getTableName());

        // 无客服二维码，使用门店客服二维码
        if (theme.getServiceQrUrl() == null) {
            StoreVO store = storeService.find(theme.getStoreId());
            theme.setServiceQrUrl(store.getServiceQrUrl());
        }

        return theme;
    }

    /**
     * 分页或列表
     *
     * @param themeVO
     * @return PageOrList<ThemeVO>
     */
    public PageOrList<ThemeVO> query(ThemeVO themeVO) {
        themeVO.setJoinTables(SESSION.getTableName(), SESSION_TEMPLATE.getTableName());

        QueryWrapper queryWrapper = QueryWrapper.create()
            // like
            .and(THEME.THEME_NAME.like(themeVO.getThemeName()))
            .and(THEME.THEME_INTRODUCTIONE.like(themeVO.getThemeIntroductione()))
            .and(THEME.POSTER_URL.like(themeVO.getPosterUrl()))
            .and(THEME.PICTURE_DESCRIPTION.like(themeVO.getPictureDescription()))

            // equal
            .and(THEME.APPLICATION_ID.eq(themeVO.getApplicationId())).and(THEME.STORE_ID.eq(themeVO.getStoreId()))
            .and(THEME.ORIGINAL_PRICE.eq(themeVO.getOriginalPrice()))
            .and(THEME.PRESENT_PRICE.eq(themeVO.getPresentPrice())).and(THEME.MIN_PEOPLE.eq(themeVO.getMinPeople()))
            .and(THEME.MAX_PEOPLE.eq(themeVO.getMaxPeople())).and(THEME.DURATION.eq(themeVO.getDuration()));

        return super.mapper.query(themeVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param themeVO
     */
    public void update(ThemeVO themeVO) {
        ThemeDomain themeDomain = super.queryChain().select(THEME.POSTER_URL).where(THEME.ID.eq(themeVO.getId())).one();

        if (themeDomain == null) {
            throw new ThrowException("未找到主题");
        }

        uploadService.replaceFileOfficial(themeDomain.getPosterUrl(), themeVO.getPosterUrl(), themeVO.getServiceQrUrl(),
            themeDomain.getServiceQrUrl());

        super.mapper.update(themeVO);
    }

    /**
     * 修改排队时长
     *
     * @param id
     * @param queueDuration
     */
    public void updateQueueDuration(Long id, Integer queueDuration) {
        ThemeVO themeVO = new ThemeVO();
        themeVO.setId(id);
        themeVO.setQueueDuration(Optional.ofNullable(queueDuration).orElse(0));
        super.mapper.update(themeVO);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(Long id) {
        ThemeDomain themeDomain = super.queryChain().select(THEME.POSTER_URL).where(THEME.ID.eq(id)).one();

        if (themeDomain == null) {
            throw new ThrowException("主题不存在");
        }

        super.mapper.deleteById(id);

        // 删除COS文件
        uploadService.deleteFileOfficial(themeDomain.getPosterUrl(), themeDomain.getServiceQrUrl());
    }
}

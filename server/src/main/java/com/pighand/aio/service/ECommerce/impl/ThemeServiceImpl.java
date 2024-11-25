package com.pighand.aio.service.ECommerce.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.ThemeDomain;
import com.pighand.aio.mapper.ECommerce.ThemeMapper;
import com.pighand.aio.service.ECommerce.ThemeService;
import com.pighand.aio.vo.ECommerce.ThemeVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import org.springframework.stereotype.Service;

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
public class ThemeServiceImpl extends BaseServiceImpl<ThemeMapper, ThemeDomain> implements ThemeService {

    /**
     * 创建
     *
     * @param themeVO
     * @return
     */
    @Override
    public ThemeVO create(ThemeVO themeVO) {
        super.mapper.insert(themeVO);

        return themeVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public ThemeDomain find(Long id) {
        return super.mapper.find(id, SESSION.getTableName(), SESSION_TEMPLATE.getTableName());
    }

    /**
     * 分页或列表
     *
     * @param themeVO
     * @return PageOrList<ThemeVO>
     */
    @Override
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
    @Override
    public void update(ThemeVO themeVO) {
        super.mapper.update(themeVO);
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

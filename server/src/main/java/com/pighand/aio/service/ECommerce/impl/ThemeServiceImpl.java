package com.pighand.aio.service.ECommerce.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.ThemeDomain;
import com.pighand.aio.mapper.ECommerce.ThemeMapper;
import com.pighand.aio.service.ECommerce.ThemeService;
import com.pighand.aio.vo.ECommerce.ThemeVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
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

        QueryWrapper queryWrapper = new QueryWrapper();

        // like
        queryWrapper.and(THEME.THEME_NAME.like(themeVO.getThemeName(), VerifyUtils::isNotEmpty));
        queryWrapper.and(THEME.THEME_INTRODUCTIONE.like(themeVO.getThemeIntroductione(), VerifyUtils::isNotEmpty));
        queryWrapper.and(THEME.POSTER_URL.like(themeVO.getPosterUrl(), VerifyUtils::isNotEmpty));
        queryWrapper.and(THEME.PICTURE_DESCRIPTION.like(themeVO.getPictureDescription(), VerifyUtils::isNotEmpty));

        // equal
        queryWrapper.and(THEME.APPLICATION_ID.eq(themeVO.getApplicationId(), VerifyUtils::isNotEmpty));
        queryWrapper.and(THEME.STORE_ID.eq(themeVO.getStoreId(), VerifyUtils::isNotEmpty));
        queryWrapper.and(THEME.ORIGINAL_PRICE.eq(themeVO.getOriginalPrice(), VerifyUtils::isNotEmpty));
        queryWrapper.and(THEME.PRESENT_PRICE.eq(themeVO.getPresentPrice(), VerifyUtils::isNotEmpty));
        queryWrapper.and(THEME.MIN_PEOPLE.eq(themeVO.getMinPeople(), VerifyUtils::isNotEmpty));
        queryWrapper.and(THEME.MAX_PEOPLE.eq(themeVO.getMaxPeople(), VerifyUtils::isNotEmpty));
        queryWrapper.and(THEME.DURATION.eq(themeVO.getDuration(), VerifyUtils::isNotEmpty));

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

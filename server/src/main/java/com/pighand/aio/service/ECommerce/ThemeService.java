package com.pighand.aio.service.ECommerce;

import com.pighand.aio.domain.ECommerce.ThemeDomain;
import com.pighand.aio.vo.ECommerce.ThemeVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;

/**
 * 电商 - 主题
 *
 * @author wangshuli
 * @createDate 2024-05-23 15:01:58
 */
public interface ThemeService extends BaseService<ThemeDomain> {

    /**
     * 创建
     *
     * @param themeVO
     * @return
     */
    ThemeVO create(ThemeVO themeVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    ThemeVO find(Long id);

    /**
     * 分页或列表
     *
     * @param themeVO
     * @return PageOrList<ThemeVO>
     */
    PageOrList<ThemeVO> query(ThemeVO themeVO);

    /**
     * 修改
     *
     * @param themeVO
     */
    void update(ThemeVO themeVO);

    void updateQueueDuration(Long id, Integer queueDuration);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}

package com.pighand.aio.service.base;

import com.pighand.aio.domain.base.UserDouyinDomain;
import com.pighand.aio.vo.base.UserDouyinVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;

/**
 * 用户 - 抖音
 *
 * @author wangshuli
 * @createDate 2024-06-05 23:58:27
 */
public interface UserDouyinService extends BaseService<UserDouyinDomain> {

    /**
     * 创建
     *
     * @param userDouyinVO
     * @return
     */
    UserDouyinVO create(UserDouyinVO userDouyinVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    UserDouyinDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param userDouyinVO
     * @return PageOrList<UserDouyinVO>
     */
    PageOrList<UserDouyinVO> query(UserDouyinVO userDouyinVO);

    /**
     * 修改
     *
     * @param userDouyinVO
     */
    void update(UserDouyinVO userDouyinVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}

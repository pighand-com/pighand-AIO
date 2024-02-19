package com.pighand.aio.service.user;

import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.aio.domain.user.UserBindDomain;
import com.pighand.aio.vo.user.UserBindVO;

/**
 * 用户 - 绑定信息
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
public interface UserBindService extends BaseService<UserBindDomain> {

    /**
     * 创建
     *
     * @param userBindVO
     * @return
     */
    UserBindVO create(UserBindVO userBindVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    UserBindDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param userBindVO
     * @return PageOrList<UserBindVO>
     */
    PageOrList<UserBindVO> query(UserBindVO userBindVO);

    /**
     * 修改
     *
     * @param userBindVO
     */
    void update(UserBindVO userBindVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}

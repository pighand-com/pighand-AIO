package com.pighand.aio.service.base;

import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.aio.domain.base.UserDomain;
import com.pighand.aio.vo.base.UserVO;

/**
 * 用户关键信息表，主要保存登录所用信息
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
public interface UserService extends BaseService<UserDomain> {

    /**
     * 创建
     *
     * @param userVO
     * @return
     */
    UserVO create(UserVO userVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    UserVO find(Long id);

    /**
     * 分页或列表
     *
     * @param userVO
     * @return PageOrList<UserVO>
     */
    PageOrList<UserVO> query(UserVO userVO);

    /**
     * 修改
     *
     * @param userVO
     */
    void update(UserVO userVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}

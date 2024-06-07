package com.pighand.aio.service.base;

import com.pighand.aio.domain.base.UserExtensionDomain;
import com.pighand.aio.vo.base.UserExtensionVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;

/**
 * 用户扩展信息。id与user表相同
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
public interface UserExtensionService extends BaseService<UserExtensionDomain> {

    /**
     * 创建
     *
     * @param userExtension
     * @return
     */
    UserExtensionDomain create(UserExtensionDomain userExtension);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    UserExtensionDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param userExtensionVO
     * @return PageOrList<UserExtensionVO>
     */
    PageOrList<UserExtensionVO> query(UserExtensionVO userExtensionVO);

    /**
     * 修改
     *
     * @param userExtensionVO
     */
    void update(UserExtensionVO userExtensionVO);

    /**
     * 修改三方平台信息
     *
     * @param userExtensionVO
     */
    void updateTripartitePlatformInfo(UserExtensionVO userExtensionVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}

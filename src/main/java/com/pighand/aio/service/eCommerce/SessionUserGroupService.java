package com.pighand.aio.service.ECommerce;

import com.pighand.aio.domain.ECommerce.SessionUserGroupDomain;
import com.pighand.aio.vo.ECommerce.SessionUserGroupVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;

/**
 * 电商 - 场次 - 用户分组
 *
 * @author wangshuli
 * @createDate 2023-12-05 16:13:27
 */
public interface SessionUserGroupService extends BaseService<SessionUserGroupDomain> {

    /**
     * 创建
     *
     * @param sessionUserGroupVO
     * @return
     */
    SessionUserGroupVO create(SessionUserGroupVO sessionUserGroupVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    SessionUserGroupDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param sessionUserGroupVO
     * @return PageOrList<SessionUserGroupVO>
     */
    PageOrList<SessionUserGroupVO> query(SessionUserGroupVO sessionUserGroupVO);

    /**
     * 修改
     *
     * @param sessionUserGroupVO
     */
    void update(SessionUserGroupVO sessionUserGroupVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);

    String getWechatAppletQrcode(Long money);
}

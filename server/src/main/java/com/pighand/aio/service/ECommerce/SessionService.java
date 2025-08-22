package com.pighand.aio.service.ECommerce;

import com.pighand.aio.domain.ECommerce.SessionDomain;
import com.pighand.aio.vo.ECommerce.SessionUserGroupVO;
import com.pighand.aio.vo.ECommerce.SessionVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;

/**
 * 电商 - 场次
 *
 * @author wangshuli
 * @createDate 2023-12-05 16:13:27
 */
public interface SessionService extends BaseService<SessionDomain> {

    /**
     * 创建
     *
     * @return
     */
    SessionVO create();

    /**
     * 详情
     *
     * @param id
     * @return
     */
    SessionVO find(Long id);

    /**
     * 分页或列表
     *
     * @param sessionVO
     * @return PageOrList<SessionVO>
     */
    PageOrList<SessionVO> query(SessionVO sessionVO);

    /**
     * 修改
     *
     * @param sessionVO
     */
    void update(SessionVO sessionVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);

    /**
     * 加入场次分组
     * <p>如果已经加入，则返回已加入的场次分组</p>
     * <p>如果没有加入，则加入场次分组</p>
     *
     * @param sessionId
     * @param sessionGroupId
     * @return
     */
    SessionUserGroupVO join(Long sessionId, Long sessionGroupId);

    SessionDomain findCurrentSession();

    /**
     * 查询当前加入的场次
     *
     * @param isReturnSessionInfo 是否返回场次信息。默认：false
     * @return
     */
    SessionUserGroupVO findCurrentJoined(Long userId, boolean isReturnSessionInfo);
}

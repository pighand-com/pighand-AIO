package com.pighand.aio.service.base;

import com.pighand.aio.domain.base.UserMessageDomain;
import com.pighand.aio.vo.base.UserMessageVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;

/**
 * 用户 - 消息
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
public interface UserMessageService extends BaseService<UserMessageDomain> {

    /**
     * 创建
     *
     * @param userMessageVO
     * @return
     */
    UserMessageVO create(UserMessageVO userMessageVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    UserMessageVO find(Long id);

    /**
     * 查询收到的
     *
     * @param userMessageVO
     * @return PageOrList<UserMessageVO>
     */
    PageOrList<UserMessageVO> queryReceived(UserMessageVO userMessageVO);

    /**
     * 查询发送的
     *
     * @param userMessageVO
     * @return PageOrList<UserMessageVO>
     */
    PageOrList<UserMessageVO> querySent(UserMessageVO userMessageVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}

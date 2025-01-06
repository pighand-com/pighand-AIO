package com.pighand.aio.service.base.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.util.UpdateEntity;
import com.pighand.aio.common.enums.UserMessageTypeEnum;
import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.domain.base.UserMessageDomain;
import com.pighand.aio.mapper.base.UserMessageMapper;
import com.pighand.aio.service.base.UserMessageService;
import com.pighand.aio.vo.base.LoginUser;
import com.pighand.aio.vo.base.UserMessageVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.exception.ThrowPrompt;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.page.PageType;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;

import static com.pighand.aio.domain.base.table.UserMessageTableDef.USER_MESSAGE;
import static com.pighand.aio.domain.base.table.UserTableDef.USER;

/**
 * 用户 - 消息
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Service
public class UserMessageServiceImpl extends BaseServiceImpl<UserMessageMapper, UserMessageDomain>
    implements UserMessageService {

    /**
     * 创建
     *
     * @param userMessageVO
     * @return
     */
    @Override
    public UserMessageVO create(UserMessageVO userMessageVO) {
        LoginUser loginUser = Context.loginUser();
        userMessageVO.setSenderId(loginUser.getId());
        userMessageVO.setSendAt(new Date());
        userMessageVO.setRead(false);
        userMessageVO.setType(UserMessageTypeEnum.USER);
        super.mapper.insert(userMessageVO);

        return userMessageVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public UserMessageVO find(Long id) {
        Long loginUserId = Context.loginUser().getId();

        // 只查接收人是登录用户的
        QueryWrapper queryWrapper = QueryWrapper.create();
        UserMessageVO message = super.mapper.find(id, USER.getTableName());

        if (message.getType().equals(UserMessageTypeEnum.USER) && !message.getSenderId().equals(loginUserId)
            && !message.getReceiverId().equals(loginUserId)) {
            throw new ThrowPrompt("未发现消息");
        }

        // 更新为已读
        if (message != null && (message.getRead() == null || !message.getRead())) {
            message.setRead(true);

            UserMessageDomain updateMessage = UpdateEntity.of(UserMessageDomain.class, id);
            updateMessage.setRead(true);

            this.mapper.update(updateMessage);
        }

        return message;
    }

    /**
     * 查询收到的
     *
     * @param userMessageVO
     * @return PageOrList<UserMessageVO>
     */
    @Override
    public PageOrList<UserMessageVO> queryReceived(UserMessageVO userMessageVO) {
        userMessageVO.setPageType(PageType.PAGE);
        userMessageVO.setJoinTables(Set.of("sender"));

        QueryWrapper queryWrapper = QueryWrapper.create();
        queryWrapper.where(USER_MESSAGE.RECEIVER_ID.eq(Context.loginUser().getId()));
        queryWrapper.orderBy(USER_MESSAGE.READ.asc(), USER_MESSAGE.ID.desc());

        return super.mapper.query(userMessageVO, queryWrapper);
    }

    /**
     * 查询发送的
     *
     * @param userMessageVO
     * @return PageOrList<UserMessageVO>
     */
    @Override
    public PageOrList<UserMessageVO> querySent(UserMessageVO userMessageVO) {
        userMessageVO.setPageType(PageType.PAGE);
        userMessageVO.setJoinTables(Set.of("receiver"));

        QueryWrapper queryWrapper = QueryWrapper.create();
        queryWrapper.where(USER_MESSAGE.SENDER_ID.eq(Context.loginUser().getId()));
        queryWrapper.orderBy(USER_MESSAGE.ID.desc());

        return super.mapper.query(userMessageVO, queryWrapper);
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        UserMessageDomain message = this.queryChain()
            .where(USER_MESSAGE.ID.eq(id).and(USER_MESSAGE.RECEIVER_ID.eq(Context.loginUser().getId()))).one();

        if (message == null) {
            throw new ThrowPrompt("消息不存在");
        }

        if (!message.getType().equals(UserMessageTypeEnum.USER)) {
            throw new ThrowPrompt("系统消息不可删除");
        }

        super.mapper.deleteById(id);
    }

}

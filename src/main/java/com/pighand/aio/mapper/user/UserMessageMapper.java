package com.pighand.aio.mapper.user;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.user.UserMessageDomain;
import com.pighand.aio.vo.user.UserMessageVO;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.framework.spring.page.PageOrList;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

import static com.pighand.aio.domain.user.table.UserMessageTableDef.USER_MESSAGE;
import static com.pighand.aio.domain.user.table.UserTableDef.USER;

/**
 * 用户 - 消息
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Mapper
public interface UserMessageMapper extends BaseMapper<UserMessageDomain> {

    /**
     * 基础查询条件
     *
     * @return
     */
    default QueryWrapper baseQuery(List<String> joinTables, QueryWrapper queryWrapper) {
        if (joinTables == null) {
            joinTables = new ArrayList<>();
        }

        if (queryWrapper == null) {
            queryWrapper = QueryWrapper.create();
        }

        queryWrapper.select(USER_MESSAGE.ALL_COLUMNS);

        if (joinTables.contains("sender")) {
            queryWrapper.select(USER.PHONE.as("sender"));
            queryWrapper.leftJoin(USER).as("sender").on(USER.ID.eq(USER_MESSAGE.SENDER_ID));
        }

        if (joinTables.contains("receiver")) {
            queryWrapper.select(USER.PHONE.as("receiver"));
            queryWrapper.leftJoin(USER).as("receiver").on(USER.ID.eq(USER_MESSAGE.RECEIVER_ID));
        }

        return queryWrapper;
    }

    /**
     * 查询详情
     *
     * @param id         主键
     * @param joinTables 关联表
     * @return
     */
    default UserMessageVO find(Long id, List<String> joinTables) {
        return this.find(id, joinTables, null);
    }

    /**
     * 查询详情
     *
     * @param id         主键
     * @param joinTables 关联表
     * @return
     */
    default UserMessageVO find(Long id, List<String> joinTables, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.baseQuery(joinTables, queryWrapper).where(USER_MESSAGE.ID.eq(id));

        return this.selectOneByQueryAs(finalQueryWrapper, UserMessageVO.class);
    }

    /**
     * 分页或列表
     *
     * @param userMessageDomain
     * @return
     */
    default PageOrList<UserMessageVO> query(UserMessageDomain userMessageDomain) {
        return this.query(userMessageDomain, null);
    }

    /**
     * 分页或列表
     *
     * @param userMessageDomain
     * @return
     */
    default PageOrList<UserMessageVO> query(UserMessageDomain userMessageDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.baseQuery(userMessageDomain.getJoinTables(), queryWrapper);

        return this.page(userMessageDomain, finalQueryWrapper, UserMessageVO.class);
    }
}

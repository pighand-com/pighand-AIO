package com.pighand.aio.mapper.user;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.user.UserDomain;
import com.pighand.aio.vo.user.UserVO;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.framework.spring.page.PageOrList;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

import static com.mybatisflex.core.query.QueryMethods.count;
import static com.pighand.aio.domain.user.table.UserBindTableDef.USER_BIND;
import static com.pighand.aio.domain.user.table.UserExtensionTableDef.USER_EXTENSION;
import static com.pighand.aio.domain.user.table.UserTableDef.USER;

/**
 * 用户关键信息表，主要保存登录所用信息
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDomain> {

    /**
     * 基础查询条件
     *
     * @return
     */
    default QueryWrapper baseQuery(List<String> joinTables, QueryWrapper queryWrapper) {
        if (joinTables == null) {
            joinTables = new ArrayList<>();
        }

        queryWrapper.select(USER.ID, USER.PHONE);

        if (queryWrapper == null) {
            queryWrapper = QueryWrapper.create();
        }

        if (joinTables.contains(USER_EXTENSION.getTableName())) {
            queryWrapper.select(USER_EXTENSION.NAME, USER_EXTENSION.PROFILE, USER_EXTENSION.REGISTER_AT);
            queryWrapper.leftJoin(USER_EXTENSION).on(USER_EXTENSION.ID.eq(USER.ID));
        }

        if (joinTables.contains("bind_count")) {
            queryWrapper.select(count(USER_BIND.ID).as("bindCount"));
            queryWrapper.leftJoin(USER_BIND).on(USER_BIND.UPPER_ID.eq(USER.ID));
            queryWrapper.groupBy(USER.ID, USER.PHONE);
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
    default UserVO find(Long id, List<String> joinTables) {
        QueryWrapper queryWrapper = this.baseQuery(joinTables, null).where(USER.ID.eq(id));

        return this.selectOneByQueryAs(queryWrapper, UserVO.class);
    }

    /**
     * 分页或列表
     *
     * @param userDomain
     * @return
     */
    default PageOrList<UserVO> query(UserDomain userDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.baseQuery(userDomain.getJoinTables(), queryWrapper);

        return this.page(userDomain, finalQueryWrapper, UserVO.class);
    }
}

package com.pighand.aio.mapper.user;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.aio.domain.user.UserBindDomain;
import com.pighand.aio.domain.user.table.UserBindTableDef;
import com.pighand.aio.domain.user.table.UserTableDef;
import com.pighand.aio.vo.user.UserBindVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户 - 绑定信息
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Mapper
public interface UserBindMapper extends BaseMapper<UserBindDomain> {

    UserBindTableDef USER_BIND = UserBindTableDef.USER_BIND;

    UserTableDef USER = UserTableDef.USER;

    /**
     * 基础查询条件
     *
     * @return
     */
    default QueryWrapper baseQuery(List<String> joinTables) {
        if (joinTables == null) {
            joinTables = new ArrayList<>();
        }

        QueryWrapper queryWrapper = QueryWrapper.create();

        if (joinTables.contains(USER.getTableName())) {
            queryWrapper.leftJoin(USER).on(USER.ID.eq(USER_BIND.UPPER_ID));
        }
        if (joinTables.contains(USER.getTableName())) {
            queryWrapper.leftJoin(USER).on(USER.ID.eq(USER_BIND.USER_ID));
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
    default UserBindVO find(Long id, List<String> joinTables) {
        QueryWrapper queryWrapper = this.baseQuery(joinTables).where(USER_BIND.ID.eq(id));

        return this.selectOneByQueryAs(queryWrapper, UserBindVO.class);
    }

    /**
     * 分页或列表
     *
     * @param userBindDomain
     * @return
     */
    default PageOrList<UserBindVO> query(UserBindDomain userBindDomain) {
        QueryWrapper queryWrapper = this.baseQuery(userBindDomain.getJoinTables());

        return this.page(userBindDomain, queryWrapper, UserBindVO.class);
    }
}

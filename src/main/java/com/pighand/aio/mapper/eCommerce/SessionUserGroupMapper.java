package com.pighand.aio.mapper.eCommerce;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.eCommerce.SessionUserGroupDomain;
import com.pighand.aio.domain.eCommerce.table.SessionUserGroupTableDef;
import com.pighand.aio.vo.eCommerce.SessionUserGroupVO;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.framework.spring.page.PageOrList;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 电商 - 场次 - 用户分组
 *
 * @author wangshuli
 * @createDate 2023-12-05 16:13:27
 */
@Mapper
public interface SessionUserGroupMapper extends BaseMapper<SessionUserGroupDomain> {

    SessionUserGroupTableDef SESSION_USER_GROUP = SessionUserGroupTableDef.SESSION_USER_GROUP;

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

        return queryWrapper;
    }

    /**
     * 查询详情
     *
     * @param id         主键
     * @param joinTables 关联表
     * @return
     */
    default SessionUserGroupVO find(Long id, List<String> joinTables) {
        QueryWrapper queryWrapper = this.baseQuery(joinTables).where(SESSION_USER_GROUP.ID.eq(id));

        return this.selectOneByQueryAs(queryWrapper, SessionUserGroupVO.class);
    }

    /**
     * 分页或列表
     *
     * @param sessionUserGroupDomain
     * @return
     */
    default PageOrList<SessionUserGroupVO> query(SessionUserGroupDomain sessionUserGroupDomain) {
        QueryWrapper queryWrapper = this.baseQuery(sessionUserGroupDomain.getJoinTables());

        return this.page(sessionUserGroupDomain, queryWrapper, SessionUserGroupVO.class);
    }
}

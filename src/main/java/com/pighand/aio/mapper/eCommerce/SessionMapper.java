package com.pighand.aio.mapper.eCommerce;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.eCommerce.SessionDomain;
import com.pighand.aio.vo.eCommerce.SessionVO;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.framework.spring.page.PageOrList;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

import static com.pighand.aio.domain.eCommerce.table.SessionTableDef.SESSION;
import static com.pighand.aio.domain.eCommerce.table.SessionTemplateTableDef.SESSION_TEMPLATE;

/**
 * 电商 - 场次
 *
 * @author wangshuli
 * @createDate 2023-12-05 16:13:27
 */
@Mapper
public interface SessionMapper extends BaseMapper<SessionDomain> {

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

        if (joinTables.contains(SESSION_TEMPLATE.getTableName())) {
            queryWrapper.leftJoin(SESSION_TEMPLATE).on(SESSION_TEMPLATE.ID.eq(SESSION.SESSION_TEMPLATE_ID));
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
    default SessionVO find(Long id, List<String> joinTables) {
        QueryWrapper queryWrapper = this.baseQuery(joinTables).where(SESSION.ID.eq(id));

        return this.selectOneByQueryAs(queryWrapper, SessionVO.class);
    }

    /**
     * 分页或列表
     *
     * @param sessionDomain
     * @return
     */
    default PageOrList<SessionVO> query(SessionDomain sessionDomain) {
        QueryWrapper queryWrapper = this.baseQuery(sessionDomain.getJoinTables());

        return this.page(sessionDomain, queryWrapper, SessionVO.class);
    }
}

package com.pighand.aio.mapper.ECommerce;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.SessionTemplateDomain;
import com.pighand.aio.vo.ECommerce.SessionTemplateVO;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.framework.spring.page.PageOrList;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

import static com.pighand.aio.domain.ECommerce.table.SessionTemplateTableDef.SESSION_TEMPLATE;

/**
 * 电商 - 场次模板。根据模板生成场次
 *
 * @author wangshuli
 * @createDate 2023-12-05 16:13:27
 */
@Mapper
public interface SessionTemplateMapper extends BaseMapper<SessionTemplateDomain> {

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
    default SessionTemplateVO find(Long id, List<String> joinTables) {
        QueryWrapper queryWrapper = this.baseQuery(joinTables).where(SESSION_TEMPLATE.ID.eq(id));

        return this.selectOneByQueryAs(queryWrapper, SessionTemplateVO.class);
    }

    /**
     * 分页或列表
     *
     * @param sessionTemplateDomain
     * @return
     */
    default PageOrList<SessionTemplateVO> query(SessionTemplateDomain sessionTemplateDomain) {
        QueryWrapper queryWrapper = this.baseQuery(sessionTemplateDomain.getJoinTables());

        return this.page(sessionTemplateDomain, queryWrapper, SessionTemplateVO.class);
    }
}

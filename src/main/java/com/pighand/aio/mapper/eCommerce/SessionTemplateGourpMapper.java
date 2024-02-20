package com.pighand.aio.mapper.eCommerce;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.eCommerce.SessionTemplateGourpDomain;
import com.pighand.aio.vo.eCommerce.SessionTemplateGourpVO;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.framework.spring.page.PageOrList;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

import static com.pighand.aio.domain.eCommerce.table.SessionTemplateGourpTableDef.SESSION_TEMPLATE_GOURP;

/**
 * 电商 - 场次模板 - 分组
 *
 * @author wangshuli
 * @createDate 2023-12-05 16:13:27
 */
@Mapper
public interface SessionTemplateGourpMapper extends BaseMapper<SessionTemplateGourpDomain> {

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
    default SessionTemplateGourpVO find(Long id, List<String> joinTables) {
        QueryWrapper queryWrapper = this.baseQuery(joinTables).where(SESSION_TEMPLATE_GOURP.ID.eq(id));

        return this.selectOneByQueryAs(queryWrapper, SessionTemplateGourpVO.class);
    }

    /**
     * 分页或列表
     *
     * @param sessionTemplateGourpDomain
     * @return
     */
    default PageOrList<SessionTemplateGourpVO> query(SessionTemplateGourpDomain sessionTemplateGourpDomain) {
        QueryWrapper queryWrapper = this.baseQuery(sessionTemplateGourpDomain.getJoinTables());

        return this.page(sessionTemplateGourpDomain, queryWrapper, SessionTemplateGourpVO.class);
    }
}

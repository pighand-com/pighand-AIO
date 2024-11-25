package com.pighand.aio.service.ECommerce.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.SessionTemplateCycleDomain;
import com.pighand.aio.mapper.ECommerce.SessionTemplateCycleMapper;
import com.pighand.aio.service.ECommerce.SessionTemplateCycleService;
import com.pighand.aio.vo.ECommerce.SessionTemplateCycleVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import org.springframework.stereotype.Service;

import static com.pighand.aio.domain.ECommerce.table.SessionTemplateCycleTableDef.SESSION_TEMPLATE_CYCLE;

/**
 * 电商 - 场次模板 - 按周期
 *
 * @author wangshuli
 * @createDate 2024-05-23 15:01:58
 */
@Service
public class SessionTemplateCycleServiceImpl
    extends BaseServiceImpl<SessionTemplateCycleMapper, SessionTemplateCycleDomain>
    implements SessionTemplateCycleService {

    /**
     * 创建
     *
     * @param sessionTemplateCycleVO
     * @return
     */
    @Override
    public SessionTemplateCycleVO create(SessionTemplateCycleVO sessionTemplateCycleVO) {
        super.mapper.insert(sessionTemplateCycleVO);

        return sessionTemplateCycleVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public SessionTemplateCycleDomain find(Long id) {
        return super.mapper.find(id, null);
    }

    /**
     * 分页或列表
     *
     * @param sessionTemplateCycleVO
     * @return PageOrList<SessionTemplateCycleVO>
     */
    @Override
    public PageOrList<SessionTemplateCycleVO> query(SessionTemplateCycleVO sessionTemplateCycleVO) {

        QueryWrapper queryWrapper = QueryWrapper.create()
            // equal
            .and(SESSION_TEMPLATE_CYCLE.SESSION_TEMPLATE_ID.eq(sessionTemplateCycleVO.getSessionTemplateId()))
            .and(SESSION_TEMPLATE_CYCLE.CYCLE_TYPE.eq(sessionTemplateCycleVO.getCycleType()))
            .and(SESSION_TEMPLATE_CYCLE.WEEK.eq(sessionTemplateCycleVO.getWeek()))
            .and(SESSION_TEMPLATE_CYCLE.DAY.eq(sessionTemplateCycleVO.getDay()))
            .and(SESSION_TEMPLATE_CYCLE.BEGIN_TIME.eq(sessionTemplateCycleVO.getBeginTime()))
            .and(SESSION_TEMPLATE_CYCLE.END_TIME.eq(sessionTemplateCycleVO.getEndTime()));

        return super.mapper.query(sessionTemplateCycleVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param sessionTemplateCycleVO
     */
    @Override
    public void update(SessionTemplateCycleVO sessionTemplateCycleVO) {
        super.mapper.update(sessionTemplateCycleVO);
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        super.mapper.deleteById(id);
    }
}

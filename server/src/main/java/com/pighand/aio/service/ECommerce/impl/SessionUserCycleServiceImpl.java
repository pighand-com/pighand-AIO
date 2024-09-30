package com.pighand.aio.service.ECommerce.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.SessionUserCycleDomain;
import com.pighand.aio.mapper.ECommerce.SessionUserCycleMapper;
import com.pighand.aio.service.ECommerce.SessionUserCycleService;
import com.pighand.aio.vo.ECommerce.SessionUserCycleVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import org.springframework.stereotype.Service;

import static com.pighand.aio.domain.ECommerce.table.SessionTableDef.SESSION;
import static com.pighand.aio.domain.ECommerce.table.SessionUserCycleTableDef.SESSION_USER_CYCLE;
import static com.pighand.aio.domain.base.table.UserTableDef.USER;

/**
 * 电商 - 场次 - 用户周期
 *
 * @author wangshuli
 * @createDate 2024-05-23 15:01:58
 */
@Service
public class SessionUserCycleServiceImpl extends BaseServiceImpl<SessionUserCycleMapper, SessionUserCycleDomain>
    implements SessionUserCycleService {

    /**
     * 创建
     *
     * @param sessionUserCycleVO
     * @return
     */
    @Override
    public SessionUserCycleVO create(SessionUserCycleVO sessionUserCycleVO) {
        super.mapper.insert(sessionUserCycleVO);

        return sessionUserCycleVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public SessionUserCycleDomain find(Long id) {
        return super.mapper.find(id, SESSION.getTableName(), USER.getTableName());
    }

    /**
     * 分页或列表
     *
     * @param sessionUserCycleVO
     * @return PageOrList<SessionUserCycleVO>
     */
    @Override
    public PageOrList<SessionUserCycleVO> query(SessionUserCycleVO sessionUserCycleVO) {
        sessionUserCycleVO.setJoinTables(SESSION.getTableName(), USER.getTableName());

        QueryWrapper queryWrapper = new QueryWrapper();

        // equal
        queryWrapper.and(SESSION_USER_CYCLE.SESSION_ID.eq(sessionUserCycleVO.getSessionId(), VerifyUtils::isNotEmpty));
        queryWrapper.and(SESSION_USER_CYCLE.SESSION_TEMPLATE_CYCLE_ID.eq(sessionUserCycleVO.getSessionTemplateCycleId(),
            VerifyUtils::isNotEmpty));
        queryWrapper.and(SESSION_USER_CYCLE.ORDER_ID.eq(sessionUserCycleVO.getOrderId(), VerifyUtils::isNotEmpty));
        queryWrapper.and(SESSION_USER_CYCLE.USER_ID.eq(sessionUserCycleVO.getUserId(), VerifyUtils::isNotEmpty));

        return super.mapper.query(sessionUserCycleVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param sessionUserCycleVO
     */
    @Override
    public void update(SessionUserCycleVO sessionUserCycleVO) {
        super.mapper.update(sessionUserCycleVO);
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

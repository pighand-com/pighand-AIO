package com.pighand.aio.service.ECommerce.impl;

import com.mybatisflex.core.query.QueryChain;
import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.domain.ECommerce.SessionDomain;
import com.pighand.aio.domain.ECommerce.SessionTemplateGourpDomain;
import com.pighand.aio.domain.ECommerce.SessionUserGroupDomain;
import com.pighand.aio.mapper.ECommerce.SessionMapper;
import com.pighand.aio.mapper.ECommerce.WalletMapper;
import com.pighand.aio.service.ECommerce.SessionService;
import com.pighand.aio.service.ECommerce.SessionTemplateGourpService;
import com.pighand.aio.service.ECommerce.SessionUserGroupService;
import com.pighand.aio.vo.ECommerce.SessionTemplateGourpVO;
import com.pighand.aio.vo.ECommerce.SessionUserGroupVO;
import com.pighand.aio.vo.ECommerce.SessionVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.exception.ThrowPrompt;
import com.pighand.framework.spring.page.PageOrList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.mybatisflex.core.query.QueryMethods.sum;
import static com.pighand.aio.domain.ECommerce.table.SessionTableDef.SESSION;
import static com.pighand.aio.domain.ECommerce.table.SessionTemplateGourpTableDef.SESSION_TEMPLATE_GOURP;
import static com.pighand.aio.domain.ECommerce.table.SessionTemplateTableDef.SESSION_TEMPLATE;
import static com.pighand.aio.domain.ECommerce.table.SessionUserGroupTableDef.SESSION_USER_GROUP;
import static com.pighand.aio.domain.ECommerce.table.WalletBillTableDef.WALLET_BILL;
import static com.pighand.aio.domain.base.table.UserExtensionTableDef.USER_EXTENSION;
import static com.pighand.aio.domain.base.table.UserTableDef.USER;

/**
 * 电商 - 场次
 *
 * @author wangshuli
 * @createDate 2023-12-05 16:13:27
 */
@Service
public class SessionServiceImpl extends BaseServiceImpl<SessionMapper, SessionDomain> implements SessionService {

    @Autowired
    private SessionTemplateGourpService sessionTemplateGourpService;
    @Autowired
    private SessionUserGroupService sessionUserGroupService;
    @Autowired
    private WalletMapper walletMapper;

    /**
     * 创建
     *
     * @param ProjectId 项目id，如果为null，则使用登录用户项目id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public SessionVO create(Long ProjectId) {
        Date now = new Date();

        // 最后有效场次
        SessionDomain lastValidSession = this.queryChain().orderBy(SESSION.ID, false).limit(1).one();

        // 最后场次未结束，则结束
        if (lastValidSession.getEndTime().after(now)) {
            lastValidSession.setEndTime(now);
            this.updateById(lastValidSession);
        }

        // 当天0点
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date beginTime = calendar.getTime();

        // 第二天8点
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        Date endTime = calendar.getTime();

        SessionVO sessionVO = new SessionVO();
        sessionVO.setProjectId(ProjectId == null ? Context.getProjectId() : ProjectId);
        sessionVO.setStoreId(1L);
        sessionVO.setSessionTemplateId(1L);
        sessionVO.setBeginTime(beginTime);
        sessionVO.setEndTime(endTime);
        sessionVO.setCreatedAt(now);
        super.mapper.insert(sessionVO);

        // 冻结剩余金额
        walletMapper.freeze();

        return sessionVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public SessionVO find(Long id) {
        SessionVO session = this.queryChain().orderBy(SESSION.ID, false).limit(1).oneAs(SessionVO.class);

        List<SessionTemplateGourpVO> groups = sessionTemplateGourpService.queryChain()
            .where(SESSION_TEMPLATE_GOURP.SESSION_TEMPLATE_ID.eq(session.getSessionTemplateId()))
            .listAs(SessionTemplateGourpVO.class);

        List<Long> groupIds = groups.stream().map(SessionTemplateGourpDomain::getId).toList();

        List<SessionUserGroupVO> users = sessionUserGroupService.queryChain()
            .select(SESSION_USER_GROUP.SESSION_TEMPLATE_GROUP_ID, USER.PHONE, USER_EXTENSION.PROFILE,
                sum(WALLET_BILL.AMOUNT).as("amount"))
            // left user
            .leftJoin(USER).on(SESSION_USER_GROUP.USER_ID.eq(USER.ID)).leftJoin(USER_EXTENSION)
            .on(USER_EXTENSION.ID.eq(SESSION_USER_GROUP.USER_ID))
            // left wall_bill
            .leftJoin(WALLET_BILL).on(WALLET_BILL.USER_ID.eq(USER.ID).and(
                WALLET_BILL.TYPE.eq(10).and(WALLET_BILL.SESSION_USER_ID.eq(SESSION_USER_GROUP.ID))
                    .and(SESSION_USER_GROUP.SESSION_ID.eq(session.getId()))))

            .where(SESSION_USER_GROUP.SESSION_ID.eq(session.getId()))
            .groupBy(SESSION_USER_GROUP.SESSION_TEMPLATE_GROUP_ID, USER.PHONE, USER_EXTENSION.PROFILE)
            .orderBy("amount", false).listAs(SessionUserGroupVO.class);

        Map<Long, SessionTemplateGourpVO> groupMap =
            groups.stream().collect(Collectors.toMap(SessionTemplateGourpVO::getId, item -> {
                item.setUsers(new ArrayList<>());
                return item;
            }));

        users.forEach(item -> {
            groupMap.get(item.getSessionTemplateGroupId()).getUsers().add(item);
        });

        session.setGroups(groupMap.values().stream().toList());

        return session;
    }

    /**
     * 分页或列表
     *
     * @param sessionVO
     * @return PageOrList<SessionVO>
     */
    @Override
    public PageOrList<SessionVO> query(SessionVO sessionVO) {
        return super.mapper.query(sessionVO, null);
    }

    /**
     * 修改
     *
     * @param sessionVO
     */
    @Override
    public void update(SessionVO sessionVO) {
        super.mapper.update(sessionVO);
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

    /**
     * 加入场次分组
     * <p>如果已经加入，则返回已加入的场次分组</p>
     * <p>如果没有加入，则加入场次分组</p>
     *
     * @param sessionId
     * @param sessionGroupId
     * @return
     */
    @Override
    public SessionUserGroupVO join(Long sessionId, Long sessionGroupId) {
        if (sessionId == null) {
            SessionDomain nowSession = this.findCurrentSession();

            if (nowSession == null) {
                throw new ThrowPrompt("当前无进行中的场次");
            }

            sessionId = nowSession.getId();
        }

        SessionVO session = super.mapper.find(sessionId, SESSION_TEMPLATE.getTableName());

        if (session == null) {
            throw new ThrowPrompt("场次不存在");
        }

        if (session.getSessionTemplate() == null) {
            throw new ThrowPrompt("场次配置错误");
        }

        // 返回已加入的场次分组
        SessionUserGroupVO sessionUserGroup = sessionUserGroupService.queryChain().leftJoin(SESSION).as("session")
            .on(SESSION_USER_GROUP.SESSION_ID.eq(SESSION.ID)).leftJoin(SESSION_TEMPLATE_GOURP).as("group")
            .on(SESSION_TEMPLATE_GOURP.ID.eq(
                SESSION_USER_GROUP.SESSION_TEMPLATE_GROUP_ID.eq(SESSION_TEMPLATE_GOURP.ID)))
            .where(SESSION_USER_GROUP.SESSION_ID.eq(sessionId))
            .and(SESSION_USER_GROUP.USER_ID.eq(Context.getLoginUser().getId())).oneAs(SessionUserGroupVO.class);

        if (sessionUserGroup != null) {
            SessionTemplateGourpDomain nowGroup =
                sessionTemplateGourpService.getById(sessionUserGroup.getSessionTemplateGroupId());
            sessionUserGroup.setSessionId(sessionId);
            sessionUserGroup.setGroup(nowGroup);
            return sessionUserGroup;
        }

        if (sessionGroupId == null) {
            return null;
        }

        SessionTemplateGourpDomain sessionTemplateGourp = sessionTemplateGourpService.getById(sessionGroupId);

        if (sessionTemplateGourp == null) {
            throw new ThrowPrompt("场次分组不存在");
        }

        if (!sessionTemplateGourp.getSessionTemplateId().equals(session.getSessionTemplateId())) {
            throw new ThrowPrompt("场次分组与场次不匹配");
        }

        SessionUserGroupVO sessionUserGroupVO = new SessionUserGroupVO();
        sessionUserGroupVO.setSessionId(sessionId);
        sessionUserGroupVO.setSessionTemplateGroupId(sessionGroupId);
        sessionUserGroupVO.setUserId(Context.getLoginUser().getId());
        sessionUserGroupVO.setCreatedAt(new Date());
        sessionUserGroupService.create(sessionUserGroupVO);

        sessionUserGroupVO.setSession(session);
        sessionUserGroupVO.setGroup(sessionTemplateGourp);
        return sessionUserGroupVO;
    }

    /**
     * 查询当前有效场次
     *
     * @return
     */
    @Override
    public SessionDomain findCurrentSession() {
        // 当前日期
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = LocalDateTime.now().format(formatter);

        return this.queryChain().where(SESSION.BEGIN_TIME.le(now)).and(SESSION.END_TIME.ge(now))
            .orderBy(SESSION.ID.desc()).limit(1).one();
    }

    /**
     * 查询当前加入的有效场次
     *
     * @param userId
     * @param isReturnSessionInfo 是否返回场次信息。默认：false
     * @return null 无进行中的场次
     */
    @Override
    public SessionUserGroupVO findCurrentJoined(Long userId, boolean isReturnSessionInfo) {
        // 当前日期
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = LocalDateTime.now().format(formatter);

        QueryChain<SessionUserGroupDomain> query = sessionUserGroupService.queryChain();
        query.innerJoin(SESSION).as("session").on(SESSION_USER_GROUP.SESSION_ID.eq(SESSION.ID));

        if (isReturnSessionInfo) {
            query.leftJoin(SESSION_TEMPLATE_GOURP).as("group").on(SESSION_TEMPLATE_GOURP.ID.eq(
                SESSION_USER_GROUP.SESSION_TEMPLATE_GROUP_ID.eq(SESSION_TEMPLATE_GOURP.ID)));
        }

        query.where(SESSION_USER_GROUP.USER_ID.eq(userId)).and(SESSION.BEGIN_TIME.le(now)).and(SESSION.END_TIME.ge(now))
            .orderBy(SESSION_USER_GROUP.ID.desc()).limit(1);

        return query.oneAs(SessionUserGroupVO.class);
    }
}

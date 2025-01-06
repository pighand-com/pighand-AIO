package com.pighand.aio.service.ECommerce.impl;

import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryMethods;
import com.pighand.aio.common.enums.RoleEnum;
import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.domain.ECommerce.WalletBillDomain;
import com.pighand.aio.domain.base.UserDomain;
import com.pighand.aio.mapper.ECommerce.WalletBillMapper;
import com.pighand.aio.service.ECommerce.SessionService;
import com.pighand.aio.service.ECommerce.SessionUserGroupService;
import com.pighand.aio.service.ECommerce.WalletBillService;
import com.pighand.aio.service.base.UserService;
import com.pighand.aio.vo.ECommerce.SessionUserGroupVO;
import com.pighand.aio.vo.ECommerce.WalletBillTop;
import com.pighand.aio.vo.ECommerce.WalletBillVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.page.PageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.pighand.aio.domain.ECommerce.table.SessionTemplateGourpTableDef.SESSION_TEMPLATE_GOURP;
import static com.pighand.aio.domain.ECommerce.table.SessionUserGroupTableDef.SESSION_USER_GROUP;
import static com.pighand.aio.domain.ECommerce.table.WalletBillTableDef.WALLET_BILL;
import static com.pighand.aio.domain.base.table.UserTableDef.USER;

/**
 * 电商 - 钱包账单
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Service
public class WalletBillServiceImpl extends BaseServiceImpl<WalletBillMapper, WalletBillDomain>
    implements WalletBillService {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionUserGroupService sessionUserGroupService;

    @Autowired
    private UserService userService;

    /**
     * 分页或列表
     *
     * @param walletBillVO
     * @return PageOrList<WalletBillVO>
     */
    @Override
    public PageOrList<WalletBillVO> query(WalletBillVO walletBillVO) {
        walletBillVO.setPageType(PageType.LIST);

        return super.mapper.query(walletBillVO, null);
    }

    /**
     * 按场次分组查询top
     *
     * @param walletBillVO
     * @return
     */
    @Override
    public List<WalletBillTop> queryBillTopBySessionGroup(WalletBillVO walletBillVO) {
        Long applicationId = Context.applicationId();
        Integer top = walletBillVO.getTop();
        Long topBySessionId = walletBillVO.getTopBySessionId();
        String topByDateType = walletBillVO.getTopByDateType();

        List<WalletBillTop> result = new ArrayList<>(0);

        if (topBySessionId != null) {
            // 按场次统计
            result = sessionUserGroupService.queryChain()
                // select from session_user_group
                .select(QueryMethods.sum(WALLET_BILL.AMOUNT).as("amount"),
                    QueryMethods.count(QueryMethods.distinct(SESSION_USER_GROUP.ID)).as("userCount"),
                    SESSION_TEMPLATE_GOURP.ID.as("sessionGroupId"), SESSION_TEMPLATE_GOURP.NAME.as("sessionGroupName"))

                // left join wallet_bill
                .leftJoin(WALLET_BILL).on(WALLET_BILL.USER_ID.eq(SESSION_USER_GROUP.USER_ID)
                    .and(SESSION_USER_GROUP.SESSION_ID.eq(WALLET_BILL.SESSION_ID)))

                // left join user
                .leftJoin(USER)
                .on(USER.ID.eq(SESSION_USER_GROUP.USER_ID).and(SESSION_USER_GROUP.SESSION_ID.eq(WALLET_BILL.SESSION_ID))
                    .and(USER.ROLE_ID.eq(RoleEnum.USER.value)))

                // inner join session_template_group
                .innerJoin(SESSION_TEMPLATE_GOURP)
                .on(SESSION_USER_GROUP.SESSION_TEMPLATE_GROUP_ID.eq(SESSION_TEMPLATE_GOURP.ID)
                    .and(SESSION_TEMPLATE_GOURP.TYPE.ne(99)))

                // where
                .where(SESSION_USER_GROUP.SESSION_ID.eq(topBySessionId)).groupBy("sessionGroupId", "sessionGroupName")
                .listAs(WalletBillTop.class);
        } else if (topByDateType != null) {
            // 按日期统计
            LocalDate currentDate = LocalDate.now();

            QueryChain queryChain = this.queryChain();
            queryChain.select(WALLET_BILL.USER_ID.as("userId"), QueryMethods.sum(WALLET_BILL.AMOUNT).as("amount"))
                .innerJoin(USER).on(USER.ID.eq(WALLET_BILL.USER_ID).and(USER.ROLE_ID.eq(RoleEnum.USER.value)));

            queryChain.where(WALLET_BILL.APPLICATION_ID.eq(applicationId));

            if (topByDateType.equals("week")) {
                LocalDate startOfWeek = currentDate.with(DayOfWeek.MONDAY);
                LocalDate endOfWeek = currentDate.with(DayOfWeek.SUNDAY);

                queryChain.and(WALLET_BILL.CREATED_AT.between(
                    DateTimeFormatter.ofPattern("YYYY-MM-dd 00:00:00").format(startOfWeek),
                    DateTimeFormatter.ofPattern("YYYY-MM-dd 23:59:59").format(endOfWeek)));
            } else if (topByDateType.equals("month")) {
                LocalDate startOfMonth = currentDate.withDayOfMonth(1);
                LocalDate endOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth());

                queryChain.and(WALLET_BILL.CREATED_AT.between(
                    DateTimeFormatter.ofPattern("YYYY-MM-dd 00:00:00").format(startOfMonth),
                    DateTimeFormatter.ofPattern("YYYY-MM-dd 23:59:59").format(endOfMonth)));
            }

            queryChain.groupBy(WALLET_BILL.USER_ID).orderBy("amount", false).limit(top);

            result = queryChain.listAs(WalletBillTop.class);

            if (result.size() > 0) {
                List<Long> userIds = result.stream().map(item -> item.getUserId()).toList();

                List<UserDomain> users =
                    userService.queryChain().select(USER.ID, USER.PHONE).where(USER.ID.in(userIds)).list();

                Map<Long, String> userMap =
                    users.stream().collect(Collectors.toMap(UserDomain::getId, UserDomain::getPhone));

                result.forEach(item -> {
                    item.setUserName(userMap.get(item.getUserId()));
                });
            }
        }

        return result;
    }

    /**
     * 统计入账总金额
     *
     * @param totalType null:所有 20:当前场次
     * @return
     */
    @Override
    public BigDecimal queryTotalAmount(Integer totalType) {
        Long userId = Context.loginUser().getId();

        QueryChain<WalletBillDomain> query = this.queryChain();
        query.select(QueryMethods.sum(WALLET_BILL.AMOUNT).as("amount"));
        query.where(WALLET_BILL.USER_ID.eq(userId));

        // 当前场次
        if (totalType.equals(20)) {
            SessionUserGroupVO sessionUser = sessionService.findCurrentJoined(userId, false);
            if (sessionUser == null) {
                return BigDecimal.ZERO;
            }

            query.and(WALLET_BILL.SESSION_ID.eq(sessionUser.getSessionId())).and(WALLET_BILL.TYPE.in(10, 11, 31));
        }

        WalletBillDomain bill = query.one();

        return Optional.ofNullable(bill).map(item -> item.getAmount()).orElse(BigDecimal.ZERO);
    }
}

package com.pighand.aio.service.ECommerce;

import com.pighand.aio.common.enums.RoleEnum;
import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.domain.ECommerce.WalletDomain;
import com.pighand.aio.domain.base.UserDomain;
import com.pighand.aio.mapper.ECommerce.WalletMapper;
import com.pighand.aio.service.ECommerce.SessionService;
import com.pighand.aio.service.ECommerce.WalletBillService;
import com.pighand.aio.service.ECommerce.WalletTransferService;
import com.pighand.aio.service.base.UserService;
import com.pighand.aio.vo.ECommerce.*;
import com.pighand.aio.vo.base.LoginUser;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.exception.ThrowPrompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.mybatisflex.core.query.QueryMethods.sum;
import static com.pighand.aio.domain.ECommerce.table.WalletTableDef.WALLET;
import static com.pighand.aio.domain.base.table.UserExtensionTableDef.USER_EXTENSION;
import static com.pighand.aio.domain.base.table.UserTableDef.USER;

/**
 * 电商 - 钱包
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Service
public class WalletService extends BaseServiceImpl<WalletMapper, WalletDomain>  {

    @Autowired
    private UserService userService;

    @Autowired
    private WalletTransferService walletTransferService;

    @Autowired
    private WalletBillService walletBillService;

    @Autowired
    private SessionService sessionService;

    /**
     * 转账
     *
     * @param walletTransferVO
     */
    @Transactional(rollbackFor = Exception.class)
    public void transfer(WalletTransferVO walletTransferVO) {
        Long applicationId = Context.applicationId();
        LoginUser loginUser = Context.loginUser();

        UserDomain toUser = userService.getById(walletTransferVO.getToUserId());
        if (toUser == null || toUser.getApplicationId() != applicationId) {
            throw new ThrowPrompt("转账用户不存在");
        }

        // 未登录，小程序端转账；否则，系统转账
        boolean isFromSystem = loginUser == null;
        // 来源id，1.用户传过来 - 扫的别人付款码。2.未传用户，使用登录人 - 扫的比人收款码，或者小程序端转账
        Long fromUserId =
            isFromSystem ? null : Optional.ofNullable(walletTransferVO.getFromUserId()).orElse(loginUser.getId());

        // TODO 测试账号
        if (fromUserId.equals(21616834167000181L) || fromUserId.equals(21624104966000138L) || fromUserId.equals(
            21624122354000176L)) {
            isFromSystem = true;
        }

        if (toUser.getId().equals(fromUserId)) {
            //            throw new ThrowPrompt("不能给自己转账");
            isFromSystem = true;
        }

        if (!isFromSystem) {
            boolean isUpdate =
                this.updateChain().setRaw(WALLET.TOKENS, WALLET.TOKENS.subtract(walletTransferVO.getAccount()))
                    .where(WALLET.USER_ID.eq(fromUserId))
                    .and(WALLET.TOKENS.subtract(walletTransferVO.getAccount()).ge(0)).update();

            if (!isUpdate) {
                throw new ThrowPrompt("余额不足");
            }
        }

        WalletDomain toUserWallet = this.queryChain().where(WALLET.USER_ID.eq(toUser.getId())).one();
        if (toUserWallet == null) {
            toUserWallet = new WalletDomain();
            toUserWallet.setUserId(toUser.getId());
            toUserWallet.setTokens(walletTransferVO.getAccount());
            toUserWallet.setFreezeTokens(BigDecimal.ZERO);
            super.mapper.insert(toUserWallet);
        } else {
            boolean isUpdate =
                this.updateChain().setRaw(WALLET.TOKENS, WALLET.TOKENS.add(walletTransferVO.getAccount()))
                    .where(WALLET.APPLICATION_ID.eq(applicationId)).and(WALLET.USER_ID.eq(toUser.getId())).update();

            if (!isUpdate) {
                throw new ThrowPrompt("转账失败");
            }
        }

        if (loginUser != null) {
            walletTransferVO.setFromUserId(loginUser.getId());
        }
        walletTransferService.create(walletTransferVO);

        // 查询所在场次
        SessionUserGroupVO currentSession = sessionService.findCurrentJoined(toUser.getId(), false);
        Long currentSessionId = currentSession == null ? null : currentSession.getSessionId();

        Date now = new Date();

        WalletBillVO fromBill = new WalletBillVO();
        fromBill.setUserId(fromUserId);
        fromBill.setType(isFromSystem ? 20 : 32);
        fromBill.setWalletType(20);
        fromBill.setAmount(walletTransferVO.getAccount().negate());
        fromBill.setWalletTransferId(walletTransferVO.getId());
        fromBill.setSessionId(currentSessionId);
        fromBill.setCreatedAt(now);

        WalletBillVO toBill = new WalletBillVO();
        toBill.setUserId(toUser.getId());
        toBill.setType(isFromSystem ? 10 : 31);
        toBill.setWalletType(20);
        toBill.setAmount(walletTransferVO.getAccount());
        toBill.setWalletTransferId(walletTransferVO.getId());
        toBill.setSessionId(currentSessionId);
        toBill.setCreatedAt(now);

        walletBillService.getMapper().insertBatch(List.of(fromBill, toBill));
    }

    /**
     * 钱包详情
     *
     * @return
     */
    public WalletVO find() {
        LoginUser loginUser = Context.loginUser();
        WalletVO walletVO = this.queryChain().where(WALLET.USER_ID.eq(loginUser.getId())).oneAs(WalletVO.class);

        BigDecimal freezeTokens = Optional.ofNullable(walletVO).map(WalletVO::getFreezeTokens).orElse(BigDecimal.ZERO);
        if (!freezeTokens.equals(BigDecimal.ZERO)) {
            SessionUserGroupVO currentSession = sessionService.findCurrentJoined(loginUser.getId(), false);

            if (currentSession == null) {
                this.mapper.unfreeze(loginUser.getId());
            }
        }

        return walletVO;
    }

    /**
     * 排行榜
     *
     * @param walletTopVO
     * @return
     */
    public List<WalletTopVO> top(WalletTopVO walletTopVO) {
        return this.queryChain()
            .select(sum(WALLET.TOKENS.add(WALLET.FREEZE_TOKENS)).as("tokens"), USER_EXTENSION.NAME, USER.PHONE)
            .innerJoin(USER).on(USER.ID.eq(WALLET.USER_ID).and(USER.ROLE_ID.eq(RoleEnum.USER)))
            .innerJoin(USER_EXTENSION).on(USER_EXTENSION.ID.eq(WALLET.USER_ID))
            .where(WALLET.TOKENS.add(WALLET.FREEZE_TOKENS).gt(0)).groupBy(USER_EXTENSION.NAME, USER.PHONE)
            .orderBy("tokens desc").limit(walletTopVO.getTop()).listAs(WalletTopVO.class);
    }
}

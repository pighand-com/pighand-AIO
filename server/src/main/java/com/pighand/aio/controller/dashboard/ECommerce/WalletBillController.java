package com.pighand.aio.controller.dashboard.ECommerce;

import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.common.interfaces.Authorization;
import com.pighand.aio.service.ECommerce.WalletBillService;
import com.pighand.aio.vo.ECommerce.WalletBillTop;
import com.pighand.aio.vo.ECommerce.WalletBillVO;
import com.pighand.framework.spring.api.annotation.Get;
import com.pighand.framework.spring.api.annotation.RestController;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 电商 - 钱包账单
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@RequiredArgsConstructor
@RestController(path = "wallet/bill", docName = "电商 - 钱包账单")
public class WalletBillController extends BaseController<WalletBillService> {

    /**
     * @param walletBillVO
     */
    @Authorization
    @Get(docSummary = "我的账单", fieldGroup = "walletBillQuery")
    public Result<PageOrList<WalletBillVO>> query(WalletBillVO walletBillVO) {
        walletBillVO.setUserId(Context.loginUser().getId());
        PageOrList<WalletBillVO> result = super.service.query(walletBillVO);

        return new Result(result);
    }

    /**
     * @param walletBillVO
     */
    @Authorization
    @Get(value = "top", docSummary = "按场次分组查询top")
    public Result<List<WalletBillTop>> queryBillTopBySessionGroup(WalletBillVO walletBillVO) {
        List<WalletBillTop> result = super.service.queryBillTopBySessionGroup(walletBillVO);

        return new Result(result);
    }

    @Authorization
    @Get(value = "total", docSummary = "查询当前场次总金额")
    public Result<BigDecimal> queryTotalAmount(WalletBillVO walletBillVO) {
        BigDecimal result = super.service.queryTotalAmount(walletBillVO.getTotalType());

        return new Result(result);
    }
}

package com.pighand.aio.controller.ECommerce;

import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.common.interfaces.Authorization;
import com.pighand.aio.service.ECommerce.WalletService;
import com.pighand.aio.vo.ECommerce.WalletTopVO;
import com.pighand.aio.vo.ECommerce.WalletTransferVO;
import com.pighand.aio.vo.ECommerce.WalletVO;
import com.pighand.framework.spring.api.annotation.Get;
import com.pighand.framework.spring.api.annotation.Post;
import com.pighand.framework.spring.api.annotation.RestController;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;

/**
 * 电商 - 钱包
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@RequiredArgsConstructor
@RestController(path = "wallet", docName = "电商 - 钱包")
public class WalletController extends BaseController<WalletService> {

    /**
     * @param walletTransferVO
     * @return
     */
    @Authorization(false)
    @Post(docSummary = "转账", fieldGroup = "walletCreate")
    public Result create(@RequestBody WalletTransferVO walletTransferVO) {
        BigDecimal success = new BigDecimal("10");
        if (walletTransferVO.getAccount() == null || walletTransferVO.getAccount().compareTo(BigDecimal.ZERO) <= 0) {
            walletTransferVO.setAccount(success);
        }

        if (walletTransferVO.getToUserId() == null) {
            walletTransferVO.setToUserId(Context.loginUser().getId());
        }

        super.service.transfer(walletTransferVO);

        return new Result(success.toString());
    }

    @Authorization()
    @Get(docSummary = "查询钱包")
    public Result<WalletVO> find() {
        WalletVO walletVO = super.service.find();

        return new Result(walletVO);
    }

    @Get(value = "top", docSummary = "排行榜")
    public Result<List<WalletTopVO>> top(WalletTopVO walletTopVO) {
        List<WalletTopVO> result = super.service.top(walletTopVO);

        return new Result(result);
    }

}

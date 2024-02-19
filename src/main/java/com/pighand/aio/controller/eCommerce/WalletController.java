package com.pighand.aio.controller.eCommerce;

import com.pighand.aio.common.interfaces.Authorization;
import com.pighand.aio.service.eCommerce.WalletService;
import com.pighand.aio.vo.eCommerce.WalletTopVO;
import com.pighand.aio.vo.eCommerce.WalletTransferVO;
import com.pighand.aio.vo.eCommerce.WalletVO;
import com.pighand.framework.spring.api.annotation.Get;
import com.pighand.framework.spring.api.annotation.Post;
import com.pighand.framework.spring.api.annotation.RestController;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

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
        super.service.transfer(walletTransferVO);

        return new Result();
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

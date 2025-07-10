package com.pighand.aio.controller.client.distribution;

import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.common.interfaces.Authorization;
import com.pighand.aio.domain.distribution.DistributionSalespersonDomain;
import com.pighand.aio.service.distribution.DistributionSalespersonService;
import com.pighand.aio.vo.distribution.DistributionSalespersonVO;
import com.pighand.framework.spring.api.annotation.Get;
import com.pighand.framework.spring.api.annotation.RestController;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.response.Result;

/**
 * 分销 - 销售资格
 *
 * @author wangshuli
 * @createDate 2025-05-24 14:35:05
 */
@Authorization
@RestController(path = "client/distribution/salesperson", docName = "分销 - 销售资格")
public class DistributionSalespersonController extends BaseController<DistributionSalespersonService> {

    @Get(docSummary = "分销资格")
    public Result<DistributionSalespersonDomain> find() {
        Long loginUserId = Context.loginUser().getId();
        DistributionSalespersonVO distributionSalesperson = super.service.findByUserId(loginUserId);

        String id = null;
        if (distributionSalesperson != null) {
            id = distributionSalesperson.getId().toString();
        }

        return new Result(id);
    }

    @Get(path = "/qr_code", docSummary = "分销小程序二维码")
    public Result QRCode() {
        Long loginUserId = Context.loginUser().getId();
        DistributionSalespersonVO distributionSalesperson = super.service.findByUserId(loginUserId);

        if (distributionSalesperson == null) {
            return new Result("未开通分销资格");
        }

        String imageBase64 = super.service.getWechatAppletQrcode(distributionSalesperson.getId());
        return new Result(imageBase64);
    }
}

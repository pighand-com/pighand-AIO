package com.pighand.aio.controller.client.CMS;

import com.pighand.aio.service.CMS.BannerService;
import com.pighand.aio.vo.CMS.BannerVO;
import com.pighand.framework.spring.api.annotation.Get;
import com.pighand.framework.spring.api.annotation.RestController;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;

/**
 * CMS - banner
 *
 * @author wangshuli
 * @createDate 2025-06-17 13:59:52
 */
@RestController(path = "client/banner", docName = "CMS - banner")
public class BannerController extends BaseController<BannerService> {

    @Get(docSummary = "列表", fieldGroup = "cmsBannerQuery")
    public Result<PageOrList<BannerVO>> query() {
        BannerVO cmsBannerVO = new BannerVO();
        cmsBannerVO.setStatus(10);

        PageOrList<BannerVO> result = super.service.query(cmsBannerVO);

        return new Result(result);
    }

}

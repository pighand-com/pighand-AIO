package com.pighand.aio.controller.base;

import com.pighand.aio.domain.base.ApplicationPlatformPayDomain;
import com.pighand.aio.service.base.ApplicationPlatformPayService;
import com.pighand.aio.vo.base.ApplicationPlatformPayVO;
import com.pighand.framework.spring.api.annotation.*;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 项目 - 支付信息
 *
 * @author wangshuli
 * @createDate 2024-04-22 15:11:06
 */
@RestController(path = "project/platform/pay", docName = "项目 - 支付信息")
public class ApplicationPlatformPayController extends BaseController<ApplicationPlatformPayService> {

    /**
     * @param projectPlatformPayVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "projectPlatformPayCreate")
    public Result<ApplicationPlatformPayVO> create(
        @Validated({ValidationGroup.Create.class}) @RequestBody ApplicationPlatformPayVO projectPlatformPayVO) {
        projectPlatformPayVO = super.service.create(projectPlatformPayVO);

        return new Result(projectPlatformPayVO);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<ApplicationPlatformPayDomain> find(@PathVariable(name = "id") Long id) {
        ApplicationPlatformPayDomain projectPlatformPayDomain = super.service.find(id);

        return new Result(projectPlatformPayDomain);
    }

    /**
     * @param projectPlatformPayVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "projectPlatformPayQuery")
    public Result<PageOrList<ApplicationPlatformPayVO>> query(ApplicationPlatformPayVO projectPlatformPayVO) {
        PageOrList<ApplicationPlatformPayVO> result = super.service.query(projectPlatformPayVO);

        return new Result(result);
    }

    /**
     * @param projectPlatformPayVO
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "projectPlatformPayUpdate")
    public Result update(@PathVariable(name = "id") Long id,
        @RequestBody ApplicationPlatformPayVO projectPlatformPayVO) {
        projectPlatformPayVO.setId(id);

        super.service.update(projectPlatformPayVO);

        return new Result();
    }

    /**
     * @param id
     */
    @Delete(path = "{id}", docSummary = "删除")
    public Result delete(@PathVariable(name = "id") Long id) {
        super.service.delete(id);
        return new Result();
    }
}

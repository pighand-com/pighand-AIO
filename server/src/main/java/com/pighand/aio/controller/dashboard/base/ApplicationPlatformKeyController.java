package com.pighand.aio.controller.dashboard.base;

import com.pighand.aio.domain.base.ApplicationPlatformKeyDomain;
import com.pighand.aio.service.base.ApplicationPlatformKeyService;
import com.pighand.aio.vo.base.ApplicationPlatformKeyVO;
import com.pighand.framework.spring.api.annotation.*;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 三方平台key
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@RequiredArgsConstructor
@RestController(path = "dashboard/platform/key", docName = "三方平台key")
public class ApplicationPlatformKeyController extends BaseController<ApplicationPlatformKeyService> {

    /**
     * @param platformKeyVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "platformKeyCreate")
    public Result<ApplicationPlatformKeyVO> create(
        @Validated({ValidationGroup.Create.class}) @RequestBody ApplicationPlatformKeyVO platformKeyVO) {
        platformKeyVO = super.service.create(platformKeyVO);

        return new Result(platformKeyVO);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<ApplicationPlatformKeyDomain> find(@PathVariable(name = "id") Long id) {
        ApplicationPlatformKeyDomain platformKeyDomain = super.service.find(id);

        return new Result(platformKeyDomain);
    }

    /**
     * @param platformKeyVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "platformKeyQuery")
    public Result<PageOrList<ApplicationPlatformKeyVO>> query(ApplicationPlatformKeyVO platformKeyVO) {
        PageOrList<ApplicationPlatformKeyVO> result = super.service.query(platformKeyVO);

        return new Result(result);
    }

    /**
     * @param platformKeyVO
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "platformKeyUpdate")
    public Result update(@PathVariable(name = "id") Long id, @RequestBody ApplicationPlatformKeyVO platformKeyVO) {
        platformKeyVO.setId(id);
        super.service.update(platformKeyVO);

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

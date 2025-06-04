package com.pighand.aio.controller.dashboard.base;

import com.pighand.aio.domain.base.ApplicationTripartitePlatformDomain;
import com.pighand.aio.service.base.ApplicationTripartitePlatformService;
import com.pighand.aio.vo.base.ApplicationTripartitePlatformVO;
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
 * 项目三方平台配置
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@RequiredArgsConstructor
@RestController(path = "dashboard/project/tripartite_platform", docName = "项目三方平台配置")
public class ApplicationTripartitePlatformController extends BaseController<ApplicationTripartitePlatformService> {

    /**
     * @param projectTripartitePlatformVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "projectTripartitePlatformCreate")
    public Result<ApplicationTripartitePlatformVO> create(@Validated({
        ValidationGroup.Create.class}) @RequestBody ApplicationTripartitePlatformVO projectTripartitePlatformVO) {
        projectTripartitePlatformVO = super.service.create(projectTripartitePlatformVO);

        return new Result(projectTripartitePlatformVO);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<ApplicationTripartitePlatformDomain> find(@PathVariable(name = "id") Long id) {
        ApplicationTripartitePlatformDomain projectTripartitePlatformDomain = super.service.find(id);

        return new Result(projectTripartitePlatformDomain);
    }

    /**
     * @param projectTripartitePlatformVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "projectTripartitePlatformQuery")
    public Result<PageOrList<ApplicationTripartitePlatformVO>> query(
        ApplicationTripartitePlatformVO projectTripartitePlatformVO) {
        PageOrList<ApplicationTripartitePlatformVO> result = super.service.query(projectTripartitePlatformVO);

        return new Result(result);
    }

    /**
     * @param projectTripartitePlatformVO
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "projectTripartitePlatformUpdate")
    public Result update(@PathVariable(name = "id") Long id,
        @RequestBody ApplicationTripartitePlatformVO projectTripartitePlatformVO) {
        projectTripartitePlatformVO.setId(id);
        super.service.update(projectTripartitePlatformVO);

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

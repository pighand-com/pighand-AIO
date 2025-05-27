package com.pighand.aio.controller.dashboard.base;

import com.pighand.aio.domain.base.ApplicationDomain;
import com.pighand.aio.service.base.ApplicationService;
import com.pighand.aio.vo.base.ApplicationVO;
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
 * 项目
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@RequiredArgsConstructor
@RestController(path = "project", docName = "项目")
public class ApplicationController extends BaseController<ApplicationService> {

    /**
     * @param projectVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "projectCreate")
    public Result<ApplicationVO> create(
        @Validated({ValidationGroup.Create.class}) @RequestBody ApplicationVO projectVO) {
        projectVO = super.service.create(projectVO);

        return new Result(projectVO);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<ApplicationDomain> find(@PathVariable(name = "id") Long id) {
        ApplicationDomain projectDomain = super.service.find(id);

        return new Result(projectDomain);
    }

    /**
     * @param projectVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "projectQuery")
    public Result<PageOrList<ApplicationVO>> query(ApplicationVO projectVO) {
        PageOrList<ApplicationVO> result = super.service.query(projectVO);

        return new Result(result);
    }

    /**
     * @param projectVO
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "projectUpdate")
    public Result update(@PathVariable(name = "id") Long id, @RequestBody ApplicationVO projectVO) {
        projectVO.setId(id);
        super.service.update(projectVO);

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

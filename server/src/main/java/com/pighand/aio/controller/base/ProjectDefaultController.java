package com.pighand.aio.controller.base;

import com.pighand.aio.domain.base.ProjectDefaultDomain;
import com.pighand.aio.service.base.ProjectDefaultService;
import com.pighand.aio.vo.base.ProjectDefaultVO;
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
 * 项目默认设置
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@RequiredArgsConstructor
@RestController(path = "project/default", docName = "项目默认设置")
public class ProjectDefaultController extends BaseController<ProjectDefaultService> {

    /**
     * @param projectDefaultVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "projectDefaultCreate")
    public Result<ProjectDefaultVO> create(
        @Validated({ValidationGroup.Create.class}) @RequestBody ProjectDefaultVO projectDefaultVO) {
        projectDefaultVO = super.service.create(projectDefaultVO);

        return new Result(projectDefaultVO);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<ProjectDefaultDomain> find(@PathVariable(name = "id") Long id) {
        ProjectDefaultDomain projectDefaultDomain = super.service.find(id);

        return new Result(projectDefaultDomain);
    }

    /**
     * @param projectDefaultVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "projectDefaultQuery")
    public Result<PageOrList<ProjectDefaultVO>> query(ProjectDefaultVO projectDefaultVO) {
        PageOrList<ProjectDefaultVO> result = super.service.query(projectDefaultVO);

        return new Result(result);
    }

    /**
     * @param projectDefaultVO
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "projectDefaultUpdate")
    public Result update(@PathVariable(name = "id") Long id, @RequestBody ProjectDefaultVO projectDefaultVO) {
        projectDefaultVO.setId(id);
        super.service.update(projectDefaultVO);

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

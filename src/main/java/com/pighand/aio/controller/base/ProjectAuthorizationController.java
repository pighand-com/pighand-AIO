package com.pighand.aio.controller.base;

import com.pighand.aio.domain.base.ProjectAuthorizationDomain;
import com.pighand.aio.service.base.ProjectAuthorizationService;
import com.pighand.aio.vo.base.ProjectAuthorizationVO;
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
 * Authorization配置
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@RequiredArgsConstructor
@RestController(path = "project/authorization", docName = "Authorization配置")
public class ProjectAuthorizationController extends BaseController<ProjectAuthorizationService> {

    /**
     * @param projectAuthorizationVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "projectAuthorizationCreate")
    public Result<ProjectAuthorizationVO> create(
        @Validated({ValidationGroup.Create.class}) @RequestBody ProjectAuthorizationVO projectAuthorizationVO) {
        projectAuthorizationVO = super.service.create(projectAuthorizationVO);

        return new Result(projectAuthorizationVO);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<ProjectAuthorizationDomain> find(@PathVariable(name = "id") Long id) {
        ProjectAuthorizationDomain projectAuthorizationDomain = super.service.find(id);

        return new Result(projectAuthorizationDomain);
    }

    /**
     * @param projectAuthorizationVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "projectAuthorizationQuery")
    public Result<PageOrList<ProjectAuthorizationVO>> query(ProjectAuthorizationVO projectAuthorizationVO) {
        PageOrList<ProjectAuthorizationVO> result = super.service.query(projectAuthorizationVO);

        return new Result(result);
    }

    /**
     * @param projectAuthorizationVO
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "projectAuthorizationUpdate")
    public Result update(@PathVariable(name = "id") Long id,
        @RequestBody ProjectAuthorizationVO projectAuthorizationVO) {
        projectAuthorizationVO.setId(id);
        super.service.update(projectAuthorizationVO);

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

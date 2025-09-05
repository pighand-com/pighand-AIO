package com.pighand.aio.controller.dashboard.base;

import com.pighand.aio.domain.base.OrgDepartmentDomain;
import com.pighand.aio.service.BaseOrgDepartmentService;
import com.pighand.aio.vo.base.OrgDepartmentVO;
import com.pighand.framework.spring.api.annotation.*;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 组织 - 部门
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@RestController(path = "base/org/department", docName = "组织 - 部门")
public class OrgDepartmentController extends BaseController<BaseOrgDepartmentService> {

    /**
     * @param baseOrgDepartmentVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "baseOrgDepartmentCreate")
    public Result<OrgDepartmentVO> create(
        @Validated({ValidationGroup.Create.class}) @RequestBody OrgDepartmentVO baseOrgDepartmentVO) {
        baseOrgDepartmentVO = super.service.create(baseOrgDepartmentVO);

        return new Result(baseOrgDepartmentVO);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<OrgDepartmentDomain> find(@PathVariable(name = "id") Long id) {
        OrgDepartmentDomain baseOrgDepartmentDomain = super.service.find(id);

        return new Result(baseOrgDepartmentDomain);
    }

    /**
     * @param baseOrgDepartmentVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "baseOrgDepartmentQuery")
    public Result<PageOrList<OrgDepartmentVO>> query(OrgDepartmentVO baseOrgDepartmentVO) {
        PageOrList<OrgDepartmentVO> result = super.service.query(baseOrgDepartmentVO);

        return new Result(result);
    }

    /**
     * @param baseOrgDepartmentVO
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "baseOrgDepartmentUpdate")
    public Result update(@PathVariable(name = "id") Long id, @RequestBody OrgDepartmentVO baseOrgDepartmentVO) {
        baseOrgDepartmentVO.setId(id);

        super.service.update(baseOrgDepartmentVO);

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

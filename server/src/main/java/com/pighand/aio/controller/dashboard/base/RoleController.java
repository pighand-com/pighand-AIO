package com.pighand.aio.controller.dashboard.base;

import com.pighand.aio.domain.base.RoleDomain;
import com.pighand.aio.service.base.RoleService;
import com.pighand.aio.vo.base.RoleVO;
import com.pighand.framework.spring.api.annotation.*;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 角色
 *
 * @author wangshuli
 * @createDate 2025-06-04 10:08:01
 */
@RestController(path = "dashboard/role", docName = "角色")
public class RoleController extends BaseController<RoleService> {

    /**
     * @param baseRoleVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "baseRoleCreate")
    public Result<RoleVO> create(@Validated({ValidationGroup.Create.class}) @RequestBody RoleVO baseRoleVO) {
        baseRoleVO = super.service.create(baseRoleVO);

        return new Result(baseRoleVO);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<RoleDomain> find(@PathVariable(name = "id") Long id) {
        RoleDomain baseRoleDomain = super.service.find(id);

        return new Result(baseRoleDomain);
    }

    /**
     * @param baseRoleVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "baseRoleQuery")
    public Result<PageOrList<RoleVO>> query(RoleVO baseRoleVO) {
        PageOrList<RoleVO> result = super.service.query(baseRoleVO);

        return new Result(result);
    }

    /**
     * @param baseRoleVO
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "baseRoleUpdate")
    public Result update(@PathVariable(name = "id") Long id, @RequestBody RoleVO baseRoleVO) {
        baseRoleVO.setId(id);

        super.service.update(baseRoleVO);

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

package com.pighand.aio.controller.dashboard.base;

import com.pighand.aio.domain.base.OrgDepartmentDomain;
import com.pighand.aio.service.base.OrgDepartmentService;
import com.pighand.aio.vo.base.OrgDepartmentVO;
import com.pighand.aio.vo.base.UserVO;
import com.pighand.framework.spring.api.annotation.*;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 组织 - 部门
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@RestController(path = "dashboard/org/department", docName = "组织 - 部门")
public class OrgDepartmentController extends BaseController<OrgDepartmentService> {

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
     * @param baseOrgDepartmentVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "baseOrgDepartmentQuery")
    public Result<PageOrList<OrgDepartmentVO>> query(OrgDepartmentVO baseOrgDepartmentVO) {
        PageOrList<OrgDepartmentVO> result = super.service.query(baseOrgDepartmentVO);

        return new Result(result);
    }
    
    /**
     * 获取部门树形结构
     *
     * @return 部门树列表
     */
    @Get(path = "tree", docSummary = "获取部门树形结构")
    public Result<List<OrgDepartmentVO>> getDepartmentTree() {
        List<OrgDepartmentVO> result = super.service.getDepartmentTree();
        return new Result(result);
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

    /**
     * 获取部门员工列表
     *
     * @param departmentId 部门ID
     * @return 员工列表
     */
    @Get(path = "{id}/users", docSummary = "获取部门员工列表")
    public Result<List<UserVO>> getDepartmentUsers(@PathVariable(name = "id") Long departmentId) {
        List<UserVO> users = super.service.getDepartmentUsers(departmentId);
        return new Result(users);
    }

    /**
     * 添加员工到部门
     *
     * @param departmentId 部门ID
     * @param userIds      用户ID列表
     * @return 操作结果
     */
    @Post(path = "{id}/users", docSummary = "添加员工到部门")
    public Result addUsersToDepart(@PathVariable(name = "id") Long departmentId, @RequestBody List<Long> userIds) {
        super.service.addUsersToDepart(departmentId, userIds);
        return new Result();
    }

    /**
     * 从部门移除员工
     *
     * @param departmentId 部门ID
     * @param userId       用户ID
     * @return 操作结果
     */
    @Delete(path = "{id}/users/{userId}", docSummary = "从部门移除员工")
    public Result removeUserFromDepart(@PathVariable(name = "id") Long departmentId,
        @PathVariable(name = "userId") Long userId) {
        super.service.removeUserFromDepart(departmentId, userId);
        return new Result();
    }

    /**
     * 搜索可添加的用户
     *
     * @param departmentId 部门ID
     * @param keyword      搜索关键词
     * @return 用户列表
     */
    @Get(path = "{id}/available-users", docSummary = "搜索可添加的用户")
    public Result<List<UserVO>> searchAvailableUsers(@PathVariable(name = "id") Long departmentId,
        @RequestParam(required = false) String keyword) {
        List<UserVO> users = super.service.searchAvailableUsers(departmentId, keyword);
        return new Result(users);
    }
}

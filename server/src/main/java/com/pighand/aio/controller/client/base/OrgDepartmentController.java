package com.pighand.aio.controller.client.base;

import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.common.interfaces.Authorization;
import com.pighand.aio.service.base.OrgDepartmentService;
import com.pighand.aio.vo.base.LoginUser;
import com.pighand.aio.vo.base.OrgDepartmentVO;
import com.pighand.aio.vo.base.OrgDepartmentSimpleVO;
import com.pighand.framework.spring.api.annotation.Get;
import com.pighand.framework.spring.api.annotation.RestController;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.response.Result;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * 组织 - 部门
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@RequiredArgsConstructor
@RestController(path = "client/org-department", docName = "组织 - 部门")
public class OrgDepartmentController extends BaseController<OrgDepartmentService> {

    /**
     * 查询我的组织架构（完整树形结构）
     * 返回当前用户所属的完整组织架构，包含所有父级部门
     *
     * @return 用户所属的组织架构树
     */
    @Authorization
    @Get(path = "my-tree", docSummary = "查询我的组织架构（完整树形结构）")
    public Result<List<OrgDepartmentVO>> getMyDepartmentTree() {
        LoginUser loginUser = Context.loginUser();
        List<OrgDepartmentVO> result = super.service.getMyDepartmentTree(loginUser.getId());
        
        return new Result<>(result);
    }

    /**
     * 查询我的组织架构（简化链式结构）
     * 返回从根部门到用户所在部门的链式结构
     *
     * @return 用户所属的组织架构（简化版）
     */
    @Authorization
    @Get(path = "my-simple", docSummary = "查询我的组织架构（简化链式结构）")
    public Result<OrgDepartmentSimpleVO> getMyDepartmentSimple() {
        LoginUser loginUser = Context.loginUser();
        OrgDepartmentSimpleVO result = super.service.getMyDepartmentSimple(loginUser.getId());
        
        return new Result<>(result);
    }
}
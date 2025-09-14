package com.pighand.aio.service.base;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.pighand.aio.domain.base.OrgDepartmentDomain;
import com.pighand.aio.domain.base.OrgDepartmentUserRelevanceDomain;
import com.pighand.aio.mapper.base.OrgDepartmentMapper;
import com.pighand.aio.service.base.OrgDepartmentUserRelevanceService;
import com.pighand.aio.service.base.UserService;
import com.pighand.aio.vo.base.OrgDepartmentVO;
import com.pighand.aio.vo.base.OrgDepartmentUserRelevanceVO;
import com.pighand.aio.vo.base.UserVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import static com.pighand.aio.domain.base.table.OrgDepartmentTableDef.ORG_DEPARTMENT;
import static com.pighand.aio.domain.base.table.OrgDepartmentUserRelevanceTableDef.ORG_DEPARTMENT_USER_RELEVANCE;
import static com.pighand.aio.domain.base.table.UserTableDef.USER;

/**
 * 组织 - 部门
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@RequiredArgsConstructor
@Service
public class OrgDepartmentService extends BaseServiceImpl<OrgDepartmentMapper, OrgDepartmentDomain> {

    private final OrgDepartmentUserRelevanceService orgDepartmentUserRelevanceService;
    private final UserService userService;

    /**
     * 创建
     *
     * @param baseOrgDepartmentVO
     * @return
     */
    public OrgDepartmentVO create(OrgDepartmentVO baseOrgDepartmentVO) {
        super.mapper.insert(baseOrgDepartmentVO);

        return baseOrgDepartmentVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public OrgDepartmentDomain find(Long id) {
        return super.mapper.find(id, null);
    }

    /**
     * 分页或列表
     *
     * @param baseOrgDepartmentVO
     * @return PageOrList<BaseOrgDepartmentVO>
     */
    public PageOrList<OrgDepartmentVO> query(OrgDepartmentVO baseOrgDepartmentVO) {

        QueryWrapper queryWrapper = QueryWrapper.create()

            // like
            .and(ORG_DEPARTMENT.NAME.like(baseOrgDepartmentVO.getName()))
            .and(ORG_DEPARTMENT.PARENT_ID.eq(baseOrgDepartmentVO.getParentId()));

        return super.mapper.query(baseOrgDepartmentVO, queryWrapper);
    }
    
    /**
     * 获取部门树形结构
     *
     * @return 部门树列表
     */
    public List<OrgDepartmentVO> getDepartmentTree() {
        // 查询所有部门
        QueryWrapper queryWrapper = QueryWrapper.create();
        PageOrList<OrgDepartmentVO> allDepartments = super.mapper.query(new OrgDepartmentVO(), queryWrapper);
        
        return buildDepartmentTree(allDepartments.getRecords());
    }
    
    /**
     * 构建部门树形结构
     *
     * @param departments 所有部门列表
     * @return 树形结构的部门列表
     */
    private List<OrgDepartmentVO> buildDepartmentTree(List<OrgDepartmentVO> departments) {
        // 按parentId分组
        Map<Long, List<OrgDepartmentVO>> departmentMap = departments.stream()
                .filter(dept -> dept.getParentId() != null)
                .collect(Collectors.groupingBy(OrgDepartmentVO::getParentId));
        
        // 找出根节点（parentId为null的部门）
        List<OrgDepartmentVO> rootDepartments = departments.stream()
                .filter(dept -> dept.getParentId() == null)
                .collect(Collectors.toList());
        
        // 为每个部门设置子部门
        for (OrgDepartmentVO department : departments) {
            List<OrgDepartmentVO> children = departmentMap.get(department.getId());
            department.setChildren(children != null ? children : new ArrayList<>());
        }
        
        return rootDepartments;
    }

    /**
     * 修改
     *
     * @param baseOrgDepartmentVO
     */
    public void update(OrgDepartmentVO baseOrgDepartmentVO) {
        UpdateChain updateChain = this.updateChain().where(ORG_DEPARTMENT.ID.eq(baseOrgDepartmentVO.getId()));

        updateChain.set(ORG_DEPARTMENT.ID, baseOrgDepartmentVO.getId(), VerifyUtils::isNotEmpty);

        updateChain.update();
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(Long id) {
        super.mapper.deleteById(id);
    }

    /**
     * 获取部门员工列表
     *
     * @param departmentId 部门ID
     * @return 员工列表
     */
    public List<UserVO> getDepartmentUsers(Long departmentId) {
        // 查询部门用户关系
        OrgDepartmentUserRelevanceVO queryVO = new OrgDepartmentUserRelevanceVO();
        queryVO.setDepartmentId(departmentId);
        PageOrList<OrgDepartmentUserRelevanceVO> relevanceList = orgDepartmentUserRelevanceService.query(queryVO);
        
        if (relevanceList.getRecords().isEmpty()) {
            return List.of();
        }
        
        // 获取用户ID列表
        List<Long> userIds = relevanceList.getRecords().stream()
                .map(OrgDepartmentUserRelevanceVO::getUserId)
                .collect(Collectors.toList());
        
        // 查询用户详情
        QueryWrapper userQuery = QueryWrapper.create()
                .where(USER.ID.in(userIds));
        
        return super.queryChain()
                .select(USER.ID, USER.USERNAME, USER.PHONE, USER.EMAIL)
                .from(USER)
                .where(USER.ID.in(userIds))
                .listAs(UserVO.class);
    }

    /**
     * 添加员工到部门
     *
     * @param departmentId 部门ID
     * @param userIds 用户ID列表
     */
    public void addUsersToDepart(Long departmentId, List<Long> userIds) {
        for (Long userId : userIds) {
            // 检查是否已存在关系
            OrgDepartmentUserRelevanceVO queryVO = new OrgDepartmentUserRelevanceVO();
            queryVO.setDepartmentId(departmentId);
            queryVO.setUserId(userId);
            PageOrList<OrgDepartmentUserRelevanceVO> existingList = orgDepartmentUserRelevanceService.query(queryVO);
            
            if (existingList.getRecords().isEmpty()) {
                // 创建新的关系
                OrgDepartmentUserRelevanceVO relevanceVO = new OrgDepartmentUserRelevanceVO();
                relevanceVO.setDepartmentId(departmentId);
                relevanceVO.setUserId(userId);
                orgDepartmentUserRelevanceService.create(relevanceVO);
            }
        }
    }

    /**
     * 从部门移除员工
     *
     * @param departmentId 部门ID
     * @param userId 用户ID
     */
    public void removeUserFromDepart(Long departmentId, Long userId) {
        // 查询并删除关系
        QueryWrapper deleteQuery = QueryWrapper.create()
                .where(ORG_DEPARTMENT_USER_RELEVANCE.DEPARTMENT_ID.eq(departmentId))
                .and(ORG_DEPARTMENT_USER_RELEVANCE.USER_ID.eq(userId));
        
        orgDepartmentUserRelevanceService.remove(deleteQuery);
    }

    /**
     * 搜索可添加的用户
     *
     * @param departmentId 部门ID
     * @param keyword 搜索关键词
     * @return 用户列表
     */
    public List<UserVO> searchAvailableUsers(Long departmentId, String keyword) {
        // 获取已在部门的用户ID列表
        OrgDepartmentUserRelevanceVO queryVO = new OrgDepartmentUserRelevanceVO();
        queryVO.setDepartmentId(departmentId);
        PageOrList<OrgDepartmentUserRelevanceVO> relevanceList = orgDepartmentUserRelevanceService.query(queryVO);
        
        List<Long> existingUserIds = relevanceList.getRecords().stream()
                .map(OrgDepartmentUserRelevanceVO::getUserId)
                .collect(Collectors.toList());
        
        // 构建查询
        var query = super.queryChain()
                .select(USER.ID, USER.USERNAME, USER.PHONE, USER.EMAIL)
                .from(USER);
        
        // 排除已在部门的用户
        if (!existingUserIds.isEmpty()) {
            query.where(USER.ID.notIn(existingUserIds));
        }
        
        // 添加搜索条件
        if (keyword != null && !keyword.trim().isEmpty()) {
            if (!existingUserIds.isEmpty()) {
                query.and(USER.USERNAME.like(keyword)
                        .or(USER.PHONE.like(keyword))
                        .or(USER.EMAIL.like(keyword)));
            } else {
                query.where(USER.USERNAME.like(keyword)
                        .or(USER.PHONE.like(keyword))
                        .or(USER.EMAIL.like(keyword)));
            }
        }
        
        return query.listAs(UserVO.class);
    }
}

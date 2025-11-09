package com.pighand.aio.service.base;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.base.OrgDepartmentDomain;
import com.pighand.aio.domain.base.OrgDepartmentUserRelevanceDomain;
import com.pighand.aio.mapper.base.OrgDepartmentMapper;
import com.pighand.aio.vo.base.OrgDepartmentSimpleVO;
import com.pighand.aio.vo.base.OrgDepartmentUserRelevanceVO;
import com.pighand.aio.vo.base.OrgDepartmentVO;
import com.pighand.aio.vo.base.UserVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
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
        // 如果有父节点ID，构建父节点链
        if (baseOrgDepartmentVO.getParentId() != null) {
            List<Long> parents = buildParentsChain(baseOrgDepartmentVO.getParentId());
            baseOrgDepartmentVO.setParents(parents);
        }

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
        Map<Long, List<OrgDepartmentVO>> departmentMap = departments.stream().filter(dept -> dept.getParentId() != null)
            .collect(Collectors.groupingBy(OrgDepartmentVO::getParentId));

        // 找出根节点（parentId为null的部门）
        List<OrgDepartmentVO> rootDepartments =
            departments.stream().filter(dept -> dept.getParentId() == null).collect(Collectors.toList());

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
        // 检查parentId是否发生变化
        boolean parentIdChanged = false;
        if (baseOrgDepartmentVO.getParentId() != null) {
            // 查询当前记录的parentId
            OrgDepartmentDomain currentDepartment = super.mapper.selectOneById(baseOrgDepartmentVO.getId());
            if (currentDepartment != null) {
                Long currentParentId = currentDepartment.getParentId();
                // 判断parentId是否发生变化
                parentIdChanged = !baseOrgDepartmentVO.getParentId().equals(currentParentId);
            }
        }

        // 如果parentId发生变化，重新构建并设置parents字段
        if (parentIdChanged) {
            List<Long> parents = buildParentsChain(baseOrgDepartmentVO.getParentId());
            baseOrgDepartmentVO.setParents(parents);
        }

        // 使用mapper的update方法来更新整个对象，包括parents字段
        super.mapper.update(baseOrgDepartmentVO);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(Long id) {
        super.mapper.deleteById(id);

        orgDepartmentUserRelevanceService.deleteByDepartmentId(id);

        // 查询所有子项
        QueryWrapper queryWrapper = QueryWrapper.create().where(ORG_DEPARTMENT.PARENT_ID.eq(id));

        PageOrList<OrgDepartmentVO> subDepartments = super.mapper.query(new OrgDepartmentVO(), queryWrapper);

        // 递归删除子项
        for (OrgDepartmentVO subDepartment : subDepartments.getRecords()) {
            delete(subDepartment.getId());
        }

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
        List<Long> userIds = relevanceList.getRecords().stream().map(OrgDepartmentUserRelevanceVO::getUserId)
            .collect(Collectors.toList());

        // 查询用户详情
        QueryWrapper userQuery = QueryWrapper.create().where(USER.ID.in(userIds));

        return super.queryChain().select(USER.ID, USER.USERNAME, USER.PHONE, USER.EMAIL).from(USER)
            .where(USER.ID.in(userIds)).listAs(UserVO.class);
    }

    /**
     * 添加员工到部门
     *
     * @param departmentId 部门ID
     * @param userIds      用户ID列表
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
     * @param userId       用户ID
     */
    public void removeUserFromDepart(Long departmentId, Long userId) {
        // 查询并删除关系
        QueryWrapper deleteQuery =
            QueryWrapper.create().where(ORG_DEPARTMENT_USER_RELEVANCE.DEPARTMENT_ID.eq(departmentId))
                .and(ORG_DEPARTMENT_USER_RELEVANCE.USER_ID.eq(userId));

        orgDepartmentUserRelevanceService.remove(deleteQuery);
    }

    /**
     * 搜索可添加的用户
     *
     * @param departmentId 部门ID
     * @param keyword      搜索关键词
     * @return 用户列表
     */
    public List<UserVO> searchAvailableUsers(Long departmentId, String keyword) {
        // 获取已在部门的用户ID列表
        OrgDepartmentUserRelevanceVO queryVO = new OrgDepartmentUserRelevanceVO();
        queryVO.setDepartmentId(departmentId);
        PageOrList<OrgDepartmentUserRelevanceVO> relevanceList = orgDepartmentUserRelevanceService.query(queryVO);

        List<Long> existingUserIds = relevanceList.getRecords().stream().map(OrgDepartmentUserRelevanceVO::getUserId)
            .collect(Collectors.toList());

        // 构建查询
        var query = super.queryChain().select(USER.ID, USER.USERNAME, USER.PHONE, USER.EMAIL).from(USER);

        // 排除已在部门的用户
        if (!existingUserIds.isEmpty()) {
            query.where(USER.ID.notIn(existingUserIds));
        }

        // 添加搜索条件
        if (keyword != null && !keyword.trim().isEmpty()) {
            if (!existingUserIds.isEmpty()) {
                query.and(USER.USERNAME.like(keyword).or(USER.PHONE.like(keyword)).or(USER.EMAIL.like(keyword)));
            } else {
                query.where(USER.USERNAME.like(keyword).or(USER.PHONE.like(keyword)).or(USER.EMAIL.like(keyword)));
            }
        }

        return query.listAs(UserVO.class);
    }

    /**
     * 根据用户ID获取组织结构
     *
     * @param userId 用户ID
     * @return 用户所属的组织结构树
     */
    public List<OrgDepartmentVO> getUserDepartmentTree(Long userId) {
        if (userId == null) {
            return List.of();
        }
        return buildUserDepartmentTree(List.of(userId));
    }

    /**
     * 根据用户ID列表获取组织结构
     *
     * @param userIds 用户ID列表
     * @return 用户所属的组织结构树
     */
    public List<OrgDepartmentVO> getUserDepartmentTree(List<Long> userIds) {
        return buildUserDepartmentTree(userIds);
    }

    /**
     * 根据用户ID列表构建组织结构树
     *
     * @param userIds 用户ID列表
     * @return 用户所属的组织结构树
     */
    private List<OrgDepartmentVO> buildUserDepartmentTree(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return List.of();
        }

        // 1. 查询用户关联的部门ID
        QueryWrapper relevanceQuery = QueryWrapper.create().where(ORG_DEPARTMENT_USER_RELEVANCE.USER_ID.in(userIds));

        List<OrgDepartmentUserRelevanceDomain> relevanceList =
            orgDepartmentUserRelevanceService.queryChain().where(ORG_DEPARTMENT_USER_RELEVANCE.USER_ID.in(userIds))
                .list();

        if (relevanceList.isEmpty()) {
            return List.of();
        }

        // 2. 获取用户直接关联的部门ID
        Set<Long> directDepartmentIds =
            relevanceList.stream().map(OrgDepartmentUserRelevanceDomain::getDepartmentId).collect(Collectors.toSet());

        // 3. 获取所有相关的部门ID（包括父级部门）
        Set<Long> allDepartmentIds = new HashSet<>(directDepartmentIds);
        for (Long departmentId : directDepartmentIds) {
            // 查询部门信息获取parentId
            OrgDepartmentDomain department = super.mapper.selectOneById(departmentId);
            if (department != null && department.getParentId() != null) {
                // 添加所有父级部门ID
                List<Long> parentIds = buildParentsChain(department.getParentId());
                allDepartmentIds.addAll(parentIds);
                allDepartmentIds.add(department.getParentId()); // 添加直接父级
            }
        }

        // 4. 查询所有相关部门的详细信息
        if (allDepartmentIds.isEmpty()) {
            return List.of();
        }

        QueryWrapper departmentQuery = QueryWrapper.create().where(ORG_DEPARTMENT.ID.in(allDepartmentIds));

        PageOrList<OrgDepartmentVO> allDepartments = super.mapper.query(new OrgDepartmentVO(), departmentQuery);

        // 5. 构建树形结构
        return buildDepartmentTree(allDepartments.getRecords());
    }

    /**
     * 构建父级链条
     *
     * @param parentId 父级ID
     * @return 父级ID列表，从根节点到直接父级
     */
    private List<Long> buildParentsChain(Long parentId) {
        List<Long> parents = new ArrayList<>();

        if (parentId == null) {
            return parents;
        }

        Long currentParentId = parentId;

        // 防止无限循环，最多查询10层
        int maxDepth = 10;
        int currentDepth = 0;

        while (currentParentId != null && currentDepth < maxDepth) {
            // 查询当前父节点
            OrgDepartmentDomain parentDepartment = super.mapper.selectOneById(currentParentId);

            if (parentDepartment == null) {
                break;
            }

            // 将当前父节点ID添加到链的开头（保证从根到叶的顺序）
            parents.add(0, currentParentId);

            // 继续查询上一级父节点
            currentParentId = parentDepartment.getParentId();
            currentDepth++;
        }

        return parents;
    }

    /**
     * 根据用户ID获取简化的组织结构（单个对象）
     *
     * @param userId 用户ID
     * @return 用户所属的组织结构（简化版）
     */
    public OrgDepartmentSimpleVO getUserDepartmentSimple(Long userId) {
        if (userId == null) {
            return null;
        }

        // 1. 查询用户关联的部门ID
        List<OrgDepartmentUserRelevanceDomain> relevanceList =
            orgDepartmentUserRelevanceService.queryChain().where(ORG_DEPARTMENT_USER_RELEVANCE.USER_ID.eq(userId))
                .list();

        if (relevanceList.isEmpty()) {
            return null;
        }

        // 2. 获取用户直接关联的第一个部门
        Long departmentId = relevanceList.get(0).getDepartmentId();
        OrgDepartmentDomain department = super.mapper.selectOneById(departmentId);

        if (department == null) {
            return null;
        }

        // 3. 找到最顶级部门
        OrgDepartmentDomain topDepartment = findTopDepartment(department);

        // 4. 从顶级部门开始构建链式结构到用户所在部门
        return buildSimpleDepartmentChainFromTop(topDepartment, departmentId);
    }

    /**
     * 找到最顶级部门
     *
     * @param department 当前部门
     * @return 最顶级部门
     */
    private OrgDepartmentDomain findTopDepartment(OrgDepartmentDomain department) {
        if (department.getParentId() == null) {
            return department;
        }

        OrgDepartmentDomain parentDepartment = super.mapper.selectOneById(department.getParentId());
        if (parentDepartment == null) {
            return department;
        }

        return findTopDepartment(parentDepartment);
    }

    /**
     * 从顶级部门开始构建简化的部门链到目标部门
     *
     * @param currentDepartment  当前部门
     * @param targetDepartmentId 目标部门ID
     * @return 简化的部门对象
     */
    private OrgDepartmentSimpleVO buildSimpleDepartmentChainFromTop(OrgDepartmentDomain currentDepartment,
        Long targetDepartmentId) {
        OrgDepartmentSimpleVO simpleVO = new OrgDepartmentSimpleVO();
        simpleVO.setId(currentDepartment.getId());
        simpleVO.setName(currentDepartment.getName());

        // 如果当前部门就是目标部门，直接返回
        if (currentDepartment.getId().equals(targetDepartmentId)) {
            return simpleVO;
        }

        // 查找子部门中通往目标部门的路径
        List<OrgDepartmentDomain> childDepartments = super.mapper.selectListByQuery(
            QueryWrapper.create().where(ORG_DEPARTMENT.PARENT_ID.eq(currentDepartment.getId())));

        for (OrgDepartmentDomain childDepartment : childDepartments) {
            // 检查这个子部门是否在通往目标部门的路径上
            if (isOnPathToTarget(childDepartment, targetDepartmentId)) {
                simpleVO.setChild(buildSimpleDepartmentChainFromTop(childDepartment, targetDepartmentId));
                break;
            }
        }

        return simpleVO;
    }

    /**
     * 检查部门是否在通往目标部门的路径上
     *
     * @param department         当前部门
     * @param targetDepartmentId 目标部门ID
     * @return 是否在路径上
     */
    private boolean isOnPathToTarget(OrgDepartmentDomain department, Long targetDepartmentId) {
        if (department.getId().equals(targetDepartmentId)) {
            return true;
        }

        // 递归检查子部门
        List<OrgDepartmentDomain> childDepartments = super.mapper.selectListByQuery(
            QueryWrapper.create().where(ORG_DEPARTMENT.PARENT_ID.eq(department.getId())));

        for (OrgDepartmentDomain childDepartment : childDepartments) {
            if (isOnPathToTarget(childDepartment, targetDepartmentId)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 获取当前用户的组织架构（完整树形结构）
     * 返回用户所属的完整组织架构，包含所有父级部门
     *
     * @param currentUserId 当前用户ID
     * @return 用户所属的组织架构树
     */
    public List<OrgDepartmentVO> getMyDepartmentTree(Long currentUserId) {
        return getUserDepartmentTree(currentUserId);
    }

    /**
     * 获取当前用户的组织架构（简化链式结构）
     * 返回从根部门到用户所在部门的链式结构
     *
     * @param currentUserId 当前用户ID
     * @return 用户所属的组织架构（简化版）
     */
    public OrgDepartmentSimpleVO getMyDepartmentSimple(Long currentUserId) {
        return getUserDepartmentSimple(currentUserId);
    }
}

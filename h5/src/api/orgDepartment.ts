import request from '@/common/request';

/**
 * 组织架构相关API
 */

/**
 * 查询我的组织架构（完整树形结构）
 * @returns 用户所属的组织架构树
 */
export const getMyDepartmentTree = async () => {
    return await request.get('org-department/my-tree');
};

/**
 * 查询我的组织架构（简化链式结构）
 * @returns 用户所属的组织架构（简化版）
 */
export const getMyDepartmentSimple = async () => {
    return await request.get('org-department/my-simple');
};

export default {
    getMyDepartmentTree,
    getMyDepartmentSimple
};
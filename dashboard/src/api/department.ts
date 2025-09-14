import request from '@/common/request';

const baseUrl = 'org/department';

export default {
    // 基础CRUD操作
    create: async (param: any) => await request.post(`${baseUrl}`, param),
    find: async (id: any) => await request.get(`${baseUrl}/${id}`),
    query: async (param?: any) => {
        if (param?.createdAtRange) {
            param.createdAtRange = param.createdAtRange.join(',');
        }
        return await request.get(`${baseUrl}`, param);
    },
    update: async (param: any) => await request.put(`${baseUrl}/${param.id}`, param),
    del: async (id: any) => await request.del(`${baseUrl}/${id}`),
    
    // 部门员工管理相关接口
    getDepartmentUsers: async (departmentId: number) => 
        await request.get(`${baseUrl}/${departmentId}/users`),
    
    addUserToDepartment: async (departmentId: number, userIds: number[]) => 
        await request.post(`${baseUrl}/${departmentId}/users`, userIds),
    
    removeUserFromDepartment: async (departmentId: number, userId: number) => 
        await request.del(`${baseUrl}/${departmentId}/users/${userId}`),
    
    searchAvailableUsers: async (departmentId: number, keyword?: string) => {
        const params = keyword ? { keyword } : {};
        return await request.get(`${baseUrl}/${departmentId}/available-users`, params);
    },
    
    // 获取部门树形结构
    getDepartmentTree: async () => await request.get(`${baseUrl}/tree`)
};
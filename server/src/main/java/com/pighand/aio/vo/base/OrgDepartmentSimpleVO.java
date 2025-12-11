package com.pighand.aio.vo.base;

import lombok.Data;

/**
 * 组织 - 部门 - 简化版本
 * 只包含id、name和child字段
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@Data
public class OrgDepartmentSimpleVO {
    
    /**
     * 部门ID
     */
    private Long id;
    
    /**
     * 部门名称
     */
    private String name;
    
    /**
     * 子部门（单个对象，不是数组）
     */
    private OrgDepartmentSimpleVO child;
}
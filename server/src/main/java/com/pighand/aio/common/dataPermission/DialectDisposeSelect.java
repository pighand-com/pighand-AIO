package com.pighand.aio.common.dataPermission;

import com.mybatisflex.core.dialect.OperateType;
import com.mybatisflex.core.dialect.impl.CommonsDialectImpl;
import com.mybatisflex.core.query.CPI;
import com.mybatisflex.core.query.QueryMethods;
import com.mybatisflex.core.query.QueryTable;
import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.common.dataPermission.ignore.RunWithIgnore;

import java.util.List;

/**
 * 查询数据权限处理
 */
public class DialectDisposeQuery extends CommonsDialectImpl {
    /**
     * 准备权限验证
     * <p>
     * 在SQL执行前自动为需要数据权限控制的表添加WHERE条件，
     * 确保查询结果只包含当前用户有权限访问的数据。
     *
     * @param queryWrapper 查询包装器，用于构建SQL查询条件
     * @param operateType
     */
    @Override
    public void prepareAuth(QueryWrapper queryWrapper, OperateType operateType) {
        if (!RunWithIgnore.isIgnoreDataPermission()) {
            // 获取查询中涉及的所有表
            List<QueryTable> queryTables = CPI.getQueryTables(queryWrapper);
            if (queryTables == null || queryTables.isEmpty()) {
                return;
            }

            // 遍历每个查询表，为需要数据权限控制的表添加过滤条件
            for (QueryTable queryTable : queryTables) {
                // 检查当前表是否配置了数据权限字段
                List<String> permissionFields = DataPermissionFields.tableFieldMap.get(queryTable.getName());

                if (permissionFields == null || permissionFields.isEmpty()) {
                    continue;
                }

                // 为每个数据权限字段添加过滤条件
                for (String permissionField : permissionFields) {
                    Object fieldValue = DataPermissionFields.getFiledValue(permissionField);

                    if (fieldValue == null) {
                        continue;
                    }

                    // 添加WHERE条件：表名.字段名 = 字段值
                    queryWrapper.and(QueryMethods.column(queryTable.getName() + "." + permissionField).eq(fieldValue));
                }
            }
        }

        // 调用父类方法，执行其他权限验证逻辑
        super.prepareAuth(queryWrapper, operateType);
    }
}

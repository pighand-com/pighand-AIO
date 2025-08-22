package com.pighand.aio.common.dataPermission;

import com.mybatisflex.core.FlexGlobalConfig;

import java.util.List;

/**
 * insert 数据权限处理
 */
public class DialectDisposeInsert {

    /**
     * 使用mybatis-flex的insert listener插入字段
     *
     * @param clazz      实体类
     * @param tableName  表名
     * @param fieldNames 字段名
     */
    public static void dispose(Class<?> clazz, String tableName, List<String> fieldNames) {
        FlexGlobalConfig config = FlexGlobalConfig.getDefaultConfig();

        config.registerInsertListener((entity) -> {
            for (String fieldName : fieldNames) {
                DataPermissionFields.setFieldValue(entity, tableName, fieldName);
            }
        }, clazz);
    }
}

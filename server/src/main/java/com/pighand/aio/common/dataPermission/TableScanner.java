package com.pighand.aio.common.dataPermission;

import com.mybatisflex.annotation.Table;
import com.pighand.framework.spring.base.BaseModel;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 扫描所有实体表，匹配需要数据权限的表和字段
 * <p>
 * 实体类如果不在”com.pighand.aio.domain“包下，需修改packageName
 */
public class TableScanner {

    /**
     * 扫描实体的包
     */
    private final String packageName = "com.pighand.aio.domain";

    public void scanTables() throws Exception {
        // 1. 创建扫描器，过滤实体类
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AssignableTypeFilter(BaseModel.class));

        for (var beanDef : scanner.findCandidateComponents(packageName)) {
            Class<?> clazz = Class.forName(beanDef.getBeanClassName());

            try {
                // 2. 检查是否有 @Table 注解
                Table tableAnnotation = clazz.getAnnotation(Table.class);
                if (tableAnnotation == null || tableAnnotation.value().isEmpty()) {
                    continue;
                }

                String tableName = tableAnnotation.value();

                List<String> foundFields = new ArrayList<>();

                // 3. 检查是否有目标字段
                for (Field field : clazz.getDeclaredFields()) {
                    var dbFieldName = DataPermissionFields.FIELD_ALIASES.get(field.getName());
                    if (dbFieldName != null) {
                        foundFields.add(dbFieldName);

                        // 缓存field的setter方法
                        DataPermissionFields.cacheFieldSetter(tableName, dbFieldName, clazz, field);

                        // 找到所有目标字段就提前退出
                        if (foundFields.size() == DataPermissionFields.TARGET_FIELDS.size()) {
                            break;
                        }
                    }
                }

                if (foundFields.isEmpty()) {
                    continue;
                }

                // 4. 注册insert处理
                DialectDisposeInsert.dispose(clazz, tableName, foundFields);

                // 5. 保存数据权限字段
                DataPermissionFields.tableFieldMap.put(tableName, foundFields);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

package com.pighand.aio.common.dataPermission;

import com.pighand.aio.common.interceptor.Context;

import java.beans.PropertyDescriptor;
import java.lang.invoke.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * 数据权限字段
 */
public class DataPermissionFields {
    /**
     * 需要做数据权限过滤的表和字段
     * <p>
     * 表名 -> 数据库字段名
     */
    public static final Map<String, List<String>> tableFieldMap = new HashMap<>();

    /**
     * 数据库字段名映射
     * <p>
     * 实体字段名 -> 数据库字段名
     */
    public static final Map<String, String> FIELD_ALIASES = Arrays.stream(DataPermissionFieldEnum.values())
        .collect(Collectors.toMap(DataPermissionFieldEnum::getFieldName, DataPermissionFieldEnum::getDbFieldName));

    /**
     * 需要做数据权限过滤的字段
     */
    public static final Set<String> TARGET_FIELDS = FIELD_ALIASES.keySet();

    /**
     * 存field的setter方法： key = tableName.fieldName
     */
    private static final Map<String, BiConsumer<Object, Object>> fieldSetterMap = new HashMap<>();

    /**
     * 生成setter缓存 key
     *
     * @param tableName 表名
     * @param fileName  字段名
     * @return
     */
    private static String getSetterKey(String tableName, String fileName) {
        return tableName + "." + fileName;
    }

    /**
     * 缓存field的setter方法
     *
     * @param tableName 表名
     * @param fileName  数据库字段名
     * @param clazz
     * @param field
     */
    public static void cacheFieldSetter(String tableName, String fileName, Class<?> clazz, Field field) {
        String setterKey = getSetterKey(tableName, fileName);

        if (fieldSetterMap.containsKey(setterKey)) {
            return;
        }

        BiConsumer<Object, Object> clearMethod = createSetter(clazz, field);
        if (clearMethod != null) {
            fieldSetterMap.put(setterKey, clearMethod);
        }
    }

    /**
     * 获取数据权限字段的默认值
     *
     * @param fieldName
     * @return
     */
    public static Object getFiledDefaultValue(String fieldName) {
        if (fieldName == null) {
            return null;
        }

        if (fieldName.equals(DataPermissionFieldEnum.APPLICATION_ID.getDbFieldName())) {
            return Context.applicationId();
        } else if (fieldName.equals(DataPermissionFieldEnum.STORE_ID.getDbFieldName())) {
            return Context.storeId();
        }

        return null;
    }

    /**
     * 回填字段值
     *
     * @param entity
     * @param tableName 表名
     * @param fileName  数据库字段名
     */
    public static void setFieldValue(Object entity, String tableName, String fileName) {
        String setterKey = getSetterKey(tableName, fileName);
        BiConsumer<Object, Object> fieldSetter = fieldSetterMap.get(setterKey);
        if (fieldSetter != null) {
            Object filedValue = getFiledDefaultValue(fileName);

            fieldSetter.accept(entity, filedValue);
        }
    }

    /**
     * 生成字段的 setter 方法
     */
    private static BiConsumer<Object, Object> createSetter(Class<?> clazz, Field field) {
        try {
            // 用 PropertyDescriptor 找 setter（避免手动拼接）
            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
            Method setter = pd.getWriteMethod();
            if (setter == null) {
                return null; // 没有 setter 就跳过
            }

            MethodHandles.Lookup lookup = MethodHandles.lookup();
            MethodHandle mh = lookup.unreflect(setter);

            // 转换成 BiConsumer<Object, Object>
            CallSite site = LambdaMetafactory.metafactory(lookup, "accept", MethodType.methodType(BiConsumer.class),
                MethodType.methodType(void.class, Object.class, Object.class), mh, mh.type());
            return (BiConsumer<Object, Object>)site.getTarget().invokeExact();
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }
}

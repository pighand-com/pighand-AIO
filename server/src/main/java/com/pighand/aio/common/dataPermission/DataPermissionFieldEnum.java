package com.pighand.aio.common.dataPermission;

/**
 * 数据权限字段枚举
 */
public enum DataPermissionFieldEnum {
    
    /**
     * 应用ID
     */
    APPLICATION_ID("applicationId", "application_id"),
    
    /**
     * 店铺ID
     */
    STORE_ID("storeId", "store_id");
    
    /**
     * 实体字段名
     */
    private final String fieldName;
    
    /**
     * 数据库字段名
     */
    private final String dbFieldName;
    
    DataPermissionFieldEnum(String fieldName, String dbFieldName) {
        this.fieldName = fieldName;
        this.dbFieldName = dbFieldName;
    }
    
    /**
     * 获取实体字段名
     *
     * @return 实体字段名
     */
    public String getFieldName() {
        return fieldName;
    }
    
    /**
     * 获取数据库字段名
     *
     * @return 数据库字段名
     */
    public String getDbFieldName() {
        return dbFieldName;
    }
    
    /**
     * 根据字段名获取枚举
     *
     * @param fieldName 字段名
     * @return 枚举实例
     */
    public static DataPermissionFieldEnum getByFieldName(String fieldName) {
        if (fieldName == null) {
            return null;
        }
        
        for (DataPermissionFieldEnum field : values()) {
            if (field.getFieldName().equals(fieldName)) {
                return field;
            }
        }
        
        return null;
    }
}
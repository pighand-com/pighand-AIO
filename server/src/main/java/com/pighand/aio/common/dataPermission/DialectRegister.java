package com.pighand.aio.common.dataPermission;

import com.mybatisflex.core.dialect.DbType;
import com.mybatisflex.core.dialect.DialectFactory;

/**
 * 注册查询权限监听方言
 */
public class DialectRegister {

    public DialectRegister() throws Exception {
        // 扫描表（注册insert处理）
        TableScanner tableScanner = new TableScanner();
        tableScanner.scanTables();

        // 注册select处理
        DialectFactory.registerDialect(DbType.MYSQL, new DialectDisposeSelect());
    }
}

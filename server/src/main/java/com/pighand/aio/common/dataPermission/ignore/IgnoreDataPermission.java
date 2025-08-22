package com.pighand.aio.common.dataPermission.ignore;

import java.lang.annotation.*;

/**
 * 忽略数据权限
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreDataPermission {
}

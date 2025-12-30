package com.pighand.aio.common.dataPermission.ignore;

import java.util.function.Supplier;

public class RunWithIgnore {
    private static final ScopedValue<Boolean> isIgnoreDataPermission = ScopedValue.newInstance();

    /**
     * 是否忽略数据权限
     *
     * @return true 忽略数据权限
     */
    public static boolean isIgnoreDataPermission() {
        Boolean ignore = isIgnoreDataPermission.get();
        return ignore != null && ignore;
    }

    /**
     * 设置忽略数据权限
     *
     * @param ignore true 忽略数据权限
     */
    public static void setIgnoreDataPermission(boolean ignore) {
        ScopedValue.where(isIgnoreDataPermission, ignore);
    }

    /**
     * 使用忽略数据权限方式运行
     * <p>
     * 使用方法：
     * var result = IgnoreDataPermission(() -> service.find(id));
     *
     * @param supplier
     * @param <T>
     * @return
     */
    public static <T> T IgnoreDataPermission(Supplier<T> supplier) {
        before();
        try {
            return supplier.get();
        } finally {
            after();
        }
    }

    /**
     * 使用忽略数据权限方式运行
     * <p>
     * 使用方法：
     * IgnoreDataPermission(() -> service.find(id));
     */
    public static void IgnoreDataPermission(Runnable runnable) {
        before();
        try {
            runnable.run();
        } finally {
            after();
        }
    }

    private static void before() {
        ScopedValue.where(isIgnoreDataPermission, true);
    }

    private static void after() {
        ScopedValue.where(isIgnoreDataPermission, null);
    }
}

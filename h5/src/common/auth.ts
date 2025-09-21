import { getUserInfo, getToken, clearAll } from './storage';
import router from '@/routers';

/**
 * 用户角色枚举
 */
export enum UserRole {
    ADMIN = 'admin',
    USER = 'user'
}

/**
 * 权限枚举
 */
export enum Permission {
    // 素材相关权限
    ASSET_VIEW = 'asset:view',
    ASSET_DOWNLOAD = 'asset:download',
    ASSET_FAVORITE = 'asset:favorite',
    ASSET_UPLOAD = 'asset:upload',
    ASSET_MANAGE = 'asset:manage',
    
    // 用户相关权限
    USER_PROFILE = 'user:profile',
    USER_MANAGE = 'user:manage',
    
    // 系统相关权限
    SYSTEM_ADMIN = 'system:admin'
}

/**
 * 角色权限映射
 */
const rolePermissions: Record<UserRole, Permission[]> = {
    [UserRole.ADMIN]: [
        Permission.ASSET_VIEW,
        Permission.ASSET_DOWNLOAD,
        Permission.ASSET_FAVORITE,
        Permission.ASSET_UPLOAD,
        Permission.ASSET_MANAGE,
        Permission.USER_PROFILE,
        Permission.USER_MANAGE,
        Permission.SYSTEM_ADMIN
    ],
    [UserRole.USER]: [
        Permission.ASSET_VIEW,
        Permission.ASSET_DOWNLOAD,
        Permission.ASSET_FAVORITE,
        Permission.USER_PROFILE
    ]
};

/**
 * 检查用户是否已登录
 */
export const isAuthenticated = (): boolean => {
    const token = getToken();
    return !!token;
};

/**
 * 获取当前用户信息
 */
export const getCurrentUser = () => {
    return getUserInfo();
};

/**
 * 获取当前用户角色
 */
export const getCurrentUserRole = (): UserRole | null => {
    const userInfo = getCurrentUser();
    return userInfo?.role || null;
};

/**
 * 检查用户是否具有指定权限
 * @param permission 权限
 */
export const hasPermission = (permission: Permission): boolean => {
    if (!isAuthenticated()) {
        return false;
    }
    
    const userRole = getCurrentUserRole();
    if (!userRole) {
        return false;
    }
    
    const permissions = rolePermissions[userRole] || [];
    return permissions.includes(permission);
};

/**
 * 检查用户是否具有指定角色
 * @param role 角色
 */
export const hasRole = (role: UserRole): boolean => {
    const userRole = getCurrentUserRole();
    return userRole === role;
};

/**
 * 检查用户是否为管理员
 */
export const isAdmin = (): boolean => {
    return hasRole(UserRole.ADMIN);
};

/**
 * 权限检查装饰器（用于组件方法）
 * @param permission 权限
 */
export const requirePermission = (permission: Permission) => {
    return (target: any, propertyKey: string, descriptor: PropertyDescriptor) => {
        const originalMethod = descriptor.value;
        
        descriptor.value = function (...args: any[]) {
            if (!hasPermission(permission)) {
                console.warn(`权限不足: ${permission}`);
                return;
            }
            return originalMethod.apply(this, args);
        };
        
        return descriptor;
    };
};

/**
 * 权限守卫 - 检查权限并处理未授权情况
 * @param permission 权限
 * @param redirectToLogin 是否重定向到登录页
 */
export const checkPermission = (permission: Permission, redirectToLogin = true): boolean => {
    if (!isAuthenticated()) {
        if (redirectToLogin) {
            router.push({ name: 'login' });
        }
        return false;
    }
    
    if (!hasPermission(permission)) {
        console.warn(`权限不足: ${permission}`);
        return false;
    }
    
    return true;
};

/**
 * 退出登录
 */
export const logout = () => {
    clearAll();
    router.replace({ name: 'login' });
};

/**
 * 权限检查的Vue组合式函数
 */
export const useAuth = () => {
    return {
        isAuthenticated,
        getCurrentUser,
        getCurrentUserRole,
        hasPermission,
        hasRole,
        isAdmin,
        checkPermission,
        logout
    };
};
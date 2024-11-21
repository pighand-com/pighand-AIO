import { shallowRef } from 'vue';
import constant from '@/common/constant';
import { Files } from '@element-plus/icons-vue';

const routes = [
    {
        path: '/',
        redirect: { name: 'login' }
    },
    {
        path: '/login',
        name: 'login',
        title: '登录',
        meta: {
            requiresAuth: false,
            pageType: constant.page_type_multi
        },
        component: () => import('@/pages/Login.vue')
    },
    {
        path: '/article',
        name: 'article',
        title: '内容管理',
        meta: {
            default: true,
            requiresAuth: true,
            icon: shallowRef(Files),
            pageType: constant.page_type_single
        },
        component: () => import('@/pages/Article.vue')
    },
    {
        path: '/user',
        name: 'user',
        title: '用户管理',
        meta: {
            default: true,
            requiresAuth: true,
            icon: shallowRef(Files),
            pageType: constant.page_type_single
        },
        component: () => import('@/pages/User.vue')
    }
];

/**
 * 获取默认路由
 *
 * 优先:
 * 1. meta.default = true
 * 2. 第一个page_type_single页面
 * 3. '/'
 */
const getDefaultRouterPath = () => {
    let defaultPath = '';
    let firstSinglePath = '';
    routes.forEach((item) => {
        if (item?.meta?.default) {
            defaultPath = item.path;
        }

        if (!defaultPath && item.meta?.pageType === constant.page_type_single) {
            firstSinglePath = item.path;
        }
    });

    return defaultPath || firstSinglePath || '/';
};

export { getDefaultRouterPath };
export default routes;

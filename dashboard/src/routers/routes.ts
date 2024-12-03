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
        path: '/marketing',
        name: 'marketing',
        title: '营销活动',
        meta: {
            requiresAuth: true,
            icon: shallowRef(Files),
            pageType: constant.page_type_single
        },
        children: [
            {
                path: 'lottery',
                name: 'lottery',
                title: '抽奖',
                meta: {
                    icon: shallowRef(Files)
                },
                component: () => import('@/pages/Lottery.vue')
            }
        ]
    },
    {
        path: '/system',
        name: 'system',
        title: '系统管理',
        meta: {
            requiresAuth: true,
            icon: shallowRef(Files),
            pageType: constant.page_type_single
        },
        children: [
            {
                path: 'user',
                name: 'user',
                title: '用户管理',
                meta: {
                    icon: shallowRef(Files)
                },
                component: () => import('@/pages/User.vue')
            }
        ]
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

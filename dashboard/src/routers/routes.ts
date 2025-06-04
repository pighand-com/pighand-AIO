import { shallowRef } from 'vue';
import constant from '@/common/constant';
import {
    BookOne,
    Oceanengine,
    GoldMedal,
    System,
    EveryUser,
    UserBusiness,
    Shop,
    Components,
    Material,
    MovieBoard,
    Movie,
    TicketOne
} from '@icon-park/vue-next';

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
            icon: shallowRef(BookOne),
            pageType: constant.page_type_single
        },
        component: () => import('@/pages/Article.vue')
    },
    {
        path: '/theme',
        name: 'theme',
        title: '主题运营',
        meta: {
            requiresAuth: true,
            icon: shallowRef(MovieBoard),
            pageType: constant.page_type_single
        },
        children: [
            {
                path: 'theme',
                name: 'theme',
                title: '主题',
                meta: {
                    icon: shallowRef(Movie)
                },
                component: () => import('@/pages/Theme.vue')
            },
            {
                path: 'ticket',
                name: 'ticket',
                title: '票务',
                meta: {
                    icon: shallowRef(TicketOne)
                },
                component: () => import('@/pages/Ticket.vue')
            }
        ]
    },
    {
        path: '/marketing',
        name: 'marketing',
        title: '营销活动',
        meta: {
            requiresAuth: true,
            icon: shallowRef(Oceanengine),
            pageType: constant.page_type_single
        },
        children: [
            {
                path: 'lottery',
                name: 'lottery',
                title: '抽奖',
                meta: {
                    icon: shallowRef(GoldMedal)
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
            icon: shallowRef(System),
            pageType: constant.page_type_single
        },
        children: [
            {
                path: 'tenant',
                name: 'tenant',
                title: '商户管理',
                meta: {
                    icon: shallowRef(UserBusiness)
                },
                component: () => import('@/pages/Tenant.vue')
            },
            {
                path: 'store',
                name: 'store',
                title: '门店管理',
                meta: {
                    icon: shallowRef(Shop)
                },
                component: () => import('@/pages/Store.vue')
            },
            {
                path: 'user',
                name: 'user',
                title: '用户管理',
                meta: {
                    icon: shallowRef(EveryUser)
                },
                component: () => import('@/pages/User.vue')
            }
        ]
    },
    {
        path: '/common',
        name: 'common',
        title: '其他',
        meta: {
            requiresAuth: true,
            icon: shallowRef(Components),
            pageType: constant.page_type_single
        },
        children: [
            {
                path: 'assets',
                name: 'assets',
                title: '素材管理',
                meta: {
                    icon: shallowRef(Material)
                },
                component: () => import('@/pages/Assets.vue')
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

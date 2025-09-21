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
    TicketOne,
    MultiPictureCarousel,
    HeavyMetal,
    FinancingTwo,
    IdCardV,
    Order,
    TreeList,
    Picture,
    Video,
    FileDoc,
    Folder,
    FileCollection
} from '@icon-park/vue-next';

const routes = [
    {
        path: '/',
        redirect: { name: 'home' }
    },
    {
        path: '/login',
        name: 'login',
        title: '登录',
        meta: {
            requiresAuth: false,
            pageType: 'mobile'
        },
        component: () => import('@/pages/Login.vue')
    },
    {
        path: '/home',
        name: 'home',
        title: '首页',
        icon: shallowRef(BookOne),
        meta: {
            requiresAuth: true,
            pageType: 'mobile',
            default: true
        },
        component: () => import('@/pages/Home.vue')
    },
    {
        path: '/category',
        name: 'category',
        title: '分类',
        icon: shallowRef(Folder),
        meta: {
            requiresAuth: true,
            pageType: 'mobile'
        },
        component: () => import('@/pages/Category.vue')
    },
    {
        path: '/profile',
        name: 'profile',
        title: '我的',
        icon: shallowRef(EveryUser),
        meta: {
            requiresAuth: true,
            pageType: 'mobile'
        },
        component: () => import('@/pages/Profile.vue')
    },
    {
        path: '/assets-list',
        name: 'assetsList',
        title: '素材列表',
        meta: {
            requiresAuth: true,
            pageType: 'mobile'
        },
        component: () => import('@/pages/AssetsList.vue')
    },
    {
        path: '/asset-detail/:type/:id',
        name: 'assetDetail',
        title: '素材详情',
        meta: {
            requiresAuth: true,
            pageType: 'mobile'
        },
        component: () => import('@/pages/AssetDetail.vue')
    },
    {
        path: '/records/:type',
        name: 'recordsList',
        title: '记录列表',
        meta: {
            requiresAuth: true,
            pageType: 'mobile'
        },
        component: () => import('@/pages/RecordsList.vue')
    },
    {
        path: '/favorite-list',
        name: 'favoriteList',
        title: '我的收藏',
        meta: {
            requiresAuth: true,
            pageType: 'mobile'
        },
        component: () => import('@/pages/FavoriteList.vue')
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
        if (!defaultPath && item.meta?.pageType === constant.page_type_single) {
            firstSinglePath = item.path;
        }
    });

    return defaultPath || firstSinglePath || '/';
};

export { getDefaultRouterPath };
export default routes;

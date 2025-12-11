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
        component: () => import('@/pages/base/Login.vue')
    },
    {
        path: '/cms',
        name: 'cms',
        title: '内容管理',
        meta: {
            default: true,
            requiresAuth: true,
            icon: shallowRef(BookOne),
            pageType: constant.page_type_single
        },
        children: [
            // {
            //     path: 'article',
            //     name: 'article',
            //     title: '文章',
            //     meta: {
            //         icon: shallowRef(Movie)
            //     },
            //     component: () => import('@/pages/CMS/Article.vue')
            // },
            // {
            //     path: 'banner',
            //     name: 'banner',
            //     title: 'Banner',
            //     meta: {
            //         icon: shallowRef(MultiPictureCarousel)
            //     },
            //     component: () => import('@/pages/CMS/Banner.vue')
            // },
            // {
            //     path: 'assets',
            //     name: 'assets',
            //     title: '素材管理',
            //     meta: {
            //         icon: shallowRef(Material)
            //     },
            //     component: () => import('@/pages/common/Assets.vue')
            // },
            {
                path: 'assets_image',
                name: 'assets_image',
                title: '图片素材',
                meta: {
                    icon: shallowRef(Picture)
                },
                component: () => import('@/pages/CMS/AssetsImage.vue')
            },
            {
                path: 'assets_video',
                name: 'assets_video',
                title: '视频素材',
                meta: {
                    icon: shallowRef(Video)
                },
                component: () => import('@/pages/CMS/AssetsVideo.vue')
            },
            {
                path: 'assets_doc',
                name: 'assets_doc',
                title: '文档素材',
                meta: {
                    icon: shallowRef(FileDoc)
                },
                component: () => import('@/pages/CMS/AssetsDoc.vue')
            },
            {
                path: 'assets_classification',
                name: 'assets_classification',
                title: '素材分类',
                meta: {
                    icon: shallowRef(Folder)
                },
                component: () => import('@/pages/CMS/AssetsClassification.vue')
            },
            {
                path: 'assets_collection',
                name: 'assets_collection',
                title: '素材专辑',
                meta: {
                    icon: shallowRef(FileCollection)
                },
                component: () => import('@/pages/CMS/AssetsCollection.vue')
            }
        ]
    },
    // {
    //     path: '/theme',
    //     name: 'theme',
    //     title: '主题运营',
    //     meta: {
    //         requiresAuth: true,
    //         icon: shallowRef(MovieBoard),
    //         pageType: constant.page_type_single
    //     },
    //     children: [
    //         {
    //             path: 'theme',
    //             name: 'theme',
    //             title: '主题',
    //             meta: {
    //                 icon: shallowRef(Movie)
    //             },
    //             component: () => import('@/pages/ECommerce/Theme.vue')
    //         },
    //         {
    //             path: 'ticket',
    //             name: 'ticket',
    //             title: '票务',
    //             meta: {
    //                 icon: shallowRef(TicketOne)
    //             },
    //             component: () => import('@/pages/ECommerce/Ticket.vue')
    //         },
    //         {
    //             path: 'ticket_validation',
    //             name: 'ticket_validation',
    //             title: '票务核销',
    //             meta: {
    //                 icon: shallowRef(System)
    //             },
    //             component: () =>
    //                 import('@/pages/ECommerce/TicketValidation.vue')
    //         },
    //         {
    //             path: 'order',
    //             name: 'order',
    //             title: '订单管理',
    //             meta: {
    //                 icon: shallowRef(Order)
    //             },
    //             component: () => import('@/pages/ECommerce/Order.vue')
    //         }
    //     ]
    // },
    // {
    //     path: '/distribution',
    //     name: 'distribution',
    //     title: '分销',
    //     meta: {
    //         requiresAuth: true,
    //         icon: shallowRef(FinancingTwo),
    //         pageType: constant.page_type_single
    //     },
    //     children: [
    //         {
    //             path: 'goods_rule',
    //             name: 'goods_rule',
    //             title: '商品分销规则',
    //             meta: {
    //                 icon: shallowRef(HeavyMetal)
    //             },
    //             component: () =>
    //                 import('@/pages/distribution/DistributionGoodsRule.vue')
    //         },
    //         {
    //             path: 'salesperson',
    //             name: 'salesperson',
    //             title: '分销资格',
    //             meta: {
    //                 icon: shallowRef(IdCardV)
    //             },
    //             component: () =>
    //                 import('@/pages/distribution/DistributionSalesperson.vue')
    //         }
    //     ]
    // },
    // {
    //     path: '/marketing',
    //     name: 'marketing',
    //     title: '营销活动',
    //     meta: {
    //         requiresAuth: true,
    //         icon: shallowRef(Oceanengine),
    //         pageType: constant.page_type_single
    //     },
    //     children: [
    //         {
    //             path: 'lottery',
    //             name: 'lottery',
    //             title: '抽奖',
    //             meta: {
    //                 icon: shallowRef(GoldMedal)
    //             },
    //             component: () => import('@/pages/MKT/Lottery.vue')
    //         }
    //     ]
    // },
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
            // {
            //     path: 'tenant',
            //     name: 'tenant',
            //     title: '商户管理',
            //     meta: {
            //         icon: shallowRef(UserBusiness)
            //     },
            //     component: () => import('@/pages/base/Tenant.vue')
            // },
            // {
            //     path: 'store',
            //     name: 'store',
            //     title: '门店管理',
            //     meta: {
            //         icon: shallowRef(Shop)
            //     },
            //     component: () => import('@/pages/base/Store.vue')
            // },
            {
                path: 'user',
                name: 'user',
                title: '用户管理',
                meta: {
                    icon: shallowRef(EveryUser)
                },
                component: () => import('@/pages/base/User.vue')
            },
            {
                path: 'department',
                name: 'department',
                title: '部门管理',
                meta: {
                    icon: shallowRef(TreeList)
                },
                component: () => import('@/pages/base/Department.vue')
            }
        ]
    }
    // {
    //     path: '/common',
    //     name: 'common',
    //     title: '其他',
    //     meta: {
    //         requiresAuth: true,
    //         icon: shallowRef(Components),
    //         pageType: constant.page_type_single
    //     },
    //     children: []
    // }
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
            if (Array.isArray(item.children) && item.children.length > 0) {
                defaultPath = item.path + '/' + item.children[0].path;
            } else {
                defaultPath = item.path;
            }
        }

        if (!defaultPath && item.meta?.pageType === constant.page_type_single) {
            if (Array.isArray(item.children) && item.children.length > 0) {
                firstSinglePath = item.path + '/' + item.children[0].path;
            } else {
                firstSinglePath = item.path;
            }
        }
    });

    return defaultPath || firstSinglePath || '/';
};

export { getDefaultRouterPath };
export default routes;

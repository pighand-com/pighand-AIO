import { shallowRef } from 'vue';
import constant from '@/common/constant';
import { Files } from '@element-plus/icons-vue';

export default [
    {
        path: '/',
        redirect: { name: 'login' }
    },
    {
        path: '/login',
        name: 'login',
        title: '登录',
        meta: {
            requiresAuth: false
        },
        components: {
            [constant.page_type_multi]: () => import('@/pages/Login.vue')
        }
    },
    {
        path: '/article',
        name: 'article',
        title: '内容管理',
        meta: {
            default: true,
            requiresAuth: true,
            icon: shallowRef(Files)
        },
        components: {
            [constant.page_type_single]: () => import('@/pages/Article.vue')
        }
    }
];

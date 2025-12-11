import { createRouter, createWebHashHistory } from 'vue-router';

import routes from './routes';
import { getToken } from '@/common/storage';

const router = createRouter({
    linkActiveClass: 'active',
    history: createWebHashHistory(),
    routes: routes as any
});

router.beforeEach((to, _from, next) => {
    const {
        meta: { requiresAuth }
    } = to;

    const token = getToken();

    if (requiresAuth !== false && !token) {
        next({ name: 'login' });
    } else {
        next();
    }
});

export default router;

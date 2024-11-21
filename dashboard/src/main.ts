import { createApp } from 'vue';
import { createStore } from 'vuex';
import ElementPlus from 'element-plus';
import VueDOMPurifyHTML from 'vue-dompurify-html';
import locale from 'element-plus/lib/locale/lang/zh-CN';
import '@/assets/css/globalFont.css';
import 'default-passive-events';

import App from './App.vue';
import router from './routers/index';

import './style.css';
import 'element-plus/dist/index.css';

import TEditor from '@/components/TEditor.vue';
import Search from '@/components/Search.vue';
import Drawer from '@/components/Drawer.vue';
import Dialog from '@/components/Dialog.vue';
import DataTable from '@/components/DataTable.vue';
import ImageView from '@/components/ImageView.vue';
import DataManager from '@/components/DataManager.vue';

const app = createApp(App);
app.use(VueDOMPurifyHTML);
app.use(router);
app.use(ElementPlus, { locale });

// 注入自定义组件
app.component('PEditor', TEditor);
app.component('PSearch', Search);
app.component('PDrawer', Drawer);
app.component('PDialog', Dialog);
app.component('PDataTable', DataTable);
app.component('PImageView', ImageView);
app.component('PDataManager', DataManager);

const store = createStore({
    state() {
        return {};
    },
    mutations: {}
});
app.use(store);

app.mount('#app');

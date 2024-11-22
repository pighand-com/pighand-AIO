export default {
    api: {
        development: 'http://127.0.0.1:3091/',
        production: 'http://127.0.0.1:3091/'
    },
    local_storage_token: 'token',
    local_storage_user_info: 'user_info',
    local_storage_application_info: 'application_info',

    /**
     * @description: 页面类型。single: 单页应用(菜单对应的页面)；multi: 多页应用
     */
    page_type_single: 'single',
    page_type_multi: 'multi',

    role_mapping: {
        admin: '管理员',
        user: '普通用户'
    }
};

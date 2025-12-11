export default {
    api: {
        development: 'http://127.0.0.1:9080/client/',
        production: 'https://aio.pighand.com/client/'
    },
    local_storage_token: 'token',
    local_storage_user_info: 'user_info',
    local_storage_application_info: 'application_info',

    /**
     * @description: 页面类型。single: 单页应用(菜单对应的页面)；multi: 多页应用；mobile: 移动端应用
     */
    page_type_single: 'single',
    page_type_multi: 'multi',
    page_type_mobile: 'mobile',

    role_mapping: {
        admin: '管理员',
        user: '普通用户'
    }
};

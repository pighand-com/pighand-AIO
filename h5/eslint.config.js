module.exports = {
    env: {
        browser: true,
        es2021: true,
        node: true
    },
    extends: [
        'eslint:recommended',
        'plugin:vue/vue3-essential',
        'plugin:prettier/recommended',
        'eslint-config-prettier'
    ],
    parserOptions: {
        ecmaVersion: 'latest', // 使用 'latest' 代替 13
        sourceType: 'module',
        ecmaFeatures: {
            modules: true,
            jsx: true
        },
        requireConfigFile: false,
        parser: '@typescript-eslint/parser'
    },
    plugins: ['vue', 'prettier'],
    rules: {
        'prettier/prettier': 'error',
        'vue/multi-word-component-names': 'off',
        'no-debugger': 'off',
        'no-console': 'off',
        'no-unused-vars': 'off'
    }
};

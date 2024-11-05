<template>
    <div class="body">
        <el-form class="form">
            <h1>登录</h1>
            <el-input
                :disabled="isLoading"
                :class="errorFields.has('username') ? 'el-input-error' : ''"
                v-model="loginForm.username"
                placeholder=""
                :prefix-icon="User"
                @input="handleInput('username')"
                @blur="handleBlur('username')" />
            <el-input
                :disabled="isLoading"
                :class="errorFields.has('password') ? 'el-input-error' : ''"
                type="password"
                v-model="loginForm.password"
                placeholder=""
                :prefix-icon="Lock"
                show-password
                @input="handleInput('password')"
                @blur="handleBlur('password')" />
            <div v-if="showCaptcha" class="captcha-container">
                <el-input
                    :disabled="isLoading"
                    :class="errorFields.has('captcha') ? 'el-input-error' : ''"
                    v-model="loginForm.captcha"
                    placeholder="请输入验证码"
                    @input="handleInput('captcha')"
                    @blur="handleBlur('captcha')" />
                <img
                    :src="captchaImage"
                    alt="验证码"
                    @click="refreshCaptcha(true)"
                    class="captcha-image" />
            </div>
            <el-button
                @click="login"
                plain
                :loading="isLoading"
                :disabled="isLoading"
                >{{ isLoading ? '登录中...' : '登录' }}</el-button
            >
        </el-form>
    </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onBeforeMount } from 'vue';
import { User, Lock } from '@element-plus/icons-vue';
import { user, common } from '@/api';
import { setToken, setUserInfo, getToken } from '@/common/storage';
import constant from '@/common/constant';
import routes from '@/routers/routes';
import router from '@/routers/index';

const errorFields = ref(new Set());
const loginForm = reactive({
    username: '',
    password: '',
    captcha: '',
    captchaId: ''
});

const captchaImage = ref('');
const showCaptcha = ref(false);

const isLoading = ref(false);

onBeforeMount(() => {
    if (getToken()) {
        router.push(getDefaultRouterPath());
    }

    refreshCaptcha();
});

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

        if (
            !defaultPath &&
            Object.keys(item.components || {}).includes(
                constant.page_type_single
            )
        ) {
            firstSinglePath = item.path;
        }
    });
    debugger;
    return defaultPath || firstSinglePath || '/';
};

const handleInput = (key: string) => {
    errorFields.value.delete(key);
};

const handleBlur = (key: string) => {
    if (!loginForm[key]) {
        errorFields.value.add(key);
    }

    if (key === 'username') {
        if (loginForm.username) {
            showCaptcha.value = true;
            refreshCaptcha();
        } else {
            showCaptcha.value = false;
        }
    }
};

const refreshCaptcha = async (force?: boolean) => {
    if (!force && (!loginForm.username || captchaImage.value)) return;

    const result = await common.getCAPTCHACode(loginForm.username);
    if (result && result.base64) {
        captchaImage.value = result.base64;
        loginForm.captchaId = result.captchaId;
    }
};

const login = async () => {
    let isRequired = true;
    Object.keys(loginForm).forEach((key) => {
        if (!loginForm[key]) {
            errorFields.value.add(key);
            isRequired = false;
        }
    });

    if (isRequired) {
        isLoading.value = true;
        try {
            const result = await user.login(loginForm);

            if (result) {
                setToken(result.token);
                setUserInfo({
                    id: result.id,
                    username: result.username,
                    role: result.role
                });
                router.push(getDefaultRouterPath());
            }
        } finally {
            isLoading.value = false;
        }
    }
};
</script>

<style lang="scss" scoped>
.body {
    background: linear-gradient(-45deg, #ee7752, #e73c7e, #23a6d5, #23d5ab);
    background-size: 400% 400%;
    animation: gradient 15s ease infinite;
    height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
}

.form {
    width: 264px;
    color: #f8f9fa;

    display: flex;
    flex-direction: column;
    align-items: center;
}

.el-input {
    padding-bottom: 18px;
    :deep(.el-input__wrapper) {
        background-color: transparent;
        box-shadow: none;
        border: 1px solid #f8f9fa;
    }

    :deep(.el-input__icon) {
        color: #f8f9fa;
    }
}

.el-input-error {
    :deep(.el-input__wrapper) {
        border: 1px solid #cb3030;
        box-shadow: 0 0 5px rgba(255, 0, 0, 0.5);
    }
}

:deep(.el-input input) {
    color: #fff;
}

.el-button {
    width: 264px;
    color: #f8f9fa;
    background-color: transparent;
    border: 1px solid #f8f9fa;

    &:hover {
        color: #212529;
        background-color: #f8f9fa;
    }
}

.captcha-container {
    display: flex;
    align-items: flex-start;
    margin-bottom: 18px;

    .el-input {
        flex: 1;
        margin-right: 10px;
    }

    .captcha-image {
        cursor: pointer;
        height: 34px;
        border-radius: 4px;
    }
}

@keyframes gradient {
    0% {
        background-position: 0% 50%;
    }
    50% {
        background-position: 100% 50%;
    }
    100% {
        background-position: 0% 50%;
    }
}
</style>

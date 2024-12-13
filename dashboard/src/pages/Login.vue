<template>
    <div class="body">
        <div class="gradient-bg">
            <div class="gradients-container">
                <div class="g1"></div>
                <div class="g2"></div>
                <div class="g3"></div>
                <div class="g4"></div>
                <div class="g5"></div>
            </div>
        </div>
        <el-form class="form" @keyup.enter="login">
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
                    ref="captchaInputRef"
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
import { User, Lock } from '@icon-park/vue-next';
import { user, common } from '@/api';
import { setToken, setUserInfo, getToken } from '@/common/storage';
import { getDefaultRouterPath } from '@/routers/routes';
import router from '@/routers/index';

const errorFields = ref(new Set());
const loginForm = reactive({
    username: '',
    password: '',
    captcha: '',
    captchaId: ''
});

const captchaImage = ref('');
const captchaInputRef = ref(null);
const showCaptcha = ref(false);

const isLoading = ref(false);

onBeforeMount(() => {
    if (getToken()) {
        router.push(getDefaultRouterPath());
    }

    refreshCaptcha();
});

const handleInput = (key: string) => {
    errorFields.value.delete(key);

    if (
        loginForm.captcha &&
        loginForm.captcha.length === 4 &&
        loginForm.username &&
        loginForm.password
    ) {
        login();
    }
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
        if (force) {
            setTimeout(() => {
                captchaInputRef.value?.input?.focus();
            }, 0);
        }
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
        const result = await user.login(loginForm);

        if (result) {
            setToken(result.token);
            setUserInfo({
                id: result.id,
                username: result.username,
                role: result.role
            });

            const defaultPath = getDefaultRouterPath();
            router.replace(defaultPath);
        } else {
            loginForm.captcha = '';
            refreshCaptcha(true);
        }

        isLoading.value = false;
    }
};
</script>

<style lang="scss" scoped>
.body {
    width: 100dvw;
    height: 100dvh;
    display: flex;
    align-items: center;
    justify-content: center;
}

.gradient-bg {
    --color-bg1: #dff9fb;
    --color-bg2: #ffffff;
    --color1: 104, 109, 224;
    --color2: 126, 214, 223;
    --color3: 223, 249, 251;
    --color4: 255, 121, 121;
    --color5: 149, 175, 192;
    --circle-size: 200%;
    --blending: hard-light;

    width: 100vw;
    height: 100vh;
    overflow: hidden;
    background: linear-gradient(40deg, var(--color-bg1), var(--color-bg2));
    top: 0;
    left: 0;

    .gradients-container {
        filter: url(#goo) blur(40px);
        width: 100%;
        height: 100%;
    }

    .g1 {
        position: absolute;
        background: radial-gradient(
                circle at center,
                rgba(var(--color1), 0.8) 0,
                rgba(var(--color1), 0) 50%
            )
            no-repeat;
        mix-blend-mode: var(--blending);

        width: var(--circle-size);
        height: var(--circle-size);
        top: calc(50% - var(--circle-size) / 2);
        left: calc(50% - var(--circle-size) / 2);

        transform-origin: center center;
        animation: moveVertical 30s ease infinite;

        opacity: 1;
    }

    .g2 {
        position: absolute;
        background: radial-gradient(
                circle at center,
                rgba(var(--color2), 0.8) 0,
                rgba(var(--color2), 0) 50%
            )
            no-repeat;
        mix-blend-mode: var(--blending);

        width: var(--circle-size);
        height: var(--circle-size);
        top: calc(50% - var(--circle-size) / 2);
        left: calc(50% - var(--circle-size) / 2);

        transform-origin: calc(50% - 400px);
        animation: moveInCircle 20s reverse infinite;

        opacity: 1;
    }

    .g3 {
        position: absolute;
        background: radial-gradient(
                circle at center,
                rgba(var(--color3), 0.8) 0,
                rgba(var(--color3), 0) 50%
            )
            no-repeat;
        mix-blend-mode: var(--blending);

        width: var(--circle-size);
        height: var(--circle-size);
        top: calc(50% - var(--circle-size) / 2 + 200px);
        left: calc(50% - var(--circle-size) / 2 - 500px);

        transform-origin: calc(50% + 400px);
        animation: moveInCircle 40s linear infinite;

        opacity: 1;
    }

    .g4 {
        position: absolute;
        background: radial-gradient(
                circle at center,
                rgba(var(--color4), 0.8) 0,
                rgba(var(--color4), 0) 50%
            )
            no-repeat;
        mix-blend-mode: var(--blending);

        width: var(--circle-size);
        height: var(--circle-size);
        top: calc(50% - var(--circle-size) / 2);
        left: calc(50% - var(--circle-size) / 2);

        transform-origin: calc(50% - 200px);
        animation: moveHorizontal 40s ease infinite;

        opacity: 0.7;
    }

    .g5 {
        position: absolute;
        background: radial-gradient(
                circle at center,
                rgba(var(--color5), 0.8) 0,
                rgba(var(--color5), 0) 50%
            )
            no-repeat;
        mix-blend-mode: var(--blending);

        width: calc(var(--circle-size) * 2);
        height: calc(var(--circle-size) * 2);
        top: calc(50% - var(--circle-size));
        left: calc(50% - var(--circle-size));

        transform-origin: calc(50% - 800px) calc(50% + 200px);
        animation: moveInCircle 20s ease infinite;

        opacity: 1;
    }
}

.form {
    width: 264px;
    color: var(--p-color-dark);
    display: flex;
    flex-direction: column;
    align-items: center;
    position: absolute;

    .el-input {
        padding-bottom: 18px;

        :deep(.el-input__wrapper) {
            background-color: transparent;
            box-shadow: none;
            border: 1px solid var(--p-color-dark);
        }

        :deep(.el-input__icon) {
            color: var(--p-color-dark);
        }

        :deep(input) {
            color: rgb(80, 80, 80);
        }

        &.el-input-error {
            :deep(.el-input__wrapper) {
                border: 1px solid #cb3030;
                box-shadow: 0 0 5px rgba(255, 0, 0, 0.5);
            }
        }
    }

    .el-button {
        width: 264px;
        color: var(--p-color-dark);
        background-color: transparent;
        border: 1px solid var(--p-color-dark);

        &:hover {
            color: var(--p-color-white);
            background-color: var(--p-color-dark);
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
}

@keyframes moveInCircle {
    0% {
        transform: rotate(0deg);
    }
    50% {
        transform: rotate(180deg);
    }
    100% {
        transform: rotate(360deg);
    }
}

@keyframes moveVertical {
    0% {
        transform: translateY(-50%);
    }
    50% {
        transform: translateY(50%);
    }
    100% {
        transform: translateY(-50%);
    }
}

@keyframes moveHorizontal {
    0% {
        transform: translateX(-50%) translateY(-10%);
    }
    50% {
        transform: translateX(50%) translateY(10%);
    }
    100% {
        transform: translateX(-50%) translateY(-10%);
    }
}
</style>

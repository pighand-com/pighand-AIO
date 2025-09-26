<template>
    <div class="body">
        <!-- SVG滤镜定义 -->
        <svg style="position: absolute; width: 0; height: 0;">
            <defs>
                <filter id="goo">
                    <feGaussianBlur in="SourceGraphic" stdDeviation="10" result="blur" />
                    <feColorMatrix in="blur" mode="matrix" values="1 0 0 0 0  0 1 0 0 0  0 0 1 0 0  0 0 0 18 -8" result="goo" />
                    <feBlend in="SourceGraphic" in2="goo" />
                </filter>
            </defs>
        </svg>
        
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
            <el-input :disabled="isLoading" :class="errorFields.has('username') ? 'el-input-error' : ''"
                v-model="loginForm.username" placeholder="" :prefix-icon="User" @input="handleInput('username')"
                @blur="handleBlur('username')" />
            <el-input :disabled="isLoading" :class="errorFields.has('password') ? 'el-input-error' : ''" type="password"
                v-model="loginForm.password" placeholder="" :prefix-icon="Lock" show-password
                @input="handleInput('password')" @blur="handleBlur('password')" />
            <div v-if="showCaptcha" class="captcha-container">
                <el-input ref="captchaInputRef" :disabled="isLoading"
                    :class="errorFields.has('captcha') ? 'el-input-error' : ''" v-model="loginForm.captcha"
                    placeholder="请输入验证码" @input="handleInput('captcha')" @blur="handleBlur('captcha')" />
                <img :src="captchaImage" alt="验证码" @click="refreshCaptcha(true)" class="captcha-image" />
            </div>
            <el-button @click="login" plain :loading="isLoading" :disabled="isLoading">{{ isLoading ? '登录中...' : '登录'
                }}</el-button>
        </el-form>
    </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onBeforeMount } from 'vue';
import { User, Lock } from '@icon-park/vue-next';
import * as API from '@/api';
import { setToken, setUserInfo, getToken, setApplicationInfo } from '@/common/storage';
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

    const result = await API.common.getCAPTCHACode(loginForm.username);
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
        const result = await API.login.login(loginForm);

        if (result) {
            setToken(result.token);
            setUserInfo({
                id: result.id,
                username: result.username,
                role: result.role,
                extension: result.extension
            });

            setApplicationInfo(result.application);

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
    width: 100vw;
    height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 20px;
    box-sizing: border-box;
    // 添加移动端安全区域适配
    padding-top: max(20px, env(safe-area-inset-top));
    padding-bottom: max(20px, env(safe-area-inset-bottom));
}

.gradient-bg {
    --color-bg1: #dff9fb;
    --color-bg2: #ffffff;
    --color1: 104, 109, 224;
    --color2: 126, 214, 223;
    --color3: 223, 249, 251;
    --color4: 255, 121, 121;
    --color5: 149, 175, 192;
    --circle-size: 150%; // 减小动画元素尺寸，优化移动端性能
    --blending: hard-light;

    width: 100vw;
    height: 100vh;
    overflow: hidden;
    background: linear-gradient(40deg, var(--color-bg1), var(--color-bg2));
    position: fixed;
    top: 0;
    left: 0;
    z-index: -1;

    .gradients-container {
        filter: url(#goo) blur(30px); // 使用SVG滤镜和模糊效果
        width: 100%;
        height: 100%;
    }

    .g1 {
        position: absolute;
        background: radial-gradient(circle at center,
                rgba(var(--color1), 0.6) 0,
                rgba(var(--color1), 0) 50%) no-repeat;
        mix-blend-mode: var(--blending);

        width: var(--circle-size);
        height: var(--circle-size);
        top: calc(50% - var(--circle-size) / 2);
        left: calc(50% - var(--circle-size) / 2);

        transform-origin: center center;
        animation: moveVertical 30s ease infinite;

        opacity: 0.8;
    }

    .g2 {
        position: absolute;
        background: radial-gradient(circle at center,
                rgba(var(--color2), 0.6) 0,
                rgba(var(--color2), 0) 50%) no-repeat;
        mix-blend-mode: var(--blending);

        width: var(--circle-size);
        height: var(--circle-size);
        top: calc(50% - var(--circle-size) / 2);
        left: calc(50% - var(--circle-size) / 2);

        transform-origin: calc(50% - 200px); // 调整移动端适配
        animation: moveInCircle 20s reverse infinite;

        opacity: 0.8;
    }

    .g3 {
        position: absolute;
        background: radial-gradient(circle at center,
                rgba(var(--color3), 0.6) 0,
                rgba(var(--color3), 0) 50%) no-repeat;
        mix-blend-mode: var(--blending);

        width: var(--circle-size);
        height: var(--circle-size);
        top: calc(50% - var(--circle-size) / 2 + 100px);
        left: calc(50% - var(--circle-size) / 2 - 200px);

        transform-origin: calc(50% + 200px);
        animation: moveInCircle 40s linear infinite;

        opacity: 0.8;
    }

    .g4 {
        position: absolute;
        background: radial-gradient(circle at center,
                rgba(var(--color4), 0.6) 0,
                rgba(var(--color4), 0) 50%) no-repeat;
        mix-blend-mode: var(--blending);

        width: var(--circle-size);
        height: var(--circle-size);
        top: calc(50% - var(--circle-size) / 2);
        left: calc(50% - var(--circle-size) / 2);

        transform-origin: calc(50% - 100px);
        animation: moveHorizontal 40s ease infinite;

        opacity: 0.6;
    }

    .g5 {
        position: absolute;
        background: radial-gradient(circle at center,
                rgba(var(--color5), 0.6) 0,
                rgba(var(--color5), 0) 50%) no-repeat;
        mix-blend-mode: var(--blending);

        width: calc(var(--circle-size) * 1.5);
        height: calc(var(--circle-size) * 1.5);
        top: calc(50% - var(--circle-size) * 0.75);
        left: calc(50% - var(--circle-size) * 0.75);

        transform-origin: calc(50% - 300px) calc(50% + 100px);
        animation: moveInCircle 20s ease infinite;

        opacity: 0.8;
    }
}

.form {
    width: 100%;
    max-width: 320px; // 移动端适配的最大宽度
    min-width: 280px; // 最小宽度保证在小屏幕上的可用性
    color: var(--p-color-dark);
    display: flex;
    flex-direction: column;
    align-items: center;
    position: relative;
    z-index: 1;

    h1 {
        font-size: 28px;
        font-weight: 600;
        margin-bottom: 32px;
        text-align: center;
        
        // 移动端字体大小适配
        @media (max-width: 375px) {
            font-size: 24px;
            margin-bottom: 24px;
        }
    }

    .el-input {
        width: 100%;
        margin-bottom: 20px; // 增加间距，适配移动端触摸

        :deep(.el-input__wrapper) {
            background-color: rgba(255, 255, 255, 0.9);
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            border: 1px solid rgba(0, 0, 0, 0.15);
            border-radius: 8px;
            height: 48px; // 增加高度，适配移动端触摸
            padding: 0 16px;
            transition: all 0.3s ease;
            
            &:hover, &:focus-within {
                border-color: var(--p-color-dark);
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
            }
        }

        :deep(.el-input__icon) {
            color: var(--p-color-dark);
            font-size: 18px;
        }

        :deep(input) {
            color: rgb(60, 60, 60);
            font-size: 16px; // 防止iOS缩放
            line-height: 1.5;
            
            &::placeholder {
                color: rgba(60, 60, 60, 0.6);
            }
        }

        &.el-input-error {
            :deep(.el-input__wrapper) {
                border: 1px solid #ff4757;
                box-shadow: 0 0 8px rgba(255, 71, 87, 0.3);
                background-color: rgba(255, 245, 245, 0.9);
            }
        }
    }

    .el-button {
        width: 100%;
        height: 48px; // 增加按钮高度，适配移动端触摸
        color: var(--p-color-white);
        background: linear-gradient(135deg, var(--p-color-dark), #5a5a5a);
        border: none;
        border-radius: 8px;
        font-size: 16px;
        font-weight: 500;
        margin-top: 8px;
        transition: all 0.3s ease;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);

        &:hover, &:active {
            transform: translateY(-1px);
            box-shadow: 0 6px 16px rgba(0, 0, 0, 0.2);
            background: linear-gradient(135deg, #4a4a4a, var(--p-color-dark));
        }

        &:disabled {
            opacity: 0.6;
            transform: none;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        }
        
        // 移动端触摸反馈
        @media (hover: none) {
            &:active {
                transform: scale(0.98);
            }
        }
    }

    .captcha-container {
        width: 100%;
        display: flex;
        align-items: flex-start;
        gap: 12px; // 使用gap替代margin
        margin-bottom: 20px;

        .el-input {
            flex: 1;
            margin-bottom: 0;
        }

        .captcha-image {
            cursor: pointer;
            height: 48px; // 与输入框高度保持一致
            min-width: 80px;
            border-radius: 8px;
            border: 1px solid rgba(0, 0, 0, 0.15);
            background-color: rgba(255, 255, 255, 0.9);
            transition: all 0.3s ease;
            
            &:hover, &:active {
                border-color: var(--p-color-dark);
                box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            }
            
            // 移动端触摸反馈
            @media (hover: none) {
                &:active {
                    transform: scale(0.95);
                }
            }
        }
    }
}

// 移动端横屏适配
@media (orientation: landscape) and (max-height: 500px) {
    .body {
        padding: 10px;
    }
    
    .form {
        h1 {
            font-size: 20px;
            margin-bottom: 16px;
        }
        
        .el-input {
            margin-bottom: 12px;
            
            :deep(.el-input__wrapper) {
                height: 40px;
            }
        }
        
        .el-button {
            height: 40px;
        }
        
        .captcha-container {
            .captcha-image {
                height: 40px;
            }
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

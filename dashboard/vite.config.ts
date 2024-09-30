import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import * as path from 'path';
import sass from 'sass';

// https://vitejs.dev/config/
export default defineConfig({
    base: '/dashboard/',
    resolve: {
        alias: {
            '@': path.resolve(__dirname, 'src')
        }
    },
    plugins: [vue()],
    css: {
        preprocessorOptions: {
            sass: {
                implementation: sass
            }
        }
    }
});

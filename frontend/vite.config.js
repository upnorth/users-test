import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import { resolve } from 'path';

export default defineConfig({
  plugins: [vue()],
  build: {
    outDir: resolve(__dirname, '../src/main/resources/META-INF/resources'),
    emptyOutDir: true
  },
  server: {
    port: 5173,
    proxy: {
      '/digg': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      '/health': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      '/q': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
});

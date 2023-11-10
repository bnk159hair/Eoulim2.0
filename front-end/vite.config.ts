import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react-swc';
import tsconfigPaths from 'vite-tsconfig-paths';
import checker from 'vite-plugin-checker';
import reactRefresh from '@vitejs/plugin-react-refresh';
import * as path from 'node:path';
import dotenv from 'dotenv'; // dotenv 라이브러리 추가

// .env 파일을 로드
dotenv.config();

export default defineConfig({
  build: {
    outDir: 'build',
  },
  plugins: [
    react(),
    tsconfigPaths(),
    checker({
      typescript: true,
    }),
    reactRefresh(),
  ],
  server: {
    open: true,
    port: 3000,
  },
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
});

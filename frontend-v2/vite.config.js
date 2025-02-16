import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  server: {
    port: 3000,
    proxy: {
      '/textile': {
        target: 'http://localhost:8082',
        changeOrigin: true, // Ensures the origin of the host header matches the target
        secure: false, // If using HTTPS with self-signed certificates
        rewrite: (path) => path.replace(/^\/textile/, '/textile'),
      },
    },
  },
});

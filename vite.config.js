import { resolve } from 'path'
import { defineConfig } from 'vite'
import handlebars from 'vite-plugin-handlebars'

export default defineConfig({
  base: '/',
  publicDir: 'public',
  plugins: [
    handlebars({
      partialDirectory: resolve(__dirname, 'partials'),
    })
  ],
  build: {
    rollupOptions: {
      input: {
        main: resolve(__dirname, 'index.html'),
        '01': resolve(__dirname, 'examples', '01.html'),
        '02': resolve(__dirname, 'examples', '02.html'),
      }
    }
  }
})

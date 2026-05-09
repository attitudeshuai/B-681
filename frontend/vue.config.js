module.exports = {
  lintOnSave: false,
  devServer: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://backend:8080',
        changeOrigin: true
      }
    }
  }
}

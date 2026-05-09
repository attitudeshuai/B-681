import axios from 'axios'
import Cookies from 'js-cookie'

// 创建axios实例
const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    // 添加 token
    const token = Cookies.get('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    console.error('Request error:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    console.error('Response error:', error)
    
    let message = '请求失败'
    
    if (error.response) {
      // 401 未授权，跳转登录页
      if (error.response.status === 401) {
        Cookies.remove('token')
        Cookies.remove('user')
        if (window.location.pathname !== '/login') {
          window.location.href = '/login'
        }
        return Promise.reject(error)
      }
      
      message = error.response.data.message || `Error: ${error.response.status}`
    } else if (error.request) {
      message = '网络连接失败，请检查网络'
    } else {
      message = error.message
    }
    
    // Element UI toast notification
    if (window.$message) {
      window.$message.error(message)
    }
    
    return Promise.reject(error)
  }
)

export default request

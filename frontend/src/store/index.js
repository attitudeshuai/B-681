import Vue from 'vue'
import Vuex from 'vuex'
import Cookies from 'js-cookie'
import { login, getCurrentUser } from '../api/auth'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    token: Cookies.get('token') || '',
    user: JSON.parse(Cookies.get('user') || 'null')
  },
  
  mutations: {
    SET_TOKEN(state, token) {
      state.token = token
      if (token) {
        Cookies.set('token', token, { expires: 7 })
      } else {
        Cookies.remove('token')
      }
    },
    
    SET_USER(state, user) {
      state.user = user
      if (user) {
        Cookies.set('user', JSON.stringify(user), { expires: 7 })
      } else {
        Cookies.remove('user')
      }
    },
    
    LOGOUT(state) {
      state.token = ''
      state.user = null
      Cookies.remove('token')
      Cookies.remove('user')
    }
  },
  
  actions: {
    // 登录
    async login({ commit }, credentials) {
      try {
        const response = await login(credentials)
        if (response.success) {
          commit('SET_TOKEN', response.token)
          commit('SET_USER', response.user)
          return response
        }
        return response
      } catch (error) {
        throw error
      }
    },
    
    // 获取用户信息
    async getUserInfo({ commit }) {
      try {
        const response = await getCurrentUser()
        if (response.success) {
          commit('SET_USER', response.data)
        }
        return response
      } catch (error) {
        commit('LOGOUT')
        throw error
      }
    },
    
    // 退出登录
    logout({ commit }) {
      commit('LOGOUT')
    }
  },
  
  getters: {
    isLoggedIn: state => !!state.token,
    isAdmin: state => state.user && state.user.role === 'ADMIN'
  }
})

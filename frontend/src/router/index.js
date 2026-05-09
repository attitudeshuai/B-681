import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '../store'

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    component: () => import('../views/Layout.vue'),
    redirect: '/dashboard',
    meta: { requiresAuth: true },
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue'),
        meta: { title: '工作台' }
      },
      {
        path: 'students',
        name: 'Students',
        component: () => import('../views/StudentList.vue'),
        meta: { title: '学生管理' }
      },
      {
        path: 'users',
        name: 'Users',
        component: () => import('../views/UserList.vue'),
        meta: { title: '用户管理', requiresAdmin: true }
      }
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const isLoggedIn = store.getters.isLoggedIn
  
  if (to.meta.requiresAuth) {
    if (!isLoggedIn) {
      next('/login')
    } else if (to.meta.requiresAdmin && !store.getters.isAdmin) {
      next('/dashboard')
    } else {
      next()
    }
  } else {
    if (to.path === '/login' && isLoggedIn) {
      next('/dashboard')
    } else {
      next()
    }
  }
})

export default router

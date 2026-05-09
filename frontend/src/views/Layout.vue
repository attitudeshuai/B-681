<template>
  <div class="layout-container">
    <!-- 侧边栏 -->
    <div class="sidebar">
      <div class="logo">
        <i class="el-icon-school"></i>
        <span>学生管理系统</span>
      </div>
      
      <el-menu
        :default-active="$route.path"
        class="sidebar-menu"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
        router
      >
        <el-menu-item index="/dashboard">
          <i class="el-icon-s-home"></i>
          <span>工作台</span>
        </el-menu-item>
        
        <el-menu-item index="/students">
          <i class="el-icon-user"></i>
          <span>学生管理</span>
        </el-menu-item>
        
        <el-menu-item v-if="isAdmin" index="/users">
          <i class="el-icon-setting"></i>
          <span>用户管理</span>
        </el-menu-item>
      </el-menu>
    </div>

    <!-- 主内容区 -->
    <div class="main-container">
      <!-- 顶部栏 -->
      <div class="navbar">
        <div class="navbar-left">
          <span class="page-title">{{ pageTitle }}</span>
        </div>
        
        <div class="navbar-right">
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              <el-avatar :size="32" icon="el-icon-user-solid"></el-avatar>
              <span class="username">{{ user ? user.username : '' }}</span>
              <i class="el-icon-arrow-down"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item disabled>{{ user ? user.role : '' }}</el-dropdown-item>
              <el-dropdown-item divided command="logout">
                <i class="el-icon-switch-button"></i> 退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>

      <!-- 内容区域 -->
      <div class="content-wrapper">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script>
import { mapState, mapGetters } from 'vuex'

export default {
  name: 'Layout',
  
  computed: {
    ...mapState(['user']),
    ...mapGetters(['isAdmin']),
    
    pageTitle() {
      return this.$route.meta.title || '学生管理系统'
    }
  },
  
  methods: {
    handleCommand(command) {
      if (command === 'logout') {
        this.$confirm('确定退出登录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$store.dispatch('logout')
          this.$router.push('/login')
          this.$message.success('已退出登录')
        }).catch(() => {})
      }
    }
  }
}
</script>

<style scoped>
.layout-container {
  display: flex;
  min-height: 100vh;
  background: #f0f2f5;
}

/* 侧边栏 */
.sidebar {
  width: 200px;
  background: #304156;
  box-shadow: 2px 0 6px rgba(0, 0, 0, 0.1);
  position: fixed;
  left: 0;
  top: 0;
  bottom: 0;
  overflow-y: auto;
  z-index: 1000;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 18px;
  font-weight: 600;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.logo i {
  font-size: 24px;
  margin-right: 10px;
}

.sidebar-menu {
  border-right: none;
}

.sidebar-menu .el-menu-item {
  height: 50px;
  line-height: 50px;
  transition: all 0.3s;
}

.sidebar-menu .el-menu-item i {
  margin-right: 10px;
  font-size: 16px;
}

.sidebar-menu .el-menu-item.is-active {
  background: rgba(64, 158, 255, 0.2) !important;
}

/* 主内容区 */
.main-container {
  flex: 1;
  margin-left: 200px;
  display: flex;
  flex-direction: column;
}

/* 顶部栏 */
.navbar {
  height: 60px;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
  padding: 0 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  position: sticky;
  top: 0;
  z-index: 999;
}

.page-title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 5px 10px;
  border-radius: 4px;
  transition: background 0.3s;
}

.user-info:hover {
  background: #f5f7fa;
}

.username {
  margin: 0 8px 0 10px;
  font-size: 14px;
  color: #606266;
}

/* 内容区域 */
.content-wrapper {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
}

/* 响应式 */
@media (max-width: 768px) {
  .sidebar {
    width: 64px;
  }
  
  .logo span {
    display: none;
  }
  
  .sidebar-menu .el-menu-item span {
    display: none;
  }
  
  .main-container {
    margin-left: 64px;
  }
}
</style>

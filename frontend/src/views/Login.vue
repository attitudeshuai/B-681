<template>
  <div class="login-container">
    <div class="login-box">
      <h1 class="login-title">学生管理系统</h1>
      <el-tabs v-model="activeTab" class="login-tabs">
        <!-- 登录 -->
        <el-tab-pane label="登录" name="login">
          <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
            <el-form-item prop="username">
              <el-input v-model="loginForm.username" placeholder="用户名" prefix-icon="el-icon-user" clearable />
            </el-form-item>
            <el-form-item prop="password">
              <el-input v-model="loginForm.password" type="password" placeholder="密码" prefix-icon="el-icon-lock" clearable @keyup.enter.native="handleLogin" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :loading="loading" class="login-btn" @click="handleLogin">登录</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 注册 -->
        <el-tab-pane label="注册" name="register">
          <el-form ref="registerForm" :model="registerForm" :rules="registerRules" class="login-form">
            <el-form-item prop="username">
              <el-input v-model="registerForm.username" placeholder="用户名" prefix-icon="el-icon-user" clearable />
            </el-form-item>
            <el-form-item prop="email">
              <el-input v-model="registerForm.email" placeholder="邮箱" prefix-icon="el-icon-message" clearable />
            </el-form-item>
            <el-form-item prop="password">
              <el-input v-model="registerForm.password" type="password" placeholder="密码" prefix-icon="el-icon-lock" clearable />
            </el-form-item>
            <el-form-item prop="confirmPassword">
              <el-input v-model="registerForm.confirmPassword" type="password" placeholder="确认密码" prefix-icon="el-icon-lock" clearable @keyup.enter.native="handleRegister" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :loading="loading" class="login-btn" @click="handleRegister">注册</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import { register } from '../api/auth'

export default {
  name: 'Login',
  data() {
    const validateConfirmPassword = (rule, value, callback) => {
      if (value !== this.registerForm.password) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }

    return {
      activeTab: 'login',
      loading: false,
      loginForm: {
        username: '',
        password: ''
      },
      registerForm: {
        username: '',
        email: '',
        password: '',
        confirmPassword: ''
      },
      loginRules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      },
      registerRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, message: '密码至少6个字符', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请确认密码', trigger: 'blur' },
          { validator: validateConfirmPassword, trigger: 'blur' }
        ]
      }
    }
  },

  methods: {
    handleLogin() {
      this.$refs.loginForm.validate(async valid => {
        if (!valid) return

        this.loading = true
        try {
          await this.$store.dispatch('login', this.loginForm)
          this.$message.success('登录成功')
          this.$router.push('/dashboard')
        } catch (error) {
          console.error('登录失败:', error)
        } finally {
          this.loading = false
        }
      })
    },

    handleRegister() {
      this.$refs.registerForm.validate(async valid => {
        if (!valid) return

        this.loading = true
        try {
          const response = await register(this.registerForm)
          if (response.success) {
            this.$message.success('注册成功，请登录')
            this.activeTab = 'login'
            this.loginForm.username = this.registerForm.username
          }
        } catch (error) {
          console.error('注册失败:', error)
        } finally {
          this.loading = false
        }
      })
    }
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.login-box {
  width: 100%;
  max-width: 450px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 40px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.login-title {
  font-size: 28px;
  font-weight: 700;
  text-align: center;
  margin-bottom: 30px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.login-tabs >>> .el-tabs__nav-wrap::after {
  display: none;
}

.login-form {
  margin-top: 20px;
}

.login-form >>> .el-input__inner {
  height: 45px;
  line-height: 45px;
  border-radius: 10px;
}

.login-btn {
  width: 100%;
  height: 45px;
  border-radius: 10px;
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
}

.login-btn:hover {
  opacity: 0.9;
}

@media (max-width: 768px) {
  .login-box {
    padding: 30px 20px;
  }
}
</style>

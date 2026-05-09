<template>
  <div class="dashboard">
    <h2 class="page-header">工作台</h2>
    
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card">
          <div class="stat-icon" style="background: #409EFF;">
            <i class="el-icon-user"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.totalStudents }}</div>
            <div class="stat-label">学生总数</div>
          </div>
        </div>
      </el-col>
      
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card">
          <div class="stat-icon" style="background: #67C23A;">
            <i class="el-icon-user-solid"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.totalUsers }}</div>
            <div class="stat-label">用户总数</div>
          </div>
        </div>
      </el-col>
      
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card">
          <div class="stat-icon" style="background: #E6A23C;">
            <i class="el-icon-school"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.totalClasses }}</div>
            <div class="stat-label">班级数量</div>
          </div>
        </div>
      </el-col>
      
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card">
          <div class="stat-icon" style="background: #F56C6C;">
            <i class="el-icon-s-promotion"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">100%</div>
            <div class="stat-label">系统运行</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 快捷入口 -->
    <div class="quick-actions">
      <h3>快捷操作</h3>
      <el-row :gutter="20">
        <el-col :xs="12" :sm="8" :md="6">
          <div class="action-card" @click="$router.push('/students')">
            <i class="el-icon-plus"></i>
            <span>添加学生</span>
          </div>
        </el-col>
        <el-col :xs="12" :sm="8" :md="6">
          <div class="action-card" @click="$router.push('/students')">
            <i class="el-icon-search"></i>
            <span>查询学生</span>
          </div>
        </el-col>
        <el-col :xs="12" :sm="8" :md="6" v-if="isAdmin">
          <div class="action-card" @click="$router.push('/users')">
            <i class="el-icon-setting"></i>
            <span>用户管理</span>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { getStudents } from '../api/student'
import { getUsers } from '../api/user'
import { mapGetters } from 'vuex'

export default {
  name: 'Dashboard',
  
  data() {
    return {
      stats: {
        totalStudents: 0,
        totalUsers: 0,
        totalClasses: 0
      }
    }
  },
  
  computed: {
    ...mapGetters(['isAdmin'])
  },
  
  mounted() {
    this.fetchStats()
  },
  
  methods: {
    async fetchStats() {
      try {
        // 获取学生统计
        const studentsRes = await getStudents()
        if (studentsRes.success) {
          this.stats.totalStudents = studentsRes.total || 0
          
          // 统计班级数量（去重）
          const classes = new Set(studentsRes.data.map(s => s.className))
          this.stats.totalClasses = classes.size
        }
        
        // 如果是管理员，获取用户统计
        if (this.isAdmin) {
          const usersRes = await getUsers()
          if (usersRes.success) {
            this.stats.totalUsers = usersRes.total || 0
          }
        }
      } catch (error) {
        console.error('获取统计数据失败:', error)
      }
    }
  }
}
</script>

<style scoped>
.dashboard {
  max-width: 1400px;
}

.page-header {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 20px;
}

.stats-row {
  margin-bottom: 30px;
}

.stat-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  align-items: center;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  transition: transform 0.3s, box-shadow 0.3s;
  cursor: pointer;
  margin-bottom: 20px;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
}

.stat-icon i {
  font-size: 28px;
  color: #fff;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #303133;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.quick-actions {
  background: #fff;
  border-radius: 12px;
  padding: 25px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

.quick-actions h3 {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 20px;
}

.action-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  padding: 30px 20px;
  text-align: center;
  color: #fff;
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
  margin-bottom: 20px;
}

.action-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.4);
}

.action-card i {
  font-size: 36px;
  display: block;
  margin-bottom: 10px;
}

.action-card span {
  font-size: 16px;
  font-weight: 600;
}

@media (max-width: 768px) {
  .stat-icon {
    width: 50px;
    height: 50px;
  }
  
  .stat-icon i {
    font-size: 24px;
  }
  
  .stat-value {
    font-size: 24px;
  }
}
</style>

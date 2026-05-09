<template>
  <div class="user-list">
    <h2 class="page-header">用户管理</h2>
    
    <!-- 用户表格 -->
    <div class="table-card">
      <el-table
        v-loading="loading"
        :data="userList"
        stripe
        class="user-table"
        :header-cell-style="{ background: '#f5f7fa', color: '#606266' }"
      >
        <el-table-column type="index" label="#" width="60" align="center" />
        <el-table-column prop="username" label="用户名" min-width="120" />
        <el-table-column prop="email" label="邮箱" min-width="180" />
        <el-table-column label="角色" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.role === 'ADMIN' ? 'danger' : 'primary'" size="small">
              {{ scope.row.role === 'ADMIN' ? '管理员' : '普通用户' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" min-width="180">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              v-if="scope.row.role !== 'ADMIN'"
              type="text"
              icon="el-icon-setting"
              @click="handleChangeRole(scope.row)"
            >
              设为管理员
            </el-button>
            <el-button
              v-else
              type="text"
              icon="el-icon-setting"
              @click="handleChangeRole(scope.row)"
            >
              取消管理员
            </el-button>
            <el-button
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              style="color: #F56C6C;"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 无数据提示 -->
      <div v-if="!loading && userList.length === 0" class="empty-state">
        <i class="el-icon-user"></i>
        <p>暂无用户数据</p>
      </div>
    </div>
  </div>
</template>

<script>
import { getUsers, updateUserRole, deleteUser } from '../api/user'

export default {
  name: 'UserList',
  
  data() {
    return {
      loading: false,
      userList: []
    }
  },
  
  mounted() {
    this.fetchUsers()
  },
  
  methods: {
    // 获取用户列表
    async fetchUsers() {
      this.loading = true
      try {
        const response = await getUsers()
        if (response.success) {
          this.userList = response.data || []
        }
      } catch (error) {
        console.error('获取用户列表失败:', error)
      } finally {
        this.loading = false
      }
    },
    
    // 修改角色
    handleChangeRole(row) {
      const newRole = row.role === 'ADMIN' ? 'USER' : 'ADMIN'
      const actionText = newRole === 'ADMIN' ? '设为管理员' : '取消管理员'
      
      this.$confirm(`确定要将用户 ${row.username} ${actionText}吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await updateUserRole(row.id, newRole)
          if (response.success) {
            this.$message.success('操作成功')
            this.fetchUsers()
          }
        } catch (error) {
          console.error('更新角色失败:', error)
        }
      }).catch(() => {})
    },
    
    // 删除用户
    handleDelete(row) {
      this.$confirm(`确定要删除用户 ${row.username} 吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteUser(row.id)
          if (response.success) {
            this.$message.success('删除成功')
            this.fetchUsers()
          }
        } catch (error) {
          console.error('删除用户失败:', error)
        }
      }).catch(() => {})
    },
    
    // 格式化日期
    formatDate(dateString) {
      if (!dateString) return '-'
      const date = new Date(dateString)
      return date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      })
    }
  }
}
</script>

<style scoped>
.user-list {
  max-width: 1400px;
}

.page-header {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 20px;
}

.table-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  min-height: 400px;
}

.user-table {
  width: 100%;
}

.user-table >>> .el-table__row {
  transition: all 0.3s ease;
}

.user-table >>> .el-table__row:hover {
  background: #f5f7fa;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #999;
}

.empty-state i {
  font-size: 64px;
  margin-bottom: 20px;
  color: #ddd;
}

.empty-state p {
  font-size: 16px;
}
</style>

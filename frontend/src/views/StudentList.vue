<template>
  <div class="student-list">
    <h2 class="page-header">学生管理</h2>

    <!-- 操作栏 -->
    <div class="action-bar">
      <div class="search-box">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索学生姓名..."
          prefix-icon="el-icon-search"
          clearable
          @clear="handleSearch"
          @keyup.enter.native="handleSearch"
          class="search-input"
        >
          <el-button slot="append" icon="el-icon-search" @click="handleSearch">搜索</el-button>
        </el-input>
      </div>
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd" class="add-btn">
        添加学生
      </el-button>
      <el-button type="success" icon="el-icon-upload2" @click="importDialogVisible = true" class="import-btn">
        批量导入
      </el-button>
    </div>

    <!-- 学生表格 -->
    <div class="table-card">
      <el-table
        v-loading="loading"
        :data="studentList"
        stripe
        class="student-table"
        :header-cell-style="{ background: '#f5f7fa', color: '#606266' }"
      >
        <el-table-column type="index" label="#" width="60" align="center" />
        <el-table-column prop="name" label="姓名" min-width="120" />
        <el-table-column prop="studentId" label="学号" min-width="120" />
        <el-table-column prop="gender" label="性别" width="80" align="center" />
        <el-table-column prop="className" label="班级" min-width="150" />
        <el-table-column label="创建时间" min-width="180">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              icon="el-icon-edit"
              @click="handleEdit(scope.row)"
              class="edit-btn"
            >
              编辑
            </el-button>
            <el-button
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              class="delete-btn"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 无数据提示 -->
      <div v-if="!loading && studentList.length === 0" class="empty-state">
        <i class="el-icon-folder-opened"></i>
        <p>暂无学生数据</p>
      </div>
    </div>

    <!-- 添加/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="500px"
      :close-on-click-modal="false"
      @close="resetForm"
    >
      <el-form
        ref="studentForm"
        :model="studentForm"
        :rules="formRules"
        label-width="80px"
      >
        <el-form-item label="姓名" prop="name">
          <el-input v-model="studentForm.name" placeholder="请输入学生姓名" clearable />
        </el-form-item>
        
        <el-form-item label="学号" prop="studentId">
          <el-input v-model="studentForm.studentId" placeholder="请输入学号" clearable />
        </el-form-item>
        
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="studentForm.gender">
            <el-radio label="男">男</el-radio>
            <el-radio label="女">女</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="班级" prop="className">
          <el-input v-model="studentForm.className" placeholder="请输入班级" clearable />
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">
          确定
        </el-button>
      </div>
    </el-dialog>

    <!-- 批量导入对话框 -->
    <el-dialog
      title="批量导入学生"
      :visible.sync="importDialogVisible"
      width="520px"
      :close-on-click-modal="false"
      @close="resetImport"
    >
      <div class="import-tips">
        <p>请上传 .xlsx 格式的 Excel 文件，表头需包含以下列：</p>
        <p><strong>姓名、学号、性别、班级</strong></p>
      </div>
      <el-upload
        ref="importUpload"
        action=""
        :auto-upload="false"
        :limit="1"
        accept=".xlsx"
        :on-change="handleImportFileChange"
        :on-remove="handleImportFileRemove"
        :file-list="importFileList"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">仅支持 .xlsx 文件</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button @click="importDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleImportSubmit" :loading="importing" :disabled="!importFile">
          开始导入
        </el-button>
      </div>
    </el-dialog>

    <!-- 导入结果对话框 -->
    <el-dialog
      title="导入结果"
      :visible.sync="importResultVisible"
      width="600px"
    >
      <div v-if="importResult" class="import-result">
        <el-row :gutter="20" class="result-summary">
          <el-col :span="8">
            <div class="summary-item">
              <div class="summary-value">{{ importResult.totalRows }}</div>
              <div class="summary-label">总行数</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="summary-item success">
              <div class="summary-value">{{ importResult.successCount }}</div>
              <div class="summary-label">成功</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="summary-item fail">
              <div class="summary-value">{{ importResult.failCount }}</div>
              <div class="summary-label">失败</div>
            </div>
          </el-col>
        </el-row>
        <div v-if="importResult.failDetails && importResult.failDetails.length > 0" class="fail-details">
          <h4>失败详情</h4>
          <el-table :data="importResult.failDetails" border size="small" max-height="300">
            <el-table-column prop="rowIndex" label="行号" width="80" align="center" />
            <el-table-column prop="reason" label="失败原因" />
          </el-table>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="closeImportResult">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getStudents, createStudent, updateStudent, deleteStudent, importStudents } from '../api/student'

export default {
  name: 'StudentList',
  
  data() {
    return {
      loading: false,
      submitting: false,
      studentList: [],
      searchKeyword: '',
      dialogVisible: false,
      dialogTitle: '添加学生',
      isEdit: false,
      studentForm: {
        id: null,
        name: '',
        studentId: '',
        gender: '男',
        className: ''
      },
      formRules: {
        name: [
          { required: true, message: '请输入学生姓名', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        studentId: [
          { required: true, message: '请输入学号', trigger: 'blur' },
          { pattern: /^\d+$/, message: '学号只能是数字', trigger: 'blur' }
        ],
        gender: [
          { required: true, message: '请选择性别', trigger: 'change' }
        ],
        className: [
          { required: true, message: '请输入班级', trigger: 'blur' }
        ]
      },
      importDialogVisible: false,
      importing: false,
      importFile: null,
      importFileList: [],
      importResultVisible: false,
      importResult: null
    }
  },
  
  mounted() {
    // 设置全局message引用用于错误处理
    window.$message = this.$message
    this.fetchStudents()
  },
  
  methods: {
    // 获取学生列表
    async fetchStudents() {
      this.loading = true
      try {
        const params = this.searchKeyword ? { name: this.searchKeyword } : {}
        const response = await getStudents(params)
        
        if (response.success) {
          this.studentList = response.data || []
        } else {
          this.$message.error(response.message || '获取学生列表失败')
        }
      } catch (error) {
        console.error('获取学生列表失败:', error)
      } finally {
        this.loading = false
      }
    },
    
    // 搜索
    handleSearch() {
      this.fetchStudents()
    },
    
    // 添加学生
    handleAdd() {
      this.isEdit = false
      this.dialogTitle = '添加学生'
      this.dialogVisible = true
    },
    
    // 编辑学生
    handleEdit(row) {
      this.isEdit = true
      this.dialogTitle = '编辑学生'
      this.studentForm = { ...row }
      this.dialogVisible = true
    },
    
    // 删除学生
    handleDelete(row) {
      this.$confirm(`确定要删除学生 ${row.name} 吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteStudent(row.id)
          
          if (response.success) {
            this.$message.success('删除成功')
            this.fetchStudents()
          } else {
            this.$message.error(response.message || '删除失败')
          }
        } catch (error) {
          console.error('删除学生失败:', error)
        }
      }).catch(() => {
        // 用户取消删除
      })
    },
    
    // 提交表单
    handleSubmit() {
      this.$refs.studentForm.validate(async (valid) => {
        if (!valid) {
          return false
        }
        
        this.submitting = true
        
        try {
          let response
          
          if (this.isEdit) {
            response = await updateStudent(this.studentForm.id, this.studentForm)
          } else {
            response = await createStudent(this.studentForm)
          }
          
          if (response.success) {
            this.$message.success(response.message || '操作成功')
            this.dialogVisible = false
            this.fetchStudents()
          } else {
            this.$message.error(response.message || '操作失败')
          }
        } catch (error) {
          console.error('提交失败:', error)
        } finally {
          this.submitting = false
        }
      })
    },
    
    // 重置表单
    resetForm() {
      this.studentForm = {
        id: null,
        name: '',
        studentId: '',
        gender: '男',
        className: ''
      }
      
      if (this.$refs.studentForm) {
        this.$refs.studentForm.resetFields()
      }
    },

    handleImportFileChange(file) {
      this.importFile = file.raw
      this.importFileList = [file]
    },

    handleImportFileRemove() {
      this.importFile = null
      this.importFileList = []
    },

    resetImport() {
      this.importFile = null
      this.importFileList = []
    },

    async handleImportSubmit() {
      if (!this.importFile) {
        this.$message.warning('请先选择文件')
        return
      }

      this.importing = true
      try {
        const response = await importStudents(this.importFile)
        if (response.success) {
          this.importResult = response.data
          this.importDialogVisible = false
          this.importResultVisible = true
          this.fetchStudents()
        } else {
          this.$message.error(response.message || '导入失败')
        }
      } catch (error) {
        console.error('导入失败:', error)
      } finally {
        this.importing = false
      }
    },

    closeImportResult() {
      this.importResultVisible = false
      this.importResult = null
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
.student-list {
  max-width: 1400px;
}

.page-header {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 20px;
}

/* 操作栏 */
.action-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  gap: 20px;
  flex-wrap: wrap;
}

.search-box {
  flex: 1;
  min-width: 300px;
  max-width: 500px;
}

.search-input >>> .el-input__inner {
  border-radius: 10px;
}

.search-input >>> .el-input-group__append {
  border-radius: 0 10px 10px 0;
}

.add-btn {
  border-radius: 10px;
  padding: 12px 30px;
}

.import-btn {
  border-radius: 10px;
  padding: 12px 30px;
}

.import-tips {
  background: #f4f4f5;
  border-radius: 8px;
  padding: 12px 16px;
  margin-bottom: 16px;
  font-size: 14px;
  color: #606266;
  line-height: 1.8;
}

.import-result .result-summary {
  text-align: center;
  margin-bottom: 20px;
}

.summary-item {
  background: #f5f7fa;
  border-radius: 8px;
  padding: 16px 0;
}

.summary-item.success {
  background: #f0f9eb;
}

.summary-item.fail {
  background: #fef0f0;
}

.summary-value {
  font-size: 28px;
  font-weight: 700;
  color: #303133;
}

.summary-item.success .summary-value {
  color: #67c23a;
}

.summary-item.fail .summary-value {
  color: #f56c6c;
}

.summary-label {
  font-size: 14px;
  color: #909399;
  margin-top: 4px;
}

.fail-details h4 {
  margin: 0 0 10px;
  font-size: 14px;
  color: #303133;
}

/* 表格卡片 */
.table-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  min-height: 400px;
}

.student-table {
  width: 100%;
}

.student-table >>> .el-table__row {
  transition: all 0.3s ease;
}

.student-table >>> .el-table__row:hover {
  background: #f5f7fa;
}

.edit-btn {
  color: #409EFF;
}

.delete-btn {
  color: #f56c6c;
}

/* 空状态 */
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

/* 响应式布局 */
@media (max-width: 768px) {
  .action-bar {
    flex-direction: column;
    align-items: stretch;
  }
  
  .search-box {
    max-width: 100%;
  }
  
  .add-btn {
    width: 100%;
  }
  
  .table-card {
    padding: 15px 10px;
  }
}
</style>

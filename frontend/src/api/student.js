import request from '../utils/request'

/**
 * 学生API接口封装
 */

// 查询所有学生
export function getStudents(params) {
  return request({
    url: '/students',
    method: 'get',
    params
  })
}

// 根据ID查询学生
export function getStudent(id) {
  return request({
    url: `/students/${id}`,
    method: 'get'
  })
}

// 创建学生
export function createStudent(data) {
  return request({
    url: '/students',
    method: 'post',
    data
  })
}

// 更新学生
export function updateStudent(id, data) {
  return request({
    url: `/students/${id}`,
    method: 'put',
    data
  })
}

// 删除学生
export function deleteStudent(id) {
  return request({
    url: `/students/${id}`,
    method: 'delete'
  })
}

// 批量导入学生
export function importStudents(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/students/import',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

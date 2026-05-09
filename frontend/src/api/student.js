import request from '../utils/request'

export function getStudents(params) {
  return request({
    url: '/students',
    method: 'get',
    params
  })
}

export function getStudent(id) {
  return request({
    url: `/students/${id}`,
    method: 'get'
  })
}

export function createStudent(data) {
  return request({
    url: '/students',
    method: 'post',
    data
  })
}

export function updateStudent(id, data) {
  return request({
    url: `/students/${id}`,
    method: 'put',
    data
  })
}

export function deleteStudent(id) {
  return request({
    url: `/students/${id}`,
    method: 'delete'
  })
}

export function importStudents(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/students/import',
    method: 'post',
    data: formData,
    headers: { 'Content-Type': 'multipart/form-data' },
    timeout: 60000
  })
}

import request from '../utils/request'

/**
 * 用户管理 API
 */

// 获取所有用户
export function getUsers() {
  return request({
    url: '/users',
    method: 'get'
  })
}

// 更新用户角色
export function updateUserRole(id, role) {
  return request({
    url: `/users/${id}/role`,
    method: 'put',
    data: { role }
  })
}

// 删除用户
export function deleteUser(id) {
  return request({
    url: `/users/${id}`,
    method: 'delete'
  })
}

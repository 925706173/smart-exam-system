import request from './request'

// 分页查询用户
export function getUserPage(params) {
  return request({
    url: '/users',
    method: 'get',
    params,
  })
}

// 创建用户
export function createUser(data) {
  return request({
    url: '/users',
    method: 'post',
    data,
  })
}

// 更新用户
export function updateUser(id, data) {
  return request({
    url: `/users/${id}`,
    method: 'put',
    data,
  })
}

// 删除用户
export function deleteUser(id) {
  return request({
    url: `/users/${id}`,
    method: 'delete',
  })
}

// 重置密码
export function resetPassword(id) {
  return request({
    url: `/users/${id}/reset-password`,
    method: 'put',
  })
}

// 冻结/解冻用户
export function toggleUserStatus(id) {
  return request({
    url: `/users/${id}/toggle-status`,
    method: 'put',
  })
}

// Excel批量导入用户
export function importUsers(file, role) {
  const formData = new FormData()
  formData.append('file', file)
  formData.append('role', role)
  return request({
    url: '/users/import',
    method: 'post',
    data: formData,
    headers: { 'Content-Type': 'multipart/form-data' },
  })
}

// 修改个人信息
export function updateProfile(data) {
  return request({
    url: '/users/profile',
    method: 'put',
    data,
  })
}

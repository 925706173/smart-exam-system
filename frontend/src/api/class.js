import request from './request'

// 分页查询班级
export function getClassPage(params) {
  return request({ url: '/classes', method: 'get', params })
}

// 获取所有班级
export function getClassAll() {
  return request({ url: '/classes/all', method: 'get' })
}

// 创建班级
export function createClass(data) {
  return request({ url: '/classes', method: 'post', data })
}

// 更新班级
export function updateClass(id, data) {
  return request({ url: `/classes/${id}`, method: 'put', data })
}

// 删除班级
export function deleteClass(id) {
  return request({ url: `/classes/${id}`, method: 'delete' })
}

// 获取班级成员
export function getClassUsers(classId, params) {
  return request({ url: `/classes/${classId}/users`, method: 'get', params })
}

// 添加成员到班级
export function addClassUser(classId, data) {
  return request({ url: `/classes/${classId}/users`, method: 'post', data })
}

// 从班级移除成员
export function removeClassUser(classId, userId) {
  return request({ url: `/classes/${classId}/users/${userId}`, method: 'delete' })
}

// 获取可添加的用户列表
export function getAvailableUsers(params) {
  return request({ url: '/classes/available-users', method: 'get', params })
}

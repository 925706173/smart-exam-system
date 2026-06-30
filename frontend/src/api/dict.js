import request from './request'

// 分页查询字典
export function getDictPage(params) {
  return request({
    url: '/dicts',
    method: 'get',
    params,
  })
}

// 按类型查询字典
export function getDictByType(type) {
  return request({
    url: `/dicts/type/${type}`,
    method: 'get',
  })
}

// 创建字典
export function createDict(data) {
  return request({
    url: '/dicts',
    method: 'post',
    data,
  })
}

// 更新字典
export function updateDict(id, data) {
  return request({
    url: `/dicts/${id}`,
    method: 'put',
    data,
  })
}

// 删除字典
export function deleteDict(id) {
  return request({
    url: `/dicts/${id}`,
    method: 'delete',
  })
}

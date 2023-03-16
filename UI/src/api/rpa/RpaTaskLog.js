import request from '@/utils/request'

// 查询RPA日志表列表
export function listRpaTaskLog(query) {
  return request({
    url: '/rpa/RpaTaskLog/list',
    method: 'get',
    params: query
  })
}

// 查询RPA日志表详细
export function getRpaTaskLog(id) {
  return request({
    url: '/rpa/RpaTaskLog/' + id,
    method: 'get'
  })
}

// 新增RPA日志表
export function addRpaTaskLog(data) {
  return request({
    url: '/rpa/RpaTaskLog',
    method: 'post',
    data: data
  })
}

// 修改RPA日志表
export function updateRpaTaskLog(data) {
  return request({
    url: '/rpa/RpaTaskLog',
    method: 'put',
    data: data
  })
}

// 删除RPA日志表
export function delRpaTaskLog(id) {
  return request({
    url: '/rpa/RpaTaskLog/' + id,
    method: 'delete'
  })
}

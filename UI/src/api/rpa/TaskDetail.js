import request from '@/utils/request'

// 查询rpa面板任务详情列表
export function listTaskDetail(query) {
  return request({
    url: '/rpa/TaskDetail/list',
    method: 'get',
    params: query
  })
}

// 查询rpa面板任务详情详细
export function getTaskDetail(id) {
  return request({
    url: '/rpa/TaskDetail/' + id,
    method: 'get'
  })
}

// 新增rpa面板任务详情
export function addTaskDetail(data) {
  return request({
    url: '/rpa/TaskDetail',
    method: 'post',
    data: data
  })
}

// 修改rpa面板任务详情
export function updateTaskDetail(data) {
  return request({
    url: '/rpa/TaskDetail',
    method: 'put',
    data: data
  })
}

// 删除rpa面板任务详情
export function delTaskDetail(id) {
  return request({
    url: '/rpa/TaskDetail/' + id,
    method: 'delete'
  })
}

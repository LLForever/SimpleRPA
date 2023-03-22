import request from '@/utils/request'

// 查询任务管理列表
export function listTaskManagerEntity(query) {
  return request({
    url: '/rpa/TaskManagerEntity/list',
    method: 'get',
    params: query
  })
}

// 查询任务管理详细
export function getTaskManagerEntity(id) {
  return request({
    url: '/rpa/TaskManagerEntity/' + id,
    method: 'get'
  })
}

// 新增任务管理
export function addTaskManagerEntity(data) {
  return request({
    url: '/rpa/TaskManagerEntity',
    method: 'post',
    data: data
  })
}

// 修改任务管理
export function updateTaskManagerEntity(data) {
  return request({
    url: '/rpa/TaskManagerEntity',
    method: 'put',
    data: data
  })
}

// 删除任务管理
export function delTaskManagerEntity(id) {
  return request({
    url: '/rpa/TaskManagerEntity/' + id,
    method: 'delete'
  })
}

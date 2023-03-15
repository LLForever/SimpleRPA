import request from '@/utils/request'

// 查询rpa任务计划详情列表
export function listRpaTaskSchedule(query) {
  return request({
    url: '/rpa/RpaTaskSchedule/list',
    method: 'get',
    params: query
  })
}

// 查询rpa任务计划详情详细
export function getRpaTaskSchedule(id) {
  return request({
    url: '/rpa/RpaTaskSchedule/' + id,
    method: 'get'
  })
}

// 新增rpa任务计划详情
export function addRpaTaskSchedule(data) {
  return request({
    url: '/rpa/RpaTaskSchedule',
    method: 'post',
    data: data
  })
}

// 修改rpa任务计划详情
export function updateRpaTaskSchedule(data) {
  return request({
    url: '/rpa/RpaTaskSchedule',
    method: 'put',
    data: data
  })
}

// 删除rpa任务计划详情
export function delRpaTaskSchedule(id) {
  return request({
    url: '/rpa/RpaTaskSchedule/' + id,
    method: 'delete'
  })
}

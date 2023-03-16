import request from '@/utils/request'

// 查询RPA任务协作列表
export function listRpaTaskCooperation(query) {
  return request({
    url: '/rpa/RpaTaskCooperation/list',
    method: 'get',
    params: query
  })
}

// 查询RPA任务协作列表
export function enableListRpaTaskCooperation(query) {
    return request({
        url: '/rpa/RpaTaskCooperation/enable_list',
        method: 'get',
        params: query
    })
}

// 查询RPA任务协作详细
export function getRpaTaskCooperation(id) {
  return request({
    url: '/rpa/RpaTaskCooperation/' + id,
    method: 'get'
  })
}

// 新增RPA任务协作
export function addRpaTaskCooperation(data) {
  return request({
    url: '/rpa/RpaTaskCooperation',
    method: 'post',
    data: data
  })
}

// 修改RPA任务协作
export function updateRpaTaskCooperation(data) {
  return request({
    url: '/rpa/RpaTaskCooperation',
    method: 'put',
    data: data
  })
}

export function changeRpaTaskCooperationStatus(data) {
    return request({
        url: '/rpa/RpaTaskCooperation/change_status',
        method: 'post',
        data: data
    })
}

// 删除RPA任务协作
export function delRpaTaskCooperation(id) {
  return request({
    url: '/rpa/RpaTaskCooperation/' + id,
    method: 'delete'
  })
}

export function getOneTaskDetail(data) {
    return request({
        url: '/rpa/RpaTaskCooperation/getTaskDetail/'+data,
        method: 'get',
        data: data
    })
}

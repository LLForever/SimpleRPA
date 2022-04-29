import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

export function uploadTaskDetail(data) {
  return request({
    url: '/rpa/panel-task/upload/detail',
    method: 'post',
    data: data
  })
}

export function sendRunTaskSig(data) {
    return request({
        url: '/rpa/panel-task/run',
        method: 'post',
        data: data
    })
}

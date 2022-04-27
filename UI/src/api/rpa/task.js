import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

export function uploadTaskDetail(data) {
  return request({
    url: '/panel-task/upload/detail',
    method: 'post',
    data: data
  })
}

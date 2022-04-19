import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

export function rpa_fun_test() {
  return request({
    url: '/rpa/panel-task/test',
    method: 'get'
  })
}

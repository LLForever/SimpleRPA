import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

export function rpa_fun_test(data) {
  return request({
    url: '/system/user/register',
    method: 'post',
    data: data
  })
}

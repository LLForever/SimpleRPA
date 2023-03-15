import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

export function getSuccessRate(data) {
    return request({
        url: '/rpa/assist/query/'+data,
        method: 'get',
        data: data
    })
}

export function query_most_error(data) {
    return request({
        url: '/rpa/assist/query_most_error/'+data,
        method: 'get',
        data: data
    })
}

export function get_recently_exec_log(data) {
    return request({
        url: '/rpa/assist/get_recently_exec_log/'+data,
        method: 'get',
        data: data
    })
}

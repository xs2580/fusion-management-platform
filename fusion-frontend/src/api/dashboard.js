import request from './request'

/** 后台首页概览统计 */
export function dashboardOverview() {
  return request({ url: '/dashboard/overview', method: 'get' })
}

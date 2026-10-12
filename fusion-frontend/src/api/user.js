import request from './request'

/** 用户分页 */
export function pageUser(params) {
  return request({ url: '/user/page', method: 'get', params })
}

/** 用户详情 */
export function getUser(id) {
  return request({ url: `/user/${id}`, method: 'get' })
}

/** 新增用户 */
export function createUser(data) {
  return request({ url: '/user', method: 'post', data })
}

/** 修改用户 */
export function updateUser(data) {
  return request({ url: '/user', method: 'put', data })
}

/** 重置密码 */
export function resetUserPassword(id, newPassword) {
  return request({ url: `/user/${id}/reset-password`, method: 'put', params: { newPassword } })
}

/** 删除用户 */
export function deleteUser(id) {
  return request({ url: `/user/${id}`, method: 'delete' })
}

/** 用户统计 */
export function userStatistics() {
  return request({ url: '/user/statistics', method: 'get' })
}

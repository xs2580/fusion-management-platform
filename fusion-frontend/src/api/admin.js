import request from './request'

/** 管理员分页 */
export function pageAdmin(params) {
  return request({ url: '/admin/page', method: 'get', params })
}

/** 管理员详情 */
export function getAdmin(id) {
  return request({ url: `/admin/${id}`, method: 'get' })
}

/** 新增管理员 */
export function createAdmin(data) {
  return request({ url: '/admin', method: 'post', data })
}

/** 修改管理员 */
export function updateAdmin(data) {
  return request({ url: '/admin', method: 'put', data })
}

/** 重置密码 */
export function resetAdminPassword(id, newPassword) {
  return request({ url: `/admin/${id}/reset-password`, method: 'put', params: { newPassword } })
}

/** 删除管理员 */
export function deleteAdmin(id) {
  return request({ url: `/admin/${id}`, method: 'delete' })
}

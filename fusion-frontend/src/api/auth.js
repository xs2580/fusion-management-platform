import request from './request'

/** 登录 */
export function login(data) {
  return request({ url: '/auth/login', method: 'post', data })
}

/** 注册 */
export function register(data) {
  return request({ url: '/auth/register', method: 'post', data })
}

/** 当前登录人信息 */
export function getInfo() {
  return request({ url: '/auth/info', method: 'get' })
}

/** 更新个人资料 */
export function updateProfile(data) {
  return request({ url: '/auth/profile', method: 'put', data })
}

/** 修改密码 */
export function changePassword(data) {
  return request({ url: '/auth/password', method: 'put', data })
}

/** 退出登录 */
export function logout() {
  return request({ url: '/auth/logout', method: 'post' })
}

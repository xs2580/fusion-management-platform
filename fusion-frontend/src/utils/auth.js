/**
 * 本地存储工具：统一 token / 用户信息 的读写
 */
const TOKEN_KEY = 'fusion_token'
const USER_KEY = 'fusion_user'
const REMEMBER_KEY = 'fusion_remember'

export function getToken() {
  return localStorage.getItem(TOKEN_KEY) || ''
}

export function setToken(token) {
  if (token) {
    localStorage.setItem(TOKEN_KEY, token)
  } else {
    localStorage.removeItem(TOKEN_KEY)
  }
}

export function removeToken() {
  localStorage.removeItem(TOKEN_KEY)
}

export function getUser() {
  const raw = localStorage.getItem(USER_KEY)
  if (!raw) return null
  try {
    return JSON.parse(raw)
  } catch (e) {
    return null
  }
}

export function setUser(user) {
  if (user) {
    localStorage.setItem(USER_KEY, JSON.stringify(user))
  } else {
    localStorage.removeItem(USER_KEY)
  }
}

export function removeUser() {
  localStorage.removeItem(USER_KEY)
}

/** 清除全部登录态 */
export function clearAuth() {
  removeToken()
  removeUser()
}

/* ---------------- 记住账号 ---------------- */
export function getRemembered() {
  const raw = localStorage.getItem(REMEMBER_KEY)
  if (!raw) return null
  try {
    return JSON.parse(raw)
  } catch (e) {
    return null
  }
}

export function setRemembered(data) {
  if (data) {
    localStorage.setItem(REMEMBER_KEY, JSON.stringify(data))
  } else {
    localStorage.removeItem(REMEMBER_KEY)
  }
}

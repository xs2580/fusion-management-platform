import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getToken, clearAuth } from '@/utils/auth'
import router from '@/router'

/**
 * Axios 实例：统一 baseURL、超时、请求头与响应拦截
 */
const service = axios.create({
  baseURL: '/api',
  timeout: 15000,
  headers: { 'Content-Type': 'application/json;charset=UTF-8' }
})

let redirecting = false

/* ---------------- 请求拦截 ---------------- */
service.interceptors.request.use(
  (config) => {
    const token = getToken()
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    // GET 请求防缓存
    if (config.method === 'get') {
      config.params = { ...(config.params || {}), _t: Date.now() }
    }
    return config
  },
  (error) => Promise.reject(error)
)

/* ---------------- 响应拦截 ---------------- */
service.interceptors.response.use(
  (response) => {
    const res = response.data

    // 非标准结构（如文件流）直接返回
    if (res == null || typeof res.code === 'undefined') {
      return res
    }

    if (res.code === 200) {
      return res
    }

    // 401 / 1002 / 1003 → 登录失效
    if ([401, 1002, 1003].includes(res.code)) {
      if (!redirecting) {
        redirecting = true
        clearAuth()
        ElMessageBox.alert(res.message || '登录状态已失效，请重新登录', '提示', {
          confirmButtonText: '重新登录',
          type: 'warning',
          showClose: false
        })
          .catch(() => {})
          .finally(() => {
            redirecting = false
            router.replace({ path: '/login', query: { redirect: router.currentRoute.value.fullPath } })
          })
      }
      return Promise.reject(new Error(res.message || '登录失效'))
    }

    ElMessage.error(res.message || '请求失败')
    return Promise.reject(new Error(res.message || '请求失败'))
  },
  (error) => {
    let message = '网络异常，请稍后重试'
    if (error.code === 'ECONNABORTED') {
      message = '请求超时，请检查后端服务是否已启动'
    } else if (error.response) {
      const status = error.response.status
      const map = {
        400: '请求参数有误',
        401: '登录状态已失效',
        403: '没有访问权限',
        404: '接口不存在',
        405: '请求方式不正确',
        500: '服务器内部错误'
      }
      message = error.response.data?.message || map[status] || `请求异常(${status})`
    } else {
      message = '无法连接后端服务，请确认 SpringBoot 已启动在 8080 端口'
    }
    ElMessage.error(message)
    return Promise.reject(error)
  }
)

export default service

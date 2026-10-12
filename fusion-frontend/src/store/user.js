import { defineStore } from 'pinia'
import { login as loginApi, logout as logoutApi, getInfo } from '@/api/auth'
import {
  getToken,
  setToken,
  getUser,
  setUser,
  clearAuth,
  setRemembered
} from '@/utils/auth'

/**
 * 登录用户状态管理
 */
export const useUserStore = defineStore('user', {
  state: () => ({
    token: getToken(),
    userInfo: getUser() || {},
    loginType: getUser()?.loginType || ''
  }),

  getters: {
    isLogin: (state) => !!state.token,
    isAdmin: (state) => state.userInfo?.loginType === 'admin',
    isUser: (state) => state.userInfo?.loginType === 'user',
    nickname: (state) => state.userInfo?.nickname || state.userInfo?.username || '未登录',
    avatar: (state) => state.userInfo?.avatar || '/avatar-default.webp',
    /** 角色默认首页 */
    homePath: (state) => (state.userInfo?.loginType === 'admin' ? '/back/home' : '/front/home')
  },

  actions: {
    /** 登录 */
    async login(form) {
      const res = await loginApi(form)
      const data = res.data || {}
      this.token = data.token
      this.userInfo = data
      this.loginType = data.loginType
      setToken(data.token)
      setUser(data)

      // 记住账号（只存账号和身份，不存密码）
      if (form.remember) {
        setRemembered({ username: form.username, loginType: form.loginType })
      } else {
        setRemembered(null)
      }
      return data
    },

    /** 拉取最新用户信息 */
    async fetchInfo() {
      const res = await getInfo()
      this.userInfo = res.data || {}
      this.loginType = this.userInfo.loginType
      setUser(this.userInfo)
      return this.userInfo
    },

    /** 本地更新用户信息 */
    patchUser(partial) {
      this.userInfo = { ...this.userInfo, ...partial }
      setUser(this.userInfo)
    },

    /** 退出登录 */
    async logout() {
      try {
        await logoutApi()
      } catch (e) {
        // 忽略：本地清理才是关键
      }
      this.reset()
    },

    /** 清空登录态 */
    reset() {
      this.token = ''
      this.userInfo = {}
      this.loginType = ''
      clearAuth()
    }
  }
})

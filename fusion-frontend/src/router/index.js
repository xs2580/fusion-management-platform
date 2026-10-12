import { createRouter, createWebHistory } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getToken, getUser } from '@/utils/auth'

/**
 * 路由表
 * ------------------------------------------------------------------
 *  /login     登录（用户 / 管理员 共用，页内切换身份）
 *  /register  前台用户注册
 *  /front/**  前台（普通用户登录后进入）
 *  /back/**   后台管理（管理员登录后进入）
 * ------------------------------------------------------------------
 */
const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue'),
    meta: { title: '注册' }
  },

  /* ======================= 前台 ======================= */
  {
    path: '/front',
    name: 'Front',
    component: () => import('@/views/front/Front.vue'),
    redirect: '/front/home',
    meta: { requiresAuth: true, loginType: 'user', title: '前台' },
    children: [
      {
        path: 'home',
        name: 'FrontHome',
        component: () => import('@/views/front/Home.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'workspace',
        name: 'FrontWorkspace',
        component: () => import('@/views/front/Workspace.vue'),
        meta: { title: '我的工作台' }
      },
      {
        path: 'profile',
        name: 'FrontProfile',
        component: () => import('@/views/front/Profile.vue'),
        meta: { title: '个人中心' }
      }
    ]
  },

  /* ======================= 后台 ======================= */
  {
    path: '/back',
    name: 'BackRoot',
    component: () => import('@/views/back/Back.vue'),
    redirect: '/back/home',
    meta: { requiresAuth: true, loginType: 'admin', title: '后台管理' },
    children: [
      {
        path: 'home',
        name: 'BackHome',
        component: () => import('@/views/back/Home.vue'),
        meta: { title: '后台首页', icon: 'HomeFilled' }
      },
      {
        path: 'adminPerson',
        name: 'BackAdminPerson',
        component: () => import('@/views/back/AdminPerson.vue'),
        meta: { title: '个人信息', icon: 'UserFilled' }
      },
      {
        path: 'password',
        name: 'BackPassword',
        component: () => import('@/views/back/Password.vue'),
        meta: { title: '修改密码', icon: 'Lock' }
      },

      /* ---------- 角色管理（菜单中归为一组） ---------- */
      {
        path: 'admin',
        name: 'BackAdmin',
        component: () => import('@/views/back/Admin.vue'),
        meta: { title: '管理员管理', group: '角色管理' }
      },
      {
        path: 'user',
        name: 'BackUser',
        component: () => import('@/views/back/User.vue'),
        meta: { title: '用户管理', group: '角色管理' }
      }
    ]
  },

  /* ======================= 异常页 ======================= */
  {
    path: '/404',
    name: 'NotFound',
    component: () => import('@/views/404.vue'),
    meta: { title: '页面不存在' }
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/404'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior: () => ({ top: 0 })
})

/* ======================= 全局前置守卫 ======================= */
router.beforeEach((to, from, next) => {
  document.title = to.meta?.title ? `${to.meta.title} · 融合管理系统平台` : '融合管理系统平台'

  const token = getToken()
  const user = getUser() || {}
  const loginType = user.loginType

  // 未登录
  if (to.meta?.requiresAuth && !token) {
    ElMessage.warning('请先登录')
    return next({ path: '/login', query: { redirect: to.fullPath } })
  }

  // 已登录访问登录页 → 回到各自首页
  if (token && (to.path === '/login' || to.path === '/register')) {
    return next(loginType === 'admin' ? '/back/home' : '/front/home')
  }

  // 身份不匹配校验：管理员进前台 or 用户进后台 → 重定向到自己的首页
  if (to.meta?.requiresAuth && to.meta?.loginType && to.meta.loginType !== loginType) {
    ElMessage.warning('当前身份无权访问该区域')
    return next(loginType === 'admin' ? '/back/home' : '/front/home')
  }

  next()
})

export default router

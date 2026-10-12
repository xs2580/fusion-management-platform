<template>
  <el-container class="back-layout">
    <!-- ================= 顶栏 ================= -->
    <el-header class="back-header" height="58px">
      <div class="header-left">
        <div class="brand">
          <div class="brand-logo">
            <el-icon :size="17" color="#fff"><Connection /></el-icon>
          </div>
          <span class="brand-name">融合管理系统平台</span>
        </div>
        <span class="brand-sub">后台管理中心</span>
      </div>

      <div class="header-right">
        <el-tooltip content="刷新当前页" placement="bottom">
          <button class="icon-btn" @click="refreshPage">
            <el-icon :size="16"><Refresh /></el-icon>
          </button>
        </el-tooltip>
        <el-tooltip :content="isFull ? '退出全屏' : '全屏显示'" placement="bottom">
          <button class="icon-btn" @click="toggleFull">
            <el-icon :size="16"><component :is="isFull ? 'Aim' : 'FullScreen'" /></el-icon>
          </button>
        </el-tooltip>

        <span class="divider"></span>

        <el-dropdown trigger="click" @command="handleCommand">
          <div class="admin-chip">
            <img class="avatar" :src="userStore.avatar" alt="avatar" />
            <div class="admin-info">
              <b>{{ userStore.nickname }}</b>
              <i>{{ roleText }}</i>
            </div>
            <el-icon :size="12" color="rgba(255,255,255,.85)"><ArrowDown /></el-icon>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="adminPerson">
                <el-icon><UserFilled /></el-icon> 个人信息
              </el-dropdown-item>
              <el-dropdown-item command="password">
                <el-icon><Lock /></el-icon> 修改密码
              </el-dropdown-item>
              <el-dropdown-item command="front" divided>
                <el-icon><Monitor /></el-icon> 切换到前台
              </el-dropdown-item>
              <el-dropdown-item command="logout" divided>
                <el-icon><SwitchButton /></el-icon> 退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </el-header>

    <el-container class="back-body">
      <!-- ================= 侧边菜单 ================= -->
      <el-aside width="216px" class="back-aside">
        <div class="aside-scroll">
          <p class="menu-label">导航菜单</p>
          <el-menu :default-active="activeMenu" router unique-opened class="back-menu">
            <el-menu-item index="/back/home">
              <el-icon><HomeFilled /></el-icon>
              <span>后台首页</span>
            </el-menu-item>

            <el-sub-menu index="role">
              <template #title>
                <el-icon><User /></el-icon>
                <span>角色管理</span>
              </template>
              <el-menu-item index="/back/admin">
                <el-icon><Avatar /></el-icon>
                <span>管理员管理</span>
              </el-menu-item>
              <el-menu-item index="/back/user">
                <el-icon><UserFilled /></el-icon>
                <span>用户管理</span>
              </el-menu-item>
            </el-sub-menu>
          </el-menu>

          <p class="menu-label">账号设置</p>
          <el-menu :default-active="activeMenu" router class="back-menu">
            <el-menu-item index="/back/adminPerson">
              <el-icon><Postcard /></el-icon>
              <span>个人信息</span>
            </el-menu-item>
            <el-menu-item index="/back/password">
              <el-icon><Lock /></el-icon>
              <span>修改密码</span>
            </el-menu-item>
          </el-menu>
        </div>

        <div class="aside-foot">
          <div class="tip-card">
            <el-icon :size="15"><InfoFilled /></el-icon>
            <div>
              <b>融合平台 v1.0.0</b>
              <span>SpringBoot 3 + Vue3</span>
            </div>
          </div>
        </div>
      </el-aside>

      <!-- ================= 内容区 ================= -->
      <el-main class="back-main">
        <div class="crumb">
          <el-icon :size="14"><Location /></el-icon>
          <span>后台管理</span>
          <template v-for="(c, i) in crumbs" :key="c">
            <span class="sep">/</span>
            <span :class="{ cur: i === crumbs.length - 1 }">{{ c }}</span>
          </template>
        </div>

        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '@/store/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const isFull = ref(false)

/** 菜单高亮：取当前路由 path */
const activeMenu = computed(() => route.path)

/** 面包屑 */
const crumbs = computed(() => {
  const title = route.meta?.title
  const group = route.meta?.group
  if (group && title) return [group, title]
  return title ? [title] : []
})

const roleText = computed(() => {
  const role = userStore.userInfo?.role
  if (role === 'SUPER_ADMIN') return '超级管理员'
  if (role === 'ADMIN') return '管理员'
  return '管理员'
})

onMounted(() => {
  userStore.fetchInfo().catch(() => {})
})

function refreshPage() {
  ElMessage.success('正在刷新…')
  setTimeout(() => window.location.reload(), 120)
}

function toggleFull() {
  if (!document.fullscreenElement) {
    document.documentElement.requestFullscreen?.()
    isFull.value = true
  } else {
    document.exitFullscreen?.()
    isFull.value = false
  }
}

async function handleCommand(cmd) {
  if (cmd === 'logout') {
    await ElMessageBox.confirm('确定要退出登录吗？', '退出确认', {
      confirmButtonText: '退出',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await userStore.logout()
    ElMessage.success('已安全退出')
    router.replace('/login')
    return
  }
  if (cmd === 'adminPerson') router.push('/back/adminPerson')
  if (cmd === 'password') router.push('/back/password')
  if (cmd === 'front') {
    ElMessage.info('管理员账号请使用普通用户账号查看前台页面')
  }
}
</script>

<style scoped>
.back-layout {
  height: 100vh;
  overflow: hidden;
}

/* ================= 顶栏 ================= */
.back-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  color: #fff;
  background: linear-gradient(100deg, #b81c2b 0%, #e63946 45%, #ff6b78 100%);
  box-shadow: 0 2px 12px rgba(184, 28, 43, 0.32);
  z-index: 10;
}
.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
  min-width: 0;
}
.brand {
  display: flex;
  align-items: center;
  gap: 9px;
}
.brand-logo {
  width: 32px;
  height: 32px;
  border-radius: 10px;
  display: grid;
  place-items: center;
  background: rgba(255, 255, 255, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.3);
}
.brand-name {
  font-size: 16px;
  font-weight: 700;
  letter-spacing: 0.6px;
}
.brand-sub {
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.16);
  border: 1px solid rgba(255, 255, 255, 0.22);
}

.header-right {
  display: flex;
  align-items: center;
  gap: 6px;
}
.icon-btn {
  width: 32px;
  height: 32px;
  border: 0;
  border-radius: 9px;
  background: rgba(255, 255, 255, 0.14);
  color: #fff;
  cursor: pointer;
  display: grid;
  place-items: center;
  transition: background 0.2s ease;
}
.icon-btn:hover {
  background: rgba(255, 255, 255, 0.28);
}
.divider {
  width: 1px;
  height: 22px;
  background: rgba(255, 255, 255, 0.28);
  margin: 0 8px;
}
.admin-chip {
  display: flex;
  align-items: center;
  gap: 9px;
  padding: 4px 10px 4px 4px;
  border-radius: 999px;
  cursor: pointer;
  background: rgba(255, 255, 255, 0.14);
  transition: background 0.2s ease;
  outline: none;
}
.admin-chip:hover {
  background: rgba(255, 255, 255, 0.26);
}
.avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid rgba(255, 255, 255, 0.9);
}
.admin-info {
  display: flex;
  flex-direction: column;
  line-height: 1.25;
}
.admin-info b {
  font-size: 13px;
  font-weight: 600;
}
.admin-info i {
  font-style: normal;
  font-size: 10.5px;
  opacity: 0.8;
}

/* ================= 主体 ================= */
.back-body {
  height: calc(100vh - 58px);
}

.back-aside {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  background: #fff;
  border-right: 1px solid #eef1f7;
  overflow: hidden;
}
.aside-scroll {
  flex: 1;
  overflow-y: auto;
  padding: 12px 10px 0;
}
.menu-label {
  margin: 10px 8px 6px;
  font-size: 11px;
  letter-spacing: 1px;
  color: #a8b2c1;
  text-transform: uppercase;
}
.back-menu {
  border-right: 0;
  --el-menu-item-height: 42px;
}
.back-menu :deep(.el-menu-item),
.back-menu :deep(.el-sub-menu__title) {
  border-radius: 9px;
  margin-bottom: 3px;
  font-size: 13.5px;
  color: #4b5563;
}
.back-menu :deep(.el-menu-item:hover),
.back-menu :deep(.el-sub-menu__title:hover) {
  background: #fdecee;
  color: #e63946;
}
.back-menu :deep(.el-menu-item.is-active) {
  background: linear-gradient(100deg, #e63946, #ff6b78);
  color: #fff;
  font-weight: 600;
  box-shadow: 0 8px 18px -10px rgba(230, 57, 70, 0.9);
}
.back-menu :deep(.el-sub-menu.is-active > .el-sub-menu__title) {
  color: #e63946;
  font-weight: 600;
}
.back-menu :deep(.el-sub-menu .el-menu-item) {
  min-width: auto;
  padding-left: 42px !important;
}

.aside-foot {
  padding: 12px;
  border-top: 1px solid #f1f4f9;
}
.tip-card {
  display: flex;
  align-items: center;
  gap: 9px;
  padding: 10px 12px;
  border-radius: 11px;
  background: linear-gradient(120deg, #fff5f6, #f8fafc);
  border: 1px solid #fbe0e3;
  color: #e63946;
}
.tip-card div {
  display: flex;
  flex-direction: column;
  line-height: 1.3;
}
.tip-card b {
  font-size: 12px;
  color: #b81c2b;
}
.tip-card span {
  font-size: 10.5px;
  color: #9aa4b2;
}

.back-main {
  padding: 16px 18px 22px;
  background: #f4f6fb;
  overflow-y: auto;
}

.crumb {
  display: flex;
  align-items: center;
  gap: 7px;
  font-size: 12.5px;
  color: #94a3b8;
  margin-bottom: 14px;
}
.crumb .sep {
  color: #cbd5e1;
}
.crumb .cur {
  color: #334155;
  font-weight: 600;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.22s ease, transform 0.22s ease;
}
.fade-enter-from {
  opacity: 0;
  transform: translateY(8px);
}
.fade-leave-to {
  opacity: 0;
}
</style>

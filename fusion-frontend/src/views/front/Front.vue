<template>
  <div class="front-layout">
    <!-- ============ 顶部导航 ============ -->
    <header class="front-header">
      <div class="header-inner">
        <div class="brand" @click="router.push('/front/home')">
          <div class="brand-logo">
            <el-icon :size="18" color="#fff"><Connection /></el-icon>
          </div>
          <div class="brand-text">
            <b>融合管理系统平台</b>
            <i>Fusion Platform</i>
          </div>
        </div>

        <nav class="nav">
          <router-link
            v-for="item in navs"
            :key="item.path"
            :to="item.path"
            class="nav-item"
            :class="{ active: route.path === item.path }"
          >
            <el-icon :size="15"><component :is="item.icon" /></el-icon>
            {{ item.title }}
          </router-link>
        </nav>

        <div class="header-right">
          <el-tag type="success" effect="light" round size="small">用户版</el-tag>
          <el-dropdown trigger="click" @command="handleCommand">
            <div class="user-chip">
              <img class="avatar" :src="userStore.avatar" alt="avatar" />
              <span class="name">{{ userStore.nickname }}</span>
              <el-icon :size="12"><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <el-icon><UserFilled /></el-icon> 个人中心
                </el-dropdown-item>
                <el-dropdown-item command="workspace">
                  <el-icon><Grid /></el-icon> 我的工作台
                </el-dropdown-item>
                <el-dropdown-item command="logout" divided>
                  <el-icon><SwitchButton /></el-icon> 退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </header>

    <!-- ============ 主体 ============ -->
    <main class="front-main">
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </main>

    <footer class="front-footer">
      © 2026 融合管理系统平台 · 前台用户端 &nbsp;|&nbsp; Powered by SpringBoot 3 + Vue3
    </footer>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '@/store/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const navs = [
  { path: '/front/home', title: '首页', icon: 'HomeFilled' },
  { path: '/front/workspace', title: '我的工作台', icon: 'Grid' },
  { path: '/front/profile', title: '个人中心', icon: 'UserFilled' }
]

onMounted(() => {
  // 刷新页面后同步一次最新资料
  userStore.fetchInfo().catch(() => {})
})

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
  if (cmd === 'profile') router.push('/front/profile')
  if (cmd === 'workspace') router.push('/front/workspace')
}
</script>

<style scoped>
.front-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f4f6fb;
}

/* ---------------- 顶部栏 ---------------- */
.front-header {
  position: sticky;
  top: 0;
  z-index: 100;
  background: rgba(255, 255, 255, 0.82);
  backdrop-filter: saturate(180%) blur(14px);
  border-bottom: 1px solid #e9edf5;
}
.header-inner {
  max-width: 1240px;
  margin: 0 auto;
  height: 62px;
  padding: 0 24px;
  display: flex;
  align-items: center;
  gap: 28px;
}
.brand {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  flex: none;
}
.brand-logo {
  width: 34px;
  height: 34px;
  border-radius: 10px;
  display: grid;
  place-items: center;
  background: linear-gradient(135deg, #3b5bdb, #7048e8);
}
.brand-text {
  display: flex;
  flex-direction: column;
  line-height: 1.2;
}
.brand-text b {
  font-size: 14.5px;
  color: #0f172a;
  letter-spacing: 0.3px;
}
.brand-text i {
  font-style: normal;
  font-size: 10px;
  color: #94a3b8;
  letter-spacing: 1px;
}

.nav {
  display: flex;
  gap: 6px;
  flex: 1;
}
.nav-item {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 15px;
  border-radius: 9px;
  font-size: 13.5px;
  color: #475569;
  transition: all 0.2s ease;
}
.nav-item:hover {
  background: #f1f5ff;
  color: #2f54eb;
}
.nav-item.active {
  background: linear-gradient(135deg, #eef2ff, #f5f0ff);
  color: #2f54eb;
  font-weight: 600;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
  flex: none;
}
.user-chip {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 4px 10px 4px 4px;
  border-radius: 999px;
  cursor: pointer;
  transition: background 0.2s ease;
  outline: none;
}
.user-chip:hover {
  background: #f1f5ff;
}
.avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #fff;
  box-shadow: 0 0 0 1px #e2e8f0;
}
.name {
  font-size: 13.5px;
  color: #334155;
  max-width: 100px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

/* ---------------- 主体 ---------------- */
.front-main {
  flex: 1;
  width: 100%;
  max-width: 1240px;
  margin: 0 auto;
  padding: 26px 24px 40px;
}

.front-footer {
  padding: 18px 24px;
  text-align: center;
  font-size: 12px;
  color: #94a3b8;
  border-top: 1px solid #e9edf5;
  background: #fff;
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

@media (max-width: 720px) {
  .brand-text i,
  .nav-item span {
    display: none;
  }
  .header-inner {
    gap: 12px;
    padding: 0 14px;
  }
  .front-main {
    padding: 18px 14px 30px;
  }
}
</style>

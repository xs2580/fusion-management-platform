<template>
  <div class="back-home">
    <!-- ============ 欢迎条 ============ -->
    <section class="welcome">
      <div class="welcome-deco"></div>
      <div class="welcome-text">
        <span class="tag">ADMIN CONSOLE</span>
        <h1>欢迎回来，{{ userStore.nickname }}</h1>
        <p>当前登录身份：{{ roleText }} · 可在此维护「角色管理」下的管理员与用户数据</p>
      </div>
      <div class="welcome-actions">
        <el-button type="primary" @click="router.push('/back/admin')">
          <el-icon class="mr6"><Avatar /></el-icon>管理员管理
        </el-button>
        <el-button plain @click="router.push('/back/user')">
          <el-icon class="mr6"><UserFilled /></el-icon>用户管理
        </el-button>
      </div>
    </section>

    <!-- ============ 统计 ============ -->
    <section class="stat-row">
      <div v-for="item in statCards" :key="item.label" class="stat-card">
        <div class="stat-top">
          <div class="stat-icon" :style="{ background: item.bg, color: item.color }">
            <el-icon :size="20"><component :is="item.icon" /></el-icon>
          </div>
          <el-tag :type="item.tagType" size="small" effect="light" round>{{ item.tag }}</el-tag>
        </div>
        <b class="stat-value">{{ item.value }}</b>
        <span class="stat-label">{{ item.label }}</span>
      </div>
    </section>

    <!-- ============ 快捷入口 + 提示 ============ -->
    <section class="content-row">
      <div class="panel">
        <h3 class="panel-title"><el-icon><Grid /></el-icon> 快捷入口</h3>
        <div class="quick-grid">
          <div v-for="q in quicks" :key="q.title" class="quick-item" @click="router.push(q.path)">
            <div class="quick-icon" :style="{ background: q.bg, color: q.color }">
              <el-icon :size="18"><component :is="q.icon" /></el-icon>
            </div>
            <div class="quick-body">
              <b>{{ q.title }}</b>
              <span>{{ q.desc }}</span>
            </div>
            <el-icon class="quick-arrow"><ArrowRight /></el-icon>
          </div>
        </div>
      </div>

      <div class="panel">
        <h3 class="panel-title"><el-icon><Guide /></el-icon> 使用指引</h3>
        <el-collapse v-model="activeCollapse" class="guide-collapse">
          <el-collapse-item title="如何新增一个管理员？" name="1">
            进入「角色管理 → 管理员管理」，点击左上角
            <el-tag size="small" type="danger" effect="light">新增管理员</el-tag>
            ，填写账号密码即可。新建账号默认密码可用 123456。
          </el-collapse-item>
          <el-collapse-item title="如何维护前台用户？" name="2">
            进入「角色管理 → 用户管理」，支持按账号 / 昵称 / 手机号搜索，可编辑资料、重置密码、删除用户。
          </el-collapse-item>
          <el-collapse-item title="密码规则说明" name="3">
            本脚手架演示使用 MD5 存储密码；生产环境请替换
            <code>PasswordUtil</code> 为 BCrypt。
          </el-collapse-item>
          <el-collapse-item title="前后端如何联调？" name="4">
            后端 8080 端口，前端 5173 端口，Vite 已配置 <code>/api</code> 代理，直接 <code>npm run dev</code> 即可。
          </el-collapse-item>
        </el-collapse>
      </div>
    </section>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { dashboardOverview } from '@/api/dashboard'

const router = useRouter()
const userStore = useUserStore()
const overview = ref({ userTotal: 0, userEnabled: 0, adminTotal: 0, adminEnabled: 0 })
const activeCollapse = ref(['1'])

const roleText = computed(() => {
  const role = userStore.userInfo?.role
  return role === 'SUPER_ADMIN' ? '超级管理员' : '管理员'
})

const statCards = computed(() => [
  { label: '用户总数', value: overview.value.userTotal ?? 0, icon: 'UserFilled', color: '#2f54eb', bg: '#eef2ff', tag: 'sys_user', tagType: 'primary' },
  { label: '启用中用户', value: overview.value.userEnabled ?? 0, icon: 'CircleCheckFilled', color: '#16a34a', bg: '#e8f8ee', tag: '正常', tagType: 'success' },
  { label: '管理员总数', value: overview.value.adminTotal ?? 0, icon: 'Avatar', color: '#e63946', bg: '#fdecee', tag: 'sys_admin', tagType: 'danger' },
  { label: '启用中管理员', value: overview.value.adminEnabled ?? 0, icon: 'StarFilled', color: '#f59e0b', bg: '#fff7e6', tag: '在线', tagType: 'warning' }
])

const quicks = [
  { title: '管理员管理', desc: '维护后台账号与权限', icon: 'Avatar', color: '#e63946', bg: '#fdecee', path: '/back/admin' },
  { title: '用户管理', desc: '维护前台普通用户', icon: 'UserFilled', color: '#2f54eb', bg: '#eef2ff', path: '/back/user' },
  { title: '个人信息', desc: '查看与完善我的资料', icon: 'Postcard', color: '#7048e8', bg: '#f3edff', path: '/back/adminPerson' },
  { title: '修改密码', desc: '定期更换登录密码', icon: 'Lock', color: '#f59e0b', bg: '#fff7e6', path: '/back/password' }
]

onMounted(async () => {
  try {
    const res = await dashboardOverview()
    overview.value = res.data || {}
  } catch (e) {
    /* 忽略，看板非关键路径 */
  }
})
</script>

<style scoped>
.back-home {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

/* ---------------- 欢迎条 ---------------- */
.welcome {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
  padding: 28px 32px;
  border-radius: 18px;
  overflow: hidden;
  color: #fff;
  background: linear-gradient(105deg, #b81c2b 0%, #e63946 48%, #ff8a94 100%);
  box-shadow: 0 18px 42px -22px rgba(230, 57, 70, 0.9);
}
.welcome-deco {
  position: absolute;
  inset: 0;
  background-image: radial-gradient(rgba(255, 255, 255, 0.18) 1px, transparent 1px);
  background-size: 20px 20px;
  opacity: 0.45;
}
.welcome::after {
  content: '';
  position: absolute;
  width: 260px;
  height: 260px;
  right: -60px;
  top: -120px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.22), transparent 68%);
}
.welcome-text {
  position: relative;
  z-index: 1;
}
.welcome .tag {
  display: inline-block;
  padding: 3px 10px;
  border-radius: 999px;
  font-size: 11px;
  letter-spacing: 1.2px;
  background: rgba(255, 255, 255, 0.18);
  border: 1px solid rgba(255, 255, 255, 0.24);
  margin-bottom: 10px;
}
.welcome-text h1 {
  margin: 0 0 7px;
  font-size: 23px;
  font-weight: 700;
}
.welcome-text p {
  margin: 0;
  font-size: 13px;
  opacity: 0.85;
}
.welcome-actions {
  position: relative;
  z-index: 1;
  display: flex;
  gap: 10px;
  flex: none;
}
.welcome-actions :deep(.el-button--primary) {
  background: #fff;
  color: #b81c2b;
  border-color: #fff;
  font-weight: 600;
}
.welcome-actions :deep(.el-button--primary:hover) {
  background: #fff5f6;
  border-color: #fff5f6;
  color: #b81c2b;
}
.welcome-actions :deep(.el-button--default) {
  background: rgba(255, 255, 255, 0.16);
  border-color: rgba(255, 255, 255, 0.45);
  color: #fff;
}
.welcome-actions :deep(.el-button--default:hover) {
  background: rgba(255, 255, 255, 0.26);
  border-color: #fff;
  color: #fff;
}
.mr6 { margin-right: 6px; }

/* ---------------- 统计卡 ---------------- */
.stat-row {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 14px;
}
.stat-card {
  display: flex;
  flex-direction: column;
  gap: 6px;
  padding: 18px 20px;
  border-radius: 15px;
  background: #fff;
  border: 1px solid #eceff6;
  transition: transform 0.22s ease, box-shadow 0.22s ease;
}
.stat-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 16px 32px -20px rgba(30, 41, 90, 0.5);
}
.stat-top {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.stat-icon {
  width: 40px;
  height: 40px;
  border-radius: 12px;
  display: grid;
  place-items: center;
}
.stat-value {
  font-size: 26px;
  font-weight: 700;
  color: #0f172a;
  line-height: 1.15;
}
.stat-label {
  font-size: 12.5px;
  color: #94a3b8;
}

/* ---------------- 内容 ---------------- */
.content-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}
.panel {
  background: #fff;
  border-radius: 16px;
  border: 1px solid #eceff6;
  padding: 18px 20px;
}
.panel-title {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 0 0 14px;
  font-size: 15px;
  font-weight: 700;
  color: #0f172a;
}
.quick-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}
.quick-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 13px 14px;
  border-radius: 12px;
  background: #fafbfe;
  border: 1px solid #eff2f8;
  cursor: pointer;
  transition: all 0.22s ease;
}
.quick-item:hover {
  background: #fff;
  border-color: #dfe6f5;
  transform: translateY(-2px);
  box-shadow: 0 12px 24px -18px rgba(30, 41, 90, 0.55);
}
.quick-icon {
  width: 36px;
  height: 36px;
  border-radius: 11px;
  display: grid;
  place-items: center;
  flex: none;
}
.quick-body {
  display: flex;
  flex-direction: column;
  line-height: 1.35;
  flex: 1;
  min-width: 0;
}
.quick-body b {
  font-size: 13.5px;
  color: #1e293b;
}
.quick-body span {
  font-size: 11.5px;
  color: #94a3b8;
}
.quick-arrow {
  color: #cbd5e1;
  transition: transform 0.22s ease, color 0.22s ease;
}
.quick-item:hover .quick-arrow {
  color: #e63946;
  transform: translateX(3px);
}

.guide-collapse :deep(.el-collapse-item__header) {
  font-size: 13.5px;
  font-weight: 600;
  color: #334155;
}
.guide-collapse :deep(.el-collapse-item__content) {
  font-size: 13px;
  line-height: 1.8;
  color: #64748b;
}
.guide-collapse code {
  padding: 1px 6px;
  border-radius: 5px;
  background: #f1f5f9;
  color: #e63946;
  font-size: 12px;
}

@media (max-width: 1100px) {
  .stat-row { grid-template-columns: repeat(2, 1fr); }
  .content-row { grid-template-columns: 1fr; }
}
@media (max-width: 700px) {
  .welcome { flex-direction: column; align-items: flex-start; padding: 22px 20px; }
  .stat-row { grid-template-columns: 1fr; }
  .quick-grid { grid-template-columns: 1fr; }
}
</style>

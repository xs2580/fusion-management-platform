<template>
  <div class="front-home">
    <!-- ============ 欢迎横幅 ============ -->
    <section class="hero">
      <div class="hero-deco"></div>
      <div class="hero-text">
        <span class="hero-tag">FUSION PLATFORM · 用户端</span>
        <h1>你好，{{ userStore.nickname }} 👋</h1>
        <p>欢迎回到融合管理系统平台，今天也要高效工作哦。</p>
        <div class="hero-actions">
          <el-button type="primary" size="large" @click="router.push('/front/workspace')">
            <el-icon class="mr6"><Grid /></el-icon>进入我的工作台
          </el-button>
          <el-button size="large" plain @click="router.push('/front/profile')">查看个人资料</el-button>
        </div>
      </div>
      <div class="hero-avatar">
        <img :src="userStore.avatar" alt="avatar" />
        <div class="ring"></div>
      </div>
    </section>

    <!-- ============ 数据卡片 ============ -->
    <section class="stat-row">
      <div v-for="item in stats" :key="item.label" class="stat-card">
        <div class="stat-icon" :style="{ background: item.bg, color: item.color }">
          <el-icon :size="20"><component :is="item.icon" /></el-icon>
        </div>
        <div class="stat-body">
          <b>{{ item.value }}</b>
          <span>{{ item.label }}</span>
        </div>
      </div>
    </section>

    <!-- ============ 功能 + 动态 ============ -->
    <section class="content-row">
      <div class="panel">
        <div class="panel-head">
          <h3><el-icon><MagicStick /></el-icon> 平台能力</h3>
        </div>
        <div class="feature-grid">
          <div v-for="f in features" :key="f.title" class="feature-item">
            <div class="feature-icon" :style="{ background: f.bg, color: f.color }">
              <el-icon :size="18"><component :is="f.icon" /></el-icon>
            </div>
            <b>{{ f.title }}</b>
            <span>{{ f.desc }}</span>
          </div>
        </div>
      </div>

      <div class="panel">
        <div class="panel-head">
          <h3><el-icon><Bell /></el-icon> 系统动态</h3>
        </div>
        <ul class="timeline">
          <li v-for="(log, i) in logs" :key="i">
            <span class="dot" :style="{ background: log.color }"></span>
            <div class="log-body">
              <b>{{ log.title }}</b>
              <span>{{ log.time }}</span>
            </div>
          </li>
        </ul>
      </div>
    </section>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'

const router = useRouter()
const userStore = useUserStore()

const stats = computed(() => [
  { label: '账号状态', value: '正常', icon: 'CircleCheckFilled', color: '#16a34a', bg: '#e8f8ee' },
  { label: '登录身份', value: '普通用户', icon: 'UserFilled', color: '#2f54eb', bg: '#eef2ff' },
  { label: '可用能力', value: '12 项', icon: 'Grid', color: '#7048e8', bg: '#f3edff' },
  { label: '数据权限', value: '只读', icon: 'Lock', color: '#f59e0b', bg: '#fff7e6' }
])

const features = [
  { icon: 'Document', title: '数据查询', desc: '多维度检索平台数据', color: '#2f54eb', bg: '#eef2ff' },
  { icon: 'DataAnalysis', title: '可视化看板', desc: '图表化呈现关键指标', color: '#7048e8', bg: '#f3edff' },
  { icon: 'Files', title: '文件中心', desc: '上传下载与统一归档', color: '#16a34a', bg: '#e8f8ee' },
  { icon: 'Bell', title: '消息通知', desc: '重要变动即时触达', color: '#f59e0b', bg: '#fff7e6' },
  { icon: 'TrendCharts', title: '趋势分析', desc: '按周期洞察业务走向', color: '#0ea5e9', bg: '#e6f6fe' },
  { icon: 'Setting', title: '个人设置', desc: '资料与安全策略维护', color: '#e63946', bg: '#fdecee' }
]

const logs = [
  { title: '欢迎使用融合管理系统平台', time: '刚刚', color: '#2f54eb' },
  { title: '系统已完成初始化，数据表创建成功', time: '10 分钟前', color: '#16a34a' },
  { title: '管理员可通过后台「角色管理」维护账号', time: '30 分钟前', color: '#7048e8' },
  { title: '请及时前往个人中心完善您的资料', time: '1 小时前', color: '#f59e0b' }
]
</script>

<style scoped>
.front-home {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

/* ---------------- Hero ---------------- */
.hero {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
  padding: 34px 38px;
  border-radius: 20px;
  overflow: hidden;
  color: #fff;
  background: linear-gradient(130deg, #1d39c4 0%, #2f54eb 45%, #7048e8 100%);
  box-shadow: 0 20px 46px -22px rgba(47, 84, 235, 0.85);
}
.hero-deco {
  position: absolute;
  inset: 0;
  background-image: radial-gradient(rgba(255, 255, 255, 0.18) 1px, transparent 1px);
  background-size: 20px 20px;
  opacity: 0.5;
}
.hero::after {
  content: '';
  position: absolute;
  width: 280px;
  height: 280px;
  right: -70px;
  top: -110px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.22), transparent 68%);
}
.hero-text {
  position: relative;
  z-index: 1;
}
.hero-tag {
  display: inline-block;
  padding: 3px 10px;
  border-radius: 999px;
  font-size: 11px;
  letter-spacing: 1.2px;
  background: rgba(255, 255, 255, 0.18);
  border: 1px solid rgba(255, 255, 255, 0.24);
  margin-bottom: 12px;
}
.hero-text h1 {
  margin: 0 0 8px;
  font-size: 26px;
  font-weight: 700;
}
.hero-text p {
  margin: 0 0 20px;
  font-size: 13.5px;
  opacity: 0.82;
}
.hero-actions {
  display: flex;
  gap: 12px;
}
.hero-actions :deep(.el-button--primary) {
  background: #fff;
  color: #2f54eb;
  border-color: #fff;
  font-weight: 600;
}
.hero-actions :deep(.el-button--primary:hover) {
  background: #f0f5ff;
  border-color: #f0f5ff;
  color: #1d39c4;
}
.hero-actions :deep(.el-button--default) {
  background: rgba(255, 255, 255, 0.14);
  border-color: rgba(255, 255, 255, 0.4);
  color: #fff;
}
.hero-actions :deep(.el-button--default:hover) {
  background: rgba(255, 255, 255, 0.24);
  border-color: #fff;
  color: #fff;
}
.mr6 {
  margin-right: 6px;
}

.hero-avatar {
  position: relative;
  z-index: 1;
  flex: none;
  width: 116px;
  height: 116px;
}
.hero-avatar img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  object-fit: cover;
  border: 4px solid rgba(255, 255, 255, 0.9);
  box-shadow: 0 14px 30px -10px rgba(0, 0, 0, 0.45);
}
.hero-avatar .ring {
  position: absolute;
  inset: -8px;
  border-radius: 50%;
  border: 2px dashed rgba(255, 255, 255, 0.45);
  animation: spin 18s linear infinite;
}
@keyframes spin {
  to { transform: rotate(360deg); }
}

/* ---------------- 统计卡 ---------------- */
.stat-row {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}
.stat-card {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 18px 20px;
  border-radius: 14px;
  background: #fff;
  border: 1px solid #eceff6;
  transition: transform 0.22s ease, box-shadow 0.22s ease;
}
.stat-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 14px 30px -16px rgba(30, 41, 90, 0.4);
}
.stat-icon {
  width: 42px;
  height: 42px;
  border-radius: 12px;
  display: grid;
  place-items: center;
  flex: none;
}
.stat-body {
  display: flex;
  flex-direction: column;
  gap: 2px;
}
.stat-body b {
  font-size: 18px;
  color: #0f172a;
}
.stat-body span {
  font-size: 12.5px;
  color: #94a3b8;
}

/* ---------------- 内容区 ---------------- */
.content-row {
  display: grid;
  grid-template-columns: 1.5fr 1fr;
  gap: 16px;
}
.panel {
  background: #fff;
  border-radius: 16px;
  border: 1px solid #eceff6;
  padding: 20px 22px;
}
.panel-head h3 {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 0 0 16px;
  font-size: 15px;
  font-weight: 700;
  color: #0f172a;
}

.feature-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}
.feature-item {
  display: flex;
  flex-direction: column;
  gap: 5px;
  padding: 14px;
  border-radius: 12px;
  background: #fafbfe;
  border: 1px solid #eff2f8;
  transition: all 0.22s ease;
}
.feature-item:hover {
  background: #fff;
  border-color: #dbe3f7;
  transform: translateY(-2px);
}
.feature-icon {
  width: 34px;
  height: 34px;
  border-radius: 10px;
  display: grid;
  place-items: center;
  margin-bottom: 4px;
}
.feature-item b {
  font-size: 13.5px;
  color: #1e293b;
}
.feature-item span {
  font-size: 12px;
  color: #94a3b8;
}

.timeline {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  gap: 2px;
}
.timeline li {
  position: relative;
  display: flex;
  gap: 12px;
  padding: 9px 0;
}
.timeline li:not(:last-child)::before {
  content: '';
  position: absolute;
  left: 4px;
  top: 26px;
  bottom: -2px;
  width: 1px;
  background: #e9edf5;
}
.timeline .dot {
  width: 9px;
  height: 9px;
  border-radius: 50%;
  margin-top: 5px;
  flex: none;
  box-shadow: 0 0 0 3px rgba(47, 84, 235, 0.08);
}
.log-body {
  display: flex;
  flex-direction: column;
  gap: 2px;
}
.log-body b {
  font-size: 13px;
  font-weight: 500;
  color: #334155;
}
.log-body span {
  font-size: 11.5px;
  color: #a8b2c1;
}

@media (max-width: 1080px) {
  .stat-row { grid-template-columns: repeat(2, 1fr); }
  .content-row { grid-template-columns: 1fr; }
  .feature-grid { grid-template-columns: repeat(2, 1fr); }
}
@media (max-width: 680px) {
  .hero { flex-direction: column; align-items: flex-start; padding: 26px 22px; }
  .hero-avatar { width: 84px; height: 84px; }
  .stat-row { grid-template-columns: 1fr; }
}
</style>

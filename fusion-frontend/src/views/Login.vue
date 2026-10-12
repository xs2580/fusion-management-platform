<template>
  <div class="login-page">
    <!-- ============ 背景装饰 ============ -->
    <div class="bg-orb orb-1"></div>
    <div class="bg-orb orb-2"></div>
    <div class="bg-orb orb-3"></div>
    <div class="bg-grid"></div>

    <!-- ============ 登录卡片 ============ -->
    <div class="login-card" :class="{ shake: shake }">
      <!-- ---------- 左：品牌区 ---------- -->
      <aside class="brand-side">
        <div class="brand-deco"></div>

        <div class="brand-head">
          <div class="brand-logo">
            <svg viewBox="0 0 48 48" width="30" height="30" fill="none">
              <circle cx="24" cy="24" r="7" fill="#fff" />
              <circle cx="24" cy="7" r="4.6" fill="#fff" opacity=".92" />
              <circle cx="38.7" cy="15.5" r="4.6" fill="#fff" opacity=".92" />
              <circle cx="38.7" cy="32.5" r="4.6" fill="#fff" opacity=".92" />
              <circle cx="24" cy="41" r="4.6" fill="#fff" opacity=".92" />
              <circle cx="9.3" cy="32.5" r="4.6" fill="#fff" opacity=".92" />
              <circle cx="9.3" cy="15.5" r="4.6" fill="#fff" opacity=".92" />
              <g stroke="#fff" stroke-width="1.6" opacity=".75">
                <path d="M24 7v10M38.7 15.5 27.5 21.4M38.7 32.5 27.5 27.4M24 41V31M9.3 32.5 20.5 27.4M9.3 15.5 20.5 21.4" />
              </g>
            </svg>
          </div>
          <div class="brand-name">
            <h1>融合管理系统平台</h1>
            <p>FUSION MANAGEMENT PLATFORM</p>
          </div>
        </div>

        <div class="brand-slogan">
          <h2>让数据融合<br />让管理更简单</h2>
          <p>SpringBoot 3 · MyBatis-Plus · Vue3 前后端分离解决方案</p>
        </div>

        <ul class="brand-features">
          <li v-for="item in features" :key="item.title">
            <span class="feat-icon">
              <el-icon :size="17"><component :is="item.icon" /></el-icon>
            </span>
            <span class="feat-text">
              <b>{{ item.title }}</b>
              <i>{{ item.desc }}</i>
            </span>
          </li>
        </ul>

        <div class="brand-foot">© 2026 融合管理系统平台 · 保留所有权利</div>
      </aside>

      <!-- ---------- 右：表单区 ---------- -->
      <section class="form-side">
        <header class="form-head">
          <h2>欢迎回来 👋</h2>
          <p>请登录您的账号，继续访问融合管理系统平台</p>
        </header>

        <!-- 身份切换 -->
        <div class="role-switch">
          <div class="role-pill" :style="{ transform: `translateX(${roleIndex * 100}%)` }"></div>
          <button
            v-for="(role, i) in roles"
            :key="role.value"
            type="button"
            class="role-item"
            :class="{ active: roleIndex === i }"
            @click="switchRole(role.value, i)"
          >
            <el-icon :size="15"><component :is="role.icon" /></el-icon>
            <span>{{ role.label }}</span>
          </button>
        </div>

        <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          class="login-form"
          label-position="top"
          @keyup.enter="handleLogin"
        >
          <el-form-item prop="username">
            <el-input
              v-model.trim="form.username"
              size="large"
              placeholder="请输入登录账号"
              :prefix-icon="User"
              clearable
            />
          </el-form-item>

          <el-form-item prop="password">
            <el-input
              v-model.trim="form.password"
              size="large"
              type="password"
              placeholder="请输入登录密码"
              :prefix-icon="Lock"
              show-password
            />
          </el-form-item>

          <div class="form-extra">
            <el-checkbox v-model="form.remember">记住账号</el-checkbox>
            <span class="link" @click="forgotTip">忘记密码？</span>
          </div>

          <el-button
            class="submit-btn"
            type="primary"
            size="large"
            :loading="loading"
            @click="handleLogin"
          >
            {{ loading ? '正在登录…' : '登 录' }}
          </el-button>
        </el-form>

        <!-- 演示账号 -->
        <div class="demo-box">
          <div class="demo-title"><span>体验账号（点击自动填充）</span></div>
          <div class="demo-list">
            <button
              v-for="acc in demoAccounts"
              :key="acc.username"
              type="button"
              class="demo-chip"
              @click="fillDemo(acc)"
            >
              <span class="dot" :style="{ background: acc.color }"></span>
              <b>{{ acc.label }}</b>
              <em>{{ acc.username }} / {{ acc.password }}</em>
            </button>
          </div>
        </div>

        <p class="to-register">
          还没有账号？
          <span class="link strong" @click="goRegister">立即注册</span>
        </p>
      </section>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { useUserStore } from '@/store/user'
import { getRemembered } from '@/utils/auth'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const formRef = ref(null)
const loading = ref(false)
const shake = ref(false)

/** 身份选项 */
const roles = [
  { label: '普通用户', value: 'user', icon: 'User' },
  { label: '管理员', value: 'admin', icon: 'Avatar' }
]

/** 左侧能力介绍 */
const features = [
  { icon: 'UserFilled', title: '用户管理', desc: '全维度的用户与权限管控体系' },
  { icon: 'Lock', title: '安全可靠', desc: 'JWT 鉴权 + 统一返回 + 全局异常' },
  { icon: 'DataAnalysis', title: '数据融合', desc: '多源数据统一接入与可视化' }
]

/** 体验账号 */
const demoAccounts = [
  { label: '普通用户', username: 'user', password: '123456', loginType: 'user', color: '#2f54eb' },
  { label: '管理员', username: 'admin', password: 'admin', loginType: 'admin', color: '#e63946' }
]

/** 从"记住账号"回填 */
const remembered = getRemembered() || {}
const initType = remembered.loginType === 'admin' ? 'admin' : 'user'

const form = reactive({
  username: remembered.username || '',
  password: '',
  loginType: initType,
  remember: !!remembered.username
})

const roleIndex = computed(() => roles.findIndex((r) => r.value === form.loginType))

const rules = {
  username: [
    { required: true, message: '请输入登录账号', trigger: 'blur' },
    { min: 2, max: 50, message: '账号长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入登录密码', trigger: 'blur' },
    { min: 5, message: '密码长度不少于 5 个字符', trigger: 'blur' }
  ]
}

/** 切换身份 */
function switchRole(value) {
  form.loginType = value
  formRef.value?.clearValidate()
}

/** 一键填充体验账号 */
function fillDemo(acc) {
  form.loginType = acc.loginType
  form.username = acc.username
  form.password = acc.password
  formRef.value?.clearValidate()
  ElMessage.success(`已填充${acc.label}账号，点击登录即可`)
}

function goRegister() {
  router.push('/register')
}

function forgotTip() {
  ElMessageBox.alert('本脚手架未接入短信/邮箱找回，请联系系统管理员在「角色管理」中重置密码。', '忘记密码', {
    confirmButtonText: '我知道了',
    type: 'info'
  })
}

function triggerShake() {
  shake.value = true
  setTimeout(() => (shake.value = false), 460)
}

async function handleLogin() {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
  } catch (e) {
    return
  }

  loading.value = true
  try {
    const data = await userStore.login({ ...form })
    ElMessage.success(`欢迎回来，${data.nickname || data.username}！`)

    const redirect = route.query.redirect
    if (redirect && typeof redirect === 'string') {
      router.replace(redirect)
    } else {
      router.replace(data.loginType === 'admin' ? '/back/home' : '/front/home')
    }
  } catch (e) {
    triggerShake()
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
/* ==================== 页面容器 ==================== */
.login-page {
  position: relative;
  min-height: 100vh;
  width: 100%;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 28px;
  background: linear-gradient(135deg, #eef2ff 0%, #f7f9fc 45%, #fdf2f8 100%);
}

/* 背景光斑 */
.bg-orb {
  position: absolute;
  border-radius: 50%;
  filter: blur(70px);
  opacity: 0.55;
  pointer-events: none;
}
.orb-1 {
  width: 460px;
  height: 460px;
  top: -140px;
  left: -120px;
  background: radial-gradient(circle, #a5b4fc 0%, rgba(165, 180, 252, 0) 70%);
  animation: float1 16s ease-in-out infinite;
}
.orb-2 {
  width: 520px;
  height: 520px;
  bottom: -180px;
  right: -140px;
  background: radial-gradient(circle, #f0abfc 0%, rgba(240, 171, 252, 0) 70%);
  animation: float2 20s ease-in-out infinite;
}
.orb-3 {
  width: 380px;
  height: 380px;
  top: 45%;
  right: 26%;
  background: radial-gradient(circle, #93c5fd 0%, rgba(147, 197, 253, 0) 70%);
  opacity: 0.4;
  animation: float1 24s ease-in-out infinite reverse;
}
@keyframes float1 {
  0%, 100% { transform: translate(0, 0) scale(1); }
  50% { transform: translate(40px, 30px) scale(1.08); }
}
@keyframes float2 {
  0%, 100% { transform: translate(0, 0) scale(1); }
  50% { transform: translate(-50px, -34px) scale(1.06); }
}

/* 网格底纹 */
.bg-grid {
  position: absolute;
  inset: 0;
  background-image: linear-gradient(rgba(47, 84, 235, 0.045) 1px, transparent 1px),
    linear-gradient(90deg, rgba(47, 84, 235, 0.045) 1px, transparent 1px);
  background-size: 46px 46px;
  mask-image: radial-gradient(ellipse 80% 70% at 50% 50%, #000 35%, transparent 100%);
  -webkit-mask-image: radial-gradient(ellipse 80% 70% at 50% 50%, #000 35%, transparent 100%);
  pointer-events: none;
}

/* ==================== 卡片 ==================== */
.login-card {
  position: relative;
  z-index: 2;
  display: flex;
  width: min(1000px, 100%);
  min-height: 596px;
  border-radius: 26px;
  overflow: hidden;
  background: #fff;
  box-shadow: 0 30px 90px -20px rgba(30, 41, 90, 0.32), 0 4px 16px rgba(30, 41, 90, 0.06);
}
.login-card.shake {
  animation: shake 0.44s cubic-bezier(0.36, 0.07, 0.19, 0.97);
}
@keyframes shake {
  10%, 90% { transform: translateX(-2px); }
  20%, 80% { transform: translateX(4px); }
  30%, 50%, 70% { transform: translateX(-7px); }
  40%, 60% { transform: translateX(7px); }
}

/* ==================== 左侧品牌区 ==================== */
.brand-side {
  position: relative;
  width: 44%;
  padding: 42px 38px;
  display: flex;
  flex-direction: column;
  color: #fff;
  background: linear-gradient(155deg, #1d39c4 0%, #2f54eb 42%, #6d33d6 100%);
  overflow: hidden;
}
.brand-deco {
  position: absolute;
  inset: 0;
  background-image: radial-gradient(rgba(255, 255, 255, 0.22) 1px, transparent 1px);
  background-size: 22px 22px;
  opacity: 0.5;
  pointer-events: none;
}
.brand-side::after {
  content: '';
  position: absolute;
  width: 320px;
  height: 320px;
  right: -140px;
  bottom: -120px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.22), transparent 68%);
  pointer-events: none;
}

.brand-head {
  position: relative;
  display: flex;
  align-items: center;
  gap: 12px;
}
.brand-logo {
  width: 46px;
  height: 46px;
  border-radius: 14px;
  display: grid;
  place-items: center;
  background: rgba(255, 255, 255, 0.16);
  border: 1px solid rgba(255, 255, 255, 0.28);
  backdrop-filter: blur(6px);
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.35);
}
.brand-name h1 {
  margin: 0;
  font-size: 17px;
  font-weight: 700;
  letter-spacing: 0.5px;
}
.brand-name p {
  margin: 3px 0 0;
  font-size: 10px;
  letter-spacing: 1.6px;
  opacity: 0.62;
}

.brand-slogan {
  position: relative;
  margin: 44px 0 30px;
}
.brand-slogan h2 {
  margin: 0 0 12px;
  font-size: 27px;
  line-height: 1.42;
  font-weight: 700;
  letter-spacing: 1px;
}
.brand-slogan p {
  margin: 0;
  font-size: 12.5px;
  line-height: 1.7;
  opacity: 0.7;
}

.brand-features {
  position: relative;
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  gap: 12px;
  flex: 1;
}
.brand-features li {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 14px;
  border-radius: 13px;
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.14);
  backdrop-filter: blur(4px);
  transition: transform 0.25s ease, background 0.25s ease;
}
.brand-features li:hover {
  transform: translateX(5px);
  background: rgba(255, 255, 255, 0.17);
}
.feat-icon {
  width: 34px;
  height: 34px;
  flex: none;
  border-radius: 10px;
  display: grid;
  place-items: center;
  background: rgba(255, 255, 255, 0.2);
}
.feat-text {
  display: flex;
  flex-direction: column;
  gap: 2px;
  line-height: 1.3;
}
.feat-text b {
  font-size: 13.5px;
  font-weight: 600;
}
.feat-text i {
  font-style: normal;
  font-size: 11.5px;
  opacity: 0.66;
}

.brand-foot {
  position: relative;
  margin-top: 26px;
  font-size: 11.5px;
  opacity: 0.5;
  letter-spacing: 0.3px;
}

/* ==================== 右侧表单区 ==================== */
.form-side {
  flex: 1;
  padding: 46px 54px 38px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  background: #fff;
}

.form-head h2 {
  margin: 0 0 8px;
  font-size: 25px;
  font-weight: 700;
  color: var(--fusion-text-1);
  letter-spacing: 0.5px;
}
.form-head p {
  margin: 0 0 24px;
  font-size: 13px;
  color: var(--fusion-text-3);
}

/* ---- 身份切换 ---- */
.role-switch {
  position: relative;
  display: flex;
  padding: 4px;
  border-radius: 12px;
  background: #f1f4fa;
  margin-bottom: 22px;
}
.role-pill {
  position: absolute;
  top: 4px;
  left: 4px;
  width: calc(50% - 4px);
  height: calc(100% - 8px);
  border-radius: 9px;
  background: #fff;
  box-shadow: 0 3px 10px rgba(30, 41, 90, 0.12);
  transition: transform 0.3s cubic-bezier(0.34, 1.3, 0.64, 1);
}
.role-item {
  position: relative;
  z-index: 1;
  flex: 1;
  height: 38px;
  border: 0;
  background: transparent;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  border-radius: 9px;
  font-size: 13.5px;
  font-weight: 600;
  color: var(--fusion-text-3);
  transition: color 0.25s ease;
  font-family: inherit;
}
.role-item.active {
  color: var(--fusion-primary);
}

/* ---- 表单 ---- */
.login-form :deep(.el-form-item) {
  margin-bottom: 18px;
}
.login-form :deep(.el-input__wrapper) {
  height: 46px;
  border-radius: 11px;
  background: #f8fafc;
  box-shadow: 0 0 0 1px #e6e9f0 inset;
  transition: box-shadow 0.22s ease, background 0.22s ease;
}
.login-form :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #c7d2fe inset;
}
.login-form :deep(.el-input__wrapper.is-focus) {
  background: #fff;
  box-shadow: 0 0 0 1px var(--fusion-primary) inset, 0 0 0 4px rgba(47, 84, 235, 0.12);
}
.login-form :deep(.el-input__prefix) {
  color: #94a3b8;
}
.login-form :deep(.el-input__inner) {
  font-size: 14px;
}

.form-extra {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin: -4px 0 18px;
}

.submit-btn {
  width: 100%;
  height: 46px;
  border: 0;
  border-radius: 11px;
  font-size: 15px;
  font-weight: 600;
  letter-spacing: 3px;
  background: linear-gradient(135deg, #3b5bdb 0%, #2f54eb 45%, #7048e8 100%);
  box-shadow: 0 12px 26px -10px rgba(47, 84, 235, 0.75);
  transition: transform 0.2s ease, box-shadow 0.2s ease, filter 0.2s ease;
}
.submit-btn:hover {
  transform: translateY(-1.5px);
  filter: brightness(1.06);
  box-shadow: 0 16px 32px -10px rgba(47, 84, 235, 0.85);
}
.submit-btn:active {
  transform: translateY(0);
}

.link {
  color: var(--fusion-text-3);
  font-size: 13px;
  cursor: pointer;
  transition: color 0.2s ease;
}
.link:hover {
  color: var(--fusion-primary);
}
.link.strong {
  color: var(--fusion-primary);
  font-weight: 600;
}

/* ---- 体验账号 ---- */
.demo-box {
  margin-top: 22px;
  padding: 14px 16px;
  border-radius: 12px;
  background: #f8fafc;
  border: 1px dashed #dbe3f0;
}
.demo-title {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 10px;
  font-size: 12px;
  color: var(--fusion-text-4);
}
.demo-title::before,
.demo-title::after {
  content: '';
  height: 1px;
  flex: 1;
  background: #e6e9f0;
}
.demo-title span {
  white-space: nowrap;
}
.demo-list {
  display: flex;
  gap: 10px;
}
.demo-chip {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 3px;
  padding: 9px 12px;
  border-radius: 10px;
  border: 1px solid #e6e9f0;
  background: #fff;
  cursor: pointer;
  text-align: left;
  font-family: inherit;
  transition: all 0.22s ease;
}
.demo-chip:hover {
  border-color: var(--fusion-primary);
  transform: translateY(-2px);
  box-shadow: 0 8px 18px -8px rgba(47, 84, 235, 0.5);
}
.demo-chip .dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  margin-bottom: 2px;
}
.demo-chip b {
  font-size: 12.5px;
  color: var(--fusion-text-2);
}
.demo-chip em {
  font-style: normal;
  font-size: 11.5px;
  color: var(--fusion-text-4);
  letter-spacing: 0.2px;
}

.to-register {
  margin: 20px 0 0;
  text-align: center;
  font-size: 13px;
  color: var(--fusion-text-3);
}

/* ==================== 响应式 ==================== */
@media (max-width: 900px) {
  .brand-side {
    display: none;
  }
  .login-card {
    width: min(460px, 100%);
    min-height: auto;
  }
  .form-side {
    padding: 36px 26px 28px;
  }
}
@media (max-width: 420px) {
  .demo-list {
    flex-direction: column;
  }
}
</style>

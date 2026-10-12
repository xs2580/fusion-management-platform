<template>
  <div class="register-page">
    <div class="bg-orb orb-1"></div>
    <div class="bg-orb orb-2"></div>

    <div class="register-card">
      <header class="card-head">
        <div class="logo">
          <el-icon :size="22" color="#fff"><Connection /></el-icon>
        </div>
        <h1>创建您的账号</h1>
        <p>注册成功后即可进入融合管理系统平台前台</p>
      </header>

      <el-form ref="formRef" :model="form" :rules="rules" label-position="top" @keyup.enter="handleRegister">
        <el-form-item label="登录账号" prop="username">
          <el-input v-model.trim="form.username" size="large" placeholder="字母开头，3-20 位" :prefix-icon="User" />
        </el-form-item>

        <div class="grid-2">
          <el-form-item label="登录密码" prop="password">
            <el-input v-model.trim="form.password" size="large" type="password" placeholder="6-20 位" :prefix-icon="Lock" show-password />
          </el-form-item>
          <el-form-item label="确认密码" prop="confirmPassword">
            <el-input v-model.trim="form.confirmPassword" size="large" type="password" placeholder="再次输入密码" :prefix-icon="Lock" show-password />
          </el-form-item>
        </div>

        <el-form-item label="昵称" prop="nickname">
          <el-input v-model.trim="form.nickname" size="large" placeholder="选填，默认与账号一致" :prefix-icon="Postcard" />
        </el-form-item>

        <div class="grid-2">
          <el-form-item label="邮箱" prop="email">
            <el-input v-model.trim="form.email" size="large" placeholder="选填" :prefix-icon="Message" />
          </el-form-item>
          <el-form-item label="手机号" prop="phone">
            <el-input v-model.trim="form.phone" size="large" placeholder="选填" :prefix-icon="Iphone" />
          </el-form-item>
        </div>

        <el-button class="submit-btn" type="primary" size="large" :loading="loading" @click="handleRegister">
          {{ loading ? '正在注册…' : '注 册 并 登 录' }}
        </el-button>
      </el-form>

      <p class="to-login">
        已有账号？
        <span class="link" @click="router.push('/login')">返回登录</span>
      </p>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock, Message, Iphone, Postcard } from '@element-plus/icons-vue'
import { register } from '@/api/auth'
import { useUserStore } from '@/store/user'
import { setToken, setUser } from '@/utils/auth'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref(null)
const loading = ref(false)

const form = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  nickname: '',
  email: '',
  phone: ''
})

const validateConfirm = (rule, value, callback) => {
  if (!value) return callback(new Error('请再次输入密码'))
  if (value !== form.password) return callback(new Error('两次输入的密码不一致'))
  callback()
}

const rules = {
  username: [
    { required: true, message: '请输入登录账号', trigger: 'blur' },
    { pattern: /^[a-zA-Z][a-zA-Z0-9_]{2,19}$/, message: '字母开头，3-20 位字母/数字/下划线', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入登录密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度 6-20 位', trigger: 'blur' }
  ],
  confirmPassword: [{ required: true, validator: validateConfirm, trigger: 'blur' }],
  email: [{ type: 'email', message: '邮箱格式不正确', trigger: 'blur' }],
  phone: [{ pattern: /^$|^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }]
}

async function handleRegister() {
  try {
    await formRef.value.validate()
  } catch (e) {
    return
  }
  loading.value = true
  try {
    const res = await register({ ...form })
    const data = res.data || {}
    // 注册成功即写入登录态，免二次登录
    userStore.token = data.token
    userStore.userInfo = data
    userStore.loginType = data.loginType
    setToken(data.token)
    setUser(data)
    ElMessage.success('注册成功，正在进入前台…')
    router.replace('/front/home')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-page {
  position: relative;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 32px 16px;
  overflow: hidden;
  background: linear-gradient(135deg, #eef2ff 0%, #f7f9fc 45%, #fdf2f8 100%);
}
.bg-orb {
  position: absolute;
  border-radius: 50%;
  filter: blur(70px);
  pointer-events: none;
}
.orb-1 {
  width: 440px;
  height: 440px;
  top: -150px;
  left: -110px;
  background: radial-gradient(circle, #a5b4fc 0%, rgba(165, 180, 252, 0) 70%);
  opacity: 0.55;
}
.orb-2 {
  width: 480px;
  height: 480px;
  bottom: -180px;
  right: -140px;
  background: radial-gradient(circle, #f0abfc 0%, rgba(240, 171, 252, 0) 70%);
  opacity: 0.5;
}

.register-card {
  position: relative;
  z-index: 2;
  width: min(560px, 100%);
  padding: 36px 42px 30px;
  border-radius: 22px;
  background: #fff;
  box-shadow: 0 28px 80px -22px rgba(30, 41, 90, 0.3);
}

.card-head {
  text-align: center;
  margin-bottom: 22px;
}
.logo {
  width: 46px;
  height: 46px;
  margin: 0 auto 12px;
  border-radius: 14px;
  display: grid;
  place-items: center;
  background: linear-gradient(135deg, #3b5bdb, #7048e8);
  box-shadow: 0 10px 22px -8px rgba(47, 84, 235, 0.75);
}
.card-head h1 {
  margin: 0 0 6px;
  font-size: 22px;
  font-weight: 700;
}
.card-head p {
  margin: 0;
  font-size: 12.5px;
  color: var(--fusion-text-3);
}

.grid-2 {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 0 16px;
}

.register-card :deep(.el-form-item) {
  margin-bottom: 16px;
}
.register-card :deep(.el-input__wrapper) {
  border-radius: 11px;
  background: #f8fafc;
  box-shadow: 0 0 0 1px #e6e9f0 inset;
}
.register-card :deep(.el-input__wrapper.is-focus) {
  background: #fff;
  box-shadow: 0 0 0 1px var(--fusion-primary) inset, 0 0 0 4px rgba(47, 84, 235, 0.12);
}

.submit-btn {
  width: 100%;
  height: 46px;
  margin-top: 6px;
  border: 0;
  border-radius: 11px;
  font-size: 15px;
  font-weight: 600;
  letter-spacing: 1px;
  background: linear-gradient(135deg, #3b5bdb 0%, #2f54eb 45%, #7048e8 100%);
  box-shadow: 0 12px 26px -10px rgba(47, 84, 235, 0.75);
}
.submit-btn:hover {
  filter: brightness(1.06);
}

.to-login {
  margin: 18px 0 0;
  text-align: center;
  font-size: 13px;
  color: var(--fusion-text-3);
}
.link {
  color: var(--fusion-primary);
  font-weight: 600;
  cursor: pointer;
}

@media (max-width: 520px) {
  .grid-2 {
    grid-template-columns: 1fr;
  }
  .register-card {
    padding: 28px 22px 22px;
  }
}
</style>

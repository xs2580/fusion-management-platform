<template>
  <div class="pwd-page">
    <div class="pwd-card page-card">
      <h2 class="page-title mb-16">修改密码</h2>

      <el-alert
        type="info"
        :closable="false"
        show-icon
        class="mb-16"
        title="为保障账号安全，建议定期更换密码；密码长度 6-20 位，建议包含字母与数字。"
      />

      <el-form ref="formRef" :model="form" :rules="rules" label-width="96px" class="pwd-form">
        <el-form-item label="原密码" prop="oldPassword">
          <el-input v-model.trim="form.oldPassword" type="password" show-password placeholder="请输入当前登录密码" />
        </el-form-item>

        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model.trim="form.newPassword" type="password" show-password placeholder="请输入新密码" />
          <div class="strength">
            <span class="bar" :class="`lv${strength.level}`"></span>
            <em :class="`t${strength.level}`">{{ strength.text }}</em>
          </div>
        </el-form-item>

        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model.trim="form.confirmPassword" type="password" show-password placeholder="请再次输入新密码" />
        </el-form-item>

        <el-form-item>
          <el-button type="danger" :loading="saving" @click="submit">确认修改</el-button>
          <el-button @click="reset">清空</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { computed, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { changePassword } from '@/api/auth'
import { useUserStore } from '@/store/user'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref(null)
const saving = ref(false)

const form = reactive({ oldPassword: '', newPassword: '', confirmPassword: '' })

/** 密码强度计算 */
const strength = computed(() => {
  const p = form.newPassword || ''
  if (!p) return { level: 0, text: '' }
  let score = 0
  if (p.length >= 6) score++
  if (p.length >= 10) score++
  if (/[a-zA-Z]/.test(p) && /\d/.test(p)) score++
  if (/[^a-zA-Z0-9]/.test(p)) score++
  if (score <= 1) return { level: 1, text: '弱' }
  if (score === 2) return { level: 2, text: '中' }
  return { level: 3, text: '强' }
})

const rules = {
  oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度 6-20 位', trigger: 'blur' },
    {
      validator: (rule, value, cb) => {
        if (value && value === form.oldPassword) return cb(new Error('新密码不能与原密码相同'))
        cb()
      },
      trigger: 'blur'
    }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    {
      validator: (rule, value, cb) => {
        if (value !== form.newPassword) return cb(new Error('两次输入的密码不一致'))
        cb()
      },
      trigger: 'blur'
    }
  ]
}

function reset() {
  form.oldPassword = ''
  form.newPassword = ''
  form.confirmPassword = ''
  formRef.value?.clearValidate()
}

async function submit() {
  try {
    await formRef.value.validate()
  } catch (e) {
    return
  }
  saving.value = true
  try {
    await changePassword({ ...form })
    ElMessage.success('密码修改成功，请使用新密码重新登录')
    reset()
    setTimeout(async () => {
      await userStore.logout()
      router.replace('/login')
    }, 900)
  } finally {
    saving.value = false
  }
}
</script>

<style scoped>
.pwd-page {
  display: flex;
  justify-content: center;
}
.pwd-card {
  width: min(620px, 100%);
  padding: 24px 28px 20px;
}
.pwd-form :deep(.el-input__wrapper) {
  border-radius: 10px;
}

.strength {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 8px;
  width: 100%;
}
.strength .bar {
  flex: 1;
  height: 5px;
  border-radius: 4px;
  background: #eef1f7;
  position: relative;
  overflow: hidden;
}
.strength .bar::after {
  content: '';
  position: absolute;
  inset: 0;
  width: 0;
  transition: width 0.3s ease, background 0.3s ease;
}
.strength .bar.lv1::after { width: 33%; background: #e63946; }
.strength .bar.lv2::after { width: 66%; background: #f59e0b; }
.strength .bar.lv3::after { width: 100%; background: #16a34a; }
.strength em {
  font-style: normal;
  font-size: 12px;
  min-width: 20px;
}
.strength .t1 { color: #e63946; }
.strength .t2 { color: #f59e0b; }
.strength .t3 { color: #16a34a; }
</style>

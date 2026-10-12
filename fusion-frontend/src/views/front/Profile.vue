<template>
  <div class="profile-page">
    <el-row :gutter="18">
      <!-- ---------- 左：名片 ---------- -->
      <el-col :xs="24" :sm="24" :md="8">
        <div class="page-card id-card">
          <div class="id-bg"></div>
          <div class="id-avatar">
            <img :src="form.avatar || userStore.avatar" alt="avatar" />
          </div>
          <h3>{{ form.nickname || userStore.nickname }}</h3>
          <p class="id-account">@{{ form.username || userStore.userInfo.username }}</p>
          <el-tag type="success" effect="light" round size="small">普通用户</el-tag>

          <ul class="id-meta">
            <li>
              <el-icon><Message /></el-icon>
              <span>{{ form.email || '未填写邮箱' }}</span>
            </li>
            <li>
              <el-icon><Iphone /></el-icon>
              <span>{{ form.phone || '未填写手机号' }}</span>
            </li>
            <li>
              <el-icon><Clock /></el-icon>
              <span>{{ lastLoginText }}</span>
            </li>
          </ul>

          <el-button class="logout-btn" plain @click="handleLogout">退出登录</el-button>
        </div>
      </el-col>

      <!-- ---------- 右：编辑资料 ---------- -->
      <el-col :xs="24" :sm="24" :md="16">
        <div class="page-card">
          <h2 class="page-title mb-16">个人资料</h2>

          <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
            <el-form-item label="头像">
              <div class="avatar-edit">
                <img :src="form.avatar || userStore.avatar" alt="avatar" />
                <div class="avatar-tips">
                  <el-input v-model.trim="form.avatar" placeholder="粘贴图片地址，或使用默认头像" />
                  <el-button link type="primary" size="small" @click="form.avatar = '/avatar-default.webp'">
                    恢复默认头像
                  </el-button>
                </div>
              </div>
            </el-form-item>

            <el-form-item label="登录账号">
              <el-input v-model="form.username" disabled />
            </el-form-item>

            <el-form-item label="昵称" prop="nickname">
              <el-input v-model.trim="form.nickname" placeholder="请输入昵称" />
            </el-form-item>

            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="form.gender">
                <el-radio :value="1">男</el-radio>
                <el-radio :value="2">女</el-radio>
                <el-radio :value="0">保密</el-radio>
              </el-radio-group>
            </el-form-item>

            <el-form-item label="邮箱" prop="email">
              <el-input v-model.trim="form.email" placeholder="请输入邮箱" />
            </el-form-item>

            <el-form-item label="手机号" prop="phone">
              <el-input v-model.trim="form.phone" placeholder="请输入手机号" />
            </el-form-item>

            <el-form-item>
              <el-button type="primary" :loading="saving" @click="save">保存修改</el-button>
              <el-button @click="reset">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { reactive, ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { updateProfile } from '@/api/auth'
import { useUserStore } from '@/store/user'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref(null)
const saving = ref(false)

const form = reactive({
  username: '',
  nickname: '',
  email: '',
  phone: '',
  avatar: '',
  gender: 0
})

const rules = {
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  email: [{ type: 'email', message: '邮箱格式不正确', trigger: 'blur' }],
  phone: [{ pattern: /^$|^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }]
}

const lastLoginText = computed(() => {
  const t = userStore.userInfo?.lastLogin
  return t ? `最后登录：${t}` : '首次登录'
})

function fill() {
  const u = userStore.userInfo || {}
  form.username = u.username || ''
  form.nickname = u.nickname || ''
  form.email = u.email || ''
  form.phone = u.phone || ''
  form.avatar = u.avatar || ''
  form.gender = u.gender ?? 0
}

function reset() {
  fill()
  formRef.value?.clearValidate()
}

async function save() {
  try {
    await formRef.value.validate()
  } catch (e) {
    return
  }
  saving.value = true
  try {
    const res = await updateProfile({ ...form })
    userStore.patchUser(res.data || {})
    ElMessage.success('资料保存成功')
  } finally {
    saving.value = false
  }
}

async function handleLogout() {
  await ElMessageBox.confirm('确定要退出登录吗？', '退出确认', {
    confirmButtonText: '退出',
    cancelButtonText: '取消',
    type: 'warning'
  })
  await userStore.logout()
  ElMessage.success('已安全退出')
  router.replace('/login')
}

onMounted(async () => {
  fill()
  try {
    await userStore.fetchInfo()
    fill()
  } catch (e) {
    /* 忽略 */
  }
})
</script>

<style scoped>
.id-card {
  position: relative;
  text-align: center;
  overflow: hidden;
  padding-top: 0;
}
.id-bg {
  height: 96px;
  margin: -20px -24px 0;
  background: linear-gradient(130deg, #1d39c4, #2f54eb 50%, #7048e8);
}
.id-avatar {
  margin-top: -44px;
}
.id-avatar img {
  width: 88px;
  height: 88px;
  border-radius: 50%;
  object-fit: cover;
  border: 4px solid #fff;
  box-shadow: 0 10px 24px -10px rgba(30, 41, 90, 0.5);
}
.id-card h3 {
  margin: 12px 0 4px;
  font-size: 17px;
  color: #0f172a;
}
.id-account {
  margin: 0 0 10px;
  font-size: 12.5px;
  color: #94a3b8;
}
.id-meta {
  list-style: none;
  margin: 20px 0 0;
  padding: 16px 0 0;
  border-top: 1px dashed #e6e9f0;
  display: flex;
  flex-direction: column;
  gap: 12px;
  text-align: left;
}
.id-meta li {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 13px;
  color: #475569;
}
.id-meta .el-icon {
  color: #94a3b8;
}
.logout-btn {
  width: 100%;
  margin-top: 20px;
  border-radius: 10px;
}

.avatar-edit {
  display: flex;
  align-items: center;
  gap: 16px;
  width: 100%;
}
.avatar-edit img {
  width: 64px;
  height: 64px;
  border-radius: 14px;
  object-fit: cover;
  border: 1px solid #e6e9f0;
  flex: none;
}
.avatar-tips {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
  align-items: flex-start;
}
</style>

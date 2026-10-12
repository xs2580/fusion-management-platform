<template>
  <div class="admin-page">
    <div class="page-card">
      <!-- ============ 头部 ============ -->
      <div class="flex-between mb-16">
        <h2 class="page-title">管理员管理 <span class="sub">sys_admin</span></h2>
        <el-button type="danger" @click="openDialog()">
          <el-icon class="mr6"><Plus /></el-icon>新增管理员
        </el-button>
      </div>

      <!-- ============ 搜索栏 ============ -->
      <div class="toolbar">
        <el-input
          v-model.trim="query.keyword"
          placeholder="请输入账号 / 昵称 / 邮箱 / 电话"
          clearable
          class="search-input"
          :prefix-icon="Search"
          @keyup.enter="handleSearch"
          @clear="handleSearch"
        />
        <el-select v-model="query.status" placeholder="全部状态" clearable class="status-select" @change="handleSearch">
          <el-option label="启用" :value="1" />
          <el-option label="禁用" :value="0" />
        </el-select>
        <el-button type="primary" @click="handleSearch">
          <el-icon class="mr6"><Search /></el-icon>搜索
        </el-button>
        <el-button @click="handleReset">重置</el-button>
      </div>

      <el-alert
        type="warning"
        :closable="false"
        show-icon
        class="mb-16"
        title="内置超级管理员（admin）不可删除，也不可被禁用，请谨慎操作其他管理员账号。"
      />

      <!-- ============ 表格 ============ -->
      <el-table v-loading="loading" :data="list" border stripe class="data-table">
        <el-table-column prop="id" label="ID" width="70" align="center" />
        <el-table-column label="头像" width="86" align="center">
          <template #default="{ row }">
            <el-avatar :size="40" :src="row.avatar" class="row-avatar">
              {{ (row.nickname || row.username || '?').charAt(0) }}
            </el-avatar>
          </template>
        </el-table-column>
        <el-table-column prop="username" label="账号" min-width="120" show-overflow-tooltip />
        <el-table-column prop="nickname" label="昵称" min-width="110" show-overflow-tooltip />
        <el-table-column label="角色" width="130" align="center">
          <template #default="{ row }">
            <el-tag :type="row.role === 'SUPER_ADMIN' ? 'danger' : 'primary'" effect="light" round size="small">
              {{ row.role === 'SUPER_ADMIN' ? '超级管理员' : '普通管理员' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="email" label="邮箱" min-width="180" show-overflow-tooltip>
          <template #default="{ row }">
            <span :class="{ 'text-muted': !row.email }">{{ row.email || '—' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="电话" min-width="130">
          <template #default="{ row }">
            <span :class="{ 'text-muted': !row.phone }">{{ row.phone || '—' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="90" align="center">
          <template #default="{ row }">
            <el-switch
              :model-value="row.status === 1"
              :disabled="row.role === 'SUPER_ADMIN'"
              inline-prompt
              active-text="启用"
              inactive-text="禁用"
              @change="(v) => toggleStatus(row, v)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="lastLogin" label="最后登录" min-width="160">
          <template #default="{ row }">
            <span class="text-sm text-muted">{{ row.lastLogin || '从未登录' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right" align="center">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="openDialog(row)">编辑</el-button>
            <el-button link type="warning" size="small" @click="handleResetPwd(row)">重置密码</el-button>
            <el-button
              link
              type="danger"
              size="small"
              :disabled="row.role === 'SUPER_ADMIN'"
              @click="handleDelete(row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
        <template #empty>
          <el-empty description="暂无管理员数据" />
        </template>
      </el-table>

      <el-pagination
        v-model:current-page="query.current"
        v-model:page-size="query.size"
        :page-sizes="[10, 20, 50, 100]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        background
        @size-change="fetchList"
        @current-change="fetchList"
      />
    </div>

    <!-- ============ 新增/编辑弹窗 ============ -->
    <el-dialog
      v-model="dialogVisible"
      :title="form.id ? '编辑管理员' : '新增管理员'"
      width="560px"
      :close-on-click-modal="false"
      @closed="handleDialogClosed"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="82px">
        <el-form-item label="账号" prop="username">
          <el-input v-model.trim="form.username" placeholder="请输入登录账号" />
        </el-form-item>

        <el-form-item v-if="!form.id" label="密码" prop="password">
          <el-input v-model.trim="form.password" type="password" show-password placeholder="默认 123456" />
        </el-form-item>

        <el-form-item label="昵称" prop="nickname">
          <el-input v-model.trim="form.nickname" placeholder="请输入昵称" />
        </el-form-item>

        <el-form-item label="角色" prop="role">
          <el-radio-group v-model="form.role" :disabled="form.role === 'SUPER_ADMIN'">
            <el-radio value="ADMIN">普通管理员</el-radio>
            <el-radio value="SUPER_ADMIN">超级管理员</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="邮箱" prop="email">
          <el-input v-model.trim="form.email" placeholder="请输入邮箱" />
        </el-form-item>

        <el-form-item label="电话" prop="phone">
          <el-input v-model.trim="form.phone" placeholder="请输入手机号" />
        </el-form-item>

        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :value="1">启用</el-radio>
            <el-radio :value="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="备注">
          <el-input v-model.trim="form.remark" type="textarea" :rows="2" placeholder="选填" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="danger" :loading="saving" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import { pageAdmin, createAdmin, updateAdmin, deleteAdmin, resetAdminPassword } from '@/api/admin'

const loading = ref(false)
const saving = ref(false)
const list = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const formRef = ref(null)

const query = reactive({ current: 1, size: 10, keyword: '', status: null })

const emptyForm = () => ({
  id: null,
  username: '',
  password: '',
  nickname: '',
  email: '',
  phone: '',
  role: 'ADMIN',
  status: 1,
  remark: ''
})

const form = reactive(emptyForm())

const rules = {
  username: [
    { required: true, message: '请输入账号', trigger: 'blur' },
    { min: 2, max: 50, message: '账号长度 2-50 位', trigger: 'blur' }
  ],
  password: [{ min: 6, max: 20, message: '密码长度 6-20 位', trigger: 'blur' }],
  email: [{ type: 'email', message: '邮箱格式不正确', trigger: 'blur' }],
  phone: [{ pattern: /^$|^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }]
}

async function fetchList() {
  loading.value = true
  try {
    const res = await pageAdmin({ ...query })
    const data = res.data || {}
    list.value = data.records || []
    total.value = Number(data.total || 0)
  } catch (e) {
    list.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  query.current = 1
  fetchList()
}

function handleReset() {
  query.keyword = ''
  query.status = null
  handleSearch()
}

function openDialog(row) {
  Object.assign(form, emptyForm(), row ? { ...row, password: '' } : {})
  dialogVisible.value = true
}

function handleDialogClosed() {
  formRef.value?.clearValidate()
}

async function handleSubmit() {
  try {
    await formRef.value.validate()
  } catch (e) {
    return
  }
  saving.value = true
  try {
    if (form.id) {
      await updateAdmin({ ...form })
      ElMessage.success('修改成功')
    } else {
      await createAdmin({ ...form, password: form.password || '123456' })
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    fetchList()
  } finally {
    saving.value = false
  }
}

async function toggleStatus(row, checked) {
  try {
    await updateAdmin({ id: row.id, status: checked ? 1 : 0 })
    row.status = checked ? 1 : 0
    ElMessage.success(checked ? '已启用' : '已禁用')
  } catch (e) {
    /* 保持原值 */
  }
}

async function handleResetPwd(row) {
  try {
    const { value } = await ElMessageBox.prompt(
      `请输入「${row.nickname || row.username}」的新密码，留空则重置为 123456`,
      '重置密码',
      {
        confirmButtonText: '确定重置',
        cancelButtonText: '取消',
        inputPlaceholder: '新密码（6-20 位）',
        inputValidator: (v) => !v || (v.length >= 6 && v.length <= 20) || '密码长度需 6-20 位'
      }
    )
    await resetAdminPassword(row.id, value || undefined)
    ElMessage.success('密码已重置')
  } catch (e) {
    /* 取消 */
  }
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(`确定删除管理员「${row.nickname || row.username}」吗？`, '删除确认', {
      confirmButtonText: '删除',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteAdmin(row.id)
    ElMessage.success('删除成功')
    fetchList()
  } catch (e) {
    /* 取消 */
  }
}

onMounted(fetchList)
</script>

<style scoped>
.sub {
  font-size: 12px;
  font-weight: 400;
  color: #94a3b8;
  margin-left: 6px;
  font-family: ui-monospace, Menlo, Consolas, monospace;
}
.toolbar {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 14px;
  flex-wrap: wrap;
}
.search-input { width: 280px; }
.status-select { width: 130px; }
.data-table {
  border-radius: 10px;
  overflow: hidden;
}
.row-avatar {
  border: 1px solid #eef1f7;
  background: #fdecee;
  color: #b81c2b;
  font-size: 13px;
}
.mr6 { margin-right: 6px; }

@media (max-width: 760px) {
  .search-input,
  .status-select { width: 100%; }
}
</style>

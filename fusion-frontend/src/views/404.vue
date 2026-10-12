<template>
  <div class="nf-page">
    <div class="nf-box">
      <h1>404</h1>
      <h2>抱歉，页面走丢了</h2>
      <p>您访问的页面不存在，或已被移动到别处。</p>
      <div class="nf-actions">
        <el-button type="primary" size="large" @click="goBack">返回上一页</el-button>
        <el-button size="large" @click="goHome">回到首页</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { getUser } from '@/utils/auth'

const router = useRouter()

function goBack() {
  router.back()
}

function goHome() {
  const user = getUser() || {}
  router.replace(user.loginType === 'admin' ? '/back/home' : '/front/home')
}
</script>

<style scoped>
.nf-page {
  min-height: 100vh;
  display: grid;
  place-items: center;
  background: linear-gradient(135deg, #eef2ff, #f7f9fc 50%, #fdf2f8);
  padding: 24px;
}
.nf-box {
  text-align: center;
}
.nf-box h1 {
  margin: 0;
  font-size: 110px;
  font-weight: 800;
  line-height: 1;
  background: linear-gradient(135deg, #2f54eb, #7048e8);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}
.nf-box h2 {
  margin: 12px 0 8px;
  font-size: 22px;
  color: var(--fusion-text-1);
}
.nf-box p {
  margin: 0 0 26px;
  color: var(--fusion-text-3);
  font-size: 14px;
}
.nf-actions {
  display: flex;
  gap: 12px;
  justify-content: center;
}
</style>

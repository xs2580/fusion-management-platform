<template>
  <div class="workspace">
    <div class="page-card">
      <div class="flex-between mb-16">
        <h2 class="page-title">我的工作台</h2>
        <el-button type="primary" plain @click="reload">
          <el-icon class="mr6"><Refresh /></el-icon>刷新
        </el-button>
      </div>

      <el-empty v-if="!list.length" description="暂无工作项，去首页看看平台能力吧" />

      <div v-else class="ws-grid">
        <div v-for="item in list" :key="item.name" class="ws-card">
          <div class="ws-head">
            <div class="ws-icon" :style="{ background: item.bg, color: item.color }">
              <el-icon :size="18"><component :is="item.icon" /></el-icon>
            </div>
            <el-tag :type="item.tagType" size="small" effect="light" round>{{ item.tag }}</el-tag>
          </div>
          <b>{{ item.name }}</b>
          <span>{{ item.desc }}</span>
          <el-progress :percentage="item.progress" :stroke-width="6" :color="item.color" :show-text="false" />
          <div class="ws-foot">
            <em>{{ item.progress }}%</em>
            <el-button link type="primary" size="small">进入</el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const list = ref([
  { name: '数据查询中心', desc: '按条件检索平台内业务数据', icon: 'Search', color: '#2f54eb', bg: '#eef2ff', tag: '进行中', tagType: 'primary', progress: 72 },
  { name: '可视化看板', desc: '关键指标图表化展示', icon: 'DataAnalysis', color: '#7048e8', bg: '#f3edff', tag: '进行中', tagType: 'primary', progress: 45 },
  { name: '文件中心', desc: '统一上传、下载与归档', icon: 'Files', color: '#16a34a', bg: '#e8f8ee', tag: '已完成', tagType: 'success', progress: 100 },
  { name: '消息通知', desc: '重要变动实时触达', icon: 'Bell', color: '#f59e0b', bg: '#fff7e6', tag: '待开始', tagType: 'warning', progress: 0 },
  { name: '趋势分析', desc: '按周期洞察业务走向', icon: 'TrendCharts', color: '#0ea5e9', bg: '#e6f6fe', tag: '进行中', tagType: 'primary', progress: 30 },
  { name: '个人设置', desc: '资料与安全策略维护', icon: 'Setting', color: '#e63946', bg: '#fdecee', tag: '待开始', tagType: 'warning', progress: 0 }
])

function reload() {
  // 真实项目在此重新拉取接口
  list.value = [...list.value]
}
</script>

<style scoped>
.ws-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}
.ws-card {
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 18px;
  border-radius: 14px;
  background: #fafbfe;
  border: 1px solid #eff2f8;
  transition: all 0.22s ease;
}
.ws-card:hover {
  background: #fff;
  border-color: #dbe3f7;
  transform: translateY(-3px);
  box-shadow: 0 14px 30px -18px rgba(30, 41, 90, 0.45);
}
.ws-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.ws-icon {
  width: 38px;
  height: 38px;
  border-radius: 11px;
  display: grid;
  place-items: center;
}
.ws-card b {
  font-size: 14.5px;
  color: #0f172a;
}
.ws-card > span {
  font-size: 12.5px;
  color: #94a3b8;
  min-height: 34px;
}
.ws-foot {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.ws-foot em {
  font-style: normal;
  font-size: 12px;
  color: #94a3b8;
}
.mr6 { margin-right: 6px; }

@media (max-width: 1080px) {
  .ws-grid { grid-template-columns: repeat(2, 1fr); }
}
@media (max-width: 680px) {
  .ws-grid { grid-template-columns: 1fr; }
}
</style>

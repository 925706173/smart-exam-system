<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <!-- 欢迎卡片 -->
      <el-col :span="16">
        <el-card>
          <template #header>
            <span>个人中心</span>
          </template>
          <div class="welcome">
            <h3>欢迎回来，{{ userStore.realName }}</h3>
            <p>角色：学生</p>
          </div>
        </el-card>
      </el-col>

      <!-- 快捷入口 -->
      <el-col :span="8">
        <el-card>
          <template #header>
            <span>快捷入口</span>
          </template>
          <div class="quick-links">
            <el-button type="primary" @click="$router.push('/student/exam-hall')">
              进入考试大厅
            </el-button>
            <el-button @click="$router.push('/student/history')">
              查看历史成绩
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 最近考试 -->
    <el-card style="margin-top: 20px">
      <template #header>
        <span>最近考试</span>
      </template>
      <el-table :data="recentExams" v-loading="loading">
        <el-table-column prop="paperTitle" label="试卷名称" />
        <el-table-column prop="totalScore" label="得分" width="100" />
        <el-table-column prop="submitTime" label="提交时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.submitTime) }}
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/store/user'
import { getStudentHistory } from '@/api/stats'
import { formatDateTime } from '@/utils/format'

const userStore = useUserStore()
const loading = ref(false)
const recentExams = ref([])

onMounted(async () => {
  loading.value = true
  try {
    const res = await getStudentHistory()
    recentExams.value = res.data.slice(0, 5)
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.welcome h3 {
  font-size: 20px;
  margin-bottom: 8px;
}

.quick-links {
  display: flex;
  flex-direction: column;
  gap: 12px;
}
</style>

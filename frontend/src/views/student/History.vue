<template>
  <div class="history">
    <el-card>
      <template #header>
        <span>历史成绩</span>
      </template>

      <el-table :data="historyList" v-loading="loading" stripe>
        <el-table-column prop="paperTitle" label="试卷名称" min-width="200" />
        <el-table-column prop="totalScore" label="得分" width="100">
          <template #default="{ row }">
            <span :class="{ 'fail': row.totalScore < 60 }">{{ row.totalScore }}</span>
          </template>
        </el-table-column>
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
import { getStudentHistory } from '@/api/stats'
import { formatDateTime } from '@/utils/format'

const loading = ref(false)
const historyList = ref([])

onMounted(async () => {
  loading.value = true
  try {
    const res = await getStudentHistory()
    historyList.value = res.data
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.fail {
  color: #f56c6c;
  font-weight: bold;
}
</style>

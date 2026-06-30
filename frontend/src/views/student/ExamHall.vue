<template>
  <div class="exam-hall">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>考试大厅</span>
          <div class="filter">
            <el-select v-model="filterStatus" placeholder="考试状态" clearable style="width: 120px">
              <el-option label="未开始" value="NOT_STARTED" />
              <el-option label="进行中" value="IN_PROGRESS" />
              <el-option label="已结束" value="CLOSED" />
            </el-select>
          </div>
        </div>
      </template>

      <el-table :data="examList" v-loading="loading" stripe>
        <el-table-column prop="title" label="试卷名称" min-width="200" />
        <el-table-column prop="subjectName" label="科目" width="120" />
        <el-table-column prop="totalScore" label="总分" width="80" />
        <el-table-column prop="duration" label="时长(分钟)" width="100" />
        <el-table-column label="考试时间" width="300">
          <template #default="{ row }">
            {{ formatDateTime(row.startTime) }} ~ {{ formatDateTime(row.endTime) }}
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ getStatusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button
              v-if="row.status === 'PUBLISHED' && !isExpired(row.endTime)"
              type="primary"
              size="small"
              @click="handleStart(row)"
            >
              开始考试
            </el-button>
            <el-button
              v-else-if="row.status === 'PUBLISHED' && isExpired(row.endTime)"
              size="small"
              disabled
            >
              已过期
            </el-button>
            <el-button
              v-else-if="row.status === 'CLOSED'"
              size="small"
              disabled
            >
              已结束
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :total="total"
        layout="total, prev, pager, next"
        style="margin-top: 20px; justify-content: flex-end"
        @current-change="loadData"
      />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getPaperPage } from '@/api/paper'
import { startExam } from '@/api/exam'
import { useExamStore } from '@/store/exam'
import { formatDateTime } from '@/utils/format'
import { debounce } from 'lodash-es'

const router = useRouter()
const examStore = useExamStore()
const loading = ref(false)
const examList = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const filterStatus = ref('')

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    const res = await getPaperPage({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      status: filterStatus.value,
    })
    examList.value = res.data.records
    total.value = res.data.total
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

// 防抖筛选
const debouncedLoad = debounce(loadData, 300)
watch(filterStatus, debouncedLoad)

onMounted(loadData)

// 开始考试
const handleStart = async (row) => {
  await ElMessageBox.confirm(
    `确定要开始考试「${row.title}」吗？考试时长${row.duration}分钟，开始后不可暂停。`,
    '提示',
    { type: 'warning' }
  )

  try {
    const res = await startExam(row.id)
    const data = res.data
    // 将考试数据存入store
    examStore.recordId = data.recordId
    examStore.paperTitle = data.paperTitle
    examStore.duration = data.duration
    examStore.totalScore = data.totalScore
    examStore.startTime = data.startTime
    examStore.endTime = data.endTime
    examStore.questionCount = data.questionCount
    // 计算剩余时间
    const end = new Date(data.endTime).getTime()
    const now = Date.now()
    examStore.remaining = Math.max(0, Math.floor((end - now) / 1000))
    ElMessage.success('考试开始')
    router.push(`/student/exam-room/${data.recordId}`)
  } catch (e) {
    console.error('开始考试失败:', e, 'paperId:', row.id)
  }
}

// 状态样式
const getStatusType = (status) => {
  const map = {
    PUBLISHED: 'success',
    CLOSED: 'info',
  }
  return map[status] || 'warning'
}

const getStatusLabel = (status) => {
  const map = {
    PUBLISHED: '进行中',
    CLOSED: '已结束',
    DRAFT: '未发布',
  }
  return map[status] || status
}

// 检查是否过期
const isExpired = (endTime) => {
  if (!endTime) return false
  return new Date(endTime).getTime() < Date.now()
}
</script>

<style scoped>
.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
</style>

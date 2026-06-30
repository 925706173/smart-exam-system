<template>
  <div class="grading-center">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>阅卷中心</span>
        </div>
      </template>

      <!-- 待阅卷试卷列表 -->
      <el-table :data="paperList" v-loading="loading" stripe>
        <el-table-column prop="paperTitle" label="试卷名称" min-width="200" />
        <el-table-column prop="subjectName" label="科目" width="120" />
        <el-table-column prop="submitCount" label="提交人数" width="100" />
        <el-table-column prop="pendingCount" label="待阅卷" width="100">
          <template #default="{ row }">
            <el-tag type="warning">{{ row.pendingCount }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="gradedCount" label="已阅卷" width="100">
          <template #default="{ row }">
            <el-tag type="success">{{ row.gradedCount }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="primary" @click="handleOpenGrading(row)">批改答卷</el-button>
            <el-button size="small" type="warning" @click="handleAiGradeSingle(row)">
              AI阅卷
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 批改答卷弹窗 -->
    <el-dialog v-model="gradingDialogVisible" title="批改答卷" width="1200px" top="3vh" destroy-on-close>
      <div class="grading-layout">
        <!-- 左侧：学生列表 -->
        <div class="student-list">
          <div class="student-list-header">学生列表</div>
          <div
            v-for="stu in studentList"
            :key="stu.recordId"
            class="student-item"
            :class="{ active: selectedRecordId === stu.recordId }"
            @click="handleSelectStudent(stu)"
          >
            <div class="student-name">{{ stu.studentName }}</div>
            <div class="student-info">
              <el-tag v-if="stu.status === 'GRADED'" type="success" size="small">已阅卷</el-tag>
              <el-tag v-else type="warning" size="small">待阅卷</el-tag>
              <span class="student-score">{{ stu.totalScore ?? '--' }}分</span>
            </div>
          </div>
          <el-empty v-if="studentList.length === 0" description="暂无提交" :image-size="60" />
        </div>

        <!-- 右侧：试卷内容 -->
        <div class="paper-content" v-loading="detailLoading">
          <template v-if="recordDetail">
            <div class="paper-header">
              <h3>{{ recordDetail.paperTitle }}</h3>
              <div class="paper-meta">
                <span>学生：{{ recordDetail.studentName }}</span>
                <span>得分：{{ recordDetail.studentScore ?? '--' }} / {{ recordDetail.totalScore }}</span>
                <el-tag :type="recordDetail.status === 'GRADED' ? 'success' : 'warning'" size="small">
                  {{ recordDetail.status === 'GRADED' ? '已阅卷' : '待阅卷' }}
                </el-tag>
              </div>
            </div>

            <!-- 题目列表 -->
            <div class="question-list">
              <div
                v-for="(q, index) in recordDetail.questions"
                :key="q.questionId"
                class="question-item"
              >
                <div class="question-header">
                  <span class="question-index">{{ index + 1 }}.</span>
                  <el-tag size="small" :type="getTypeTag(q.type)">{{ getTypeLabel(q.type) }}</el-tag>
                  <span class="question-score">（{{ q.fullScore }}分）</span>
                  <el-tag v-if="q.gradingStatus === 'TEACHER_DONE'" type="success" size="small" style="margin-left: 8px">已批改</el-tag>
                  <el-tag v-else-if="q.gradingStatus === 'AI_DONE'" type="info" size="small" style="margin-left: 8px">AI已评</el-tag>
                  <el-tag v-else-if="q.gradingStatus === 'AUTO_GRADED'" type="success" size="small" style="margin-left: 8px">自动判分</el-tag>
                </div>

                <div class="question-title">{{ q.title }}</div>

                <!-- 选项（选择题） -->
                <div v-if="q.options && q.options.length > 0" class="question-options">
                  <div
                    v-for="opt in q.options"
                    :key="opt.id"
                    class="option-item"
                    :class="{ 'correct-option': opt.isCorrect === 1 }"
                  >
                    <span class="option-label">{{ opt.optionLabel }}.</span>
                    <span class="option-content">{{ opt.optionContent }}</span>
                    <el-tag v-if="opt.isCorrect === 1" type="success" size="small">正确答案</el-tag>
                  </div>
                </div>

                <!-- 学生答案 -->
                <div class="answer-section">
                  <div class="answer-label">学生答案：</div>
                  <div class="answer-content" :class="{ 'answer-empty': !q.userAnswer }">
                    {{ q.userAnswer || '未作答' }}
                  </div>
                </div>

                <!-- 参考答案 -->
                <div v-if="q.referenceAnswer" class="answer-section">
                  <div class="answer-label">参考答案：</div>
                  <div class="answer-content reference">{{ q.referenceAnswer }}</div>
                </div>

                <!-- 解析 -->
                <div v-if="q.explanation" class="answer-section">
                  <div class="answer-label">解析：</div>
                  <div class="answer-content explanation">{{ q.explanation }}</div>
                </div>

                <!-- 评分区域 -->
                <div class="grading-section">
                  <!-- 客观题：自动判分只读 -->
                  <template v-if="isObjective(q.type)">
                    <div class="auto-score">
                      <span>自动判分：</span>
                      <el-tag :type="q.isCorrect === 1 ? 'success' : 'danger'" size="small">
                        {{ q.finalScore ?? q.autoScore ?? 0 }}分
                      </el-tag>
                      <span v-if="q.isCorrect === 1" class="correct-text">正确</span>
                      <span v-else-if="q.isCorrect === 0" class="wrong-text">错误</span>
                    </div>
                  </template>

                  <!-- 主观题/填空题：可编辑 -->
                  <template v-else>
                    <div v-if="q.aiScore != null" class="ai-score-row">
                      <span>AI评分：</span>
                      <el-tag type="info" size="small">{{ q.aiScore }}分</el-tag>
                    </div>
                    <div v-if="q.aiComment" class="ai-comment-box">
                      <div class="ai-comment-label">AI评语：</div>
                      <div class="ai-comment-text">{{ q.aiComment }}</div>
                    </div>
                    <div class="manual-score-row">
                      <span class="score-label">人工评分：</span>
                      <el-input-number
                        v-model="gradeScores[q.answerId]"
                        :min="0"
                        :max="Number(q.fullScore)"
                        :precision="1"
                        :step="0.5"
                        size="small"
                        style="width: 140px"
                      />
                      <span class="score-max">/ {{ q.fullScore }}分</span>
                    </div>
                  </template>
                </div>
              </div>
            </div>

            <!-- 底部提交 -->
            <div class="grading-footer" v-if="!isObjectiveOnly">
              <el-button @click="gradingDialogVisible = false">取消</el-button>
              <el-button type="primary" @click="handleSubmitGrade" :loading="submitting">
                提交评分
              </el-button>
            </div>
          </template>
          <el-empty v-else description="请从左侧选择学生" :image-size="80" />
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getGradingPapers, getPaperStudents, getRecordDetail, batchGrade, triggerAiGrade } from '@/api/paper'
import { formatDateTime } from '@/utils/format'

const loading = ref(false)
const paperList = ref([])

// 批改弹窗
const gradingDialogVisible = ref(false)
const currentPaperId = ref(null)
const studentList = ref([])
const selectedRecordId = ref(null)
const recordDetail = ref(null)
const detailLoading = ref(false)
const gradeScores = reactive({})
const submitting = ref(false)

// 是否全部是客观题
const isObjectiveOnly = computed(() => {
  if (!recordDetail.value) return true
  return recordDetail.value.questions.every(q => isObjective(q.type))
})

// 加载待阅卷试卷
onMounted(async () => {
  loading.value = true
  try {
    const res = await getGradingPapers({ pageNum: 1, pageSize: 50 })
    paperList.value = res.data.records
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
})

// 打开批改弹窗
const handleOpenGrading = async (row) => {
  currentPaperId.value = row.paperId
  gradingDialogVisible.value = true
  selectedRecordId.value = null
  recordDetail.value = null

  // 加载学生列表
  try {
    const res = await getPaperStudents(row.paperId)
    studentList.value = res.data
    // 自动选中第一个学生
    if (studentList.value.length > 0) {
      handleSelectStudent(studentList.value[0])
    }
  } catch (e) {
    console.error(e)
  }
}

// 选择学生
const handleSelectStudent = async (stu) => {
  selectedRecordId.value = stu.recordId
  detailLoading.value = true
  recordDetail.value = null

  try {
    const res = await getRecordDetail(currentPaperId.value, stu.recordId)
    recordDetail.value = res.data

    // 初始化评分数据
    Object.keys(gradeScores).forEach(key => delete gradeScores[key])
    if (res.data.questions) {
      for (const q of res.data.questions) {
        if (q.answerId && !isObjective(q.type)) {
          gradeScores[q.answerId] = q.finalScore != null ? Number(q.finalScore) : (q.aiScore != null ? Number(q.aiScore) : 0)
        }
      }
    }
  } catch (e) {
    console.error(e)
  } finally {
    detailLoading.value = false
  }
}

// 提交评分
const handleSubmitGrade = async () => {
  if (!selectedRecordId.value || !recordDetail.value) return

  // 收集有变化的评分
  const items = []
  for (const q of recordDetail.value.questions) {
    if (q.answerId && !isObjective(q.type)) {
      const score = gradeScores[q.answerId]
      if (score != null) {
        items.push({ answerId: q.answerId, finalScore: score })
      }
    }
  }

  if (items.length === 0) {
    ElMessage.warning('没有需要提交的评分')
    return
  }

  submitting.value = true
  try {
    await batchGrade(selectedRecordId.value, { items })
    ElMessage.success('评分提交成功')

    // 刷新学生列表
    const stuRes = await getPaperStudents(currentPaperId.value)
    studentList.value = stuRes.data

    // 刷新当前学生详情
    const detailRes = await getRecordDetail(currentPaperId.value, selectedRecordId.value)
    recordDetail.value = detailRes.data

    // 刷新试卷列表
    const paperRes = await getGradingPapers({ pageNum: 1, pageSize: 50 })
    paperList.value = paperRes.data.records
  } catch (e) {
    console.error(e)
  } finally {
    submitting.value = false
  }
}

// AI阅卷
const handleAiGradeSingle = async (row) => {
  try {
    await triggerAiGrade(row.paperId)
    ElMessage.success('AI阅卷已触发，请稍后刷新查看结果')
    // 刷新列表
    const res = await getGradingPapers({ pageNum: 1, pageSize: 50 })
    paperList.value = res.data.records
  } catch (e) {
    console.error(e)
  }
}

// 工具函数
const isObjective = (type) => {
  return ['SINGLE', 'MULTI', 'JUDGE'].includes(type)
}

const getTypeLabel = (type) => {
  const map = { SINGLE: '单选', MULTI: '多选', JUDGE: '判断', FILL: '填空', SUBJECTIVE: '主观' }
  return map[type] || type
}

const getTypeTag = (type) => {
  const map = { SINGLE: '', MULTI: 'success', JUDGE: 'warning', FILL: 'info', SUBJECTIVE: 'danger' }
  return map[type] || ''
}
</script>

<style scoped>
.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

/* 批改弹窗布局 */
.grading-layout {
  display: flex;
  gap: 16px;
  height: 70vh;
}

/* 左侧学生列表 */
.student-list {
  width: 200px;
  flex-shrink: 0;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  overflow-y: auto;
}

.student-list-header {
  padding: 10px 12px;
  font-weight: 600;
  font-size: 14px;
  background: #f5f7fa;
  border-bottom: 1px solid #e4e7ed;
  position: sticky;
  top: 0;
  z-index: 1;
}

.student-item {
  padding: 10px 12px;
  cursor: pointer;
  border-bottom: 1px solid #f0f0f0;
  transition: background 0.2s;
}

.student-item:hover {
  background: #f5f7fa;
}

.student-item.active {
  background: #ecf5ff;
  border-left: 3px solid #409eff;
}

.student-name {
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 4px;
}

.student-info {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 12px;
  color: #909399;
}

.student-score {
  margin-left: auto;
}

/* 右侧试卷内容 */
.paper-content {
  flex: 1;
  overflow-y: auto;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 16px;
}

.paper-header {
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #ebeef5;
}

.paper-header h3 {
  margin: 0 0 8px 0;
  font-size: 18px;
}

.paper-meta {
  display: flex;
  align-items: center;
  gap: 16px;
  font-size: 13px;
  color: #606266;
}

/* 题目 */
.question-item {
  margin-bottom: 20px;
  padding: 16px;
  background: #fafafa;
  border-radius: 6px;
  border: 1px solid #ebeef5;
}

.question-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}

.question-index {
  font-weight: 600;
  font-size: 15px;
  color: #303133;
}

.question-score {
  font-size: 13px;
  color: #909399;
}

.question-title {
  font-size: 14px;
  line-height: 1.6;
  color: #303133;
  margin-bottom: 10px;
}

/* 选项 */
.question-options {
  margin-bottom: 10px;
  padding-left: 8px;
}

.option-item {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 4px 8px;
  margin-bottom: 4px;
  border-radius: 4px;
  font-size: 13px;
}

.option-item.correct-option {
  background: #f0f9eb;
  color: #67c23a;
}

.option-label {
  font-weight: 600;
  min-width: 20px;
}

/* 答案区域 */
.answer-section {
  margin-bottom: 8px;
}

.answer-label {
  font-size: 13px;
  color: #909399;
  margin-bottom: 4px;
}

.answer-content {
  font-size: 13px;
  padding: 8px 12px;
  background: #fff;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  line-height: 1.6;
  white-space: pre-wrap;
}

.answer-content.answer-empty {
  color: #c0c4cc;
  font-style: italic;
}

.answer-content.reference {
  background: #f0f9eb;
  border-color: #e1f3d8;
}

.answer-content.explanation {
  background: #fdf6ec;
  border-color: #faecd8;
  font-size: 12px;
}

/* 评分区域 */
.grading-section {
  margin-top: 12px;
  padding-top: 10px;
  border-top: 1px dashed #dcdfe6;
}

.auto-score {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
}

.correct-text {
  color: #67c23a;
  font-weight: 500;
}

.wrong-text {
  color: #f56c6c;
  font-weight: 500;
}

.ai-score-row {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  margin-bottom: 8px;
}

.ai-comment-box {
  background: #ecf5ff;
  border: 1px solid #d9ecff;
  border-radius: 4px;
  padding: 8px 12px;
  margin-bottom: 8px;
}

.ai-comment-label {
  font-size: 12px;
  color: #409eff;
  font-weight: 500;
  margin-bottom: 4px;
}

.ai-comment-text {
  font-size: 12px;
  color: #606266;
  line-height: 1.8;
  white-space: pre-wrap;
  word-break: break-word;
}

.manual-score-row {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
}

.score-label {
  font-weight: 500;
  min-width: 80px;
}

.score-max {
  color: #909399;
  font-size: 12px;
}

/* 底部按钮 */
.grading-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 20px;
  padding-top: 16px;
  border-top: 1px solid #ebeef5;
}
</style>

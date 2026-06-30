<template>
  <div class="exam-review">
    <el-card>
      <template #header>
        <span>试卷复查</span>
      </template>

      <!-- 已批改试卷列表 -->
      <el-table :data="reviewList" v-loading="loading" stripe>
        <el-table-column prop="paperTitle" label="试卷名称" min-width="200" />
        <el-table-column prop="subjectName" label="科目" width="120" />
        <el-table-column label="得分" width="150">
          <template #default="{ row }">
            <span :class="{ 'score-fail': row.studentScore < 60 }">
              {{ row.studentScore ?? '--' }} / {{ row.totalScore }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="submitTime" label="提交时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.submitTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="primary" @click="handleViewDetail(row)">查看详情</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-empty v-if="!loading && reviewList.length === 0" description="暂无已批改的试卷" />
    </el-card>

    <!-- 试卷复查弹窗 -->
    <el-dialog v-model="dialogVisible" title="试卷复查" width="1200px" top="3vh">
      <div v-if="recordDetail" class="review-layout">
        <!-- 左侧：题目导航 -->
        <div class="question-nav">
          <div class="nav-header">题目导航</div>
          <div class="nav-grid">
            <div
              v-for="(q, index) in recordDetail.questions"
              :key="q.questionId"
              class="nav-item"
              :class="getNavClass(q)"
              @click="scrollToQuestion(index)"
            >
              {{ index + 1 }}
            </div>
          </div>
          <div class="nav-legend">
            <span class="legend-item"><span class="legend-dot correct"></span>正确</span>
            <span class="legend-item"><span class="legend-dot wrong"></span>错误</span>
            <span class="legend-item"><span class="legend-dot partial"></span>部分正确</span>
          </div>
        </div>

        <!-- 右侧：试卷内容 -->
        <div class="paper-content" v-loading="detailLoading">
          <template v-if="recordDetail">
            <div class="paper-header">
              <h3>{{ recordDetail.paperTitle }}</h3>
              <div class="paper-meta">
                <span>得分：<strong :class="{ 'score-fail': recordDetail.studentScore < 60 }">{{ recordDetail.studentScore ?? '--' }}</strong> / {{ recordDetail.totalScore }}</span>
              </div>
            </div>

            <!-- 题目列表 -->
            <div class="question-list">
              <div
                v-for="(q, index) in recordDetail.questions"
                :key="q.questionId"
                :id="'question-' + index"
                class="question-item"
              >
                <div class="question-header">
                  <span class="question-index">{{ index + 1 }}.</span>
                  <el-tag size="small" :type="getTypeTag(q.type)">{{ getTypeLabel(q.type) }}</el-tag>
                  <span class="question-score">（{{ q.fullScore }}分）</span>
                  <el-tag v-if="q.gradingStatus === 'TEACHER_DONE'" type="success" size="small" style="margin-left: 8px">教师批改</el-tag>
                  <el-tag v-else-if="q.gradingStatus === 'AI_DONE'" type="info" size="small" style="margin-left: 8px">AI评分</el-tag>
                  <el-tag v-else-if="q.gradingStatus === 'AUTO_GRADED'" type="success" size="small" style="margin-left: 8px">自动判分</el-tag>
                </div>

                <div class="question-title">{{ q.title }}</div>

                <!-- 选项（选择题） -->
                <div v-if="q.options && q.options.length > 0" class="question-options">
                  <div
                    v-for="opt in q.options"
                    :key="opt.id"
                    class="option-item"
                    :class="{
                      'correct-option': opt.isCorrect === 1,
                      'student-chosen': isStudentChoice(q, opt)
                    }"
                  >
                    <span class="option-label">{{ opt.optionLabel }}.</span>
                    <span class="option-content">{{ opt.optionContent }}</span>
                    <el-tag v-if="opt.isCorrect === 1" type="success" size="small">正确答案</el-tag>
                    <el-tag v-if="isStudentChoice(q, opt)" type="primary" size="small">我的选择</el-tag>
                  </div>
                </div>

                <!-- 学生答案 -->
                <div class="answer-section">
                  <div class="answer-label">我的答案：</div>
                  <div class="answer-content" :class="getAnswerClass(q)">
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

                <!-- 评分区域（只读） -->
                <div class="grading-section">
                  <!-- 客观题 -->
                  <template v-if="isObjective(q.type)">
                    <div class="auto-score">
                      <span>得分：</span>
                      <el-tag :type="q.isCorrect === 1 ? 'success' : 'danger'" size="small">
                        {{ q.finalScore ?? q.autoScore ?? 0 }}分
                      </el-tag>
                      <span v-if="q.isCorrect === 1" class="correct-text">正确</span>
                      <span v-else-if="q.isCorrect === 0" class="wrong-text">错误</span>
                    </div>
                  </template>

                  <!-- 主观题/填空题 -->
                  <template v-else>
                    <div v-if="q.aiScore != null" class="ai-score-row">
                      <span>AI评分：</span>
                      <el-tag type="info" size="small">{{ q.aiScore }}分</el-tag>
                    </div>
                    <div v-if="q.aiComment" class="ai-comment-box">
                      <div class="ai-comment-label">AI评语：</div>
                      <div class="ai-comment-text">{{ q.aiComment }}</div>
                    </div>
                    <div class="final-score-row">
                      <span class="score-label">最终得分：</span>
                      <el-tag :type="getScoreTagType(q)" size="small">{{ q.finalScore ?? 0 }}分</el-tag>
                      <span class="score-max">/ {{ q.fullScore }}分</span>
                    </div>
                  </template>
                </div>
              </div>
            </div>
          </template>
          <el-empty v-else description="加载中..." :image-size="80" />
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getReviewList, getReviewDetail } from '@/api/exam'
import { formatDateTime } from '@/utils/format'

const loading = ref(false)
const reviewList = ref([])

const dialogVisible = ref(false)
const recordDetail = ref(null)
const detailLoading = ref(false)

onMounted(async () => {
  loading.value = true
  try {
    const res = await getReviewList()
    reviewList.value = res.data
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
})

const handleViewDetail = async (row) => {
  dialogVisible.value = true
  detailLoading.value = true
  recordDetail.value = null
  try {
    const res = await getReviewDetail(row.recordId)
    recordDetail.value = res.data
  } catch (e) {
    console.error(e)
    ElMessage.error('加载试卷详情失败')
  } finally {
    detailLoading.value = false
  }
}

const scrollToQuestion = (index) => {
  const el = document.getElementById('question-' + index)
  if (el) {
    el.scrollIntoView({ behavior: 'smooth', block: 'start' })
  }
}

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

const isStudentChoice = (q, opt) => {
  if (!q.userAnswer) return false
  if (q.type === 'SINGLE' || q.type === 'JUDGE') {
    return q.userAnswer === opt.optionLabel
  }
  if (q.type === 'MULTI') {
    return q.userAnswer.includes(opt.optionLabel)
  }
  return false
}

const getNavClass = (q) => {
  if (isObjective(q.type)) {
    if (q.isCorrect === 1) return 'nav-correct'
    if (q.isCorrect === 0) return 'nav-wrong'
  } else {
    if (q.finalScore != null && q.fullScore && Number(q.finalScore) >= Number(q.fullScore)) return 'nav-correct'
    if (q.finalScore != null && Number(q.finalScore) > 0) return 'nav-partial'
    if (q.finalScore != null && Number(q.finalScore) === 0) return 'nav-wrong'
  }
  return ''
}

const getAnswerClass = (q) => {
  if (!q.userAnswer) return 'answer-empty'
  if (isObjective(q.type)) {
    return q.isCorrect === 1 ? 'answer-correct' : 'answer-wrong'
  }
  return ''
}

const getScoreTagType = (q) => {
  if (q.finalScore == null || !q.fullScore) return 'info'
  const ratio = Number(q.finalScore) / Number(q.fullScore)
  if (ratio >= 0.8) return 'success'
  if (ratio >= 0.6) return 'warning'
  return 'danger'
}
</script>

<style scoped>
.score-fail {
  color: #f56c6c;
  font-weight: 600;
}

/* 弹窗布局 */
.review-layout {
  display: flex;
  gap: 16px;
  height: 70vh;
}

/* 左侧题目导航 */
.question-nav {
  width: 160px;
  flex-shrink: 0;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  overflow-y: auto;
}

.nav-header {
  padding: 10px 12px;
  font-weight: 600;
  font-size: 14px;
  background: #f5f7fa;
  border-bottom: 1px solid #e4e7ed;
  position: sticky;
  top: 0;
  z-index: 1;
}

.nav-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 6px;
  padding: 10px;
}

.nav-item {
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.2s;
}

.nav-item:hover {
  border-color: #409eff;
  color: #409eff;
}

.nav-item.nav-correct {
  background: #f0f9eb;
  border-color: #67c23a;
  color: #67c23a;
}

.nav-item.nav-wrong {
  background: #fef0f0;
  border-color: #f56c6c;
  color: #f56c6c;
}

.nav-item.nav-partial {
  background: #fdf6ec;
  border-color: #e6a23c;
  color: #e6a23c;
}

.nav-legend {
  padding: 8px 10px;
  border-top: 1px solid #e4e7ed;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 11px;
  color: #909399;
}

.legend-dot {
  width: 10px;
  height: 10px;
  border-radius: 2px;
}

.legend-dot.correct {
  background: #67c23a;
}

.legend-dot.wrong {
  background: #f56c6c;
}

.legend-dot.partial {
  background: #e6a23c;
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
  font-size: 14px;
  color: #606266;
}

.paper-meta strong {
  font-size: 20px;
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

.option-item.student-chosen {
  border: 1px solid #409eff;
  background: #ecf5ff;
}

.option-item.student-chosen.correct-option {
  background: #f0f9eb;
  border-color: #67c23a;
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

.answer-content.answer-correct {
  border-color: #67c23a;
  background: #f0f9eb;
}

.answer-content.answer-wrong {
  border-color: #f56c6c;
  background: #fef0f0;
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

.final-score-row {
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
</style>
